package com.example.barbeariahgg.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.barbeariahgg.MainActivity;
import com.example.barbeariahgg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText edt_email_register;
    private EditText edt_senha_register;
    private EditText edt_confirmar_senha_register;
    private CheckBox ckb_mostrar_senha_register;
    private Button btn_registrar_register;
    private Button btn_login_register;
    private ProgressBar loginProgressBar_register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        edt_email_register = findViewById(R.id.edt_email_register);
        edt_senha_register = findViewById(R.id.edt_senha_register);
        edt_confirmar_senha_register = findViewById(R.id.edt_confirmar_senha_register);
        ckb_mostrar_senha_register = findViewById(R.id.ckb_mostrar_senha_register);
        btn_registrar_register = findViewById(R.id.btn_registrar_register);
        btn_login_register = findViewById(R.id.btn_login_register);
        loginProgressBar_register = findViewById(R.id.loginProgressBar_register);

        ckb_mostrar_senha_register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edt_senha_register.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_confirmar_senha_register.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    edt_senha_register.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_confirmar_senha_register.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn_registrar_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerEmail = edt_email_register.getText().toString();
                String registerSenha = edt_senha_register.getText().toString();
                String registerConfSenha = edt_confirmar_senha_register.getText().toString();

                if (!TextUtils.isEmpty(registerEmail) && !TextUtils.isEmpty(registerSenha) && !TextUtils.isEmpty(registerConfSenha)){
                    if (registerSenha.equals(registerConfSenha)){
                        loginProgressBar_register.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(registerEmail,registerSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    abrirTelaPrincipal();
                                }
                                else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(Register.this,""+error, Toast.LENGTH_SHORT).show();
                                    loginProgressBar_register.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(Register.this, "A senha deve ser a mesma!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Register.this, "Informações inválidas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}