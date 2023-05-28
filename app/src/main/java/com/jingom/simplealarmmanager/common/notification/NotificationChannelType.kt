package com.jingom.simplealarmmanager.common.notification

import android.app.NotificationManager
import android.content.Context
import androidx.annotation.StringRes
import com.jingom.simplealarmmanager.R

enum class NotificationChannelType(
	val id: String,
	@StringRes val channelNameResId: Int,
	val importantLevel: Int
) {
	APP_ALARM("alarm", R.string.alarm, NotificationManager.IMPORTANCE_HIGH);

	fun getChannelName(context: Context): String {
		return context.getString(channelNameResId)
	}
}