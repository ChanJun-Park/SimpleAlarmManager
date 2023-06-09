package com.jingom.simplealarmmanager.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.Settings
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
		val content = getAlarmContent(alarm)

		val notification = NotificationCompat.Builder(applicationContext, appChannelId)
			.setTicker(content)
			.setContentTitle(title)
			.setContentText(content)
			.setWhen(System.currentTimeMillis())
			.setLargeIcon(largeIcon)
			.setSmallIcon(R.drawable.ic_launcher_foreground)
			.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
			.setVibrate(longArrayOf(1000, 500, 500, 1000, 1000))
			.setOnlyAlertOnce(true)
			.setAutoCancel(true)
			.setDeleteIntent(getNotificationDeletePendingIntent(alarm))
			.build()

		notificationManager.notify(alarm.id.toInt(), notification)
	}

	private fun getAlarmContent(alarm: Alarm): String {
		val alarmTimeString = alarm.time.formatWithLocale(DateTimeFormatters.ALARM_TIME_AM_PM)
		val cancelString = applicationContext.getString(R.string.swipe_to_cancel_alarm)

		return "$alarmTimeString $cancelString"
	}

	private fun getNotificationDeletePendingIntent(alarm: Alarm): PendingIntent {
		val pendingIntentFlag = PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE

		return PendingIntent.getBroadcast(
			applicationContext,
			getAlarmRequestCode(alarm),
			getAlarmIntent(alarm),
			pendingIntentFlag
		)
	}

	private fun getAlarmRequestCode(alarm: Alarm): Int {
		return alarm.id.toInt()
	}

	private fun getAlarmIntent(alarm: Alarm): Intent {
		return Intent(applicationContext, AlarmNotificationDeleteReceiver::class.java).apply {
			putExtra(AlarmInfo.KEY, alarm.toIntentModel())
		}
	}
}