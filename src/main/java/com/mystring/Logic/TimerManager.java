/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mystring.Logic;

/**
 *
 * @author MO_mo$aad
 */

import javax.swing.*;


import javax.swing.Timer;
import javax.swing.JOptionPane;


public class TimerManager {
    private Timer player1Timer;
    private Timer player2Timer;
    private boolean isPlayer1Turn;
    private JLabel player1TimerLabel;
    private JLabel player2TimerLabel;

    public TimerManager(Player player1, Player player2) {
        // افتراض أن الدور يبدأ باللاعب الأول
        isPlayer1Turn = true;

        // تهيئة مؤقت اللاعب الأول
        player1Timer = new Timer(1000, e -> {
            player1.decrementTime();
            updateTimerLabel(player1TimerLabel, player1);
            if (player1.getTimeRemaining() <= 0) {
                endGame("Player 2 wins by timeout!");
            }
        });

        // تهيئة مؤقت اللاعب الثاني
        player2Timer = new Timer(1000, e -> {
            player2.decrementTime();
            updateTimerLabel(player2TimerLabel, player2);
            if (player2.getTimeRemaining() <= 0) {
                endGame("Player 1 wins by timeout!");
            }
        });

        // بدء مؤقت اللاعب الأول
        player1Timer.start();
    }

    // تعيين المؤشرات (Labels) لتحديثها أثناء اللعبة
    public void setLabels(JLabel player1Label, JLabel player2Label) {
        this.player1TimerLabel = player1Label;
        this.player2TimerLabel = player2Label;
    }

    // تحديث النص الخاص بالوقت المتبقي في المؤشر
    private void updateTimerLabel(JLabel label, Player player) {
        if (label != null) {
            label.setText(player.getName() + " Time: " + player.getFormattedTime());
        }
    }

    // تبديل الأدوار بين اللاعبين
 public void switchTurn() {
    if (isPlayer1Turn) {
        player1Timer.stop(); // إيقاف مؤقت اللاعب الأول
        player2Timer.start(); // بدء مؤقت اللاعب الثاني
    } else {
        player2Timer.stop(); // إيقاف مؤقت اللاعب الثاني
        player1Timer.start(); // بدء مؤقت اللاعب الأول
    }
    isPlayer1Turn = !isPlayer1Turn; // تبديل الدور
}

    // إنهاء اللعبة برسالة
    private void endGame(String message) {
        stopTimers();
        JOptionPane.showMessageDialog(null, message);
        System.exit(0); // إغلاق البرنامج
    }

    // تحديد الدور الحالي
    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    // إيقاف كلا المؤقتين (في حال إنهاء اللعبة أو إعادة التشغيل)
    public void stopTimers() {
        if (player1Timer != null) player1Timer.stop();
        if (player2Timer != null) player2Timer.stop();
    }

    // إعادة ضبط المؤقتات (في حال إعادة اللعبة)
    public void resetTimers(Player player1, Player player2, int timeInMinutes) {
        stopTimers();
        player1.resetTime(timeInMinutes);
        player2.resetTime(timeInMinutes);
        updateTimerLabel(player1TimerLabel, player1);
        updateTimerLabel(player2TimerLabel, player2);
        isPlayer1Turn = true;
        player1Timer.start();
    }
}
