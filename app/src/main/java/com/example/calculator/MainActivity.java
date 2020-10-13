package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
{
    private int[] numericButtons = {R.id.btn_Seven, R.id.btn_Eight, R.id.btn_Nine,
                                    R.id.btn_Four, R.id.btn_Five, R.id.btn_Six,
                                    R.id.btn_Zero, R.id.btn_One, R.id.btn_Two, R.id.btn_Three};

    private int[] operatorButtons = {R.id.btn_Divide, R.id.btn_Multiply, R.id.btn_Minus, R.id.btn_Add};

    private int[] otherButtons = {R.id.btn_ClearEntry, R.id.btn_Clear, R.id.btn_BS, R.id.btn_Dot, R.id.btn_Pos_Neg, R.id.btn_Equal};
    private TextView tv_userInput, tv_userResult;

    private boolean lastNumeric;
    private boolean stateError;
    private boolean lastDot;
    Operator multiply = new Operator("x", 2, true, Operator.PRECEDENCE_POWER + 1) {
        @Override
        public double apply(double... args) {
            final int arg = (int) args[0];
            if ((double) arg != args[0]) {
                throw new IllegalArgumentException("Operand for factorial has to be an integer");
            }
            if (arg < 0) {
                throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
            }
            double result;
            result = args[0] + args[1];
            return result;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_userInput = (TextView)findViewById(R.id.tv_userInput);
        tv_userResult = (TextView)findViewById(R.id.tv_userResult);

        setNumericOnClickListener();
        setOperatorOnClickListener();
    }

    private void setNumericOnClickListener()
    {
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button button = (Button) v;
                if (stateError)
                {
                    tv_userInput.setText(button.getText());
                    stateError = false;
                }
                else
                {
                    tv_userInput.append(button.getText());
                }
                lastNumeric = true;
            }
        };
        for (int id : numericButtons)
        {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorOnClickListener()
    {
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (lastNumeric && !stateError)
                {
                    Button button = (Button) v;
                    tv_userInput.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        };
        for (int id : operatorButtons)
        {
            findViewById(id).setOnClickListener(listener);
        }

        findViewById(R.id.btn_ClearEntry).setOnClickListener
        (
            new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    tv_userInput.setText("");
                    tv_userResult.setText("");
                    lastNumeric = false;
                    stateError = false;
                    lastDot = false;
                }
            }
        );

        findViewById(R.id.btn_Clear).setOnClickListener
        (
            new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    tv_userInput.setText("");
                    lastNumeric = false;
                    stateError = false;
                    lastDot = false;
                }
            }
        );

        findViewById(R.id.btn_BS).setOnClickListener
        (
            new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    char last = 0;
                    if (tv_userInput.getText() != null && tv_userInput.getText().length() > 0)
                    {
                        last = tv_userInput.getText().charAt(tv_userInput.getText().length() - 1);
                        tv_userInput.setText(tv_userInput.getText().subSequence(0, tv_userInput.getText().length() - 1));
                    }

                    if (last == '+' || last == '-' || last == 'X' || last == '/')
                    {
                        lastNumeric = true;
                    }
                    lastNumeric = false;
                    stateError = false;
                    lastDot = false;
                }
            }
        );

        findViewById(R.id.btn_Equal).setOnClickListener
        (
            new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onEqual();
                }
            }
        );
    }

    private void onEqual()
    {
        if (lastNumeric && !stateError)
        {
            String txt = tv_userInput.getText().toString();
            Expression expression = new ExpressionBuilder(txt).build();
            try
            {
                int result = (int) expression.evaluate();
                tv_userResult.setText(Integer.toString(result));
                lastDot = true;
            }
            catch (ArithmeticException ex)
            {
                tv_userInput.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}