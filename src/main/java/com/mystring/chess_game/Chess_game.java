package com.mystring.chess_game;

import com.mystring.GUI.Board;
import com.mystring.Logic.Player;
import com.mystring.Logic.TimerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Chess_game {
    public static void main(String[] args) {
        // استخدام Look and Feel الخاص بالنظام
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // إنشاء الإطار الرئيسي
        JFrame inputFrame = new JFrame("Chess Game player");
        inputFrame.setSize(500, 400);
        inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inputFrame.setLocationRelativeTo(null); // توسيط النافذة

        // إنشاء لوحة الخلفية مع التدرج اللوني
        JPanel backgroundPanel = createStyledBackgroundPanel();

        // إضافة العناصر للوحة
        addPlayerInputComponents(inputFrame, backgroundPanel);

        // عرض النافذة
        inputFrame.add(backgroundPanel);
        inputFrame.setVisible(true);
    }

    private static JPanel createStyledBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                
                // إنشاء تدرج لوني جذاب
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(70, 70, 70), 
                    getWidth(), getHeight(), new Color(30, 30, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // إضافة تأثير التظليل الخفيف
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private static void addPlayerInputComponents(JFrame inputFrame, JPanel backgroundPanel) {
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ألوان مخصصة
        Color labelColor = new Color(200, 200, 200);
        Color inputColor = new Color(240, 240, 240);

        // إنشاء وتنسيق العناوين
        JLabel titleLabel = new JLabel("Chess Game Setup");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        backgroundPanel.add(titleLabel, gbc);

        // إعداد حقول اللاعبين
        gbc.gridwidth = 1;
        
        // اللاعب الأول
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel player1Label = createStyledLabel(" First Player:", labelColor);
        backgroundPanel.add(player1Label, gbc);

        gbc.gridx = 1;
        JTextField player1Field = createStyledTextField();
        backgroundPanel.add(player1Field, gbc);

        // اللاعب الثاني
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel player2Label = createStyledLabel("Second Player:", labelColor);
        backgroundPanel.add(player2Label, gbc);

        gbc.gridx = 1;
        JTextField player2Field = createStyledTextField();
        backgroundPanel.add(player2Field, gbc);

        // وقت اللعبة
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel timeLabel = createStyledLabel("Set Game time:", labelColor);
        backgroundPanel.add(timeLabel, gbc);

        gbc.gridx = 1;
        JTextField timeField = createStyledTextField();
        backgroundPanel.add(timeField, gbc);

        // زر البدء
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton startButton = createStyledButton("Start Game");
        backgroundPanel.add(startButton, gbc);

        // معالجة الضغط على زر البدء
        startButton.addActionListener(e -> {
            String player1Name = player1Field.getText().trim();
            String player2Name = player2Field.getText().trim();
            String gameTimeText = timeField.getText().trim();

            if (validateInputs(inputFrame, player1Name, player2Name, gameTimeText)) {
                int gameTime = Integer.parseInt(gameTimeText);
                
                // رسالة تأكيد
                JOptionPane.showMessageDialog(
                    inputFrame, 
                    String.format("سيتم بدء اللعبة:\nاللاعب الأول: %s\nاللاعب الثاني: %s\nالوقت: %d دقيقة", 
                    player1Name, player2Name, gameTime),
                    "تأكيد بدء اللعبة",
                    JOptionPane.INFORMATION_MESSAGE
                );

                inputFrame.dispose();
                startChessGame(player1Name, player2Name, gameTime);
            }
        });
    }

    private static JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(color);
        return label;
    }

    private static JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(50, 150, 50));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        
        // إضافة تأثير التحويم
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 180, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 150, 50));
            }
        });

        return button;
    }

    private static boolean validateInputs(JFrame parent, String player1, String player2, String time) {
        // التحقق من إدخال الأسماء
        if (player1.isEmpty() || player2.isEmpty()) {
            showErrorMessage(parent, "يجب إدخال أسماء اللاعبين");
            return false;
        }

        // التحقق من تشابه الأسماء
        if (player1.equalsIgnoreCase(player2)) {
            showErrorMessage(parent, "يجب أن يكون للاعبين أسماء مختلفة");
            return false;
        }

        // التحقق من طول الأسماء
        if (player1.length() < 2 || player2.length() < 2) {
            showErrorMessage(parent, "يجب أن يكون طول الاسم على الأقل حرفان");
            return false;
        }

        // التحقق من وقت اللعبة
        try {
            int gameTime = Integer.parseInt(time);
            if (gameTime < 1 || gameTime > 120) {
                showErrorMessage(parent, "يجب أن يكون وقت اللعبة بين 1 و 120 دقيقة");
                return false;
            }
        } catch (NumberFormatException ex) {
            showErrorMessage(parent, "برجاء إدخال وقت صحيح");
            return false;
        }

        return true;
    }

    private static void showErrorMessage(JFrame parent, String message) {
        JOptionPane.showMessageDialog(
            parent, 
            message, 
            "خطأ في الإدخال", 
            JOptionPane.ERROR_MESSAGE
        );
    }

    private static void startChessGame(String player1Name, String player2Name, int gameTime) {
        Player player1 = new Player(player1Name, gameTime);
        Player player2 = new Player(player2Name, gameTime);

        TimerManager timerManager = new TimerManager(player1, player2);

        // إعداد واجهة اللعبة
        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // لوحة التحكم (أسفل الشاشة)
        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JLabel player1TimerLabel = new JLabel(player1.getName() + " Time: " + gameTime + ":00", SwingConstants.CENTER);
        JLabel player2TimerLabel = new JLabel(player2.getName() + " Time: " + gameTime + ":00", SwingConstants.CENTER);

        player1TimerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        player2TimerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        controlPanel.add(player1TimerLabel);
        controlPanel.add(player2TimerLabel);

        timerManager.setLabels(player1TimerLabel, player2TimerLabel);

        // إعداد لوحة الشطرنج
        Board board = new Board(player1, player2, timerManager);

        // إضافة اللوحة ولوحة التحكم إلى الإطار
        frame.add(board, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}