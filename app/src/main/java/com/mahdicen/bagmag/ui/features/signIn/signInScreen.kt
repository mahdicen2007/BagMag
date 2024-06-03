package com.mahdicen.bagmag.ui.features.signIn

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mahdicen.bagmag.R
import com.mahdicen.bagmag.ui.features.signUp.IconApp
import com.mahdicen.bagmag.ui.features.signUp.MainTextField
import com.mahdicen.bagmag.ui.features.signUp.PasswordTextField
import com.mahdicen.bagmag.ui.features.signUp.SignUpViewModel
import com.mahdicen.bagmag.ui.theme.BackgroundMain
import com.mahdicen.bagmag.ui.theme.BagMagTheme
import com.mahdicen.bagmag.ui.theme.Blue
import com.mahdicen.bagmag.util.MyScreens
import com.mahdicen.bagmag.util.NetworkChecker
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    BagMagTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            SignInScreen()
        }
    }
}

@Composable
fun SignInScreen() {

    val sysUi = rememberSystemUiController()
    sysUi.setStatusBarColor(darkIcons = true, color = Color.Blue)
    sysUi.setNavigationBarColor(darkIcons = true, color = Color.White)

    val context = LocalContext.current
    val navigation = getNavController()
    val viewModel = getViewModel<SignInViewModel>()

    clearSignInInputs(viewModel = viewModel)

    Box {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(Blue),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconApp()
            MainCardSignIn(navigation, viewModel) {
                viewModel.signInUser {
                    if (it == "Success") {
                        navigation.navigate(MyScreens.MainScreen.route) {
                            popUpTo(MyScreens.MainScreen.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}

@Composable
fun MainCardSignIn(navigation: NavController, viewModel: SignInViewModel, signInEvent: () -> Unit) {

    val email = viewModel.email.observeAsState("")
    val password = viewModel.password.observeAsState("")

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = ShapeDefaults.Medium,
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Sign In",
                modifier = Modifier.padding(vertical = 10.dp),
                style = TextStyle(color = Blue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            )

            MainTextField(
                edtValue = email.value, icon = R.drawable.ic_email, hint = "Email"
            ) { viewModel.email.value = it }

            PasswordTextField(
                edtValue = password.value, icon = R.drawable.ic_lock, hint = "Password"
            ) { viewModel.password.value = it }

            Button(
                onClick = {

                    if (email.value.isNotEmpty() && password.value.isNotEmpty()) {

                        if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {

                            if (NetworkChecker(context).isInternetConnected) {
                                signInEvent.invoke()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Internet Connection failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "Email format is wrong!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(context, "Fill in the blanks!", Toast.LENGTH_SHORT).show()
                    }

                },
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(Blue),
                shape = ShapeDefaults.Medium
            ) {
                Text(
                    text = "Sign In Account",
                    modifier = Modifier.padding(8.dp),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {

                Text(text = "Don't have an Account?")

                TextButton(onClick = {
                    navigation.navigate(MyScreens.SignUpScreen.route) {
                        popUpTo(MyScreens.SignInScreen.route) {
                            inclusive = true
                        }
                    }
                }) {
                    Text(text = "Register one", color = Blue)
                }

            }

        }

    }

}

fun clearSignInInputs(viewModel : SignInViewModel) {
    viewModel.email.value = ""
    viewModel.password.value = ""
}