package com.example.instargram;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ResetPasswordActivity extends AppCompatActivity {

    MaterialEditText send_email;

    Button btn_reset;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        btn_reset = findViewById(R.id.btn_reset);
        send_email = findViewById(R.id.send_email);


        auth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = send_email.getText().toString();

                if (email.equals("")){
                    Toast.makeText(ResetPasswordActivity.this,"All filed are required!",Toast.LENGTH_SHORT
                    ).show();

                }else {
                  auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this,"Please check your Email.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));

                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(ResetPasswordActivity.this,error,Toast.LENGTH_SHORT).show();

                        }
                      }
                  });


                }
            }
        });
    }
}
