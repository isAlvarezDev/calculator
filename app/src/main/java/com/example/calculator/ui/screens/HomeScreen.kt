package com.example.calculator.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculator.R
import com.example.calculator.data.local.LocalDigitDataProvider
import com.example.calculator.data.local.LocalSymbolDataProvider

@Composable
fun HomeScreen(
    text: String,
    onClickClear: () -> Unit,
    onClickBackspace: () -> Unit,
    onClickPercentage: () -> Unit,
    onClickDigit: (String) -> Unit,
    onClickEqual: () -> Unit,
    isSpinningBackwards: Boolean,
    onClickSpinning: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color =
        if (!isSpinningBackwards) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.error

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.homescreen_padding))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(.5f)
                .fillMaxWidth()
        ) {
            HomeScreenTextResult(
                text = text,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
        Divider(
            thickness = dimensionResource(R.dimen.thickness),
            color = color
        )
        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreenButtonList(
                text = text,
                onClickClear = onClickClear,
                onClickBackspace = onClickBackspace,
                onClickPercentage = onClickPercentage,
                onClickDigit = { onClickDigit(it) },
                onClickEqual = onClickEqual,
                isSpinningBackwards = isSpinningBackwards,
                onClickSpinning = onClickSpinning,
                modifier = Modifier.fillMaxSize(),
                color = color
            )
        }
    }
}

@Composable
private fun HomeScreenTextResult(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.inverseSurface,
        style = MaterialTheme.typography.displayLarge,
        textAlign = TextAlign.Right,
        modifier = modifier
    )
}

@Composable
private fun HomeScreenButtonList(
    text: String,
    color: Color,
    onClickClear: () -> Unit,
    onClickBackspace: () -> Unit,
    onClickPercentage: () -> Unit,
    onClickDigit: (String) -> Unit,
    onClickEqual: () -> Unit,
    onClickSpinning: () -> Unit,
    isSpinningBackwards: Boolean,
    modifier: Modifier = Modifier
) {
    Row {
        Box(modifier = Modifier.weight(3f)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = modifier,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                item {
                    HomeScreenTextActionButton(
                        text =
                        if (text == "") LocalSymbolDataProvider.clearSymbol.value
                        else "C",
                        color = color,
                        onClick = onClickClear,
                    )
                }
                item {
                    HomeScreenTextActionButton(
                        text = LocalSymbolDataProvider.backspaceSymbol.value,
                        color = color,
                        onClick = onClickBackspace,
                    )
                }
                item {
                    HomeScreenTextActionButton(
                        text = LocalSymbolDataProvider.percentageSymbol.value,
                        color = color,
                        onClick = onClickPercentage,
                    )
                }
                items(LocalDigitDataProvider.digits) { digit ->
                    HomeScreenTextDigitButton(
                        text = digit.number,
                        onClick = { onClickDigit(digit.number) },
                    )
                }
                item {
                    HomeScreenSpinBorderAnimation(
                        isSpinningBackwards = isSpinningBackwards,
                        onClickSpinning = onClickSpinning,
                        modifier = Modifier.wrapContentSize(Alignment.Center)
                    )
                }
                items(LocalDigitDataProvider.zeroAndDot) { digit ->
                    HomeScreenTextDigitButton(
                        text = digit.number,
                        onClick = { onClickDigit(digit.number) },
                    )
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = modifier,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                items(LocalSymbolDataProvider.operands) { operand ->
                    HomeScreenTextActionButton(
                        text = operand.value,
                        color = color,
                        onClick = { onClickDigit(operand.value) }
                    )
                }
                item {
                    HomeScreenEqualSymbolButton(
                        color = color,
                        onClick = onClickEqual
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreenSpinBorderAnimation(
    onClickSpinning: () -> Unit,
    isSpinningBackwards: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val borderWidth = dimensionResource(id = R.dimen.medium_padding)
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
        onClick = onClickSpinning,
        modifier = Modifier
            .padding(start = dimensionResource(id = R.dimen.small_padding))
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
                            style = Stroke(borderWidth.value),
                        )
                    }
                }
                .padding(dimensionResource(id = R.dimen.medium_padding))

        )
    }
}

@Composable
private fun HomeScreenEqualSymbolButton(
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = modifier
    ) {
        Text(
            text = LocalSymbolDataProvider.equalSymbol.value,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
private fun HomeScreenTextActionButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
private fun HomeScreenTextDigitButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.inverseSurface,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}


@Preview
@Composable
fun HomeScreenTextDigitButtonPreview() {
    HomeScreenTextDigitButton(
        text = "1",
        onClick = {}
    )
}

@Preview(widthDp = 60, heightDp = 60)
@Composable
fun HomeScreenImageBorderAnimationPreview() {
    HomeScreenSpinBorderAnimation(
        isSpinningBackwards = false,
        onClickSpinning = {}
    )
}

@Preview(widthDp = 60, heightDp = 60)
@Composable
fun HomeScreenEqualSymbolButtonPreview() {
    HomeScreenEqualSymbolButton(color =  Color.Magenta, onClick = {})
}

@Preview
@Composable
fun HomeScreenTextResultPreview() {
    HomeScreenTextResult(text = "1")
}

@Preview
@Composable
fun HomeScreenButtonListPreview() {
    HomeScreenButtonList(
        text = "",
        color = Color.LightGray,
        onClickClear = { /*TODO*/ },
        onClickBackspace = { /*TODO*/ },
        onClickPercentage = { /*TODO*/ },
        onClickDigit = { /*TODO*/ },
        onClickEqual = { /*TODO*/ },
        isSpinningBackwards = false,
        onClickSpinning = {}
    )
}