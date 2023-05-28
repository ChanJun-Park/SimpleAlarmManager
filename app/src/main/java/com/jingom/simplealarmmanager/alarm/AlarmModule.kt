package com.jingom.simplealarmmanager.alarm

import android.app.AlarmManager
import android.content.Context
import com.jingom.simplealarmmanager.common.BootReceiverManager
import com.jingom.simplealarmmanager.common.DefaultBootReceiverManager
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AlarmModule {

	companion object {

		@Provides
		@Singleton
		fun provideAppAlarmManager(
			@ApplicationContext
			applicationContext: Context
		): AppAlarmManager {
			return DefaultAppAlarmManager(applicationContext)
		}

		@Provides
		@Singleton
		fun provideAlarmController(
			alarmRepository: AlarmRepository,
			alarmManager: AppAlarmManager,
			bootReceiverManager: BootReceiverManager
		): AlarmController {
			return DefaultAlarmController(alarmRepository, alarmManager, bootReceiverManager)
		}

		@Provides
		@Singleton
		fun provideAlarmNotificationManager(
			@ApplicationContext
			applicationContext: Context
		): AlarmNotificationManager {
			return DefaultAlarmNotificationManager(applicationContext)
		}
	}
}