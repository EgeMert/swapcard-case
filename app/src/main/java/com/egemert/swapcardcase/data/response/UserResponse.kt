package com.egemert.swapcardcase.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("results")
    val results: List<User>? = null,
    @SerializedName("info")
    val info: Info? = null
)

data class User(
    @SerializedName("name") val name: Name? = null,
    @SerializedName("location") val location: Location? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("login") val login: Login? = null,
    @SerializedName("id") val id: Id? = null,
    @SerializedName("picture") val picture: Picture? = null
)

data class Name(
    @SerializedName("title") val title: String? = null,
    @SerializedName("first") val first: String? = null,
    @SerializedName("last") val last: String? = null
)

data class Location(
    @SerializedName("country") val country: String? = null
)

data class Login(
    @SerializedName("uuid") val uuid: String? = null
)

data class Id(
    @SerializedName("name") val name: String? = null,
    @SerializedName("value") val value: String? = null
)

data class Picture(
    @SerializedName("large") val large: String? = null
)

data class Info(
    @SerializedName("seed")
    val seed: String? = null,
    @SerializedName("results")
    val results: Int? = null,
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("version")
    val version: String? = null
)
