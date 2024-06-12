package com.example.calculatorapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: TextView
    private var operand1: Double = 0.0
    private var operand2: Double = 0.0
    private var currentOperation: Char = ' '
    private var pendingOperation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputField = findViewById(R.id.inputField)

        val buttons = listOf(
            R.id.button0 to "0", R.id.button1 to "1", R.id.button2 to "2",
            R.id.button3 to "3", R.id.button4 to "4", R.id.button5 to "5",
            R.id.button6 to "6", R.id.button7 to "7", R.id.button8 to "8",
            R.id.button9 to "9", R.id.buttonDot to ".", R.id.buttonAdd to "+",
            R.id.buttonSubtract to "-", R.id.buttonMultiply to "*", R.id.buttonDivide to "/",
            R.id.buttonEquals to "=", R.id.buttonClear to "AC", R.id.buttonPercent to "%"
        )

        buttons.forEach { (id, text) ->
            findViewById<Button>(id).setOnClickListener {
                Log.d("Calculator", "Button $text clicked")
                onButtonClick(text)
            }
        }
    }

    private fun onButtonClick(text: String) {
        when (text) {
            "AC" -> clear()
            "=" -> calculate()
            "+", "-", "*", "/", "%" -> setOperation(text[0])
            else -> appendNumber(text)
        }
    }

    private fun clear() {
        Log.d("Calculator", "Clear operation")
        inputField.text = ""
        operand1 = 0.0
        operand2 = 0.0
        currentOperation = ' '
        pendingOperation = false
    }

    private fun calculate() {
        val input = inputField.text.toString()
        if (input.isNotEmpty() && currentOperation != ' ') {
            operand2 = input.substringAfterLast(currentOperation).toDouble()
            Log.d("Calculator", "Calculating: $operand1 $currentOperation $operand2")
            val result = when (currentOperation) {
                '+' -> operand1 + operand2
                '-' -> operand1 - operand2
                '*' -> operand1 * operand2
                '/' -> if (operand2 != 0.0) operand1 / operand2 else {
                    inputField.text = "Error"
                    return
                }
                '%' -> operand1 % operand2
                else -> 0.0
            }
            inputField.text = result.toString()
            Log.d("Calculator", "Result: $result")
            currentOperation = ' '
            pendingOperation = false
        }
    }

    private fun setOperation(operation: Char) {
        val input = inputField.text.toString()
        if (input.isNotEmpty()) {
            if (pendingOperation) {
                calculate()
            }
            operand1 = input.toDouble()
            currentOperation = operation
            Log.d("Calculator", "Set operation: $currentOperation with operand1: $operand1")
            inputField.text = "$operand1 $currentOperation "
            pendingOperation = true
        }
    }

    private fun appendNumber(number: String) {
        if (pendingOperation) {
            inputField.append(number)
        } else {
            inputField.text = inputField.text.toString() + number
        }
    }
}
