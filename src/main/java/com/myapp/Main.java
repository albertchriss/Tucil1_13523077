package com.myapp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    private GridPane gridContainer;

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
    private Text filename;

    FileChooser fileChooser = new FileChooser();
    private File inputFile;

    FileReader reader = new FileReader();

    Solver solver;

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
        gridContainer.setVisible(false);
        initColor();
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
        gridContainer.setVisible(false);
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
        solver.solve();
        if (!solver.isSolved){
            alertMsg.setText(solver.solveMsg);
        }
        else{
            createGrid();
            alertMsg.setFill(Color.GREEN);
            alertMsg.setText("Banyak kasus ditinjau: " + solver.getCounter() + "                     Waktu Pencarian: " + solver.getDuration() + " ms");
        }
    }

    private void createGrid(){
        int size=25 ;
        gridContainer.setVisible(true);
        gridContainer.getChildren().clear(); // Clear previous grid if needed
        gridContainer.setMaxWidth((size + 5)*solver.getWidth()+5);
        gridContainer.setMaxHeight((size + 5)*solver.getLength()+5);
        for (int row = 0; row < solver.getLength(); row++) {
            for (int col = 0; col < solver.getWidth(); col++) {
                char letter;
                if (solver.getMap()[row][col][0] > 0) {
                    letter = (char) solver.getMap()[row][col][0];
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
                Text text = new Text(letter + "");
                text.setFill(Color.WHITE); 
                text.setFont(new Font(size * 0.5));  
                text.setTextAlignment(TextAlignment.CENTER);  // Center text alignment
                text.setBoundsType(TextBoundsType.VISUAL); // More accurate centering
                text.setStyle("-fx-font-weight: bold;");

                // Stack rectangle and text together
                StackPane stack = new StackPane();
                stack.getChildren().addAll(rect, text);
                stack.setAlignment(Pos.CENTER);
                // // Create a clip (mask) to round only the left side
                // Rectangle clip = new Rectangle(size, size);
                // clip.setArcWidth(size * 0.9); // Round left side
                // clip.setArcHeight(size * 0.9);
                // clip.setX(-size * 0.5); // Shift clip to affect only one side
                // Rectangle rect2 = new Rectangle(size, size);
                // rect.setFill(color);
                // Rectangle clip2 = new Rectangle(size, size);
                // clip2.setArcWidth(size * 0.9); // Round left side
                // clip2.setArcHeight(size * 0.9);
                // clip2.setX(size * 0.5); // Shift clip to affect only one side

                // rect.setClip(clip); // Apply the clip
                // rect2.setClip(clip2); // Apply the clip
                // Add the rectangle to the GridPane
                gridContainer.add(stack, col, row);
                // gridContainer.add(rect2, col, row);
            }
        }
    }

    private void initColor(){
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
