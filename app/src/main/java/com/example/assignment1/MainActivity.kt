package com.example.assignment1
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//import android.os.Bundle
//import android.util.Log
//import android.widget.EditText
//import android.widget.Button
//import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.core.content.ContextCompat
private var score = 0
private var holdPosition = 0
private lateinit var scoreTextView: TextView
private lateinit var climbButton: Button
private lateinit var fallButton: Button
private lateinit var resetButton: Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        scoreTextView = findViewById(R.id.scoreTextView)
        climbButton = findViewById(R.id.climbButton)
        fallButton = findViewById(R.id.fallButton)
        resetButton = findViewById(R.id.resetButton)

        // Restore saved state if available
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("score", 0)
            holdPosition = savedInstanceState.getInt("holdPosition", 0)
        }

        updateUI()

        // Set up button listeners
        climbButton.setOnClickListener {
            if (holdPosition < 9) {
                holdPosition++
                score += when (holdPosition) {
                    in 1..3 -> 1
                    in 4..6 -> 2
                    in 7..9 -> 3
                    else -> 0
                }
                Log.d("ClimbingApp", "Climb clicked. Hold: $holdPosition, Score: $score")
                updateUI()
            }
        }

        fallButton.setOnClickListener {
            if (holdPosition > 0 && holdPosition < 9) {
                score = maxOf(score - 3, 0)
                Log.d("ClimbingApp", "Fall clicked. Hold: $holdPosition, Score: $score")
                updateUI()
            }
        }

        resetButton.setOnClickListener {
            score = 0
            holdPosition = 0
            Log.d("ClimbingApp", "Reset clicked. Score: $score")
            updateUI()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score", score)
        outState.putInt("holdPosition", holdPosition)
    }

    private fun updateUI() {
        // Update score text view with the current score
        scoreTextView.text = getString(R.string.score_format, score)

        // Set the color of the score based on the current hold position
        scoreTextView.setTextColor(getScoreColor())

        // Enable/disable buttons based on the current state
        climbButton.isEnabled = holdPosition < 9
        fallButton.isEnabled = holdPosition > 0 && holdPosition < 9
        resetButton.isEnabled = holdPosition == 9 || score > 0
    }

    private fun getScoreColor(): Int {
        return when (holdPosition) {
            in 1..3 -> ContextCompat.getColor(this, R.color.blue_zone)
            in 4..6 -> ContextCompat.getColor(this, R.color.green_zone)
            in 7..9 -> ContextCompat.getColor(this, R.color.red_zone)
            else -> ContextCompat.getColor(this, R.color.default_color)
        }
    }
}
