package com.easysport.assistantappnewmb.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.easysport.assistantappnewmb.R
import com.easysport.assistantappnewmb.data.Bet
import com.easysport.assistantappnewmb.databinding.FragmentProfitTrackerBinding
import com.easysport.assistantappnewmb.viewmodel.TrackerViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class ProfitTrackerFragment : Fragment() {

    private var _binding: FragmentProfitTrackerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TrackerViewModel by viewModels()
    private lateinit var historyAdapter: BetHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfitTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupResultSpinner()
        setupDatePicker()
        setupRecyclerView()
        setupAddButton()
        observeBets()
        setTodayDate()
    }

    private fun setupResultSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.bet_results, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerBetResult.adapter = adapter
        }
    }

    private fun setupDatePicker() {
        binding.etBetDate.setOnClickListener { showDatePicker() }
        binding.etBetDate.isFocusable = false
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            binding.etBetDate.setText("%04d-%02d-%02d".format(year, month + 1, day))
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun setTodayDate() {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        binding.etBetDate.setText(today)
    }

    private fun setupRecyclerView() {
        historyAdapter = BetHistoryAdapter { bet -> viewModel.deleteBet(bet) }
        binding.rvBetHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBetHistory.adapter = historyAdapter
        binding.rvBetHistory.isNestedScrollingEnabled = false
    }

    private fun setupAddButton() {
        binding.btnAddBet.setOnClickListener { addBet() }
    }

    private fun addBet() {
        val date = binding.etBetDate.text.toString().trim()
        val name = binding.etBetName.text.toString().trim()
        val odds = binding.etBetOdds.text.toString().trim()
        val stakeText = binding.etBetStake.text.toString().trim()
        val stake = stakeText.toDoubleOrNull()

        if (date.isEmpty() || name.isEmpty() || odds.isEmpty() || stake == null || stake <= 0) {
            Toast.makeText(requireContext(), "Please fill in all fields with valid values", Toast.LENGTH_SHORT).show()
            return
        }

        val result = when (binding.spinnerBetResult.selectedItemPosition) {
            1 -> "win"
            2 -> "loss"
            else -> "pending"
        }

        val bet = Bet(
            id = System.currentTimeMillis(),
            date = date,
            name = name,
            odds = odds,
            stake = stake,
            result = result
        )
        viewModel.addBet(bet)

        binding.etBetName.text?.clear()
        binding.etBetOdds.text?.clear()
        binding.etBetStake.text?.clear()
        binding.spinnerBetResult.setSelection(0)
        setTodayDate()
    }

    private fun observeBets() {
        viewModel.bets.observe(viewLifecycleOwner) { bets ->
            historyAdapter.submitList(bets)
            updateAnalytics(bets)
            binding.tvNoBets.visibility = if (bets.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun updateAnalytics(bets: List<Bet>) {
        val totalBets = bets.size
        val wins = bets.count { it.result == "win" }
        val losses = bets.count { it.result == "loss" }
        val totalStake = bets.sumOf { it.stake }
        val netProfit = viewModel.calculateNetProfit(bets)
        val roi = if (totalStake > 0) (netProfit / totalStake) * 100 else 0.0

        binding.tvTotalBets.text = totalBets.toString()
        binding.tvWinsLosses.text = "$wins / $losses"
        binding.tvNetProfit.text = "$${"%.2f".format(netProfit)}"
        binding.tvRoi.text = "${"%.2f".format(roi)}%"

        val ctx = requireContext()
        val netColor = when {
            netProfit > 0 -> ContextCompat.getColor(ctx, R.color.win_color)
            netProfit < 0 -> ContextCompat.getColor(ctx, R.color.loss_color)
            else -> ContextCompat.getColor(ctx, R.color.white)
        }
        binding.tvNetProfit.setTextColor(netColor)

        val roiColor = when {
            roi > 0 -> ContextCompat.getColor(ctx, R.color.win_color)
            roi < 0 -> ContextCompat.getColor(ctx, R.color.loss_color)
            else -> ContextCompat.getColor(ctx, R.color.white)
        }
        binding.tvRoi.setTextColor(roiColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
