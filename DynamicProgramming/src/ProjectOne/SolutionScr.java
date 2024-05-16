package ProjectOne;

import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The SolutionScr class extends BorderPane and is used to display a solution screen
 * for a dynamic programming problem in a graphical user interface. It visualizes the
 * dynamic programming table along with paths and costs.
 */
public class SolutionScr extends BorderPane {

    City[] cities; // Array of City objects
    final String source; // Source city name
    final String des; // Destination city name
    CellInfo[][] dpTable; // 2D array representing the dynamic programming table
    private final int prefWidth = 50; // Preferred width for cells and labels in the grid
    private final int prefHeight = 10; // Preferred height for cells and labels in the grid

    private int start, end; // Indices for source and destination cities within the array
    private int numberOfStages; // Number of stages in the journey

    private Stage stage; // Stage for the application
    private Scene scene; // Scene for navigation

    /**
     * Constructor for the SolutionScr class.
     * Initializes the UI components and dynamic programming structures based on the provided cities and path.
     * @param stage The primary stage of the application
     * @param scene The previous scene for navigation purposes
     * @param cities Array of cities involved in the problem
     * @param source Name of the source city
     * @param des Name of the destination city
     */
    public SolutionScr(Stage stage, Scene scene, City[] cities, String source, String des) {

        this.stage = stage;
        this.scene = scene;

        this.cities = cities;
        this.source = source;
        this.des = des;

        initializeSourceAndDes();

        generateDPTable();

        addFX();
    }

    /**
     * Initializes UI components and binds them together on the layout.
     */
    private void addFX() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal gap between grid cells
        gridPane.setVgap(10); // Vertical gap between grid cells

        this.numberOfStages = cities[cities.length - 1].getStage();

        String[] colors = arrayOfColors(this.numberOfStages);

        // Adding column headers
        for (int i = start; i < end + 1; i++) {
            Label header = new Label(cities[i].getName());
            header.setStyle(
                    "-fx-background-color: #222831; -fx-text-fill: #FFD369; -fx-font-size: 14px; -fx-padding: 0.5em; -fx-background-radius: 10px; -fx-border-color: "
                            + colors[cities[i].getStage()] + "; -fx-border-width: 2px; -fx-border-radius: 10px;");
            header.setAlignment(Pos.CENTER);
            header.setPrefWidth(prefWidth);
            header.setPrefHeight(prefHeight);
            gridPane.add(header, i + 1, 0); // +1 because first column is for row headers
        }

        // Adding row headers
        for (int i = start; i < end + 1; i++) {
            Label rowHeader = new Label(cities[i].getName());
            rowHeader.setStyle(
                    "-fx-background-color: #222831; -fx-text-fill: #FFD369; -fx-font-size: 14px; -fx-padding: 0.5em; -fx-background-radius: 10px; -fx-border-color: "
                            + colors[cities[i].getStage()] + "; -fx-border-width: 2px; -fx-border-radius: 10px;");
            rowHeader.setAlignment(Pos.CENTER);
            rowHeader.setPrefWidth(prefWidth);
            rowHeader.setPrefHeight(prefHeight);
            gridPane.add(rowHeader, 0, i + 1);
        }

        // Adding data cells
        for (int i = start; i < end + 1; i++) {
            for (int j = start; j < end + 1; j++) {
                if (dpTable[i][j] != null) {
                    Label cell = new Label(String.valueOf(dpTable[i][j].getOptimalCost()));
                    cell.setCursor(Cursor.HAND);

                    String optimalPath = dpTable[i][j].getOptimalPath().toString();
                    String city_i = cities[i].getName(), city_j = cities[j].getName();
                    CellInfo[] cellInfos = dpTable[i][j].getCellInfos();
                    cell.setOnMouseClicked(event -> {
                        showDetailsStage(
                                "The Optimal Path Between City " + city_i + " and " + city_j + " is: " + optimalPath,
                                cellInfos);
                    });

                    if (i == 0 && j == dpTable[i].length - 1)
                        cell.setStyle(
                                "-fx-background-color: #FFF5E0; -fx-text-fill: #222831; -fx-font-size: 14px; -fx-padding: 0.5em; -fx-background-radius: 10px; -fx-border-color: #41B06E; -fx-border-width: 2px; -fx-border-radius: 10px;");
                    else
                        cell.setStyle(
                                "-fx-background-color: #FFF5E0; -fx-text-fill: #222831; -fx-font-size: 14px; -fx-padding: 0.5em; -fx-background-radius: 10px; -fx-border-color: #1679AB; -fx-border-width: 2px; -fx-border-radius: 10px;");

                    cell.setAlignment(Pos.CENTER);

                    cell.setPrefWidth(prefWidth);
                    cell.setPrefHeight(prefHeight);
                    gridPane.add(cell, j + 1, i + 1);

                }
            }
        }

        Label dynamicLabel = new Label("Dynamic Programming Table");
        dynamicLabel.setStyle("-fx-text-fill: #FFF5E0; " + "-fx-padding: 2; " + "-fx-font-size: 22px;");

        VBox leftBox = new VBox(10, dynamicLabel, gridPane);
        leftBox.setAlignment(Pos.CENTER);

        // Wrapping the VBox with a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(leftBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setStyle("-fx-background: black; " + "-fx-border-color: gold; " + "-fx-border-width: 2px; "
                + "-fx-border-radius: 10px;");

        scrollPane.setPadding(new Insets(10));
        setLeft(scrollPane);

        setPadding(new Insets(15));
        gridPane.setAlignment(Pos.CENTER);
        setAlignment(gridPane, Pos.CENTER);

        Label optimalCostLabel = new Label("Optimal Cost Between " + this.source + " and " + this.des);
        optimalCostLabel.setStyle("-fx-text-fill: #FFF5E0; " + "-fx-padding: 2; " + "-fx-font-size: 16px;");
        optimalCostLabel.setAlignment(Pos.CENTER);

        Label optimalCostSolution = new Label(String.valueOf(dpTable[start][end].getOptimalCost()));
        optimalCostSolution.setStyle(
                "-fx-background-color: #FFF5E0; -fx-text-fill: #222831; -fx-font-size: 16px; -fx-padding: 0.5em; -fx-background-radius: 10px; -fx-border-color: #41B06E; -fx-border-width: 2px; -fx-border-radius: 10px;");

        optimalCostSolution.setAlignment(Pos.CENTER);

        Label optimalPathLabel = new Label("Optimal Path Between " + this.source + " and " + this.des);
        optimalPathLabel.setStyle("-fx-text-fill: #FFF5E0; " + "-fx-padding: 2; " + "-fx-font-size: 16px;");
        optimalPathLabel.setAlignment(Pos.CENTER);

        Label optimalPathSolution = new Label(dpTable[start][end].getOptimalPath());
        optimalPathSolution.setStyle(
                "-fx-background-color: #FFF5E0; -fx-text-fill: #222831; -fx-font-size: 16px; -fx-padding: 0.5em; -fx-background-radius: 10px; -fx-border-color: #41B06E; -fx-border-width: 2px; -fx-border-radius: 10px;");

        optimalPathSolution.setAlignment(Pos.CENTER);

        Button alternativeButton = new Button("Alternative Paths");


        CellInfo[] solutionInfo = dpTable[start][end].getCellInfos();
        alternativeButton.setOnAction(e -> {
            showDetailsStage(solutionInfo);            
        });
        alternativeButton.setMaxWidth(Double.MAX_VALUE);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            stage.setScene(scene);
        });
        backButton.setMaxWidth(Double.MAX_VALUE);

        VBox rightBox = new VBox(10, optimalCostLabel, optimalCostSolution, optimalPathLabel, optimalPathSolution,
                alternativeButton, backButton);
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPadding(new Insets(25));

        // Dynamic content resizing and style application
        double maxWidth = Math.max(optimalCostSolution.getLayoutBounds().getWidth(),
                optimalPathSolution.getLayoutBounds().getWidth());
        maxWidth += 100; // Additional padding for aesthetics

        optimalCostSolution.setMinWidth(maxWidth);
        optimalPathSolution.setMinWidth(maxWidth);

        setRight(rightBox);
        BorderPane.setMargin(rightBox, new Insets(0, 150, 0, 0));

        // Button for navigating back to the previous scene.
        Button back = new Button("Back");

        back.setOnAction(e -> {
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setFullScreen(true);

        });

    }

    /**
     * Initializes the source and destination indices based on the cities array.
     */
    private void initializeSourceAndDes() {
        for (int i = 0; i < cities.length; i++) {
            if (cities[i].getName().equals(source)) {
                start = i;
            }
        }

        for (int i = 0; i < cities.length; i++) {
            if (cities[i].getName().equals(des)) {
                end = i;
            }
        }

    }

    /**
     * Generates the dynamic programming table for the problem using the cities' information.
     */
    private void generateDPTable() {
        dpTable = new CellInfo[cities.length][cities.length];

        for (int i = 0; i < cities.length; i++) {
            int[] petrolCost = cities[i].getPetrolCost();
            int stage = cities[i].getStage();

            int count = 0;
            for (int j = 0; j < cities.length; j++)
                if (cities[j].getStage() == stage - 1) {
                    count = j;
                    break;
                }

            for (int j = 0; j < petrolCost.length; j++) {
                CellInfo cellInfo = new CellInfo(cities[i].getHotelCost() + petrolCost[j],
                        cities[j + count].getName() + " -> " + cities[i].getName());
                dpTable[j + count][i] = cellInfo;

            }
        }

        for (int i = 0; i < dpTable.length; i++)
            for (int j = 0; j < dpTable.length; j++)
                if (cities[j].getStage() - cities[i].getStage() > 1)
                    dpTable[i][j] = setCost(i, j);

    }

    /**
     * Determines and sets the optimal cost and path for a particular cell in the dynamic programming table.
     * @param i Source city index
     * @param j Destination city index
     * @return CellInfo object with the minimal cost and path
     */
    private CellInfo setCost(int i, int j) {
        int stage = cities[j].getStage() - 1;
        int min = Integer.MAX_VALUE;

        int optimal_i = 0, optimal_k = 0;

        CellInfo[] cellInfos = new CellInfo[3];
        byte cellInfoCounter = 0;
        for (int k = i + 1; k < j; k++) {
            if (dpTable[i][k] != null && dpTable[k][j] != null) {

                int cost = dpTable[i][k].getOptimalCost() + dpTable[k][j].getOptimalCost();

                if (cellInfoCounter < 3) {
                    CellInfo cellInfo = new CellInfo(cost,
                            dpTable[i][k].getOptimalPath() + " -> " + cities[j].getName());
                    cellInfos[cellInfoCounter++] = cellInfo;
                }

                if (cities[k].getStage() == stage && cost < min) {
                    min = cost;
                    optimal_i = i;
                    optimal_k = k;
                }
            }
        }

        CellInfo cellInfo = new CellInfo(min,
                dpTable[optimal_i][optimal_k].getOptimalPath() + " -> " + cities[j].getName());

        cellInfo.setCellInfos(cellInfos);
        return cellInfo;

    }

    /**
     * Creates and shows a new stage displaying the details for a specific cell, including alternative solutions.
     * @param message The message to display in the stage
     * @param cellInfos Array of alternative CellInfo objects for additional details
     */
    private void showDetailsStage(String message, CellInfo[] cellInfos) {
        // Create a new stage
        Stage detailStage = new Stage();

        // Set the content of the stage
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-background-color: #FFF5E0; -fx-text-fill: #222831; -fx-font-size: 16px; -fx-padding: 0.5em; -fx-background-radius: 10px; -fx-border-color: #41B06E; -fx-border-width: 2px; -fx-border-radius: 10px;");

        messageLabel.setWrapText(true);
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getStylesheets().add("theme.css");
        layout.getChildren().add(messageLabel);

        if (cellInfos != null) {
            Label alternativeLabel = new Label("Alternative Solutions");
            alternativeLabel.setStyle("-fx-text-fill: #FFF5E0; " + "-fx-padding: 2; " + "-fx-font-size: 16px;");
            alternativeLabel.setAlignment(Pos.CENTER);

            layout.getChildren().add(alternativeLabel);

            for (int i = 0; i < cellInfos.length; i++)
                if (cellInfos[i] != null) {
                    Label alternative = new Label("Alternative Solution : " + cellInfos[i].getOptimalPath()
                            + " With Cost " + cellInfos[i].getOptimalCost());
                    alternative.setStyle(
                            "-fx-background-color: #FFF5E0; -fx-text-fill: #222831; -fx-font-size: 16px; -fx-padding: 0.5em; -fx-background-radius: 10px; -fx-border-color: #41B06E; -fx-border-width: 2px; -fx-border-radius: 10px;");

                    alternative.setAlignment(Pos.CENTER);

                    layout.getChildren().add(alternative);

                }
        }

        Scene scene = new Scene(layout, 600, 500);

        // Configure and show the stage
        detailStage.setTitle("Path Details");
        detailStage.setScene(scene);
        detailStage.show();
    }

    /**
     * Similar to the above method but handles showing alternatives without a specific message.
     * @param cellInfos Array of CellInfo objects containing alternative solutions
     */
    private void showDetailsStage(CellInfo[] cellInfos) {
        // Create a new stage
        Stage detailStage = new Stage();

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getStylesheets().add("theme.css");

        if (cellInfos != null) {
            Label alternativeLabel = new Label("Alternative Solutions");
            alternativeLabel.setStyle("-fx-text-fill: #FFF5E0; " + "-fx-padding: 2; " + "-fx-font-size: 16px;");
            alternativeLabel.setAlignment(Pos.CENTER);

            layout.getChildren().add(alternativeLabel);

            for (int i = 0; i < cellInfos.length; i++)
                if (cellInfos[i] != null) {
                    Label alternative = new Label("Alternative Solution : " + cellInfos[i].getOptimalPath()
                            + " With Cost " + cellInfos[i].getOptimalCost());
                    alternative.setStyle(
                            "-fx-background-color: #FFF5E0; -fx-text-fill: #222831; -fx-font-size: 16px; -fx-padding: 0.5em; -fx-background-radius: 10px; -fx-border-color: #41B06E; -fx-border-width: 2px; -fx-border-radius: 10px;");

                    alternative.setAlignment(Pos.CENTER);

                    layout.getChildren().add(alternative);

                }
        }

        Scene scene = new Scene(layout, 600, 500);

        // Configure and show the stage
        detailStage.setTitle("Path Details");
        detailStage.setScene(scene);
        detailStage.show();
    }

    /**
     * Generates an array of random color codes.
     * @param size The number of color codes to generate
     * @return An array of hex color codes
     */
    private String[] arrayOfColors(int size) {
        Random rand = new Random();
        String[] colorCodes = new String[size + 1]; // Array to hold color codes

        for (int i = 0; i < colorCodes.length; i++) {
            // Generate random colors
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();

            // Create a color object
            Color color = new Color(r, g, b, 1.0);

            // Convert color to hexadecimal (stripping alpha value)
            colorCodes[i] = String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
                    (int) (color.getBlue() * 255));
        }

        return colorCodes;
    }

}
