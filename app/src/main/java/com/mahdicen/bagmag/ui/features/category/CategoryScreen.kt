package com.mahdicen.bagmag.ui.features.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mahdicen.bagmag.R
import com.mahdicen.bagmag.model.data.Product
import com.mahdicen.bagmag.ui.features.IntroScreen
import com.mahdicen.bagmag.ui.features.main.CategoryItem
import com.mahdicen.bagmag.ui.theme.BackgroundMain
import com.mahdicen.bagmag.ui.theme.BagMagTheme
import com.mahdicen.bagmag.util.MyScreens
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import dev.burnoo.cokoin.viewmodel.getViewModel

@Preview(showBackground = true)
@Composable
fun CategoryPreview() {
    BagMagTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            CategoryScreen(category = "Backpack")
        }
    }
}

@Composable
fun CategoryScreen(category: String) {

    val viewModel = getViewModel<CategoryViewModel>()
    viewModel.loadDataByCategory(category)

    val navigation = getNavController()


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        CategoryToolbar(category)

        val data = viewModel.dataProductsCategory
        CategoryList(data.value) {
            navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
        }

    }


}

@Composable
fun CategoryList(data: List<Product>, onProductClicked: (String) -> Unit) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {

        items(data.size) {
            CategoryItemInCategory(data = data[it], onProductClicked = onProductClicked)
        }

    }

}

@Composable
fun CategoryItemInCategory(data: Product, onProductClicked: (String) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { onProductClicked.invoke(data.productId) },
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
                modifier = Modifier.padding(10.dp)
            ) {

                Text(
                    text = data.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = data.price + " Tomans",
                    style = TextStyle(
                        fontSize = 15.sp
                    )
                )

            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryToolbar(categoryName: String) {

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = categoryName,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 6.dp),
                textAlign = TextAlign.Center
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        ),
    )
}


