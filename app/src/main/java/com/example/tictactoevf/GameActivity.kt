package com.example.tictactoevf
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    private lateinit var statusTextView: TextView
    private var gameActive = true
    private var activePlayer = 0
    private var gameState = IntArray(9) { 2 }

    private val winPositions = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        statusTextView = findViewById<TextView>(R.id.userPseudoDisplay)
        val pseudo = intent.getStringExtra("PSEUDO")
        statusTextView.text = "Tour de $pseudo"
    }

    fun cellTapped(view: View) {
        try {
            if (!gameActive) return

            val cell = view as ImageView
            val tappedCell = cell.tag.toString().toInt()

            if (gameState[tappedCell] != 2) return

            gameState[tappedCell] = activePlayer
            cell.setImageResource(if (activePlayer == 0) R.drawable.x else R.drawable.o)
            cell.isEnabled = false

            checkWinner()
            if (gameActive) {
                activePlayer = 1


                Handler(Looper.getMainLooper()).postDelayed({
                    computerTurn()

                    if (gameActive) {
                        activePlayer = 0
                    }
                }, 500)
            }
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in cellTapped", e)
        }
    }


    private fun computerTurn() {

        val winningCell = findStrategicCell(1)
        if (winningCell != null) {
            markCell(winningCell, 1)
            checkWinner()
            return
        }


        val blockingCell = findStrategicCell(0)
        if (blockingCell != null) {
            markCell(blockingCell, 1)
            return
        }


        val emptyCells = gameState.indices.filter { gameState[it] == 2 }
        if (emptyCells.isNotEmpty()) {
            val randomCell = emptyCells.random()
            markCell(randomCell, 1)
        }

        checkWinner()
    }


    private fun findStrategicCell(player: Int): Int? {
        winPositions.forEach { (a, b, c) ->
            if (gameState[a] == player && gameState[b] == player && gameState[c] == 2) {
                return c
            } else if (gameState[a] == player && gameState[c] == player && gameState[b] == 2) {
                return b
            } else if (gameState[b] == player && gameState[c] == player && gameState[a] == 2) {
                return a
            }
        }
        return null
    }


    private fun markCell(index: Int, player: Int) {
        gameState[index] = player

        val cellIdString = if (index < 9) "case0${index + 1}" else "case${index + 1}"
        val cellId = resources.getIdentifier(cellIdString, "id", packageName)
        val cell = findViewById<ImageView?>(cellId)
        if (cell != null) {
            cell.setImageResource(if (player == 0) R.drawable.x else R.drawable.o)
            cell.isEnabled = false
        } else {
            Log.e("GameActivity", "Cell with ID $cellId (resolved from $cellIdString) not found")
        }
    }




    @SuppressLint("SetTextI18n")
    private fun checkWinner() {
        for ((a, b, c) in winPositions) {
            if (gameState[a] == gameState[b] && gameState[b] == gameState[c]) {
                if (gameState[a] != 2) {
                    gameActive = false


                    val winnerStr = when (gameState[a]) {
                        1 -> "L'ordinateur a gagné !"
                        0 -> "Tu as gagné !"

                        else -> ""
                    }

                    statusTextView.text = winnerStr
                    return
                }
            }
        }


        if (!gameState.contains(2)) {
            gameActive = false
            statusTextView.text = "Match nul !"
        }
    }


    @SuppressLint("SetTextI18n")
    fun resetGame(view: View) {
        gameActive = true
        activePlayer = 0
        gameState.fill(2)


        for (i in 0..8) {

            val cellIdString = if (i < 9) "case0${i + 1}" else "case${i + 1}"
            val cellId = resources.getIdentifier(cellIdString, "id", packageName)
            val cell = findViewById<ImageView>(cellId)
            cell?.setImageResource(0)
            cell?.isEnabled = true
        }


        val pseudo = intent.getStringExtra("PSEUDO") ?: "Joueur"
        statusTextView.text = "Tour de $pseudo"
    }}

