package com.jingom.simplealarmmanager.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jingom.simplealarmmanager.R
import com.jingom.simplealarmmanager.presentation.timealarm.TimeAlarmHomeScreen
import com.jingom.simplealarmmanager.presentation.timer.TimerScreen

@Composable
fun AlarmHomeScreen(
	notificationPermissionState: NotificationPermissionState,
	alarmHomeState: AlarmHomeState = rememberAlarmHomeState(),
	onFinish: () -> Unit = {}
) {

	Column(Modifier.fillMaxSize()) {
		NavigationArea(
			alarmHomeState = alarmHomeState,
			modifier = Modifier.weight(1f)
		)
		AlarmHomeBottomAppBar(
			alarmHomeState = alarmHomeState
		)
	}

	NotificationPermissionAlert(
		notificationPermissionState = notificationPermissionState,
		onPermissionDenied = onFinish
	)
}

@Composable
private fun NavigationArea(
	alarmHomeState: AlarmHomeState,
	modifier: Modifier = Modifier
) {
	Surface(modifier = modifier.fillMaxSize()) {
		NavHost(
			navController = alarmHomeState.navController,
			startDestination = AppRoute.TIME_ALARM_HOME_SCREEN
		) {
			composable(
				route = AppRoute.TIME_ALARM_HOME_SCREEN,
			) {
				TimeAlarmHomeScreen()
			}
			composable(
				route = AppRoute.TIMER_SCREEN
			) {
				TimerScreen()
			}
		}
	}
}

@Composable
private fun AlarmHomeBottomAppBar(
	alarmHomeState: AlarmHomeState
) {
	BottomAppBar(
		modifier = Modifier
			.fillMaxWidth()
			.height(60.dp)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceEvenly,
			modifier = Modifier.fillMaxSize()
		) {
			IconButton(
				onClick = alarmHomeState::navigateToTimeAlarm
			) {
				Icon(
					imageVector = Icons.Default.List,
					contentDescription = stringResource(R.string.screen_time_alarm_list)
				)
			}

			IconButton(
				onClick = alarmHomeState::navigateToTimer
			) {
				Icon(
					imageVector = Icons.Default.DateRange,
					contentDescription = stringResource(R.string.screen_time_alarm_list)
				)
			}
		}
	}
}

@Composable
private fun NotificationPermissionAlert(
	notificationPermissionState: NotificationPermissionState,
	onPermissionDenied: () -> Unit = {}
) {
	val context = LocalContext.current
	var needToShowDialog by remember {
		mutableStateOf(notificationPermissionState.hasNotificationPermission.not())
	}

	if (needToShowDialog) {
		AlertDialog(
			onDismissRequest = {
				Toast.makeText(context, R.string.cannot_working_notification_permission_revoked, Toast.LENGTH_LONG).show()
				onPermissionDenied()
			},
			confirmButton = {
				Button(
					onClick = {
						notificationPermissionState.requestPermission(
							onGranted = {
								needToShowDialog = false
							},
							onDenied = { shouldShowRationale ->
								Toast.makeText(context, R.string.cannot_working_notification_permission_revoked, Toast.LENGTH_LONG).show()
								if (shouldShowRationale.not()) {
									Toast.makeText(context, R.string.go_setting_to_grant_notification_permission, Toast.LENGTH_LONG).show()
								}
								onPermissionDenied()
							}
						)
					}
				) {
					Text(
						text = stringResource(R.string.confirm),
						style = MaterialTheme.typography.labelLarge
					)
				}
			},
			title = {
				Text(
					text = stringResource(R.string.notification_permission_required),
					style = MaterialTheme.typography.labelLarge
				)
			},
			text = {
				Text(
					text = stringResource(R.string.notification_permission_required_description),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		)
	}
}