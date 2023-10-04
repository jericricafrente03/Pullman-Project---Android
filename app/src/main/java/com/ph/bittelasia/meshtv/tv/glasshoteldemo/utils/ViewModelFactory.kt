package com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.repository.Repository


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val dbHelper: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GlassViewModel::class.java)) {
            return GlassViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}