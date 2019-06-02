package jp.tomiyama.noir.drag_and_drop;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    HorizontalScrollView horizontalScrollView;
    LinearLayout linearLayout;
    // 画面サイズ
//    int screenWidth;
//    int screenHeight;

    private ImageView picture;

    private ImageView[] cards;
    private int[] resources;

    private ImageView tenImageView;
    private ImageView oneImageView;
    // ドラッグしているものが一の位(or 十の位)の画像中にあるかを判定する変数
    private boolean flagTen = false;
    private boolean flagOne = false;

    // 選択しているカードの数
    private int judgeNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        linearLayout = findViewById(R.id.linearLayout);
        tenImageView = findViewById(R.id.tenImageView);
        oneImageView = findViewById(R.id.oneImageView);

        // 画面サイズの取得
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        Log.d("p.x", String.valueOf(p.x));
        Log.d("p.y", String.valueOf(p.y));

        cards = new ImageView[10];
        resources = new int[]{
                R.drawable.zero,  R.drawable.one,   R.drawable.two, R.drawable.three,
                R.drawable.four,  R.drawable.five,  R.drawable.six, R.drawable.seven,
                R.drawable.eight, R.drawable.nine
        };

        for(int i = 0; i < 10; i++){
            cards[i] = new ImageView(this);
        }

        tenImageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()){
                    case DragEvent.ACTION_DRAG_EXITED:
                        flagTen = false;
                        Log.d("ACTION_DRAG_EXITED","DragEvent.ACTION_DRAG_EXITED");
                        break;
                    case DragEvent.ACTION_DROP:
//                        x = dragEvent.getX();
//                        y = dragEvent.getY();
                        Log.d("ACTION_DROP","DragEvent.ACTION_DROP");
                        // 1の位の判定値をfalseに
                        flagOne = false;
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        flagTen = true;
                        Log.d("ACTION_DRAG_ENTERED","DragEvent.ACTION_DRAG_ENTERED");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        oneImageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()){
                    case DragEvent.ACTION_DRAG_EXITED:
                        flagOne = false;
                        Log.d("ACTION_DRAG_EXITED","DragEvent.ACTION_DRAG_EXITED");
                        break;
                    case DragEvent.ACTION_DROP:
                        Log.d("ACTION_DROP","DragEvent.ACTION_DROP");
                        flagTen = false;
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        flagOne = true;
                        Log.d("ACTION_DRAG_ENTERED","DragEvent.ACTION_DRAG_ENTERED");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(p.x/4, MP);

        for(int i = 0; i < 10; i++){
            cards[i].setImageResource(resources[i]);
            linearLayout.addView(cards[i],params);
            final int finalI = i;
            cards[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    judgeNumber = finalI;
                    Log.d("finalI", String.valueOf(finalI));
                    ClipData clipData = ClipData.newPlainText("card" + finalI, "Drag");
                    view.startDrag(clipData, new View.DragShadowBuilder(view), view, 0);
                    return false;
                }
            });

            cards[i].setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    if(dragEvent.getAction() == DragEvent.ACTION_DRAG_ENDED){

                        Log.d("flagTen", String.valueOf(flagTen));
                        Log.d("flagOne", String.valueOf(flagOne));


                        if(flagTen){
                            tenImageView.setImageResource(resources[judgeNumber]);
                            Log.d("resources(Ten)", String.valueOf(judgeNumber));
                        }
                        if(flagOne){
                            oneImageView.setImageResource(resources[judgeNumber]);
                            Log.d("resources(One)", String.valueOf(judgeNumber));
                        }
                        return false;
                    }
                    return true;
                }
            });

        }
    }

}
