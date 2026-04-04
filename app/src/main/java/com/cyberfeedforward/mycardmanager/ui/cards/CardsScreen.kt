package com.cyberfeedforward.mycardmanager.ui.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberfeedforward.mycardmanager.ui.theme.MyCardManagerTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun CardsScreen(
    uiState: CardsUiState,
    savedScans: List<ScanHistoryStorage.SavedScan>,
    onAddCard: () -> Unit,
    onRemoveCard: () -> Unit,
    onScan: () -> Unit,
    onEditScan: (index: Int) -> Unit,
    onDeleteScan: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Cards: ${uiState.cardCount}",
            style = MaterialTheme.typography.headlineSmall,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onAddCard) {
                Text(text = "Add")
            }
            Button(onClick = onRemoveCard) {
                Text(text = "Remove")
            }
            Button(onClick = onScan) {
                Text(text = "Scan")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                itemsIndexed(
                    items = savedScans,
                    key = { index, scan -> "$index:${scan.type.name}:${scan.code}:${scan.name}" },
                ) { index, scan ->
                    SavedScanGridItem(
                        scan = scan,
                        onEdit = { onEditScan(index) },
                        onDelete = { onDeleteScan(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedScanGridItem(
    scan: ScanHistoryStorage.SavedScan,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                )
                Text(
                    text = scan.name.ifBlank { "(Unnamed)" },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )

                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More actions",
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Edit") },
                            onClick = {
                                menuExpanded = false
                                onEdit()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Delete") },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            },
                        )
                    }
                }
            }

            Text(
                text = scan.code,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardsScreenPreview() {
    MyCardManagerTheme {
        CardsScreen(
            uiState = CardsUiState(
                cardCount = 3,
                isScannerVisible = false,
                scanResult = null,
            ),
            savedScans = listOf(
                ScanHistoryStorage.SavedScan(
                    name = "Costco",
                    code = "1234567890",
                    type = ScannedCodeType.Barcode1D,
                ),
                ScanHistoryStorage.SavedScan(
                    name = "My QR",
                    code = "666",
                    type = ScannedCodeType.QrCode,
                ),
            ),
            onAddCard = {},
            onRemoveCard = {},
            onScan = {},
            onEditScan = {},
            onDeleteScan = {},
        )
    }
}
