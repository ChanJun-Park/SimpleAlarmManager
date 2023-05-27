package com.jingom.simplealarmmanager.data.alarm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jingom.simplealarmmanager.data.alarm.entity.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmEntityDao {
	@Insert
	fun insert(alarm: AlarmEntity)

	@Delete
	fun delete(alarm: AlarmEntity)

	@Query("SELECT * FROM alarm WHERE id = :id")
	fun select(id: Long): AlarmEntity?

	@Query("SELECT * FROM alarm")
	fun getAllFlow(): Flow<List<AlarmEntity>>
}