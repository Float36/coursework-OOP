package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

// клас для представлення фігури
public class Piece {
    public int col;         // Координати фігури на дошці.
    public int row;
    public int xPos;        // Позиція фігури у пікселях для відображення на екрані.
    public int yPos;

    public boolean isWhite;                 // колір фігури
    public String name;                     // назва фігури

    public boolean isFirstMove = true;      // чи це перший рух фігури

    BufferedImage sheet;
    {
        try {
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("pieces.png"));  // ініціалізація спрайту
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int sheetScale = sheet.getWidth()/6;  // масштаб однієї фігури на фото
    Image sprite;                                   // обрізане відображення для кожної фігури
    Board board;                                    // посилання на дошку

    public Piece(Board board){
        this.board = board;
    }

    // перевіряє чи можливий рух на вказані координати
    public boolean isValidMovemenet(int col, int row){
        return true;
    }

    // перевірка чи заважає фігура руху
    public boolean moveCollidesWithPiece(int col, int row){
        return false;
    }

    // метод малює фігуру на заданих координатах
    public void paint(Graphics2D g2d){
        g2d.drawImage(sprite, xPos, yPos, null);
    }
}
