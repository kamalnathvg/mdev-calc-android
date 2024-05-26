package com.example.calc.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class CalculatorModel {
    private String currentText = "0";

    private String calcOperators = "+-/X";//{"+" , "-" , "/", "X"};

    private String numbers = "0123456789";

    private Boolean enableDecimal = true;
    private ArrayList<String> operators = new ArrayList<String>();

    private ArrayList<String> numbersStringList = new ArrayList<String>();

    private ArrayList<Double> numbersList = new ArrayList<Double>();
    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public void addNumber(String newText){
        Log.d("logger", "currentText before appending : " + currentText);
        Log.d("logger", "newText before appending : " + newText);

        Double lastNumberDouble = 1.5;
        String lastNumberString = "0";


        if(!numbersList.isEmpty()){
            lastNumberDouble = numbersList.get(numbersList.size()-1 );
        }
        if(!numbersStringList.isEmpty()){
             lastNumberString = numbersStringList.get(numbersStringList.size()-1);
        }

        Log.d("logger", "lastNumberDouble: " + lastNumberDouble);
        Log.d("logger", "lastNumberString: " + lastNumberString);


        String lastChar = String.valueOf(currentText.charAt(currentText.length()-1));
        //ignore adding decimal points if there's already one
        if( lastNumberString.contains(".") && newText.equals(".") && operators.size() < numbersStringList.size()){
            Log.d("logger", ". already exists");
            updateOperatorsAndNumbers();
            return;
        }
        // remove leading zero when entering a number for first time
        if(currentText.equals("0") && numbers.contains(newText)){
            Log.d("logger","trying to add "+newText + " , removing leading 0s");
            currentText = newText;
            updateOperatorsAndNumbers();
            return;
        }
        //ignore multiple leading zeroes in the beginning
        if(currentText.equals("0") && newText.equals("0")){
            Log.d("logger","trying to add more zeroes, ignoring...");
            updateOperatorsAndNumbers();
            return;
        }
        //ignore inserting operators in the beginning
//        if(currentText.equals("0") && calcOperators.contains(newText)){
//            Log.d("logger", "trying to add operator in the beginning, ignoring...");
//            updateOperatorsAndNumbers();
//            return;
//        }
        //replace operator if the last character is operator and newText is an operator
        if(calcOperators.contains(lastChar) && calcOperators.contains(newText)){
            Log.d("logger","replacing with new operator...");
            currentText = currentText.substring(0,currentText.length() -1) + newText;
            updateOperatorsAndNumbers();
            return;
        }
        //if decimal point is pressed after an operator, add zero in front of it
        if(calcOperators.contains(lastChar) && newText.equals(".")){
            currentText = currentText + "0.";
            updateOperatorsAndNumbers();
            return;
        }
        //if operator is pressed and last char was a decimal point remove the decimal point
        if(lastChar.equals(".") && calcOperators.contains(newText)){
            Log.d("logger", "removing trailing decimal point");
            currentText = currentText.substring(0,currentText.length() -1 ) + newText;
            updateOperatorsAndNumbers();
            return;
        }
        //if last number is zero and a new operator is added, ignore the last number
//        if(numbersList.get(numbersList.size()-1) == 0.0 && calcOperators.contains(newText)){
//            Log.d("logger","last number is zero and a new operator is added ");
//            String lastOperator = operators.get(operators.size()-1);
//            int lastOperatorIndex = currentText.lastIndexOf(lastOperator);
//            currentText = currentText.substring(0,lastOperatorIndex) + newText;
//            updateOperatorsAndNumbers();
//            return;
//        }
        // ignore trailing zeroes after decimal point if an operator is pressed
        if(lastNumberDouble % 1 == 0 && lastNumberString.contains(".") && calcOperators.contains(newText)){
            Log.d("logger", "there are trailing zeroes, number is whole number and operator is pressed");
            if(!operators.isEmpty()){
                Log.d("logger","there are others operators in the list");
                String lastOperator = operators.get(operators.size()-1);
                int lastOperatorIndex = currentText.lastIndexOf(lastOperator);
                currentText = currentText.substring(0,lastOperatorIndex+1) + lastNumberString.split("\\.")[0] + newText;
                updateOperatorsAndNumbers();
                return;
            }
            Log.d("logger","there are no operators in the list");
            currentText = lastNumberString.split("\\.")[0] + newText;
            updateOperatorsAndNumbers();
            return;
        }

        currentText = currentText + newText;
        Log.d("logger", "added " + newText + " to " + currentText);
        updateOperatorsAndNumbers();

    }

    public void addOperator(String operatorText){

    }

    public void addDecimalPoint(){

    }


    public void clearLastString(){
        if(currentText.length() <= 1){
            currentText = "0";
            Log.d("logger","currentText is " + currentText );
            return;
        }
        Log.d("logger","removed : " + currentText.substring(currentText.length() -1));
        currentText = currentText.substring(0,currentText.length()-1);
        Log.d("logger","currentText : " + currentText);

    }
    public void clearAll(){
        currentText = "0";
        Log.d("logger","cleared everything");
        Log.d("logger","currentText : " + currentText);
    }
    public String calculate(){
        updateOperatorsAndNumbers();

        // remove trailing operator before calculation
        if(numbersList.size() == operators.size()){
            operators.remove(operators.size() -1 );
        }

        while(!operators.isEmpty()){
            Log.d("logger", "operators before first calculation" + operators);
            Log.d("logger", "operands before first calculation" + numbersList);
            int operatorIndex = 0;
            double result = 0;
            if(operators.contains("/")){
                Log.d("logger", "operation is division" + numbersList);
                operatorIndex = operators.indexOf("/");
                result = numbersList.get(operatorIndex) / numbersList.get(operatorIndex + 1);
                //do division
            } else if (operators.contains("X")) {
                Log.d("logger", "operation is multiplication" + numbersList);
               // do multiplication
                operatorIndex = operators.indexOf("X");
                result = numbersList.get(operatorIndex) * numbersList.get(operatorIndex + 1);

            } else if(operators.contains("+")){
                Log.d("logger", "operation is addition" + numbersList);
                //do addition
                operatorIndex = operators.indexOf("+");
                result = numbersList.get(operatorIndex) + numbersList.get(operatorIndex + 1);
            }
            else {
                Log.d("logger", "operation is subtraction" + numbersList);
               // do subtraction
                operatorIndex = operators.indexOf("-");
                result = numbersList.get(operatorIndex) - numbersList.get(operatorIndex + 1);
            }

            //remove the applied operator and replace the two operands with the result
            operators.remove(operatorIndex);
            numbersList.remove(operatorIndex);
            numbersList.set(operatorIndex,result);

            Log.d("logger", "result after first calculation" + result);

        }
        if (numbersList.size() == 1){
            double finalResult = numbersList.get(0);
            if(finalResult % 1 == 0){
                int finalInt = (int) finalResult;
                return String.valueOf(finalInt);
            }
            return String.valueOf(finalResult);
        }
        return "N/A";
    }

    public void updateOperatorsAndNumbers(){
        numbersList.clear();
        operators.clear();
        numbersStringList.clear();

        String[] numbersinString = currentText.split("[\\+\\-/X]");
        numbersStringList = new ArrayList<>(Arrays.asList(numbersinString));
        Log.d("logger","Numbers in String : " + numbersStringList);

        for (int i = 0;i < numbersinString.length; i++ ){
            double number = Double.parseDouble(numbersinString[i]);
            numbersList.add(number);
        }
        Log.d("logger","Numbers :" + numbersList);

        for(int i = 0; i < currentText.length(); i++ ){
                String currentString = String.valueOf(currentText.charAt(i));
                if(calcOperators.contains(currentString)){
                    operators.add(currentString);
                }
            }
        Log.d("logger","operators : " + operators);
        }

}
