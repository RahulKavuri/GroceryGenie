package com.klu.grocerygenie

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {

    private lateinit var totalAmountTextView: TextView
    private lateinit var payNowButton: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var addNewCard: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)

        totalAmountTextView = findViewById(R.id.totalAmount)
        payNowButton = findViewById(R.id.payNowButton)
        radioGroup = findViewById(R.id.paymentOptions)
        addNewCard = findViewById(R.id.addNewCard)

        // Get total amount from intent
        val total = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        totalAmountTextView.text = "₹$total"

        payNowButton.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            } else {
                val selectedRadio = findViewById<RadioButton>(selectedId)
                val intent = Intent(this, OrderSuccessActivity::class.java)
                startActivity(intent)
                finish()

            }
        }

        addNewCard.setOnClickListener {
            Toast.makeText(this, "Feature to add card coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}
