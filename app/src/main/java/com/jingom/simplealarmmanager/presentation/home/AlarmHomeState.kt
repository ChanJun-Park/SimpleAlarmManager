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

	fun navigateToDetail(alarmId: Long) {
		val detailRoute = AlarmHomeRoute.getDetailScreenRouteWithArg(alarmId)

		navController.navigate(detailRoute)
	}

	fun navigateToListFromDetail() {
		if (currentRoute != AlarmHomeRoute.DETAIL_SCREEN) {
			return
		}

		navController.popBackStack()
	}
}