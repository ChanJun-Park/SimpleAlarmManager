package com.jingom.simplealarmmanager.common

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CommonModule {

	companion object {
		@Provides
		@Singleton
		fun provideBootReceiverManager(
			@ApplicationContext
			applicationContext: Context
		): BootReceiverManager {
			return DefaultBootReceiverManager(applicationContext)
		}
	}
}