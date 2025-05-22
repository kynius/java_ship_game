package Client.maps;

import Client.main.MessageHandler;
import DTOs.ShipPlacementDto;
import Server.game.cell.Cell;
import Server.game.cell.ShipsCell;
import Client.main.Client;
import Server.game.cell.ShootingCell;
import Server.game.utility.Directions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Map {
    private static Layout layout;
    private static final String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static int cellCount;
    public static ArrayList<String> consoleMessages;
    public static void GeneratePlacingMap(ArrayList<ShipsCell> cells) {
        JFrame frame = Client.frame;
        frame.getContentPane().removeAll();
        cellCount = (int) Math.sqrt(cells.size());
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
        generateLayout(Layout.Placing);
    }
    public static void GenerateComputerShootMap(ArrayList<ShipsCell> cells){
        JFrame frame = Client.frame;
        frame.getContentPane().removeAll();
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
                    cellButton = removeHoverAndClickEffect(cellButton);
                    if(cell.isShip() && cell.isHit())
                    {
                        cellButton.setBackground(Color.RED);
                    }
                    else if(cell.isShip()){
                        cellButton.setBackground(Color.GRAY);
                    }
                    else if(!cell.isShip() && cell.isHit()){
                        cellButton.setBackground(Color.BLACK);
                    }
                    else {
                        cellButton.setBackground(Color.WHITE);
                    }
                    gridPanel.add(cellButton);
                });
        frame.add(gridPanel, BorderLayout.CENTER);
        generateLayout(Layout.ComputerShooting);
    }
    public static void GeneratePlayerShootMap(ArrayList<ShootingCell> cells){
        JFrame frame = Client.frame;
        frame.getContentPane().removeAll();
        JPanel gridPanel = generateTemplate(cellCount);
        cells.stream()
                .sorted(Comparator.comparingInt((ShootingCell cell) -> cell.getY()).thenComparingInt(Cell::getX))
                .forEach(cell -> {
                    if(cell.getX() == 1) {
                        JLabel rowLabel = new JLabel(String.valueOf(cell.getY()), SwingConstants.CENTER);
                        gridPanel.add(rowLabel);
                    }
                    JButton cellButton = new JButton();
                    cellButton.setPreferredSize(new Dimension(50, 50));
                    if(cell.isAimed() && cell.isShot())
                    {
                        cellButton.setBackground(Color.RED);
                        removeHoverAndClickEffect(cellButton);
                    }
                    else if(!cell.isAimed() && cell.isShot())
                    {
                        cellButton.setBackground(Color.BLACK);
                        removeHoverAndClickEffect(cellButton);
                    }
                    else {
                        cellButton.setBackground(Color.WHITE);
                        cellButton.addActionListener(e -> {
                            MessageHandler.sendObject(cell.getCoordinates());
                        });
                    }
                    gridPanel.add(cellButton);
                });
        frame.add(gridPanel, BorderLayout.CENTER);
        generateLayout(Layout.PlayerShooting);
    }
    private static void disableAllButtons(JPanel gridPanel){
        ArrayList<JButton> buttons = new ArrayList<>();
        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JButton) {
                removeHoverAndClickEffect((JButton) component);
            }
        }
    }
    private static JButton removeHoverAndClickEffect(JButton cellButton){
        cellButton.setFocusPainted(false);
        cellButton.setContentAreaFilled(false);
        cellButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        cellButton.setOpaque(true);
        return cellButton;
    }
    private static JPanel generateTemplate(int cellCount){
        JPanel gridPanel = new JPanel(new GridLayout(cellCount + 1, cellCount + 1));
        for (int col = 0; col <= cellCount; col++) {
            if (col == 0) {
                gridPanel.add(new JLabel(""));
            } else {
                JLabel colLabel = new JLabel(LETTERS[col - 1], SwingConstants.CENTER);
                gridPanel.add(colLabel);
            }
        }
        return gridPanel;
    }
    private static void generateLayout(Layout layoutEnum){
//        if(layout == layoutEnum) return;
        JFrame frame = Client.frame;
        JTextArea console = new JTextArea(10, 40);
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        JScrollPane consoleScrollPane = new JScrollPane(console);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Konsola"));
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(300, 300));
        frame.add(consoleScrollPane, BorderLayout.SOUTH);
        // INFO PANEL
        if(layoutEnum == Layout.Placing)
        {
            infoPanel.setBorder(BorderFactory.createTitledBorder("FAZA STAWIANIA STATKÓW"));
            JLabel legendLabel1 = new JLabel("<html><span style='color: white;'>▉</span> - <span style='color: black;'>Dostępne miejsce na statek</span></html>");
            legendLabel1.setFont(new Font("Arial", Font.BOLD, 20));
            JLabel legendLabel2 = new JLabel("<html><span style='color: gray;'>▉</span> - <span style='color: black;'>Miejsce niedostępne</span></html>");
            legendLabel2.setFont(new Font("Arial", Font.BOLD, 20));
            JLabel legendLabel3 = new JLabel("<html><span style='color: dark-gray;'>▉</span> - <span style='color: black;'>Statek już rozmieszczony</span></html>");
            legendLabel3.setFont(new Font("Arial", Font.BOLD, 20));
            infoPanel.add(legendLabel1);
            infoPanel.add(legendLabel2);
            infoPanel.add(legendLabel3);
        }
        if(layoutEnum == Layout.PlayerShooting)
        {
            infoPanel.setBorder(BorderFactory.createTitledBorder("Faza strzelania gracza"));
            JLabel legendLabel1 = new JLabel("<html><span style='color: red;'>▉</span> - <span style='color: black;'>Cel trafiony</span></html>");
            legendLabel1.setFont(new Font("Arial", Font.BOLD, 20));
            JLabel legendLabel2 = new JLabel("<html><span style='color: black;'>▉</span> - <span style='color: black;'>Cel nietrafiony</span></html>");
            legendLabel2.setFont(new Font("Arial", Font.BOLD, 20));
            JLabel legendLabel3 = new JLabel("<html><span style='color: white;'>▉</span> - <span style='color: black;'>Możliwość oddania strzału</span></html>");
            legendLabel3.setFont(new Font("Arial", Font.BOLD, 20));
            infoPanel.add(legendLabel1);
            infoPanel.add(legendLabel2);
            infoPanel.add(legendLabel3);
        }
        if(layoutEnum == Layout.ComputerShooting)
        {
            infoPanel.setBorder(BorderFactory.createTitledBorder("Faza strzelania komputera"));
            JLabel legendLabel1 = new JLabel("<html><span style='color: red;'>▉</span> - <span style='color: black;'>Statek gracza trafiony</span></html>");
            legendLabel1.setFont(new Font("Arial", Font.BOLD, 20));
            JLabel legendLabel2 = new JLabel("<html><span style='color: black;'>▉</span> - <span style='color: black;'>Strzał nietrafiony</span></html>");
            legendLabel2.setFont(new Font("Arial", Font.BOLD, 20));
            JLabel legendLabel3 = new JLabel("<html><span style='color: white;'>▉</span> - <span style='color: black;'>Pole jeszcze niezaatakowane</span></html>");
            legendLabel3.setFont(new Font("Arial", Font.BOLD, 20));
            JLabel legendLabel4 = new JLabel("<html><span style='color: gray;'>▉</span> - <span style='color: black;'>Pozycja statku gracza</span></html>");
            legendLabel4.setFont(new Font("Arial", Font.BOLD, 20));
            infoPanel.add(legendLabel1);
            infoPanel.add(legendLabel2);
            infoPanel.add(legendLabel3);
            infoPanel.add(legendLabel4);
        }
        frame.add(infoPanel, BorderLayout.EAST);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        refreshConsole();
    }
    public static void refreshConsole(){
        JFrame frame = Client.frame;
        JScrollPane consoleScrollPane = (JScrollPane) frame.getContentPane().getComponent(1);
        JTextArea console = (JTextArea) consoleScrollPane.getViewport().getView();
        console.setText("");
        consoleMessages.forEach(message -> console.append(message + "\n"));
        console.setCaretPosition(console.getDocument().getLength());
        frame.revalidate();
        frame.repaint();
    }
}
