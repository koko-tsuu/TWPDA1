package com.example.twpda1.controllers;

import javafx.scene.layout.AnchorPane;

import java.util.*;

public class LogicController {

    private Stack<String> stk = new Stack<>();
    private TWPDAController twpdaController;
    public LogicController(TWPDAController twpdaController) {
        this.twpdaController = twpdaController;
    }

    private String startState;
    private Map<String, List<String>> transitions = new HashMap<>();


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

    public void isAccepted() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input string: ");

        String inputStr = scanner.nextLine();

        //read the input string
        System.out.println("This is the input:" + inputStr);

        stk.push(twpdaController.initPSymbol);
//        String newInputStr = twpdaController.leftEndmarker + inputStr;
//        newInputStr = newInputStr.concat(twpdaController.rightEndmarker);

        boolean isAccepted = TWPDAImp(inputStr, startState, stk, 0);

        System.out.println(isAccepted);
    }

    public boolean TWPDAImp(String inputStr, String currentState, Stack<String> stack, int inputPosition) {

        if (inputPosition == inputStr.length()) {
            // Base case: reached the end of the input string
            // Check if currentState is a final state and stack is empty
            // Return true if accepted, false otherwise
            if (twpdaController.finalStates.contains(currentState) && stack.isEmpty()) {
                return true; // Accepting computation path found
            }

            System.out.println("false");
            return false; // No accepting computation path found
        }

        char currentInput = inputStr.charAt(inputPosition);
        List<String> currentTransitions = transitions.get(currentState);
        List<String> matchingTransitions = findTransitionsForInput(currentTransitions, currentInput);
        List<String> lambdaTransitions = findTransitionsForInput(currentTransitions, 'Z');

        matchingTransitions.addAll(lambdaTransitions);

        System.out.println("curr state: " + currentState);
        System.out.println("curr stack: " + stack);
        System.out.println("curr head: " + currentInput);

        if(currentInput == '¢' || currentInput == '$') {
            int newInputPosition = inputPosition + 1;
            if(newInputPosition < inputStr.length()) {
                if (TWPDAImp(inputStr, currentState, stack, newInputPosition)) {
                    return true; // Accepting computation path found
                }
            }

        }

        for (String transition : matchingTransitions) {

            String[] parts = transition.split(" ");
            int headMovement = Integer.parseInt(parts[3]);
            String nextState = parts[4];
            String popSymbol = parts[2];
            String pushSymbol = parts[5];

            Stack<String> newStack = new Stack<>();
            newStack.addAll(stack);

            // Check for pop operation
            if (!popSymbol.equals("Z")) {
                if (!newStack.isEmpty() && newStack.peek().equals(popSymbol)) {
                    newStack.pop();
                    System.out.println("popped " + popSymbol);
                } else {
                    // Crash: Pop symbol mismatch, continue with next transition
                    continue;
                }
            }

            // Check for push operation
            if (!pushSymbol.equals("Z")) {
                newStack.push(pushSymbol);
                System.out.println("pushed " + pushSymbol);
            }

            // Move input head based on head movement
            int newInputPosition = inputPosition + headMovement;

            System.out.println("newinputpos: " + newInputPosition);

            // Make a recursive call with the updated state, stack, and input position
            if (TWPDAImp(inputStr, nextState, newStack, newInputPosition)) {
                return true; // Accepting computation path found
            }
        }
        return false;
    }

//    public boolean TWPDAImp(String inputStr) {
//
//        boolean isAccepted = false;
//
//        //get the start state
//        String currentState = startState;
//        stk.push(twpdaController.initPSymbol);
//        String newInputStr = twpdaController.leftEndmarker + inputStr;
//        newInputStr = newInputStr.concat(twpdaController.rightEndmarker);
//
//        for(int i = 0; i < newInputStr.length(); i++) {
//            char currentInput = newInputStr.charAt(i);
//            List<String> currentTransitions = transitions.get(currentState);
//            List<String> matchingTransitions;
//
//            System.out.println("curr state: " + currentState);
//            System.out.println("curr stack: " + stk);
//            System.out.println("curr head: " + currentInput);
//
//            //find the transition that accepts currentInput as an input
//            matchingTransitions = findTransitionsForInput(currentTransitions, currentInput);
//
//            if(matchingTransitions.size() == 1) {
//                //one timeline
//
//                System.out.println("one timeline");
//
//                String[] parts = matchingTransitions.get(0).split(" ");
//
//                System.out.println(matchingTransitions.get(0));
//
//                //set currentState as nextState
//                currentState = parts[4];
//
//                System.out.println("curr state in: " + parts[4]);
//                System.out.println("pop symbol: " + parts[2]);
//                System.out.println("push symbol: " + parts[5]);
//
//                //check for pop
//                if(!parts[2].equals("Z")) {
//                    //not lambda, check if pop
//                    if(!stk.isEmpty() && stk.peek().equals(parts[2])) {
//                        //pop that character
//                        stk.pop();
//                        System.out.println("popped " + parts[2]);
//                    } else {
//                        //crash bc no popping of that character
//                        return isAccepted;
//                    }
//                }
//
//                //check for push
//                if(!parts[5].equals("Z")) {
//                    //push if not lambda
//                    stk.push(parts[5]);
//                    System.out.println("pushed " + parts[5]);
//                }
//
//                //move input string
//
//                if(parts[3].equals("1")) {
//                    //move right
//                    i += 0;
//                    System.out.println("right");
//                } else if(parts[3].equals("-1")) {
//                    //move left
//                    i -= 2;
//                    System.out.println("left");
//                } else if(parts[3].equals("0")) {
//                    //stay put
//                    i--;
//                    System.out.println("stay put");
//                }
//
//
//            } else if(matchingTransitions.size() > 1) {
//                //multiple timelines
//
//            } else {
//                //crash
//                return isAccepted;
//            }
//        }
//
//        System.out.println("final stack: " + stk);
//        System.out.println("final state: " + currentState);
//        //if at final state and if stack is empty
//        if(twpdaController.finalStates.contains(currentState) && stk.isEmpty()) {
//            isAccepted = true;
//        }
//
//        return isAccepted;
//    }

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
