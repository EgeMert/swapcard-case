package com.egemert.swapcardcase.navigation

import kotlinx.serialization.Serializable

sealed class Screens {

    @Serializable
    data object UserList : Screens()

    @Serializable
    data object Bookmark : Screens()
}