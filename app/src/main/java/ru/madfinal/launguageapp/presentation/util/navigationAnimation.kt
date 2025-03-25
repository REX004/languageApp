package ru.madfinal.launguageapp.presentation.util

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import ru.madfinal.launguageapp.R

fun NavController.navigateWithAnimation(destinationId: Int, args: Bundle? = null) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    this.navigate(destinationId, args, navOptions)
}