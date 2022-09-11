package com.example.semestral;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.semestral.adapters.ChatAdapter;
import com.example.semestral.database.ModelChat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {

    TextView nickname;
    Button btn_addComment, btn_logout;
    EditText input_message;
    ArrayList<ModelChat> list_comments = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        try{
            read("https://shopick-2022.herokuapp.com/api/v1/comentarios");
        }catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }

        nickname = findViewById(R.id.chat_text_nickname);
        btn_addComment = findViewById(R.id.chat_btn_send_msg);
        input_message = findViewById(R.id.chat_input_message);
        recyclerView = findViewById(R.id.chat_recycler);
        btn_logout = findViewById(R.id.chat_btn_logout);

        nickname.setText(getIntent().getExtras().getString("nickname"));

        btn_addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!input_message.getText().toString().trim().equals("")){
                    create("https://shopick-2022.herokuapp.com/api/v1/comentarios");
                }else{
                    Toast.makeText(Chat.this, "Ingrese un comentario", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits")
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("nickname");
                if(editor.commit()){
                    Intent intent = new Intent(Chat.this, Login.class);
                    startActivity(intent);
                }
            }
        });
    }



    public void create(String url) {
        RequestQueue queue = Volley.newRequestQueue(Chat.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Chat.this, "Nuevo comentario", Toast.LENGTH_SHORT).show();
                read("https://shopick-2022.herokuapp.com/api/v1/comentarios");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Chat.this, "No se pudo crear el comentario: " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nickname", nickname.getText().toString().trim());
                params.put("megusta", String.valueOf(0));
                params.put("comentario", input_message.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }



    public void read(String url) {
        RequestQueue queue = Volley.newRequestQueue(Chat.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray json = jsonObject.getJSONArray("chats");
                    list_comments = new ArrayList<>();

                    for(int i=0; i<json.length(); i++){
                        ModelChat modelChat = new ModelChat(Chat.this);
                        modelChat.setId(Integer.parseInt(json.getJSONObject(i).getString("id")));
                        modelChat.setNickname(json.getJSONObject(i).getString("nickname"));
                        modelChat.setMegusta(Integer.parseInt(json.getJSONObject(i).getString("megusta")));
                        modelChat.setComentario(json.getJSONObject(i).getString("comentario"));
                        modelChat.setUpdated_at(json.getJSONObject(i).getString("updated_at"));
                        list_comments.add(modelChat);
                    }

                    ChatAdapter chatAdapter = new ChatAdapter(Chat.this, list_comments);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(chatAdapter);
                    Toast.makeText(Chat.this, "La lista de comentarios ha sido actualizada", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(Chat.this, "ERR "+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Chat.this, "Error al retornar lista de comentarios: "+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}