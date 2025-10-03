package br.com.fiap.softtek.factory

import AuthInterceptor
import android.content.Context
import br.com.fiap.softtek.services.SofttekMapService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    fun getRetrofit(context: Context): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getSofttekMapService(context: Context): SofttekMapService {
        return getRetrofit(context).create(SofttekMapService::class.java)
    }
}