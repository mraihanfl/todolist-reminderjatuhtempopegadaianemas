package com.han.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextView log;
    EditText inputEmail, inputPassword, inputConfirmPassword;
    String email, password, password2;
    Button btndaftar;
    boolean passwordVisible;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        inputConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btndaftar = findViewById(R.id.buttonRegister);


        inputPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2 ;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=inputPassword.getRight()-inputPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = inputPassword.getSelectionEnd();
                        if(passwordVisible){
                            //set drawable image here
                            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gambarpassword,0,R.drawable.gambar_mata_vissible_off,0);
                            //for hide password
                            inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }else {
                            //set drawable image here
                            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gambarpassword,0,R.drawable.gambar_mata_vissible,0);
                            //for show password
                            inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        inputPassword.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });

        inputConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2 ;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=inputConfirmPassword.getRight()-inputConfirmPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = inputConfirmPassword.getSelectionEnd();
                        if(passwordVisible){
                            //set drawable image here
                            inputConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gambarpassword,0,R.drawable.gambar_mata_vissible_off,0);
                            //for hide password
                            inputConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }else {
                            //set drawable image here
                            inputConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gambarpassword,0,R.drawable.gambar_mata_vissible,0);
                            //for show password
                            inputConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        inputConfirmPassword.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });


        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrasi();
            }
        });


        log = (TextView)findViewById(R.id.textViewSudahPunyaAkun);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

    }

    private void registrasi() {
        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
        password2 = inputConfirmPassword.getText().toString();


        if(email.isEmpty()){
            inputEmail.setError("Email is required!");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Please provide valid email!");
            inputEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            inputPassword.setError("Password is required!");
            inputPassword.requestFocus();
            return;
        }

        if(password2.isEmpty()){
            inputConfirmPassword.setError("Confirm Password is required!");
            inputConfirmPassword.requestFocus();
            return;
        }

        if(!password.equals(password2)){
            inputConfirmPassword.setError("Password Not matching");
            inputConfirmPassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Registrasi Gagal", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });

    }

}