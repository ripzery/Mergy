package me.ripzery.warpcan.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 3/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object Request {
    sealed class Retriable : Parcelable {
        @Parcelize
        data class Upload(
                @SerializedName("base_64_image") val base64Image: String,
                @SerializedName("season_id") val seasonId: Int,
                @SerializedName("image_type_id") val imageTypeId: Int
        ) : Retriable()

        @Parcelize
        data class SendEmail(
                @SerializedName("email") val email: String,
                @SerializedName("user_profile_id") val userProfileId: Int,
                @SerializedName("image_url") val imageUrl: String,
                @SerializedName("fname") val firstName: String,
                @SerializedName("lname") val lastName: String,
                @SerializedName("season_id") val seasonId: Int,
                @SerializedName("image_id") val imageId: Int
        ) : Retriable()
    }


    data class RetrieveUsers(val date: String)
}

object Response {
    @Parcelize
    data class Config(val errors: Boolean, val message: ArrayList<Photo>) : Parcelable

    @Parcelize
    data class Photo(
            @SerializedName("image_id") val imageId: Int,
            @SerializedName("image_type") val imageType: Int,
            @SerializedName("image_type_name") val imageTypeName: String,
            @SerializedName("season_id") val seasonId: Int,
            @SerializedName("t_created") val tCreated: Date,
            @SerializedName("image_url") val imageUrl: String
    ) : Parcelable

    @Parcelize
    data class Upload(val errors: Boolean, val message: ImageUrl) : Parcelable

    @Parcelize
    data class UploadLog(@SerializedName("photo_id") val photoId: Int, val msg: String) : Parcelable

    @Parcelize
    data class ImageUrl(@SerializedName("image_url") val imageUrl: String, val log: UploadLog) : Parcelable

    @Parcelize
    data class RetrieveUsers(val errors: Boolean, val message: ArrayList<User>) : Parcelable

    @Parcelize
    data class User(
            @SerializedName("user_profile_id") val userProfileId: Int,
            @SerializedName("fname") val firstName: String,
            @SerializedName("lname") val lastName: String,
            @SerializedName("gender") val gender: String,
            @SerializedName("email") val email: String,
            @SerializedName("phone") val phone: String,
            @SerializedName("season_id") val seasonId: Int,
            @SerializedName("t_created") val createdDate: Date
    ) : Parcelable

    @Parcelize
    data class SendEmail(
            val errors: Boolean,
            val message: Email
    ) : Parcelable

    @Parcelize
    data class Email(
            @SerializedName("request_id") val requestId: String
    ) : Parcelable
}