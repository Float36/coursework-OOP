package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu {
    public void showMenu() {
        JFrame frame = new JFrame("Шахи - Головне меню");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 1));
        frame.setLocationRelativeTo(null);

        JButton startButton1 = new JButton("Звичайний матч");
        JButton startButton2 = new JButton("Шахи наосліп");
        JButton exitButton = new JButton("Вийти");

        startButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Закриваємо меню
                Main.startGame(1); // Викликаємо метод для запуску гри
            }
        });

        startButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Закриваємо меню
                Main.startGame(2); // Викликаємо метод для запуску гри
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Завершуємо програму
            }
        });

        frame.add(startButton1);
        frame.add(startButton2);
        frame.add(exitButton);
        frame.setVisible(true);
    }
}
