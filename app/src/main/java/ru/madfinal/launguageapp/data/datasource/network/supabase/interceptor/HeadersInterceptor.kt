package ru.madfinal.lastweeksproject.data.datasource.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.madfinal.lastweeksproject.data.datasource.network.config.NetworkConfig

class HeadersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
//            .addHeader("apiKey", NetworkConfig.API_KEY)
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}