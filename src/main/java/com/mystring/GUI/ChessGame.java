/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mystring.GUI;

/**
 *
 * @author MO_mo$aad
 */

import javax.swing.*;
import java.awt.*;
import com.mystring.Logic.Player;
import com.mystring.Logic.TimerManager;

public class ChessGame extends JFrame {
    private Board board;
    private JLabel player1TimerLabel, player2TimerLabel, scoreLabe1;
    private TimerManager timerManager;
    private Player player1, player2;

    public ChessGame() {
        setTitle("Chess Game");
        setSize(850, 950);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // إنشاء اللاعبين وإدارة الوقت
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        timerManager = new TimerManager(player1, player2);

        // لوحة الشطرنج
        board = new Board(player1, player2, timerManager);
        add(board, BorderLayout.CENTER);

        // لوحة التحكم
        initializeControlPanel();
    }

    private void initializeControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(3, 1));
        player1TimerLabel = new JLabel();
        player2TimerLabel = new JLabel();
        scoreLabe1 = new JLabel("Score: Player 1 - 0 | Player 2 - 0");

        controlPanel.add(player1TimerLabel);
        controlPanel.add(player2TimerLabel);
        controlPanel.add(scoreLabe1);

        add(controlPanel, BorderLayout.SOUTH);

        timerManager.setLabels(player1TimerLabel, player2TimerLabel);
    }
}

