package com.elfiky.ibtikarandroidtask.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class OnDownloadCompleteReceiver(
    private val context: Context
) : LifecycleObserver {

    private var observer: (() -> Unit)? = null
    private val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            if (action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return
            observer?.invoke()
        }
    }

    fun observe(owner: LifecycleOwner, onDownloadCompleted: () -> Unit) {
        owner.lifecycle.addObserver(this)
        observer = onDownloadCompleted
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun start() {
        context.registerReceiver(broadCastReceiver, filter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun stop() {
        context.unregisterReceiver(broadCastReceiver)
    }
}