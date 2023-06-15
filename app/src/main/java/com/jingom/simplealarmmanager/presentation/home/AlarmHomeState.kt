package com.jingom.simplealarmmanager.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberAlarmHomeState(
	navController: NavHostController = rememberNavController()
): AlarmHomeState = remember(navController) {
	AlarmHomeState(navController)
}

@Stable
class AlarmHomeState(
	val navController: NavHostController
) {
	private val currentRoute: String?
		get() = navController.currentDestination?.route

	fun navigateToTimer() {
		navController.navigate(AppRoute.TIMER_SCREEN) {
			popUpTo(navController.graph.id) {
				inclusive = true
			}
		}
	}

	fun navigateToTimeAlarm() {
		navController.navigate(AppRoute.TIME_ALARM_HOME_SCREEN) {
			popUpTo(navController.graph.id) {
				inclusive = true
			}
		}
	}
}