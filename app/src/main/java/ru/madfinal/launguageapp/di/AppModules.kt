package ru.madfinal.launguageapp.di

import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.madfinal.lastweeksproject.data.datasource.network.retrofit.SupabaseNetworkModule
import ru.madfinal.launguageapp.data.auth.AuthDataSource
import ru.madfinal.launguageapp.data.auth.repository.AuthRepositoryImpl
import ru.madfinal.launguageapp.data.datasource.locale.UserPreferences
import ru.madfinal.launguageapp.data.datasource.network.yandex.YandexSpeechApi
import ru.madfinal.launguageapp.data.exercise.animals.AnimalRepositoryImpl
import ru.madfinal.launguageapp.data.exercise.speech.YandexSpeechRecognitionRepository
import ru.madfinal.launguageapp.data.exercise.word.WordRepositoryImpl
import ru.madfinal.launguageapp.data.exercise.wordPractice.WordPracticeRepositoryImpl
import ru.madfinal.launguageapp.data.main.repository.MainRepositoryImpl
import ru.madfinal.launguageapp.data.main.user.UserRepositoryImpl
import ru.madfinal.launguageapp.data.mapper.UserMapper
import ru.madfinal.launguageapp.domain.auth.UserRepository
import ru.madfinal.launguageapp.domain.auth.repository.AuthRepository
import ru.madfinal.launguageapp.domain.auth.usecase.LoginUseCase
import ru.madfinal.launguageapp.domain.auth.usecase.SignupUseCase
import ru.madfinal.launguageapp.domain.exercise.animals.repository.AnimalRepository
import ru.madfinal.launguageapp.domain.exercise.animals.usecase.GetRandomAnimalUseCase
import ru.madfinal.launguageapp.domain.exercise.speech.SpeechRecognitionRepository
import ru.madfinal.launguageapp.domain.exercise.word.WordRepository
import ru.madfinal.launguageapp.domain.exercise.wordPractice.repository.WordPracticeRepository
import ru.madfinal.launguageapp.domain.exercise.wordPractice.usecase.GetRandomWordUseCase
import ru.madfinal.launguageapp.domain.main.repository.MainRepository
import ru.madfinal.launguageapp.presentation.auth.login.LoginViewModel
import ru.madfinal.launguageapp.presentation.auth.signup.SignupViewModel
import ru.madfinal.launguageapp.presentation.exercise.animals.AnimalsViewModel
import ru.madfinal.launguageapp.presentation.exercise.animals.TensorFlowHelper
import ru.madfinal.launguageapp.presentation.exercise.listen.ListeningViewModel
import ru.madfinal.launguageapp.presentation.exercise.wordpractice.WordPracticeViewModel
import ru.madfinal.launguageapp.presentation.main.MainViewModel
import java.util.concurrent.TimeUnit

val networkModule = module {
    // Добавляем URL для Supabase
    single { "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1uZ2Npc3lqbXZibWVibmxhcGh0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzAxOTAyMDIsImV4cCI6MjA0NTc2NjIwMn0.MFGhx41ZSshllA0x677E_sy3nFOcjZG-HFuTNE8yBz4" as String }

    // Добавляем OkHttpClient для Supabase
    single(qualifier = org.koin.core.qualifier.named("supabaseClient")) {
        SupabaseNetworkModule.provideOkHttpClient()
    }

    // Создаем Retrofit для Supabase
    single {
        SupabaseNetworkModule.provideRetrofit(
            get(),
            get(qualifier = org.koin.core.qualifier.named("supabaseClient"))
        )
    }

    // Создаем SupabaseApi
    single {
        SupabaseNetworkModule.provideSupabaseApi(get())
    }

    single { AuthDataSource(get()) }

    // SharedPreferences
    single<SharedPreferences> {
        androidContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    // Yandex Speech API - отдельный клиент OkHttp с заголовками для Yandex
    single(qualifier = org.koin.core.qualifier.named("yandexClient")) {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Api-Key AQVN2OI-O2jQiG1rUH6HluhDiL7oj7my4HIz-STr")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    // Retrofit для Yandex Speech API
    single {
        Retrofit.Builder()
            .baseUrl("https://stt.api.cloud.yandex.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(get(qualifier = org.koin.core.qualifier.named("yandexClient")))
            .build()
            .create(YandexSpeechApi::class.java)
    }
}

val repositoryModule = module {
    single<AnimalRepository> { AnimalRepositoryImpl(get(), get()) }
    single<WordPracticeRepository> { WordPracticeRepositoryImpl(get(), get()) }
    single<MainRepository> { MainRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<WordRepository> { WordRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single { UserMapper() }

    single<SpeechRecognitionRepository> { YandexSpeechRecognitionRepository(get()) }
}

val useCaseModule = module {
    factory { GetRandomWordUseCase(get()) }
    factory { GetRandomAnimalUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { SignupUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { WordPracticeViewModel(get()) }
    viewModel { AnimalsViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SignupViewModel(get()) }
    viewModel { ListeningViewModel(get(), get(), get()) }
}

val helperModule = module {
    single { TensorFlowHelper(androidContext()) }
}

val preferencesModule = module {
    single { UserPreferences(androidContext()) }
}

val appModules = listOf(
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    helperModule,
    preferencesModule
)