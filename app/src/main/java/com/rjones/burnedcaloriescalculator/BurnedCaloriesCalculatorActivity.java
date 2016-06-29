package com.rjones.burnedcaloriescalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class BurnedCaloriesCalculatorActivity extends AppCompatActivity
        implements OnEditorActionListener, OnSeekBarChangeListener, OnItemSelectedListener, OnKeyListener {

    //define variables for widgets
    private EditText weightEditText;
    private TextView milesRanTextView;
    private SeekBar milesRanSeekBar;
    private TextView caloriesBurnedTextView;
    private Spinner feetSpinner;
    private Spinner inchesSpinner;
    private TextView bmiTextView;
    private EditText nameEditText;

    //define SharedPreferences object
    private SharedPreferences savedValues;

    //define instance variables
    private String weightString;
    private String nameString;
    private int feet_spin = 1;
    private int inches_spin = 1;
    private int milesRan = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        // get references to widgets
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        milesRanTextView = (TextView) findViewById(R.id.milesRanTextView);
        milesRanSeekBar = (SeekBar) findViewById(R.id.milesRanSeekBar);
        caloriesBurnedTextView = (TextView) findViewById(R.id.caloriesBurnedTextView);
        feetSpinner = (Spinner) findViewById(R.id.feetSpinner);
        inchesSpinner = (Spinner) findViewById(R.id.inchesSpinner);
        bmiTextView = (TextView) findViewById(R.id.bmiTextView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);

        //set array adapter for feet spinner
        ArrayAdapter<CharSequence> adapterFeet = ArrayAdapter.createFromResource(
                this, R.array.feet_array, android.R.layout.simple_spinner_item);
        adapterFeet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feetSpinner.setAdapter(adapterFeet);

        //set array adapter for feet spinner
        ArrayAdapter<CharSequence> adapterInches = ArrayAdapter.createFromResource(
                this, R.array.inches_array, android.R.layout.simple_spinner_item);
        adapterInches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchesSpinner.setAdapter(adapterInches);

        // set listeners
        weightEditText.setOnEditorActionListener(this);
        weightEditText.setOnKeyListener(this);
        milesRanSeekBar.setOnSeekBarChangeListener(this);
        milesRanSeekBar.setOnKeyListener(this);
        feetSpinner.setOnItemSelectedListener(this);
        inchesSpinner.setOnItemSelectedListener(this);

        //get SharedPreferences object
        savedValues = getSharedPreferences("SaveValues", MODE_PRIVATE);
    } // END onCreate method

    public void calculateAndDisplay(){
        //get Weight Amount
        weightString = weightEditText.getText().toString();
        float weight;
        if(weightString.equals("")){
            weight = 0;
        }else{
            weight = Float.parseFloat(weightString);
        }

        //get miles ran
        milesRan = milesRanSeekBar.getProgress();

        // get spinner feet: index
        int feet = feetSpinner.getSelectedItemPosition();

        // get spinner inches: index
        int inches = inchesSpinner.getSelectedItemPosition();

        /*
            caloriesBurned = 0.75 * weight * milesRan;
            bmi = (weight * 703) / ((12 * feet + inches) * (12 * feet + inches));
         */

        //calculate calories burned
            double caloriesBurned = 0.75 * weight * milesRan;
            DecimalFormat decimal = new DecimalFormat("#.00");
            caloriesBurnedTextView.setText(decimal.format(caloriesBurned));

        //calculate bmi
            double bmi = (weight * 703) / ((12 * feet + inches) * (12 * feet + inches));
            bmiTextView.setText(decimal.format(bmi));
    }

    //========================================================
    // SharedPreferences
    //========================================================


    @Override
    protected void onPause() {
        //save the instance variables
        Editor editor = savedValues.edit();
        editor.putString("weightString",weightString);
        editor.putInt("milesRan",milesRan);
//        editor.putInt("feet_spin",feet_spin);
//        editor.putInt("inches_spin",inches_spin);
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //get the instance variables
        weightString = savedValues.getString("weightString","");
        milesRan = savedValues.getInt("milesRan",1);

        //set weight amount
        weightEditText.setText(weightString);

//        //set miles ran
//        milesRanSeekBar.setProgress(milesRan);
//        milesRanTextView.setText(milesRan);

        //set feet
        feetSpinner.setSelection(feet_spin);
        //set inches
        inchesSpinner.setSelection(inches_spin);
    }

    //*****************************************************
    // Event handler for the EditText
    //*****************************************************
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    //*****************************************************
    // Event handler for the SeekBar
    //*****************************************************
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        milesRanTextView.setText(progress + "");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        calculateAndDisplay();
    }


    //*****************************************************
    // Event handler for the Spinner
    //*****************************************************
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        feet_spin = position + 1;
        inches_spin = position + 1;
        calculateAndDisplay();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    //*****************************************************
    // Event handler for the keyboard and DPad
    //*****************************************************
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        switch (keyCode) {

            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                calculateAndDisplay();
                imm.hideSoftInputFromWindow(
                        weightEditText.getWindowToken(), 0);
                return true;  // consume the event

            case KeyEvent.KEYCODE_DPAD_DOWN:
                calculateAndDisplay();
                imm.hideSoftInputFromWindow(
                        weightEditText.getWindowToken(), 0);
                break;  // don't consume the event

            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (v.getId() == R.id.milesRanSeekBar) {
                    calculateAndDisplay();
                }
                break;  // don't consume the event
        }
        return false;  // don't consume the event
    }
}
