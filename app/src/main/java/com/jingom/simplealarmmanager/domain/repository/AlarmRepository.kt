package com.jingom.simplealarmmanager.domain.repository

import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import kotlinx.coroutines.flow.Flow

/**
 * AlarmRepository
 *
 * <p>로컬 저장소에 저장된 알람 정보들을 관리한다.</p>
 *
 * <p>로컬 저장소에 저장된 알람 정보들은 모두 현재 AlarmManager 를 통해 안드로이드 시스템 상에 등록된 상태다.</p>
 */
interface AlarmRepository {
	suspend fun upsert(alarm: Alarm): Long
	suspend fun delete(alarm: Alarm)
	suspend fun get(id: Long): Alarm?
	suspend fun getAll(): List<Alarm>
	suspend fun isAlarmEmpty(): Boolean
	fun getAllAlarmFlow(): Flow<List<Alarm>>
}