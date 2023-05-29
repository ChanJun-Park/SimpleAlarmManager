package com.jingom.simplealarmmanager.alarm

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.jingom.simplealarmmanager.R
import com.jingom.simplealarmmanager.common.date.DateTimeFormatters
import com.jingom.simplealarmmanager.common.date.formatWithLocale
import com.jingom.simplealarmmanager.common.notification.NotificationChannelType
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import dagger.hilt.android.qualifiers.ApplicationContext

interface AlarmNotificationManager {
	fun notify(alarm: Alarm)
}

class DefaultAlarmNotificationManager(
	@ApplicationContext
	private val applicationContext: Context
) : AlarmNotificationManager {

	private val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

	override fun notify(alarm: Alarm) {
		Log.d("AlarmNotificationManager", "AlarmNotificationManager called")

		val largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_launcher_foreground)
		val appChannelId = NotificationChannelType.APP_ALARM.id

		val title = alarm.name
		val content = alarm.time.formatWithLocale(DateTimeFormatters.ALARM_TIME_AM_PM)

		val notification = NotificationCompat.Builder(applicationContext, appChannelId)
			.setTicker(content)
			.setContentTitle(title)
			.setContentText(content)
			.setWhen(System.currentTimeMillis())
			.setLargeIcon(largeIcon)
			.setSmallIcon(R.drawable.ic_launcher_foreground)
			.setAutoCancel(true)
			.build()

		notificationManager.notify(alarm.id.toInt(), notification)
	}
}