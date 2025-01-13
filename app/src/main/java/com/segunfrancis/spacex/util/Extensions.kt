package com.segunfrancis.spacex.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.segunfrancis.spacex.util.SpaceXConstants.DATE_TIME_PATTERN
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import retrofit2.HttpException
import com.segunfrancis.spacex.R
import timber.log.Timber
import java.text.NumberFormat

fun Throwable.handleThrowable(): String {
    Timber.e(this)
    return when {
        this is UnknownHostException -> "Please check your internet connection and try again"
        this is HttpException && this.code() == 403 -> "You are unauthorized to make this action"
        this is HttpException && this.code() in 500..599 -> "Something went wrong.\nIt is not you, it is us"
        this is HttpException -> "Something went wrong.\n" +
                "It is not you, it is us. Try again"
        this is SocketTimeoutException -> "Please check your network connection.\nMake sure you are connected to a good network"
        else -> "Something went wrong"
    }
}

fun ImageView.loadImage(imageUrl: String?) {
    CircularProgressIndicator(context)
    Glide.with(this)
        .load(imageUrl)
        .placeholder(context.circularIndicator())
        .error(R.drawable.ic_image_load_error)
        .into(this)
}

private fun Context.circularIndicator(): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(this)
    circularProgressDrawable.apply {
        strokeWidth = 3.0F
        setColorSchemeColors(Color.GREEN, Color.rgb(216, 27, 96))
        centerRadius = 20.0F
        start()
    }
    return circularProgressDrawable
}

fun String.formatDateAndTime(): Pair<String, String> {
    return try {
        val defaultFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, Locale.getDefault())
        val accessor = defaultFormatter.parse(this)
        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
        val date = LocalDateTime.from(accessor).format(dateFormat)
        val time = LocalDateTime.from(accessor).format(timeFormat)
        date to time
    } catch (t: Throwable) {
        Timber.e(t)
        "" to ""
    }
}

fun Long.dateDifference(): Long {
    val timeInMillis = this * 1000
    val timeDifference = System.currentTimeMillis() - timeInMillis
    return (((timeDifference / 1000) / 60) / 60) / 24
}

fun Long.formatDateDifference(): String {
    return when {
        this > 1L -> "- ${NumberFormat.getNumberInstance().format(this)} days"
        this == 1L -> "- $this day"
        this == 0L -> "Today"
        this == -1L -> "$this day"
        else -> "${NumberFormat.getNumberInstance().format(this)} days"
    }
}

fun View.showMessage(message: String?, retryAction: () -> Unit) {
    message?.let {
        Snackbar.make(this, it, Snackbar.LENGTH_INDEFINITE).setAction("Retry") { retryAction() }
            .show()
    }
}

fun <T> List<T>.secondOrNull(): T? {
    return if (size >= 2) this[1] else null
}

fun NavController.safeNavigate(directions: NavDirections) {
   currentDestination?.getAction(directions.actionId)?.run { navigate(directions) }
}
