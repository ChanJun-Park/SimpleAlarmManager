package com.jingom.simplealarmmanager.common.date

import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.util.Locale

fun Temporal.formatWithLocale(
	formatter: DateTimeFormatter,
	locale: Locale = Locale.getDefault()
): String {
	return formatter.withLocale(locale).format(this)
}