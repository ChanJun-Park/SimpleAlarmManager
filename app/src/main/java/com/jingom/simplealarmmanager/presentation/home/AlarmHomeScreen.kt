package com.jingom.simplealarmmanager.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jingom.simplealarmmanager.presentation.home.detail.AlarmDetailScreen
import com.jingom.simplealarmmanager.presentation.home.list.AlarmListScreen
import java.lang.IllegalStateException

@Composable
fun AlarmHomeScreen(
	alarmHomeState: AlarmHomeState = rememberAlarmHomeState()
) {
	Surface(modifier = Modifier.fillMaxSize()) {
		NavHost(
			navController = alarmHomeState.navController,
			startDestination = AlarmHomeRoute.LIST_SCREEN
		) {
			composable(
				route = AlarmHomeRoute.LIST_SCREEN,
			) {
				AlarmListScreen(alarmHomeState = alarmHomeState)
			}
			composable(
				route = AlarmHomeRoute.DETAIL_SCREEN,
				arguments = listOf(navArgument(AlarmHomeRoute.DETAIL_SCREEN_ARG) { nullable = true })
			) { navBackStackEntry ->
				val alarmId = getAlarmIdFromArgs(navBackStackEntry)

				AlarmDetailScreen(
					alarmId = alarmId,
					alarmHomeState = alarmHomeState
				)
			}
		}
	}
}

private fun getAlarmIdFromArgs(navBackStackEntry: NavBackStackEntry): Long? = navBackStackEntry.arguments?.getString(AlarmHomeRoute.DETAIL_SCREEN_ARG)?.toLongOrNull()