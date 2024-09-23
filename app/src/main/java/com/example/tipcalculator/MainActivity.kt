package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorApp()
        }
    }
}

private fun handleKeyPress(key: String, currentAmount: String): String {
    return when (key) {
        "x" -> currentAmount.dropLast(1) // Remove last character

        else -> currentAmount + key // Append key
    }
}

private fun Double.format(digits: Int) = "%.${digits}f".format(this)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculatorApp() {
    var amount by remember { mutableStateOf("") }
    val totalBill by remember { mutableStateOf("0.00") }
    val tipPercentages = listOf(15, 18, 20)
    var selectedTipPercentage by remember { mutableStateOf(tipPercentages.first()) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Title bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = "Tip Calculator", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        }

        // Main content area
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Calculated Tip and Total Bill
            val calculatedTip = amount.toDoubleOrNull()?.let { it * selectedTipPercentage / 100 }
            val calculatedTotalBill = calculatedTip?.let { it + (amount.toDoubleOrNull() ?: 0.0) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tip: $${calculatedTip?.format(2) ?: "0.00"}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp) // Increase font size
                )
                Text(text = ", ")
                Text(
                    text = "Total Bill: $${calculatedTotalBill?.format(2) ?: "0.00"}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp) // Increase font size
                )
            }

            // Amount input field without border
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Enter Amount") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                // Green underline
                Divider(
                    color = Color.Green,
                    thickness = 2.dp,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Reduced space here

            // Tip percentage buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                tipPercentages.forEach { percent ->
                    Button(
                        onClick = { selectedTipPercentage = percent },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTipPercentage == percent) Color(0xFF1EB980) else Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .padding(0.dp)
                    ) {
                        Text("$percent%", color = Color.White)
                    }
                }
            }
        }

        // Keyboard area at the bottom
        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
        ) {
            val keys = listOf(
                listOf("1", "2", "3", "-"),
                listOf("4", "5", "6", "sp"),
                listOf("7", "8", "9", "x"),
                listOf(".", "0", ".", "<-")
            )
            keys.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { key ->
                        Button(
                            onClick = {  when (key) {
                                "sp" -> amount += " " // Add space
                                "x" -> amount = handleKeyPress(key, amount) // Rem ove last character
                                // Append number or symbol
                            } },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when (key) {
                                    "-", "sp", "x" -> Color(0xFFB0B0B0)
                                    "<-" -> Color.Blue
                                    else -> Color.White
                                }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(1.dp)
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            when (key) {
                                "sp" -> Image(painter = painterResource(id = R.drawable.space), contentDescription = "Space")
                                "x" -> Image(painter = painterResource(id = R.drawable.delete), contentDescription = "Delete")
                                "-" -> Image(painter = painterResource(id = R.drawable.minus), contentDescription = "Minus")
                                "<-" -> Image(
                                    painter = painterResource(id = R.drawable.check),
                                    contentDescription = "Return",
                                    modifier = Modifier.size(38.dp),
                                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                                )
                                else -> Text(key, color = Color.Black, fontSize = 24.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalculatorApp()
}