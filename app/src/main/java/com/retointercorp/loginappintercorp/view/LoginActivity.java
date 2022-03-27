package com.retointercorp.loginappintercorp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.retointercorp.loginappintercorp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private LoginButton loginButton;

    private EditText countryEt;
    private EditText phoneEt;

    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private EditText otpEt;
    private LinearLayout layoutOtp;
    private TextView lblOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initFirebase();
        initFacebook();
        initPhoneAuth();
    }

    private void handleFacebookToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            mFirebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void initFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    private void initFacebook(){
        loginButton.setPermissions(getResources().getString(R.string.email_permission_facebook), getResources().getString(R.string.public_permission_facebook));
        CallbackManager mCallbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_cancel), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_SHORT).show();
            }
        });
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user!=null){
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        };
    }

    private void initViews(){
        loginButton = findViewById(R.id.login_button);
        countryEt = findViewById(R.id.countryEt);
        phoneEt = findViewById(R.id.phoneEt);
        lblOtp = findViewById(R.id.lblOtp);

        countryEt.setOnFocusChangeListener((view, b) -> {
            if (b) {
                countryEt.setHint("+51");
            } else {
                countryEt.setHint("");
            }
        });

        phoneEt.setOnFocusChangeListener((view, b) -> {
            if (b) {
                phoneEt.setHint("952568462");
            } else {
                phoneEt.setHint("");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initPhoneAuth(){
        Button nextStepBtn = findViewById(R.id.nextStepBtn);
        Button verifyCode = findViewById(R.id.verifyCode);
        TextView resendCode = findViewById(R.id.resendCode);
        layoutOtp = findViewById(R.id.layoutOtp);
        otpEt = findViewById(R.id.otpEt);

        mCallbacks  = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_cancel), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                mForceResendingToken = forceResendingToken;
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.message_code_success), Toast.LENGTH_SHORT).show();
            }
        };

        nextStepBtn.setOnClickListener(view -> {
            String phone = countryEt.getText().toString() + phoneEt.getText().toString();
            if(TextUtils.isEmpty(countryEt.getText().toString()) || TextUtils.isEmpty(phoneEt.getText().toString())){
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.message_alert_phone), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.message_send), Toast.LENGTH_LONG).show();
                lblOtp.setText(getResources().getString(R.string.label_info_otp)+" "+phone);
                layoutOtp.setVisibility(View.VISIBLE);
                startPhoneVerification(phone);
            }
        });

        verifyCode.setOnClickListener(view -> {
            String otp = otpEt.getText().toString();
            if(TextUtils.isEmpty(otp)){
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.message_alert_otp), Toast.LENGTH_SHORT).show();
            }else{
                verifyCode(mVerificationId, otp);
            }
        });

        resendCode.setOnClickListener(view -> {
            String phone = countryEt.getText().toString() + phoneEt.getText().toString();
            if(TextUtils.isEmpty(countryEt.getText().toString()) || TextUtils.isEmpty(phoneEt.getText().toString())){
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.message_alert_phone), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.message_send), Toast.LENGTH_LONG).show();
                lblOtp.setText(getResources().getString(R.string.label_info_otp)+" "+phone);
                resendCode(phone, mForceResendingToken);
            }

        });

    }

    private void startPhoneVerification(String phone){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendCode(String phone, PhoneAuthProvider.ForceResendingToken token){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(token)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String mVerificationId, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneCredential(credential);
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    String phone = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getPhoneNumber();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.message_phone_successful)+" "+ phone, Toast.LENGTH_SHORT).show();
                    layoutOtp.setVisibility(View.GONE);
                    resetText();
                })
                .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_cancel), Toast.LENGTH_SHORT).show());
    }

    private void resetText(){
        countryEt.setText("");
        phoneEt.setText("");
        otpEt.setText("");
        lblOtp.setText("");
    }
}