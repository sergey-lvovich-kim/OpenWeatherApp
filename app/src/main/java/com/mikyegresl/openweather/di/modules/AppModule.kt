package com.mikyegresl.openweather.di

import android.app.Application
import androidx.room.Room
import com.mikyegresl.openweather.Constants
import com.mikyegresl.openweather.data.CitiesRepository
import com.mikyegresl.openweather.data.CitiesRepositoryImpl
import com.mikyegresl.openweather.data.WeatherRepository
import com.mikyegresl.openweather.data.WeatherRepositoryImpl
import com.mikyegresl.openweather.data.local.CitiesDao
import com.mikyegresl.openweather.data.local.Database
import com.mikyegresl.openweather.data.local.Prefs
import com.mikyegresl.openweather.data.local.WeatherDao
import com.mikyegresl.openweather.data.remote.OpenWeatherApi
import com.mikyegresl.openweather.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule(private val application: Application) {
    @Provides
    fun application(): Application = application

    @Provides
    @AppScope
    fun prefs(application: Application): Prefs = Prefs(application)

    @Provides
    @AppScope
    fun database(application: Application): Database {
        return Room.databaseBuilder(application, Database::class.java, Database.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @AppScope
    fun citiesDao(application: Application): CitiesDao = database(application).citiesDao()

    @Provides
    @AppScope
    fun weatherDao(application: Application): WeatherDao = database(application).weatherDao()

    @Provides
    @AppScope
    fun citiesRepository(citiesDao: CitiesDao): CitiesRepository =
        CitiesRepositoryImpl(citiesDao)

    @Provides
    @AppScope
    fun weatherRepository(weatherDao: WeatherDao, weatherApi: OpenWeatherApi): WeatherRepository =
        WeatherRepositoryImpl(weatherDao, weatherApi)

    @Provides
    @AppScope
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @AppScope
    fun httpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @AppScope
    fun retrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.API_BASE_URL)
            .build()

    }

    @Provides
    @AppScope
    fun openWeatherApi(retrofit: Retrofit): OpenWeatherApi {
        return retrofit.create(OpenWeatherApi::class.java)
    }
}