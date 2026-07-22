package com.cyberfeedforward.loyaltycardmanager.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// import com.loyaltycard.shared.R

@Preview(showBackground = true)
@Composable
fun AboutRoute(
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val isTablet = maxWidth >= 600.dp
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            /*Image(
                painter = painterResource(id = R.drawable.feature_graphic),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(if (isTablet) 0.5f else 1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.FillWidth,
            )*/

            Text(
                fontSize = 36.sp,
                text = "About", //stringResource(R.string.about),
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
                        + "At checkout, select a loyalty card and scan it."
                        + "\n\n"
                        + "- This app is forever free - no ads. \uD83E\uDD70",
            )

            Text(
                fontSize = 36.sp,
                text = "Security", //stringResource(R.string.security),
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                fontSize = 20.sp,
                text = "Your card info is stored on your phone."
                       + "\n\n"
                       + "We will never ask for your data.",
            )

            Text(
                fontSize = 36.sp,
                text = "Managing Cards",
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                fontSize = 20.sp,
                text = "To add a new card, click the camera button and scan the bar code on your card",
            )

            Text(
                fontSize = 25.sp,
                text = "Numbers Only",
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            )

            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                fontSize = 20.sp,
                text = "By default we show only numbers. "
                        + "You can toggle this behavior when you edit a card."
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
                        + "Please make sure the Card Number is correct. "
            )

            Text(
                fontSize = 36.sp,
                text = "Fun",
                color = MaterialTheme.colorScheme.primary,
            )

            val annotatedString = buildAnnotatedString {
                append("This project was created to help me get familiar with Google and iOS app development.")
                append("\n\n")
                append("I hope you enjoy it, and have fun \uD83D\uDE01")
                append("\n\n")
                append("This project is hosted on GitHub: ")
                withLink(
                    LinkAnnotation.Url(
                        "https://github.com/TrevyBurgess/LoyaltyCardManager",
                        TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline))
                    )
                ) {
                    append("LoyaltyCardManager")
                }
            }

            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                text = annotatedString,
                fontSize = 20.sp
            )

            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 64.dp),
                fontSize = 20.sp,
                text = "Trevy Burgess",
            )
        }
    }
}
