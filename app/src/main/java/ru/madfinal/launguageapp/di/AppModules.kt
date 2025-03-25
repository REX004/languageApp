package ru.madfinal.launguageapp.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    // Добавляем URL для Supabase
//    single { "https://mngcisyjmvbmebnlapht.supabase.co" as String }
//
//    // Добавляем OkHttpClient
//    single { SupabaseNetworkModule.provideOkHttpClient() }
//
//    // Создаем Retrofit
//    single { SupabaseNetworkModule.provideRetrofit(get(), get()) }
//
//    // Создаем SupabaseApi
//    single { SupabaseNetworkModule.provideSupabaseApi(get()) }
//
//    // AuthDataSource
//    single { AuthDataSource(get()) }
//
//    // SharedPreferences
//    single<SharedPreferences> {
//        androidContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
//    }
}

val repositoryModule = module {
//    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
//    single { UserMapper() }
//    single<ExchangeRepository> { ExchangeRepositoryImpl(get(), get()) }
//    single<InterestRepository> { InterestRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
//    factory { LoginUseCase(get()) }
//    factory { GetExchangeRatesUseCase(get()) }
//    factory { GetInterestUseCase(get()) }
//    factory { GetExchangesUseCase(get()) }
}

val viewModelModule = module {
//    viewModel { LoginViewModel(get()) }
//    viewModel { ExchangeRateViewModel(get()) }
//    viewModel { InterestViewModel(get()) }
//    viewModel { ExchangeViewModel(get()) }
}

val appModules = listOf(
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)