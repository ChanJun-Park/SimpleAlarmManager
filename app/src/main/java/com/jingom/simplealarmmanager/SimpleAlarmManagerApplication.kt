package com.jingom.simplealarmmanager

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.jingom.simplealarmmanager.common.notification.NotificationChannelManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SimpleAlarmManagerApplication: Application(), Configuration.Provider {

	@Inject
	lateinit var workerFactory: HiltWorkerFactory

	@Inject
	lateinit var notificationChannelManager: NotificationChannelManager

	override fun onCreate() {
		super.onCreate()

		notificationChannelManager.createChannels()
	}

	override fun getWorkManagerConfiguration(): Configuration {
		return Configuration.Builder()
			.setWorkerFactory(workerFactory)
			.build()
	}
}