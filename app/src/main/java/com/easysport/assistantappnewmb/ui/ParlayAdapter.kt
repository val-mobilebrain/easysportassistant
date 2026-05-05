package com.easysport.assistantappnewmb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easysport.assistantappnewmb.databinding.ItemParlaySelectionBinding
import com.easysport.assistantappnewmb.viewmodel.ParlaySelection

class ParlayAdapter(
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<ParlayAdapter.ViewHolder>() {

    private var items: List<ParlaySelection> = emptyList()

    fun submitList(list: List<ParlaySelection>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParlaySelectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemParlaySelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ParlaySelection, index: Int) {
            binding.tvSelectionName.text = item.name
            binding.tvSelectionOdds.text = item.displayOdds
            binding.btnRemoveSelection.setOnClickListener { onRemove(index) }
        }
    }
}
