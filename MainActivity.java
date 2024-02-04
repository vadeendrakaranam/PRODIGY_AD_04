package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int GRID_SIZE = 3;
    private char[][] board = new char[GRID_SIZE][GRID_SIZE];
    private boolean playerXTurn = true;
    private boolean gameEnded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeBoard();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                @SuppressLint("DiscouragedApi") int buttonId = getResources().getIdentifier("button" + i + j, "id", getPackageName());
                Button button = findViewById(buttonId);

                // Set the onClick listener with a lambda expression
                button.setOnClickListener(view -> onCellClicked(button));
            }
        }

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(view -> resetGame());
    }

    public void onCellClicked(View view) {
        if (gameEnded) {
            Toast.makeText(this, "Game over! Please reset to play again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Button clickedButton = (Button) view;

        // Get the button's ID
        int buttonId = clickedButton.getId();

        // Calculate the row and column based on the button's ID
        int row = (buttonId - R.id.button00) / GRID_SIZE;
        int col = (buttonId - R.id.button00) % GRID_SIZE;

        if (board[row][col] == '\u0000') {  // Check if the cell is empty
            char currentPlayerSymbol = playerXTurn ? 'X' : 'O';
            board[row][col] = currentPlayerSymbol;
            clickedButton.setText(String.valueOf(currentPlayerSymbol));

            if (checkForWin(row, col)) {
                String winner = playerXTurn ? "Player X" : "Player O";
                Toast.makeText(this, winner + " wins!", Toast.LENGTH_SHORT).show();
                gameEnded = true;
            } else if (isBoardFull()) {
                Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show();
                gameEnded = true;
            } else {
                playerXTurn = !playerXTurn; // Switch turns
            }
        } else {
            Toast.makeText(this, "Cell already occupied. Choose another cell.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetGame() {
        initializeBoard();
        clearButtons();
        playerXTurn = true;
        gameEnded = false;
        Toast.makeText(this, "Game reset!", Toast.LENGTH_SHORT).show();
    }

    private void initializeBoard() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                board[i][j] = '\u0000';  // Empty cell represented by null character
            }
        }
    }

    private void clearButtons() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                @SuppressLint("DiscouragedApi") int buttonId = getResources().getIdentifier("button" + i + j, "id", getPackageName());
                Button button = findViewById(buttonId);
                button.setText("");
            }
        }
    }

    private boolean checkForWin(int row, int col) {
        // Check row
        if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
            return true;
        }
        // Check column
        if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
            return true;
        }
        // Check diagonals
        if (row == col && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        if (row + col == 2 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (board[i][j] == '\u0000') {
                    return false; // There is an empty cell, the board is not full
                }
            }
        }
        return true; // All cells are filled, it's a draw
    }

}
