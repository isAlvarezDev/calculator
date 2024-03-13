package com.example.calculator.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator.R

@Composable
fun HomeScreenImageBorderAnimation(
    modifier: Modifier = Modifier
) {
    var isSpinningBackwards by rememberSaveable { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val borderWidth = 20.dp.value
    val colors = listOf(
        Color(MaterialTheme.colorScheme.primary.value),
        Color(MaterialTheme.colorScheme.background.value),
        Color(MaterialTheme.colorScheme.secondary.value)
    )
    val reverseColors = listOf(
        Color(MaterialTheme.colorScheme.error.value),
        Color(MaterialTheme.colorScheme.background.value),
        Color(MaterialTheme.colorScheme.outline.value)
    )
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = if (!isSpinningBackwards) 0f else 360f,
        targetValue = if (!isSpinningBackwards) 360f else 0f,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        ), label = ""
    )
    TextButton(onClick = { isSpinningBackwards = !isSpinningBackwards }) {
        Image(
            painter = painterResource(R.drawable.ic_android),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                if (!isSpinningBackwards) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary
            ),
            modifier = modifier
                .drawBehind {
                    rotate(rotationAnimation.value)
                    {
                        drawCircle(
                            brush =
                            if (!isSpinningBackwards) Brush.horizontalGradient(colors)
                            else Brush.horizontalGradient(reverseColors),
                            style = Stroke(borderWidth)
                        )
                    }
                }
                .padding(10.dp)
                .clip(CircleShape)
        )
    }
}

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
    isPrimaryColor: Boolean,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(0.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = if (isPrimaryColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}


@Preview
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

@Preview(widthDp = 50, heightDp = 50)
@Composable
fun HomeScreenImageBorderAnimationPreview() {
    HomeScreenImageBorderAnimation()
}