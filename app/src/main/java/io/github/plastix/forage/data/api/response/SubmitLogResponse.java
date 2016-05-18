package io.github.plastix.forage.data.api.response;

import com.google.gson.annotations.SerializedName;

public class SubmitLogResponse {

    @SerializedName("success")
    public boolean isSuccessful;

    @SerializedName("message")
    public String message;


    @Override
    public String toString() {
        return "SubmitLogResponse{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                '}';
    }
}
