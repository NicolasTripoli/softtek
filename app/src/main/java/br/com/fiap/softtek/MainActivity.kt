package br.com.fiap.softtek

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softtek.screens.CheckIn
import br.com.fiap.softtek.screens.Home
import br.com.fiap.softtek.screens.Login
import br.com.fiap.softtek.screens.RelatoAnonimo
import br.com.fiap.softtek.screens.TelaRelatos
import br.com.fiap.softtek.screens.RelatoEmail
import br.com.fiap.softtek.screens.Dashboard
import br.com.fiap.softtek.ui.theme.SoftTekTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoftTekTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable(route = "login") { Login(navController) }
                        composable(route = "home") { Home(navController) }
                        composable(route = "checkIn") { CheckIn(navController) }
                        composable(route = "relatoAnonimo") { RelatoAnonimo(navController) }
                        composable(route = "avaliacaoPsicosocial") { 
                            // Temporariamente redirecionando para Home até a tela ser implementada
                            Home(navController) 
                        }
                        composable(route = "telaRelatos") { TelaRelatos(navController) }
                        composable(route = "relatoEmail") { RelatoEmail(navController) }
                        composable(route = "dashboard") { Dashboard(navController) }
                    }
                }
            }
        }
        hideNavigationBar()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideNavigationBar() {
        window.insetsController?.hide(WindowInsets.Type.navigationBars())
        window.insetsController?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE    }
}