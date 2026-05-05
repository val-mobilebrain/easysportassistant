package com.easysport.assistantappnewmb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.easysport.assistantappnewmb.OddsUtils
import com.easysport.assistantappnewmb.R
import com.easysport.assistantappnewmb.data.Bet
import com.easysport.assistantappnewmb.databinding.ItemBetHistoryBinding

class BetHistoryAdapter(
    private val onDelete: (Bet) -> Unit
) : RecyclerView.Adapter<BetHistoryAdapter.ViewHolder>() {

    private var items: List<Bet> = emptyList()

    fun submitList(list: List<Bet>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBetHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemBetHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bet: Bet) {
            binding.tvBetName.text = bet.name
            binding.tvBetDate.text = bet.date
            binding.tvBetOdds.text = bet.odds
            binding.tvBetStake.text = "$${String.format("%.2f", bet.stake)}"
            binding.tvBetStatus.text = bet.result.replaceFirstChar { it.uppercase() }

            val ctx = binding.root.context
            val (statusColor, stripeColor) = when (bet.result) {
                "win" -> Pair(R.color.win_color, R.color.win_color)
                "loss" -> Pair(R.color.loss_color, R.color.loss_color)
                else -> Pair(R.color.pending_color, R.color.pending_color)
            }

            binding.tvBetStatus.backgroundTintList =
                ContextCompat.getColorStateList(ctx, statusColor)
            binding.viewStatusStripe.setBackgroundColor(
                ContextCompat.getColor(ctx, stripeColor)
            )

            val profitText = when (bet.result) {
                "win" -> {
                    val decimal = OddsUtils.parseAutoToDecimal(bet.odds)
                    if (decimal != null && decimal > 1.0) {
                        "+$${String.format("%.2f", (bet.stake * decimal) - bet.stake)}"
                    } else "-"
                }
                "loss" -> "-$${String.format("%.2f", bet.stake)}"
                else -> "Pending"
            }
            binding.tvBetProfit.text = profitText

            val profitColor = when (bet.result) {
                "win" -> R.color.win_color
                "loss" -> R.color.loss_color
                else -> R.color.pending_color
            }
            binding.tvBetProfit.setTextColor(ContextCompat.getColor(ctx, profitColor))

            binding.btnDeleteBet.setOnClickListener { onDelete(bet) }
        }
    }
}
