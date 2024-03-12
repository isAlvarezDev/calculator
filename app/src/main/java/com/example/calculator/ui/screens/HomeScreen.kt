package com.example.calculator.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator.R



@Composable
fun HomeScreenBackspaceButton(
    onClickImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClickImage,
        shape = CutCornerShape(0.dp),
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.ic_outline_backspace),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
fun HomeScreenTextButton(
    text: String,
    onClick: () -> Unit,
    isPrimaryColor: Boolean
) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(
            text = text,
            color = if (isPrimaryColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}


@Preview()
@Composable
fun HomeScreenTextButtonPreview() {
    HomeScreenTextButton(
        text = "1",
        isPrimaryColor = false,
        onClick = {}
    )
}

@Preview
@Composable
fun HomeScreenBackspaceButtonPreview() {
    HomeScreenBackspaceButton(onClickImage = { /*TODO*/ })
}


