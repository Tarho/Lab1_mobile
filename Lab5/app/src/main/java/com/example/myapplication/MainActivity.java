package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    MaterialButton buttonDivide, buttonMutiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        assignId(buttonC, R.id.buttons_c);
        assignId(buttonBrackOpen, R.id.buttons_open_brack);
        assignId(buttonBrackClose, R.id.buttons_close_brack);

        assignId(buttonDivide, R.id.buttons_divide);
        assignId(buttonMutiply, R.id.buttons_mutiplely);
        assignId(buttonPlus, R.id.buttons_sum);
        assignId(buttonMinus, R.id.buttons_minus);
        assignId(buttonEquals, R.id.buttons_equals);

        assignId(button0, R.id.buttons_0);
        assignId(button1, R.id.buttons_1);
        assignId(button2, R.id.buttons_2);
        assignId(button3, R.id.buttons_3);
        assignId(button4, R.id.buttons_4);
        assignId(button5, R.id.buttons_5);
        assignId(button6, R.id.buttons_6);
        assignId(button7, R.id.buttons_7);
        assignId(button8, R.id.buttons_8);
        assignId(button9, R.id.buttons_9);

        assignId(buttonAC, R.id.buttons_ac);
        assignId(buttonDot, R.id.buttons_dot);

    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        //solutionTv.setText(buttonText);
        String dataToCalculate = solutionTv.getText().toString();

        if (buttonText.equals("ac")) {
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }

        if (buttonText.equals(("="))) {
            solutionTv.setText(resultTv.getText());
            return;
        }

        if (buttonText.equals("c")) {
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
        } else {
            dataToCalculate = dataToCalculate + buttonText;
        }
        solutionTv.setText(dataToCalculate);

        String finalResult=getResult(dataToCalculate);
        if(!finalResult.equals("ERR")){
            resultTv.setText(finalResult);
        }
    }
    String getResult(String data){
        try{
            Context context=Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable=context.initStandardObjects();
            String finalResult= context.evaluateString(scriptable,data,"Javascript",1,null).toString();
            if(finalResult.endsWith(".0")){
                finalResult.replace(".0","");
            }
            return finalResult;
        }catch ( Exception e){
            return "ERR";
        }

    }
}