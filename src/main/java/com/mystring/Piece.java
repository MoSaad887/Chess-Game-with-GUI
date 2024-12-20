/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mystring;

/**
 *
 * @author MO_mo$aad
 */
public class Piece {
    public String pieceType;  // نوع القطعة (مثل: "pawn_white", "rook_black")
    public int row, col; 
    public boolean firstMove;// الموقع الحالي للقطعة

    public Piece(String pieceType, int row, int col) {
        this.pieceType = pieceType;
        this.row = row;
        this.col = col;
        this.firstMove = true; // البيدق في البداية يمكنه التحرك بمربعين
    }
    public static final int PAWN_VALUE = 1;
    public static final int KNIGHT_VALUE = 3;
    public static final int BISHOP_VALUE = 3;
    public static final int ROOK_VALUE = 5;
    public static final int QUEEN_VALUE = 9;
    public static final int KING_VALUE = 0; // الملك ليس له قيمة في اللعبة
    
    // دالة لإرجاع قيمة القطعة بناءً على نوعها
    public int getPieceValue() {
        switch (pieceType.split("_")[1]) {
            case "pawn": return PAWN_VALUE;
            case "knight": return KNIGHT_VALUE;
            case "bishop": return BISHOP_VALUE;
            case "rook": return ROOK_VALUE;
            case "queen": return QUEEN_VALUE;
            case "king": return KING_VALUE;
            default: return 0;
        }
    }
}
