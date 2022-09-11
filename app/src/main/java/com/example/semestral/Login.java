package com.example.semestral;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText input_nickname;
    Button btn_login, btn_signin;
    String nickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_nickname = findViewById(R.id.login_input_nickname);
        btn_login = findViewById(R.id.login_btn_in);
        btn_signin = findViewById(R.id.login_btn_signin);

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String nicknamePref = preferences.getString("nickname", "no existe");

        String noPreferences = "no existe";

        if(nicknamePref.equals(noPreferences)){
            nickname = input_nickname.getText().toString().trim();
        }else{
            input_nickname.setText(nicknamePref);
            Intent intent = new Intent(Login.this, Chat.class);
            intent.putExtra("nickname", nicknamePref);
            startActivity(intent);
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!input_nickname.getText().toString().equals("")){
                    try{
                        verifyUser("https://shopick-2022.herokuapp.com/api/v1/cuentas");
                    }catch (Exception e){
                        Toast.makeText(Login.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Login.this, "Ingrese un nickname", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signin.class);
                startActivity(intent);
            }
        });
    }


    public void verifyUser(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                boolean existNickname = false;
                String nickname = input_nickname.getText().toString().trim();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray json = jsonObject.getJSONArray("nicknames");
                    String jsonNickname;

                    for(int i=0 ; i<json.length(); i++){
                        jsonNickname = json.getJSONObject(i).getString("nickname");
                        if(jsonNickname.equalsIgnoreCase(nickname)){
                            existNickname = true;
                            Toast.makeText(Login.this, "Bienvenido, "+nickname, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    if(existNickname){
                        Intent intent = new Intent(Login.this, Chat.class);
                        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits")
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("nickname", nickname);
                        if(editor.commit()){
                            Toast.makeText(Login.this, "Credenciales de inico de session almacendas", Toast.LENGTH_SHORT).show();
                        }
                        intent.putExtra("nickname", nickname);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this, "No existe una cuenta con el nickname "+nickname, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Login.this, "ERR_: "+e, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Error al leer datos de "+url, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }



    public void readPreferences(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String nickname = preferences.getString("nickname", "no existe");
    }


}