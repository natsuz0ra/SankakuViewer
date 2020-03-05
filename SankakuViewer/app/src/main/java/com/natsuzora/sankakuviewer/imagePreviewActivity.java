package com.natsuzora.sankakuviewer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class imagePreviewActivity extends AppCompatActivity {
    public static String id;
    public static String sample_url;

    public static ImageView sample_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.imagev_activity);

        id = getIntent().getStringExtra("id");
        sample_imageview = findViewById(R.id.imagePreview);

        getpreviewinfo gpi = new getpreviewinfo();
        gpi.start();
    }

    @SuppressLint("HandlerLeak")
    public static Handler load_handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 0){
                Picasso.get().load(sample_url).into(sample_imageview);
            }
        }
    };
}
