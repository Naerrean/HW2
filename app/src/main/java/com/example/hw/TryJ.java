package com.example.hw;

import org.json.JSONException;
import org.json.JSONObject;

public class TryJ {
    public static void getSomeDataWithCallback(String key, CallbackJsonHandler callback) {
        // В качестве примера выполним какие-то действия в background потоке
        new Thread(() -> {
            // Колбек требуется вызывать в Unity потоке
            UnityBridge.runOnUnityThread(() -> {
                try {
                    callback.onHandleResult(new JSONObject().put("Success", true).put("ValueStr", key).put( "ValueInt", 42));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
