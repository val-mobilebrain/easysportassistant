package com.easysport.assistantappnewmb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class ParlaySelection(
    val name: String,
    val decimal: Double,
    val displayOdds: String
)

class ParlayViewModel : ViewModel() {

    private val _selections = MutableLiveData<MutableList<ParlaySelection>>(mutableListOf())
    val selections: LiveData<MutableList<ParlaySelection>> = _selections

    fun addSelection(selection: ParlaySelection) {
        val list = _selections.value ?: mutableListOf()
        list.add(selection)
        _selections.value = list
    }

    fun removeSelection(index: Int) {
        val list = _selections.value ?: return
        if (index in list.indices) {
            list.removeAt(index)
            _selections.value = list
        }
    }

    fun getSelections(): List<ParlaySelection> = _selections.value ?: emptyList()
}
