package com.jingom.simplealarmmanager.alarm

import android.os.Parcelable
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import kotlinx.parcelize.Parcelize
import java.time.LocalTime

@Parcelize
data class AlarmInfo(
	val id: Long,
	val name: String,
	val time: LocalTime,
): Parcelable {

	companion object {
		const val KEY = "alarmInfo"
	}
}

fun Alarm.toIntentModel() = AlarmInfo(
	id = id,
	name = name,
	time = time
)
