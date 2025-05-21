package Client.maps;

import Server.game.cell.Cell;
import Server.game.cell.ShipsCell;
import Client.main.Client;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

// Podzielić na 3 mapy, ShipsCell - Stawianie statków, ShipsCell - Strzały przeciwnika, ShootingCell - strzały gracza


public class Map {
    public static void GeneretateMap(ArrayList<ShipsCell> cells) {
        JFrame frame = Client.frame;
        var cellCount = (int) Math.sqrt(cells.size());
        JPanel gridPanel = new JPanel(new GridLayout(cellCount + 1, cellCount + 1));
        for (int col = 0; col <= cellCount; col++) {
            if (col == 0) {
                gridPanel.add(new JLabel(""));
            } else {
                JLabel colLabel = new JLabel(String.valueOf(col), SwingConstants.CENTER);
                gridPanel.add(colLabel);
            }
        }
        cells.stream()
                .sorted(Comparator.comparingInt((ShipsCell cell) -> cell.getY()).thenComparingInt(Cell::getX))
                .forEach(cell -> {
                    if(cell.getX() == 1) {
                        JLabel rowLabel = new JLabel(String.valueOf(cell.getY()), SwingConstants.CENTER);
                        gridPanel.add(rowLabel);
                    }
                    if(!cell.isPossibleToShip())
                    {

                    }
                    JButton cellButton = new JButton();
                    cellButton.setPreferredSize(new Dimension(50, 50));
                    cellButton.setBackground(Color.LIGHT_GRAY);
                    cellButton.addActionListener(e -> {
                        try {
                            Client.out.writeObject(cell.getCoordinates());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    gridPanel.add(cellButton);
                });
        frame.getContentPane().removeAll();
        frame.add(gridPanel, BorderLayout.CENTER);
        JTextArea console = new JTextArea(10, 40);
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        JScrollPane consoleScrollPane = new JScrollPane(console);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Konsola"));
        frame.add(consoleScrollPane, BorderLayout.SOUTH);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informacje"));
        infoPanel.setPreferredSize(new Dimension(300, 300));
        infoPanel.add(new JLabel("Player:"));
        infoPanel.add(new JLabel("Points:"));
        infoPanel.add(new JLabel("Turn:"));
        frame.add(infoPanel, BorderLayout.EAST);

        // Refresh the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }
}
