package com.jingom.simplealarmmanager.common

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext

interface BootReceiverManager {
	val bootReceiverEnabled: Boolean
	fun enableReceiver()
	fun disableReceiver()
}

class DefaultBootReceiverManager(
	@ApplicationContext
	private val applicationContext: Context
) : BootReceiverManager {

	override val bootReceiverEnabled: Boolean
		get() {
			val receiver = ComponentName(applicationContext, BootReceiver::class.java)
			return applicationContext.packageManager.getComponentEnabledSetting(receiver) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
		}

	override fun enableReceiver() {
		val receiver = ComponentName(applicationContext, BootReceiver::class.java)

		applicationContext.packageManager.setComponentEnabledSetting(
			receiver,
			PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
			PackageManager.DONT_KILL_APP
		)
	}

	override fun disableReceiver() {
		val receiver = ComponentName(applicationContext, BootReceiver::class.java)

		applicationContext.packageManager.setComponentEnabledSetting(
			receiver,
			PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
			PackageManager.DONT_KILL_APP
		)
	}
}