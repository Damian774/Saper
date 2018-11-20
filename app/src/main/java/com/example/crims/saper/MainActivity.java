package com.example.crims.saper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout.LayoutParams params;
    RelativeLayout relativeLayout;
    BombButton buttons[];
    ArrayList<BombButton> bombsList;
    ArrayList<BombButton> numbersList;
    TextView tv_header;
    ArrayList<Integer> randomArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = findViewById(R.id.RelativeLayout);
        bombsList = new ArrayList<>();
        numbersList = new ArrayList<>();
        tv_header = findViewById(R.id.TV_header);
        buttons = new BombButton[25];
        randomArrayList = new ArrayList<>();
        load();

    }

    public void load(){
        for (int i = 0, k = 1; i < 25; i++) {
            BombButton button = new BombButton(this);
            button.setId(i + 1);
            button.setTag(Integer.toString(1));

            int width = getResources().getDisplayMetrics().widthPixels / 5;
            int height = getResources().getDisplayMetrics().widthPixels / 5;
            params = new RelativeLayout.LayoutParams(width, height);


            if (i == 0) params.addRule(RelativeLayout.RIGHT_OF, relativeLayout.getId());
            else if (i == 5 * k) {
                params.addRule(RelativeLayout.BELOW, buttons[5 * k - 5].getId());
                k++;
            } else {
                params.addRule(RelativeLayout.RIGHT_OF, buttons[i - 1].getId());
                if (k > 1) params.addRule(RelativeLayout.BELOW, buttons[5 * k - 10].getId());
            }

            button.setLayoutParams(params);
            relativeLayout.addView(button);
            button.setOnClickListener(this);

            buttons[i] = button;
        }
        createBombs(buttons);
        checkSurroundings(buttons);
        prepareFields(buttons);
    }


    public void createBombs(BombButton[] buttons) {
        Random random = new Random();
        while (randomArrayList.size() < 5) {
            int r = random.nextInt(25);
            if(randomArrayList.contains(r)) continue;
            randomArrayList.add(r);
            buttons[r].setTag("BOMB");
            bombsList.add(buttons[r]);
        }
    }

    public void checkSurroundings(BombButton[] buttons) {
        BombButton[][] array2d = new BombButton[5][5];
        int index=0;
            for(int m = 0;m<5;m++)
                for(int n=0;n<5;n++){
                    array2d[m][n] = buttons[index];
                    index++;
                }
        for (int i = 0; i < 5; i++)
            for(int j=0;j<5;j++)
              if (array2d[i][j].getTag().equals("BOMB"))
            {
                if(i-1>=0 && j-1>=0)array2d[i-1][j-1].bombsAround++;
                if(i-1>=0)array2d[i-1][j].bombsAround++;
                if(i-1>=0 && j+1<=4)array2d[i-1][j+1].bombsAround++;
                if(j-1>=0)array2d[i][j-1].bombsAround++;
                if(j+1<=4)array2d[i][j+1].bombsAround++;
                if(i+1<=4 && j-1>=0)array2d[i+1][j-1].bombsAround++;
                if(i+1<=4)array2d[i+1][j].bombsAround++;
                if(i+1<=4 && j+1<=4)array2d[i+1][j+1].bombsAround++;

                }

    }

        public void prepareFields (BombButton[]buttons){
            for (BombButton b : buttons) {
                if (!b.getTag().equals("BOMB")) {
                    //if (b.bombsAround == 0 || b.bombsAround == 1) b.bombsAround=1;
                    b.setTag(Integer.toString(b.bombsAround));
                }
            }
        }

    @Override
    public void onClick(View v) {
        BombButton button = findViewById(v.getId());
        String tag = button.getTag().toString();
        button.setText(tag);
        if(button.getTag().equals("BOMB")) loseGame(); else{
            numbersList.add(button);
            if(bombsList.size()+numbersList.size()==25) winGame();
        }


    }

    public void winGame(){
        for(BombButton b : bombsList){
            b.setText("BOMB");
        }
        tv_header.setText("You won");
        for(BombButton b : buttons) b.setOnClickListener(null);
    }

    public void loseGame(){
        for(BombButton b : bombsList){
            b.setText("BOOM");
        }
        tv_header.setText("BOOM!");
        for(BombButton b : buttons) b.setOnClickListener(null);

    }


    public void restartSmooth(View v){
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }
    public void restartBlink(View v){
        recreate();
    }
}

class BombButton extends android.support.v7.widget.AppCompatButton {
    public int bombsAround;
    public BombButton(Context context) {
        super(context);
        bombsAround = 0;
        //setTag("1");
    }
}

