package com.kumawat.frozer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kumawat.frozer.R;

import static android.content.ContentValues.TAG;

public class LoginPanel extends AppCompatActivity implements View.OnClickListener {

    private String email, password,repassword;
    private boolean login = true, forgot=false;
    private Button loginBtn;
    private EditText emET, passET, repassET;
    private TextInputLayout em, pass, repass;
    private TextView forgotPassTv, toggleLogin;
    private FirebaseAuth mAuth;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                login();
                break;
            case R.id.toggleLogin:
                forgotPassTv.setVisibility(View.VISIBLE);
                setToggleLogin();
                break;
            case R.id.forgotPass:
                forgot=true;
                pass.setVisibility(View.GONE);
                repass.setVisibility(View.GONE);
                loginBtn.setText("Send Reset Email.");
                forgotPassTv.setVisibility(View.GONE);
                forgotPass();
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_panel);
        emET = findViewById(R.id.enterEmail);
        passET = findViewById(R.id.EnterPass);
        repassET = findViewById(R.id.reEnterPass);
        em = findViewById(R.id.emailTextInputLayout);
        pass = findViewById(R.id.passwordTextInputLayout);
        repass = findViewById(R.id.confirmPasswordTextInputLayout);
        forgotPassTv = findViewById(R.id.forgotPass);
        toggleLogin = findViewById(R.id.toggleLogin);
        loginBtn = findViewById(R.id.loginButton);
        mAuth = FirebaseAuth.getInstance();
        forgotPassTv.setOnClickListener(this);
        toggleLogin.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }
    private void setToggleLogin(){
        login=!login;
        forgot=false;
        if(login){
            pass.setError(null);
            repass.setError(null);
            passET.setText(null);
            repassET.setText(null);
            pass.setVisibility(View.VISIBLE);
            repass.setVisibility(View.GONE);
            loginBtn.setText("Login");
            toggleLogin.setText("Don't Have an Account? Register here.");
        }else{
            pass.setError(null);
            repass.setError(null);
            passET.setText(null);
            repassET.setText(null);
            pass.setVisibility(View.VISIBLE);
            repass.setVisibility(View.VISIBLE);
            loginBtn.setText("Register");
            toggleLogin.setText("Have an Account? Login here.");
        }
    }
    public void login(){
        if (forgot){
            forgotPass();
        }
        else if (login) {
            email = emET.getText().toString();
            password = passET.getText().toString();
            boolean ok=true;
            if (!isValidMail(email)) {
                em.setError("Enter correct email.");
                ok=false;
            }
            if (password.length() < 6) {
                pass.setError("Password less than 6 characters");
                ok=false;
            }
            if(ok)
                loginUser();
        }else{
            register();
        }
    }
    public void loginUser(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginPanel.this, Dashboard.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPanel.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    public void register(){
        email = emET.getText().toString();
        password = passET.getText().toString();
        repassword = repassET.getText().toString();
        boolean ok=true;
        if (!isValidMail(email)) {
            em.setError("Enter correct email.");
            ok=false;
        }
        if (!password.equals(repassword)) {
            repass.setError("Password does not match.");
            ok=false;
        }
        if (password.length() < 6) {
            pass.setError("Password less than 6 characters");
            ok=false;
        }
        if(ok){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(LoginPanel.this, Dashboard.class));
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginPanel.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }
    private void forgotPass(){
        email=emET.getText().toString();
        if (!isValidMail(email))
            em.setError("Enter correct email.");
        else {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(LoginPanel.this, "Reset password email sent", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(LoginPanel.this, "Failed to send password reset mail", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: " + e.getMessage());
                }
            });
        }
    }
    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    @Override
    public void onBackPressed() {
        if (forgot){
            forgot=false;
            login=false;
            setToggleLogin();
        }else if (!login){
            setToggleLogin();
        }else
            super.onBackPressed();
    }
}
