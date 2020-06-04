package com.eduwars.corejava.common.connector;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * @author Ravi Singh
 * @date 03/06/20-06-2020
 * @project eduwars
 */
public abstract class ClassicResponse extends JsonElement {


    private static Gson gsonSerializer = new Gson();

    /**
     * map response into class
     *
     * @param cls
     */
    public <T extends Object> T mapResponse(Class<T> cls) {
        if(this.isJsonObject()) {
            return gsonSerializer.fromJson(this.getAsJsonObject(), cls);
        } else if(this.isJsonArray()) {
            return gsonSerializer.fromJson(this.getAsJsonArray(),cls);
        } else {
            throw new IllegalStateException("Not a JSON Object or JSON Array");
        }
    }
}