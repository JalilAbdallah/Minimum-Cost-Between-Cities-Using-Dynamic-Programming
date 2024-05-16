package ProjectOne;



import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


// Main application class that extends JavaFX Application
public class Main extends Application {
    
    // Declare variables for the application
    File file;
    int[][] arr;
    City[] cities;
    String source;
    String des;

    // Start method that sets up the primary stage
    public void start(Stage stage) throws Exception {
        // Setup custom cursor
        Image cursorImage = new Image("cursor.png");
        Cursor cursor = new ImageCursor(cursorImage);

        // Main layout pane
        BorderPane bp = new BorderPane();

        // Set up visual effects for glow and color adjustment
        Glow glow = new Glow();
        glow.setLevel(1.0);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.1);
        colorAdjust.setContrast(0.0);
        colorAdjust.setSaturation(0.8);
        colorAdjust.setHue(0.166);
        glow.setInput(colorAdjust);

        // Logo at the top of the application
        ImageView logoTopView = new ImageView(new Image("bzuLogo.png"));
        logoTopView.setPreserveRatio(true);
        logoTopView.setFitHeight(289.5 / 4);
        logoTopView.setFitWidth(422.5 / 4);

        // Event handler for mouse interaction on the logo
        logoTopView.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> logoTopView.setEffect(glow));
        logoTopView.addEventHandler(MouseEvent.MOUSE_EXITED, e -> logoTopView.setEffect(null));

        HBox topBox = new HBox(logoTopView);
        topBox.setAlignment(Pos.TOP_LEFT);
        bp.setTop(topBox);

        // Secondary logo in the center
        Image logoImage = new Image("logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(logoImage.getHeight() / 3.5);
        logoView.setFitWidth(logoImage.getWidth() / 3.5);

        logoView.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> logoView.setEffect(glow));
        logoView.addEventHandler(MouseEvent.MOUSE_EXITED, e -> logoView.setEffect(null));

        addZoomEffect(logoView, 0.1, 1.0, 1500);

        Image minimumImage = new Image("MinimumCost.png");
        ImageView minimumView = new ImageView(minimumImage);
        minimumView.setFitHeight(minimumImage.getHeight() / 5.5);
        minimumView.setFitWidth(minimumImage.getWidth() / 5.5);

        Image betweenImage = new Image("BetweenTwoCities.png");
        ImageView betweenView = new ImageView(betweenImage);
        betweenView.setFitHeight(betweenImage.getHeight() / 9.5);
        betweenView.setFitWidth(betweenImage.getWidth() / 9.5);

        // Create a sequential transition for animations
        SequentialTransition sequentialTransition = new SequentialTransition();
        addZoomEffect(minimumView, 0.1, 1.0, 850, sequentialTransition);
        addZoomEffect(betweenView, 0.1, 1.0, 700, sequentialTransition);
        sequentialTransition.play();

        VBox vBox = new VBox(10, logoView, minimumView, betweenView);
        vBox.setAlignment(Pos.CENTER);
        bp.setCenter(vBox);

        // Button for loading cities from a file
        Button loadCities = new Button("Load The Cities File");
        BorderPane.setAlignment(loadCities, Pos.CENTER);
        bp.setBottom(loadCities);

        // Setup the scene and stage
        Scene scene = new Scene(bp, 1200, 600);
        bp.setPadding(new Insets(15, 15, 15, 15));

        // Configure file chooser for loading files
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Chooser");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*txt"));

        // Event handler for loading cities button
        loadCities.setOnAction(e -> {
            file = fileChooser.showOpenDialog(stage);
            try {
                readCities();
                SolutionScr solutionScr = new SolutionScr(stage, scene, cities, source, des);
                Scene solutionScene = new Scene(solutionScr);
                solutionScene.setCursor(cursor);
                solutionScene.getStylesheets().add("theme.css");
                stage.setScene(solutionScene);
                stage.setMaximized(true);
            } catch (FileNotFoundException e2) {
                // Handle file not found
            } catch (Exception e2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid File Format");
                alert.showAndWait();
            }
        });
        scene.setCursor(cursor);
        scene.getStylesheets().add("theme.css");
        stage.setScene(scene);
        stage.setTitle("Minimum Cost Between Two Cities");
        stage.show();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }

    // Method to read city data from a file
    private void readCities() throws IOException {
        if (file == null)
            throw new FileNotFoundException();
        try (Scanner sc = new Scanner(file)) {
			int numberOfCities = Integer.parseInt(sc.nextLine().trim());

			String[] tkz = sc.nextLine().split(",");

			source = tkz[0].trim();
			des = tkz[1].trim();
			cities = new City[numberOfCities];

			int number = 0;
			int totalLines = 1;
			int firstLine = 0;
			int stage = 1;
			City city;
			while (sc.hasNextLine()) {
				String[] lineInfo = sc.nextLine().split(",");

				if (number == 0) {
					city = new City(lineInfo[0], 0, 0, new int[0]);
					cities[number++] = city;
				}

				for (int i = 1; i < lineInfo.length; i++) {
					lineInfo[i] = lineInfo[i].trim();
					String[] info = lineInfo[i].substring(1, lineInfo[i].length() - 1).split(":");
					if (firstLine == 0) {
						int[] arr = new int[totalLines];

						arr[0] = Integer.parseInt(info[1]);

						city = new City(info[0], stage, Integer.parseInt(info[2]), arr);

						cities[number] = city;
						number++;
					} else {
						String cityName = info[0];
						getCity(cityName, numberOfCities).getPetrolCost()[firstLine] = Integer.parseInt(info[1]);
					}
				}

				if (firstLine == 0)
					stage++;

				firstLine++;

				if (firstLine == totalLines) {
					firstLine = 0;
					totalLines = lineInfo.length - 1;
				}

			}
        } catch (IOException e) {
            throw new IOException();
        }
    }

    // Helper method to get a city by name
    private City getCity(String cityName, int numberOfCities) {
        for (int i = 0; i < numberOfCities; i++)
            if (cities[i].getName().equals(cityName))
                return cities[i];
        return null;
    }

    // Helper method to apply a zoom effect to an image view
    private void addZoomEffect(ImageView imageView, double fromScale, double toScale, int durationMillis) {
        ScaleTransition st = new ScaleTransition(Duration.millis(durationMillis), imageView);
        st.setFromX(fromScale);
        st.setFromY(fromScale);
        st.setToX(toScale);
        st.setToY(toScale);
        st.play();
    }

    // Overloaded method to add a zoom effect with a sequential transition
    private void addZoomEffect(ImageView imageView, double fromScale, double toScale, int durationMillis,
                               SequentialTransition sequentialTransition) {
        ScaleTransition st = new ScaleTransition(Duration.millis(durationMillis), imageView);
        st.setFromX(fromScale);
        st.setFromY(fromScale);
        st.setToX(toScale);
        st.setToY(toScale);
        sequentialTransition.getChildren().add(st);
    }
}
