package com.cyberfeedforward.loyaltycardmanager.ui.cards

import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.cyberfeedforward.loyaltycardmanager.ui.theme.LoyaltyCardManagerTheme
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun BarcodeScannerDialog(
    onBarcodeScanned: (String, ScannedCodeType) -> Unit,
    onDismiss: () -> Unit,
    onError: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val isInPreview = LocalInspectionMode.current

    val analysisExecutor = remember {
        CameraExecutors.newAnalysisExecutor()
    }

    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var scanHandled by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Scan code")
        },
        text = {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                factory = { factoryContext ->
                    PreviewView(factoryContext).also { created ->
                        created.scaleType = PreviewView.ScaleType.FILL_CENTER
                        created.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        previewView = created
                    }
                },
            )
        },
    )

    LaunchedEffect(previewView) {
        if (isInPreview) return@LaunchedEffect
        val view = previewView ?: return@LaunchedEffect

        bindCameraUseCases(
            context = context,
            previewView = view,
            onResult = { value, type ->
                if (!scanHandled) {
                    scanHandled = true
                    onBarcodeScanned(value, type)
                }
            },
            onError = onError,
            lifecycleOwner = lifecycleOwner,
            analysisExecutor = analysisExecutor,
        )
    }

    DisposableEffect(lifecycleOwner) {
        if (isInPreview) {
            onDispose { }
        } else {
            onDispose {
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener(
                    {
                        runCatching { cameraProviderFuture.get().unbindAll() }
                    },
                    ContextCompat.getMainExecutor(context),
                )

                runCatching { analysisExecutor.shutdownNow() }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BarcodeScannerDialogPreview() {
    LoyaltyCardManagerTheme {
        BarcodeScannerDialog(
            onBarcodeScanned = { _, _ -> },
            onDismiss = {},
            onError = {},
        )
    }
}

@SuppressLint("UnsafeOptInUsageError")
private fun bindCameraUseCases(
    context: Context,
    previewView: PreviewView,
    onResult: (String, ScannedCodeType) -> Unit,
    onError: (String) -> Unit,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    analysisExecutor: ExecutorService,
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener(
        {
            val cameraProvider = runCatching { cameraProviderFuture.get() }
                .getOrElse { ex ->
                    onError(ex.message ?: "Unable to open camera")
                    return@addListener
                }

            val preview = CameraPreview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            val analyzer = createBarcodeAnalyzer(
                executor = analysisExecutor,
                onResult = onResult,
                onError = onError,
            )

            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { it.setAnalyzer(analysisExecutor, analyzer) }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            runCatching {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, analysis)
            }.onFailure { ex ->
                onError(ex.message ?: "Unable to start camera")
            }
        },
        mainExecutor,
    )
}

@SuppressLint("UnsafeOptInUsageError")
private fun createBarcodeAnalyzer(
    executor: Executor,
    onResult: (String, ScannedCodeType) -> Unit,
    onError: (String) -> Unit,
): ImageAnalysis.Analyzer {
    val scanner = BarcodeScanning.getClient()

    return object : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage == null) {
                imageProxy.close()
                return
            }

            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener(executor) { barcodes ->
                    val first = barcodes.firstOrNull()
                    val value = first?.rawValue
                    if (!value.isNullOrBlank()) {
                        val type = if (first.format == Barcode.FORMAT_QR_CODE) {
                            ScannedCodeType.QrCode
                        } else {
                            ScannedCodeType.Barcode1D
                        }
                        onResult(value, type)
                    }
                }
                .addOnFailureListener(executor) { ex ->
                    onError(ex.message ?: "Scan failed")
                }
                .addOnCompleteListener(executor) {
                    imageProxy.close()
                }
        }
    }
}

internal object CameraExecutors {
    fun newAnalysisExecutor(): ExecutorService = Executors.newSingleThreadExecutor()
}


