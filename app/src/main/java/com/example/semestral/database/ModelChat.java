package com.example.semestral.database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class ModelChat {

    private int id, megusta;
    private String nickname, comentario;
    private String created_at, updated_at;
    private boolean isLike;
    private  Activity activity;

    public ModelChat(Activity activity){
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMegusta() {
        return megusta;
    }

    public void setMegusta(int megusta) {
        this.megusta = megusta;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at.substring(0, 19);
    }

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean like) {
        isLike = like;
    }

    /*@SuppressLint("SimpleDateFormat")
    public void setUpdated_at(Date updated_at) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        this.updated_at = dateFormat.format(updated_at);
        Toast.makeText(activity, "setUpdate_at() run = "+dateFormat.format(updated_at), Toast.LENGTH_SHORT).show();
    }*/
}
