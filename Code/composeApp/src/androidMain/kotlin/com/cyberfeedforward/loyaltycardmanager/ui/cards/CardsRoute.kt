package com.cyberfeedforward.loyaltycardmanager.ui.cards

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlin.math.roundToInt
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyberfeedforward.loyaltycardmanager.ui.settings.SettingsViewModel
import com.cyberfeedforward.loyaltycardmanager.util.Logger
import java.io.File

@Composable
fun CardsRoute(
    viewModel: CardsViewModel = viewModel(),
    settingsViewModel: SettingsViewModel,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initStorage(
            ScanHistoryStorage(file = File(context.filesDir, "scanned_codes.json"))
        )
    }

    var editingName by rememberSaveable { mutableStateOf("") }
    var editingCode by rememberSaveable { mutableStateOf("") }
    var editingTypeName by rememberSaveable { mutableStateOf(ScannedCodeType.Barcode1D.name) }
    var editingOnlyNumbers by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.editingIndex) {
        val index = uiState.editingIndex
        if (index != null) {
            val scan = uiState.savedScans.getOrNull(index)
            if (scan != null) {
                editingName = scan.name
                editingCode = scan.code
                editingTypeName = scan.type.name
                editingOnlyNumbers = scan.onlyNumbers
            }
        }
    }

    // var hasCameraPermission by ... (no change to the rest of the file yet, but removing the LaunchedEffect that filters editingCode)


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
        val displayCode = remember(scan?.code, scan?.onlyNumbers) {
            if (scan == null) return@remember ""
            if (scan.onlyNumbers) scan.code.filter { it.isDigit() } else scan.code
        }
        val codeBitmap = remember(displayCode, scan?.type) {
            if (scan == null || displayCode.isBlank()) return@remember null
            generateCodeBitmapSafely(
                value = displayCode,
                type = scan.type,
            )
        }

        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }

        Dialog(
            onDismissRequest = viewModel::onDismissView,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = viewModel::onDismissView
                    ),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                            }
                        }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { /* prevent click-through to background Box */ }
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = scan?.name.orEmpty(),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (scan != null && codeBitmap != null) {
                                Image(
                                    bitmap = codeBitmap.asImageBitmap(),
                                    contentDescription = "Card Number",
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
                                if (scan.onlyNumbers && scan.code != displayCode) {
                                    Text(
                                        text = displayCode,
                                        fontSize = 20.sp,
                                    )
                                }
                                else {
                                    Text(
                                        text = scan.code,
                                        fontSize = 20.sp,
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            TextButton(onClick = viewModel::onDismissView) {
                                Text(text = "OK")
                            }
                        }
                    }
                }
            }
        }
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
        val codeBitmap = remember(editingCode, editingType, editingOnlyNumbers) {
            if (editingCode.isBlank()) return@remember null
            val codeToGenerate = if (editingOnlyNumbers) editingCode.filter { it.isDigit() } else editingCode
            generateCodeBitmapSafely(
                value = codeToGenerate,
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
                                onlyNumbers = editingOnlyNumbers,
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
                            contentDescription = "Card Number",
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
                        placeholder = { Text(text = "What is your Card Name?") },
                        isError = isNameError,
                        singleLine = true,
                    )

                    OutlinedTextField(
                        value = editingCode,
                        onValueChange = { editingCode = it },
                        label = { Text(text = "Card Number *") },
                        placeholder = { Text(text = "What is your Card Number?") },
                        isError = isCodeError,
                        singleLine = true,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Type:",
                            fontSize = 18.sp,
                        )

                        Box {
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = editingOnlyNumbers,
                            onCheckedChange = { editingOnlyNumbers = it }
                        )
                        Text(
                            text = "Show numbers only",
                            fontSize = 18.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Note:\n",
                            color = Red,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 10.dp),
                            )
                        Text(text = "Please verify your Card Number is correct before saving")
                    }
                }
            },
        )
    }

    if (uiState.isScannerVisible) {
        BarcodeScannerDialog(
            onBarcodeScanned = { value, type ->
                viewModel.onBarcodeScanned(
                    value = value,
                    type = type,
                    removeControlCharacters = settingsUiState.removeControlCharacters
                )
            },
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
    var onlyNumbers by rememberSaveable { mutableStateOf(true) }

    var scannedTypeName by rememberSaveable(type) { mutableStateOf(type.name) }
    val scannedType = remember(scannedTypeName) {
        ScannedCodeType.entries.firstOrNull { it.name == scannedTypeName }
            ?: ScannedCodeType.Barcode1D
    }
    var isTypeMenuExpanded by remember { mutableStateOf(false) }
    val isNameError = cardName.isBlank()
    val isCodeError = scannedCode.isBlank()

    val codeBitmap = remember(scannedCode, scannedType, onlyNumbers) {
        if (scannedCode.isBlank()) return@remember null
        val codeToGenerate = if (onlyNumbers) scannedCode.filter { it.isDigit() } else scannedCode
        generateCodeBitmapSafely(
            value = codeToGenerate,
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
                            onlyNumbers = onlyNumbers,
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
                    label = { Text(text = "Card Number *") },
                    placeholder = { Text(text = "What is your Card Number?") },
                    isError = isCodeError,
                    singleLine = true,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Type",
                        fontSize = 18.sp
                    )
                    Box {
                        TextButton(onClick = { isTypeMenuExpanded = true }) {
                            Text(
                                text = scannedType.label,
                                fontSize = 18.sp
                            )
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = onlyNumbers,
                        onCheckedChange = { onlyNumbers = it }
                    )
                    Text(
                        text = "Show numbers only",
                        fontSize = 18.sp
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Note:\n",
                        color = Red,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 10.dp),
                    )
                    Text(text = "Please verify your Card Number is correct before saving")
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
    }.onFailure { Logger.e("Failed to generate bitmap for $type", it) }
        .getOrNull()
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
