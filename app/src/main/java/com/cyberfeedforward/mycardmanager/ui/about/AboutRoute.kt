package com.cyberfeedforward.mycardmanager.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun AboutRoute(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                fontSize = 36.sp,
                text = "About",
                style = MaterialTheme.typography.headlineSmall,
                )
        }

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 20.sp,
            text = "Is your pocket filled with too many loyalty cards?"
                    + "\n\n"
                    + "Do you sometimes lose loyalty cards when you need it?"
                    + "\n\n"
                    + "This app allows you to leave your cards home and just use this app to manage them"
                    + "\n\n"
                    + "Just scan your cards and add them to your list"
                    + "\n\n"
                    + "When needed, select a card and scan it at checkout",
            )
    }
}
