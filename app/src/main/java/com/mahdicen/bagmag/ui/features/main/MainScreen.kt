package com.mahdicen.bagmag.ui.features.main

import android.provider.CalendarContract.Colors
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mahdicen.bagmag.R
import com.mahdicen.bagmag.model.data.Ads
import com.mahdicen.bagmag.model.data.CheckOut
import com.mahdicen.bagmag.model.data.Product
import com.mahdicen.bagmag.model.repository.TokenInMemory
import com.mahdicen.bagmag.ui.theme.BackgroundMain
import com.mahdicen.bagmag.ui.theme.BagMagTheme
import com.mahdicen.bagmag.ui.theme.CardViewBackground
import com.mahdicen.bagmag.util.CATEGORY
import com.mahdicen.bagmag.util.MyScreens
import com.mahdicen.bagmag.util.NetworkChecker
import com.mahdicen.bagmag.util.PAYMENT_PENDING
import com.mahdicen.bagmag.util.PAYMENT_SUCCESS
import com.mahdicen.bagmag.util.TAGS
import com.mahdicen.bagmag.util.stylePrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import dev.burnoo.cokoin.viewmodel.getViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import java.util.logging.Handler
import javax.security.auth.Subject

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BagMagTheme {
        Surface(
            color = BackgroundMain, modifier = Modifier.fillMaxSize()
        ) {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {


    Log.v("testToken", TokenInMemory.token.toString())


    val context = LocalContext.current

    Log.v("testToken", NetworkChecker(context).isInternetConnected.toString())

    val sysUi = rememberSystemUiController()
    sysUi.setStatusBarColor(darkIcons = true, color = Color.White)
    sysUi.setNavigationBarColor(darkIcons = true, color = Color.White)

    // a VM with a parameters inside =>
    val viewModel = getViewModel<MainViewModel>(
        parameters = {
            parametersOf(
                NetworkChecker(context).isInternetConnected
            )
        }
    )

    val navigation = getNavController()

    if (NetworkChecker(context).isInternetConnected) {
        viewModel.loadBadgeNumber()
    }

    if (viewModel.getPaymentStatus() == PAYMENT_PENDING) {
        if (NetworkChecker(context).isInternetConnected) {
            viewModel.getCheckOutData()
        }
    }

    Box {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {

            if (viewModel.showProgressBar.value) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Blue
                )
            }

            TopToolBar(
                badgeNumber = viewModel.badgeNumber.value,
                onCartClicked = {
                    if (NetworkChecker(context).isInternetConnected)
                        navigation.navigate(MyScreens.CartScreen.route)
                    else
                        Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show()
                },
                onProfileClicked = {
                    navigation.navigate(MyScreens.ProfileScreen.route)
                }
            )

            CateGoryBar(CATEGORY) {
                navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
            }

            val productDataState = viewModel.dataProduct
            val adsDataState = viewModel.dataAds


            ProductSubjectList(TAGS, productDataState.value, adsDataState.value) {
                navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
            }

        }

        if (viewModel.showPaymentResultDialog.value) {

            PaymentResultDialog(checkoutResult = viewModel.checkOutData.value) {
                viewModel.showPaymentResultDialog.value = false
            }
        }

    }



}

@Composable
private fun PaymentResultDialog(
    checkoutResult: CheckOut,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {

        Card(
            shape = ShapeDefaults.Medium,
            elevation = CardDefaults.cardElevation(8.dp) ,
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Payment Result",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Main Data
                if (checkoutResult.order?.status?.toInt() == PAYMENT_SUCCESS) {

                    AsyncImage(
                        model = R.drawable.success_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(110.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + stylePrice(
                            (checkoutResult.order!!.amount).substring(
                                0,
                                (checkoutResult.order!!.amount).length - 1
                            )
                        )
                    )

                } else {

                    AsyncImage(
                        model = R.drawable.fail_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(top = 6.dp, bottom = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was not successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + stylePrice(
                            (checkoutResult.order!!.amount).substring(
                                0,
                                (checkoutResult.order.amount).length - 1
                            )
                        )
                    )

                }

                // Ok Button
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "ok")
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                }
            }
        }
    }
}



// --------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolBar(badgeNumber: Int, onCartClicked: () -> Unit, onProfileClicked: () -> Unit) {

    TopAppBar(title = {
        Text(
            text = "Bag Mag",
            fontWeight = Bold,
            fontFamily = FontFamily.SansSerif
        )
    },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        ),
        actions = {
            IconButton(
                onClick = { onCartClicked.invoke() },
                modifier = Modifier.padding(end = 10.dp),
            ) {
                if (badgeNumber == 0) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cart),
                        contentDescription = null
                    )
                } else {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text(text = badgeNumber.toString())
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cart),
                            contentDescription = null
                        )
                    }
                }
            }
            IconButton(onClick = { onProfileClicked.invoke() }) {
                Icon(painterResource(id = R.drawable.ic_person_small), null)
            }
        }
    )

}

// --------------------------------------------------------------------------------

@Composable
fun CateGoryBar(categoryList: List<Pair<String, Int>>, onCategoryClicked: (String) -> Unit) {

    // recycler in compose =>
    LazyRow(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(end = 20.dp)
    ) {

        items(categoryList.size) {
            CategoryItem(categoryList[it], onCategoryClicked)
        }

    }

}

@Composable
fun CategoryItem(subject: Pair<String, Int>, onCategoryClicked: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(start = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Surface(
            shape = ShapeDefaults.Medium,
            color = CardViewBackground,
            modifier = Modifier.clickable(onClick = { onCategoryClicked.invoke(subject.first) },
                indication = rememberRipple(color = Color.Gray, radius = 40.dp),
                interactionSource = remember { MutableInteractionSource() })

        ) {
            Image(
                painter = painterResource(subject.second),
                modifier = Modifier.padding(16.dp),
                contentDescription = null
            )
        }

        Text(
            text = subject.first, modifier = Modifier.padding(top = 4.dp), color = Color.Gray
        )

    }
}

// --------------------------------------------------------------------------------

@Composable
fun BigMiddlePoster(ad: Ads, onProductCLicked: (String) -> Unit) {

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(16.dp, 24.dp, 16.dp)
            .clip(ShapeDefaults.Medium)
            .clickable { onProductCLicked },
        model = ad.imageURL,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )

}

// --------------------------------------------------------------------------------


@Composable
fun ProductSubjectList(
    tags: List<String>,
    products: List<Product>,
    ads: List<Ads>,
    onProductClicked: (String) -> Unit
) {

    val context = LocalContext.current


    Column {

        tags.forEachIndexed { it, s ->

            val withTagData = products.filter { product ->
                product.tags == tags[it]
            }

            ProductSubject(
                tags[it],
                withTagData.shuffled(),
                ads,
                onProductClicked
            )  // shuffled() will randomize the order

            if (ads.size >= 2) {
                if (it == 1 || it == 2) {
                    BigMiddlePoster(ads[it - 1], onProductClicked)
                }
            }


        }

    }

}

@Composable
fun ProductSubject(
    subjectText: String,
    products: List<Product>,
    ads: List<Ads>,
    onProductCLicked: (String) -> Unit
) {

    Column(
        modifier = Modifier.padding(top = 32.dp)
    ) {

        Text(
            text = subjectText,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = Bold
        )

        ProductBar(products = products, onProductCLicked)

    }

}

@Composable
fun ProductBar(products: List<Product>, onProductCLicked: (String) -> Unit) {

    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(products.size) {
            ProductItem(products[it], onProductCLicked)
        }
    }

}

@Composable
fun ProductItem(product: Product, onProductCLicked: (String) -> Unit) {

    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onProductCLicked.invoke(product.productId) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column {

            // to work with Coil we need this type of image =>
            AsyncImage(
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop,
                model = product.imgUrl,
                contentDescription = null
            )

            Column(
                modifier = Modifier.padding(10.dp)
            ) {

                Text(
                    text = product.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = product.price + " Tomans",
                    style = TextStyle(
                        fontSize = 15.sp
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = product.soldItem + " have soled",
                    style = TextStyle(
                        fontSize = 13.sp,
                    ),
                    color = Color.Gray
                )

            }

        }

    }

}


// --------------------------------------------------------------------------------
