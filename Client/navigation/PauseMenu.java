package Client.navigation;

import Client.main.Client;
import Client.main.MessageHandler;
import RequestClasses.PauseStopRequest;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a modal pause menu dialog during gameplay.
 * Allows the user to resume the game or exit to the desktop.
 */
public class PauseMenu {
    /**
     * Displays the pause menu dialog with options to resume the game or exit.
     * The dialog is modal, meaning it blocks input to other windows until closed.
     * Sends a pause start request to the server when displayed.
     * Sends a pause stop request to the server when resuming.
     */
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