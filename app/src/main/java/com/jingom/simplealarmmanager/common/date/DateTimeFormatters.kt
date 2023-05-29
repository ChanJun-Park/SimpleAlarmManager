package com.jingom.simplealarmmanager.common.date

import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

object DateTimeFormatters {
	val ALARM_TIME_AM_PM: DateTimeFormatter = DateTimeFormatter
		.ofPattern("a h:mm")
		.withResolverStyle(ResolverStyle.STRICT)
}