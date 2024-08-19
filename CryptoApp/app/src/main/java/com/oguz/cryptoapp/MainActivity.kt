package com.oguz.cryptoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.oguz.cryptoapp.ui.theme.CryptoAppTheme
import com.oguz.cryptoapp.view.CryptoDetailsScreen
import com.oguz.cryptoapp.view.CryptoListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoAppTheme {

                //TODO:rememberNavController -> COmpose da ReComposion diye bir durum var. Her hangi bir gösterdiğimiz
                //  composeble larda bir değişiklik olursa değişkenlerden bir tanesinde bir değişiklik olursa bütük ekranı tekrar
                //  recompose ediyor. Tekrar bastan olusturuyor
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "cryptoListScreen") {

                    composable("cryptoListScreen") {
                        //TODO: route -> nereye gidecek
                        //CryptoListScreen
                        CryptoListScreen(navController = navController)
                    }

                    composable("CryptoDetailsScreen/{cryptoId}/{cryptoPrice}", arguments = listOf(
                        navArgument("cryptoId") {
                            type = NavType.StringType
                        },

                        navArgument("CryptoPrice") {
                            type = NavType.StringType
                        }
                    )) {
                        /**
                         * CryptoListScreen den CryptoDetailsScreen a yollanan argümanların değişken olarak bize verilmesi
                         * gerekiyor.
                         */
                        //CryptoDetailsScreen

                        val cryptoId = remember {
                            it.arguments?.getString("cryptoId")
                        }

                        val cryptoPrice = remember {
                            it.arguments?.getString("cryptoPrice")
                        }

                        CryptoDetailsScreen(
                            id = cryptoId ?: "",
                            price = cryptoPrice ?: "",
                            navController = navController
                        )

                    }
                }
            }
        }
    }
}

