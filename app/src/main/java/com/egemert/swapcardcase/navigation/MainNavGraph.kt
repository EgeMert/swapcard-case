package com.egemert.swapcardcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.egemert.swapcardcase.screen.BookmarkScreen
import com.egemert.swapcardcase.screen.UserListScreen

@Composable
fun MainNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: Screens = Screens.UserList,
    router: Router,
    navGraphBuilder: NavGraphBuilder.() -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navGraphBuilder.invoke(this)
        mainNavigation(router = router)
    }
}

fun NavGraphBuilder.mainNavigation(
    router: Router
) {
    composable<Screens.UserList> {
        UserListScreen(router = router)
    }
    composable<Screens.Bookmark> {
        BookmarkScreen(router = router)
    }

}