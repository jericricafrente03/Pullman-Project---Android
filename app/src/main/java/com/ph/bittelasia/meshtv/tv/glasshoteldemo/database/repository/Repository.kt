package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.repository

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.data.ProjectData
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.db.GlassDB
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.util.networkBoundResource
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.ui.Tv
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.ParsingJson
import kotlinx.coroutines.delay

class Repository(
    private val data: ProjectData,
    private val db: GlassDB,
    private val context: Context
) {
    private val uiDao = db.mainUiDao()
    private val amenitiesDao = db.amenitiesDao()
    private val tvListDao = db.tvListDao()
    private val bgDao = db.bGDao()

    fun getMainUi() = networkBoundResource(
        query = {
            uiDao.getAllMainUi()
        },
        fetch = {
            data.getMainUi()
        },
        saveFetchResult = { ui ->
            db.withTransaction {
                uiDao.deleteMainUi()
                uiDao.insertMainUi(ui)
            }
        }
    )
    fun getAmenities() = networkBoundResource(
        query = {
            amenitiesDao.getAllAmenities()
        },
        fetch = {
            data.getAmenities()
        },
        saveFetchResult = { amenities ->
            db.withTransaction {
                amenitiesDao.deleteAllAmenities()
                amenitiesDao.insertAmenities(amenities)
            }
        }
    )
    fun getTvList() = networkBoundResource(
        query = {
            tvListDao.getAllTvList()
        },
        fetch = {
           ParsingJson.getTvList(context)
        },
        saveFetchResult = { tvList ->
            db.withTransaction {
                tvListDao.deleteTvList()
                tvListDao.insertTvList(tvList)
            }
        }
    )
    fun getBg() = networkBoundResource(
        query = {
            bgDao.getBg()
        },
        fetch = {
            data.backgroundMode()
        },
        saveFetchResult = { bg ->
            db.withTransaction {
                bgDao.deleteBg()
                bgDao.insertBg(bg)
            }
        }
    )
}