package ec.edu.uisek.githubclient.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ec.edu.uisek.githubclient.GitHubConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val BASE_URL = "https://api.github.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val token = GitHubConfig.GITHUB_TOKEN
            println("Token es Vacío? ${token.isEmpty()}")

            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}