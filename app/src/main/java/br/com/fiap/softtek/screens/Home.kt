package br.com.fiap.softtek.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softtek.components.MenuHeader
import br.com.fiap.softtek.ui.theme.DarkBlue
import br.com.fiap.softtek.ui.theme.LightGrey

@Composable
fun Home(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(DarkBlue, LightGrey, DarkBlue),
                )
            )
    ) {
        MenuHeader("Home", "home")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHome() {
    val navController = rememberNavController()
    Home(navController)
}