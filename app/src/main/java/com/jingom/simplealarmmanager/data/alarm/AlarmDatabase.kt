package com.jingom.simplealarmmanager.data.alarm

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jingom.simplealarmmanager.data.alarm.dao.AlarmEntityDao
import com.jingom.simplealarmmanager.data.alarm.entity.AlarmEntity

@Database(entities = [AlarmEntity::class], version = 1)
abstract class AlarmDatabase: RoomDatabase() {
	abstract fun getAlarmEntityDao(): AlarmEntityDao
}