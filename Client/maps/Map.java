package Client.maps;
import Client.main.MessageHandler;
import Client.navigation.PauseMenu;
import DTOs.*;
import RequestClasses.PauseStartRequest;
import Server.game.cell.Cell;
import Server.game.cell.ShipsCell;
import Client.main.Client;
import Server.game.cell.ShootingCell;
import Server.game.utility.Directions;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
/**
 * Provides methods to generate and update the main game UI,
 * including ship placement, shooting phases, scoreboard, and end game dialogs.
 * Handles user interactions and updates the console area.
 */
public class Map {
    /** Label displaying the current ship direction in the legend. */
    private static JLabel GlobalLegendDirection;
    /** Current direction for ship placement. */
    private static Directions direction = Directions.UP;
    /** Current layout state of the game UI. */
    private static Layout layout;
    /** Length of the ship currently being placed. */
    private static int shipLength;
    /** Letters used for column labels in the grid. */
    private static final String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    /** Number of cells per row/column in the grid. */
    private static int cellCount;
    /** List of messages displayed in the console area. */
    public static ArrayList<String> consoleMessages;
    /**
     * Generates the ship placement map UI, allowing the player to place ships.
     *
     * @param shipPlacementRequestDto Data transfer object containing ship placement info.
     */
    public static void GeneratePlacingMap(ShipPlacementRequestDto shipPlacementRequestDto) {
        shipLength = shipPlacementRequestDto.getShipLength();
        var cells = (ArrayList<ShipsCell>) shipPlacementRequestDto.getShipmap().get_cells();
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
                            var dto = new ShipPlacementDto(cell.getCoordinates(), direction);
                            MessageHandler.sendObject(dto);
                        });
                    }
                    gridPanel.add(cellButton);
                });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyChar() == 'r' || e.getKeyChar() == 'R') && !e.isConsumed() && layout == Layout.Placing) {
                    e.consume();
                    if(direction == Directions.LEFT)
                    {
                        direction = Directions.UP;
                    }
                    else{
                        direction = Directions.values()[direction.ordinal() + 1];
                    }
                    consoleMessages.add("Zmieniono kierunek na: " + direction.getLabel());
                    GlobalLegendDirection.setText("<html><span style='color: black;'> Aktualny kierunek: " + direction.getLabel() + "</span></html>");
                    refreshConsole();
                }
                if ((e.getKeyChar() == 'p' || e.getKeyChar() == 'P' || e.getKeyCode() == KeyEvent.VK_ESCAPE) && !e.isConsumed()) {
                    e.consume();
                    MessageHandler.sendObject(new PauseStartRequest());
                    PauseMenu.displayPauseMenu();
                }
            }
        });
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.add(gridPanel, BorderLayout.CENTER);
        generateLayout(Layout.Placing);
    }
    /**
     * Generates the UI for the computer's shooting phase, showing the result of the computer's shot.
     *
     * @param receiveShotDto Data transfer object with the result of the computer's shot.
     */
    public static void GenerateComputerShootMap(ReceiveShotDto receiveShotDto) {
        var cells = receiveShotDto.getShipsMap().get_cells();
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
                    if(cell.getCoordinates().equals(receiveShotDto.getLastShot())){
                        cellButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    }
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
    /**
     * Generates the UI for the player's shooting phase, allowing the player to select a target.
     *
     * @param cells List of cells representing the current shooting map.
     */
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
    /**
     * Removes hover and click effects from a button, making it non-interactive.
     *
     * @param cellButton The button to modify.
     * @return The modified button.
     */
    private static JButton removeHoverAndClickEffect(JButton cellButton){
        cellButton.setFocusPainted(false);
        cellButton.setContentAreaFilled(false);
        cellButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        cellButton.setOpaque(true);
        return cellButton;
    }
    /**
     * Generates a grid panel template with column and row labels.
     *
     * @param cellCount Number of cells per row/column.
     * @return The generated grid panel.
     */
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
    /**
     * Updates the layout and info panel based on the current game phase.
     *
     * @param layoutEnum The current layout state.
     */
    private static void generateLayout(Layout layoutEnum) {
        layout = layoutEnum;
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
        if(layoutEnum == Layout.Placing)
        {
            infoPanel.setBorder(BorderFactory.createTitledBorder("FAZA STAWIANIA STATKÓW"));
            JLabel legendLabel = new JLabel("<html><span style='color: black;'>▶ MAPA KOLORÓW: </span></html>");
            legendLabel.setFont(new Font("Arial", Font.BOLD, 22));
            JLabel legendLabel1 = new JLabel("<html><span style='color: white;'>▉</span> - <span style='color: black;'>Dostępne miejsce na statek</span></html>");
            legendLabel1.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel legendLabel2 = new JLabel("<html><span style='color: gray;'>▉</span> - <span style='color: black;'>Miejsce niedostępne</span></html>");
            legendLabel2.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel legendLabel3 = new JLabel("<html><span style='color: dark-gray;'>▉</span> - <span style='color: black;'>Statek już rozmieszczony</span></html>");
            legendLabel3.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel keyBindingsLabel = new JLabel("<html><span style='color: black;'>▶ STEROWANIE: </span></html>");
            keyBindingsLabel.setFont(new Font("Arial", Font.BOLD, 22));
            JLabel changeDirection = new JLabel("<html><span style='color: black;'> R - Obróć kierunek</span></html>");
            changeDirection.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel pauseInfo = new JLabel("<html><span style='color: black;'> P/ESC - Menu pauzy</span></html>");
            pauseInfo.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel infoLabel = new JLabel("<html><span style='color: black;'>▶ GRA: </span></html>");
            infoLabel.setFont(new Font("Arial", Font.BOLD, 22));
            JLabel legendDirection = new JLabel("<html><span style='color: black;'> Aktualny kierunek: " + direction.getLabel() + "</span></html>");
            legendDirection.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel legendShipLength = new JLabel("<html><span style='color: black;'> Stawiasz statek długości: " + shipLength + "</span></html>");
            legendShipLength.setFont(new Font("Arial", Font.PLAIN, 20));
            infoPanel.add(legendLabel);
            infoPanel.add(legendLabel1);
            infoPanel.add(legendLabel2);
            infoPanel.add(legendLabel3);
            infoPanel.add(keyBindingsLabel);
            infoPanel.add(pauseInfo);
            infoPanel.add(changeDirection);
            infoPanel.add(infoLabel);
            infoPanel.add(legendShipLength);
            infoPanel.add(legendDirection);
            GlobalLegendDirection = legendDirection;
        }
        if(layoutEnum == Layout.PlayerShooting)
        {
            infoPanel.setBorder(BorderFactory.createTitledBorder("Faza strzelania gracza"));
            JLabel legendLabel1 = new JLabel("<html><span style='color: red;'>▉</span> - <span style='color: black;'>Cel trafiony</span></html>");
            legendLabel1.setFont(new Font("Arial", Font.BOLD, 18));
            JLabel legendLabel2 = new JLabel("<html><span style='color: black;'>▉</span> - <span style='color: black;'>Cel nietrafiony</span></html>");
            legendLabel2.setFont(new Font("Arial", Font.BOLD, 18));
            JLabel legendLabel3 = new JLabel("<html><span style='color: white;'>▉</span> - <span style='color: black;'>Możliwość oddania strzału</span></html>");
            legendLabel3.setFont(new Font("Arial", Font.BOLD, 18));

            infoPanel.add(legendLabel1);
            infoPanel.add(legendLabel2);
            infoPanel.add(legendLabel3);
        }
        if(layoutEnum == Layout.ComputerShooting)
        {
            infoPanel.setBorder(BorderFactory.createTitledBorder("Faza strzelania komputera"));
            JLabel legendLabel1 = new JLabel("<html><span style='color: red;'>▉</span> - <span style='color: black;'>Statek gracza trafiony</span></html>");
            legendLabel1.setFont(new Font("Arial", Font.BOLD, 18));
            JLabel legendLabel2 = new JLabel("<html><span style='color: black;'>▉</span> - <span style='color: black;'>Strzał nietrafiony</span></html>");
            legendLabel2.setFont(new Font("Arial", Font.BOLD, 18));
            JLabel legendLabel3 = new JLabel("<html><span style='color: white;'>▉</span> - <span style='color: black;'>Pole jeszcze niezaatakowane</span></html>");
            legendLabel3.setFont(new Font("Arial", Font.BOLD, 18));
            JLabel legendLabel4 = new JLabel("<html><span style='color: gray;'>▉</span> - <span style='color: black;'>Pozycja statku gracza</span></html>");
            legendLabel4.setFont(new Font("Arial", Font.BOLD, 18));
            JLabel legendLabel5 = new JLabel("<html><span style='color: yellow;'>▢</span> - <span style='color: black;'>Ostatni strzał komputera</span></html>");
            legendLabel5.setFont(new Font("Arial", Font.BOLD, 18));
            infoPanel.add(legendLabel1);
            infoPanel.add(legendLabel2);
            infoPanel.add(legendLabel3);
            infoPanel.add(legendLabel4);
            infoPanel.add(legendLabel5);
        }

        frame.add(infoPanel, BorderLayout.EAST);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        refreshConsole();
    }
    /**
     * Refreshes the console area with the latest messages.
     */
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
    /**
     * Displays the end game dialog with the result and options to save the score.
     *
     * @param gameEndDto Data transfer object containing end game information.
     */
    public static void GenerateEndGame(GameEndDto gameEndDto){
        var frame = Client.frame;
        JDialog dialog = new JDialog(frame, "Koniec gry", true);
        dialog.setSize(600, 300);
        dialog.setLayout(new BorderLayout());
        var panel = new JPanel(new GridLayout(5, 1, 20, 20));
        JLabel endGameLabel = new JLabel("Koniec gry!", SwingConstants.CENTER);
        endGameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        JLabel winOrloseLabel;
        if(gameEndDto.isPlayerWon()) {
            winOrloseLabel = new JLabel("<html><span style='color: green;'>Wygrałeś!</span></html>", SwingConstants.CENTER);
        } else {
            winOrloseLabel = new JLabel("<html><span style='color: red;'>Przegrałeś!</span></html>", SwingConstants.CENTER);
        }
        winOrloseLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel gameTimeLabel = new JLabel("Czas gry: " + gameEndDto.getGameTimeInSeconds() + " sekund", SwingConstants.CENTER);
        gameTimeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel movesLabel = new JLabel("Liczba wykonanych ruchów: " + gameEndDto.getMovesDoneByYou(), SwingConstants.CENTER);
        movesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        JButton saveScore = new JButton("Zapisz wynik");
        saveScore.setMaximumSize(new Dimension(200, 50));
        JButton dontSaveScore = new JButton("Nie zapisuj");
        dontSaveScore.setMaximumSize(new Dimension(200, 50));
        saveScore.addActionListener(e -> {
            JDialog nickNameDialog = new JDialog(dialog, "Zapisanie wyniku", true);
            nickNameDialog.setSize(800, 100);
            nickNameDialog.setLayout(new BorderLayout());
            JLabel nickNameLabel = new JLabel("Podaj swój nick oraz wciśnij ENTER", SwingConstants.CENTER);
            JTextField nickNameField = new JTextField();
            var nicknamePanel = new JPanel(new GridLayout(1, 2, 20, 20));
            nicknamePanel.add(nickNameLabel);
            nicknamePanel.add(nickNameField);
            nickNameField.addActionListener(e1 -> {
                String playerName = nickNameField.getText();
                MessageHandler.sendObject(new ScoreDto(playerName, gameEndDto.isPlayerWon(), gameEndDto.getGameTimeInSeconds(), gameEndDto.getMovesDoneByYou()));
            });
            nickNameDialog.add(nicknamePanel, BorderLayout.CENTER);
            nickNameDialog.setLocationRelativeTo(frame);
            nickNameDialog.setVisible(true);
        });
        dontSaveScore.addActionListener(e -> System.exit(0));
        panel.add(endGameLabel);
        panel.add(winOrloseLabel);
        panel.add(gameTimeLabel);
        panel.add(movesLabel);
        buttonPanel.add(saveScore);
        buttonPanel.add(dontSaveScore);
        panel.add(buttonPanel);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    /**
     * Displays the scoreboard dialog with a list of player scores.
     *
     * @param scoreboardDto Data transfer object containing the scoreboard.
     */
    public static void GenerateScoreboard(ScoreboardDto scoreboardDto) {

        JFrame frame = Client.frame;
        JDialog dialog = new JDialog(frame, "Tablica wyników", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());
        String[] columnNames = {"Lp.", "Nazwa", "Rezultat", "Czas", "Ruchy"};
        Object[][] data = new Object[scoreboardDto.getScores().size()][5];
        int i = 0;
        for (ScoreDto score : scoreboardDto.getScores()) {
            data[i][0] = i + 1; // Lp.
            data[i][1] = score.getPlayerName();
            data[i][2] = score.isPlayerWon() ? "Wygrana" : "Przegrana";
            data[i][3] = score.getGameTimeInSeconds() + " sekund";
            data[i][4] = score.getMovesDoneByYou();
            i++;
        }
        JTable scoreboardTable = new JTable(data, columnNames);
        scoreboardTable.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(scoreboardTable);
        dialog.add(scrollPane, BorderLayout.CENTER);
        JButton closeButton = new JButton("Zamknij");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
