package com.egemert.swapcardcase.network.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("results")
    val results: List<User>? = null,
    @SerializedName("info")
    val info: Info? = null
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
