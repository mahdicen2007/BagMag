package com.mahdicen.bagmag.util

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineExceptionHandler
import java.text.SimpleDateFormat
import java.util.Calendar

val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    Log.v("error" , throwable.message.toString())
}

fun Modifier.clickWithRedRipple(color: Color, onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(color = color),
        onClick = onClick
    )
}

fun stylePrice(oldPrice :String) :String {

    // examples : 3'000 , 247 , 2'543'534
    if (oldPrice.length > 3) {

        val reversedPrice = oldPrice.reversed()
        var newPrice = ""

        for (i in oldPrice.indices) {
            newPrice += reversedPrice[i]
            if ( (i+1) % 3 == 0 ) {
                newPrice += ","
            }
        }

        val readyToGoPrice = newPrice.reversed()

        if (readyToGoPrice.first() == ',') {
            return readyToGoPrice.substring(1) + " Tomans"
        }

        return "$readyToGoPrice Tomans"

    }

    return "$oldPrice Tomans"

}

@SuppressLint("SimpleDateFormat")
fun styleTime(timeInMillis :Long) :String {

    val formatter = SimpleDateFormat("yyyy/MM/dd hh:mm")

    val calender = Calendar.getInstance()
    calender.timeInMillis = timeInMillis

    return formatter.format(calender.time)

}