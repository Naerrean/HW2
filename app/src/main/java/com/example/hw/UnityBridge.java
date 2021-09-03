package com.example.hw;

import android.os.Handler;

public final class UnityBridge {
    // Содержит ссылку на C# реализацию интерфейса
    private static JavaMessageHandler javaMessageHandler;
    // Перенаправляет вызов в Unity поток
    private static Handler unityMainThreadHandler;

    public static void registerMessageHandler(JavaMessageHandler handler) {
        javaMessageHandler = handler;
        if(unityMainThreadHandler == null) {
            // Так как эту функцию вызываем всегда на старте Unity,
            // этот вызов идет из нужного нам в дальнейшем потока,
            // создадим для него Handler
            unityMainThreadHandler = new Handler();
        }
    }

    // Функция перевода выполнения в Unity поток, потребуется в дальнейшем
    public static void runOnUnityThread(Runnable runnable) {
        if(unityMainThreadHandler != null && runnable != null) {
            unityMainThreadHandler.post(runnable);
        }
    }

    // Пишем какую-нибудь функцию, которая будет отправлять сообщения в Unity
    public static void SendMessageToUnity(final String message, final String data) {
        runOnUnityThread(() -> {
            if(javaMessageHandler != null) {
                javaMessageHandler.onMessage(message, data);
            }
        });
    }
}
