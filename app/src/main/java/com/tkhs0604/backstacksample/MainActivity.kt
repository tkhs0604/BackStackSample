package com.tkhs0604.backstacksample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tkhs0604.backstacksample.ui.theme.BackStackSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BackStackSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainNavHost()
                }
            }
        }
    }
}

@VisibleForTesting
@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.AScreen.value,
) {
    val backStack by navController.currentBackStack.collectAsState()
    val routes = remember(backStack) {
        backStack.formattedRoutes()
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        composable(Destinations.AScreen.value) {
            AScreen(
                routes = routes,
                toAScreen = {
                    // stack A screen again
                    navController.navigate(Destinations.AScreen.value)
                },
                toAScreenWithLaunchSingleTop = {
                    // do nothing if A screen is already on the top of the stack
                    navController.navigate(Destinations.AScreen.value) {
                        launchSingleTop = true
                    }
                },
                toBScreen = {
                    navController.navigate(Destinations.BScreen.value)
                },
                toCScreen = {
                    navController.navigate(Destinations.CScreen.value)
                },
                back = {
                    navController.popBackStack()
                },
            )
        }
        composable(Destinations.BScreen.value) {
            BScreen(
                routes = routes,
                toAScreen = {
                    navController.navigate(Destinations.AScreen.value)
                },
                toAScreenWithInclusivePopUpTo = {
                    navController.navigate(Destinations.AScreen.value) {
                        popUpTo(Destinations.AScreen.value) {
                            inclusive = true
                        }
                    }
                },
                toCScreen = {
                    navController.navigate(Destinations.CScreen.value)
                },
                back = {
                    navController.popBackStack()
                },
            )
        }
        composable(Destinations.CScreen.value) {
            CScreen(
                routes = routes,
                toAScreen = {
                    navController.navigate(Destinations.AScreen.value)
                },
                toAScreenWithInclusivePopUpTo = {
                    navController.navigate(Destinations.AScreen.value) {
                        popUpTo(Destinations.AScreen.value) {
                            inclusive = true
                        }
                    }
                },
                toAScreenWithPopUpTo = {
                    navController.navigate(Destinations.AScreen.value) {
                        popUpTo(Destinations.AScreen.value)
                    }
                },
                toBScreen = {
                    navController.navigate(Destinations.BScreen.value)
                },
                backToAScreen = {
                    navController.popBackStack(Destinations.AScreen.value, inclusive = false)
                },
                backToAScreenWithInclusive = {
                    navController.popBackStack(Destinations.AScreen.value, inclusive = true)
                },
                back = {
                    navController.popBackStack()
                },
            )
        }
    }
}

private enum class Destinations(val value: String) {
    AScreen("A"),
    BScreen("B"),
    CScreen("C"),
}

@Composable
private fun AScreen(
    routes: List<String>,
    toAScreen: () -> Unit,
    toAScreenWithLaunchSingleTop: () -> Unit,
    toBScreen: () -> Unit,
    toCScreen: () -> Unit,
    back: () -> Unit,
) {
    ScreenScaffold(
        screenName = "A",
        backgroundColor = Color(0xFFE4A5B0),
        routes = routes,
    ) {
        Button(onClick = toAScreen) {
            Text(text = "Go to A")
        }
        Button(onClick = toAScreenWithLaunchSingleTop) {
            Text(text = "Go to A (launchSingleTop)")
        }
        Button(onClick = toBScreen) {
            Text(text = "Go to B")
        }
        Button(onClick = toCScreen) {
            Text(text = "Go to C")
        }
        Button(onClick = back) {
            Text(text = "Go back")
        }
    }
}

@Composable
private fun BScreen(
    routes: List<String>,
    toAScreen: () -> Unit,
    toAScreenWithInclusivePopUpTo: () -> Unit,
    toCScreen: () -> Unit,
    back: () -> Unit,
) {
    ScreenScaffold(
        screenName = "B",
        backgroundColor = Color(0xFFDAC1FA),
        routes = routes,
    ) {
        Button(onClick = toAScreen) {
            Text(text = "Go to A")
        }
        Button(onClick = toAScreenWithInclusivePopUpTo) {
            Text(text = "Go to A (popUpTo / inclusive)")
        }
        Button(onClick = toCScreen) {
            Text(text = "Go to C")
        }
        Button(onClick = back) {
            Text(text = "Go back")
        }
    }
}

@Composable
private fun CScreen(
    routes: List<String>,
    toAScreen: () -> Unit,
    toAScreenWithInclusivePopUpTo: () -> Unit,
    toAScreenWithPopUpTo: () -> Unit,
    toBScreen: () -> Unit,
    backToAScreen: () -> Unit,
    backToAScreenWithInclusive: () -> Unit,
    back: () -> Unit,
) {
    ScreenScaffold(
        screenName = "C",
        backgroundColor = Color(0xFF56AB91),
        routes = routes,
    ) {
        Button(onClick = toAScreen) {
            Text(text = "Go to A")
        }
        Button(onClick = toAScreenWithInclusivePopUpTo) {
            Text(text = "Go to A (popUpTo / inclusive)")
        }
        Button(onClick = toAScreenWithPopUpTo) {
            Text(text = "Go to A (popUpTo)")
        }
        Button(onClick = backToAScreen) {
            Text(text = "Go back to A")
        }
        Button(onClick = backToAScreenWithInclusive) {
            Text(text = "Go back to A (inclusive)")
        }
        Button(onClick = toBScreen) {
            Text(text = "Go to B")
        }
        Button(onClick = back) {
            Text(text = "Go back")
        }
    }
}

@Composable
private inline fun ScreenScaffold(
    screenName: String,
    backgroundColor: Color,
    routes: List<String>,
    buttonSectionContent: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = screenName, style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.padding(8.dp))

            buttonSectionContent()
        }

        Divider()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item { Text(text = "Backstack count: ${routes.size - 1}") }
            items(routes) { route ->
                Text(text = route)
            }
        }
    }
}

private fun List<NavBackStackEntry>.formattedRoutes(): List<String> {
    return map { it.destination.route }
        .map { route ->
            if (route != null) {
                "┃ $route ┃"
            } else {
                "┗━┛"
            }
        }
        .reversed()
}
