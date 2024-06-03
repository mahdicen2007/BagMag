package com.mahdicen.bagmag.ui.features.cart

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahdicen.bagmag.R
import com.mahdicen.bagmag.model.data.Product
import com.mahdicen.bagmag.ui.features.IntroScreen
import com.mahdicen.bagmag.ui.features.product.DotsTyping
import com.mahdicen.bagmag.ui.features.profile.AddUserLocationDataDialog
import com.mahdicen.bagmag.ui.theme.BackgroundMain
import com.mahdicen.bagmag.ui.theme.BagMagTheme
import com.mahdicen.bagmag.ui.theme.CardViewBackground
import com.mahdicen.bagmag.util.EmptyProduct
import com.mahdicen.bagmag.util.MyScreens
import com.mahdicen.bagmag.util.NetworkChecker
import com.mahdicen.bagmag.util.PAYMENT_PENDING
import com.mahdicen.bagmag.util.stylePrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Preview(showBackground = true)
@Composable
fun CartPreview() {
    BagMagTheme {
        Surface(
            color = BackgroundMain, modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {

                CartItem(data = EmptyProduct,
                    isChangingNumber = Pair("", false),
                    onAddItemClicked = {},
                    onRemoveItemClicked = {},
                    onItemClicked = {})

                CartItem(data = EmptyProduct,
                    isChangingNumber = Pair("", false),
                    onAddItemClicked = {},
                    onRemoveItemClicked = {},
                    onItemClicked = {})

            }

        }
    }
}


@Composable
fun CartScreen() {

    val context = LocalContext.current
    val viewModel = getViewModel<CartViewModel>()

    val getDataDialogState = remember { mutableStateOf(false) }
    val navigation = getNavController()

    viewModel.loadCartData()

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 74.dp)
        ) {

            CartToolbar(
                onBackClicked = {
                    navigation.popBackStack()
                },
                onProfileClicked = {
                    navigation.navigate(MyScreens.ProfileScreen.route)
                }
            )

            if (viewModel.productList.value.isNotEmpty()) {

                CartList(
                    data = viewModel.productList.value,
                    isChangingNumber = viewModel.isChangingNumber.value,
                    onAddItemClicked = { viewModel.addItem(it) },
                    onRemoveItemClicked = { viewModel.removeItem(it) },
                    onItemClicked = {
                        navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

            } else {
                NoDataAnimation()
            }

        }

        PurchaseAll(totalPrice = viewModel.totalPrice.value.toString()) {

            if (viewModel.productList.value.isNotEmpty()) {

                val locationData = viewModel.getUserLocation()
                if (locationData.first == "click to add" || locationData.second == "click to add") {
                    getDataDialogState.value = true
                } else {

                    // Purchase =>
                    viewModel.purchaseAll(
                        locationData.first,
                        locationData.second
                    ) { success, link ->
                        if (success) {
                            Toast.makeText(context, "Pay Using ZarinPal", Toast.LENGTH_SHORT).show()

                            viewModel.setPaymentStatus(PAYMENT_PENDING)

                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            context.startActivity(intent)

                        } else {
                            Toast.makeText(context, "Problem in Payment", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            } else {
                Toast.makeText(context, "There is no product to purchase", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        if (getDataDialogState.value) {
            AddUserLocationDataDialog(
                showSaveLocation = true,
                onDismiss = { getDataDialogState.value = false },
                onSubmitClicked = { address, postalCode, isChecked ->

                    if (NetworkChecker(context).isInternetConnected) {

                        if (isChecked) {
                            viewModel.setUserLocation(address, postalCode)
                        }

                        // Purchase =>
                        viewModel.purchaseAll(address, postalCode) { success, link ->
                            if (success) {
                                Toast.makeText(context, "Pay Using ZarinPal", Toast.LENGTH_SHORT)
                                    .show()

                                viewModel.setPaymentStatus(PAYMENT_PENDING)

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                context.startActivity(intent)

                            } else {
                                Toast.makeText(context, "Problem in Payment", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    } else {
                        Toast.makeText(
                            context,
                            "Please Check internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            )
        }

    }

}

@Composable
fun PurchaseAll(
    totalPrice: String,
    onPurchaseClicked: () -> Unit
) {

    val configuration = LocalConfiguration.current

    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color.White,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .size(180.dp, 60.dp),
                onClick = {
                    if (NetworkChecker(context).isInternetConnected) {
                        onPurchaseClicked.invoke()
                    } else {
                        Toast.makeText(
                            context,
                            "Please check internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {

                Text(
                    text = "Let's Purchase",
                    color = Color.White,
                    modifier = Modifier.padding(4.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

            }

            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(ShapeDefaults.Large),
                color = CardViewBackground
            ) {
                Text(
                    text = stylePrice(totalPrice),
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(8.dp)
                )
            }

        }

    }

}

@Composable
fun NoDataAnimation() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.no_data)
    )

    LottieAnimation(
        modifier = Modifier,
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

}

@Composable
fun CartItem(
    data: Product,
    isChangingNumber: Pair<String, Boolean>,
    onAddItemClicked: (String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onItemClicked: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { onItemClicked.invoke(data.productId) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column {

            AsyncImage(
                model = data.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Column(
                modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 10.dp)
            ) {

                Text(
                    text = data.name, style = TextStyle(
                        fontSize = 16.sp, fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "From ${data.category} Group",
                    style = TextStyle(
                        fontSize = 15.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Product Authenticity Guarantee",
                    style = TextStyle(
                        fontSize = 15.sp
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Available in stock to ship",
                    style = TextStyle(
                        fontSize = 15.sp
                    )
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .clip(ShapeDefaults.Large),
                    color = CardViewBackground
                ) {

                    Text(
                        text = stylePrice(
                            (data.price.toInt() * (data.quantity ?: "1").toInt()).toString()
                        ),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(10.dp)
                    )

                }


                Card(
                    modifier = Modifier
                        .padding(0.dp)
                        .clip(ShapeDefaults.Medium)
                        .size(width = 125.dp, height = 75.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(width = 2.dp, color = Color.Blue)
                ) {

                    Row(
                        Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        if (data.quantity?.toInt() == 1) {

                            IconButton(onClick = {
                                onRemoveItemClicked.invoke(data.productId)
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            }

                        } else {

                            IconButton(onClick = {
                                onRemoveItemClicked.invoke(data.productId)
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_minus),
                                    contentDescription = null
                                )
                            }

                        }

                        if (isChangingNumber.first == data.productId && isChangingNumber.second) {
                            Text(
                                text = "...",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        } else {

                            Text(
                                text = data.quantity.toString(),
                                fontSize = 18.sp,
                                color = Color.Black
                            )

                        }


                        IconButton(onClick = { onAddItemClicked.invoke(data.productId) }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }

                    }

                }

            }

        }

    }

}

@Composable
fun CartList(
    data: List<Product>,
    isChangingNumber: Pair<String, Boolean>,
    onAddItemClicked: (String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onItemClicked: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(data.size) {

            CartItem(
                data = data[it],
                isChangingNumber = isChangingNumber,
                onAddItemClicked = onAddItemClicked,
                onRemoveItemClicked = onRemoveItemClicked,
                onItemClicked = onItemClicked
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartToolbar(
    onBackClicked: () -> Unit, onProfileClicked: () -> Unit
) {

    TopAppBar(title = {
        Text(
            text = "My Cart",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        )
    }, modifier = Modifier.fillMaxWidth(), navigationIcon = {
        IconButton(onClick = { onBackClicked.invoke() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack, contentDescription = null
            )
        }
    }, colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = Color.White
    ), actions = {
        IconButton(
            onClick = { onProfileClicked.invoke() },
            modifier = Modifier.padding(end = 10.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_person_small),
                contentDescription = null
            )
        }
    })

}