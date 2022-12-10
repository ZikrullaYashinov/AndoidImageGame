package com.example.imagedownload;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    Animation animation1, animation2, animation3;
    boolean isOneSelect = false;
    boolean key1 = false, key2 = false, key3 = false, key4 = false;
    ArrayList<GameItem> imageViews;
    String[] imgPlace = {"1","1","1","2","1","2","1","2","1","2","1","2","1","2","1","2","1","2"};
    int startId, endId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        load();

    }

    private void load(){
        imageViews = new ArrayList<>();
        imageViews.add(new GameItem(findViewById(R.id.image11), imgPlace[0]));
        imageViews.add(new GameItem(findViewById(R.id.image12), imgPlace[1]));
        imageViews.add(new GameItem(findViewById(R.id.image13), imgPlace[2]));
        imageViews.add(new GameItem(findViewById(R.id.image21), imgPlace[3]));
        imageViews.add(new GameItem(findViewById(R.id.image22), imgPlace[4]));
        imageViews.add(new GameItem(findViewById(R.id.image23), imgPlace[5]));
        imageViews.add(new GameItem(findViewById(R.id.image31), imgPlace[6]));
        imageViews.add(new GameItem(findViewById(R.id.image32), imgPlace[7]));
        imageViews.add(new GameItem(findViewById(R.id.image33), imgPlace[8]));
        imageViews.add(new GameItem(findViewById(R.id.image41), imgPlace[9]));
        imageViews.add(new GameItem(findViewById(R.id.image42), imgPlace[10]));
        imageViews.add(new GameItem(findViewById(R.id.image43), imgPlace[11]));
        imageViews.add(new GameItem(findViewById(R.id.image51), imgPlace[12]));
        imageViews.add(new GameItem(findViewById(R.id.image53), imgPlace[14]));
        imageViews.add(new GameItem(findViewById(R.id.image61), imgPlace[15]));
        imageViews.add(new GameItem(findViewById(R.id.image52), imgPlace[13]));
        imageViews.add(new GameItem(findViewById(R.id.image62), imgPlace[16]));
        imageViews.add(new GameItem(findViewById(R.id.image63), imgPlace[17]));

        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).getImageView().setOnClickListener(this);
        }

        animation1 = AnimationUtils.loadAnimation(GameActivity.this, R.anim.collection1);
        animation2 = AnimationUtils.loadAnimation(GameActivity.this, R.anim.collection2);
        animation3 = AnimationUtils.loadAnimation(GameActivity.this, R.anim.collection3);
    }

    @Override
    public void onClick(View view) {

        endId = view.getId();

        if (!isOneSelect){
            startId = endId;
        }
        else if (startId == endId){
            key4 = true;
        }

        ImageView imageView = findViewById(endId);
        imageView.startAnimation(animation1);

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!key1 || !key2){
                    if (!key4) {
                        imageView.setImageResource(getImageRes(Objects.requireNonNull(getImageViewTag(endId))));
                        imageView.startAnimation(animation2);
                    } else {
                        imageView.setImageResource(R.drawable.photoplaceholder);
                        imageView.startAnimation(animation2);
                    }
                }
                else {
                    key1 = false;
                    imageView.setImageResource(R.drawable.photoplaceholder);

                    key2 = false;
                    imageViews.get(getIndex(startId)).getImageView()
                            .setImageResource(R.drawable.photoplaceholder);

                    key3 = true;
                    imageView.startAnimation(animation2);
                    imageViews.get(getIndex(startId)).getImageView()
                            .startAnimation(animation2);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!key3 && !key4){
                    if (isOneSelect){
                        if (Objects.equals(getImageViewTag(endId), getImageViewTag(startId))){
                            imageViews.get(getIndex(startId)).getImageView()
                                    .startAnimation(animation3);
                            imageView.startAnimation(animation3);
                        } else {
                            key1 = true;
                            imageView.startAnimation(animation1);
                            key2 = true;
                            imageViews.get(getIndex(startId)).getImageView()
                                    .startAnimation(animation1);
                        }
                    }
                    isOneSelect = true;
                } else {
                    isOneSelect = false;
                    if (key3)
                        key3 = false;
                    if (key4)
                        key4 = false;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViews.get(getIndex(startId)).getImageView().setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                isOneSelect = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private int getIndex(int id){
        for (int i = 0; i < imageViews.size(); i++) {
            if (id == imageViews.get(i).getImageView().getId()){
                return i;
            }
        }
        return -1;
    }
    private String getImageViewTag(int id){
        for (int i = 0; i < imageViews.size(); i++) {
            if (id == imageViews.get(i).getImageView().getId()){
                return imageViews.get(i).getTag();
            }
        }
        return null;
    }
    private int getImageRes(String id){
        switch (id){
            case "1":
                return R.drawable.ic_launcher_background;
            case "2":
                return R.drawable.ic_launcher_foreground;
        }
        return 0;
    }
}
