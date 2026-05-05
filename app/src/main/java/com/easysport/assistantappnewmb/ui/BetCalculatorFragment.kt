package com.easysport.assistantappnewmb.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.easysport.assistantappnewmb.OddsUtils
import com.easysport.assistantappnewmb.R
import com.easysport.assistantappnewmb.databinding.FragmentBetCalculatorBinding

class BetCalculatorFragment : Fragment() {

    private var _binding: FragmentBetCalculatorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBetCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        setupListeners()
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.odds_formats, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerOddsFormat.adapter = adapter
        }
        binding.spinnerOddsFormat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                calculateBet()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupListeners() {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { calculateBet() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        binding.etStake.addTextChangedListener(watcher)
        binding.etBetOdds.addTextChangedListener(watcher)

        binding.btnPlus1.setOnClickListener { adjustStake(1.0) }
        binding.btnPlus5.setOnClickListener { adjustStake(5.0) }
        binding.btnPlus10.setOnClickListener { adjustStake(10.0) }
    }

    private fun adjustStake(amount: Double) {
        val current = binding.etStake.text.toString().toDoubleOrNull() ?: 0.0
        binding.etStake.setText("%.2f".format(current + amount))
        calculateBet()
    }

    private fun calculateBet() {
        val stake = binding.etStake.text.toString().toDoubleOrNull() ?: 0.0
        val oddsInput = binding.etBetOdds.text.toString().trim()
        val format = when (binding.spinnerOddsFormat.selectedItemPosition) {
            0 -> "decimal"
            1 -> "american"
            2 -> "fractional"
            else -> "decimal"
        }

        if (oddsInput.isEmpty() || stake <= 0.0) {
            binding.tvPayout.text = "$0.00"
            binding.tvProfit.text = "$0.00"
            binding.tvImpliedProb.text = "-"
            return
        }

        val decimal = OddsUtils.parseToDecimal(oddsInput, format)
        if (decimal != null && decimal > 1.0) {
            val payout = stake * decimal
            val profit = payout - stake
            binding.tvPayout.text = "$${"%.2f".format(payout)}"
            binding.tvProfit.text = "$${"%.2f".format(profit)}"
            binding.tvImpliedProb.text = "${"%.2f".format(OddsUtils.impliedProbability(decimal))}%"
        } else {
            binding.tvPayout.text = "$0.00"
            binding.tvProfit.text = "$0.00"
            binding.tvImpliedProb.text = "-"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
