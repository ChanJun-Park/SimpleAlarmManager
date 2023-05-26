package com.jingom.simplealarmmanager.presentation.home.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jingom.simplealarmmanager.presentation.home.AlarmHomeState

@Composable
fun AlarmListScreen(
	alarmHomeState: AlarmHomeState,
	viewModel: AlarmListViewModel = viewModel()
) {
	Column(Modifier.fillMaxSize()) {
		Text(text = "AlarmListScreen")
	}
}