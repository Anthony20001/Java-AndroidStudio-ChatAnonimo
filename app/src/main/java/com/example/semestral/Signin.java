package com.example.semestral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Signin extends AppCompatActivity {

    EditText input_nickname;
    Button btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        input_nickname = findViewById(R.id.signin_input_nickname);
        btn_signin = findViewById(R.id.signin_btn);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create("https://shopick-2022.herokuapp.com/api/v1/cuentas");
            }
        });
    }


    public void create(String url) {
        RequestQueue queue = Volley.newRequestQueue(Signin.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Signin.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Signin.this, Login.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Signin.this, "ERR: "+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("nickname", input_nickname.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }
}