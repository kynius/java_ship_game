package navigation;

import main.Client;

import javax.swing.*;
import java.awt.*;

public class ConfigMenu implements Menu {
    public static void displayMenu() {
        JFrame frame = Client.frame;
        JLabel configLabel = new JLabel("Wybierz opcje konfiguracji:", SwingConstants.CENTER);
        configLabel.setFont(new Font("Arial", Font.BOLD, 48));
        frame.add(configLabel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(400, 400, 400, 400));

        JButton configListButton = new JButton("Lista konfiguracji");
        configListButton.setFont(new Font("Arial", Font.PLAIN, 36));
        JButton fileConfigButton = new JButton("Dodaj konfigurację z pliku");
        fileConfigButton.setFont(new Font("Arial", Font.PLAIN, 36));
        JButton exitButton = new JButton("Powrót do menu głównego");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 36));

        configListButton.addActionListener(_ -> {
            // TODO
        });

        fileConfigButton.addActionListener(_ -> {
            // TODO
        });

        exitButton.addActionListener(_ -> {
            frame.getContentPane().removeAll();
            MainMenu.displayMenu();
            frame.revalidate();
            frame.repaint();
        });


        buttonPanel.add(configListButton);
        buttonPanel.add(fileConfigButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.CENTER);
    }
}
