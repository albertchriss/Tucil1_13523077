package com.myapp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import java.awt.image.BufferedImage;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import puzzle.FileReader;
import puzzle.Solver;

public class Main {

    @FXML
    private Text alertMsg;

    @FXML
    private TextArea blocksInput;

    @FXML
    private TextField colInput;

    @FXML
    private Text filename;

    @FXML
    private ScrollPane gridContainer;

    @FXML
    private TextArea mapInput;

    @FXML
    private Pane mapInputContainer;

    @FXML
    private ChoiceBox<String> modeInput;

    @FXML
    private TextField numBlocksInput;

    @FXML
    private TextField rowInput;

    @FXML
    private Button saveButton;
    
    private VBox container;

    private FileChooser fileChooser = new FileChooser();
    private File inputFile;
    private FileReader reader = new FileReader();
    private Solver solver;
    private final Map<Character, Color> colorMap = new HashMap<>();

    @FXML
    public void initialize(){
        modeInput.setItems(FXCollections.observableArrayList("DEFAULT", "CUSTOM", "PYRAMID"));
        modeInput.setValue("DEFAULT");
        modeInput.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("CUSTOM".equals(newValue)) {
                mapInputContainer.setDisable(false);
            } else {
                mapInput.clear();
                mapInputContainer.setDisable(true);
            }
        });
        mapInputContainer.setDisable(true);
        initColor();
        saveButton.setDisable(true);
    }  
    
    @FXML
    void onClickUploadFile(ActionEvent event) {
        alertMsg.setText("");
        alertMsg.setFill(Color.RED);
        
        File initialDirectory = new File("./test");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        inputFile = fileChooser.showOpenDialog(new Stage());
        
        if (inputFile == null) {
            return;
        }

        filename.setText(inputFile.getName());
        
        reader.readInput(inputFile.toPath());
        if (!reader.validInput){
            alertMsg.setText(reader.errorMsg);
            return;
        }

        rowInput.setText(reader.row + "");
        colInput.setText(reader.col + "");
        numBlocksInput.setText(reader.numBlocks + "");
        modeInput.setValue(reader.mode);
        if (reader.mode.equals("CUSTOM")){
            mapInput.setText(reader.getMapAsString());
        }
        blocksInput.setText(reader.getBlocksAsString());
    }

    @FXML
    void onClickSolve(ActionEvent event) {
        saveButton.setDisable(true);
        gridContainer.setContent(null);
        alertMsg.setText("");
        alertMsg.setFill(Color.RED);
        reader.readInput(rowInput.getText(), colInput.getText(), modeInput.getValue(), numBlocksInput.getText(), blocksInput.getText(), mapInput.getText());

        if (!reader.validInput){
            alertMsg.setText(reader.errorMsg);
            return;
        }
        if (reader.mode.equals("DEFAULT")){
            solver = new Solver(reader.row, reader.col, reader.blocks);
        }
        else if (reader.mode.equals("CUSTOM")){
            solver = new Solver(reader.row, reader.col, reader.map, reader.blocks);
        }
        else{
            solver = new Solver(reader.row, reader.col, reader.row, reader.blocks);
        }
        container = new VBox(5);
        container.setAlignment(Pos.CENTER);

        Task<Void> solveTask = new Task<Void>() {
            @Override
            protected Void call() {
                if (reader.mode.equals("DEFAULT")) {
                    solver = new Solver(reader.row, reader.col, reader.blocks);
                } 
                else if (reader.mode.equals("CUSTOM")) {
                    solver = new Solver(reader.row, reader.col, reader.map, reader.blocks);
                } 
                else {
                    solver = new Solver(reader.row, reader.col, reader.row, reader.blocks);
                }

                solver.solve();  // Solve on background thread
                return null;
            }
        };

        // When solver.solve() finishes, update the UI on the JavaFX Application Thread
        solveTask.setOnSucceeded(e -> {
            if (!solver.isSolved) {
                alertMsg.setFill(Color.RED);
                alertMsg.setText(solver.solveMsg + "\nBanyak kasus ditinjau: " + solver.getCounter() + "                     Waktu Pencarian: " + solver.getDuration() + " ms");
            } 
            else {
                container.getChildren().clear();
                for (int k = solver.getHeight() - 1; k >= 0; k--) {
                    GridPane grid = createGrid(k);
                    container.getChildren().add(grid);
                }

                gridContainer.setContent(container);
                container.setPrefHeight(Math.max(gridContainer.getHeight(), container.getHeight()));
                container.setPrefWidth(Math.max(gridContainer.getWidth(), container.getWidth()));

                alertMsg.setFill(Color.GREEN);
                alertMsg.setText("Banyak kasus ditinjau: " + solver.getCounter() + "                     Waktu Pencarian: " + solver.getDuration() + " ms");
                saveButton.setDisable(false);
            }
        });

        solveTask.setOnFailed(e -> {
            alertMsg.setText("Error: Failed to solve.");
        });

        Task<Void> setCounter = new Task<>() {
            @Override
            protected Void call() {
                alertMsg.setFill(Color.BLACK);
                while (!solveTask.isDone()) {
                    // Update the UI safely
                    Platform.runLater(() -> {
                        container.getChildren().clear();
                        for (int k = solver.getHeight() - 1; k >= 0; k--) {
                            GridPane grid = createGrid(k);
                            container.getChildren().add(grid);
                        }
        
                        gridContainer.setContent(container);
                        container.setPrefHeight(Math.max(gridContainer.getHeight(), container.getHeight()));
                        container.setPrefWidth(Math.max(gridContainer.getWidth(), container.getWidth()));
                        alertMsg.setText("Banyak kasus ditinjau: " + solver.getCounter());
                    });

                    try {
                        Thread.sleep(100); // Small delay to prevent excessive UI updates
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                return null;
            }
        };

        new Thread(solveTask).start();
        new Thread(setCounter).start();
    }

    @FXML
    void onClickSaveRes(ActionEvent event) {
        // Ensure JavaFX thread
        Platform.runLater(() -> {
            try {
                // Capture snapshot
                WritableImage image = container.snapshot(new SnapshotParameters(), null);

                // Convert to BufferedImage manually
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

                int[] buffer = new int[width * height];
                image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), buffer, 0, width);
                bufferedImage.setRGB(0, 0, width, height, buffer, 0, width);

                // Save as PNG
                ImageIO.write(bufferedImage, "png", new File("output.png"));
                System.out.println("Image saved to: output.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private GridPane createGrid(int height){
        int size=25, width = solver.getWidth() - height, length = solver.getLength() - height;
        GridPane grid = new GridPane();
        grid.setHgap(5); grid.setVgap(5);
        grid.setPadding(new javafx.geometry.Insets(5));
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: rgb(120,120,120); -fx-background-radius: 10px;");
        grid.setMaxWidth((size + 5)*width+5);
        grid.setMaxHeight((size + 5)*length+5);
        for (int row = height; row < solver.getLength(); row++) {
            for (int col = height; col < solver.getWidth(); col++) {
                char letter;
                if (solver.getMap()[row][col][height] > 0) {
                    letter = (char) solver.getMap()[row][col][height];
                }
                else if (solver.getMap()[row][col][height] == -1){
                    letter = '-';
                }
                else{
                    letter = '\0';
                }
                Color color = colorMap.getOrDefault(letter, Color.GRAY); // Default to gray if unknown
                Rectangle rect = new Rectangle(size, size);
                rect.setFill(color);
                rect.setArcHeight(size * 0.9);
                rect.setArcWidth(size * 0.9);
                // Create the text
                if (letter == '-') letter = '\0';
                Text text = new Text(letter + "");
                text.setFill(Color.WHITE); 
                text.setFont(new Font(size * 0.5));  
                text.setTextAlignment(TextAlignment.CENTER); 
                text.setBoundsType(TextBoundsType.VISUAL); 
                text.setStyle("-fx-font-weight: bold;");

                // Stack rectangle and text together
                StackPane stack = new StackPane();
                stack.getChildren().addAll(rect, text);
                stack.setAlignment(Pos.CENTER);
                grid.add(stack, col - height, row - height);
            }
        }
        return grid;
    }

    private void initColor(){
        colorMap.put('-', Color.rgb(120, 120, 120));
        colorMap.put('A', Color.BLUE);
        colorMap.put('B', Color.RED);
        colorMap.put('C', Color.GREEN);
        colorMap.put('D', Color.ORANGE);
        colorMap.put('E', Color.PURPLE);
        colorMap.put('F', Color.YELLOWGREEN);
        colorMap.put('G', Color.CYAN);
        colorMap.put('H', Color.MAGENTA);
        colorMap.put('I', Color.BROWN);
        colorMap.put('J', Color.PINK);
        colorMap.put('K', Color.BLACK);
        colorMap.put('L', Color.LIGHTBLUE);
        colorMap.put('M', Color.LIMEGREEN);
        colorMap.put('N', Color.GOLD);
        colorMap.put('O', Color.INDIGO);
        colorMap.put('P', Color.DARKGREEN);
        colorMap.put('Q', Color.DARKRED);
        colorMap.put('R', Color.DARKBLUE);
        colorMap.put('S', Color.DARKORANGE);
        colorMap.put('T', Color.DARKVIOLET);
        colorMap.put('U', Color.TEAL);
        colorMap.put('V', Color.SKYBLUE);
        colorMap.put('W', Color.SILVER);
        colorMap.put('X', Color.MIDNIGHTBLUE);
        colorMap.put('Y', Color.SALMON);
        colorMap.put('Z', Color.OLIVE);
    }

}
