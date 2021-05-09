package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    // this line declares top level variable in the class for binding object. Its defined in this level because it will be used in multiple methods in Main Activity class
    // lateinit keyword promises that your code will initialize the variable before using it. If it doesn't app will crash
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // this line initializes the object which we will use to access the views from our layout
        binding = ActivityMainBinding.inflate(layoutInflater)

        //instead of passing the layout resource id , this is going to specify that the root of hierarchy of views in our app is binding
        setContentView(binding.root)

        // what happen if we click on calculate button
        binding.calculateButton.setOnClickListener {

            // calls calculateTip() function
            calculateTip()
        }

        // to hide keyboard on Enter key
        binding.costOfServiceEditText.setOnKeyListener{
                view, keyCode, _ -> handleKeyEvent(view, keyCode)
        }
    }

    // function to calculate tip
    private fun calculateTip(){
        // get the cost of service user entered and convert it to string first
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        // now convert that String cost to double or null
        val cost = stringInTextField.toDoubleOrNull()

        // if user didn't enter anything or enter 0 and exit this function early
        if(cost == null || cost == 0.0){
            // call the displayTip() function
            displayTip(0.0)
            return
        }

        // to figure out which radio button is checked
        val tipPercentage = when(binding.tipOptions.checkedRadioButtonId){
            R.id.twenty_percent -> 0.20
            R.id.eighteen_percent -> 0.18
            else -> 0.15
        }

        // set the tip accordingly
        var tip = tipPercentage*cost

        // check whether round up switch is on or off and accordingly set tip
        if(binding.roundUpSwitch.isChecked){
            tip = kotlin.math.ceil(tip)
        }

        //call displayTip() method
        displayTip(tip)

    }

    // displayTip fun to display tip
    private fun displayTip(tip: Double){

        // to get currency sign and also add tip which we calculate earlier
        val formattedTip  = NumberFormat.getCurrencyInstance(Locale("en","in", "rs")).format(tip)
        binding.tipResult.text = getString(R.string.tip_amount,formattedTip)
    }

    // to hide keyboard on enter key
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}


