package com.jingom.simplealarmmanager.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm

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

	fun navigateToDetail(alarm: Alarm) {
		val detailRoute = AlarmHomeRoute.getDetailScreenRouteWithArg(alarm.id)

		navController.navigate(detailRoute)
	}

	fun navigateToAdd() {

	}

	fun navigateToListFromDetail() {
		if (currentRoute != AlarmHomeRoute.DETAIL_SCREEN) {
			return
		}

		navController.popBackStack()
	}
}