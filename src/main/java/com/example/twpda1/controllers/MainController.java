package com.example.twpda1.controllers;

import com.example.twpda1.app.MainApp;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainController {
    @FXML
    private AnchorPane rootPane; // The root AnchorPane in main-view.fxml
    @FXML
    private Button Step;
    @FXML
    private Button Reset;
    private TWPDAController twpdaController;
    private LogicController logicController;
    @FXML
    private TextArea textField;
    @FXML
    private Button runBtn;
    @FXML
    private Text promptText;
    @FXML
    private Button uploadBtn;
    @FXML
    private Text stateTxt;

    @FXML
    private Text state;

    @FXML
    private Text readInput;

    @FXML
    private Text notReadInput;

    @FXML
    private Text stack;

    @FXML
    private Text stackTxt;

    @FXML
    private Text string;

    @FXML
    private TextFlow stringTxt;

    @FXML
    private Text verdict;

    @FXML
    private Text verdictTxt;



    //reference code in choosing a file and reading it asynchronously
    //https://edencoding.com/how-to-open-edit-sync-and-save-a-text-file-in-javafx/
    @FXML
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        //only allow text files to be selected using chooser
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
        );
        //set initial directory somewhere user will recognise
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        //let user select file
        File fileToLoad = fileChooser.showOpenDialog(null);
        //if file has been chosen, load it using asynchronous method (define later)
        if(fileToLoad != null){
            readFileAsync(fileToLoad);
        }
    }

    @FXML
    public void makeAppear() {
        Step.setVisible(true);
        Reset.setVisible(true);
        logicController.isAccepted(textField.getText());
    }

    public void openNewWindow() {
        try {
            Stage newStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/twpda1/main-view.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 600, 355);

            newStage.setTitle("Two Way Pushdown Automata");
            newStage.setScene(newScene);
            newStage.setResizable(false);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void promptInput() {
        chooseFile();
    }

    private void readFileAsync(File fileToLoad) {
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws IOException {
                StringBuilder content = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
                return content.toString();
            }
        };

        task.setOnSucceeded(event -> {
            String fileContents = task.getValue();
            runBtn.setVisible(true);
            promptText.setVisible(true);
            textField.setVisible(true);
            uploadBtn.setVisible(false);
            twpdaController = new TWPDAController();
            twpdaController.readAndConvert(fileContents);
            logicController = new LogicController(twpdaController, stateTxt, state, stackTxt, stack, stringTxt, string, verdict, verdictTxt, Step, Reset, readInput, notReadInput);
            logicController.initializeTWPDA();
        });

        Thread thread = new Thread(task);
        thread.start();
    }


}
