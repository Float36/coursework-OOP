package main;

import pieces.Piece;

// клас для перевірки чи знаходиться король під шахом і чи наступний хід не загрожує королю
public class CheckScanner {
    Board board;

    public CheckScanner(Board board){
        this.board = board;
    }

    // метод для перевірки чи король під шахом
    public boolean isKingChacked(Move move) {
        Piece king = board.findKing(move.piece.isWhite);
        assert king != null;

        int kingCol = king.col;
        int kingRow = king.row;

        if (board.selectedPiece != null && board.selectedPiece.name.equals("Король")) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        return hitByRock(move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) || //вверх
               hitByRock(move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) || //вправо
               hitByRock(move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) || //вниз
               hitByRock(move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) || //вліво

               hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) || // вверх ліворуч
               hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) || // вверх праворуч
               hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1) || // вниз праворуч
               hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1) || //вниз ліворуч

               hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
               hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) ||
               hitByKing(king, kingCol, kingRow);
    }

    // перевірка на можливу загрозу від усіх фігур
    private boolean  hitByRock(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {
        for (int i = 1; i < 8; i++){
            if (kingCol + (i*colVal) == col && kingRow + (i*rowVal) == row){
                break;
            }

            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * rowVal));

            if (piece != null && piece != board.selectedPiece){
                if (!board.sameTeam(piece, king) && (piece.name.equals("Тура") || piece.name.equals("Королева"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }


    private boolean  hitByBishop(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {
        for (int i = 1; i < 8; i++){
            if (kingCol - (i*colVal) == col && kingRow - (i*rowVal) == row){
                break;
            }

            Piece piece = board.getPiece(kingCol - (i * colVal), kingRow - (i * rowVal));

            if (piece != null && piece != board.selectedPiece){
                if (!board.sameTeam(piece, king) && (piece.name.equals("Слон") || piece.name.equals("Королева"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean  hitByKnight(int col, int row, Piece king, int kingCol, int kingRow){
        return checkKnight(board.getPiece(kingCol - 1, kingRow -2), king, col, row) ||
               checkKnight(board.getPiece(kingCol + 1, kingRow -2), king, col, row) ||
               checkKnight(board.getPiece(kingCol + 2, kingRow -1), king, col, row) ||
               checkKnight(board.getPiece(kingCol + 2, kingRow +1), king, col, row) ||
               checkKnight(board.getPiece(kingCol + 1, kingRow +2), king, col, row) ||
               checkKnight(board.getPiece(kingCol - 1, kingRow +2), king, col, row) ||
               checkKnight(board.getPiece(kingCol - 2, kingRow +1), king, col, row) ||
               checkKnight(board.getPiece(kingCol - 2, kingRow -1), king, col, row);
    }

    private boolean checkKnight(Piece p, Piece k, int col, int row){
        return p != null && !board.sameTeam(p, k) && p.name.equals("Кінь") && !(p.col == col && p.row == row);
    }

    private boolean hitByKing(Piece king, int kingCol, int kingRow){
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
               checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
               checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
               checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
               checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
               checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
               checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
               checkKing(board.getPiece(kingCol, kingRow + 1), king);
    }

    private boolean checkKing(Piece p, Piece k) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Король");
    }

    private boolean hitByPawn(int col, int row, Piece king, int kingCol, int kingRow){
        int colorVal = king.isWhite ? -1 : 1;
        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorVal), king, col, row) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + colorVal), king, col, row);
    }

    private boolean checkPawn(Piece p, Piece k, int col, int row){
        return p != null && !board.sameTeam(p, k) && p.name.equals("Пішак") && !(p.col == col && p.row == row);
    }

    // перевіряємо чи гра завершена (якщо фігура може походити то гра не завершена)
    public boolean isGameOver(Piece king){
        for (Piece piece : board.pieceArrayList) {
            if (board.sameTeam(piece, king)){
                board.selectedPiece = piece == king ? king : null;
                for (int row = 0; row < board.rows; row++) {
                    for (int col = 0; col < board.cols; col++){
                        Move move = new Move(board, piece, col, row);
                        if (board.isValidMove(move)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
