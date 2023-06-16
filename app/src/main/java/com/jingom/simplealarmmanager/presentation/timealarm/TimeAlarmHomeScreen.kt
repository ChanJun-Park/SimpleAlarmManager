package com.jingom.simplealarmmanager.presentation.timealarm

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jingom.simplealarmmanager.presentation.timealarm.list.TimeAlarmListScreen

@Composable
fun TimeAlarmHomeScreen(
	timeAlarmHomeState: TimeAlarmHomeState = rememberTimeAlarmHomeState(),
) {
	Surface(modifier = Modifier.fillMaxSize()) {
		TimeAlarmListScreen(timeAlarmHomeState = timeAlarmHomeState)
	}
}
