package com.mahdicen.bagmag.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mahdicen.bagmag.R
import com.mahdicen.bagmag.ui.theme.BackgroundMain
import com.mahdicen.bagmag.ui.theme.BagMagTheme
import com.mahdicen.bagmag.ui.theme.Blue
import com.mahdicen.bagmag.util.MyScreens
import dev.burnoo.cokoin.navigation.getNavController
import okhttp3.internal.wait

@Preview(showBackground = true)
@Composable
fun IntroPreview() {
    BagMagTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            IntroScreen()
        }
    }
}

@Composable
fun IntroScreen() {

    val sysUi = rememberSystemUiController()
    sysUi.setStatusBarColor(darkIcons = true, color = Color.Transparent)
    sysUi.setNavigationBarColor(darkIcons = true, color = Color.Transparent)

    val navigation = getNavController()

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.bag),
        contentDescription = null,
        contentScale = ContentScale.FillHeight
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(bottom = 88.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth(0.6f),
            onClick = {
                navigation.navigate(MyScreens.SignUpScreen.route)
            }
        ) {

            Text(
                text = "Sign Up",
                color = Color.White
            )

        }

        Button(
            modifier = Modifier
                .fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            onClick = {
                navigation.navigate(
                    MyScreens.SignInScreen.route,
                )
            }
        ) {

            Text(
                text = "Sign In",
                color = Blue
            )

        }

    }

}