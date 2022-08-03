package ru.hixon.notificationstogit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GitNotificationListener extends NotificationListenerService {

    // https://github.com/settings/tokens
    private static final String GITHUB_PERSONAL_TOKEN = "";
    private static final String GITHUB_API_URL = "";

    private static final Set<String> PACKAGES_FOR_NOTIFICATIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "ru.yandex.mail",
            "com.whatsapp",
            "org.telegram.messenger")));
    
    public static final String CURRENT_PACKAGE = "ru.hixon.notificationstogit";

    private final ExecutorService pool = Executors.newFixedThreadPool(1);
    private volatile long lastReceivedEvent = 0L;

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.i(CURRENT_PACKAGE,"onListenerConnected");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        final String packageName = sbn.getPackageName();
        if (packageName.equals(CURRENT_PACKAGE)) {
            // when we start the app, we got a strange event with this name
            return;
        }
        if (!PACKAGES_FOR_NOTIFICATIONS.contains(packageName)) {
            // I don't care about other events
            return;
        }

        Log.w(CURRENT_PACKAGE, "Received notification event: packageName=" + packageName);

        final long currentLastReceivedEvent = this.lastReceivedEvent;
        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - currentLastReceivedEvent < 1000L) {
            // probably, it is duplicated events, and we don't care about them
            Log.w(CURRENT_PACKAGE, "Ignore duplicated events: packageName=" + packageName);
            return;
        }
        this.lastReceivedEvent = currentTimeMillis;

        pool.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;

                try {
                    String gitSha = getFileGitSha();

                    // send new version of a file to github
                    URL url = new URL(GITHUB_API_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Authorization", "token " + GITHUB_PERSONAL_TOKEN);
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setRequestProperty("Accept-Type", "application/vnd.github+json");

                    urlConnection.setDoOutput(true);
                    try(OutputStream os = urlConnection.getOutputStream()) {
                        String content = packageName + " " + System.currentTimeMillis();
                        String contentAsBase64 = Base64.encodeToString(content.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT).trim();
                        String jsonStr = "{\"message\":\"a new commit message\",\"committer\":{\"name\":\"Super Duper\",\"email\":\"supernexus5@gmail.com\"},\"content\":\"" + contentAsBase64 + "\",\"sha\":\"" + gitSha + "\"}";
                        byte[] input = jsonStr.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        String responseStr = response.toString();
//                        Log.w(CURRENT_PACKAGE, "received response: responseStr= " + responseStr);
                    }
                } catch (Exception e) {
                    Log.w(CURRENT_PACKAGE, "Got error, when send request to github", e);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });
    }

    private String getFileGitSha() throws IOException {
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(GITHUB_API_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "token " + GITHUB_PERSONAL_TOKEN);
            urlConnection.setRequestProperty("Accept-Type", "application/vnd.github+json");

            JsonReader jsonReader = new JsonReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            jsonReader.beginObject();
            String sha = null;
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (name.equals("sha")) {
                    sha = jsonReader.nextString();
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            return sha;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private void startForeground() {
        String NOTIFICATION_CHANNEL_ID = CURRENT_PACKAGE;
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2, notification);
    }
}
