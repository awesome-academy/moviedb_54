package com.sun.moviedb_54.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sun.moviedb_54.data.source.remote.APIService
import com.sun.moviedb_54.ultis.Constant
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {

    fun provideApi(retrofit: Retrofit) = retrofit.create(APIService::class.java)

    single { provideApi(get()) }
}

val retrofitModule = module {

    fun provideGson() = GsonBuilder().create()

    fun provideGsonConverterFactory(factory: Gson) = GsonConverterFactory.create(factory)

    fun provideHttpClient() = OkHttpClient.Builder().build()

    fun provideRetrofit(factory: GsonConverterFactory, client: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(factory)
            .client(client)
            .build()

    single { provideGson() }
    single { provideHttpClient() }
    single { provideGsonConverterFactory(get()) }
    single { provideRetrofit(get(), get()) }
}