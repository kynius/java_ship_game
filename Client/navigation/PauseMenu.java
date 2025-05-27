package Client.navigation;

import Client.main.Client;
import Client.main.MessageHandler;
import RequestClasses.PauseStopRequest;

import javax.swing.*;
import java.awt.*;

public class PauseMenu {
    public static void displayPauseMenu() {
        var frame = Client.frame;
        JDialog dialog = new JDialog(frame, "Menu Pauzy", true); // true = modal
        dialog.setSize(600, 300);
        dialog.setLayout(new BorderLayout());
        var buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        JButton resumeButton = new JButton("Wznów grę");
        resumeButton.setMaximumSize(new Dimension(200, 50));
        JButton exitButton = new JButton("Wyjdź do pulpitu");
        exitButton.setMaximumSize(new Dimension(200, 50));
        resumeButton.addActionListener(e -> {
            dialog.dispose();
            MessageHandler.sendObject(new PauseStopRequest());
        });
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(resumeButton);
        buttonPanel.add(exitButton);
        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}