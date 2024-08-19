package com.oguz.cryptoapp.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

/**
 * API a bir istek yapacagız bir veri dönecek bu veri success ,error ve loading durumlarında dönebilir
 * Resource kaynak dosyası herhangi bir veri tipi döndürüyor val data: T? = null -> T bir type ve nullable
 */