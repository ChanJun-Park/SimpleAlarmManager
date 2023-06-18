package com.jingom.simplealarmmanager.presentation.timer

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jingom.simplealarmmanager.R
import com.jingom.simplealarmmanager.common.pxToDp
import com.jingom.simplealarmmanager.ui.theme.SimpleAlarmManagerTheme
import java.util.UUID
import kotlin.math.min

@Composable
fun TimerScreen(
	viewModel: TimerViewModel = hiltViewModel()
) {
	val timerState = viewModel.timerState.collectAsStateWithLifecycle()
	val selectedTimeInSecond by getSelectedTimeInSecondState(timerState)
	val leftTimeInSecond by getLeftTimeInSecondState(timerState)

	TimerScreen(
		selectedTimeInSecond = selectedTimeInSecond,
		leftTimeInSecond = leftTimeInSecond,
		circleAngleProvider = { timerState.value.getCircleAngle() }
	)
	LaunchedEffect(true) {
		viewModel.startTimer()
	}
}

@Composable
private fun getSelectedTimeInSecondState(timerState: State<TimerState>) = remember(timerState.value.id) {
	derivedStateOf {
		timerState.value.selectedTimeInMillis / 1000
	}
}

@Composable
private fun getLeftTimeInSecondState(timerState: State<TimerState>) = remember(timerState.value.id) {
	derivedStateOf {
		timerState.value.leftTimeInMillis / 1000
	}

}

@Composable
fun TimerScreen(
	selectedTimeInSecond: Long,
	leftTimeInSecond: Long,
	circleAngleProvider: () -> Float
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxSize()
	) {
		TimerCircleLayout(
			selectedTimeInSecond = selectedTimeInSecond,
			leftTimeInSecond = leftTimeInSecond,
			circleAngleProvider = circleAngleProvider
		)
	}
}

@Composable
fun TimerCircleLayout(
	selectedTimeInSecond: Long,
	leftTimeInSecond: Long,
	circleAngleProvider: () -> Float
) {
	BoxWithConstraints(Modifier.fillMaxSize()) {
		val circleDiameter = min(constraints.maxWidth, constraints.maxHeight)

		TimerCircle(
			circleAngleProvider = circleAngleProvider,
			modifier = Modifier
				.size(circleDiameter.pxToDp())
				.padding(dimensionResource(R.dimen.common_horizontal_space))
				.align(Alignment.TopCenter)
		)

		TimerText(
			selectedTimeInSecond = selectedTimeInSecond,
			leftTimeInSecond = leftTimeInSecond,
			modifier = Modifier
				.size(circleDiameter.pxToDp())
				.padding(dimensionResource(R.dimen.common_horizontal_space))
				.align(Alignment.TopCenter)
		)
	}
}

@Composable
fun TimerCircle(
	circleAngleProvider: () -> Float,
	modifier: Modifier = Modifier
) {
	Canvas(modifier) {
		drawCircle(
			color = Color.Blue.copy(alpha = 0.3f),
			style = Stroke(
				width = 1.dp.toPx(),
				cap = StrokeCap.Round
			),
		)
		drawArc(
			color = Color.Blue,
			startAngle = -90f,
			sweepAngle = circleAngleProvider(),
			style = Stroke(
				width = 10.dp.toPx(),
				cap = StrokeCap.Round
			),
			useCenter = false
		)
	}
}

fun TimerState.getCircleAngle(): Float {
	return when (this) {
		is TimerState.OnGoing -> 360f * (leftTimeInMillis / selectedTimeInMillis.toFloat())
		is TimerState.Paused -> 360f * (leftTimeInMillis / selectedTimeInMillis.toFloat())
		is TimerState.ReadyToStart -> 360f
	}
}

@Composable
private fun TimerText(
	selectedTimeInSecond: Long,
	leftTimeInSecond: Long,
	modifier: Modifier = Modifier
) {
	Box(modifier) {
		Text(
			text = getSelectedTimeText(selectedTimeInSecond),
			style = MaterialTheme.typography.bodyLarge,
			modifier = Modifier
				.align(Alignment.TopCenter)
				.padding(vertical = 50.dp)
		)

		Text(
			text = getLeftTimeText(leftTimeInSecond),
			style = MaterialTheme.typography.headlineLarge,
			modifier = Modifier
				.align(Alignment.Center)
				.padding(vertical = 50.dp)
		)
	}
}

@Composable
fun getSelectedTimeText(
	selectedTimeInSecond: Long,
): String {
	var currentSelectedTimeInSecond = selectedTimeInSecond

	val hour = currentSelectedTimeInSecond / 3600
	currentSelectedTimeInSecond -= hour * 3600

	val minute = currentSelectedTimeInSecond / 60
	currentSelectedTimeInSecond -= minute * 60

	val second = currentSelectedTimeInSecond

	val stringBuilder = StringBuilder()

	if (hour != 0L) {
		stringBuilder.append(hour).append(stringResource(R.string.hour_abbr))
	}

	if (hour != 0L && minute != 0L) {
		stringBuilder.append(" ")
	}

	if (minute != 0L) {
		stringBuilder.append(minute).append(stringResource(R.string.minute_abbr))
	}

	if (minute != 0L && second != 0L) {
		stringBuilder.append(" ")
	}

	if (second != 0L) {
		stringBuilder.append(second).append(stringResource(R.string.second_abbr))
	}

	return stringBuilder.toString()
}

fun getLeftTimeText(
	leftTimeInSecond: Long
): String {

	var timeToLeftInSeconds = leftTimeInSecond

	val hour = timeToLeftInSeconds / 3600
	timeToLeftInSeconds -= hour * 3600

	val minute = timeToLeftInSeconds / 60
	timeToLeftInSeconds -= minute * 60

	val second = timeToLeftInSeconds

	val stringBuilder = StringBuilder()

	if (hour != 0L) {
		stringBuilder.append(hour).append(" : ")
	}

	if (hour != 0L || minute != 0L) {
		stringBuilder.append(minute).append(" : ")
	}

	stringBuilder.append(second)

	return stringBuilder.toString()
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TimerCircleLayoutPreview() {
	SimpleAlarmManagerTheme {
		TimerCircleLayout(
			selectedTimeInSecond = 10L,
			leftTimeInSecond = 5L,
			circleAngleProvider = { 180f }
		)
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
			timerState = TimerState.OnGoing(
				id = UUID.randomUUID().mostSignificantBits,
				selectedTimeInMillis = 1500L,
				leftTimeInMillis = 1000L
			)
		)
	}
}