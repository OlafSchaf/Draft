package com.example.iadraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class CreateAccount extends AppCompatActivity
{

    private EditText userName, userPassword, userEmail;
    private Button  registerButton;


    private Button userLogin;
    private FirebaseAuth DataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setupUIViews();

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccount.this, MainPage.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(validate())
                {
                    String email_verification= userEmail.getText().toString().trim();
                    String password_verification= userPassword.getText().toString().trim();
                    DataBase.createUserWithEmailAndPassword(email_verification, password_verification).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(CreateAccount.this , "Registration Complete", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateAccount.this, MainPage.class));
                                }
                               else
                                {

                                    Toast.makeText(CreateAccount.this , "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }
            }
        });
    }
    private void setupUIViews()
    {
        userName = (EditText) findViewById(R.id.etName);
        userPassword = (EditText) findViewById(R.id.etPassword);
        userLogin = (Button) findViewById(R.id.NewLogin);
        userEmail = (EditText) findViewById(R.id.etEmail);
        registerButton= (Button) findViewById(R.id.registerButton);
        DataBase = FirebaseAuth.getInstance();
    }

    private boolean validate()
    {
        boolean checking = false;
        String username = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password =  userPassword.getText().toString();


        if(username.isEmpty()&& password.isEmpty()&& email.isEmpty())
        {
            Toast.makeText(this, "Enter information", Toast.LENGTH_SHORT).show();
        }
        else
        {
            checking = true;
        }
        return checking;
    }
}
