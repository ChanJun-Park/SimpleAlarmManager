package com.jingom.simplealarmmanager.alarm

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@HiltWorker
class AlarmNotifyWorker @AssistedInject constructor(
	@Assisted appContext: Context,
	@Assisted private val params: WorkerParameters,
	private val alarmNotificationManager: AlarmNotificationManager,
	private val alarmRepository: AlarmRepository,
	private val appAlarmManager: AppAlarmManager
) : CoroutineWorker(appContext, params) {

	override suspend fun doWork(): Result {
		val alarmId = params.inputData.getLong(KEY_ALARM_ID, -1L)
		if (alarmId == -1L) {
			return Result.success()
		}

		val result = runCatching {
			val alarm = alarmRepository.get(alarmId) ?: return Result.success()
			alarmNotificationManager.notify(alarm)

			appAlarmManager.registerAlarm(
				alarm,
				LocalDate.now().plusDays(1)
			)
		}

		return if (result.isFailure) {
			Result.failure()
		} else {
			Result.success()
		}
	}

	companion object {
		const val KEY_ALARM_ID = "alarmId"
		private const val ALARM_NOTIFY = "alarmNotify"

		fun schedule(alarmId: Long, context: Context) {
			val inputData = Data.Builder()
				.putLong(KEY_ALARM_ID, alarmId)
				.build()

			val workRequest = OneTimeWorkRequestBuilder<AlarmNotifyWorker>()
				.setBackoffCriteria(
					BackoffPolicy.LINEAR,
					10,
					TimeUnit.SECONDS
				)
				.setInputData(inputData)
				.build()

			WorkManager
				.getInstance(context)
				.enqueueUniqueWork(
					ALARM_NOTIFY,
					ExistingWorkPolicy.KEEP,
					workRequest
				)
		}
	}
}