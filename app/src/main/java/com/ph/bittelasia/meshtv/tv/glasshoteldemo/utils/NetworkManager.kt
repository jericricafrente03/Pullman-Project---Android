package com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*


object NetworkManager {

    fun Context.checkInternet(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            val hasInternet = async(Dispatchers.Default) {
                return@async checkAddress(internetAddress())
            }
            callback.invoke(hasInternet.await())
        }
    }

    fun Context.checkInternetConnection(callback: suspend (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val hasInternet = async(Dispatchers.Default) {
                var online = false
                val t: Long = Calendar.getInstance().timeInMillis
                val runtime = Runtime.getRuntime()
                try {
                    val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
                    val exitValue = ipProcess.waitFor()
                    online = exitValue == 0
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val t2: Long = Calendar.getInstance().timeInMillis
                    Log.i("NetWork check Time", (t2 - t).toString() + "")
                }
                return@async online
            }
            callback.invoke(hasInternet.await())
        }
    }

    fun Context.disableLAN() {
        CoroutineScope(Dispatchers.Default).launch {
            launch {
                ADB.exec("ifconfig eth0 down")
            }

        }
    }

    fun Context.enableLAN() {
        CoroutineScope(Dispatchers.Default).launch {
            launch {
                ADB.exec("ifconfig eth0 up")
            }
        }
    }

    fun Context.internetAddress(): String {
        return "http://www.google.com"
    }

    fun Context.serverAddress(): String {
        return "http://192.168.200.3:8080"
    }

    fun Context.enableWifi(callback: suspend (WifiInfo?) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            val info = async(Dispatchers.Default) {
                var counter = 0
                val wifiManager =
                    this@enableWifi.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                if (!wifiManager.isWifiEnabled) {
                    wifiManager.isWifiEnabled = true
                    while (!wifiManager.isWifiEnabled) {
                        counter += 1
                        if (counter > 100) {
                            cancel()
                        }
                        delay(500)
                    }
                }
                return@async wifiManager.connectionInfo
            }
            withContext(Dispatchers.Main) {
                callback.invoke(info.await())
            }
        }
    }


    fun Context.disableWifi(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            val con = async {
                ADB.exec("ifconfig wlan down")
                val wifiManager =
                    this@disableWifi.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                return@async wifiManager.isWifiEnabled
            }
            callback.invoke(con.await())
        }
    }

    fun Context.viewWifiInfo() {
        val wifiManager =
            this@viewWifiInfo.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        Log.i("meme", "the wifi $wifiManager")
    }

    suspend fun Context.checkAddress(address: String?): Boolean {
        val con = CoroutineScope(Dispatchers.Default).async {
            var online = false
            val t: Long = Calendar.getInstance().timeInMillis
            val runtime = Runtime.getRuntime()
            try {
                val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
                val exitValue = ipProcess.waitFor()
                online = exitValue == 0
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                val t2: Long = Calendar.getInstance().timeInMillis
                Log.i("NetWork check Time", (t2 - t).toString() + "")
            }
            return@async online
        }
        return con.await()
    }


    fun Context.connection(callback: (Int) -> Unit) {
        var result = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return
            if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                callback.invoke(NetworkCapabilities.TRANSPORT_WIFI)
            if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                callback.invoke(NetworkCapabilities.TRANSPORT_CELLULAR)
            if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                callback.invoke(NetworkCapabilities.TRANSPORT_ETHERNET)

        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> callback.invoke(NetworkCapabilities.TRANSPORT_WIFI)
                        ConnectivityManager.TYPE_MOBILE -> callback.invoke(NetworkCapabilities.TRANSPORT_CELLULAR)
                        ConnectivityManager.TYPE_ETHERNET -> callback.invoke(NetworkCapabilities.TRANSPORT_ETHERNET)
                    }

                }
            }
        }
    }

    fun Context.openChrome(url: String, onError: (() -> Unit)? = null) {
        openBrowser("com.android.chrome", url) {
            openBrowser("com.android.beta", url) {
                openBrowser("com.android.dev", url) {
                    openBrowser("com.android.canary", url) {
                        onError?.invoke() ?: openBrowser(null, url)
                    }
                }
            }
        }
    }

    fun Context.openBrowser(packageName: String?, url: String, onError: (() -> Unit)? = null) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                setPackage(packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (e: ActivityNotFoundException) {
            onError?.invoke()
        }
    }
}