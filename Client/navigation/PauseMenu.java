package Client.navigation;

import javax.swing.*;
import java.awt.*;

public class PauseMenu {
    public static void displayPauseMenu() {
        JFrame frame = new JFrame("Menu Pauzy");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 1));

        JButton resumeButton = new JButton("Wznów grę");
        JButton abandonButton = new JButton("Porzuć grę");
        JButton exitButton = new JButton("Wyjdź do pulpitu");

        resumeButton.addActionListener(e -> {
            frame.dispose();
        });

        abandonButton.addActionListener(e -> {
            frame.dispose();
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        frame.add(resumeButton);
        frame.add(abandonButton);
        frame.add(exitButton);

        frame.setVisible(true);
    }
}