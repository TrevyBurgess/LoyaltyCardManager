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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberfeedforward.mycardmanager.R

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
        Text(
            fontSize = 36.sp,
            text = stringResource(R.string.about),
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
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

        Text(
            fontSize = 36.sp,
            text = stringResource(R.string.security),
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            fontSize = 20.sp,
            text = "Your data is stored locally. No data is collected."
            + "\n\n"
            + "All data will be lost when you uninstall the app",
        )
    }
}
