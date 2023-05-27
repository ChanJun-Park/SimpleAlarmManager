package com.jingom.simplealarmmanager.data.di

import com.jingom.simplealarmmanager.data.alarm.dao.AlarmEntityDao
import com.jingom.simplealarmmanager.data.alarm.repository.DefaultAlarmRepository
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

	companion object {
		@Provides
		@Singleton
		fun provideAlarmRepository(alarmEntityDao: AlarmEntityDao): AlarmRepository {
			return DefaultAlarmRepository(alarmEntityDao)
		}
	}
}