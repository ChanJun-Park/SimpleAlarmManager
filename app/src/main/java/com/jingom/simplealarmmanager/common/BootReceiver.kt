package com.jingom.simplealarmmanager.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jingom.simplealarmmanager.alarm.AppAlarmRecoverWorker

class BootReceiver: BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		if (intent.action == "android.intent.action.BOOT_COMPLETED") {
			AppAlarmRecoverWorker.schedule(context)
		}
	}
}