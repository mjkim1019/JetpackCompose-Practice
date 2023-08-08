package com.mjkim.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mjkim.calculator.ui.theme.CalculatorTheme
import com.mjkim.calculator.ui.theme.calculatorShapes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val h = mainTextField(title = "키")
        val w = mainTextField(title = "몸무게")
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .widthIn(40.dp)
                .heightIn(20.dp),
            shape = calculatorShapes.small,
            onClick = {
                keyboardController?.hide()
                viewModel.calculate(height = h.toDouble(), weight = w.toDouble())
                println("${viewModel.rankSate.value}")
            }
        ) {
            Text("결과")
        }
    }
}

@Composable
fun mainTextField(title: String): String {
    val (text, setText) = remember {
        mutableStateOf("")
    }
    TextField(
        value = text,
        onValueChange = setText,
        label = { Text(title) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
    return text
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorTheme {
        MainScreen()
    }
}