package com.jingom.simplealarmmanager.presentation.timealarm

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jingom.simplealarmmanager.presentation.timealarm.detail.TimeAlarmDetailScreen
import com.jingom.simplealarmmanager.presentation.timealarm.list.TimeAlarmListScreen

@Composable
fun TimeAlarmHomeScreen(
	timeAlarmHomeState: TimeAlarmHomeState = rememberTimeAlarmHomeState(),
) {
	Surface(modifier = Modifier.fillMaxSize()) {
		NavHost(
			navController = timeAlarmHomeState.navController,
			startDestination = TimeAlarmHomeRoute.LIST_SCREEN
		) {
			composable(
				route = TimeAlarmHomeRoute.LIST_SCREEN,
			) {
				TimeAlarmListScreen(timeAlarmHomeState = timeAlarmHomeState)
			}
			composable(
				route = TimeAlarmHomeRoute.DETAIL_SCREEN,
				arguments = listOf(navArgument(TimeAlarmHomeRoute.DETAIL_SCREEN_ARG) { nullable = true })
			) { navBackStackEntry ->
				val alarmId = getAlarmIdFromArgs(navBackStackEntry)

				TimeAlarmDetailScreen(
					alarmId = alarmId,
					timeAlarmHomeState = timeAlarmHomeState
				)
			}
		}
	}
}

private fun getAlarmIdFromArgs(navBackStackEntry: NavBackStackEntry): Long? = navBackStackEntry.arguments?.getString(TimeAlarmHomeRoute.DETAIL_SCREEN_ARG)?.toLongOrNull()