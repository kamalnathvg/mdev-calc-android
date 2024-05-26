package com.example.calc;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calc.models.CalculatorModel;

public class MainActivity extends AppCompatActivity {
    CalculatorModel calculatorModel = new CalculatorModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //OnClick Implementation for Numbers and Decimal
    public void onNumberClick(android.view.View view){

        TextView textView = findViewById(R.id.resultText);
        Button numButton = (Button) view;
        String clickedText = numButton.getText().toString();

        Log.d("logger", clickedText + "Clicked!");

        calculatorModel.addNumber(clickedText);
        textView.setText(calculatorModel.getCurrentText());

    }

    public void onOperatorClick(android.view.View view){
        TextView textView = findViewById(R.id.resultText);
        Button operatorButton = (Button) view;
        String selectedOperator = operatorButton.getText().toString();

        Log.d("logger", "Operator clicked : " + selectedOperator);



    }

    public void onDecimalClick(android.view.View view){

    }


    //onClick Implementation for clearButton
    public void onClearClick(android.view.View view){
        calculatorModel.clearLastString();

        TextView textView = findViewById(R.id.resultText);
        textView.setText(calculatorModel.getCurrentText());
    }

    public void onCalculateClick(android.view.View view){

        String result = calculatorModel.calculate();

        TextView textView = findViewById(R.id.resultText);
        textView.setText(result);

    }

    public void onAllClearClick(android.view.View view){
        calculatorModel.clearAll();
        TextView textView = findViewById(R.id.resultText);
        textView.setText(calculatorModel.getCurrentText());

    }
}