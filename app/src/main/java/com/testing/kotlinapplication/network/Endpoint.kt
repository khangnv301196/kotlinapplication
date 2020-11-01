package com.testing.kotlinapplication.network

import com.testing.kotlinapplication.network.model.*
import io.reactivex.Observable
import retrofit2.http.*


interface Endpoint {
    @POST("/api/customer/dang-ky")
    fun register(@Body params: HashMap<String, String>): Observable<RegisterRespone>

    @POST("/api/customer/dang-nhap")
    fun login(@Body params: HashMap<String, String>): Observable<LoginRespone>

    @GET("/api/customer/dang-xuat")
    fun logout(@Header("Authorization") token: String): Observable<RegisterRespone>

    @GET("/api/customer/danh-sach-san-pham")
    fun getProduct(
        @Header("Authorization") token: String,
        @QueryMap params: HashMap<String, String>
    ): Observable<ProductRespone>

    @GET("/api/customer/dashboard")
    fun getDashBoard(): Observable<TopResponse>

    @GET("/api/customer/chi-tiet-san-pham")
    fun getDetailProduct(
        @Header("Authorization") token: String,
        @QueryMap params: HashMap<String, String>
    ): Observable<DetailProductReponse>

    @POST("/api/customer/order")
    fun postNewOrder(
        @Header("Authorization") token: String,
        @Body bodys: HashMap<String, String>
    ): Observable<RegisterRespone>

    @GET("/api/customer/list-orders")
    fun getListOrder(
        @Header("Authorization") token: String,
        @QueryMap params: HashMap<String, String>
    ): Observable<OrderResponse>

    @POST("/api/customer/update-status-order")
    fun getUpdateOrder(
        @Header("Authorization") token: String,
        @QueryMap params: HashMap<String, String>
    ): Observable<RegisterRespone>
}