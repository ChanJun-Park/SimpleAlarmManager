package com.jingom.simplealarmmanager.alarm

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class AppAlarmRecoverWorker @AssistedInject constructor(
	@Assisted appContext: Context,
	@Assisted params: WorkerParameters,
	private val alarmController: AlarmController
) : CoroutineWorker(appContext, params) {

	override suspend fun doWork(): Result {
		val result = runCatching {
			alarmController.recoverAllAlarm()
		}

		return if (result.isFailure) {
			Result.failure()
		} else {
			Result.success()
		}
	}

	companion object {
		private const val APP_ALARM_RECOVER = "appAlarmRecover"

		fun schedule(context: Context) {
			val workRequest = OneTimeWorkRequestBuilder<AppAlarmRecoverWorker>()
				.setBackoffCriteria(
					BackoffPolicy.LINEAR,
					10,
					TimeUnit.SECONDS
				)
				.build()

			WorkManager
				.getInstance(context)
				.enqueueUniqueWork(
					APP_ALARM_RECOVER,
					ExistingWorkPolicy.KEEP,
					workRequest
				)
		}
	}
}