package Client.maps;

import Client.main.MessageHandler;
import DTOs.ShipPlacementDto;
import Server.game.cell.Cell;
import Server.game.cell.ShipsCell;
import Client.main.Client;
import Server.game.utility.Directions;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

// Podzielić na 3 mapy, ShipsCell - Stawianie statków, ShipsCell - Strzały przeciwnika, ShootingCell - strzały gracza


public class Map {
    public static ArrayList<String> consoleMessages;
    public static void GeneratePlacingMap(ArrayList<ShipsCell> cells) {
        JFrame frame = Client.frame;
        frame.getContentPane().removeAll();
        var cellCount = (int) Math.sqrt(cells.size());
        JPanel gridPanel = generateTemplate(cellCount);
        cells.stream()
                .sorted(Comparator.comparingInt((ShipsCell cell) -> cell.getY()).thenComparingInt(Cell::getX))
                .forEach(cell -> {
                    if(cell.getX() == 1) {
                        JLabel rowLabel = new JLabel(String.valueOf(cell.getY()), SwingConstants.CENTER);
                        gridPanel.add(rowLabel);
                    }
                    JButton cellButton = new JButton();
                    cellButton.setPreferredSize(new Dimension(50, 50));
                    if(!cell.isPossibleToShip() && cell.isShip())
                    {
                        cellButton.setBackground(Color.GRAY);
                        cellButton = removeHoverAndClickEffect(cellButton);
                    }else if(!cell.isPossibleToShip())
                    {
                        cellButton.setBackground(Color.LIGHT_GRAY);
                        cellButton = removeHoverAndClickEffect(cellButton);
                    }
                    else {
                        cellButton.setBackground(Color.WHITE);
                        cellButton.addActionListener(e -> {
                            var dto = new ShipPlacementDto(cell.getCoordinates(), Directions.RIGHT);
                            MessageHandler.sendObject(dto);
                        });
                    }
                    gridPanel.add(cellButton);
                });
        frame.add(gridPanel, BorderLayout.CENTER);
        generateLayout();
    }
    public static void GenerateComputerShootMap(ArrayList<ShipsCell> cells){
        JFrame frame = Client.frame;
        var cellCount = (int) Math.sqrt(cells.size());
        JPanel gridPanel = generateTemplate(cellCount);
        cells.stream()
                .sorted(Comparator.comparingInt((ShipsCell cell) -> cell.getY()).thenComparingInt(Cell::getX))
                .forEach(cell -> {
                    if(cell.getX() == 1) {
                        JLabel rowLabel = new JLabel(String.valueOf(cell.getY()), SwingConstants.CENTER);
                        gridPanel.add(rowLabel);
                    }
                    JButton cellButton = new JButton();
                    cellButton.setPreferredSize(new Dimension(50, 50));
                    if(cell.isHit() && !cell.isShip())
                    {
                        cellButton.setBackground(Color.RED);
                        cellButton = removeHoverAndClickEffect(cellButton);
                    }else if(cell.isHit() && cell.isShip()){
                        cellButton.setBackground(Color.GREEN);
                        cellButton = removeHoverAndClickEffect(cellButton);
                    }
                    else {
                        cellButton.setBackground(Color.LIGHT_GRAY);
                        cellButton = removeHoverAndClickEffect(cellButton);
                    }
                    gridPanel.add(cellButton);
                });
        frame.getContentPane().removeAll();
        generateLayout();
        frame.add(gridPanel, BorderLayout.CENTER);
    }
    private static JButton removeHoverAndClickEffect(JButton cellButton){
        cellButton.setFocusPainted(false);
        cellButton.setContentAreaFilled(false);
//        cellButton.setBorderPainted(false);
        cellButton.setOpaque(true);
        return cellButton;
    }
    private static JPanel generateTemplate(int cellCount){
        JPanel gridPanel = new JPanel(new GridLayout(cellCount + 1, cellCount + 1));
        for (int col = 0; col <= cellCount; col++) {
            if (col == 0) {
                gridPanel.add(new JLabel(""));
            } else {
                JLabel colLabel = new JLabel(String.valueOf(col), SwingConstants.CENTER);
                gridPanel.add(colLabel);
            }
        }
        return gridPanel;
    }
    private static void generateLayout(){
        JFrame frame = Client.frame;
        JTextArea console = new JTextArea(10, 40);
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        JScrollPane consoleScrollPane = new JScrollPane(console);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Konsola"));
        consoleMessages.forEach(console::append);
        frame.add(consoleScrollPane, BorderLayout.SOUTH);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informacje"));
        infoPanel.setPreferredSize(new Dimension(300, 300));
        infoPanel.add(new JLabel("RED: MISSED"));
        infoPanel.add(new JLabel("BLUE: HIT"));
        infoPanel.add(new JLabel("Turn: "));
        infoPanel.add(new JLabel("SHOT NUMBER: "));
        frame.add(infoPanel, BorderLayout.EAST);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }
    public static void refreshConsole(){
        JFrame frame = Client.frame;
        JScrollPane consoleScrollPane = (JScrollPane) frame.getContentPane().getComponent(1);
        JTextArea console = (JTextArea) consoleScrollPane.getViewport().getView();
        console.setText("");
        consoleMessages.forEach(message -> console.append(message + "\n"));
        frame.revalidate();
        frame.repaint();
    }
}
