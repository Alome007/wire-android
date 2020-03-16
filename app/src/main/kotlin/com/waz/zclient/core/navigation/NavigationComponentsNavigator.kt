package com.waz.zclient.core.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import java.lang.ref.WeakReference

internal class NavigationComponentsNavigator(
    private val navController: NavController,
    activity: Activity?
) : Navigator {

    private val activityWeakRef = WeakReference<Activity>(activity)

    override fun navigateTo(navigationId: Int) {
        navController.navigate(navigationId)
    }

    override fun navigateTo(navigationId: Int, bundle: Bundle) {
        navController.navigate(navigationId, bundle)
    }

    //TODO: do we need a custom navigator (for backstack, etc.)
    override fun navigateTo(uri: Uri) {
        activityWeakRef.get()?.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}

fun Fragment.navigator(): Navigator = NavigationComponentsNavigator(findNavController(), activity)
