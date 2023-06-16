package com.jingom.simplealarmmanager.presentation.timealarm

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.presentation.timealarm.detail.TimerAlarmDetailActivity

@Composable
fun rememberTimeAlarmHomeState(): TimeAlarmHomeState = remember {
	TimeAlarmHomeState()
}

@Stable
class TimeAlarmHomeState {
	fun navigateToDetail(context: Context, alarm: Alarm) {
		TimerAlarmDetailActivity.startTimerAlarmDetailActivity(context, alarm.id)
	}

	fun navigateToAdd(context: Context) {
		TimerAlarmDetailActivity.startTimerAlarmDetailActivity(context, null)
	}
}