package com.jingom.simplealarmmanager.presentation.timealarm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm

@Composable
fun rememberTimeAlarmHomeState(
	navController: NavHostController = rememberNavController()
): TimeAlarmHomeState = remember(navController) {
	TimeAlarmHomeState(navController)
}

@Stable
class TimeAlarmHomeState(
	val navController: NavHostController
) {
	private val currentRoute: String?
		get() = navController.currentDestination?.route

	fun navigateToDetail(alarm: Alarm) {
		val detailRoute = TimeAlarmHomeRoute.getDetailScreenRouteWithArg(alarm.id)

		navController.navigate(detailRoute)
	}

	fun navigateToAdd() {
		val addRoute = TimeAlarmHomeRoute.getDetailScreenRouteWithoutArg()

		navController.navigate(addRoute)
	}

	fun navigateToListFromDetail() {
		if (currentRoute != TimeAlarmHomeRoute.DETAIL_SCREEN) {
			return
		}

		navController.popBackStack()
	}
}