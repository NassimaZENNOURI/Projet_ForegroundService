package ma.ensa.tp_pratique;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean isPlaying = false;
    private ImageButton playPauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playPauseButton = findViewById(R.id.btnPlayPause);

        Intent playIntent = new Intent(this, MyService.class);
        playIntent.setAction("PLAY");

        Intent pauseIntent = new Intent(this, MyService.class);
        pauseIntent.setAction("PAUSE");

        // Gestion du bouton Play/Pause
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopService(pauseIntent);
                playPauseButton.setImageResource(R.drawable.ic_play);
                isPlaying = false;
            } else {
                startForegroundService(playIntent);
                playPauseButton.setImageResource(R.drawable.ic_stop);
                isPlaying = true;
            }
        });

    }
}