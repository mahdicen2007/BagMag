package com.mahdicen.bagmag.ui.features.signUp

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mahdicen.bagmag.R
import com.mahdicen.bagmag.ui.theme.BackgroundMain
import com.mahdicen.bagmag.ui.theme.BagMagTheme
import com.mahdicen.bagmag.ui.theme.Blue
import com.mahdicen.bagmag.util.MyScreens
import com.mahdicen.bagmag.util.NetworkChecker
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    BagMagTheme {
        Surface(
            color = BackgroundMain, modifier = Modifier.fillMaxSize()
        ) {
            SignUpScreen()
        }
    }
}

@Composable
fun SignUpScreen() {

    val sysUi = rememberSystemUiController()
    sysUi.setStatusBarColor(darkIcons = true, color = Color.Blue)
    sysUi.setNavigationBarColor(darkIcons = true, color = Color.White)

    val context = LocalContext.current
    val navigation = getNavController()
    val viewModel = getViewModel<SignUpViewModel>()

    clearSignUpInputs(viewModel = viewModel)

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
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()), // responsive UI
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconApp()
            MainCardSignUp(navigation, viewModel) {

                viewModel.signUpUser {



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
fun IconApp() {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .size(64.dp)
    ) {
        Image(
            contentDescription = null,
            painter = painterResource(id = R.drawable.ic_backpack),
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun MainCardSignUp(navigation: NavController, viewModel: SignUpViewModel, signUpEvent: () -> Unit) {
    val name = viewModel.name.observeAsState("")
    val email = viewModel.email.observeAsState("")
    val password = viewModel.password.observeAsState("")
    val confirmPass = viewModel.confirmPass.observeAsState("")

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
                text = "Sign Up",
                modifier = Modifier.padding(vertical = 10.dp),
                style = TextStyle(color = Blue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            )

            MainTextField(
                edtValue = name.value, icon = R.drawable.ic_person, hint = "Full Name "
            ) { viewModel.name.value = it }

            MainTextField(
                edtValue = email.value, icon = R.drawable.ic_email, hint = "Email"
            ) { viewModel.email.value = it }

            PasswordTextField(
                edtValue = password.value, icon = R.drawable.ic_lock, hint = "Password"
            ) { viewModel.password.value = it }

            PasswordTextField(
                edtValue = confirmPass.value, icon = R.drawable.ic_lock, hint = "Confirm Password"
            ) { viewModel.confirmPass.value = it }

            Button(
                onClick = {

                    if (name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty() && confirmPass.value.isNotEmpty()) {

                        if (password.value == confirmPass.value) {

                            if (password.value.length >= 8) {

                                if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {

                                    if (NetworkChecker(context).isInternetConnected) {
                                        Log.v("testNet" , NetworkChecker(context).isInternetConnected.toString())
                                        signUpEvent.invoke()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Internet Connection failed!",
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
                                Toast.makeText(
                                    context,
                                    "Passwords characters must be at least 8! ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(
                                context, "Passwords combination is wrong!", Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(context, "Fill in the blanks!", Toast.LENGTH_SHORT).show()
                    }

                },
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(Blue),
                shape = ShapeDefaults.Medium
            ) {
                Text(
                    text = "Register Account",
                    modifier = Modifier.padding(8.dp),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {

                Text(text = "Already have an Account?")

                TextButton(onClick = {
                    navigation.navigate(MyScreens.SignInScreen.route) {
                        popUpTo(MyScreens.SignUpScreen.route) {
                            inclusive = true
                        }
                    }
                }) {
                    Text(text = "Log In", color = Blue)
                }

            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTextField(
    edtValue: String, icon: Int, hint: String, onValueChanges: (String) -> Unit
) {

    OutlinedTextField(
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ) ,
        label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = onValueChanges,
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .padding(vertical = 12.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        },
        shape = ShapeDefaults.Medium,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            unfocusedLeadingIconColor = Color.Gray,
            unfocusedLabelColor = Color.Gray,
            unfocusedIndicatorColor = Color.Gray,
            unfocusedTrailingIconColor = Color.Gray,
            focusedLeadingIconColor = Color.Gray,
            focusedTrailingIconColor = Color.Gray
        )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    edtValue: String, icon: Int, hint: String, onValueChanges: (String) -> Unit
) {
    val passwordVisible = remember { mutableStateOf(value = false) }

    OutlinedTextField(label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = onValueChanges,
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .padding(vertical = 12.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        },
        shape = ShapeDefaults.Medium,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            unfocusedLeadingIconColor = Color.Gray,
            unfocusedLabelColor = Color.Gray,
            unfocusedIndicatorColor = Color.Gray,
            unfocusedTrailingIconColor = Color.Gray,
            focusedLeadingIconColor = Color.Gray,
            focusedTrailingIconColor = Color.Gray

        ),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {

            val image = if (passwordVisible.value) painterResource(id = R.drawable.ic_eye_off)
            else painterResource(id = R.drawable.ic_eye_on)

            Icon(painter = image,
                contentDescription = null,
                modifier = Modifier
                    .clickable { passwordVisible.value = !passwordVisible.value }
                    .size(28.dp))

        })

}

fun clearSignUpInputs(viewModel :SignUpViewModel) {
    viewModel.email.value = ""
    viewModel.name.value = ""
    viewModel.password.value = ""
    viewModel.confirmPass.value = ""
}
