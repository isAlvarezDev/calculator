package com.example.calculator.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.calculator.data.local.LocalDigitDataProvider
import com.example.calculator.data.local.LocalSymbolDataProvider
import com.example.calculator.ui.theme.CalculatorTheme

@Composable
fun HomeScreen(
    value: String,
    onValueChange: (String) -> Unit,
    onClickClear: () -> Unit,
    onClickBackspace: () -> Unit,
    onClickPercentage: () -> Unit,
    onClickDigit: () -> Unit,
    onClickEqual: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxHeight(.5f)
                .fillMaxWidth()
        ) {
            HomeScreenTextField(
                onValueChange = onValueChange,
                value = value,
                modifier = Modifier.fillMaxSize()
            )
        }
        Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreenButtonList(
                onClickClear = onClickClear,
                onClickBackspace = onClickBackspace,
                onClickPercentage = onClickPercentage,
                onClickDigit = onClickDigit,
                onClickEqual = onClickEqual,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun HomeScreenTextField(
    onValueChange: (String) -> Unit,
    value: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = false,
        textStyle = MaterialTheme.typography.headlineMedium,
        modifier = modifier
    )
}

@Composable
private fun HomeScreenButtonList(
    onClickClear: () -> Unit,
    onClickBackspace: () -> Unit,
    onClickPercentage: () -> Unit,
    onClickDigit: () -> Unit,
    onClickEqual: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Box(modifier = Modifier.weight(3f)) {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                item {
                    HomeScreenTextButton(
                        text = LocalSymbolDataProvider.clearSymbol.value,
                        onClick = onClickClear,
                        isPrimaryColor = true
                    )
                }
                item {
                    HomeScreenBackspaceButton(
                        onClickImage = onClickBackspace,


                    )
                }
                item {
                    HomeScreenTextButton(
                        text = LocalSymbolDataProvider.percentageSymbol.value,
                        onClick = onClickPercentage,
                        isPrimaryColor = true
                    )
                }
                items(LocalDigitDataProvider.digits) { digit ->
                    HomeScreenTextButton(
                        text = digit.number,
                        onClick = onClickDigit,
                        isPrimaryColor = false
                    )
                }
                item {
                    HomeScreenSpinBorderAnimation(
                        modifier = Modifier.wrapContentSize(Alignment.Center)
                    )
                }
                items(LocalDigitDataProvider.zeroAndDot) { digit ->
                    HomeScreenTextButton(
                        text = digit.number,
                        onClick = onClickDigit,
                        isPrimaryColor = false,
                        modifier = Modifier.clip(CircleShape)
                    )
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            LazyVerticalGrid(columns = GridCells.Fixed(1)) {
                items(LocalSymbolDataProvider.operands) { operand ->
                    HomeScreenTextButton(
                        text = operand.value,
                        onClick = onClickDigit,
                        isPrimaryColor = true
                    )
                }
                item { HomeScreenEqualSymbolButton(onClick = onClickEqual) }
            }
        }
    }
}

@Composable
private fun HomeScreenSpinBorderAnimation(
    modifier: Modifier = Modifier
) {
    var isSpinningBackwards by rememberSaveable { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val borderWidth = 12.dp.value
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
    TextButton(
        onClick = { isSpinningBackwards = !isSpinningBackwards },
        modifier = Modifier
            .padding(start = 2.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = modifier
                .drawBehind {
                    rotate(rotationAnimation.value)
                    {
                        drawCircle(
                            brush =
                            if (!isSpinningBackwards) Brush.horizontalGradient(colors)
                            else Brush.horizontalGradient(reverseColors),
                            style = Stroke(borderWidth),
                        )
                    }
                }
                .padding(12.dp)

        )
    }
}

@Composable
private fun HomeScreenEqualSymbolButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
    ) {
        Text(
            text = LocalSymbolDataProvider.equalSymbol.value,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
private fun HomeScreenBackspaceButton(
    onClickImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClickImage,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.ic_outline_backspace),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
private fun HomeScreenTextButton(
    text: String,
    onClick: () -> Unit,
    isPrimaryColor: Boolean,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = if (isPrimaryColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inverseSurface,
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

@Preview(widthDp = 60, heightDp = 60)
@Composable
fun HomeScreenImageBorderAnimationPreview() {
    HomeScreenSpinBorderAnimation()
}

@Preview(widthDp = 60, heightDp = 60)
@Composable
fun HomeScreenEqualSymbolButtonPreview() {
    HomeScreenEqualSymbolButton(onClick = {})
}

@Preview
@Composable
fun HomeScreenTextFieldPreview() {
    HomeScreenTextField(onValueChange = {}, value = "1")
}

@Preview
@Composable
fun HomeScreenButtonListPreview() {
    HomeScreenButtonList(
        onClickClear = { /*TODO*/ },
        onClickBackspace = { /*TODO*/ },
        onClickPercentage = { /*TODO*/ },
        onClickDigit = { /*TODO*/ },
        onClickEqual = { /*TODO*/ }
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    CalculatorTheme(darkTheme = true) {
        HomeScreen(
            value = "1",
            onValueChange = {},
            onClickClear = { /*TODO*/ },
            onClickBackspace = { /*TODO*/ },
            onClickPercentage = { /*TODO*/ },
            onClickDigit = { /*TODO*/ },
            onClickEqual = { /*TODO*/ },
            modifier = Modifier.fillMaxSize()
        )
    }
}