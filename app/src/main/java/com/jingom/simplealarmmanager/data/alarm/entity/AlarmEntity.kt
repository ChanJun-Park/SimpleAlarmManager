package com.jingom.simplealarmmanager.data.alarm.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class AlarmEntity(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo("id")
	val id: Long = 0,
	@ColumnInfo("name")
	val name: String = "",
	@ColumnInfo("time")
	val time: String = "",
	@ColumnInfo("alarm_on")
	val alarmOn: Boolean = true
)
