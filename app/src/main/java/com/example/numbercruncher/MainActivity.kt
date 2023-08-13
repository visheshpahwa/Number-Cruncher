package com.example.numbercruncher

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var placeholderTextView: TextView
    private lateinit var answerTextView: TextView

    private var currentExpression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeholderTextView = findViewById(R.id.placeholder)
        answerTextView = findViewById(R.id.answer)

        // Set click listeners for number buttons
        val numberButtonIds = listOf(R.id.num0, R.id.num1, R.id.num2, R.id.num3, R.id.num4, R.id.num5, R.id.num6, R.id.num7, R.id.num8, R.id.num9, R.id.numDot)
        numberButtonIds.forEach { buttonId ->
            findViewById<View>(buttonId).setOnClickListener {
                appendToExpression((it as TextView).text.toString())
            }
        }

        // Set click listeners for operator buttons
        val operatorButtonIds = listOf(R.id.actionAdd, R.id.actionMinus, R.id.actionMultiply, R.id.actionDivide, R.id.actionEquals, R.id.actionBack)
        operatorButtonIds.forEach { buttonId ->
            findViewById<View>(buttonId).setOnClickListener {
                handleOperatorButton((it as TextView).text.toString())
            }
        }

        // Set click listener for clear button
        findViewById<View>(R.id.clear).setOnClickListener {
            clearExpression()
        }
        // Set click listener for brackets
        val openBracketButton = findViewById<TextView>(R.id.startBracket)
        val closeBracketButton = findViewById<TextView>(R.id.closeBracket)
        openBracketButton.setOnClickListener {
            appendToExpression("(")
        }

        closeBracketButton.setOnClickListener {
            appendToExpression(")")
        }
    }

    private fun appendToExpression(value: String) {
        currentExpression += value
        updateDisplay()
    }

    private fun handleOperatorButton(operator: String) {
        when (operator) {
            "=" -> evaluateExpression()
            "Back" -> deleteLastCharacter()
            else -> appendToExpression(" $operator ")
        }
    }

    private fun evaluateExpression() {
        try {
            val expression = ExpressionBuilder(placeholderTextView.text.toString()).build()
            val result = expression.evaluate()
            val longResult = result.toLong()
            if (result == longResult.toDouble()) {
                Toast.makeText(this, "Double", Toast.LENGTH_SHORT).show()
                answerTextView.text = longResult.toString()
            } else
                answerTextView.text = result.toString()

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show();

            Log.d("EXCEPTION", "Message: ${e.message}")
        }

    }


    private fun deleteLastCharacter() {
        if (currentExpression.isNotEmpty()) {
            currentExpression = currentExpression.dropLast(1)
            updateDisplay()
        }
    }
    private fun clearExpression() {
        currentExpression = ""
        clearResult()
        updateDisplay()
    }

    private fun clearResult() {
        answerTextView.text = ""
    }



    private fun updateDisplay() {
        placeholderTextView.text = currentExpression
    }
}