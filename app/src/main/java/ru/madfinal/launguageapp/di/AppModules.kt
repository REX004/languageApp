package ru.madfinal.launguageapp.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.madfinal.lastweeksproject.data.datasource.network.retrofit.SupabaseNetworkModule
import ru.madfinal.launguageapp.data.exercise.animals.AnimalRepositoryImpl
import ru.madfinal.launguageapp.data.exercise.wordPractice.WordPracticeRepositoryImpl
import ru.madfinal.launguageapp.data.main.repository.MainRepositoryImpl
import ru.madfinal.launguageapp.domain.exercise.animals.repository.AnimalRepository
import ru.madfinal.launguageapp.domain.exercise.animals.usecase.GetRandomAnimalUseCase
import ru.madfinal.launguageapp.domain.exercise.wordPractice.repository.WordPracticeRepository
import ru.madfinal.launguageapp.domain.exercise.wordPractice.usecase.GetRandomWordUseCase
import ru.madfinal.launguageapp.domain.main.MainRepository
import ru.madfinal.launguageapp.presentation.exercise.animals.AnimalsViewModel
import ru.madfinal.launguageapp.presentation.exercise.animals.TensorFlowHelper
import ru.madfinal.launguageapp.presentation.exercise.wordpractice.WordPracticeViewModel
import ru.madfinal.launguageapp.presentation.main.MainViewModel

val networkModule = module {
    // Добавляем URL для Supabase
    single { "https://mngcisyjmvbmebnlapht.supabase.co" as String }

    // Добавляем OkHttpClient
    single { SupabaseNetworkModule.provideOkHttpClient() }

    // Создаем Retrofit
    single { SupabaseNetworkModule.provideRetrofit(get(), get()) }

    // Создаем SupabaseApi
    single { SupabaseNetworkModule.provideSupabaseApi(get()) }

    // AuthDataSource
//    single { AuthDataSource(get()) }

    // SharedPreferences
    single<SharedPreferences> {
        androidContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }
}

val repositoryModule = module {
    single<AnimalRepository> { AnimalRepositoryImpl(get(), get()) }
    single<WordPracticeRepository> { WordPracticeRepositoryImpl(get(), get()) }
    single<MainRepository> { MainRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    factory { GetRandomWordUseCase(get()) }
    factory { GetRandomAnimalUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { WordPracticeViewModel(get()) }
    viewModel { AnimalsViewModel(get(), get()) }
}

val helperModule = module {
    single { TensorFlowHelper(androidContext()) }
}

val appModules = listOf(
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    helperModule
)