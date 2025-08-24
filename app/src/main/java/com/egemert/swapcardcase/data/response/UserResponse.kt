package com.egemert.swapcardcase.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("results")
    val results: List<User>? = null,
    @SerializedName("info")
    val info: Info? = null
)

data class User(
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("name") val name: Name? = null,
    @SerializedName("location") val location: Location? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("login") val login: Login? = null,
    @SerializedName("dob") val dob: DateOfBirth? = null,
    @SerializedName("registered") val registered: Registered? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("cell") val cell: String? = null,
    @SerializedName("id") val id: Id? = null,
    @SerializedName("picture") val picture: Picture? = null,
    @SerializedName("nat") val nationality: String? = null
)

data class Name(
    @SerializedName("title") val title: String? = null,
    @SerializedName("first") val first: String? = null,
    @SerializedName("last") val last: String? = null
)

data class Location(
    @SerializedName("street") val street: Street? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("coordinates") val coordinates: Coordinates? = null,
    @SerializedName("timezone") val timezone: Timezone? = null
)

data class Street(
    @SerializedName("number") val number: Int? = null,
    @SerializedName("name") val name: String? = null
)

data class Coordinates(
    @SerializedName("latitude") val latitude: String? = null,
    @SerializedName("longitude") val longitude: String? = null
)

data class Timezone(
    @SerializedName("offset") val offset: String? = null,
    @SerializedName("description") val description: String? = null
)

data class Login(
    @SerializedName("uuid") val uuid: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("salt") val salt: String? = null,
    @SerializedName("md5") val md5: String? = null,
    @SerializedName("sha1") val sha1: String? = null,
    @SerializedName("sha256") val sha256: String? = null
)

data class DateOfBirth(
    @SerializedName("date") val date: String? = null,
    @SerializedName("age") val age: Int? = null
)

data class Registered(
    @SerializedName("date") val date: String? = null,
    @SerializedName("age") val age: Int? = null
)

data class Id(
    @SerializedName("name") val name: String? = null,
    @SerializedName("value") val value: String? = null
)

data class Picture(
    @SerializedName("large") val large: String? = null,
    @SerializedName("medium") val medium: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null
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
