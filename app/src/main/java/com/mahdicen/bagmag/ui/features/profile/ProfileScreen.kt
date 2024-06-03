package com.mahdicen.bagmag.ui.features.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahdicen.bagmag.R
import com.mahdicen.bagmag.model.repository.TokenInMemory
import com.mahdicen.bagmag.ui.features.product.NormalTextField
import com.mahdicen.bagmag.ui.features.signUp.MainTextField
import com.mahdicen.bagmag.ui.theme.BackgroundMain
import com.mahdicen.bagmag.ui.theme.BagMagTheme
import com.mahdicen.bagmag.ui.theme.Blue
import com.mahdicen.bagmag.ui.theme.CardViewBackground
import com.mahdicen.bagmag.util.MyScreens
import com.mahdicen.bagmag.util.styleTime
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import javax.security.auth.Subject

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    BagMagTheme {
        Surface(
            color = BackgroundMain, modifier = Modifier.fillMaxSize()
        ) {
            ProfileScreen()
        }
    }
}

@Composable
fun ProfileScreen() {

    val context = LocalContext.current
    val navigation = getNavController()

    val viewModel = getViewModel<ProfileViewModel>()
    viewModel.loadUserData()

    Box {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ProfileToolbar() {
                navigation.popBackStack()
            }

            MainAnimation()

            Spacer(modifier = Modifier.padding(top = 8.dp))

            ShowDataSection(
                subject = "Email Address",
                text = viewModel.email.value,
                null
            )

            ShowDataSection(
                subject = "Address",
                text = viewModel.address.value
                ) {
                viewModel.showLocationDialog.value = true
            }

            ShowDataSection(
                subject = "Postal Code",
                text = viewModel.postalCode.value
            ) {
                viewModel.showLocationDialog.value = true
            }

            ShowDataSection(
                subject = "LoginTime",
                text = styleTime(viewModel.loginTime.value.toLong()) ,
                null
            )
            
            Button(
                onClick = {

                    Toast.makeText(context, "Hope to see you again :)", Toast.LENGTH_SHORT).show()
                    viewModel.signOut()

                    navigation.navigate(MyScreens.MainScreen.route) {
                        popUpTo(MyScreens.MainScreen.route) {
                            inclusive = true
                        }
                        navigation.popBackStack()
                        navigation.popBackStack()
                    }

                } ,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(top = 32.dp, bottom = 16.dp)
                    .height(50.dp),
            ) {
                Text(
                    text = "Sign Out" ,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            if (viewModel.showLocationDialog.value) {
                AddUserLocationDataDialog(
                    showSaveLocation = false,
                    onDismiss = { viewModel.showLocationDialog.value = false },
                    onSubmitClicked = { address , postalCode , _ ->
                        viewModel.setUserLocation(address , postalCode)
                    }
                )
            }


        }

    }


}


@Composable
fun AddUserLocationDataDialog(
    showSaveLocation: Boolean,
    onDismiss: () -> Unit,
    onSubmitClicked: (String, String, Boolean) -> Unit
) {

    val context = LocalContext.current
    val checkedState = remember { mutableStateOf(true) }
    val userAddress = remember { mutableStateOf("") }
    val userPostalCode = remember { mutableStateOf("") }
    val fraction = if (showSaveLocation) 0.41f else 0.34f

    Dialog(
        onDismissRequest = onDismiss,
    ) {

        Card(
            modifier = Modifier.fillMaxHeight(fraction).fillMaxWidth(0.85f),
            shape = ShapeDefaults.Medium ,
            colors = CardDefaults.cardColors(containerColor = Color.White) ,
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Add Location Data",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                NormalTextField(userAddress.value, hint = "Enter your Address") {
                    userAddress.value = it
                }

                NormalTextField(userPostalCode.value, "Enter your Postal code") {
                    userPostalCode.value = it
                }

                if (showSaveLocation) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                        )

                        Text(text = "Save To Profile")

                    }

                }


                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {

                        if (
                            (userAddress.value.isNotEmpty() || userAddress.value.isNotBlank()) &&
                            (userPostalCode.value.isNotEmpty() || userPostalCode.value.isNotBlank())
                        ) {
                            onSubmitClicked(
                                userAddress.value,
                                userPostalCode.value,
                                checkedState.value
                            )
                            onDismiss.invoke()
                        } else {
                            Toast.makeText(context, "please write first...", Toast.LENGTH_SHORT)
                                .show()
                        }


                    }) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileToolbar(
    onBackClicked: () -> Unit
) {

    TopAppBar(
        title = {
            Text(
                text = "My Profile",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 50.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClicked.invoke() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        )
    )

}

@Composable
fun MainAnimation() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.profile_anim)
    )

    LottieAnimation(
        modifier = Modifier
            .size(270.dp)
            .padding(top = 32.dp, bottom = 16.dp),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

}

@Composable
fun ShowDataSection(
    subject: String,
    text: String,
    onLocationClicked: (() -> Unit)?
) {

    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            .clickable { onLocationClicked?.invoke() },
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = subject,
            fontSize = 18.sp,
            color = Blue,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = text,
            modifier = Modifier.padding(top = 4.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Divider(color = Blue, thickness = 1.dp, modifier = Modifier.padding(top = 16.dp))

    }

}
