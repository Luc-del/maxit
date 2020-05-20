package com.example.maxit;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class HorizontalNumberPicker extends LinearLayout {
    private EditText value;
    private int min, max;

    public HorizontalNumberPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.horizontal_numberpicker, this);

        value = findViewById(R.id.number);
        value.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int newValue = getValue();
                    if (newValue < min) value.setText(String.valueOf(min));
                    else if (newValue > max) value.setText(String.valueOf(max));
                }
            }
        });

// AS IS : not working if we delete all text and reacts instantly
//        value.addTextChangedListener(new TextWatcher() {
//            boolean ready = true;
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (value.hasFocus() && ready) {
//                    ready = false;
//                    int input = Integer.parseInt(s.toString());
//                    Log.d("try", ""+input);
//                    if (input < min) input = min;
//                    else if (input > max) input = max;
//
//                    s.clear();
//                    s.append(Integer.toString(input));
//                    ready = true;
//                }
//            }
//        });

        final Button add = findViewById(R.id.add);
        add.setOnClickListener(new AddHandler(1));

        final Button retreive = findViewById(R.id.retreive);
        retreive.setOnClickListener(new AddHandler(-1));
    }

    private class AddHandler implements OnClickListener {
        final int diff;

        public AddHandler(int diff) {
            this.diff = diff;
        }

        @Override
        public void onClick(View v) {
            int newValue = getValue() + diff;
            if (newValue < min) {
                newValue = min;
            } else if (newValue > max) {
                newValue = max;
            }
            value.setText(String.valueOf(newValue));
        }
    }


    public int getValue() {
        if (value != null) {
            try {
                final String number = value.getText().toString();
                return Integer.parseInt(number);
            } catch (NumberFormatException ex) {
            }
        }
        return 0;
    }

    public void setValue(final int number) {
        if (value != null) {
            value.setText(String.valueOf(number));
        }
    }

    public int getMinValue() {
        return min;
    }

    public void setMinValue(int min) {
        this.min = min;
    }

    public int getMaxValue() {
        return max;
    }

    public void setMaxValue(int max) {
        this.max = max;
    }
}