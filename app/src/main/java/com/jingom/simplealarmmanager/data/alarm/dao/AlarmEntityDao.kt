package com.jingom.simplealarmmanager.data.alarm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmEntityDao {
	@Insert
	fun insert(alarm: Alarm)

	@Delete
	fun delete(alarm: Alarm)

	@Query("SELECT * FROM alarm")
	fun getAllFlow(): Flow<Alarm>
}