package com.mjkim.calculator


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mjkim.calculator.ui.theme.CalculatorTheme
import com.mjkim.calculator.ui.theme.calculatorShapes
import com.mjkim.calculator.ui.theme.calculatorTypography
import com.mjkim.calculator.ui.utils.formattedString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    val navController = rememberNavController()
    val viewModel = viewModel<MainViewModel>()
    NavHostScreen(viewModel = viewModel, navController = navController)
}

@Composable
fun NavHostScreen(viewModel: MainViewModel, navController: NavHostController) {
    val rank = viewModel.rankSate.value
    NavHost(navController = navController, startDestination = CalculatorScreen.Main.name) {
        composable(CalculatorScreen.Main.name) {
            MainScreen(viewModel) { h, w ->
                viewModel.calculate(h, w)
            }
        }
        composable("${CalculatorScreen.Result.name}/{rank}") { backEntryStack ->
            ResultScreen(viewModel, navController, backEntryStack.arguments?.getString("rank"))
        }
    }
}

enum class CalculatorScreen {
    Main, Result
}

@Composable
fun ResultScreen(viewModel: MainViewModel, navController: NavHostController, rank: String? = "") {
    Log.d("Result", "rankState = ${viewModel.rankSate.value.kor}")
    val imgId = when (rank) {
        Rank.OBESITY.kor -> R.drawable.img_pubao
        Rank.LOW.kor -> R.drawable.img_lesser_panda
        Rank.NORMAL.kor -> R.drawable.ic_smile
        else -> R.drawable.img_choi
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("당신은 ${rank}입니다!", style = calculatorTypography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = imgId),
            contentDescription = Rank.values().find { it.kor == rank }?.name,
            modifier = Modifier.size(100.dp),
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("뒤로가기")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel, onResultClicked: (Double, Double) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        val h = calculatorTextField(
            exText = viewModel.heightState.value?.formattedString() ?: "",
            title = "키",
            imeAction = ImeAction.Next
        )
        val w = calculatorTextField(
            exText = viewModel.weightState.value?.formattedString() ?: "",
            title = "몸무게",
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .widthIn(40.dp)
                .heightIn(20.dp)
                .align(Alignment.End),
            shape = calculatorShapes.small,
            onClick = {
                keyboardController?.hide()
                if (h.isNotEmpty() && w.isNotEmpty()) {
                    onResultClicked(h.toDouble(), w.toDouble())
                }

            }
        ) {
            Text("결과")
        }
    }
}

@Composable
fun calculatorTextField(exText: String = "", title: String, imeAction: ImeAction): String {
    val (text, setText) = remember {
        mutableStateOf(exText)
    }
    OutlinedTextField(
        value = text,
        onValueChange = setText,
        label = { Text(title) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction,
        ),
        modifier = Modifier.fillMaxWidth()
    )
    return text
}
