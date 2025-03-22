package com.msdc.rentalwheels.modules

import com.google.firebase.firestore.FirebaseFirestore
import com.msdc.rentalwheels.data.repository.CarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideCarRepository(firestore: FirebaseFirestore): CarRepository {
        return CarRepository(firestore)
    }
}