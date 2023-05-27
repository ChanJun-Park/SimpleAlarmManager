package com.jingom.simplealarmmanager.presentation.home.list

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jingom.simplealarmmanager.R
import com.jingom.simplealarmmanager.common.date.DateTimeFormatters
import com.jingom.simplealarmmanager.common.date.formatWithLocale
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.presentation.home.AlarmHomeState
import java.time.LocalTime

@Composable
fun AlarmListScreen(
	alarmHomeState: AlarmHomeState,
	viewModel: AlarmListViewModel = hiltViewModel()
) {
	val alarmListState by viewModel.alarmListState.collectAsStateWithLifecycle()

	AlarmListScreen(
		alarmListState = alarmListState,
		onAlarmClick = alarmHomeState::navigateToDetail,
		onAddAlarmClick = alarmHomeState::navigateToAdd,
		onAlarmOnToggle = viewModel::alarmOnToggle,
	)

	LaunchedEffect(key1 = true) {
		viewModel.init()
	}
}

@Composable
fun AlarmListScreen(
	alarmListState: AlarmListState,
	onAlarmClick: (Alarm) -> Unit = {},
	onAddAlarmClick: () -> Unit = {},
	onAlarmOnToggle: (Alarm) -> Unit = {}
) {
	Column(Modifier.fillMaxSize()) {
		AlarmListHeader(onAddAlarmClick = onAddAlarmClick)
		when (alarmListState) {
			is AlarmListState.Loading -> AlarmListLoading()
			is AlarmListState.Success -> AlarmListLoaded(
				alarmList = alarmListState.alarmList,
				onAlarmClick = onAlarmClick,
				onAlarmOnToggle = onAlarmOnToggle
			)
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AlarmListScreenPreview() {
	val alarmList = listOf(
		Alarm(
			id = 1,
			name = "test",
			time = LocalTime.of(1, 0, 0)
		),
		Alarm(
			id = 2,
			name = "test",
			time = LocalTime.of(2, 10, 0)
		),
		Alarm(
			id = 3,
			name = "test",
			time = LocalTime.of(3, 50, 0)
		),
		Alarm(
			id = 4,
			name = "test",
			time = LocalTime.of(13, 7, 0)
		),
		Alarm(
			id = 5,
			name = "test",
			time = LocalTime.of(18, 13, 0)
		)
	)
	val alarmListState = AlarmListState.Success(
		alarmList = alarmList
	)
	Surface(
		color = Color.Gray.copy(alpha = 0.1f),
		modifier = Modifier.fillMaxSize()
	) {
		AlarmListScreen(
			alarmListState = alarmListState
		)
	}
}

@Composable
private fun AlarmListLoading() {
	Box(Modifier.fillMaxSize()) {
		CircularProgressIndicator(
			modifier = Modifier.size(100.dp)
		)
	}
}

@Composable
private fun AlarmListEmpty() {
	Box(Modifier.fillMaxSize()) {
		Text(
			text = stringResource(R.string.alarm_empty),
			style = MaterialTheme.typography.titleLarge,
			modifier = Modifier.align(Alignment.Center)
		)
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AlarmListEmptyPreview() {
	Surface(Modifier.fillMaxSize()) {
		AlarmListEmpty()
	}
}

@Composable
private fun AlarmListLoaded(
	alarmList: List<Alarm>,
	onAlarmClick: (Alarm) -> Unit = {},
	onAlarmOnToggle: (Alarm) -> Unit = {}
) {
	if (alarmList.isEmpty()) {
		AlarmListEmpty()
	} else {
		AlarmList(
			alarmList = alarmList,
			onAlarmClick = onAlarmClick,
			onAlarmOnToggle = onAlarmOnToggle
		)
	}
}

@Composable
private fun AlarmList(
	alarmList: List<Alarm>,
	onAlarmClick: (Alarm) -> Unit = {},
	onAlarmOnToggle: (Alarm) -> Unit = {}
) {
	LazyColumn(
		contentPadding = PaddingValues(
			horizontal = dimensionResource(R.dimen.common_horizontal_space),
			vertical = 5.dp
		),
		modifier = Modifier.fillMaxSize()
	) {
		items(
			items = alarmList,
			key = { alarm -> alarm.id }
		) { alarm ->
			AlarmItem(
				alarm = alarm,
				onAlarmClick = onAlarmClick,
				onAlarmOnToggle = onAlarmOnToggle,
				modifier = Modifier.padding(vertical = 5.dp)
			)
		}
	}
}

@Composable
private fun AlarmItem(
	alarm: Alarm,
	modifier: Modifier = Modifier,
	onAlarmClick: (Alarm) -> Unit = {},
	onAlarmOnToggle: (Alarm) -> Unit = {}
) {
	Surface(
		shape = RoundedCornerShape(10.dp),
		modifier = modifier
			.fillMaxWidth()
			.wrapContentHeight()
			.clickable {
				onAlarmClick(alarm)
			}
	) {
		Row(
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(
				horizontal = 10.dp,
				vertical = 10.dp
			)
		) {
			Column {
				if (alarm.name.isNotEmpty()) {
					Text(
						text = alarm.name,
						style = MaterialTheme.typography.labelSmall,
						modifier = Modifier.padding(bottom = 5.dp)
					)
				}
				Text(
					text = alarm.time.formatWithLocale(DateTimeFormatters.ALARM_TIME_AM_PM),
					style = MaterialTheme.typography.titleLarge
				)
			}
			Switch(
				checked = alarm.alarmOn,
				onCheckedChange = { onAlarmOnToggle(alarm) }
			)
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AlarmItemPreview() {
	val alarm = Alarm(
		id = 1,
		name = "아침 기상",
		time = LocalTime.of(8, 0, 0),
		true
	)
	var test by remember {
		mutableStateOf(alarm)
	}
	Surface(color = Color.Black) {
		AlarmItem(
			alarm = test,
			onAlarmOnToggle = {
				test = test.copy(alarmOn = test.alarmOn.not())
			}
		)
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AlarmListPreview() {
	val alarmList = listOf(
		Alarm(
			id = 1,
			name = "test",
			time = LocalTime.of(1, 0, 0)
		),
		Alarm(
			id = 2,
			name = "test",
			time = LocalTime.of(2, 10, 0)
		),
		Alarm(
			id = 3,
			name = "test",
			time = LocalTime.of(3, 50, 0)
		),
		Alarm(
			id = 4,
			name = "test",
			time = LocalTime.of(13, 7, 0)
		),
		Alarm(
			id = 5,
			name = "test",
			time = LocalTime.of(18, 13, 0)
		)
	)

	Surface(
		color = Color.Gray
	) {
		AlarmListLoaded(
			alarmList = alarmList
		)
	}
}

@Composable
private fun AlarmListHeader(
	onAddAlarmClick: () -> Unit = {}
) {
	Row(
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = dimensionResource(R.dimen.common_horizontal_space)
			)
			.padding(
				top = dimensionResource(R.dimen.common_horizontal_space)
			)
	) {
		Text(
			text = stringResource(R.string.alarm_list),
			style = MaterialTheme.typography.headlineMedium
		)
		IconButton(onClick = onAddAlarmClick) {
			Image(
				imageVector = Icons.Default.Add,
				contentDescription = stringResource(R.string.add_alarm)
			)
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AlarmListHeaderPreview() {
	Surface {
		AlarmListHeader()
	}
}