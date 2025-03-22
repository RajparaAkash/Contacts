package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Keep
@Entity(tableName = "QuickResponse")
public class ModelQuickResponse implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    int id;
    String message;


    public ModelQuickResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
