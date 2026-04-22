package com.cyberfeedforward.loyaltycardmanager2.ui.about

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberfeedforward.loyaltycardmanager2.R

@Preview(showBackground = true)
@Composable
fun AboutRoute(
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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
                    + "Do you sometimes lose loyalty cards when you need them?"
                    + "\n\n"
                    + "With Loyalty Card Manager, you just scan a loyalty card and add it to your list of cards."
                    + "\n\n"
                    + "At checkout, select a loyalty card and scan it.",
            )

        Text(
            fontSize = 36.sp,
            text = "Managing Cards",
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            fontSize = 20.sp,
            text = "To add a new card, click the camera button.",
        )

        Text(
            fontSize = 25.sp,
            text = "Note",
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
        )

        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            fontSize = 20.sp,
            text = "We use the Google's API to scan bar codes. "
                    + "However, sometimes it returns some strange characters."
                    + "\n\n"
                    + "Please make sure the card number is correct."
        )

        Text(
            fontSize = 36.sp,
            text = stringResource(R.string.security),
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            fontSize = 20.sp,
            text = "Your card info is stored on your phone. No data is collected."
            + "\n\n"
            + "All data will be lost when you uninstall the app",
        )
    }
}


