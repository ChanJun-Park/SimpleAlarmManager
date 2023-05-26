package com.jingom.simplealarmmanager.presentation.home.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jingom.simplealarmmanager.presentation.home.AlarmHomeState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AlarmDetailScreen(
	alarmId: Long,
	alarmHomeState: AlarmHomeState,
	viewModel: AlarmDetailViewModel = viewModel()
) {
	Column(Modifier.fillMaxSize()) {
		Text(text = "AlarmDetailScreen")
	}
}