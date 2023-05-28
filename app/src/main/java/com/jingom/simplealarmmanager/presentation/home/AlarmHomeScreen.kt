package com.jingom.simplealarmmanager.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jingom.simplealarmmanager.R
import com.jingom.simplealarmmanager.presentation.home.detail.AlarmDetailScreen
import com.jingom.simplealarmmanager.presentation.home.list.AlarmListScreen

@Composable
fun AlarmHomeScreen(
	notificationPermissionState: NotificationPermissionState,
	alarmHomeState: AlarmHomeState = rememberAlarmHomeState(),
	onFinish: () -> Unit = {}
) {
	Surface(modifier = Modifier.fillMaxSize()) {
		NavHost(
			navController = alarmHomeState.navController,
			startDestination = AlarmHomeRoute.LIST_SCREEN
		) {
			composable(
				route = AlarmHomeRoute.LIST_SCREEN,
			) {
				AlarmListScreen(alarmHomeState = alarmHomeState)
			}
			composable(
				route = AlarmHomeRoute.DETAIL_SCREEN,
				arguments = listOf(navArgument(AlarmHomeRoute.DETAIL_SCREEN_ARG) { nullable = true })
			) { navBackStackEntry ->
				val alarmId = getAlarmIdFromArgs(navBackStackEntry)

				AlarmDetailScreen(
					alarmId = alarmId,
					alarmHomeState = alarmHomeState
				)
			}
		}
	}

	NotificationPermissionAlert(
		notificationPermissionState = notificationPermissionState,
		onPermissionDenied = onFinish
	)
}

private fun getAlarmIdFromArgs(navBackStackEntry: NavBackStackEntry): Long? = navBackStackEntry.arguments?.getString(AlarmHomeRoute.DETAIL_SCREEN_ARG)?.toLongOrNull()

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