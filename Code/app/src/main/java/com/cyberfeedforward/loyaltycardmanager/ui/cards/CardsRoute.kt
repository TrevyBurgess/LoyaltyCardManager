package com.cyberfeedforward.loyaltycardmanager.ui.cards

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File

@Composable
fun CardsRoute(
    viewModel: CardsViewModel = viewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initStorage(
            ScanHistoryStorage(file = File(context.filesDir, "scanned_codes.json"))
        )
    }

    var editingName by rememberSaveable { mutableStateOf("") }
    var editingCode by rememberSaveable { mutableStateOf("") }
    var editingTypeName by rememberSaveable { mutableStateOf(ScannedCodeType.Barcode1D.name) }

    LaunchedEffect(uiState.editingIndex) {
        val index = uiState.editingIndex
        if (index != null) {
            val scan = uiState.savedScans.getOrNull(index)
            if (scan != null) {
                editingName = scan.name
                editingCode = scan.code
                editingTypeName = scan.type.name
            }
        }
    }

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
        savedScans = uiState.savedScans,
        onScan = {
            if (hasCameraPermission) {
                viewModel.onScanRequested()
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        onEditScan = viewModel::onEditRequested,
        onDeleteScan = viewModel::onDeleteRequested,
        onCardClick = viewModel::onCardClick,
    )

    if (uiState.viewingIndex != null) {
        val scan = uiState.savedScans.getOrNull(uiState.viewingIndex!!)
        val codeBitmap = remember(scan?.code, scan?.type) {
            if (scan == null) return@remember null
            generateCodeBitmapSafely(
                value = scan.code,
                type = scan.type,
            )
        }

        AlertDialog(
            onDismissRequest = viewModel::onDismissView,
            confirmButton = {
                TextButton(onClick = viewModel::onDismissView) {
                    Text(text = "OK")
                }
            },
            title = {
                Text(text = scan?.name.orEmpty())
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (scan != null && codeBitmap != null) {
                        Image(
                            bitmap = codeBitmap.asImageBitmap(),
                            contentDescription = "Card code",
                            modifier = if (scan.type.isQr) {
                                Modifier.size(220.dp)
                            } else {
                                Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                            },
                        )
                    }

                    if (scan != null) {
                        Text(
                            text = scan.code,
                            fontSize = 20.sp,
                        )
                    }
                }
            },
        )
    }

    if (uiState.pendingDeleteIndex != null) {
        AlertDialog(
            onDismissRequest = viewModel::onDismissDelete,
            confirmButton = {
                TextButton(onClick = viewModel::onConfirmDelete) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onDismissDelete) {
                    Text(text = "No")
                }
            },
            title = {
                Text(text = "Delete")
            },
            text = {
                Text(text = "Are you sure you want to delete this card?")
            },
        )
    }

    if (uiState.editingIndex != null) {
        val editingType = remember(editingTypeName) {
            ScannedCodeType.entries.firstOrNull { it.name == editingTypeName }
                ?: ScannedCodeType.Barcode1D
        }
        val codeBitmap = remember(editingCode, editingType) {
            if (editingCode.isBlank()) return@remember null
            generateCodeBitmapSafely(
                value = editingCode,
                type = editingType,
            )
        }
        var isTypeMenuExpanded by remember { mutableStateOf(false) }
        val isNameError = editingName.isBlank()
        val isCodeError = editingCode.isBlank()

        AlertDialog(
            onDismissRequest = viewModel::onDismissEdit,
            confirmButton = {
                TextButton(
                    enabled = !isNameError && !isCodeError,
                    onClick = {
                        val index = uiState.editingIndex ?: return@TextButton
                        viewModel.onSaveEdit(
                            index = index,
                            scan = ScanHistoryStorage.SavedScan(
                                name = editingName,
                                code = editingCode,
                                type = editingType,
                            )
                        )
                    }
                ) {
                    Text(text = "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onDismissEdit) {
                    Text(text = "Cancel")
                }
            },
            title = {
                Text(text = "Edit")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (codeBitmap != null) {
                        Image(
                            bitmap = codeBitmap.asImageBitmap(),
                            contentDescription = "Card code",
                            modifier = if (editingType.isQr) {
                                Modifier.fillMaxWidth()
                            } else {
                                Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            },
                        )
                    }

                    OutlinedTextField(
                        value = editingName,
                        onValueChange = { editingName = it },
                        label = { Text(text = "Card Name *") },
                        isError = isNameError,
                        singleLine = true,
                    )

                    OutlinedTextField(
                        value = editingCode,
                        onValueChange = { editingCode = it },
                        label = { Text(text = "Card Code *") },
                        isError = isCodeError,
                        singleLine = true,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Type:",
                            modifier = Modifier.padding(top = 14.dp),
                            fontSize = 18.sp,
                        )

                        Column() {
                            TextButton(onClick = { isTypeMenuExpanded = true }) {
                                Text(
                                    text = editingType.label,
                                    fontSize = 18.sp,
                                )
                            }

                            DropdownMenu(
                                expanded = isTypeMenuExpanded,
                                onDismissRequest = { isTypeMenuExpanded = false },
                            ) {
                                ScannedCodeType.entries.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(text = type.label) },
                                        onClick = {
                                            editingTypeName = type.name
                                            isTypeMenuExpanded = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            },
        )
    }

    if (uiState.isScannerVisible) {
        BarcodeScannerDialog(
            onBarcodeScanned = viewModel::onBarcodeScanned,
            onDismiss = viewModel::onScannerDismissed,
            onError = viewModel::onScanError,
        )
    }

    val scanResult = uiState.scanResult
    if (scanResult != null) {
        when (scanResult) {
            is ScanResultUi.Success -> {
                ScanResultDialog(
                    viewModel = viewModel,
                    message = scanResult.value,
                    type = scanResult.type,
                )
            }
            is ScanResultUi.Error -> {
                ScanFailDialog(viewModel, scanResult.message)
            }
        }
    }
}

@Composable
private fun ScanResultDialog(
    viewModel: CardsViewModel,
    message: String,
    type: ScannedCodeType,
) {
    var cardName by rememberSaveable { mutableStateOf("") }
    var scannedCode by rememberSaveable(message) { mutableStateOf(message) }
    var scannedTypeName by rememberSaveable(type) { mutableStateOf(type.name) }
    val scannedType = remember(scannedTypeName) {
        ScannedCodeType.entries.firstOrNull { it.name == scannedTypeName }
            ?: ScannedCodeType.Barcode1D
    }
    var isTypeMenuExpanded by remember { mutableStateOf(false) }
    val isNameError = cardName.isBlank()
    val isCodeError = scannedCode.isBlank()

    val codeBitmap = remember(scannedCode, scannedType) {
        if (scannedCode.isBlank()) return@remember null
        generateCodeBitmapSafely(
            value = scannedCode,
            type = scannedType,
        )
    }

    AlertDialog(
        onDismissRequest = viewModel::onScanResultDismissed,
        confirmButton = {
            TextButton(
                enabled = !isNameError && !isCodeError,
                onClick = {
                    viewModel.onSaveNewScan(
                        ScanHistoryStorage.SavedScan(
                            name = cardName,
                            code = scannedCode,
                            type = scannedType,
                        )
                    )
                },
            ) {
                Text(text = "Save")
            }
        },
        title = {
            Text(
                text = "Scanned Card",
                fontSize = 25.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (codeBitmap != null) {
                    Image(
                        bitmap = codeBitmap.asImageBitmap(),
                        contentDescription = "Scanned code",
                        modifier = if (scannedType.isQr) {
                            Modifier.fillMaxWidth()
                        } else {
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        },
                    )
                }

                OutlinedTextField(
                    value = cardName,
                    onValueChange = { cardName = it },
                    label = { Text(text = "Card Name *") },
                    placeholder = { Text(text = "What is your Card Name?") },
                    isError = isNameError,
                    singleLine = true,
                )

                OutlinedTextField(
                    value = scannedCode,
                    onValueChange = { scannedCode = it },
                    label = { Text(text = "Card Code *") },
                    isError = isCodeError,
                    singleLine = true,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Type")
                    TextButton(onClick = { isTypeMenuExpanded = true }) {
                        Text(text = scannedType.label)
                    }

                    DropdownMenu(
                        expanded = isTypeMenuExpanded,
                        onDismissRequest = { isTypeMenuExpanded = false },
                    ) {
                        ScannedCodeType.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option.label) },
                                onClick = {
                                    scannedTypeName = option.name
                                    isTypeMenuExpanded = false
                                },
                            )
                        }
                    }
                }
            }
        },
    )
}

private fun generateCodeBitmapSafely(
    value: String,
    type: ScannedCodeType,
): Bitmap? {
    if (value.isBlank()) return null

    return runCatching {
        if (type.isQr) {
            CodeImageGenerator.generateQrBitmap(
                value = value,
                sizePx = 512,
            )
        } else {
            CodeImageGenerator.generateBarcodeBitmap(
                value = value,
                widthPx = 768,
                heightPx = 256,
            )
        }
    }.getOrNull()
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
