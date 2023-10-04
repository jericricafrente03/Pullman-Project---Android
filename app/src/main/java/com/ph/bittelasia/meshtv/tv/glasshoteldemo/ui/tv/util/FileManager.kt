package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.util

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import java.util.*

suspend fun String.toFile(context: Context, callback: (File) -> Unit)
{
    coroutineScope {
        try {
            var outputFile: File? = null
            val file = async(Dispatchers.IO) {
                try {
                    directory {
                        outputFile = File(
                            context.applicationContext.getExternalFilesDir(it!!),
                            substring(lastIndexOf('/') + 1)
                        )
                    }
                    Log.i("MESH-LIB-FileManager", "File Check : ${outputFile?.absolutePath}")
                    if(outputFile!=null) {
                        if (outputFile!!.exists()) {
                            return@async outputFile
                        }
                    }
                    if (URLUtil.isValidUrl(this@toFile)) {
                        val url = URL(this@toFile)
                        directory {
                            outputFile = File(
                                context.applicationContext.getExternalFilesDir(it!!),
                                url.file.substring(url.file.lastIndexOf('/') + 1)
                            )
                        }
                        if(outputFile!=null) {
                            if (outputFile!!.exists()) {
                                Log.i("MESH-LIB-FileManager", "File Retrieved ${outputFile!!.absolutePath}")
                                return@async outputFile
                            } else {
                                try {
                                    val c: HttpURLConnection =
                                        url.openConnection() as HttpURLConnection
                                    c.requestMethod = "GET"
                                    c.connect()

                                    if (c.responseCode != HttpURLConnection.HTTP_OK) {
                                        Log.i(
                                            "MESH-LIB-FileManager",
                                            "Server returned HTTP " + c.responseCode.toString() + " " + c.responseCode
                                        )
                                    }
                                    //Create New File if not present
                                    if (!outputFile!!.exists()) {
                                        outputFile!!.createNewFile()
                                        Log.i(
                                            "MESH-LIB-FileManager",
                                            "File Created ${outputFile!!.absolutePath}"
                                        )
                                    }
                                    if (outputFile != null) {
                                        val fos = FileOutputStream(outputFile)
                                        val `is`: InputStream = c.inputStream
                                        val buffer = ByteArray(1024)
                                        var len1 = 0
                                        while ((`is`.read(buffer).also { len1 = it }) != -1) {
                                            fos.write(buffer, 0, len1)
                                        }

                                        fos.close()
                                        `is`.close()
                                    }
                                }catch (e : Exception){
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                    else{
                        Log.e("MESH-LIB-FileManager", "@toFile: Not Valid URL -> ${this@toFile}")
                    }
                    Log.i("MESH-LIB-FileManager", "File Retrieved ${outputFile?.absolutePath}")
                    return@async outputFile
                } catch (e: Exception) {
                    e.printStackTrace()
                    outputFile = null
                    Log.e(
                        "MESH-LIB-FileManager",
                        "Download Error Exception " + Arrays.toString(e.stackTrace)
                    )
                }
            }
            if(file.await() is File) {
                callback.invoke(file.await() as File)
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}

fun String.getMimeType(): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(this)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}


fun String.directory(callback: (String?) -> Unit){
    getMimeType()?.apply {
        when(this){
            "audio/ogg" ,
            "audio/occ" -> callback.invoke("audio")
            "video/mpeg" ,
            "video/wav",
            "video/mp4" -> callback.invoke("video")
            "image/jpeg",
            "image/bmp",
            "image/gif",
            "image/jpg",
            "image/png" -> callback.invoke("image")
            "text/plain"-> callback.invoke("text")
        }
    }
}