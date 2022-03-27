package com.example.loginappintercorp.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginappintercorp.R;
import com.example.loginappintercorp.model.Client;
import com.example.loginappintercorp.viewmodel.ClientViewModel;
import com.facebook.login.LoginManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEt;
    private EditText lastnameEt;
    private EditText ageEt;
    private EditText dateEt;

    private TextInputLayout nameTv;
    private TextInputLayout lastnameTv;
    private TextInputLayout ageTv;
    private TextInputLayout dateTv;

    private final ClientViewModel clientViewModel = new ClientViewModel();
    private FirebaseAuth mFirebaseAuth;
    private ImageButton btnImage;
    private DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initFirebase();
        initUser();
        initView();
        initDatePicker();
        initAction();
        initValidateTexts();
    }

    private void initView(){
        nameEt = findViewById(R.id.nameEt);
        lastnameEt = findViewById(R.id.lastnameEt);
        ageEt = findViewById(R.id.ageEt);
        dateEt = findViewById(R.id.datebirthEt);

        nameTv = findViewById(R.id.nameTv);
        lastnameTv = findViewById(R.id.lastnameTv);
        ageTv = findViewById(R.id.ageTv);
        dateTv = findViewById(R.id.datebirthTv);
        btnImage = findViewById(R.id.sharepic);
    }

    private void initAction(){
        Button btn = findViewById(R.id.registerBtn);
        Button btn_logout = findViewById(R.id.logoutBtn);
        btn.setOnClickListener(v->{
            if (validateText()){
                Client client = new Client(nameEt.getText().toString(), lastnameEt.getText().toString(), Integer.parseInt(ageEt.getText().toString()), dateEt.getText().toString());
                clientViewModel.add(client).addOnSuccessListener(success -> Toast.makeText(this, getResources().getString(R.string.client_created), Toast.LENGTH_SHORT).show()).addOnFailureListener(error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
        btn_logout.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            finish();
        });
        btnImage.setOnClickListener(v-> datePickerDialog.show());
    }

    private void initDatePicker(){
        @SuppressLint("SetTextI18n") DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> dateEt.setText(i2 + "/" + (i1+1) + "/" + i);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month  = cal.get(Calendar.MONTH);
        int day  = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
    }

    private boolean validateText(){
        try {
            if(TextUtils.isEmpty(nameEt.getText().toString())) {modifyHelperTextError(nameTv); return false;}
            if(TextUtils.isEmpty(lastnameEt.getText().toString())) {modifyHelperTextError(lastnameTv); return false;}
            if(TextUtils.isEmpty(ageEt.getText().toString())) {modifyHelperTextError(ageTv); return false;}
            if(Integer.parseInt(ageEt.getText().toString())<=0) {modifyHelperTextError(ageTv); return false;}
            if(TextUtils.isEmpty(dateEt.getText().toString())) {modifyHelperTextError(dateTv); return false;}
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void initValidateTexts(){
        nameEt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() <= 0)) {
                    modifyHelperTextError(nameTv);
                } else {
                    modifyHelperTextSuccess(nameTv);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() >= 0)  modifyHelperTextSuccess(nameTv);
            }

            public void afterTextChanged(Editable s) {
                if ((s.length() <= 0)) {
                    modifyHelperTextError(nameTv);
                } else {
                    modifyHelperTextSuccess(nameTv);
                }
            }
        });

        lastnameEt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() <= 0)) {
                    modifyHelperTextError(lastnameTv);
                } else {
                    modifyHelperTextSuccess(lastnameTv);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() >= 0)  modifyHelperTextSuccess(lastnameTv);
            }

            public void afterTextChanged(Editable s) {
                if ((s.length() <= 0)) {
                    modifyHelperTextError(lastnameTv);
                } else {
                    modifyHelperTextSuccess(lastnameTv);
                }
            }
        });

        ageEt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() <= 0)) {
                    modifyHelperTextError(ageTv);
                } else {
                    modifyHelperTextSuccess(ageTv);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() >= 0)  modifyHelperTextSuccess(ageTv);
            }

            public void afterTextChanged(Editable s) {
                if ((s.length() <= 0)) {
                    modifyHelperTextError(ageTv);
                } else {
                    modifyHelperTextSuccess(ageTv);
                }
            }
        });

        dateEt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() <= 0)) {
                    modifyHelperTextError(dateTv);
                } else {
                    modifyHelperTextSuccess(dateTv);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() >= 0)  modifyHelperTextSuccess(dateTv);
            }

            public void afterTextChanged(Editable s) {
                if ((s.length() <= 0)) {
                    modifyHelperTextError(dateTv);
                } else {
                    modifyHelperTextSuccess(dateTv);
                }
            }
        });
    }

    private void modifyHelperTextError(@NonNull TextInputLayout textInputLayout){
        textInputLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        textInputLayout.setHelperText(getResources().getString(R.string.invalid_text));
    }

    private void modifyHelperTextSuccess(@NonNull TextInputLayout textInputLayout){
        textInputLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        textInputLayout.setHelperText(getResources().getString(R.string.correct_text));
    }

    private void initFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    private void initUser(){
        try {
            FirebaseUser user = mFirebaseAuth.getCurrentUser();
            TextView lblUser = findViewById(R.id.lblUser);
            if(!TextUtils.isEmpty(user != null ? user.getDisplayName() : null)){
                lblUser.setText(user != null ? user.getDisplayName() : null);
            }else{
                lblUser.setText(user != null ? user.getPhoneNumber() : null);
            }
        }catch (Exception e){
            TextView lblWelcome = findViewById(R.id.lblWelcome);
            lblWelcome.setText("");
        }
    }
}
