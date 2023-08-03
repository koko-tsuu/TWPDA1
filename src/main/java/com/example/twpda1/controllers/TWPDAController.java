package com.example.twpda1.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TWPDAController {

    //read the input file and convert
    // it into figures in the GUI

    public int numOfStates = 0;
    public List<String> states = new ArrayList<>();
    public int numOfInputs = 0;
    public List<String> inputs = new ArrayList<>();
    public String leftEndmarker = "";
    public String rightEndmarker = "";
    public int numberOfPSymbols = 0;
    public List<String> pSymbols = new ArrayList<>();
    public int numberofFStates = 0;
    public int numberOfTransitions = 0;
    public List<String> transitions = new ArrayList<>();
    public String startState = "";
    public List<String> finalStates = new ArrayList<>();

    public List<StackPane> circlesList = new ArrayList<>();
    public String initPSymbol = "";

    private double circleRadius = 30; // Set the radius of the circle (adjust as needed)
    private double circleX = 200; // Set the X position of the circle (adjust as needed)
    private double circleY = 200; // Set the Y position of the circle (adjust as needed)

    //convert the file content to figures
    public void readAndConvert(String fileContents) {
        processLines(fileContents);
    }

    public void processLines(String code) {
        // Split the string into lines using the newline character as the delimiter
        String[] lines = code.split("\\n");


        for(int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if(i == 0) {
                //first line get the number assign to states
                numOfStates = Integer.parseInt(line);
            } else if(i == 1) {
                //second line, read the inputs based on the number
                String[] statesArray = line.split(" ");
                states.addAll(Arrays.asList(statesArray));
            } else if (i == 2) {
                //third line, get the number assign to variable inputs
                numOfInputs = Integer.parseInt(line);
            } else if (i == 3) {
                //fourth line, assign to list of inputs
                String[] inputArray = line.split(" ");
                inputs.addAll(Arrays.asList(inputArray));
            } else if (i == 4) {
                //fifth line, assign as left endmarker
                leftEndmarker = line.trim();
            } else if (i == 5) {
                //sixth line, assign as right endmarker
                rightEndmarker = line.trim();
            } else if (i == 6) {
                //seventh line, assign as number of pushdown symbols
                numberOfPSymbols = Integer.parseInt(line);
            } else if (i == 7) {
                //eight line, read inputs assign to list of pushdown symbols
                String[] pSymbolsArray = line.split(" ");
                pSymbols.addAll(Arrays.asList(pSymbolsArray));
            } else if (i == 8) {
                //ninth line, assign number of transitions
                numberOfTransitions = Integer.parseInt(line);
            } else if(i == 9) {
                //tenth line, get the transitions
                transitions.add(line);

                for (int j = i + 1; j <= i + numberOfTransitions-1; j++) {
                    transitions.add(lines[j]);
                }

            } else if (i == 10 + numberOfTransitions-1) {
                //11 line, assign a start state
                initPSymbol = line.trim();
            } else if (i == 11 + numberOfTransitions-1) {
                //12th line, assign a start state
                startState = line.trim();
            } else if (i == 12 + numberOfTransitions-1) {
                //13th line, assign number of final states to a variable
                numberofFStates = Integer.parseInt(line);
            }  else if (i == 13 + numberOfTransitions-1) {
                //14th line, get the list of final states
                String[] finalStatesArray = line.split(" ");
                finalStates.addAll(Arrays.asList(finalStatesArray));
            }
        }

    }

}
