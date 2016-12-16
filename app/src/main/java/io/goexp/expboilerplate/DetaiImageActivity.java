package io.goexp.expboilerplate;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.scala.exp.android.sdk.Exp;
import com.scala.exp.android.sdk.channels.IChannel;
import com.scala.exp.android.sdk.model.Content;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cesar Oyarzun on 11/10/16.
 */
public class DetaiImageActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_image);
        Serializable content = getIntent().getSerializableExtra("content");
        final Serializable uuid = getIntent().getSerializableExtra("uuid");
        ImageView image  = (ImageView) findViewById(R.id.image_detail);
        AsyncTask<String, Void, Bitmap> execute = new ImageDownloaderTask(image).execute(content.toString());
        Button flingButton  = (Button) findViewById(R.id.fling_button);
        flingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IChannel channel = Exp.getChannel("my-channel",false,true);
                Map<String, Object> payload = new HashMap<String, Object>();
                payload.put("uuid", uuid);
                channel.fling(payload);
            }
        });
    }
}
