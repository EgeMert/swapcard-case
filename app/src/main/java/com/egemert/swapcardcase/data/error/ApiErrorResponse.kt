package com.egemert.swapcardcase.data.error

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("error")
    val error: String? = null
)
