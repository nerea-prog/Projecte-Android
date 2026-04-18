package com.example.projecte_android.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Client Retrofit per connectar l'aplicació amb l'API REST de TaskBuddy.
 *
 * Implementa el patró Singleton mitjançant un companion object per garantir
 * que només existeixi una única instància de la connexió durant tota
 * l'execució de l'aplicació.
 */
class RetrofitClient {
    companion object {
        /** Instància única del servei de l'API. Null fins que es crida [getApi] per primera vegada. */
        private var mTaskApi: TaskApiService? = null

        /**
         * Retorna la instància única de [TaskApiService].
         *
         * Si no existeix cap instància, la crea configurant Gson amb format
         * de dates ISO 8601 i apuntant al servidor de l'API a oracle.
         *
         * Amb @Synchronized evitem que dos fils creïn la instància alhora.
         * @return La instància configurada de [TaskApiService].
         */
        @Synchronized
        fun getApi(): TaskApiService {
            if (mTaskApi == null) {

                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

                // Construïm Retrofit amb la URL base del servidor
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