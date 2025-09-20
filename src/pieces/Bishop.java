package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Bishop extends Piece{
    public Bishop(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isWhite = isWhite;
        this.name = "Слон";

        this.sprite = sheet.getSubimage(2 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovemenet(int col, int row) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);          // перевірка на правильність ходу Слона
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        // вверх ліворуч
        if (this.col > col && this.row > row){
            for (int i = 1; i < Math.abs(this.col - col); i++){
                if (board.getPiece(this.col - i, this.row - i) != null){
                    return true;
                }
            }
        }

        // вверх праворуч
        if (this.col < col && this.row > row){
            for (int i = 1; i < Math.abs(this.col - col); i++){
                if (board.getPiece(this.col + i, this.row - i) != null){
                    return true;
                }
            }
        }

        // вниз ліворуч
        if (this.col > col && this.row < row){
            for (int i = 1; i < Math.abs(this.col - col); i++){
                if (board.getPiece(this.col - i, this.row + i) != null){
                    return true;
                }
            }
        }

        // вниз праворуч
        if (this.col < col && this.row < row){
            for (int i = 1; i < Math.abs(this.col - col); i++){
                if (board.getPiece(this.col + i, this.row + i) != null){
                    return true;
                }
            }
        }

        return false;
    }
}
