package com.jingom.simplealarmmanager.presentation.home

object AlarmHomeRoute {
	const val DETAIL_SCREEN_ARG = "alarm_id"

	const val LIST_SCREEN = "alarm_home/list"
	const val DETAIL_SCREEN = "alarm_home/detail?alarm_id={$DETAIL_SCREEN_ARG}"

	fun getDetailScreenRouteWithArg(alarmId: Long): String {
		return DETAIL_SCREEN.replace("{$DETAIL_SCREEN_ARG}", "$alarmId")
	}
}