package ma.ensa.tp_pratique;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private MediaPlayer mediaPlayer;
    private static final String CHANNEL_ID = "MusicPlayerChannel";


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Player Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private void startMusic() {
        if (mediaPlayer == null ) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music1);
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start();
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void updateNotification(String contentText) {
        Notification notification = getNotification(contentText);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if ("PLAY".equals(action)) {
            startForeground(1, getNotification("Playing"));
            startMusic();
        } else if ("PAUSE".equals(action)) {
            pauseMusic();
            updateNotification("Paused");
            stopForeground(false);
        }
        return START_STICKY;
    }

    private Notification getNotification(String contentText) {
        Intent playIntent = new Intent(this, MyService.class);
        playIntent.setAction("PLAY");
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent pauseIntent = new Intent(this, MyService.class);
        pauseIntent.setAction("PAUSE");
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Player")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_music)
                .addAction(R.drawable.ic_play, "Play", playPendingIntent)
                .addAction(R.drawable.ic_stop, "Pause", pausePendingIntent)
                .build();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}