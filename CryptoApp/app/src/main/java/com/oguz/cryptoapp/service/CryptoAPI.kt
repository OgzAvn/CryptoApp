package com.oguz.cryptoapp.service

import com.oguz.cryptoapp.model.Crypto
import com.oguz.cryptoapp.model.CryptoList
import retrofit2.http.GET

interface CryptoAPI {


    @GET("atilsamancioglu/IA32-CryptoComposeData/main/cryptolist.json")
    suspend fun getCryptoList(): CryptoList

    @GET("atilsamancioglu/IA32-CryptoComposeData/main/crypto.json")
    suspend fun getCrypto() : Crypto
}