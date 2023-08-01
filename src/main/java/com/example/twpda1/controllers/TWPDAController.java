package com.example.twpda1.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
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

    private double circleRadius = 30; // Set the radius of the circle (adjust as needed)
    private double circleX = 200; // Set the X position of the circle (adjust as needed)
    private double circleY = 200; // Set the Y position of the circle (adjust as needed)

    public TWPDAController(AnchorPane containerPane) {
        this.containerPane = containerPane;
    }

    private AnchorPane containerPane;

    //convert the file content to figures
    public void readAndConvert(String fileContents) {
        processLines(fileContents);

        for(String state : states) {
            createCircleState(state);
        }

        for (String transition : transitions) {
            createTransitions(transition);
        }

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
                //eleventh line, assign a start state
                startState = line.trim();
            } else if (i == 11 + numberOfTransitions-1) {
                //twelfth line, assign number of final states to a variable
                numberofFStates = Integer.parseInt(line);
            }  else if (i == 12 + numberOfTransitions-1) {
                //thirteenth line, get the list of final states
                String[] finalStatesArray = line.split(" ");
                finalStates.addAll(Arrays.asList(finalStatesArray));
            }
        }

//        System.out.println(numOfStates);
//        System.out.println(states);
//        System.out.println(numOfInputs);
//        System.out.println(inputs);
//        System.out.println(leftEndmarker);
//        System.out.println(rightEndmarker);
//        System.out.println(numberOfPSymbols);
//        System.out.println(pSymbols);
        System.out.println(numberOfTransitions);
        System.out.println(transitions);
//        System.out.println(startState);
//        System.out.println(numberofFStates);
//        System.out.println(finalStates);

    }

    public void createCircleState(String txt) {

        // Create a circle
        Circle circle = new Circle(circleRadius);
        circle.setFill(Paint.valueOf("#B7ECC9"));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        // Create a label with the desired text
        Label label = new Label(txt);

        StackPane circles;

        // Create a StackPane to hold the circle and label
        if(finalStates.contains(txt)) {
            double secondCircleRadius = circleRadius + 4; // Adjust the size of the second circle as needed
            Circle secondCircle = new Circle(secondCircleRadius);
            secondCircle.setFill(Paint.valueOf("#B7ECC9")); // Set the fill color to transparent
            secondCircle.setStroke(Color.BLACK); // Set the stroke color of the second circle
            secondCircle.setStrokeWidth(2); // Adjust the width of the second circle's outline

            circles = new StackPane(secondCircle, circle, label);
        } else {
            circles = new StackPane(circle, label);
        }

        circles.setPrefWidth(circleRadius);
        circles.setPrefHeight(circleRadius);

        // Center the label inside the circle
        label.setAlignment(Pos.CENTER);

        // Set the content display of the label to CENTER to ensure it is centered inside the StackPane
        label.setContentDisplay(ContentDisplay.CENTER);

        // Set the font size and color of the label (customize as needed)
        label.setFont(Font.font(16));
        label.setTextFill(Color.BLACK);

        circles.setOnMousePressed(circleOnMousePressedEventHandler);
        circles.setOnMouseDragged(circleOnMouseDraggedEventHandler);

        circles.setTranslateX(circleX);
        circles.setTranslateY(circleY);

        if (startState.equals(txt)) {
            //it is a starting state, put a triangle

            // Create a small triangle shape (arrow)
            double arrowSize = 30; // Adjust the size of the arrow as needed
            Polygon arrow = new Polygon(
                    0, 0,
                    -arrowSize, 0, // Left corner
                    -arrowSize / 2, -arrowSize // Top corner
            );
            arrow.setFill(Color.WHITE); // Set the fill color of the arrow
            arrow.setStroke(Color.BLACK); // Set the stroke color of the arrow

            // Position the arrow to the right of the circle
            arrow.setTranslateX((-circleRadius - arrowSize / 2)+0.75); // Position it to the right of the circle
            arrow.setTranslateY((-arrowSize / 2)+15); // Center the arrow vertically with the circle

            arrow.setRotate(90);

            // Add the arrow to the StackPane (circles) to display it along with the circle and label
            circles.getChildren().add(arrow);

        }

        // Add the circle to the container pane
        containerPane.getChildren().add(circles);

        // Adjust the circle's position for the next occurrence
        circleX += 200;

        circlesList.add(circles);
    }

    //make the transitions

    public void createTransitions(String transition) {

        String text = "";

        String[] elements = transition.split("\\s+");
        Point2D initStatePos = null;
        Point2D nextStatePos = null;
        StackPane initStateC = null;
        StackPane nextStateC = null;

        //the first three characters are
        //init state, input symbol or endmarkers, symbol to pop

        //the last three characters are
        //-1,0,1 (move), next state, symbol to push

            //assign init state to the circle with the same state name
            Optional<StackPane> initState = circlesList.stream()
                    .filter(circle -> {
                        // Get the label or circle inside the StackPane
                        Node innerNode = circle.getChildren().get(1);
                        String labelText = "";

                        // Check if the innerNode is a Label or a Circle
                        if (innerNode instanceof Label) {
                            labelText = ((Label) innerNode).getText();
                        } else if (innerNode instanceof Circle) {
                            labelText = ((Label) circle.getChildren().get(2)).getText();
                        }

                        // Check if the circle's label matches the target label
                        return labelText.equals(elements[0]);
                    })
                    .findFirst();

            if (initState.isPresent()) {
                StackPane circle = initState.get();
                // Get the local position of the StackPane within the AnchorPane
                double anchorX = circle.localToParent(circle.getBoundsInLocal()).getMaxX();
                double anchorY = circle.localToParent(circle.getBoundsInLocal()).getMaxY();
                System.out.println("Circle with label " + elements[0] + " found at (" + anchorX + ", " + anchorY + ")");
                initStatePos = new Point2D(anchorX, anchorY);
                initStateC = initState.get();
            } else {
                System.out.println("Circle with label " + elements[0] + " not found.");
            }

        //assign next state to the circle with the same state name
        Optional<StackPane> nextState = circlesList.stream()
                .filter(circle -> {
                    // Get the label or circle inside the StackPane
                    Node innerNode = circle.getChildren().get(1);
                    String labelText = "";

                    // Check if the innerNode is a Label or a Circle
                    if (innerNode instanceof Label) {
                        labelText = ((Label) innerNode).getText();
                    } else if (innerNode instanceof Circle) {
                        labelText = ((Label) circle.getChildren().get(2)).getText();
                    }

                    // Check if the circle's label matches the target label
                    return labelText.equals(elements[4]);
                })
                .findFirst();

        if (nextState.isPresent()) {
            StackPane circle = nextState.get();
            // Get the local position of the StackPane within the AnchorPane
            double anchorX = circle.localToParent(circle.getBoundsInLocal()).getMaxX();
            double anchorY = circle.localToParent(circle.getBoundsInLocal()).getMaxY();
            System.out.println("Circle with label " + elements[4] + " found at (" + anchorX + ", " + anchorY + ")");
            nextStatePos = new Point2D(anchorX, anchorY);
            nextStateC = nextState.get();
        } else {
            System.out.println("Circle with label " + elements[4] + " not found.");
        }

        text = elements[1] + ", " + elements[2] + "; " + elements[3] + ", " + elements[5];
        System.out.println(text);

        drawArrowBetweenCircles(initStatePos, nextStatePos, initStateC, nextStateC, text);
    }

    public void drawArrowBetweenCircles(Point2D start, Point2D end, StackPane startCircle, StackPane endCircle, String text) {


        if(start.getX() == end.getX() && start.getY() == end.getY()) {

            // Create a curved arrow when start state is equal to end state
            double controlX = (start.getX() + end.getX()) / 2;
            double controlY = (start.getY() + end.getY()) / 2;

            QuadCurve curvedArrow = new QuadCurve(start.getX()-50, start.getY()-20, controlX-30, controlY-30, end.getX()+50, end.getY()-20);

            // Customize the appearance of the curved arrow as needed
            curvedArrow.setStroke(Color.BLUE);
            curvedArrow.setStrokeWidth(3.0);

            // Add the curved arrow to the container pane
            containerPane.getChildren().add(curvedArrow);

        } else {
            Line arrowLine = new Line(start.getX(), start.getY(), end.getX(), end.getY());

            // Customize the appearance of the arrow line as needed
            arrowLine.setStroke(Color.GREEN);
            arrowLine.setStrokeWidth(3.0);

            containerPane.getChildren().add(arrowLine);
            arrowLine.toBack();

        }

        Label label = new Label(text);
        label.setFont(Font.font("Arial", 14));
        label.setTextFill(Color.BLACK);

        // Position the label on top of the arrow line
        double labelX = (start.getX() + end.getX()) / 2;
        double labelY = (start.getY() + end.getY()) / 2;

        // Adjust the label position to avoid overlapping with the arrowhead
        if (labelX < end.getX()) {
            labelX += 15;
        } else {
            labelX -= 15;
        }

        if (labelY < end.getY()) {
            labelY += 15;
        } else {
            labelY -= 15;
        }

        label.setLayoutX(labelX - label.getWidth() / 2);
        label.setLayoutY(labelY - label.getHeight() / 2);

        // Add the arrowhead and label to the container pane
        containerPane.getChildren().addAll(label);

    }

    // Mouse event handler for circle press
    public EventHandler<MouseEvent> circleOnMousePressedEventHandler = event -> {
        StackPane circles = (StackPane) event.getSource();
        circles.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
    };

    // Mouse event handler for circle drag
    public EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = event -> {
        StackPane circles = (StackPane) event.getSource();
        double[] userData = (double[]) circles.getUserData();
        double offsetX = event.getSceneX() - userData[0];
        double offsetY = event.getSceneY() - userData[1];
        circles.setTranslateX(circles.getTranslateX() + offsetX);
        circles.setTranslateY(circles.getTranslateY() + offsetY);
        circles.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
    };


}
