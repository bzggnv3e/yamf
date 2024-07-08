package io.github.kaii_lb.yamfsquared.manager.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.kaii_lb.yamfsquared.manager.services.YAMFManagerProxy

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            YAMFManagerProxy.launchSideBar()
        }
    }
}