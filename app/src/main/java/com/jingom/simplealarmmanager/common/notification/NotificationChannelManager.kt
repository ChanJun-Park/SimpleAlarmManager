package com.jingom.simplealarmmanager.common.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext

interface NotificationChannelManager {
	fun createChannels()
}

class DefaultNotificationChannelManager(
	@ApplicationContext
	private val applicationContext: Context
): NotificationChannelManager {

	private val notificationManager: NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

	override fun createChannels() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			return
		}

		NotificationChannelType.values().forEach {
			createChannel(it)
		}
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun createChannel(type: NotificationChannelType) {
		val channel = NotificationChannel(
			type.id,
			type.getChannelName(applicationContext),
			type.importantLevel
		)

		notificationManager.createNotificationChannel(channel)
	}
}