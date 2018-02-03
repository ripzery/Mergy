package me.ripzery.mergy.network

import io.reactivex.Observable
import retrofit2.http.POST


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 3/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface IsetanEndpoints {
    @POST("api/config/index")
    fun retrievePhotos(): Observable<Response.Config>

    @POST("api/upload/media")
    fun upload(request: Request.Upload): Observable<Response.Upload>

    @POST("api/user/getrecord")
    fun retrieveUserInfos(request: Request.RetrieveUsers): Observable<Response.RetrieveUsers>

    @POST("api/user/sendmail")
    fun sendEmail(request: Request.SendEmail): Observable<Response.SendEmail>
}