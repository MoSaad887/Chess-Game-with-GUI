/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mystring.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mystring.Logic.Player;
import com.mystring.Logic.TimerManager;
import com.mystring.Piece;

public class Board extends JPanel implements ActionListener {
    private final Player player1, player2;
    private final TimerManager timerManager;
    private Piece[][] board_Index;
    private Piece selectedPiece = null;
    private Piece whiteKing, blackKing;

    public Board(Player player1, Player player2, TimerManager timerManager) {
        this.player1 = player1;
        this.player2 = player2;
        this.timerManager = timerManager;

        setLayout(new GridLayout(8, 8));
        initializeBoard();
        drawBoard();
    }     

    private void initializeBoard() {
        board_Index = new Piece[8][8];

        // القطع السوداء
        board_Index[0][0] = new Piece("black_rook", 0, 0);
        board_Index[0][1] = new Piece("black_knight", 0, 1);
        board_Index[0][2] = new Piece("black_bishop", 0, 2);
        board_Index[0][3] = new Piece("black_queen", 0, 3);
        board_Index[0][4] = new Piece("black_king", 0, 4);
        board_Index[0][5] = new Piece("black_bishop", 0, 5);
        board_Index[0][6] = new Piece("black_knight", 0, 6);
        board_Index[0][7] = new Piece("black_rook", 0, 7);
        for (int i = 0; i < 8; i++) board_Index[1][i] = new Piece("black_pawn", 1, i);

        blackKing = board_Index[0][4];

        // القطع البيضاء
        for (int i = 0; i < 8; i++) board_Index[6][i] = new Piece("white_pawn", 6, i);
        board_Index[7][0] = new Piece("white_rook", 7, 0);
        board_Index[7][1] = new Piece("white_knight", 7, 1);
        board_Index[7][2] = new Piece("white_bishop", 7, 2);
        board_Index[7][3] = new Piece("white_queen", 7, 3);
        board_Index[7][4] = new Piece("white_king", 7, 4);
        board_Index[7][5] = new Piece("white_bishop", 7, 5);
        board_Index[7][6] = new Piece("white_knight", 7, 6);
        board_Index[7][7] = new Piece("white_rook", 7, 7);

        whiteKing = board_Index[7][4];
    }


    // رسم اللوحة
    private void drawBoard() {
    removeAll();
    for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
            JPanel square = new JPanel();
            square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);

            if (board_Index[row][col] != null) {
                // تحميل الصورة المناسبة بناءً على نوع القطعة
                String imagePath = "C:\\Users\\MO_mo$aad\\Documents\\NetBeansProjects\\Chess_game\\src\\main\\pieces\\" + board_Index[row][col].pieceType + ".png"; 
                ImageIcon originalIcon = new ImageIcon(imagePath);
                
                // تغيير حجم الصورة لتناسب الـ JLabel
                Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);
                
                JLabel pieceLabel = new JLabel(resizedIcon);
                pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
                square.add(pieceLabel);
            }

            final int r = row, c = col;
            square.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    handleSquareClick(r, c);
                }
            });
            add(square);
        }
    }
    revalidate();
    repaint();
}


    // عند الضغط على مربع
   private void handleSquareClick(int row, int col) {
    // التحقق إذا كان اللاعب يحاول اللعب في غير دوره
    if ((timerManager.isPlayer1Turn() && selectedPiece != null && !selectedPiece.pieceType.contains("white")) ||
        (!timerManager.isPlayer1Turn() && selectedPiece != null && !selectedPiece.pieceType.contains("black"))) {
        JOptionPane.showMessageDialog(this, "It's not your turn!");
        return; // الخروج من الدالة إذا كان اللاعب يحاول اللعب في غير دوره
    }

    if (selectedPiece == null) {
        // تحديد القطعة المحددة إذا كانت ضمن قطعة اللاعب الحالي
        if (board_Index[row][col] != null && 
            (timerManager.isPlayer1Turn() && board_Index[row][col].pieceType.contains("white")) ||
            (!timerManager.isPlayer1Turn() && board_Index[row][col].pieceType.contains("black"))) {
            selectedPiece = board_Index[row][col];
        }
    } else {
        // التحقق من الحركة الصحيحة
        if (isValidMove(selectedPiece, row, col)) {
            boolean captured = false;
            int points = 0;

            // إذا كانت هناك قطعة على المربع وتم التقاطها
            if (board_Index[row][col] != null) {
                if (!board_Index[row][col].pieceType.contains(selectedPiece.pieceType.split("_")[0])) {
                    captured = true;
                    Player currentPlayer = timerManager.isPlayer1Turn() ? player1 : player2;
                    points = board_Index[row][col].getPieceValue();
                    currentPlayer.addScore(points);
                }
            }

            // تحريك القطعة
            movePiece(selectedPiece, row, col);
            selectedPiece = null;
            timerManager.switchTurn(); // تبديل الدور

            if (captured) {
                // إذا تم التقاط قطعة و حصل اللاعب على نقاط
                if (points == 0) {
                    JOptionPane.showMessageDialog(this, "Congratulations! You Win!");
                    System.exit(0); // إغلاق البرنامج إذا كانت النتيجة فوز
                } else {
                    JOptionPane.showMessageDialog(this, "Piece captured! " + points + " points added.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid move!");
            selectedPiece = null;  // إعادة تعيين القطعة المحددة
        }
    }
    drawBoard();  // إعادة رسم اللوحة بعد الحركة
}



    // تحريك القطعة
    private void movePiece(Piece piece, int row, int col) {
        // حفظ الحالة الحالية
        Piece previousPiece = board_Index[row][col];
        int oldRow = piece.row;
        int oldCol = piece.col;
        
        // تحريك القطعة
        board_Index[piece.row][piece.col] = null;
        board_Index[row][col] = piece;
        piece.row = row;
        piece.col = col;
        
        // التحقق من الكش بعد الحركة
        if (isCheck(piece.pieceType.contains("white"))) {
            // إعادة اللوحة للحالة السابقة
            board_Index[oldRow][oldCol] = piece;
            board_Index[row][col] = previousPiece;
            piece.row = oldRow;
            piece.col = oldCol;
            
            JOptionPane.showMessageDialog(this, "هذه الحركة تضع الملك في وضع كش!");
            return;
        }

        // التحقق من الترقية للبيادق
        if (piece.pieceType.contains("pawn")) {
            handlePromotion(piece, row);
        }
    }


    // التحقق من صحة الحركة
    private boolean isValidMove(Piece piece, int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) return false; // خارج الحدود
        if (board_Index[row][col] != null && board_Index[row][col].pieceType.contains(piece.pieceType.split("_")[0])) return false; // نفس اللون
        String pieceType = piece.pieceType.split("_")[1];
        switch (pieceType) {
            case "pawn":
                return isValidPawnMove(piece, row, col);
            case "knight":
                return isValidKnightMove(piece, row, col);
            case "rook":
                return isValidRookMove(piece, row, col);
            case "bishop":
                return isValidBishopMove(piece, row, col);
            case "queen":
                return isValidQueenMove(piece, row, col);
            case "king":
                return isValidKingMove(piece, row, col);
            default:
                return false;
        }
    }

    // تحقق من حركات القطع
   private boolean isValidPawnMove(Piece piece, int row, int col) {
    int direction = piece.pieceType.contains("white") ? -1 : 1; // الأبيض يتحرك للأعلى، الأسود للأسفل
    int startRow = piece.pieceType.contains("white") ? 6 : 1; // الصف الابتدائي للبيدق

    // إذا كانت هذه هي أول حركة للبيدق
    if (piece.firstMove) {
        // حركة للأمام بمربع واحد
        if (row == piece.row + direction && col == piece.col && board_Index[row][col] == null) {
            piece.firstMove = false;  // بعد أول حركة، نمنع الحركة المكونة من مربعين
            return true;
        }
        // حركة للأمام بمربعين فقط في البداية
        if (row == piece.row + 2 * direction && col == piece.col && board_Index[row][col] == null && board_Index[piece.row + direction][col] == null) {
            piece.firstMove = false;  // بعد أول حركة، نمنع الحركة المكونة من مربعين
            return true;
        }
    } else {
        // بعد أول حركة، يتحرك البيدق بمربع واحد فقط
        if (row == piece.row + direction && col == piece.col && board_Index[row][col] == null) {
            return true;
        }
    }

    // التقط القطع بشكل مائل
    if (row == piece.row + direction && Math.abs(col - piece.col) == 1 && board_Index[row][col] != null && !board_Index[row][col].pieceType.contains(piece.pieceType.split("_")[0])) {
        return true;
    }

    return false; // إذا لم تنطبق أي من الشروط
}


   private boolean isValidKnightMove(Piece piece, int row, int col) {
        int rowDiff = Math.abs(piece.row - row);
        int colDiff = Math.abs(piece.col - col);
        
        // التحقق من نمط حركة الفارس
        boolean isValidKnightMove = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
        
        // التأكد من عدم وجود قطعة من نفس اللون في الوجهة
        if (isValidKnightMove && board_Index[row][col] != null) {
            return !board_Index[row][col].pieceType.contains(piece.pieceType.split("_")[0]);
        }
        
        return isValidKnightMove;
    }
    private boolean isCheck(boolean forWhite) {
        Piece targetKing = forWhite ? whiteKing : blackKing;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board_Index[row][col] != null && 
                    !board_Index[row][col].pieceType.contains(targetKing.pieceType.split("_")[0])) {
                    
                    // محاولة التحقق من إمكانية الوصول للملك
                    if (isValidMove(board_Index[row][col], targetKing.row, targetKing.col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void handlePromotion(Piece pawn, int row) {
        // يتم تنفيذ ترقية البيدق عند الوصول للصف الأخير
        if ((pawn.pieceType.contains("white") && row == 0) || 
            (pawn.pieceType.contains("black") && row == 7)) {
            
            String[] options = {"Queen", "Rook", "Bishop", "Knight"};
            String color = pawn.pieceType.split("_")[0];
            
            int choice = JOptionPane.showOptionDialog(this, 
                "اختر القطعة للترقية", 
                "ترقية البيدق", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, options, options[0]);
            
            switch(choice) {
                case 0:
                    board_Index[row][pawn.col] = new Piece(color + "_queen", row, pawn.col);
                    break;
                case 1:
                    board_Index[row][pawn.col] = new Piece(color + "_rook", row, pawn.col);
                    break;
                case 2:
                    board_Index[row][pawn.col] = new Piece(color + "_bishop", row, pawn.col);
                    break;
                case 3:
                    board_Index[row][pawn.col] = new Piece(color + "_knight", row, pawn.col);
                    break;
            }
        }
    }



    private boolean isValidRookMove(Piece piece, int row, int col) {
    // التحقق من أن الحركة على نفس الصف أو العمود
    if (row != piece.row && col != piece.col) {
        return false;
    }

    // التحقق من أن جميع المربعات بين الموقع الحالي والموقع الهدف فارغة
    if (row == piece.row) {
        // حركة أفقية
        int start = Math.min(piece.col, col) + 1;
        int end = Math.max(piece.col, col);
        for (int c = start; c < end; c++) {
            if (board_Index[row][c] != null) {
                return false;
            }
        }
    } else {
        // حركة رأسية
        int start = Math.min(piece.row, row) + 1;
        int end = Math.max(piece.row, row);
        for (int r = start; r < end; r++) {
            if (board_Index[r][col] != null) {
                return false;
            }
        }
    }

    // التحقق من أن الهدف لا يحتوي على قطعة من نفس اللون
    if (board_Index[row][col] != null && board_Index[row][col].pieceType.contains(piece.pieceType.split("_")[0])) {
        return false;
    }

    return true;
}


    private boolean isValidBishopMove(Piece piece, int row, int col) {
    // التحقق من أن الحركة قطرية
    if (Math.abs(piece.row - row) != Math.abs(piece.col - col)) {
        return false;
    }

    // التحقق من أن جميع المربعات بين الموقع الحالي والموقع الهدف فارغة
    int rowDirection = row > piece.row ? 1 : -1;
    int colDirection = col > piece.col ? 1 : -1;
    int currentRow = piece.row + rowDirection;
    int currentCol = piece.col + colDirection;

    while (currentRow != row && currentCol != col) {
        if (board_Index[currentRow][currentCol] != null) {
            return false;
        }
        currentRow += rowDirection;
        currentCol += colDirection;
    }

    // التحقق من أن الهدف لا يحتوي على قطعة من نفس اللون
    if (board_Index[row][col] != null && board_Index[row][col].pieceType.contains(piece.pieceType.split("_")[0])) {
        return false;
    }

    return true;
}


    private boolean isValidQueenMove(Piece piece, int row, int col) {
    // التحقق من أن الحركة قانونية كـ قلعة أو فيل
    boolean validRookMove = isValidRookMove(piece, row, col);
    boolean validBishopMove = isValidBishopMove(piece, row, col);

    // الملكة يمكنها التحرك إذا كانت الحركة قانونية كقلعة أو كفيل
    return validRookMove || validBishopMove;
}
    


    private boolean isValidKingMove(Piece piece, int row, int col) {
        int rowDiff = Math.abs(piece.row - row);
        int colDiff = Math.abs(piece.col - col);
        
        // التحقق من المسافة المسموح بها للملك
        boolean isValidDistance = rowDiff <= 1 && colDiff <= 1;
        
        // التأكد من عدم وجود قطعة من نفس اللون في الوجهة
        if (isValidDistance && board_Index[row][col] != null) {
            return !board_Index[row][col].pieceType.contains(piece.pieceType.split("_")[0]);
        }
        
        return isValidDistance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        drawBoard();
    }
}

