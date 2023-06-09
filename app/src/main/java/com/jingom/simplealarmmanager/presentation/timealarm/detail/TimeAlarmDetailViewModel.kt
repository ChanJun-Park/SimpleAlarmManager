package com.jingom.simplealarmmanager.presentation.timealarm.detail

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.simplealarmmanager.alarm.AlarmController
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.presentation.timealarm.detail.TimeAlarmDetailEditState.Companion.canEdit
import com.jingom.simplealarmmanager.presentation.timealarm.detail.TimeAlarmDetailEditState.Companion.canSave
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TimeAlarmDetailViewModel @Inject constructor(
	private val alarmController: AlarmController
) : ViewModel() {

	private val _timeAlarmDetailState = MutableStateFlow<TimeAlarmDetailState>(TimeAlarmDetailState.Loading)
	val timeAlarmDetailState = _timeAlarmDetailState.asStateFlow()

	var alarmNameForTextField = mutableStateOf("")

	private var initialized: Boolean = false

	fun init(alarmId: Long?) {
		if (initialized) {
			return
		}

		initAlarmDetailState(alarmId)

		collectAlarmNameEditFlow()

		initialized = true
	}

	private fun initAlarmDetailState(alarmId: Long?) {
		if (alarmId == null) {
			setNewAlarmDetailState()
		} else {
			setEditAlarmDetailState(alarmId)
		}
	}

	private fun setNewAlarmDetailState() {
		_timeAlarmDetailState.update {
			TimeAlarmDetailState.Success(
				alarm = Alarm(),
				editState = TimeAlarmDetailEditState.Initialized
			)
		}
	}

	private fun setEditAlarmDetailState(alarmId: Long) {
		viewModelScope.launch {
			val alarm = alarmController.get(alarmId)

			_timeAlarmDetailState.update {
				if (alarm != null) {
					TimeAlarmDetailState.Success(
						alarm = alarm,
						editState = TimeAlarmDetailEditState.Initialized
					)
				} else {
					TimeAlarmDetailState.Fail
				}
			}

			alarm?.name?.let {
				alarmNameForTextField.value = it
			}
		}
	}

	private fun collectAlarmNameEditFlow() {
		viewModelScope.launch {
			snapshotFlow {
				alarmNameForTextField
			}.collectLatest { newAlarmName ->
				updateAlarmNameInState(newAlarmName.value)
			}
		}
	}

	private fun updateAlarmNameInState(newAlarmName: String) {
		val currentDetailState = _timeAlarmDetailState.value
		if (currentDetailState !is TimeAlarmDetailState.Success) {
			return
		}

		if (currentDetailState.editState.canEdit().not()) {
			return
		}

		if (newAlarmName == currentDetailState.alarm.name) {
			return
		}

		_timeAlarmDetailState.update {
			currentDetailState.copy(
				alarm = currentDetailState.alarm.copy(
					name = newAlarmName
				),
				editState = TimeAlarmDetailEditState.Edited
			)
		}
	}

	fun saveAlarm() {
		val currentDetailState = _timeAlarmDetailState.value
		val currentAlarmName = alarmNameForTextField.value
		if (currentDetailState !is TimeAlarmDetailState.Success) {
			return
		}

		if (currentDetailState.editState.canSave().not()) {
			return
		}

		val editedAlarm = currentDetailState.alarm.copy(
			name = currentAlarmName
		)

		viewModelScope.launch {
			alarmController.upsert(editedAlarm)
			_timeAlarmDetailState.update {
				currentDetailState.copy(
					editState = TimeAlarmDetailEditState.Saved
				)
			}
		}
	}

	fun deleteAlarm() {
		val currentDetailState = _timeAlarmDetailState.value
		if (currentDetailState !is TimeAlarmDetailState.Success) {
			return
		}

		viewModelScope.launch {
			alarmController.delete(currentDetailState.alarm)
			_timeAlarmDetailState.update {
				currentDetailState.copy(
					editState = TimeAlarmDetailEditState.Deleted
				)
			}
		}
	}

	fun changeAlarmTime(localTime: LocalTime) {
		val currentDetailState = _timeAlarmDetailState.value
		if (currentDetailState !is TimeAlarmDetailState.Success) {
			return
		}

		if (currentDetailState.editState.canEdit().not()) {
			return
		}

		_timeAlarmDetailState.update {
			currentDetailState.copy(
				alarm = currentDetailState.alarm.copy(
					time = localTime
				),
				editState = TimeAlarmDetailEditState.Edited
			)
		}
	}
}