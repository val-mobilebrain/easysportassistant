package com.easysport.assistantappnewmb.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.easysport.assistantappnewmb.OddsUtils
import com.easysport.assistantappnewmb.R
import com.easysport.assistantappnewmb.databinding.FragmentParlayBuilderBinding
import com.easysport.assistantappnewmb.viewmodel.ParlaySelection
import com.easysport.assistantappnewmb.viewmodel.ParlayViewModel

class ParlayBuilderFragment : Fragment() {

    private var _binding: FragmentParlayBuilderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ParlayViewModel by activityViewModels()
    private lateinit var adapter: ParlayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParlayBuilderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        setupRecyclerView()
        setupListeners()
        observeSelections()
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.odds_formats, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerParlayFormat.adapter = adapter
        }
    }

    private fun setupRecyclerView() {
        adapter = ParlayAdapter { index -> viewModel.removeSelection(index) }
        binding.rvParlaySelections.layoutManager = LinearLayoutManager(requireContext())
        binding.rvParlaySelections.adapter = adapter
        binding.rvParlaySelections.isNestedScrollingEnabled = false
    }

    private fun setupListeners() {
        binding.btnAddSelection.setOnClickListener { addSelection() }
        binding.etParlayStake.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { updateParlayTotal() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeSelections() {
        viewModel.selections.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.toList())
            updateParlayTotal()
        }
    }

    private fun addSelection() {
        val name = binding.etParlayName.text.toString().trim()
        val oddsInput = binding.etParlayOdds.text.toString().trim()
        val format = when (binding.spinnerParlayFormat.selectedItemPosition) {
            0 -> "decimal"
            1 -> "american"
            2 -> "fractional"
            else -> "decimal"
        }

        if (name.isEmpty() || oddsInput.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter both name and odds", Toast.LENGTH_SHORT).show()
            return
        }

        val decimal = OddsUtils.parseToDecimal(oddsInput, format)
        if (decimal == null || decimal <= 1.0) {
            Toast.makeText(requireContext(), "Invalid odds", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.addSelection(ParlaySelection(name, decimal, oddsInput))
        binding.etParlayName.text?.clear()
        binding.etParlayOdds.text?.clear()
    }

    private fun updateParlayTotal() {
        val selections = viewModel.getSelections()
        if (selections.isEmpty()) {
            binding.layoutParlayTotal.visibility = View.GONE
            return
        }

        val stake = binding.etParlayStake.text.toString().toDoubleOrNull() ?: 0.0
        val combinedOdds = selections.fold(1.0) { acc, sel -> acc * sel.decimal }
        val payout = stake * combinedOdds
        val profit = payout - stake

        binding.tvCombinedOdds.text =
            "${"%.2f".format(combinedOdds)} / ${OddsUtils.decimalToAmerican(combinedOdds)}"
        binding.tvParlayPayout.text = "$${"%.2f".format(payout)}"
        binding.tvParlayProfit.text = "$${"%.2f".format(profit)}"
        binding.layoutParlayTotal.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
