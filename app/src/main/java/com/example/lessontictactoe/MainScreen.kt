package com.example.lessontictactoe

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFF5C4033),
            background = Color(0xFFF5E8C7),
            surface = Color(0xFFEAD9A8),
            secondary = Color(0xFF3C2F2F),
            onPrimary = Color.White,
            onBackground = Color(0xFF3C2F2F),
            onSurface = Color(0xFF3C2F2F),
            onSecondary = Color.White
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var gameStarted by remember { mutableStateOf(false) }
            var boardSize by remember { mutableIntStateOf(3) }
            var playerXScore by remember { mutableIntStateOf(0) }
            var playerOScore by remember { mutableIntStateOf(0) }
            var showModeDialog by remember { mutableStateOf(true) }
            var showSizeDialog by remember { mutableStateOf(false) }
            var showGameOverDialog by remember { mutableStateOf(false) }
            var winner by remember { mutableStateOf("") }
            var gameResetKey by remember { mutableIntStateOf(0) }
            var vsBot by remember { mutableStateOf(false) }
            var playerTurn by remember { mutableStateOf(true) }

            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tic Tac Toe+",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (gameStarted) {
                    ScoreDisplay(playerXScore = playerXScore, playerOScore = playerOScore, vsBot = vsBot)
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
                        key = gameResetKey,
                        vsBot = vsBot,
                        playerTurn = playerTurn,
                        onPlayerTurnChange = { playerTurn = it }
                    )
                    GameControls(
                        onResetRound = {
                            gameResetKey++
                            playerTurn = true
                        },
                        onNewGame = {
                            gameStarted = false
                            showModeDialog = true
                            playerXScore = 0
                            playerOScore = 0
                            vsBot = false
                        },
                        onShowScore = {
                            showGameOverDialog = true
                            winner = "Score"
                        }
                    )
                }
            }
            if (showModeDialog) {
                GameModeDialog(
                    onModeSelected = { botMode ->
                        vsBot = botMode
                        showModeDialog = false
                        showSizeDialog = true
                    },
                    onDismiss = { showModeDialog = false }
                )
            }
            if (showSizeDialog) {
                BoardSizeDialog(
                    onSizeSelected = { size ->
                        boardSize = size
                        showSizeDialog = false
                        gameStarted = true
                        playerTurn = true
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
                        playerTurn = true
                    },
                    vsBot = vsBot
                )
            }
        }
    }
}

@Composable
fun GameModeDialog(
    onModeSelected: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Game Mode",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(
                    onClick = { onModeSelected(false) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "2 Players")
                }
                Button(
                    onClick = { onModeSelected(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Play vs Bot")
                }
            }
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
            shape = RoundedCornerShape(8.dp),
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
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
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
fun ScoreDisplay(playerXScore: Int, playerOScore: Int, vsBot: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (vsBot) "You (X)" else "Player X",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )
            Text(
                text = playerXScore.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (vsBot) "Bot (O)" else "Player O",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Blue
            )
            Text(
                text = playerOScore.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun GameBoard(
    boardSize: Int,
    onGameEnd: (String) -> Unit,
    key: Int,
    vsBot: Boolean,
    playerTurn: Boolean,
    onPlayerTurnChange: (Boolean) -> Unit
) {
    val field = remember(key) { mutableStateListOf(*Array(boardSize * boardSize) { "_" }) }
    var currentPlayer by remember(key) { mutableStateOf("X") }
    var timeLeft by remember(key) { mutableIntStateOf(10) }
    var gameOver by remember(key) { mutableStateOf(false) }
    val winningCells = remember(key) { mutableStateListOf<Int>() }

    LaunchedEffect(key1 = playerTurn, key2 = gameOver) {
        if (vsBot && !playerTurn && !gameOver) {
            delay(500)
            val emptyCells = field.mapIndexedNotNull { index, cell ->
                if (cell == "_") index else null
            }
            if (emptyCells.isNotEmpty()) {
                val botMove = findBestMove(field, boardSize, "O") ?: emptyCells.random()
                field[botMove] = "O"
                if (checkWin(field, boardSize, "O")) {
                    gameOver = true
                    winningCells.addAll(findWinningCells(field, boardSize, "O"))
                    onGameEnd("O")
                } else if (!field.contains("_")) {
                    gameOver = true
                    onGameEnd("Draw")
                } else {
                    currentPlayer = "X"
                    timeLeft = 10
                    onPlayerTurnChange(true)
                }
            }
        }
    }

    LaunchedEffect(key1 = currentPlayer, key2 = gameOver, key3 = playerTurn) {
        if (!gameOver) {
            if (playerTurn) {
                timeLeft = 10
                while (timeLeft > 0 && !gameOver && playerTurn) {
                    delay(1000L)
                    timeLeft--
                }
                if (timeLeft == 0 && !gameOver && playerTurn) {
                    if (vsBot) {
                        currentPlayer = "O"
                        onPlayerTurnChange(false)
                    } else {
                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                        onPlayerTurnChange(!playerTurn)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (vsBot && currentPlayer == "O") "Bot's turn" else "Time left: $timeLeft sec",
            style = MaterialTheme.typography.bodyLarge,
            color = if (timeLeft <= 3 && playerTurn) Color.Red else MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = if (vsBot) {
                if (playerTurn) "Your turn (X)" else "Bot's turn (O)"
            } else {
                "Current Player: $currentPlayer"
            },
            style = MaterialTheme.typography.titleMedium,
            color = if (currentPlayer == "X") Color.Red else Color.Blue,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column {
            for (row in 0 until boardSize) {
                Row {
                    for (col in 0 until boardSize) {
                        val index = row * boardSize + col
                        val isWinningCell = winningCells.contains(index)
                        val animatedColor by animateColorAsState(
                            targetValue = if (isWinningCell) {
                                if ((System.currentTimeMillis() / 500) % 2 == 0L) Color(0xFFF5E8C7) else Color.Transparent
                            } else Color.Transparent,
                            animationSpec = tween(500)
                        )

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(2.dp)
                                .border(
                                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                    RoundedCornerShape(2.dp)
                                )
                                .background(animatedColor)
                                .clickable(
                                    enabled = field[index] == "_" && !gameOver && (playerTurn || !vsBot)
                                ) {
                                    if (field[index] == "_" && !gameOver) {
                                        field[index] = currentPlayer
                                        if (checkWin(field, boardSize, currentPlayer)) {
                                            gameOver = true
                                            winningCells.addAll(findWinningCells(field, boardSize, currentPlayer))
                                            onGameEnd(currentPlayer)
                                        } else if (!field.contains("_")) {
                                            gameOver = true
                                            onGameEnd("Draw")
                                        } else {
                                            currentPlayer = if (currentPlayer == "X") "O" else "X"
                                            timeLeft = 10
                                            if (vsBot) {
                                                onPlayerTurnChange(false)
                                            } else {
                                                onPlayerTurnChange(!playerTurn)
                                            }
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

@Composable
fun GameControls(
    onResetRound: () -> Unit,
    onNewGame: () -> Unit,
    onShowScore: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onResetRound,
            modifier = Modifier
                .fillMaxWidth(0.8f)
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
                .fillMaxWidth(0.8f)
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
                .fillMaxWidth(0.8f)
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
    onNextRound: () -> Unit,
    vsBot: Boolean
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
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
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (vsBot) "You: $playerXScore" else "Player X: $playerXScore",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = if (vsBot) "Bot: $playerOScore" else "Player O: $playerOScore",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Blue,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } else {
                    Text(
                        text = when {
                            winner == "Draw" -> "It's a Draw!"
                            vsBot && winner == "X" -> "You Win!"
                            vsBot && winner == "O" -> "Bot Wins!"
                            else -> "Player $winner Wins!"
                        },
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

    var win = true
    for (i in 0 until boardSize) {
        if (field[i * boardSize + i] != player) {
            win = false
            break
        }
    }
    if (win) return true

    win = true
    for (i in 0 until boardSize) {
        if (field[i * boardSize + (boardSize - 1 - i)] != player) {
            win = false
            break
        }
    }
    return win
}

fun findWinningCells(field: List<String>, boardSize: Int, player: String): List<Int> {
    val winningCells = mutableListOf<Int>()

    for (row in 0 until boardSize) {
        var win = true
        for (col in 0 until boardSize) {
            if (field[row * boardSize + col] != player) {
                win = false
                break
            }
        }
        if (win) {
            for (col in 0 until boardSize) {
                winningCells.add(row * boardSize + col)
            }
            return winningCells
        }
    }

    for (col in 0 until boardSize) {
        var win = true
        for (row in 0 until boardSize) {
            if (field[row * boardSize + col] != player) {
                win = false
                break
            }
        }
        if (win) {
            for (row in 0 until boardSize) {
                winningCells.add(row * boardSize + col)
            }
            return winningCells
        }
    }

    var win = true
    for (i in 0 until boardSize) {
        if (field[i * boardSize + i] != player) {
            win = false
            break
        }
    }
    if (win) {
        for (i in 0 until boardSize) {
            winningCells.add(i * boardSize + i)
        }
        return winningCells
    }

    win = true
    for (i in 0 until boardSize) {
        if (field[i * boardSize + (boardSize - 1 - i)] != player) {
            win = false
            break
        }
    }
    if (win) {
        for (i in 0 until boardSize) {
            winningCells.add(i * boardSize + (boardSize - 1 - i))
        }
        return winningCells
    }
    return winningCells
}

fun findBestMove(field: List<String>, boardSize: Int, player: String): Int? {
    for (i in field.indices) {
        if (field[i] == "_") {
            val newField = field.toMutableList()
            newField[i] = player
            if (checkWin(newField, boardSize, player)) {
                return i
            }
        }
    }

    val opponent = if (player == "X") "O" else "X"
    for (i in field.indices) {
        if (field[i] == "_") {
            val newField = field.toMutableList()
            newField[i] = opponent
            if (checkWin(newField, boardSize, opponent)) {
                return i
            }
        }
    }

    return null
}