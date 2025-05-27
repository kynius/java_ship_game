package Client.navigation;

import Client.main.Client;
import Client.main.MessageHandler;
import RequestClasses.ScoreboardRequest;
import Server.game.utility.MapConfigfuration;
import Server.game.utility.ShipsConfiguration;

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
            var scoreBoard = new JButton("Zobacz tabelę wyników");
            scoreBoard.setFont(new Font("Arial", Font.PLAIN, 36));
            var exitButton = new JButton("Wyjdź");
            exitButton.setFont(new Font("Arial", Font.PLAIN, 36));

            startGameButton.addActionListener(e -> {
                    var sizeFrame = new JFrame("Wybierz wielkość planszy");
                    sizeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    sizeFrame.setSize(600, 100);
                    sizeFrame.setLayout(new FlowLayout());
                    var smallMap = new JButton("Mała plansza (6x6)");
                    smallMap.addActionListener(event -> {
                            int[] mapConfigurations = new int[]{1,1,2};
                            MessageHandler.sendObject(new MapConfigfuration(6, new ShipsConfiguration(mapConfigurations)));
                            sizeFrame.dispose();
                    });
                    var normalMap = new JButton("Średnia plansza (10x10)");
                    normalMap.addActionListener(event -> {
                            int[] mapConfigurations = new int[]{1,2,3,4};
                            MessageHandler.sendObject(new MapConfigfuration(10, new ShipsConfiguration(mapConfigurations)));
                            sizeFrame.dispose();
                    });
                    var bigMap = new JButton("Duża plansza (15x15)");
                    bigMap.addActionListener(event -> {
                            int[] mapConfigurations = new int[]{1,2,3,3,4};
                            MessageHandler.sendObject(new MapConfigfuration(15, new ShipsConfiguration(mapConfigurations)));
                            sizeFrame.dispose();
                    });
                    sizeFrame.add(smallMap);
                    sizeFrame.add(normalMap);
                    sizeFrame.add(bigMap);
                    sizeFrame.setVisible(true);
                    sizeFrame.setLocationRelativeTo(null);
            });
            scoreBoard.addActionListener(e -> {
                    MessageHandler.sendObject(new ScoreboardRequest());
            });
            exitButton.addActionListener(e -> System.exit(0));

            buttonPanel.add(startGameButton);
            buttonPanel.add(scoreBoard);
            buttonPanel.add(exitButton);

            frame.add(buttonPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            }
}