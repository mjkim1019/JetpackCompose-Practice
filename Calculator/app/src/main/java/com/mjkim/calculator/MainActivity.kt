package com.mjkim.calculator


import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mjkim.calculator.ui.theme.CalculatorTheme
import com.mjkim.calculator.ui.theme.calculatorShapes
import com.mjkim.calculator.ui.theme.calculatorTypography

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
    NavHostScreen(navController = navController)
}

@Composable
fun NavHostScreen(navController: NavHostController) {
    NavHost(navController = navController, startDestination = CalculatorScreen.Main.name) {
        composable(CalculatorScreen.Main.name) {
            MainScreen { result ->
                navController.navigate("${CalculatorScreen.Result.name}/$result")
            }
        }
        composable("${CalculatorScreen.Result.name}/{rank}") { backEntryStack ->
            ResultScreen(navController, backEntryStack.arguments?.getString("rank"))
        }
    }
}

enum class CalculatorScreen {
    Main, Result
}

@Composable
fun ResultScreen(navController: NavHostController, rank: String? = "") {
    val imgId = when (rank) {
        Rank.OBESITY.kor -> R.drawable.pubao
        Rank.LOW.kor -> R.drawable.lesser_panda
        else -> R.drawable.choi
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
            modifier = Modifier
                .widthIn(200.dp)
                .heightIn(200.dp),
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
fun MainScreen(onNavigateToResult: (String) -> Unit) {
    val viewModel: MainViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        val h = calculatorTextField(title = "키", imeAction = ImeAction.Next)
        val w = calculatorTextField(title = "몸무게", imeAction = ImeAction.Done)
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
                onNavigateToResult(viewModel.rankSate.value.kor)
            }
        ) {
            Text("결과")
        }
    }
}

@Composable
fun calculatorTextField(title: String, imeAction: ImeAction): String {
    val (text, setText) = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = text,
        onValueChange = setText,
        label = { Text(title) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        modifier = Modifier.fillMaxWidth()
    )
    return text
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorTheme {
        ResultScreen(rememberNavController(), Rank.OBESITY.kor)
    }
}