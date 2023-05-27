package com.jingom.simplealarmmanager.presentation.home.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmListViewModel @Inject constructor(
	private val alarmRepository: AlarmRepository
): ViewModel() {

	private var initialized: Boolean = false

	private val _alarmListState = MutableStateFlow<AlarmListState>(AlarmListState.Loading)
	val alarmListState = _alarmListState.asStateFlow()

	fun init() {
		if (initialized) {
			return
		}

		initAlarmListState()
	}

	private fun initAlarmListState() {
		viewModelScope.launch {
			alarmRepository
				.getAllAlarmFlow()
				.collectLatest { alarmList ->
					_alarmListState.update {
						AlarmListState.Success(alarmList)
					}
				}
		}
	}

	fun alarmOnToggle(alarm: Alarm) {
	}
}