package com.ph.bittelasia.meshtv.tv.glasshoteldemo.core

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


abstract class BaseActivity<V: ViewDataBinding> : AppCompatActivity() {
    private var _binding : V?= null
    val binding : V get() = _binding!!
    object Debug {
        fun log(tag: Any, content: String) {
            val name = tag::class.java.simpleName
            Log.i("MESH-$name", content)
        }
        fun errorLog(tag: Any, content: String) {
            val name = tag::class.java.simpleName
            Log.e("MESH-$name", content)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, getLayout())
        setFragment()
        setContent()
    }
    fun attachFragment(f: Fragment, containerID: Int) {
        try {
            if (findViewById<View>(containerID) != null)
                if (supportFragmentManager.findFragmentById(containerID) == null)
                    supportFragmentManager.beginTransaction().replace(
                        containerID,
                        f
                    ).commitAllowingStateLoss()
        } catch (e: Exception) {
            Debug.errorLog(this, Arrays.toString(e.stackTrace))
        }
    }
    fun removeFragment(f: Fragment, containerID: Int){
        try {
            if (findViewById<View>(containerID) != null)
                supportFragmentManager.beginTransaction().remove(f).commitAllowingStateLoss()
        } catch (e: Exception) {
            Debug.errorLog(this, Arrays.toString(e.stackTrace))
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Debug.log(this, "@onDestroy")
    }
    abstract fun getLayout(): Int
    open fun setContent(){}
    open fun setFragment(){}
}