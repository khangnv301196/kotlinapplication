package com.testing.kotlinapplication.network

import com.testing.kotlinapplication.network.model.*
import io.reactivex.Observable
import retrofit2.http.*


interface Endpoint {
    @POST("/api/customer/dang-ky")
    fun register(@Body params: HashMap<String, String>): Observable<RegisterRespone>

    @POST("/api/customer/dang-nhap")
    fun login(@Body params: HashMap<String, String>): Observable<LoginRespone>

    @GET("/api/customer/danh-sach-san-pham")
    fun getProduct(
        @Header("Authorization") token: String,
        @QueryMap params: HashMap<String, String>
    ): Observable<ProductRespone>

    @GET("/api/customer/chi-tiet-san-pham")
    fun getDetailProduct(
        @Header("Authorization") token: String,
        @QueryMap params: HashMap<String, String>
    ): Observable<DetailProductReponse>

    @POST("/api/customer/them-gio-hang")
    fun postNewCart(
        @Header("Authorization") token: String,
        @Body bodys: HashMap<String, String>
    ): Observable<RegisterRespone>

    @GET("/api/customer/danh-sach-gio-hang")
    fun getCartByUserId(
        @Header("Authorization") token: String,
        @QueryMap params: HashMap<String, String>
    ): Observable<CartResponse>

    @POST("/api/customer/xoa-gio-hang")
    fun deleteProductInCart(
        @Header("Authorization") token: String,
        @QueryMap params: HashMap<String, String>
    )

    @POST("/api/customer/cap-nhat-gio-hang")
    fun updateCart(
        @Header("Authorization") token: String,
        @Body bodys: HashMap<String, String>
    )
}