package com.ganlen.compartamosviaje;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class BienvenidaViajero extends AppCompatActivity {
      private EditText loginviajero_email, loginviajero_password;
      private Button btn_IngresarViajero;
      private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bienvenida_viajero);
        loginviajero_email = findViewById(R.id.emailviajero_login);
        loginviajero_password = findViewById(R.id.contraseñaviajero_login);
        btn_IngresarViajero = findViewById(R.id.id_IngresarViajero);


    }


    public void IngresarViajero (View v) {
        String email = loginviajero_email.getText().toString().trim();
        String password = loginviajero_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            loginviajero_email.setError("Este campo no puede estar vacío");
            return;

        }
        if (TextUtils.isEmpty(password)) {
            loginviajero_password.setError("Este campo no puede estar vacío");
            return;

        }
        final ProgressDialog progressDialog = ProgressDialog.show(BienvenidaViajero.this, "Por favor espera", "Procesando...", true);

        (firebaseAuth.signInWithEmailAndPassword(loginviajero_email.getText().toString(), loginviajero_password.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();


                        if (task.isSuccessful()) {
                            Toast.makeText(BienvenidaViajero.this, "Ingresaste correctamente", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(BienvenidaViajero.this, MainActivity.class);
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);
                        } else {

                            String error = "";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                error = "Contraseña Muy Corta";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                error = "Contraseña Incorrecta";

                            } catch (FirebaseAuthInvalidUserException e) {
                                error = "Email Inválido";

                            } catch (FirebaseAuthUserCollisionException e) {
                                error = "Esta cuenta Ya Existe!";
                            } catch (Exception e) {
                                error = "Error Desconocido, Intenta Nuevamente";
                                e.printStackTrace();

                            }

                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();


                        }

                    }


                });
    }
}
