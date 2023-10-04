package com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils

import androidx.lifecycle.*
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.repository.Repository


class GlassViewModel(repo: Repository) : ViewModel() {
    val mainUi = repo.getMainUi().asLiveData()
    val amenities = repo.getAmenities().asLiveData()
    val tvList = repo.getTvList().asLiveData()
    val bg = repo.getBg().asLiveData()
}
