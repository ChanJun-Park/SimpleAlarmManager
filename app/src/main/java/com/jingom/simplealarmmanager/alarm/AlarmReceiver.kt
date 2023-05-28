package com.jingom.simplealarmmanager.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver: BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		context ?: return
		intent ?: return


		val alarmInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent.getParcelableExtra(AlarmInfo.KEY, AlarmInfo::class.java)
		} else {
			intent.getParcelableExtra(AlarmInfo.KEY) as? AlarmInfo
		} ?: return

		AlarmNotifyWorker.schedule(alarmInfo.id, context)
	}
}