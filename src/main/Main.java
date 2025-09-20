package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static JFrame gameFrame;

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.showMenu();
    }

    public static void startGame(int gameMode) {
        gameFrame = new JFrame("Шахи");
        gameFrame.setLayout(new GridBagLayout());
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setMinimumSize(new Dimension(800, 800));
        gameFrame.setLocationRelativeTo(null);

        Board board = new Board(gameMode);
        gameFrame.add(board);
        gameFrame.setVisible(true);
        System.out.println("Гра розпочата!");
    }
}
