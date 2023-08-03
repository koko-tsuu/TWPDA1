package com.example.twpda1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.*;

public class LogicController {

    private Stack<String> stk = new Stack<>();
    private TWPDAController twpdaController;
    public LogicController(TWPDAController twpdaController, Text stateTxt, Text state, Text stackTxt, Text stack, TextFlow stringTxt, Text string,
                           Text verdict, Text verdictTxt, Button Step, Button Reset, Text readInput, Text notReadInput) {
        this.twpdaController = twpdaController;
        this.stateTxt = stateTxt;
        this.state = state;
        this.stackTxt = stackTxt;
        this.stack = stack;
        this.string = string;
        this.stringTxt = stringTxt;
        this.verdict = verdict;
        this.verdictTxt = verdictTxt;
        this.Step = Step;
        this.Reset = Reset;
        this.readInput = readInput;
        this.notReadInput = notReadInput;
    }

    private String startState;
    private Map<String, List<String>> transitions = new HashMap<>();
    private boolean isAccepted;

    private int counter;

    private String inputTape;

    private String currentState;

    @FXML
    private Text stateTxt;

    @FXML
    private Text readInput;

    @FXML
    private Text notReadInput;

    @FXML
    private Text state;

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

    @FXML
    private Button Step;

    @FXML
    private Button Reset;


    //get the input string
    public void initializeTWPDA() {
        startState = twpdaController.startState;

        //get the transitions for each state

        for (int i = 0; i < twpdaController.numOfStates; i++) {
            //get the transition

            List<String> currTransitions = new ArrayList<>();

            String stateName = twpdaController.states.get(i);

            for (String transition: twpdaController.transitions) {
                String[] parts = transition.split(" ");

                // Get the first substring (before the space)
                String firstSubstring = parts[0];

                //find stateName that equals to the first substring before a space in the string
                //and add that element

                if(firstSubstring.equals(stateName)) {
                    currTransitions.add(transition);
                }
            }

            transitions.put(stateName, currTransitions);
        }

    }

    public void isAccepted(String inputStr) {

        stk.clear();
        stk.push(twpdaController.initPSymbol);
        String newInputStr = twpdaController.leftEndmarker + inputStr;
        newInputStr = newInputStr.concat(twpdaController.rightEndmarker);
        inputTape = newInputStr;

        stack.setVisible(true);
        string.setVisible(true);
        state.setVisible(true);
        stackTxt.setVisible(true);
        stringTxt.setVisible(true);
        stateTxt.setVisible(true);
        readInput.setVisible(true);
        notReadInput.setVisible(true);
        Step.setDisable(false);
        verdict.setVisible(true);
        verdictTxt.setVisible(true);
        verdictTxt.setText("");

        TWPDAImp();

    }


    public void setText() {
        stateTxt.setText(currentState);
        stackTxt.setText(String.valueOf(stk));
        readInput.setText(inputTape.substring(0, counter));
        notReadInput.setText(inputTape.substring(counter));
        readInput.setFont(Font.font("System", FontWeight.BOLD, 14));
    }

    public void TWPDAImp() {

        //get the start state
        currentState = startState;
        counter = 0;

        setText();

        //when next button is pressed
        //call the implementation function
        Step.setOnAction(this::onStepButtonClick);

        //when reset button is pressed
        //call the imp function from the start
        Reset.setOnAction(this::onResetButtonClick);

    }

    public void onStepButtonClick(ActionEvent event) {

        stack.setVisible(true);
        string.setVisible(true);
        state.setVisible(true);
        stackTxt.setVisible(true);
        stringTxt.setVisible(true);
        stateTxt.setVisible(true);
        readInput.setVisible(true);
        notReadInput.setVisible(true);

        if(counter < inputTape.length()) {
            TWPDA();
        } else {
            checkVerdict();
            Step.setDisable(true);
        }
    }

    public void checkVerdict() {

        //if at final state and if stack is empty

        if(twpdaController.finalStates.contains(currentState) && stk.isEmpty()) {
            isAccepted = true;
            setText();
        }

        verdict.setVisible(true);
        verdictTxt.setVisible(true);
        verdictTxt.setText(String.valueOf(isAccepted).toUpperCase());

        if(isAccepted) {
            verdictTxt.setFill(Color.GREEN);
            verdictTxt.setFont(Font.font("System", FontWeight.BOLD, 14));
        } else {
            verdictTxt.setFill(Color.RED);
            verdictTxt.setFont(Font.font("System", FontWeight.BOLD, 14));
        }

        System.out.println(isAccepted);

        stk.clear();
    }

    public void onResetButtonClick(ActionEvent event) {
        currentState = startState;
        stk.clear();
        counter = 0;
        Step.setDisable(false);
        verdictTxt.setVisible(false);
        stateTxt.setVisible(false);
        stackTxt.setVisible(false);
        stringTxt.setVisible(false);
        stk.push(twpdaController.initPSymbol);
    }


    public void TWPDA() {

        char currentInput = inputTape.charAt(counter);
        List<String> currentTransitions = transitions.get(currentState);
        List<String> matchingTransitions;
        boolean hasSet = false;

        //find the transition that accepts currentInput as an input
        matchingTransitions = findTransitionsForInput(currentTransitions, currentInput);

        if(matchingTransitions.size() == 1) {
            //one timeline

            String[] parts = matchingTransitions.get(0).split(" ");

            System.out.println(matchingTransitions.get(0));

            String headMovement = parts[3];
            String nextState = parts[4];
            String popSymbol = parts[2];
            String pushSymbol = parts[5];

            //set currentState as nextState
            currentState = nextState;

            //check for pop
            if(!popSymbol.equals("λ")) {
                //not lambda, check if pop
                if(!stk.isEmpty() && stk.peek().equals(popSymbol)) {
                    //pop that character
                    stk.pop();
                    System.out.println("popped " + popSymbol);
                } else {
                    //crash
                    if(!hasSet) {
                        setText();
                        hasSet = true;
                    }
                    isAccepted = false;
                    counter = inputTape.length();
                }
            }

            //check for push
            if(!pushSymbol.equals("λ")) {
                //push if not lambda
                stk.push(pushSymbol);
                System.out.println("pushed " + pushSymbol);
            }

            //move input string

            if(headMovement.equals("1")) {
                //move right
                counter += Integer.parseInt(headMovement);
                System.out.println("right");
            } else if(headMovement.equals("-1")) {
                //move left
                counter += Integer.parseInt(headMovement);
                System.out.println("left");
            } else if(headMovement.equals("0")) {
                //stay put
                counter += Integer.parseInt(headMovement);
                System.out.println("stay put");
            }


        } else if(matchingTransitions.size() > 1) {
            //multiple timelines

        } else {
            if(!hasSet) {
                setText();
                hasSet = true;
            }
            isAccepted = false;
            counter = inputTape.length();
        }

        if(!hasSet) {
            setText();
        }

    }

    public List<String> findTransitionsForInput(List<String> currentTransitions, char currentInput) {
        List<String> matchingTransitions = new ArrayList<>();

        for (String transition : currentTransitions) {
            String[] parts = transition.split(" ");
            if (parts.length >= 2 && parts[1].charAt(0) == currentInput) {
                matchingTransitions.add(transition);
            }
        }

        return matchingTransitions;
    }

}
