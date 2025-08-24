package com.egemert.swapcardcase.navigation

import androidx.navigation.NavHostController

class RouterImpl(
    private val navHostController: NavHostController,
    private val startDestination: Screens = Screens.UserList
) : Router {

    override fun goToBookmarkScreen() {
        navigate(Screens.Bookmark, removeFromHistory = true, singleTop = true)
    }


    private fun navigate(
        screen: Screens,
        removeFromHistory: Boolean = false,
        singleTop: Boolean = false,
        removeBackStack: Boolean = false
    ) {
        navHostController.apply {
            navigate(screen) {
                if (removeFromHistory) {
                    if (singleTop) {
                        popUpTo(startDestination) {
                            inclusive = true
                        }
                    } else {
                        popUpTo(0) {
                            saveState = false
                        }
                    }

                } else if (removeBackStack) {
                    popUpTo(screen) {
                        inclusive = true
                    }
                } else {
                    restoreState = true
                }
                launchSingleTop = singleTop
            }
        }
    }
}