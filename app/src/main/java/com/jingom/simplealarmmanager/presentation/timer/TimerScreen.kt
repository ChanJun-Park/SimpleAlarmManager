package com.jingom.simplealarmmanager.presentation.timer

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jingom.simplealarmmanager.R
import com.jingom.simplealarmmanager.ui.theme.SimpleAlarmManagerTheme

@Composable
fun TimerScreen() {

}

@Composable
fun TimerScreen(leftTimeMillis: Long) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxSize()
	) {


	}
}

@Composable
private fun TimerButtons(
	timerState: TimerState,
	onStartClick: () -> Unit = {},
	onPauseClick: () -> Unit = {},
	onResumeClick: () -> Unit = {},
	onDeleteClick: () -> Unit = {}
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceEvenly,
		modifier = Modifier.fillMaxWidth()
	) {
		if (timerState is TimerState.ReadyToStart) {
			TimerButton(
				text = stringResource(R.string.start),
				onClick = onStartClick
			)
			return@Row
		}

		TimerButton(
			text = stringResource(R.string.delete),
			colors = buttonColors(
				containerColor = MaterialTheme.colorScheme.secondaryContainer,
				contentColor = MaterialTheme.colorScheme.onSecondaryContainer
			),
			onClick = onDeleteClick
		)

		TimerButton(
			text = if (timerState is TimerState.OnGoing) {
				stringResource(R.string.pause)
			} else {
				stringResource(R.string.resume)
			},
			colors = buttonColors(
				containerColor = Color.Red,
				contentColor = Color.White
			),
			onClick = if (timerState is TimerState.OnGoing) {
				onPauseClick
			} else {
				onResumeClick
			}
		)
	}
}

@Composable
fun TimerButton(
	text: String = "",
	colors: ButtonColors = buttonColors(),
	onClick: () -> Unit = {}
) {
	TextButton(
		shape = RoundedCornerShape(percent = 50),
		colors = colors,
		onClick = onClick,
		modifier = Modifier.defaultMinSize(
			minWidth = dimensionResource(R.dimen.common_button_minimum_width)
		)
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.bodyLarge
		)
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TimerButtonsPreview() {
	SimpleAlarmManagerTheme {
		TimerButtons(
			timerState = TimerState.OnGoing(timeToLeftInMillis = 1000L)
		)
	}
}