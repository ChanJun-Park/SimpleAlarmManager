package com.jingom.simplealarmmanager.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat

@Composable
fun rememberNotificationPermissionState(
	activity: ComponentActivity
): NotificationPermissionState {

	val callback = remember {
		NotificationPermissionCallback()
	}

	val permissionRequestLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
		onResult = { isGranted ->
			if (isGranted) {
				callback.notifyGrant()
			} else {
				val shouldShowRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
					activity.shouldShowRequestPermissionRationale(
						Manifest.permission.POST_NOTIFICATIONS
					)
				} else {
					false
				}
				callback.notifyDenial(shouldShowRationale)
			}
		}
	)

	return remember(activity, callback, permissionRequestLauncher) {
		NotificationPermissionState(activity, callback, permissionRequestLauncher)
	}
}

class NotificationPermissionCallback {

	private val listeners = mutableListOf<OnRequestPermissionResult>()

	fun registerListener(listener: OnRequestPermissionResult) {
		listeners.add(listener)
	}

	fun unregisterListener(listener: OnRequestPermissionResult) {
		listeners.remove(listener)
	}

	fun notifyGrant() {
		listeners.forEach {
			it.onGranted()
		}
	}

	fun notifyDenial(shouldShowRationale: Boolean) {
		listeners.forEach {
			it.onDenied(shouldShowRationale)
		}
	}

	interface OnRequestPermissionResult {
		fun onGranted()
		fun onDenied(shouldShowRationale: Boolean)
	}
}

@Stable
class NotificationPermissionState(
	private val activity: ComponentActivity,
	private val notificationPermissionCallback: NotificationPermissionCallback,
	private val permissionRequestLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
	private val isTiramisuAbove: Boolean
		get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU


	val hasNotificationPermission: Boolean
		get() = if (isTiramisuAbove) {
			ContextCompat.checkSelfPermission(
				activity,
				Manifest.permission.POST_NOTIFICATIONS
			) == PackageManager.PERMISSION_GRANTED
		} else {
			true
		}

	val shouldShowRationale: Boolean
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			activity.shouldShowRequestPermissionRationale(
				Manifest.permission.POST_NOTIFICATIONS
			)
		} else {
			false
		}

	fun requestPermission(
		onGranted: () -> Unit = {},
		onDenied: (shouldShowRationale: Boolean) -> Unit = {}
	) {
		if (isTiramisuAbove) {
			notificationPermissionCallback.registerListener(
				object: NotificationPermissionCallback.OnRequestPermissionResult {
					override fun onGranted() {
						onGranted()
						notificationPermissionCallback.unregisterListener(this)
					}

					override fun onDenied(shouldShowRationale: Boolean) {
						onDenied.invoke(shouldShowRationale)
						notificationPermissionCallback.unregisterListener(this)
					}
				}
			)

			permissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
		} else {
			onGranted()
		}
	}
}