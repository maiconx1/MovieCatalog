package br.com.codz.moviecatalog.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val bearerToken: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest =
            originalRequest
                .newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $bearerToken")
                .build()

        return chain.proceed(newRequest)
    }
}
