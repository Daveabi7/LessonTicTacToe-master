package com.example.lessontictactoe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var gameStarted by remember { mutableStateOf(false) }
        var boardSize by remember { mutableIntStateOf(3) }
        var playerXScore by remember { mutableIntStateOf(0) }
        var playerOScore by remember { mutableIntStateOf(0) }
        var showSizeDialog by remember { mutableStateOf(true) }
        var showGameOverDialog by remember { mutableStateOf(false) }
        var winner by remember { mutableStateOf("") }
        var gameResetKey by remember { mutableIntStateOf(0) }

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tic Tac Toe+",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (!gameStarted && !showSizeDialog) {
                Button(
                    onClick = { showSizeDialog = true },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Start Game")
                }
            }

            if (gameStarted) {
                ScoreDisplay(playerXScore = playerXScore, playerOScore = playerOScore)
                GameBoard(
                    boardSize = boardSize,
                    onGameEnd = { result ->
                        showGameOverDialog = true
                        winner = result
                        when (result) {
                            "X" -> playerXScore++
                            "O" -> playerOScore++
                        }
                    },
                    key = gameResetKey
                )
                GameControls(
                    onResetRound = { gameResetKey++ },
                    onNewGame = {
                        gameStarted = false
                        showSizeDialog = true
                        playerXScore = 0
                        playerOScore = 0
                    },
                    onShowScore = {
                        showGameOverDialog = true
                        winner = "Score"
                    }
                )
            }
        }

        if (showSizeDialog) {
            BoardSizeDialog(
                onSizeSelected = { size ->
                    boardSize = size
                    showSizeDialog = false
                    gameStarted = true
                },
                onDismiss = { showSizeDialog = false }
            )
        }

        if (showGameOverDialog) {
            GameOverDialog(
                winner = winner,
                playerXScore = playerXScore,
                playerOScore = playerOScore,
                onDismiss = { showGameOverDialog = false },
                onNextRound = {
                    showGameOverDialog = false
                    gameResetKey++
                }
            )
        }
    }
}

@Composable
fun BoardSizeDialog(
    onSizeSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Board Size",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { onSizeSelected(3) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "3x3")
                }

                Button(
                    onClick = { onSizeSelected(4) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "4x4")
                }

                Button(
                    onClick = { onSizeSelected(5) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "5x5")
                }
            }
        }
    }
}

@Composable
fun ScoreDisplay(playerXScore: Int, playerOScore: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Player X",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )
            Text(
                text = playerXScore.toString(),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Player O",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Blue
            )
            Text(
                text = playerOScore.toString(),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun GameBoard(
    boardSize: Int,
    onGameEnd: (String) -> Unit,
    key: Int
) {
    val field = remember(key) { mutableStateListOf(*Array(boardSize * boardSize) { "_" }) }
    var currentPlayer by remember(key) { mutableStateOf("X") }
    var timeLeft by remember(key) { mutableIntStateOf(10) }
    var gameOver by remember(key) { mutableStateOf(false) }

    LaunchedEffect(key1 = currentPlayer, key2 = gameOver) {
        if (!gameOver) {
            timeLeft = 10
            while (timeLeft > 0 && !gameOver) {
                delay(1000L)
                timeLeft--
            }
            if (timeLeft == 0 && !gameOver) {
                currentPlayer = if (currentPlayer == "X") "O" else "X"
            }
        }
    }

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Time left: $timeLeft sec",
            style = MaterialTheme.typography.bodyLarge,
            color = if (timeLeft <= 3) Color.Red else MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Current Player: $currentPlayer",
            style = MaterialTheme.typography.titleMedium,
            color = if (currentPlayer == "X") Color.Red else Color.Blue,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column {
            for (row in 0 until boardSize) {
                Row {
                    for (col in 0 until boardSize) {
                        val index = row * boardSize + col
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(2.dp)
                                .border(
                                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable {
                                    if (field[index] == "_" && !gameOver) {
                                        field[index] = currentPlayer
                                        if (checkWin(field, boardSize, currentPlayer)) {
                                            gameOver = true
                                            onGameEnd(currentPlayer)
                                        } else if (!field.contains("_")) {
                                            gameOver = true
                                            onGameEnd("Draw")
                                        } else {
                                            currentPlayer = if (currentPlayer == "X") "O" else "X"
                                            timeLeft = 10
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = field[index],
                                style = MaterialTheme.typography.titleLarge,
                                color = if (field[index] == "X") Color.Red else Color.Blue
                            )
                        }
                    }
                }
            }
        }
    }
}

fun checkWin(field: List<String>, boardSize: Int, player: String): Boolean {
    // Check rows
    for (row in 0 until boardSize) {
        var win = true
        for (col in 0 until boardSize) {
            if (field[row * boardSize + col] != player) {
                win = false
                break
            }
        }
        if (win) return true
    }

    // Check columns
    for (col in 0 until boardSize) {
        var win = true
        for (row in 0 until boardSize) {
            if (field[row * boardSize + col] != player) {
                win = false
                break
            }
        }
        if (win) return true
    }

    // Check diagonal 1
    var win = true
    for (i in 0 until boardSize) {
        if (field[i * boardSize + i] != player) {
            win = false
            break
        }
    }
    if (win) return true

    // Check diagonal 2
    win = true
    for (i in 0 until boardSize) {
        if (field[i * boardSize + (boardSize - 1 - i)] != player) {
            win = false
            break
        }
    }
    return win
}

@Composable
fun GameControls(
    onResetRound: () -> Unit,
    onNewGame: () -> Unit,
    onShowScore: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onResetRound,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(text = "Reset")
        }

        Button(
            onClick = onNewGame,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(text = "New Game")
        }

        Button(
            onClick = onShowScore,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(text = "Score")
        }
    }
}

@Composable
fun GameOverDialog(
    winner: String,
    playerXScore: Int,
    playerOScore: Int,
    onDismiss: () -> Unit,
    onNextRound: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (winner == "Score") {
                    Text(
                        text = "Current Score",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Player X: $playerXScore",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Player O: $playerOScore",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Blue,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } else {
                    Text(
                        text = if (winner == "Draw") "It's a Draw!" else "Player $winner Wins!",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = if (winner == "X") Color.Red else if (winner == "O") Color.Blue else MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = onNextRound,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = if (winner == "Score") "Close" else "Next Round")
                }
            }
        }
    }
}