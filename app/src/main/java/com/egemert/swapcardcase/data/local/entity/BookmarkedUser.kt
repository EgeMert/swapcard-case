package com.egemert.swapcardcase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.egemert.swapcardcase.data.response.Location
import com.egemert.swapcardcase.data.response.Login
import com.egemert.swapcardcase.data.response.Name
import com.egemert.swapcardcase.data.response.Picture
import com.egemert.swapcardcase.data.response.User

@Entity(tableName = "bookmarked_users")
data class BookmarkedUser(
    @PrimaryKey
    val uuid: String,
    val name: String?,
    val email: String?,
    val country: String?,
    val pictureUrl: String?
) {
    companion object {
        fun fromUser(user: User): BookmarkedUser {
            return BookmarkedUser(
                uuid = user.login?.uuid ?: "",
                name = "${user.name?.first ?: ""} ${user.name?.last ?: ""}".trim(),
                email = user.email,
                country = user.location?.country,
                pictureUrl = user.picture?.large
            )
        }
    }

    fun toUser(): User {
        return User(
            name = name?.let {
                val parts = it.split(" ")
                Name(
                    first = parts.firstOrNull() ?: "",
                    last = parts.drop(1).joinToString(" "),
                    title = null
                )
            },
            email = email,
            location = Location(country = country),
            login = Login(uuid = uuid),
            picture = Picture(large = pictureUrl),
            id = null
        )
    }
}
