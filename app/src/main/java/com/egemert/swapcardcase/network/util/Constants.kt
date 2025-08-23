package com.egemert.swapcardcase.network.util

object Constants {
    const val BASE_URL = "https://randomuser.me/" // Random User API base URL
    const val TIMEOUT_IN_SEC = 30L
    
    // API Endpoints
    object Endpoints {
        const val USERS = "users"
    }
    
    // Query Parameters
    object Params {
        const val PAGE = "page"
        const val PAGE_SIZE = "results"
    }
    
    // Headers
    object Headers {
        const val ACCEPT = "Accept"
        const val CONTENT_TYPE = "Content-Type"
        const val ACCEPT_LANGUAGE = "Accept-Language"
    }
}
