package com.jingom.simplealarmmanager.alarm

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.jingom.simplealarmmanager.R
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
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
		Log.d("AlarmNotifyWorker", "AlarmNotifyWorker called")
		val alarmId = params.inputData.getLong(KEY_ALARM_ID, -1L)
		if (alarmId == -1L) {
			Log.d("AlarmNotifyWorker", "alarmId is -1")
			return Result.success()
		}

		val result = runCatching {
			val alarm = alarmRepository.get(alarmId) ?: run {
				Log.d("AlarmNotifyWorker", "alarm is null")
				return Result.success()
			}
			alarmNotificationManager.notify(alarm)

			appAlarmManager.registerAlarm(
				alarm,
				LocalDate.now().plusDays(1)
			)

			val audioManager = ContextCompat.getSystemService(applicationContext, AudioManager::class.java)
			audioManager?.setStreamVolume(
				AudioManager.STREAM_MUSIC,
				audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).times(0.8).toInt(),
				0
			)

			val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.snowfall_butterflies)
			mediaPlayer.start()

			delay(180000L)

			mediaPlayer.release()
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

		private fun getUniqueWorkName(alarmId: Long) = "$ALARM_NOTIFY: $alarmId"

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
					getUniqueWorkName(alarmId),
					ExistingWorkPolicy.KEEP,
					workRequest
				)
		}

		fun cancelWork(alarmId: Long, context: Context) {
			WorkManager
				.getInstance(context)
				.cancelUniqueWork(
					getUniqueWorkName(alarmId)
				)
		}
	}
}