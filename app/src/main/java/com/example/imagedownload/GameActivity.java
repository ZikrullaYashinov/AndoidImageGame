package com.example.imagedownload;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable
    TextView selectCountTextView;
    ImageView restart;
    Animation animation1, animation2, animation3;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isOneSelect = false, run = true;
    boolean key1 = false, key2 = false, key3 = false, key4 = false;
    int winner = 0;
    ArrayList<GameItem> imageViews;
    int startId, endId;
    int selectCount = 0;

    String[] imgPlace;
    String[] imgPlaceItems = {"0", "1", "2", "3", "4", "5", "6", "7", "8",
            "0", "1", "2", "3", "4", "5", "6", "7", "8"};
    Random random;
    int[] randomInt;
    // ----------------------------------------------------------

    // Main method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadOne();

        generateMain();

        load();

        click();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Dasturdan chiqmoqchimisiz?")
                .setCancelable(false)
                .setPositiveButton("Ha", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Yo'q", null)
                .show();
    }
    // ----------------------------------------------------------

    // Click method
    @Override
    public void onClick(View view) {
        if (run) {
            run = false;

            selectCount++;
            showSelectCount();

            endId = view.getId();

            if (!isOneSelect)
                startId = endId;
            else if (startId == endId)
                key4 = true;

            ImageView imageView = findViewById(endId);
            imageView.startAnimation(animation1);

            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!key1 || !key2) {
                        if (!key4) {
                            imageView.setImageResource(
                                    getImageRes(Objects.requireNonNull(getImageViewTag(endId))));
                            imageView.startAnimation(animation2);
                        } else {
                            imageView.setImageResource(R.drawable.photoplaceholder);
                            imageView.startAnimation(animation2);
                        }
                    } else {
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
                    if (!key3 && !key4) {
                        if (isOneSelect) {
                            if (Objects.equals(getImageViewTag(endId), getImageViewTag(startId))) {
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
                        } else {
                            isOneSelect = true;
                            run = true;
                        }
                    } else {
                        isOneSelect = false;
                        if (key3)
                            key3 = false;
                        if (key4)
                            key4 = false;
                        run = true;
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
                    winner++;
                    isOneSelect = false;
                    run = true;
                    if (winner == imgPlace.length / 2)
                        winner();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void click() {
        restart.setOnClickListener(view -> {
            if (run) {
                selectCount = 0;
                showSelectCount();
                restart();
                run = true;
            }
        });
    }
    // ----------------------------------------------------------

    // My methods
    private RecordUesr getRecord(){
        String sharedPreferencesString = sharedPreferences.getString("record", "");
        Type type = new TypeToken<List<RecordUesr>>(){}.getType();
        List<RecordUesr> recordUesrs = MyGson.getInstance().getGson()
                .fromJson(sharedPreferencesString, type);
        if (recordUesrs == null)
            return new RecordUesr("", 0, 0);
        else {
            RecordUesr recordUesr = recordUesrs.get(0);
            int min = recordUesrs.get(0).getUrunishlarSoni();
            for (int i = 1; i < recordUesrs.size(); i++) {
                if (min > recordUesrs.get(i).getUrunishlarSoni()) {
                    min = recordUesrs.get(i).getUrunishlarSoni();
                    recordUesr = recordUesrs.get(i);
                }
            }
            return recordUesr;
        }
    }
    private void setRecord(int rec){
        String name = "Men";
        int urunishlarSoni = rec;
        int orni = 1;
        RecordUesr recordUesr = new RecordUesr(name, urunishlarSoni, orni);
        String sharedPreferencesString = sharedPreferences.getString("record", "");
        List<RecordUesr> recordUesrs;
        if (sharedPreferencesString.equals("")){
            recordUesrs = new ArrayList<>();
        }else {
            Type type = new TypeToken<List<RecordUesr>>(){}.getType();
            recordUesrs = MyGson.getInstance().getGson().fromJson(sharedPreferencesString, type);
        }
        recordUesrs.add(recordUesr);
        String jsonString = MyGson.getInstance().getGson().toJson(recordUesrs);
        editor.putString("record", jsonString).commit();
    }

    private void winner() {
        new AlertDialog.Builder(this)
                .setMessage("Tabriklaymiz siz g'olib bo'ldingiz!!!\n" +
                        "Sizning urunishlaringiz " + selectCount + " ta")
                .setPositiveButton("Ok", null).show();
        restart();
    }

    private void restart(){
        startId = 0;
        endId = 0;
        isOneSelect = false;
        key1 = false;
        key2 = false;
        key3 = false;
        key4 = false;
        generateMain();
        load();
        for (GameItem imageView : imageViews) {
            imageView.getImageView().setVisibility(View.VISIBLE);
            imageView.getImageView().setImageResource(R.drawable.photoplaceholder);
        }
        winner = 0;
        selectCount = 0;
        showSelectCount();
    }
    private int[] randomIntRestart(){
        return new int[imgPlace.length];
    }

    private void showSelectCount() {
        this.selectCountTextView.setText("Urunishlar soni: " + selectCount);
    }

    private void loadOne(){
        selectCountTextView = findViewById(R.id.selectCount);
        restart = findViewById(R.id.restart);
        imgPlace = new String[imgPlaceItems.length];
        random = new Random();
        randomInt = randomIntRestart();
        animation1 = AnimationUtils.loadAnimation(GameActivity.this, R.anim.collection1);
        animation2 = AnimationUtils.loadAnimation(GameActivity.this, R.anim.collection2);
        animation3 = AnimationUtils.loadAnimation(GameActivity.this, R.anim.collection3);
        sharedPreferences = getSharedPreferences("Record", MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
    }

    private void generateMain(){
        this.randomInt = newGenerateArray(randomIntRestart());
        int j=0;
        for (String i : imgPlaceItems){
            this.imgPlace[randomInt[j]-1] = i;
            j++;
        }
    }
    private int[] newGenerateArray(int[] randomInt) {
        randomInt[0] = generate();
        for (int j = 1; j < 18; j++) {
            boolean b = true;
            while (b) {
                int g = generate();
                b = false;
                for (int i = 0; i < 18; i++) {
                    if (g == randomInt[i]) {
                        b = true;
                        i = 18;
                    }
                }
                if (!b) {
                    randomInt[j] = g;
                }
            }
        }
        return randomInt;
    }
    private int generate(){
        return random.nextInt(19);
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
                return R.drawable.img1;
            case "2":
                return R.drawable.img2;
            case "3":
                return R.drawable.img3;
            case "4":
                return R.drawable.img4;
            case "5":
                return R.drawable.img5;
            case "6":
                return R.drawable.img6;
            case "7":
                return R.drawable.img7;
            case "8":
                return R.drawable.img8;
            case "0":
                return R.drawable.img9;
        }
        return 0;
    }
    // ----------------------------------------------------------
}