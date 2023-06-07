package com.jingom.simplealarmmanager.presentation.home.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.simplealarmmanager.alarm.AlarmController
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmListViewModel @Inject constructor(
	private val alarmController: AlarmController
) : ViewModel() {

	private var initialized: Boolean = false

	private val _alarmListState = MutableStateFlow<AlarmListState>(AlarmListState.Loading)
	val alarmListState = _alarmListState.asStateFlow()

	fun init() {
		if (initialized) {
			return
		}

		initAlarmListState()

		initialized = true
	}

	private fun initAlarmListState() {
		viewModelScope.launch {
			alarmController
				.getAllAlarmFlow()
				.collectLatest { alarmList ->
					_alarmListState.update {
						AlarmListState.Success(alarmList)
					}
				}
		}
	}

	fun alarmOnToggle(alarm: Alarm) {
		viewModelScope.launch {
			alarmController.upsert(
				alarm.copy(
					alarmOn = alarm.alarmOn.not()
				)
			)
		}
	}
}