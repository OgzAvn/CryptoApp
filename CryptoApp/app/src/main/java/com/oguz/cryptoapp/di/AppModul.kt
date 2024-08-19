package com.oguz.cryptoapp.di

import com.oguz.cryptoapp.repository.CryptoRepository
import com.oguz.cryptoapp.service.CryptoAPI
import com.oguz.cryptoapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModul {

    @Singleton
    @Provides
    fun provideCryptoApi() : CryptoAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(CryptoAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideCryptoRepository(api : CryptoAPI) = CryptoRepository(api)
    //TODO:Bunu yaptıgımız zaman repository mizi artık viewmodel içerisinde kullanabiliriz.

}


