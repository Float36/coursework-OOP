package main;

import pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {
    Board board;

    public Input(Board board){
        this.board = board;
    }


    // якщо мишку натиснуто
    @Override
    public void mousePressed(MouseEvent e) {
        // клітинка в якій було натиснуто кнопку миші
        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        // якщо на даній клітинці знаходиться фігура, робимо її обраноє
        Piece pieceXY = board.getPiece(col, row);
        if (pieceXY != null){
            board.selectedPiece = pieceXY;
        }

    }

    // якщо мишку відпущено
    @Override
    public void mouseReleased(MouseEvent e) {
        // клітинка в якій було відтиснуто кнопку миші
        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        if (board.selectedPiece != null){
            Move move = new Move(board, board.selectedPiece, col, row);

            // якщо рух можливий то ми робимо цей рух
            if (board.isValidMove(move)){
                board.makeMove(move);
            } else {
                board.selectedPiece.xPos = board.selectedPiece.col * board.tileSize;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tileSize;
            }
        }

        // знімаємо виділену фігуру
        board.selectedPiece = null;
        board.repaint();
    }


    // якщо мишку переміщуємо, забезпечує плавне переміщення фігури разом із курсором
    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPiece != null){
            board.selectedPiece.xPos = e.getX() - board.tileSize / 2;       // центруємо фігуру до курсора
            board.selectedPiece.yPos = e.getY() - board.tileSize / 2;

            board.repaint();
        }
    }

}

