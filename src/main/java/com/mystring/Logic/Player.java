/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mystring.Logic;

/**
 *
 * @author MO_mo$aad
 */

public class Player {
    private String name;
    private int timeRemaining; // الوقت المتبقي بالثواني
    private int score;
    public int points;
    // المُنشئ الافتراضي (مع تعيين الوقت لاحقًا)
    public Player(String name) {
        this.name = name;
        this.timeRemaining = 10 * 60; // الوقت الافتراضي 10 دقائق (بالثواني)
        this.score = 0;
    }

    // مُنشئ مخصص مع وقت محدد
    public Player(String name, int timeInMinutes) {
        this.name = name;
        this.timeRemaining = timeInMinutes * 60; // تحويل الوقت إلى ثوانٍ
        this.score = 0 ;
    }

    // إرجاع اسم اللاعب
    public String getName() {
        return name;
    }

    // إرجاع الوقت المتبقي
    public int getTimeRemaining() {
        return timeRemaining;
    }

    // إنقاص الوقت بمقدار ثانية واحدة
    public void decrementTime() {
        if (timeRemaining > 0) {
            timeRemaining--;
        }
    }

    // تحديث النقاط
    public void addScore(int points) {
        score +=points;
    }

    // إرجاع النقاط الحالية
    public int getScore() {
        return score;
    }

    // إعادة ضبط الوقت (في حال الحاجة لإعادة اللعبة أو بدء دور جديد)
    public void resetTime(int timeInMinutes) {
        this.timeRemaining = timeInMinutes * 60;
    }

    // عرض الوقت المتبقي بتنسيق "دقائق:ثواني"
    public String getFormattedTime() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}

