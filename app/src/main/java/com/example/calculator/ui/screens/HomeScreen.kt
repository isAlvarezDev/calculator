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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    modifier: Modifier = Modifier
) {
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
            HomeScreenTextItem(
                text = text,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
        Divider(
            thickness = dimensionResource(R.dimen.thickness),
            color = MaterialTheme.colorScheme.primary
        )
        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreenButtonList(
                onClickClear = onClickClear,
                onClickBackspace = onClickBackspace,
                onClickPercentage = onClickPercentage,
                onClickDigit = { onClickDigit(it) },
                onClickEqual = onClickEqual,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun HomeScreenTextItem(
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
    onClickClear: () -> Unit,
    onClickBackspace: () -> Unit,
    onClickPercentage: () -> Unit,
    onClickDigit: (String) -> Unit,
    onClickEqual: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Box(modifier = Modifier.weight(3f)) {
            LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = modifier, verticalArrangement = Arrangement.SpaceAround) {
                item {
                    HomeScreenTextButton(
                        text = LocalSymbolDataProvider.clearSymbol.value,
                        onClick = onClickClear,
                        isPrimaryColor = true
                    )
                }
                item { HomeScreenTextButton(
                    text = LocalSymbolDataProvider.backspaceSymbol.value,
                    onClick = onClickBackspace,
                    isPrimaryColor = true
                ) }
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
                        onClick = { onClickDigit(digit.number) },
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
                        onClick = { onClickDigit(digit.number) },
                        isPrimaryColor = false,
                        modifier = Modifier.clip(CircleShape)
                    )
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = modifier, verticalArrangement = Arrangement.SpaceAround) {
                items(LocalSymbolDataProvider.operands) { operand ->
                    HomeScreenTextButton(
                        text = operand.value,
                        onClick = { onClickDigit(operand.value) },
                        isPrimaryColor = true
                    )
                }
                item { HomeScreenEqualSymbolButton(
                    onClick = onClickEqual,
                ) }
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
        onClick = { isSpinningBackwards = !isSpinningBackwards },
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
private fun HomeScreenEqualSymbolButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
    ) {
        Text(
            text = LocalSymbolDataProvider.equalSymbol.value,
            style = MaterialTheme.typography.headlineLarge
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
            style = MaterialTheme.typography.headlineLarge
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
fun HomeScreenTextItemPreview() {
    HomeScreenTextItem(text = "1")
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