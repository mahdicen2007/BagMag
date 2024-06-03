package com.mahdicen.bagmag.ui.features.product

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.mahdicen.bagmag.R
import com.mahdicen.bagmag.model.data.Comment
import com.mahdicen.bagmag.model.data.Product
import com.mahdicen.bagmag.ui.features.signUp.MainTextField
import com.mahdicen.bagmag.ui.theme.BackgroundMain
import com.mahdicen.bagmag.ui.theme.BagMagTheme
import com.mahdicen.bagmag.ui.theme.CardViewBackground
import com.mahdicen.bagmag.ui.theme.LightBlue
import com.mahdicen.bagmag.util.MyScreens
import com.mahdicen.bagmag.util.NetworkChecker
import com.mahdicen.bagmag.util.clickWithRedRipple
import com.mahdicen.bagmag.util.stylePrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Preview(showBackground = true)
@Composable
fun ProductPreview() {
    BagMagTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            ProductScreen("")
        }
    }
}


@Composable
fun ProductScreen(productID: String) {

    val context = LocalContext.current

    val viewModel = getViewModel<ProductViewModel>()
    val navigation = getNavController()

    viewModel.loadData(
        productID,
        NetworkChecker(context).isInternetConnected
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 56.dp),

            ) {

            ProductToolbar(
                productName = "Details",
                badgeNumber = viewModel.badgeNumber.value,
                onBackClicked = {
                    navigation.popBackStack() // remove on trans from BackStack
                },
                onCartClicked = {
                    if (NetworkChecker(context).isInternetConnected)
                        navigation.navigate(MyScreens.CartScreen.route)
                    else
                        Toast.makeText(context, "Please check the connection!", Toast.LENGTH_SHORT)
                            .show()
                }
            )

            val comments =
                if (NetworkChecker(context).isInternetConnected)
                    viewModel.comments.value
                else
                    listOf<Comment>()

            ProductItem(
                data = viewModel.thisProduct.value,
                comments = comments,
                onCategoryClicked = {
                    navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
                },
                onAddNewComment = {
                    viewModel.addNewComment(productID, it) { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            )

        }

        AddToCart(
            viewModel.thisProduct.value.price,
            viewModel.isAddingProduct.value
        ) {

            if (NetworkChecker(context = context).isInternetConnected) {
                viewModel.addProductToCart(productID) {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show()
            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductToolbar(
    productName: String,
    badgeNumber: Int,
    onBackClicked: () -> Unit,
    onCartClicked: () -> Unit
) {

    TopAppBar(
        title = {
            Text(
                text = productName,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = { onBackClicked.invoke() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
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
        }
    )

}

@Composable
fun ProductItem(
    data: Product,
    comments: List<Comment>,
    onCategoryClicked: (String) -> Unit,
    onAddNewComment: (String) -> Unit
) {

    Column {

        ProductDesign(data = data, onCategoryClicked)

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .padding(
                    vertical = 14.dp,
                    horizontal = 16.dp
                )
        )

        ProductDetail(data, comments.size.toString())

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
        )

        ProductComments(comments, onAddNewComment)

        Spacer(modifier = Modifier.height(15.dp))

    }

}

@Composable
fun ProductComments(
    comments: List<Comment>,
    addNewComment: (String) -> Unit
) {

    val showCommentDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (comments.isNotEmpty()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Comments",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )

            TextButton(onClick = {
                if (NetworkChecker(context = context).isInternetConnected)
                    showCommentDialog.value = true
                else
                    Toast
                        .makeText(context, "Connect to internet first!", Toast.LENGTH_SHORT)
                        .show()
            }) {
                Text(
                    text = "Add New Comment",
                    fontSize = 14.sp,
                    color = Color.Blue
                )
            }

        }

        comments.forEach {
            CommentBody(comment = it)
        }

    } else {

        TextButton(
            modifier = Modifier.padding(start = 16.dp),
            onClick = {
                if (NetworkChecker(context = context).isInternetConnected)
                    showCommentDialog.value = true
                else
                    Toast
                        .makeText(context, "Connect to internet first!", Toast.LENGTH_SHORT)
                        .show()
            },
        ) {
            Text(
                text = "Add New Comment",
                fontSize = 14.sp,
                color = Color.Blue
            )
        }

    }

    if (showCommentDialog.value) {

        AddCommentDialog(
            onDismiss = { showCommentDialog.value = false },
            onPositiveClick = {
                addNewComment.invoke(it)
            }
        )

    }

}

@Composable
fun AddCommentDialog(
    onDismiss: () -> Unit,
    onPositiveClick: (String) -> Unit
) {

    val context = LocalContext.current

    val userComment = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss.invoke() }) {

        Card(
            modifier = Modifier.fillMaxHeight(0.25f),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            shape = ShapeDefaults.Medium,
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Write your Comment",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                NormalTextField(
                    edtValue = userComment.value,
                    hint = "Write sth...",
                ) {
                    userComment.value = it
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = { onDismiss.invoke() }) {
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    TextButton(onClick = {
                        if (userComment.value.isNotEmpty() && userComment.value.isNotBlank()) {
                            if (NetworkChecker(context).isInternetConnected) {
                                onPositiveClick(userComment.value)
                                onDismiss.invoke()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please check your connection!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill in the blanks!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Text(text = "Comment")
                    }

                }

            }

        }

    }

}

@Composable
fun CommentBody(comment: Comment) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        shape = ShapeDefaults.Large,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {

            Text(
                text = comment.userEmail,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = comment.text,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 14.sp,
            )

        }

    }

}

@Composable
fun ProductDetail(data: Product, commentNumber: String) {

    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.padding(start = 20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_details_comment),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                val commentText =
                    if (NetworkChecker(context).isInternetConnected)
                        "$commentNumber Comments"
                    else
                        "No Internet connection!"

                Text(
                    text = commentText,
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 14.sp
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_details_material),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = data.material,
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 14.sp
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_details_sold),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = data.soldItem + " Sold",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 14.sp
                )

            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDesign(
    data: Product,
    onCategoryClicked: (String) -> Unit
) {

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            .clip(ShapeDefaults.Medium),
        model = data.imgUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )

    Text(
        text = data.name,
        modifier = Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    )

    Text(
        text = data.detailText,
        modifier = Modifier.padding(top = 6.dp, start = 20.dp, end = 20.dp),
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Justify
    )

    SuggestionChip(
        modifier = Modifier
            .padding(top = 8.dp, start = 20.dp),
        onClick = {
            onCategoryClicked.invoke(data.category)
        },
        label = {
            Text(
                text = data.category,
                fontSize = 12.sp
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = LightBlue,
            labelColor = Color.White,
        ),
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderWidth = 0.dp,
            borderColor = CardViewBackground,
            disabledBorderColor = CardViewBackground
        )
    )


}

@Composable
fun AddToCart(
    price: String,
    isAddingProduct: Boolean,
    onAddCartClicked: () -> Unit
) {

    val configuration = LocalConfiguration.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp),
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
                onClick = { onAddCartClicked.invoke() }
            ) {

                if (isAddingProduct) {
                    DotsTyping()
                } else {
                    Text(
                        text = "Add To Cart",
                        color = Color.White,
                        modifier = Modifier.padding(4.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

            }

            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(ShapeDefaults.Large),
                color = CardViewBackground
            ) {
                Text(
                    text = stylePrice(price),
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
fun DotsTyping() {

    val dotSize = 10.dp
    val delayUnit = 350
    val maxOffset = 10f

    @Composable
    fun Dot(
        offset: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        ), label = ""
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp

        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NormalTextField(
    edtValue: String, hint: String, onValueChanges: (String) -> Unit
) {

    OutlinedTextField(
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ),
        label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = onValueChanges,
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .padding(vertical = 10.dp),
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
