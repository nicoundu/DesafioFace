package com.desafiolatam.desafioface.fcm;

import com.desafiolatam.desafioface.notifications.FavoriteNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String email = remoteMessage.getData().get("email");
        FavoriteNotification.notify(this, email);
    }
}
