package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject.Prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
private String check="";
private TextView pageTitle,titleQues;
private EditText phone,ques1,ques2;
private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pageTitle = (TextView) findViewById(R.id.page_title);
        titleQues = (TextView) findViewById(R.id.title_questions);
        phone = (EditText) findViewById(R.id.find_phone_number);
        ques1 = (EditText) findViewById(R.id.question_1);
        ques2 = (EditText) findViewById(R.id.question_2);
        verifyBtn = (Button) findViewById(R.id.verify_btn);

      //  verifyUser();



    }

    @Override
    protected void onStart() {
        super.onStart();
        phone.setVisibility(View.GONE);
         setAnswers();

    }


    private void setAnswers(){

        if(check.equals("settings")){

            DisplayPreviousAnswers();
            pageTitle.setText("Set Questions");
            titleQues.setText("Please set answers for the following security questions");
            verifyBtn.setText("Set");

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String answer1 = ques1.getText().toString().toLowerCase();
                    String answer2 = ques2.getText().toString().toLowerCase();
                    if(ques1.equals("")||ques2.equals("")){
                        Toast.makeText(ResetPasswordActivity.this,"Please answer Both the Questions",Toast.LENGTH_SHORT).show();
                    }

                    else{
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child(Prevelant.currentonlineUsers.getPhone());
                        HashMap<String,Object> userdataMap = new HashMap<>();
                        userdataMap.put("answer1",answer1);
                        userdataMap.put("answer2",answer2);

                        ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ResetPasswordActivity.this,"You have answered the security questions correct",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ResetPasswordActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });


                    }
                }
            });

        }
        else if(check.equals("login")){

            phone.setVisibility(View.VISIBLE);
            verifyBtn.setText("Confirm");
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            });

        }


    }

    private void DisplayPreviousAnswers(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevelant.currentonlineUsers.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();
                    ques1.setText(ans1);
                    ques2.setText(ans2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void verifyUser() {

        String user_phone = phone.getText().toString();
        final String answer1 = ques1.getText().toString().toLowerCase();
        final String answer2 = ques2.getText().toString().toLowerCase();

        if(!phone.equals("")&&!answer1.equals("")&& !answer2.equals("")){
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(user_phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        String mPhone = snapshot.child("phone").getValue().toString();


                        if(snapshot.hasChild("Security Questions")){

                            String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();

                            if(!ans1.equals(answer1)){
                                Toast.makeText(ResetPasswordActivity.this, "Your first answer is wrong", Toast.LENGTH_SHORT).show();
                            }

                            else if(!ans2.equals(answer2)){
                                Toast.makeText(ResetPasswordActivity.this, "Your second answer is wrong", Toast.LENGTH_SHORT).show();
                            }

                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write New Password here...");
                                builder.setView(newPassword);
                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(!newPassword.getText().toString().equals("")){
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(ResetPasswordActivity.this,"Your Password has been changed successfully",Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    finish();
                                    }
                                });
                                builder.show();
                            }

                        }


                        else{
                            Toast.makeText(ResetPasswordActivity.this,"You have not set the security questions.",Toast.LENGTH_SHORT).show();

                        }


                    }

                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this,"This phone does not exist",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        else
        {
            Toast.makeText(this, "Complete The Form.", Toast.LENGTH_SHORT).show();
        }

    }

}