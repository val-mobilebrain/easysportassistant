package com.easysport.assistantappnewmb.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.easysport.assistantappnewmb.OddsUtils
import com.easysport.assistantappnewmb.databinding.FragmentOddsConverterBinding

class OddsConverterFragment : Fragment() {

    private var _binding: FragmentOddsConverterBinding? = null
    private val binding get() = _binding!!
    private var isUpdating = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOddsConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etDecimal.addTextChangedListener(makeWatcher { convertFrom("decimal") })
        binding.etAmerican.addTextChangedListener(makeWatcher { convertFrom("american") })
        binding.etFractional.addTextChangedListener(makeWatcher { convertFrom("fractional") })
    }

    private fun makeWatcher(action: () -> Unit) = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (!isUpdating) action()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun convertFrom(source: String) {
        isUpdating = true
        try {
            val input = when (source) {
                "decimal" -> binding.etDecimal.text.toString()
                "american" -> binding.etAmerican.text.toString()
                "fractional" -> binding.etFractional.text.toString()
                else -> ""
            }

            if (input.trim().isEmpty()) {
                if (source != "decimal") binding.etDecimal.text?.clear()
                if (source != "american") binding.etAmerican.text?.clear()
                if (source != "fractional") binding.etFractional.text?.clear()
                binding.tvProbability.text = "-"
                return
            }

            val decimal = OddsUtils.parseToDecimal(input, source)
            if (decimal != null && decimal > 1.0) {
                if (source != "decimal") binding.etDecimal.setText("%.2f".format(decimal))
                if (source != "american") binding.etAmerican.setText(OddsUtils.decimalToAmerican(decimal))
                if (source != "fractional") binding.etFractional.setText(OddsUtils.decimalToFractional(decimal))
                binding.tvProbability.text = "${"%.2f".format(OddsUtils.impliedProbability(decimal))}%"
            } else {
                binding.tvProbability.text = "-"
            }
        } finally {
            isUpdating = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
