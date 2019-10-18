package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.letstalk.CountyCode.CountryData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnterContactNumberActivity extends AppCompatActivity {

    @BindView(R.id.btnContinue) Button btnContinue;

    private static final String TAG = "EnterContactNumberActiv";

    private Spinner countryCode;

    @BindView(R.id.entermobile) EditText txtmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_contact_number);

        countryCode =findViewById(R.id.spinnerCountries);
        countryCode.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countriesNames));

        //txtmobile = findViewById(R.id.entermobile);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnContinue)
    public void getOtp(){
        String code= CountryData.countriesAreaCode[countryCode.getSelectedItemPosition()];

        String number = txtmobile.getText().toString().trim();

        if(number.isEmpty() || number.length()<10){
            txtmobile.setError("Valid Number is Required.");
            txtmobile.requestFocus();
            return;
        }

        String phoneNumber = "+" + code + number;

        Log.d(TAG, "getOtp: "+phoneNumber);

        Intent intent = new Intent(this,PhoneVerification.class);
        intent.putExtra("phonenumber",phoneNumber);
        startActivity(intent);
    }
}
