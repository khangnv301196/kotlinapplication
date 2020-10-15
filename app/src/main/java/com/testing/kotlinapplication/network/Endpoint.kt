package com.testing.kotlinapplication.network

import com.testing.kotlinapplication.network.model.DetailProductReponse
import com.testing.kotlinapplication.network.model.LoginRespone
import com.testing.kotlinapplication.network.model.ProductRespone
import com.testing.kotlinapplication.network.model.RegisterRespone
import io.reactivex.Observable
import retrofit2.http.*


interface Endpoint {
    @POST("/api/customer/dang-ky")
    fun register(@Body params: HashMap<String, String>): Observable<RegisterRespone>

    @POST("/api/customer/dang-nhap")
    fun login(@Body params: HashMap<String, String>): Observable<LoginRespone>

    @GET("/api/customer/danh-sach-san-pham")
    fun getProduct(@Header("Authorization") token: String, @QueryMap params: HashMap<String, String>): Observable<ProductRespone>

    @GET("/api/customer/chi-tiet-san-pham")
    fun getDetailProduct(@Header("Authorization") token: String, @QueryMap params: HashMap<String, String>): Observable<DetailProductReponse>
}