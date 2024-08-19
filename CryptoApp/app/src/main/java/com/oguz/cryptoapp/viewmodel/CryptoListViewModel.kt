package com.oguz.cryptoapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguz.cryptoapp.model.CryptoListItem
import com.oguz.cryptoapp.repository.CryptoRepository
import com.oguz.cryptoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel(){

    /**
     * LiveData yerine mutableStateof kullanabiliriz
     */

    var cryptoList = mutableStateOf<List<CryptoListItem>>(listOf()) //TODO:bu şuanlık boş ama CryptoListItem alacak demek
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)


    /**
     * indirdigim veriler uzerınden arama yapacagım ve sadece kalan sonuclar bana gosterılecek
     * bunun ıcın cryptoList i devamlı degısterecegız ama kullanıcı bısı yazdı ama sonra sıldı
     * her sıldıgınde bastan ınderemeyız cryptoları gerek yok zaten lıste elımde onuun ıcınde search yapıyorum
     * o sebeple ındırdıgım verıyı initialCryptoList buaraya kaydedeceım sonra gerekırse burdan cekerım
     */
    private var initialCryptoList = listOf<CryptoListItem>()
    private var isSearchStarting = true



    //TODO : Searh bar için ilk önce verilecek inecek sonra o verilerden alacağız
    init {
        downloadCryptos() //TODO: O nedenle ilk olarak verileri alalım
    }


    fun searchCryptoList(query : String){

        val listToSearch = if (isSearchStarting){
            cryptoList.value //TODO: Eğer arama ilk defa yapılıyorsa burdan al
        }else
        {
            initialCryptoList
        }

        viewModelScope.launch(Dispatchers.Default) {
            //TODO:Dispatchers.Default -> CPU Intensive işlemeleri yapacagız o neden default da yapıyoruz.
            if (query.isEmpty()){ //TODO:Edittext boşmu diye bakıyorum
                cryptoList.value = initialCryptoList //TODO:Arama bitmiş artık kullanıcı sılmıs yazdıgını ılk basta ındırdıgım ne ıse onu ver diyorum
                isSearchStarting = true
                return@launch
            }

            //TODO:Eger bısey aranıyorusa sonucları alıp kullanıcıya gosterebılırım
            val result = listToSearch.filter {
                it.currency.contains(query.trim(),ignoreCase = true)
            }


            //TODO:işte verileri burada kaydedıyoruz ilk defa arama yapıyorsa
            if(isSearchStarting){
                initialCryptoList = cryptoList.value
                isSearchStarting = false
            }


            cryptoList.value = result //TODO: Arama sonuçlarını cryptoList.value'a atarız, böylece arama sonuçları kullanıcıya gösterilir.

        }
    }

    /**Coroutine kullanmak için ya bu fun ı suspend yapacaksın ya da "viewModelScope.launch " kullanakcaksın
     *
     */
    fun downloadCryptos(){

        viewModelScope.launch {
            isLoading.value = true

            val result = repository.getCryptoList()
            println("my data:"+result.data)
            when(result){

                is Resource.Success<*> -> {
                    //TODO:Eğer success ise zaten direk result ım resource içinde data yada message olacak
                    // Çünkü resource sınıfnı oyle yapmıstık eger hata mesajı varsa bana strıng verecek gerıye
                    //  eger yoksa cryptolist verecek.
                    val cryptoItems = result.data!!.mapIndexed { index, item ->
                        CryptoListItem(item.currency,item.price)
                    } as List<CryptoListItem>
                    /**
                     * Her bir item öğesi için CryptoListItem(item.currency, item.price) oluşturuyorsun.
                     * Bu, item nesnesinin currency ve price özelliklerini alarak yeni bir CryptoListItem oluşturur.
                     * result.data içinde dönen kripto verilerini alıp, CryptoListItem nesnelerine dönüştürdüm.
                     */
                    println("myitems"+cryptoItems)
                    errorMessage.value = ""
                    isLoading.value = false
                    cryptoList.value += cryptoItems //TODO: cryptoList e me ekliyorum success gelirse
                    //TODO:mutableStateOf<List<CryptoListItem>>(listOf()) içerisinde bunlar kayıtlı olacak.
                }

                is Resource.Error<*> -> {
                    errorMessage.value = result.message!! //TODO: Hata varsa repository içerisinde mesaj döndürüyoruz
                    //TODO: class CryptoRepository -> catch (e : Exception){ return Resource.Error("Error") }
                    isLoading.value = false
                }

                else -> {
                    isLoading.value = true
                    errorMessage.value = result.message!!
                }
            }

        }

    }

    /* -> Bu şekilde de yazılabilir!!
    fun searchCryptoList(query: String) {
    viewModelScope.launch(Dispatchers.Default) {
        val filtered = if(!query.isEmpty()) {
            initialCryptoList.filter {
                it.currency.contains(query.trim(), ignoreCase = true)
            }
        }
        else {
            initialCryptoList
        }

        cryptoList.value = filtered
    }
}


 fun loadCryptos() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getCryptoList()
            println("my data"+result.data)
            when(result) {
                is Resource.Success -> {
                    val cryptoItems = result.data!!.mapIndexed { index, item ->
                        CryptoListItem(item.currency,item.price)
                    } as List<CryptoListItem>
                    println("myitems"+cryptoItems)
                    errorMessage.value = ""
                    isLoading.value = false
                    cryptoList.value = result.data
                    initialCryptoList = result.data
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }
     */


}