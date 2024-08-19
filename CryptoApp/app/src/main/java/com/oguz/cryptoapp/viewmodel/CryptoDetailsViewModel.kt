package com.oguz.cryptoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.oguz.cryptoapp.model.Crypto
import com.oguz.cryptoapp.repository.CryptoRepository
import com.oguz.cryptoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CryptoDetailsViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

//TODO:Sadece 1 tane cryto dönecek
    /**
     * Burada fun ı suspend yaptık viewmodelscope.launch yapmadık farkını view içerisinde göreceğiz.
     */
    suspend fun getCrypto() : Resource<Crypto>{

        return repository.getCrypto()
    }

}