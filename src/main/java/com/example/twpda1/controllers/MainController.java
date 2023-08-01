package com.example.twpda1.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainController {
    @FXML
    private AnchorPane rootPane; // The root AnchorPane in main-view.fxml
    private TWPDAController twpdaController;
    private LogicController logicController;


    //reference code in choosing a file and reading it asynchronously
    //https://edencoding.com/how-to-open-edit-sync-and-save-a-text-file-in-javafx/
    @FXML
    private void chooseFile(ActionEvent event) {
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
            // Do something with the file contents, such as displaying in a TextArea
            // For demonstration, we'll just print the contents
            twpdaController = new TWPDAController(rootPane);
            twpdaController.readAndConvert(fileContents);
            logicController = new LogicController(twpdaController);
            logicController.initializeTWPDA();
            logicController.isAccepted();

        });

        Thread thread = new Thread(task);
        thread.start();
    }

    //process the file

}
