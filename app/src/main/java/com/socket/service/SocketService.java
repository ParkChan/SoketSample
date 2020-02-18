package com.socket.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.orhanobut.logger.Logger;
import com.socket.MainActivity;
import com.socket.R;
import com.socket.socket.SocketMannager;
import com.socket.socket.common.SocketInterface;
import com.socket.socket.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 안드로이드 Oreo(26) 백그라운드 서비스 제한 관련
 * startForegroundService 사용
 */
public class SocketService extends Service {

    private final String TAG = SocketService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG + " onCreate");
        initSocket();
        showNoticication(getResources().getString(R.string.run_socket_service));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            Logger.d(TAG + " onStartCommand START_NOT_STICKY");
            return START_NOT_STICKY;
        } else {
            Logger.d(TAG + " onStartCommand else");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d(TAG + " onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Logger.d(TAG + " onDestroy");
        SocketMannager.getInstance().socketOff();
        super.onDestroy();
    }

    private void initSocket() {
        SocketMannager.getInstance().socketOn();
        SocketMannager.getInstance().setSocketInterface(new SocketInterface() {
            @Override
            public void onConnect(Object... args) {

            }

            @Override
            public void onDisconnect(Object... args) {

            }

            @Override
            public void onConnectError(Object... args) {

            }

            @Override
            public void onConnectTimeOut(Object... args) {

            }

            @Override
            public void onMessageReceived(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String username;
                String message;
                try {
                    username = data.getString("username");
                    message = data.getString("message");
                } catch (JSONException e) {
                    return;
                }

                StringBuilder builder = new StringBuilder();
                builder.append("User name : ");
                builder.append(username);
                builder.append("\n");
                builder.append(" Message : ");
                builder.append(message);

                LogUtil.d(TAG, " onMessageReceived" + builder.toString());
                SocketMannager.getInstance().sendData(builder.toString());
                showNoticication(builder.toString());
            }
        });
    }

    private void showNoticication(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_service);
            remoteViews.setTextViewText(R.id.tv_message, message);
            NotificationCompat.Builder builder;

            String channelId = "channel_order";
            String channelName = "channel_name_rider";
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, channelId);

            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(remoteViews)
                    .setContentIntent(pendingIntent);

            startForeground(1, builder.build());
        }
    }
}


