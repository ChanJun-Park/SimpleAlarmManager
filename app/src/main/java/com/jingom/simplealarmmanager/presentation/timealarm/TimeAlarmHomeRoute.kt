package com.jingom.simplealarmmanager.presentation.timealarm

object TimeAlarmHomeRoute {
	const val DETAIL_SCREEN_ARG = "alarm_id"

	const val LIST_SCREEN = "time_alarm_home/list"
	const val DETAIL_SCREEN = "time_alarm_home/detail?alarm_id={$DETAIL_SCREEN_ARG}"

	fun getDetailScreenRouteWithArg(alarmId: Long): String {
		return DETAIL_SCREEN.replace("{$DETAIL_SCREEN_ARG}", "$alarmId")
	}

	fun getDetailScreenRouteWithoutArg(): String {
		return DETAIL_SCREEN
	}
}