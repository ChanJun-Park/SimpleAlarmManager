package com.jingom.simplealarmmanager.presentation.home.detail

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.simplealarmmanager.alarm.AlarmController
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.presentation.home.detail.AlarmDetailEditState.Companion.canEdit
import com.jingom.simplealarmmanager.presentation.home.detail.AlarmDetailEditState.Companion.canSave
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AlarmDetailViewModel @Inject constructor(
	private val alarmController: AlarmController
) : ViewModel() {

	private val _alarmDetailState = MutableStateFlow<AlarmDetailState>(AlarmDetailState.Loading)
	val alarmDetailState = _alarmDetailState.asStateFlow()

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
		_alarmDetailState.update {
			AlarmDetailState.Success(
				alarm = Alarm(),
				editState = AlarmDetailEditState.Initialized
			)
		}
	}

	private fun setEditAlarmDetailState(alarmId: Long) {
		viewModelScope.launch {
			val alarm = alarmController.get(alarmId)

			_alarmDetailState.update {
				if (alarm != null) {
					AlarmDetailState.Success(
						alarm = alarm,
						editState = AlarmDetailEditState.Initialized
					)
				} else {
					AlarmDetailState.Fail
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
		val currentDetailState = _alarmDetailState.value
		if (currentDetailState !is AlarmDetailState.Success) {
			return
		}

		if (currentDetailState.editState.canEdit().not()) {
			return
		}

		if (newAlarmName == currentDetailState.alarm.name) {
			return
		}

		_alarmDetailState.update {
			currentDetailState.copy(
				alarm = currentDetailState.alarm.copy(
					name = newAlarmName
				),
				editState = AlarmDetailEditState.Edited
			)
		}
	}

	fun saveAlarm() {
		val currentDetailState = _alarmDetailState.value
		val currentAlarmName = alarmNameForTextField.value
		if (currentDetailState !is AlarmDetailState.Success) {
			return
		}

		if (currentDetailState.editState.canSave().not()) {
			return
		}

		val editedAlarm = currentDetailState.alarm.copy(
			name = currentAlarmName
		)

		viewModelScope.launch {
			alarmController.insert(editedAlarm)
			_alarmDetailState.update {
				currentDetailState.copy(
					editState = AlarmDetailEditState.Saved
				)
			}
		}
	}

	fun deleteAlarm() {
		val currentDetailState = _alarmDetailState.value
		if (currentDetailState !is AlarmDetailState.Success) {
			return
		}

		viewModelScope.launch {
			alarmController.delete(currentDetailState.alarm)
			_alarmDetailState.update {
				currentDetailState.copy(
					editState = AlarmDetailEditState.Deleted
				)
			}
		}
	}

	fun changeAlarmTime(localTime: LocalTime) {
		val currentDetailState = _alarmDetailState.value
		if (currentDetailState !is AlarmDetailState.Success) {
			return
		}

		if (currentDetailState.editState.canEdit().not()) {
			return
		}

		_alarmDetailState.update {
			currentDetailState.copy(
				alarm = currentDetailState.alarm.copy(
					time = localTime
				),
				editState = AlarmDetailEditState.Edited
			)
		}
	}
}