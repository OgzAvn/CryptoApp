package com.oguz.cryptoapp.repository

import com.oguz.cryptoapp.model.Crypto
import com.oguz.cryptoapp.model.CryptoList
import com.oguz.cryptoapp.service.CryptoAPI
import com.oguz.cryptoapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped //TODO:Tek aktivite var aktivite çalıştığı sürece bu sınıf geçerli
class CryptoRepository @Inject constructor(

    private val api : CryptoAPI

) {

    suspend fun getCryptoList() : Resource<CryptoList> {

        val response = try {

            api.getCryptoList()


        }catch (e : Exception){

            return Resource.Error("Error")
        }

        return Resource.Success(response)

    }


    suspend fun getCrypto() : Resource<Crypto>{

        val response = try {
            api.getCrypto()
        }catch (e:Exception){
            return Resource.Error("error")
        }

        return Resource.Success(response)
    }

}