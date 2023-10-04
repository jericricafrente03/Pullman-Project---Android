package com.ph.bittelasia.meshtv.tv.glasshoteldemo.main

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.core.BaseFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.FragmentMainPageBinding
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.internet.ConnectivityObserver
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.internet.NetworkConnectivityObserver
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.ADB
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.NetworkManager.checkInternetConnection
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.NetworkManager.disableWifi
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.NetworkManager.enableWifi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainPage : BaseFragment<FragmentMainPageBinding>() {
    private lateinit var connectivityObserver: ConnectivityObserver
    override fun addContents() {
        super.addContents()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                connectivityObserver = NetworkConnectivityObserver(requireContext())
                val typeface = ResourcesCompat.getFont(requireContext(), R.font.itc)

                binding.apply {
                    tvCountry.typeface = typeface
                    ivBg.load(R.drawable.newbg)
                    ivTv.setOnFocusChangeListener { _, b ->
                        if (b){
                            ivTv.load(R.drawable.b1)
                        }else{
                            ivTv.load(R.drawable.a1)
                        }
                    }
                    ivNetflix.setOnFocusChangeListener { _, b ->
                        if (b){
                            ivNetflix.load(R.drawable.b2)
                        }else{
                            ivNetflix.load(R.drawable.a2)
                        }
                    }
                    ivMessage.setOnFocusChangeListener { _, b ->
                        if (b){
                            ivMessage.load(R.drawable.b4)
                        }else{
                            ivMessage.load(R.drawable.a4)
                        }
                    }
                    ivHotelInfo.setOnFocusChangeListener { _, b ->
                        if (b){
                            ivHotelInfo.load(R.drawable.b3)
                        }else{
                            ivHotelInfo.load(R.drawable.a3)
                        }
                    }
                    ivTv.setOnClickListener {
                        Navigation.findNavController(it).navigate(R.id.action_mainPage_to_tv)
                    }
                    ivHotelInfo.setOnClickListener {
                        Navigation.findNavController(it).navigate(R.id.action_mainPage_to_amenities)
                    }
                    ivMessage.setOnClickListener {
                        Navigation.findNavController(it).navigate(R.id.action_mainPage_to_messages)
                    }
                    ivNetflix.setOnClickListener {
                        ADB.exec( "monkey -p com.netflix.mediaclient -c android.intent.category.LAUNCHER 1")
                    }

                    launch {
                        connectivityObserver.observe().collectLatest {
                            Log.e("meme","Connection Status -> $it")
                            Snackbar.make(requireView(), "Connection Status -> $it",Snackbar.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
    }
    private fun appLaunch(@NonNull packageName: String?, needInternet: Boolean = false) {
        if (needInternet) {
            requireActivity().checkInternetConnection { internet ->
                if (internet) {
                    ADB.exec("monkey -p $packageName -c android.intent.category.LAUNCHER 1")
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        requireActivity().enableWifi { wifiInfo ->
                            Toast.makeText(requireContext(), "Connecting to ${wifiInfo?.ssid}", Toast.LENGTH_LONG).show()
                            var count = 0
                            while (count != 2) {
                                if (count != 1) {
                                    requireActivity().checkInternetConnection { internet ->
                                        Toast.makeText(requireContext(), "Launching ..", Toast.LENGTH_LONG).show()
                                        if (internet) {
                                            ADB.exec("monkey -p $packageName -c android.intent.category.LAUNCHER 1")
                                            count = 1
                                        } else {
                                            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    break
                                }
                            }
                        }
                    }
                }
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
               ADB.exec("monkey -p $packageName -c android.intent.category.LAUNCHER 1")
                requireActivity().disableWifi {
                    ADB.exec("su -c 'svc wifi disable'")
                }
            }
        }
    }

    override fun getLayout() = R.layout.fragment_main_page

}