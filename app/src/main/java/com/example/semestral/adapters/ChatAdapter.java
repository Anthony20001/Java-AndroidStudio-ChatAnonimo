package com.example.semestral.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.semestral.R;
import com.example.semestral.database.ModelChat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolderDates> {

    Activity activity;
    ArrayList<ModelChat> list_chats = new ArrayList();

    public ChatAdapter(Activity activity, ArrayList<ModelChat> list_chats){
        this.activity = activity;
        this.list_chats = list_chats;
    }

    @NonNull
    @Override
    public ViewHolderDates onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_chat_item, null, false);
        return new ViewHolderDates(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDates holder, int position) {
        holder.setDates(list_chats.get(position));
    }

    @Override
    public int getItemCount() {
        return list_chats.size();
    }



    public class ViewHolderDates extends RecyclerView.ViewHolder {
        TextView nickname, date, cant_like, comment;
        ImageButton btn_like;
        int id;
        boolean isLike = false;

        public ViewHolderDates(@NonNull View itemView) {
            super(itemView);

            nickname = itemView.findViewById(R.id.model_chat_nickname);
            date = itemView.findViewById(R.id.model_chat_text_date);
            cant_like = itemView.findViewById(R.id.model_chat_text_like);
            comment = itemView.findViewById(R.id.model_chat_text_message);
            btn_like = itemView.findViewById(R.id.model_chat_btn_like);
        }

        public void setDates(ModelChat chat) {
            try{
                nickname.setText(chat.getNickname());
                date.setText(chat.getUpdated_at());
                cant_like.setText(String.valueOf(chat.getMegusta()));
                comment.setText(chat.getComentario());
                id = chat.getId();
            }catch (Exception e){
                Toast.makeText(activity, ""+e, Toast.LENGTH_SHORT).show();
            }


            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isLike = like_animation(btn_like, R.raw.lottie_chat, isLike, cant_like);
                    readLike("https://shopick-2022.herokuapp.com/api/v1/comentarios/"+id, isLike);
                }
            });
        }
    }


    public void readLike(String url, boolean isLike){
        try{
            RequestQueue queue = Volley.newRequestQueue(activity);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        Toast.makeText(activity, "current likes: "+json.getString("megusta"), Toast.LENGTH_SHORT).show();
                        //updateLike(url, Integer.parseInt(json.getString("megusta")), isLike);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity, "readLike(): "+error, Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        }catch (Exception e){
            Toast.makeText(activity, "ERR_readLike(): "+e, Toast.LENGTH_SHORT).show();
        }

    }



    public void updateLike(String url, int cant_likes, boolean isLike){
        try{
            RequestQueue queue = Volley.newRequestQueue(activity);
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(activity, "Cantidad de me gusta actualizado", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity, "updateLike(): "+error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    if(isLike){
                        param.put("megusta", String.valueOf(cant_likes-1));
                    }else{
                        param.put("megusta", String.valueOf(cant_likes+1));
                    }
                    Toast.makeText(activity, "updateLike() = "+cant_likes, Toast.LENGTH_SHORT).show();
                    return super.getParams();
                }
            };
            queue.add(stringRequest);
        }catch (Exception e){
            Toast.makeText(activity, "ERR_updateLike(): "+e, Toast.LENGTH_SHORT).show();
        }
    }



    @SuppressLint("ResourceType")
    public boolean like_animation(ImageButton imglottie, int animation, boolean like, TextView cant_like){
        if(!like){
            /*imglottie.setAnimation(animation);
            imglottie.playAnimation();*/
            //imglottie.setImageResource(R.raw.like);
            imglottie.setImageResource(R.drawable.like_enable);
            cant_like.setText("1");
        }else{
            imglottie.setImageResource(R.drawable.like_disable);
            cant_like.setText("0");
        }
        return !like;
    }
}
