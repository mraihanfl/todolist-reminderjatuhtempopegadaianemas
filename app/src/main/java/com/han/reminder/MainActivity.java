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

public class MainActivity extends AppCompatActivity {
    TextView reg, lupaPassword;
    EditText inputEmail, inputPassword;
    String email, password;
    Button btnlogin;
    boolean passwordVisible;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        btnlogin = findViewById(R.id.buttonLogin);


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


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ceklogin();
            }
        });


        reg = findViewById(R.id.textViewBuatAkun);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        lupaPassword = findViewById(R.id.textViewLupaPassword);
        lupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });


    }

    private void ceklogin() {
        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();

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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Login Sukses", Toast.LENGTH_LONG).show();
                            Intent Intent_Home = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(Intent_Home);




                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Login Gagal, Cek Email dan Password", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });

    }

}