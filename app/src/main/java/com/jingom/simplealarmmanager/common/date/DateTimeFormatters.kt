package com.jingom.simplealarmmanager.common.date

import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

object DateTimeFormatters {
//	val ALARM_TIME_AM_PM: DateTimeFormatter = DateTimeFormatterBuilder()
//		.appendValue(ChronoField.AMPM_OF_DAY)
//		.appendLiteral(' ')
//		.appendValue(ChronoField.CLOCK_HOUR_OF_AMPM, 2)
//		.appendLiteral(':')
//		.appendValue(ChronoField.MINUTE_OF_HOUR, 2)
//		.toFormatter()
//		.withResolverStyle(ResolverStyle.STRICT)

	val ALARM_TIME_AM_PM: DateTimeFormatter = DateTimeFormatter
		.ofPattern("a h:mm")
		.withResolverStyle(ResolverStyle.STRICT)
}