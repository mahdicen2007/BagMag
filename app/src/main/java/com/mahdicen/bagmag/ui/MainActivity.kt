package com.mahdicen.bagmag.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mahdicen.bagmag.di.myModules
import com.mahdicen.bagmag.model.repository.TokenInMemory
import com.mahdicen.bagmag.model.repository.user.UserRepository
import com.mahdicen.bagmag.ui.features.IntroScreen
import com.mahdicen.bagmag.ui.features.cart.CartScreen
import com.mahdicen.bagmag.ui.features.category.CategoryScreen
import com.mahdicen.bagmag.ui.features.main.MainScreen
import com.mahdicen.bagmag.ui.features.product.ProductScreen
import com.mahdicen.bagmag.ui.features.profile.ProfileScreen
import com.mahdicen.bagmag.ui.features.signIn.SignInScreen
import com.mahdicen.bagmag.ui.features.signUp.SignUpScreen
import com.mahdicen.bagmag.ui.theme.BackgroundMain
import com.mahdicen.bagmag.ui.theme.BagMagTheme
import com.mahdicen.bagmag.util.KEY_Category_ARG
import com.mahdicen.bagmag.util.KEY_PRODUCT_ARG
import com.mahdicen.bagmag.util.MyScreens
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContent {

            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules( myModules )
            }) {
                BagMagTheme {
                    Surface(
                        color = BackgroundMain,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val userRepo :UserRepository = get()
                        userRepo.loadToken()
                        BagMagUi()
                    }
                }
            }
        }
    }
}

@Composable
private fun BagMagUi() {

    val navController = rememberNavController()
    KoinNavHost(
        navController = navController,
        startDestination = MyScreens.MainScreen.route
    ) {

        composable(MyScreens.MainScreen.route) {

            if (TokenInMemory.token != null) {
                MainScreen()
            } else {
                IntroScreen()
            }

        }

        composable(
            MyScreens.ProductScreen.route + "/" + "{$KEY_PRODUCT_ARG}" ,
            arguments = listOf( navArgument(name = KEY_PRODUCT_ARG) {
                type = NavType.StringType
            })
        ) {
            ProductScreen(it.arguments!!.getString(KEY_PRODUCT_ARG , "null"))
        }

        composable(
            MyScreens.CategoryScreen.route + "/" + "{$KEY_Category_ARG}"  ,
            arguments = listOf( navArgument(name = KEY_Category_ARG) {
                type = NavType.StringType
            })
            ) {
            CategoryScreen(it.arguments!!.getString(KEY_Category_ARG , "null"))
        }

        composable(MyScreens.ProfileScreen.route) {
            ProfileScreen()
        }

        composable(MyScreens.CartScreen.route) {
            CartScreen()
        }

        composable(MyScreens.SignUpScreen.route) {
            SignUpScreen()
        }

        composable(MyScreens.SignInScreen.route) {
            SignInScreen()
        }

    }

}

@Composable
fun NoInternetScreen() {

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BagMagTheme {
        Greeting("Android")
    }
}