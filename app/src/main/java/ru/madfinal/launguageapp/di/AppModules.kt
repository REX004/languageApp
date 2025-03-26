package ru.madfinal.launguageapp.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.madfinal.lastweeksproject.data.datasource.network.retrofit.SupabaseNetworkModule
import ru.madfinal.launguageapp.data.exercise.wordPractice.WordPracticeRepositoryImpl
import ru.madfinal.launguageapp.data.main.repository.MainRepositoryImpl
import ru.madfinal.launguageapp.domain.exercise.wordPractice.repository.WordPracticeRepository
import ru.madfinal.launguageapp.domain.exercise.wordPractice.usecase.GetRandomWordUseCase
import ru.madfinal.launguageapp.domain.main.MainRepository
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
//    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
//    single { UserMapper() }
    single<WordPracticeRepository> { WordPracticeRepositoryImpl(get(), get()) }
    single<MainRepository> { MainRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
//    factory { LoginUseCase(get()) }
    factory { GetRandomWordUseCase(get()) }
//    factory { GetInterestUseCase(get()) }
//    factory { GetExchangesUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { WordPracticeViewModel(get()) }
//    viewModel { InterestViewModel(get()) }
//    viewModel { ExchangeViewModel(get()) }
}

val appModules = listOf(
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)