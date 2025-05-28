package com.example.tictactoevf
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.content.Intent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startGameButton)
        val pseudoInput = findViewById<EditText>(R.id.pseudoInput)

        startButton.setOnClickListener {
            val pseudo = pseudoInput.text.toString()
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("PSEUDO", pseudo)
            }
            startActivity(intent)
        }
    }
}
