package Client.navigation;

import Client.main.Client;
import javax.swing.*;
import java.awt.*;

public class MainMenu implements Menu {
    public static void displayMenu() {
            JFrame frame = Client.frame;
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1920, 1080);
            frame.setLayout(new BorderLayout());
            frame.setResizable(false);
            var titleLabel = new JLabel("Java Ship Game", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
            frame.add(titleLabel, BorderLayout.NORTH);

            var buttonPanel = new JPanel(new GridLayout(3, 1, 20, 20));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(400, 400, 400, 400));

            var startGameButton = new JButton("Zacznij grę");
            startGameButton.setFont(new Font("Arial", Font.PLAIN, 36));
            var configButton = new JButton("Wybierz konfigurację");
            configButton.setFont(new Font("Arial", Font.PLAIN, 36));
            var exitButton = new JButton("Wyjdź");
            exitButton.setFont(new Font("Arial", Font.PLAIN, 36));

            startGameButton.addActionListener(e -> {
                    var sizeFrame = new JFrame("Wybierz wielkość planszy");
                    sizeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    sizeFrame.setSize(400, 100);
                    sizeFrame.setLayout(new FlowLayout());
                    for (int i = 5; i <= 10; i++) {
                            var button = new JButton("" + i);
                            int finalI = i;
                            button.addActionListener(event -> {
                                    sizeFrame.dispose();
                            });
                            sizeFrame.add(button);
                    }
                    sizeFrame.setVisible(true);
                    sizeFrame.setLocationRelativeTo(null);
            });
            configButton.addActionListener(e -> {
                    frame.getContentPane().removeAll();
                    ConfigMenu.displayMenu();
                    frame.revalidate();
                    frame.repaint();
            });
            exitButton.addActionListener(e -> System.exit(0));

            buttonPanel.add(startGameButton);
            buttonPanel.add(configButton);
            buttonPanel.add(exitButton);

            frame.add(buttonPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            }
}