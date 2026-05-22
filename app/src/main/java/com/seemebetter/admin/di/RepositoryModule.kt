package com.seemebetter.admin.di

import com.seemebetter.admin.repository.AuthRepository
import com.seemebetter.admin.repository.QuestionsRepository
import com.seemebetter.admin.repository.ResponsesRepository
import com.seemebetter.admin.repository.SettingsRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule

