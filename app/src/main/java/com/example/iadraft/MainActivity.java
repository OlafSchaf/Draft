package com.example.iadraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    private EditText Username;
    private EditText Pass;
    private TextView Info;
    private Button Login;
    private int counter = 3;
    private Button NewProfile;
    private FirebaseAuth databaseCheck;
    private ProgressDialog waitText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Username = (EditText) findViewById(R.id.LoginName);
        Pass = (EditText) findViewById(R.id.New);
        Login = (Button) findViewById(R.id.loginButton);
        NewProfile = (Button)findViewById(R.id.NewAccountButton);
        databaseCheck = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        waitText = new ProgressDialog(this);

        if (user != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this, MainPage.class));

        }

        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                 validate(Username.getText().toString(), Pass.getText().toString());
            }
        });
        NewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent newacc = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(newacc);
            }
        });

    }
    private void validate(String userUsername, String userPass)
    {

        waitText.setMessage("Please wait while we verify your login information");
        waitText.show();
        databaseCheck.signInWithEmailAndPassword(userUsername, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    waitText.dismiss();
                    Toast.makeText(MainActivity.this, "Login Sucessfull", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MainPage.class));
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Failed to login please try again", Toast.LENGTH_SHORT).show();
                    waitText.dismiss();
                }
            }
        });

    }
}