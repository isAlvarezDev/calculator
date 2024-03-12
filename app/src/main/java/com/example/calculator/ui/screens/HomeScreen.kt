package com.example.calculator.ui.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreenTextButton(
    text: String,
    onClick: () -> Unit,
    baseColor: Boolean
) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(
            text = text,
            color = if (baseColor) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview
@Composable
fun HomeScreenTextButtonPreview() {
    HomeScreenTextButton(
        text = "1",
        baseColor = false,
        onClick = {}
    )
}