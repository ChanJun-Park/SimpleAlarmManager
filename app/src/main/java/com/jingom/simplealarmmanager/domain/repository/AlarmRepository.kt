package com.jingom.simplealarmmanager.domain.repository

import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
	suspend fun insert(alarm: Alarm)
	suspend fun delete(alarm: Alarm)
	fun getAllAlarmFlow(): Flow<List<Alarm>>
}