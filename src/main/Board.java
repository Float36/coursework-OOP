package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static main.Main.gameFrame;

public class Board extends JPanel {
    public int tileSize = 90;           // розмір клітинки
    int cols = 8;
    int rows = 8;
    ArrayList<Piece> pieceArrayList = new ArrayList<>();            // список усіх фігур на дошці
    public Piece selectedPiece;             // фігура яку ви хочете перемістити
    Input input = new Input(this);
    public CheckScanner checkScanner = new CheckScanner(this);
    public int enPassantTile = -1;
    private int gameMode;                       // режим гри
    private boolean isWhiteToMove = true;       // хто ходить наступним
    private boolean isGameOver = false;       // чи гра закінчена


    public Board(int gameMode){
        this.gameMode = gameMode;

        // встановлює розміри дошки на основі кількості стовпців рядків і розміру плитки
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));

        // додає обробник миші для кліків і руху мишею
        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }


    // повертає фігуру якщо вона інує на цій клітинці, або null якщо неіснує
    public Piece getPiece(int col, int row){
        for (Piece piece : pieceArrayList) {
            if (piece.col == col && piece.row == row){
                return piece;
            }
        }

        return null;
    }

    // робимо хід
    public void makeMove(Move move){
        if (move.piece.name.equals("Пішак")){
            movePawn(move);
        } else if (move.piece.name.equals("Король")) {
            moveKing((move));
        }
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * tileSize;
            move.piece.yPos = move.newRow * tileSize;

            move.piece.isFirstMove = false;

            capture(move.capture);

            isWhiteToMove = !isWhiteToMove;

            updateGameState();

        if (gameMode == 2){
            Graphics g = getGraphics();
            if (g != null) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight()); // Малюємо білий квадрат на весь екран
                g.dispose();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Таймер було перервано!");
            }
            repaint(); // Прибираємо білий квадрат, оновлюючи вікно
        }
    }

    // робимо хід королем
    private void moveKing(Move move) {
        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rock;
            if (move.piece.col < move.newCol) {
                rock = getPiece(7, move.piece.row);
                rock.col = 5;
            } else {
                rock = getPiece(0, move.piece.row);
                rock.col = 3;
            }
            rock.xPos = rock.col * tileSize;
        }
    }

    // робимо хід пішаком
    private void movePawn(Move move){
        // ан пассант
        int colorIndex = move.piece.isWhite ? 1 : -1;
        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.piece.row - move.newRow) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else{
            enPassantTile = -1;
        }
        // прохід далі
        colorIndex = move.piece.isWhite ? 0 : 7;
        if (move.newRow == colorIndex){
            promotePawn(move);
        }
    }

    // підвищуємо пішака до королеви
    private void promotePawn(Move move) {
        pieceArrayList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    // метод побиття фігури (видаляємо зі списку фігуру якщо така існує на клітинці куди походили)
    public void capture(Piece piece){
        pieceArrayList.remove(piece);
    }

    // перевіряємо чи ми можемо зробити такий хід
    public boolean isValidMove(Move move) {

        // перевірка на завершення гри
        if(isGameOver){
            return false;
        }

        // перевірка чия зараз черга ходити
        if (move.piece.isWhite != isWhiteToMove){
            return false;
        }

        // перевірка чи знаходяться фігури в одній команді
        if (sameTeam(move.piece, move.capture)){
            return false;
        }

        // перевірка на коректність ходу
        if (!move.piece.isValidMovemenet(move.newCol, move.newRow)){
            return false;
        }

        // перевірка на те чи стоїть перед обраною фігурою інша
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)){
            return false;
        }

        // перевірка на шах
        if (checkScanner.isKingChacked(move)) {
            return false;
        }

        return true;
    }

    // перевірка чи фігура яка стає на місце іншої фігури в одній команді
    public boolean sameTeam(Piece p1, Piece p2){
        if (p1 == null || p2 == null){
            return false;
        }

        return p1.isWhite == p2.isWhite;
    }

    // метод який повертає номер клітинки
    public int getTileNum(int col, int row){
        return row * rows + col;
    }

    // метод для знаходення короля серед усіх фігур на дошці
    Piece findKing(boolean isWhite){
        for (Piece piece : pieceArrayList){
            if (isWhite == piece.isWhite && piece.name.equals("Король")){
                return piece;
            }
        }
        return null;
    }

    // додаємо фігури на дошку
    public void addPieces(){
        this.pieceArrayList.add(new Rock(this, 0, 0, false));
        this.pieceArrayList.add(new Knight(this, 1, 0, false));
        this.pieceArrayList.add(new Bishop(this, 2, 0, false));
        this.pieceArrayList.add(new Queen(this, 3, 0, false));
        this.pieceArrayList.add(new King(this, 4, 0, false));
        this.pieceArrayList.add(new Bishop(this, 5, 0, false));
        this.pieceArrayList.add(new Knight(this, 6, 0, false));
        this.pieceArrayList.add(new Rock(this, 7, 0, false));
        this.pieceArrayList.add(new Pawn(this, 0, 1, false));
        this.pieceArrayList.add(new Pawn(this, 1, 1, false));
        this.pieceArrayList.add(new Pawn(this, 2, 1, false));
        this.pieceArrayList.add(new Pawn(this, 3, 1, false));
        this.pieceArrayList.add(new Pawn(this, 4, 1, false));
        this.pieceArrayList.add(new Pawn(this, 5, 1, false));
        this.pieceArrayList.add(new Pawn(this, 6, 1, false));
        this.pieceArrayList.add(new Pawn(this, 7, 1, false));
        this.pieceArrayList.add(new Rock(this, 0, 7, true));
        this.pieceArrayList.add(new Knight(this, 1, 7, true));
        this.pieceArrayList.add(new Bishop(this, 2, 7, true));
        this.pieceArrayList.add(new Queen(this, 3, 7, true));
        this.pieceArrayList.add(new King(this, 4, 7, true));
        this.pieceArrayList.add(new Bishop(this, 5, 7, true));
        this.pieceArrayList.add(new Knight(this, 6, 7, true));
        this.pieceArrayList.add(new Rock(this, 7, 7, true));
        this.pieceArrayList.add(new Pawn(this, 0, 6, true));
        this.pieceArrayList.add(new Pawn(this, 1, 6, true));
        this.pieceArrayList.add(new Pawn(this, 2, 6, true));
        this.pieceArrayList.add(new Pawn(this, 3, 6, true));
        this.pieceArrayList.add(new Pawn(this, 4, 6, true));
        this.pieceArrayList.add(new Pawn(this, 5, 6, true));
        this.pieceArrayList.add(new Pawn(this, 6, 6, true));
        this.pieceArrayList.add(new Pawn(this, 7, 6, true));
    }

    // метод для перевірку на мат або пат після кожного ходу
    private void updateGameState() {
        String winnerMessage = " ";

        Piece king = findKing(isWhiteToMove);
        if (checkScanner.isGameOver(king)){
            if(checkScanner.isKingChacked(new Move(this, king, king.col, king.row))){
                System.out.println(isWhiteToMove ? "Чорні перемогли!" : "Білі перемогли!");
                winnerMessage = isWhiteToMove ? "Чорні перемогли!" : "Білі перемогли!";
            }else {
                System.out.println("Пат");
                winnerMessage = "Пат";
            }
            isGameOver = true;
        } else if (insufficientMaterial(true) && insufficientMaterial(false)){
            System.out.println("Недостатньо фігур для перемоги");
            winnerMessage = "Недостатньо фігур для перемоги";
            isGameOver = true;
        }

        if (isGameOver){
            JOptionPane.showMessageDialog(gameFrame, winnerMessage, "Гра завершена", JOptionPane.INFORMATION_MESSAGE);
            gameFrame.dispose(); // Закриваємо вікно гри

            // Повертаємось до головного меню
            Menu menu = new Menu();
            menu.showMenu();
        }
    }

    // метод для перевірки чи достатньо фігур для продовження гри (якщо залишився в команді король, або король і кінь - нічия)
    private boolean insufficientMaterial(boolean isWhite){
        ArrayList<String> names = pieceArrayList.stream()
                .filter(p -> p.isWhite == isWhite)
                .map(p -> p.name)
                .collect(Collectors.toCollection(ArrayList::new));
        if (names.contains("Королева") || names.contains("Тура") || names.contains("Пішак")){
            return false;
        }
        return names.size() < 3;
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        // малювання дошки
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < cols; c++){
                g2d.setColor((c+r) % 2 == 0 ? new Color(255, 209, 119) : new Color(101, 0, 0));
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }
        }

        // підсвічуємо можливі ходи для фігури
        if (selectedPiece != null){
            for (int r = 0; r < rows; r++){
                for (int c = 0; c < cols; c++){
                    if (isValidMove(new Move(this, selectedPiece, c, r))){
                        g2d.setColor(new Color(4, 202, 0, 115));
                        g2d.fillRect(c*tileSize, r*tileSize, tileSize, tileSize);
                        // для режиму шахів наосліп ми також малюємо фігуру суперника якщо ми можемо її побити
                        if (getPiece(c, r) != null){
                            Piece piece = getPiece(c ,r);
                            piece.paint(g2d);
                        }
                    }
                }
            }
        }

        // виводимо фігури в залежності від ігрового режиму
        if (gameMode == 1){
            for (Piece piece : pieceArrayList){
                piece.paint(g2d);
            }
        } else if (gameMode == 2){
            for (Piece piece : pieceArrayList){
                if (isWhiteToMove){
                    if (piece.isWhite){
                        piece.paint(g2d);
                    }
                } else {
                    if (!piece.isWhite){
                        piece.paint(g2d);
                    }
                }
            }
        }
    }
}
