package com.jingom.simplealarmmanager.alarm

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.jingom.simplealarmmanager.R
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
		val largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_launcher_foreground)
		val appChannelId = NotificationChannelType.APP_ALARM.id

		val content = alarm.name

		val notification = NotificationCompat.Builder(applicationContext, appChannelId)
			.setTicker(content)
			.setContentTitle(applicationContext.resources.getString(R.string.app_name))
			.setContentText(content)
			.setWhen(System.currentTimeMillis())
			.setLargeIcon(largeIcon)
			.setSmallIcon(R.drawable.ic_launcher_foreground)
			.setAutoCancel(true)
			.build()

		notificationManager.notify(alarm.id.toInt(), notification)
	}
}