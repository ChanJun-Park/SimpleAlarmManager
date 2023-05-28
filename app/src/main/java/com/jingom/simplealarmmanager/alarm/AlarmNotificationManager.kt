package com.jingom.simplealarmmanager.alarm

import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

interface AlarmNotificationManager {
	fun notify(alarmInfo: AlarmInfo)
}

class DefaultAlarmNotificationManager(
	@ApplicationContext
	applicationContext: Context
): AlarmNotificationManager {

	private val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

	override fun notify(alarmInfo: AlarmInfo) {

	}
}