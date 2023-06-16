package com.jingom.simplealarmmanager.presentation.timealarm.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jingom.simplealarmmanager.ui.theme.SimpleAlarmManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimerAlarmDetailActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val alarmId = intent.getAlarmId()

		setContent {
			SimpleAlarmManagerTheme {
				TimeAlarmDetailScreen(
					alarmId = alarmId,
					navigateBack = this::finish
				)
			}
		}
	}

	companion object {
		private const val ARG_ALARM_ID = "alarmId"

		fun startTimerAlarmDetailActivity(context: Context, alarmId: Long?) {
			Intent(context, TimerAlarmDetailActivity::class.java).apply {
				putExtra(ARG_ALARM_ID, alarmId)
				context.startActivity(this)
			}
		}

		private fun Intent.getAlarmId(): Long? {
			val alarmId = getLongExtra(ARG_ALARM_ID, -1)
			return if (alarmId != -1L) {
				alarmId
			} else {
				null
			}
		}
	}
}
