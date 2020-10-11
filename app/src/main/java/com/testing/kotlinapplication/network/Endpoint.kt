package com.testing.kotlinapplication.network

import com.testing.kotlinapplication.network.model.LoginRespone
import com.testing.kotlinapplication.network.model.RegisterRespone
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST


interface Endpoint {
    @POST("/api/customer/dang-ky")
    fun register(@Body params: HashMap<String, String>): Observable<RegisterRespone>

    @POST("/api/customer/dang-nhap")
    fun login(@Body params: HashMap<String, String>): Observable<LoginRespone>
}