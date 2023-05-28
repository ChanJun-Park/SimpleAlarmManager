package com.jingom.simplealarmmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import com.jingom.simplealarmmanager.presentation.home.AlarmHomeScreen
import com.jingom.simplealarmmanager.presentation.home.rememberNotificationPermissionState
import com.jingom.simplealarmmanager.ui.theme.SimpleAlarmManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			SimpleAlarmManagerTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					color = MaterialTheme.colorScheme.background,
					modifier = Modifier.fillMaxSize()
				) {
					AlarmHomeScreen(
						notificationPermissionState = rememberNotificationPermissionState(this),
						onFinish = {
							finish()
						}
					)
				}
			}
		}
	}
}
