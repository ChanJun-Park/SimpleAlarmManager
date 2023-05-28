package com.jingom.simplealarmmanager.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

interface AppAlarmManager {
	fun registerAlarm(alarm: Alarm, targetDate: LocalDate = LocalDate.now())
	fun cancelAlarm(alarm: Alarm)
}

class DefaultAppAlarmManager(
	private val applicationContext: Context
) : AppAlarmManager {

	private val alarmManager = applicationContext.getSystemService(
		Context.ALARM_SERVICE
	) as AlarmManager

	@SuppressLint("MissingPermission")
	override fun registerAlarm(alarm: Alarm, targetDate: LocalDate) {
		alarmManager.setAlarmClock(
			getAlarmClockInfo(alarm, targetDate),
			getAlarmPendingIntent(alarm)
		)
	}

	override fun cancelAlarm(alarm: Alarm) {
		alarmManager.cancel(
			getAlarmPendingIntent(alarm)
		)
	}

	private fun getAlarmClockInfo(alarm: Alarm, targetDate: LocalDate = LocalDate.now()): AlarmClockInfo {
		val alarmInstant = ZonedDateTime.of(
			targetDate,
			alarm.time,
			ZoneId.systemDefault()
		).toInstant()

		return AlarmClockInfo(
			alarmInstant.toEpochMilli(),
			null
		)
	}

	private fun getAlarmPendingIntent(alarm: Alarm): PendingIntent {
		val pendingIntentFlag = PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE

		return PendingIntent.getBroadcast(
			applicationContext,
			getAlarmRequestCode(alarm),
			getAlarmIntent(alarm),
			pendingIntentFlag
		)
	}

	private fun getAlarmIntent(alarm: Alarm): Intent {
		return Intent(applicationContext, AlarmReceiver::class.java).apply {
			putExtra(AlarmInfo.KEY, alarm.toIntentModel())
		}
	}

	private fun getAlarmRequestCode(alarm: Alarm): Int {
		return alarm.id.toInt()
	}
}