package com.cyberfeedforward.mycardmanager.ui.cards

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CardsRoute(
    viewModel: CardsViewModel = viewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            if (granted) {
                viewModel.onScanRequested()
            } else {
                viewModel.onScanError("Camera permission denied")
            }
        },
    )

    CardsScreen(
        uiState = uiState,
        onAddCard = viewModel::onAddCard,
        onRemoveCard = viewModel::onRemoveCard,
        onScan = {
            if (hasCameraPermission) {
                viewModel.onScanRequested()
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
    )

    if (uiState.isScannerVisible) {
        BarcodeScannerDialog(
            onBarcodeScanned = viewModel::onBarcodeScanned,
            onDismiss = viewModel::onScannerDismissed,
            onError = viewModel::onScanError,
        )
    }

    val scanResult = uiState.scanResult
    if (scanResult != null) {
        val message = when (scanResult) {
            is ScanResultUi.Success -> scanResult.value
            is ScanResultUi.Error -> scanResult.message
        }

        if (scanResult is ScanResultUi.Success) {
            ScanResultDialog(viewModel, message)
        }
        else {
            ScanFailDialog(viewModel, message)
        }
    }
}

@Composable
private fun ScanResultDialog(
    viewModel: CardsViewModel,
    message: String
) {
    AlertDialog(
        onDismissRequest = viewModel::onScanResultDismissed,
        confirmButton = {
            TextButton(onClick = viewModel::onScanResultDismissed) {
                Text(text = "OK")
            }
        },
        title = {
            Text(
                text = "Scanned code"
            )
        },
        text = {
            Text(text = message)
        },
    )
}

@Composable
private fun ScanFailDialog(
    viewModel: CardsViewModel,
    message: String
) {
    AlertDialog(
        onDismissRequest = viewModel::onScanResultDismissed,
        confirmButton = {
            TextButton(onClick = viewModel::onScanResultDismissed) {
                Text(text = "OK")
            }
        },
        title = {
            Text(
                text = "Scan failed"
            )
        },
        text = {
            Text(text = message)
        },
    )
}
