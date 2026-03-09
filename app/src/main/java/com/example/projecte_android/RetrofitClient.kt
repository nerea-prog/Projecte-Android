package com.example.projecte_android

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private var mTaskApi: TaskApiService? = null

        @Synchronized
        fun getApi(): TaskApiService {
            if (mTaskApi == null) {

                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

                mTaskApi = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("http://129.213.48.249:8081/")
                    .build()
                    .create(TaskApiService::class.java)
            }
            return mTaskApi!!
        }
    }
}