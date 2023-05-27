package com.jingom.simplealarmmanager.data.di

import android.content.Context
import androidx.room.Room
import com.jingom.simplealarmmanager.data.alarm.AlarmDatabase
import com.jingom.simplealarmmanager.data.alarm.dao.AlarmEntityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DBModule {

	companion object {
		@Provides
		@Singleton
		fun provideAlarmDatabase(@ApplicationContext context: Context): AlarmDatabase {
			return Room.databaseBuilder(
				context,
				AlarmDatabase::class.java,
				"alarmDatabase"
			).build()
		}

		@Provides
		@Singleton
		fun provideAlarmEntityDao(db: AlarmDatabase): AlarmEntityDao {
			return db.getAlarmEntityDao()
		}
	}
}