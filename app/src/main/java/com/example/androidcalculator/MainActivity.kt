package com.example.androidcalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Represent whether the lastly pressed key is numeric or not
    private var lastNumeric: Boolean = false

    // If true, do not allow to add another DOT
    private var lastDot: Boolean = false

    private var tvContent: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvContent = findViewById(R.id.tvContent)
    }

    fun onDigit(view: View) {
        tvContent?.append((view as Button).text)
        lastNumeric = true
        lastDot = false
    }

    fun onClear(view: View) {
        tvContent?.text = ""
        lastNumeric = false
        lastDot = false
    }

    /**
     * Append . to the TextView
     */
    fun onDecimalPoint(view: View) {
        // If the last appended value is numeric then append(".") or don't.
        tvContent?.text?.let {
            if (lastNumeric && !lastDot && !isDotExist(it.toString())) {
                tvContent?.append(".")
                lastNumeric = false // Update the flag
                lastDot = true      // Update the flag
            } else if (!lastNumeric && !lastDot && !isDotExist(it.toString())) {
                tvContent?.append("0.")
                lastNumeric = false // Update the flag
                lastDot = true      // Update the flag
            }
        }
    }

    /**
     * Append +,-,*,/ operators to the TextView as per the Button.Text
     */
    fun onOperator(view: View) {
        tvContent?.text?.let {
            if (lastNumeric && !isOperatorAdded(it.toString())) {
                tvContent?.append((view as Button).text)
                lastNumeric = false // Update the flag
                lastDot = false     // Reset the DOT flag
            }
        }
    }

    /**
     * Change the sign of the number ( - -> +, + -> - )
     */
    fun onPrefixChange(view: View) {
        tvContent?.text?.let {
            if (lastNumeric) {
                if (it.startsWith("-")) {
                    tvContent?.text = it.substring(1)
                } else {
                    tvContent?.text = "-" + it
                }
            }
        }
    }

    fun onPercent(view: View) {
        tvContent?.text?.let {
            if (lastNumeric) {
                tvContent?.text = (it.toString().toDouble() / 100.0).toString()
            }
        }
    }

    /**
     * Calculate the output
     */
    fun onEqual(view: View) {
        // If the last input is a number only, solution can be found.
        if (lastNumeric) {
            // Read the textView value
            var tvValue = tvContent?.text.toString()
            var prefix = ""
            try {
                // Here if the value starts with '-' then we will separate it and perform the calculation with value.
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                // If the inputValue contains the Division operator
                when {
                    tvValue.contains("/") -> {
                        // Will split the inputValue
                        val splitValue = tvValue.split("/")

                        var one = splitValue[0] // Value One
                        val two = splitValue[1] // Value Two

                        // If the prefix is not empty then we will append it with first value i.e one.
                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        /**
                        * Here as the value one and two will be calculated based on the operator and
                        * if the result contains the zero after decimal point will remove it.
                        * And display the result to TextView
                        */
                        tvContent?.text =
                            removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                    }
                    tvValue.contains("*") -> {
                        val splitValue = tvValue.split("*")

                        var one = splitValue[0]
                        val two = splitValue[1]

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        tvContent?.text =
                            removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                    }
                    tvValue.contains("-") -> {
                        val splitValue = tvValue.split("-")

                        var one = splitValue[0]
                        val two = splitValue[1]

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        tvContent?.text =
                            removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                    }
                    tvValue.contains("+") -> {
                        val splitValue = tvValue.split("+")

                        var one = splitValue[0]
                        val two = splitValue[1]

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        tvContent?.text =
                            removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                    }
                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Remove the zero after decimal point
     * to make the result more readable
     */
    private fun removeZeroAfterDot(result: String): String {
        var value = result

        if (result.contains(".0")) {
            value = result.substring(0, result.length - 2)
        }

        return value
    }

    /**
     * Check whether any of the operator is used or not.
     */
    private fun isOperatorAdded(value: String): Boolean {
        /**
         * Here first we will check that if the value starts with "-" then will ignore it.
         * As it is the result value and perform further calculation.
         */
        return if (value.startsWith("-")) {
            false
        } else {
            (value.contains("/")
                    || value.contains("*")
                    || value.contains("-")
                    || value.contains("+"))
        }
    }

    /**
     * Check whether the value already contains a dot or not.
     */
    private fun isDotExist(value: String): Boolean {
        return value.contains(".")
    }

}