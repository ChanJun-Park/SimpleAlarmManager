package com.jingom.simplealarmmanager.presentation.home.detail

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jingom.simplealarmmanager.R
import com.jingom.simplealarmmanager.common.date.DateTimeFormatters
import com.jingom.simplealarmmanager.common.date.formatWithLocale
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.presentation.home.AlarmHomeState
import com.jingom.simplealarmmanager.presentation.home.detail.AlarmDetailEditState.Companion.canEdit
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

@Composable
fun AlarmDetailScreen(
	alarmId: Long?,
	alarmHomeState: AlarmHomeState,
	viewModel: AlarmDetailViewModel = hiltViewModel()
) {
	val alarmDetailState by viewModel.alarmDetailState.collectAsStateWithLifecycle()
	val (alarmNameForTextField, setAlarmNameForTextField) = viewModel.alarmNameForTextField

	AlarmDetailScreen(
		alarmDetailState = alarmDetailState,
		alarmNameForTextField = alarmNameForTextField,
		onAlarmNameEdited = setAlarmNameForTextField,
		onAlarmTimeChanged = viewModel::changeAlarmTime,
		onCancelClick = alarmHomeState::navigateToListFromDetail,
		onSaveClick = viewModel::saveAlarm,
		onDeleteClick = viewModel::deleteAlarm
	)

	LaunchedEffect(key1 = alarmId) {
		viewModel.init(alarmId)
	}

	(alarmDetailState as? AlarmDetailState.Success)?.let {
		if (it.editState is AlarmDetailEditState.Saved) {
			LaunchedEffect(key1 = true) {
				alarmHomeState.navigateToListFromDetail()
			}
		}
	}
}

@Composable
fun AlarmDetailScreen(
	alarmDetailState: AlarmDetailState,
	alarmNameForTextField: String = "",
	onAlarmNameEdited: (String) -> Unit = {},
	onAlarmTimeChanged: (LocalTime) -> Unit = {},
	onCancelClick: () -> Unit = {},
	onSaveClick: () -> Unit = {},
	onDeleteClick: () -> Unit = {}
) {
	Column(Modifier.fillMaxSize()) {

		AlarmDetailToolbar(
			onUpButtonClick = onCancelClick
		)

		Spacer(modifier = Modifier.height(30.dp))

		Box(
			Modifier
				.fillMaxWidth()
				.fillMaxHeight()
				.weight(1f)
		) {
			when (alarmDetailState) {
				is AlarmDetailState.Fail -> LoadFailScreen()
				is AlarmDetailState.Loading -> LoadingScreen()
				is AlarmDetailState.Success -> AlarmDetail(
					alarmDetailState = alarmDetailState,
					alarmNameForTextField = alarmNameForTextField,
					onAlarmNameEdited = onAlarmNameEdited,
					onAlarmTimeChanged = onAlarmTimeChanged,
				)
			}
		}

		AlarmDetailBottomMenu(
			onCancelClick = onCancelClick,
			onSaveClick = onSaveClick,
			onDeleteClick = onDeleteClick,
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmDetailToolbar(
	onUpButtonClick: () -> Unit = {}
) {
	CenterAlignedTopAppBar(
		title = {
			Text(
				text = stringResource(R.string.edit_alarm),
				style = MaterialTheme.typography.titleLarge,
				textAlign = TextAlign.Center
			)
		},
		navigationIcon = {
			IconButton(onClick = onUpButtonClick) {
				Image(
					imageVector = Icons.Default.ArrowBack,
					contentDescription = stringResource(R.string.back_button)
				)
			}
		},
		modifier = Modifier.fillMaxWidth()
	)
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AlarmDetailToolbarPreview() {
	Surface {
		AlarmDetailToolbar()
	}
}

@Composable
private fun LoadingScreen() {
	Box(Modifier.fillMaxSize()) {
		CircularProgressIndicator(
			Modifier
				.size(100.dp)
				.align(Alignment.Center)
		)
	}
}

@Composable
private fun LoadFailScreen() {
	Box(Modifier.fillMaxSize()) {
		Text(
			text = stringResource(R.string.failed_to_loading_alarm),
			style = MaterialTheme.typography.headlineMedium
		)
	}
}

@Composable
private fun AlarmDetail(
	alarmDetailState: AlarmDetailState.Success,
	alarmNameForTextField: String = "",
	onAlarmNameEdited: (String) -> Unit = {},
	onAlarmTimeChanged: (LocalTime) -> Unit = {}
) {
	Column(Modifier.fillMaxSize()) {
		AlarmName(
			alarmName = alarmNameForTextField,
			onAlarmNameEdited = onAlarmNameEdited,
			enabled = alarmDetailState.editState.canEdit()
		)
		Spacer(modifier = Modifier.height(20.dp))
		AlarmTime(
			alarmTime = alarmDetailState.alarm.time,
			onAlarmTimeChanged = onAlarmTimeChanged
		)
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AlarmDetailScreenPreview() {
	val alarmDetailState = AlarmDetailState.Success(
		alarm = Alarm(1, "test", LocalTime.of(13, 0, 0), true),
		editState = AlarmDetailEditState.Initialized
	)

	AlarmDetailScreen(alarmDetailState)
}

@Preview(name = "Loading", showBackground = true)
@Preview(name = "Loading", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AlarmDetailScreenPreview2() {
	val alarmDetailState = AlarmDetailState.Loading

	AlarmDetailScreen(alarmDetailState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmName(
	alarmName: String = "",
	onAlarmNameEdited: (String) -> Unit = {},
	enabled: Boolean = true
) {
	Column(
		Modifier
			.fillMaxWidth()
			.padding(horizontal = dimensionResource(R.dimen.common_horizontal_space))
	) {
		Text(
			text = stringResource(R.string.alarm_name),
			style = MaterialTheme.typography.titleSmall
		)
		
		Spacer(modifier = Modifier.height(5.dp))

		TextField(
			value = alarmName,
			onValueChange = onAlarmNameEdited,
			textStyle = MaterialTheme.typography.bodyLarge,
			enabled = enabled,
			modifier = Modifier.fillMaxWidth()
		)
	}
}

@Composable
private fun AlarmTime(
	alarmTime: LocalTime = LocalTime.of(13, 0, 0),
	onAlarmTimeChanged: (LocalTime) -> Unit = {}
) {
	val timeDialogState = rememberMaterialDialogState()

	Column(
		Modifier
			.fillMaxWidth()
			.padding(horizontal = dimensionResource(R.dimen.common_horizontal_space))
	) {
		Text(
			text = stringResource(R.string.alarm_time),
			style = MaterialTheme.typography.titleSmall
		)

		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				text = alarmTime.formatWithLocale(DateTimeFormatters.ALARM_TIME_AM_PM),
				style = MaterialTheme.typography.titleLarge,
				modifier = Modifier.clickable {
					timeDialogState.show()
				}
			)
			IconButton(onClick = { timeDialogState.show() }) {
				Image(
					imageVector = Icons.Default.Edit,
					contentDescription = stringResource(R.string.edit_alarm_time)
				)
			}
		}
	}

	MaterialDialog(
		dialogState = timeDialogState,
		buttons = {
			positiveButton(text = "OK") {

			}
			negativeButton(text = "Cancel") {

			}
		}
	) {
		timepicker(
			initialTime = alarmTime
		) {
			onAlarmTimeChanged(it)
		}
	}
}

@Composable
private fun AlarmDetailBottomMenu(
	onCancelClick: () -> Unit = {},
	onSaveClick: () -> Unit = {},
	onDeleteClick: () -> Unit = {}
) {
	Row(
		horizontalArrangement = Arrangement.SpaceEvenly,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight()
			.padding(vertical = dimensionResource(R.dimen.common_horizontal_space))
	) {
		BottomMenuButton(
			onClick = onDeleteClick,
			text = stringResource(R.string.delete_alarm)
		)
		BottomMenuButton(
			onClick = onCancelClick,
			text = stringResource(R.string.cancel_alarm_edit),
		)
		BottomMenuButton(
			onClick = onSaveClick,
			text = stringResource(R.string.save_alarm),
		)
	}
}

@Composable
private fun BottomMenuButton(
	text: String = "",
	onClick: () -> Unit = {}
) {
	Button(
		onClick = onClick,
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.background,
			contentColor = contentColorFor(MaterialTheme.colorScheme.background)
		)
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.labelLarge
		)
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AlarmDetailBottomMenuPreview() {
	Box {
		AlarmDetailBottomMenu()
	}
}