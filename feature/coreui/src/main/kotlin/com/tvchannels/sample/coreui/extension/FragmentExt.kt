package com.tvchannels.sample.coreui.extension

import android.content.DialogInterface
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tvchannels.sample.coreui.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


fun Fragment.getColor(@ColorRes resId: Int) = requireContext().getColorCompat(resId)

fun Fragment.getDimen(@DimenRes resId: Int) = requireContext().getDimen(resId)

fun Fragment.getDrawable(@DrawableRes resId: Int) = requireContext().getDrawableCompat(resId)

fun Fragment.getFont(@FontRes resId: Int) = requireContext().getFont(resId)

fun Fragment.showToast(message: CharSequence) = requireContext().showToast(message)

fun Fragment.openLink(link: String) = requireContext().openLink(link)

fun <T> Fragment.collectWhenStarted(flow: Flow<T>, block: suspend (value: T) -> Unit) =
    flow.flowWithLifecycle(lifecycle)
        .onEach(block)
        .launchIn(viewLifecycleOwner.lifecycleScope)
fun <T> Fragment.collectLatestWhenStarted(flow: Flow<T>, block: suspend (value: T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        flow.flowWithLifecycle(lifecycle).collectLatest { block(it) }
    }
}

fun <T> Fragment.collectWhenResumed(flow: Flow<T>, block: suspend (value: T) -> Unit) =
    flow.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
        .onEach(block)
        .launchIn(viewLifecycleOwner.lifecycleScope)

fun <T> Fragment.collectLatestWhenResumed(flow: Flow<T>, block: suspend (value: T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        flow.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED).collectLatest { block(it) }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : AppCompatActivity> Fragment.requireCurrentActivity() = requireActivity() as T

fun Fragment.permissionsLauncherAny(
    onGranted: (permissions: List<String>) -> Unit,
    onNotGranted: (denies: List<String>, rationales: List<String>) -> Unit = { _, _ -> },
) = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
) { permissions ->

    val permissionsPartition = permissions.entries.partition { !it.value }
    val notGrantedPermissions = permissionsPartition.first.map { it.key }
    val grantedPermissions = permissionsPartition.second.map { it.key }

    if (grantedPermissions.isNotEmpty()) {
        onGranted(grantedPermissions)
    } else {
        val deniesAndRationales = notGrantedPermissions.partition {
            shouldShowRequestPermissionRationale(it)
        }
        onNotGranted(deniesAndRationales.first, deniesAndRationales.second)
    }
}

fun Fragment.permissionsLauncherAll(
    onGranted: () -> Unit,
    onNotGranted: (denies: List<String>, rationales: List<String>) -> Unit = { _, _ -> },
) = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
) { permissions ->

    val notGrantedPermissions = permissions.entries.filter { !it.value }.map { it.key }

    if (notGrantedPermissions.isEmpty()) {
        onGranted()
    } else {
        val deniesAndRationales = notGrantedPermissions.partition {
            shouldShowRequestPermissionRationale(it)
        }
        onNotGranted(deniesAndRationales.first, deniesAndRationales.second)
    }
}

fun Fragment.permissionsLauncherEvery(
    onGranted: (permission: String) -> Unit,
    onDenied: (permission: String) -> Unit = {},
    onRationale: (permission: String) -> Unit = {}
) = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
) { permissions ->
    permissions.forEach { (permission, isGranted) ->
        if (isGranted) {
            onGranted(permission)
        } else if (!shouldShowRequestPermissionRationale(permission)) {
            onDenied(permission)
        } else {
            onRationale(permission)
        }
    }
}

inline fun Fragment.errorDialog(
    title: String = getString(R.string.dialogErrorTitle),
    message: String = getString(R.string.dialogErrorContent),
    buttonText: String = getString(R.string.dialogOk),
    isCancellable: Boolean = false,
    crossinline onPositiveClick: (DialogInterface) -> Unit
) =
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            buttonText
        ) { dialog, _ ->
            dialog.dismiss()
            onPositiveClick.invoke(dialog)
        }.setCancelable(isCancellable)
        .create()

