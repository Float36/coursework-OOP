package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Rock extends Piece{
    public Rock(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isWhite = isWhite;
        this.name = "Тура";

        // Вирізає відповідний спрайт зі спрайт-листа та масштабує його під клітинку дошки
        this.sprite = sheet.getSubimage(4 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovemenet(int col, int row) {
        return this.col == col || this.row == row;          // перевірка на правильність ходу тури
    }

    // Перевіряє, чи немає фігур на шляху руху тури
    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        // перевірка чи ми можемо походити ліворуч
        if (this.col > col){
            for (int c = this.col-1; c > col; c--){
                if (board.getPiece(c, this.row) != null){
                    return true;
                }
            }
        }

        // перевірка чи ми можемо походити праворуч
        if (this.col < col){
            for (int c = this.col+1; c < col; c++){
                if (board.getPiece(c, this.row) != null){
                    return true;
                }
            }
        }

        // перевірка чи ми можемо походити вверх
        if (this.row > row){
            for (int r = this.row-1; r > row; r--){
                if (board.getPiece(this.col, r) != null){
                    return true;
                }
            }
        }

        // перевірка чи ми можемо походити вниз
        if (this.row < row){
            for (int r = this.row+1; r < row; r++){
                if (board.getPiece(this.col, r) != null){
                    return true;
                }
            }
        }

        return false;
    }
}
