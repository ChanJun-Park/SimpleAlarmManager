package com.jingom.simplealarmmanager.data.alarm.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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

fun Alarm.toDBModel(): AlarmEntity =
	AlarmEntity(
		id = id,
		name = name,
		time = time.format(DateTimeFormatter.ISO_LOCAL_TIME),
		alarmOn = alarmOn
	)

fun AlarmEntity.toDomainModel(): Alarm =
	Alarm(
		id = id,
		name = name,
		time = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME),
		alarmOn = alarmOn
	)
