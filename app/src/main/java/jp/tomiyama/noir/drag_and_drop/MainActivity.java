package jp.tomiyama.noir.drag_and_drop;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
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

    private int[] resources;

    private ImageView tenImageView;
    private ImageView oneImageView;

    // ドラッグしているものが一の位(or 十の位)の画像中にあるかを判定する変数
    private boolean flagTen = false;
    private boolean flagOne = false;

    // 選択しているカードの数
    private int judgeNumber = 0;

    // 現在の数字
    private int tenNumber = 0;
    private int oneNumber = 0;
    private int sumNumber = 0;

    // カードの状態を保存する変数
    private Card[] cards;

    @SuppressLint("ClickableViewAccessibility")
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

        // linearLayoutにaddViewするImageView
        final ImageView[] imageCards = new ImageView[10];
        cards = new Card[10];

        resources = new int[]{
                R.drawable.zero,  R.drawable.one,   R.drawable.two, R.drawable.three,
                R.drawable.four,  R.drawable.five,  R.drawable.six, R.drawable.seven,
                R.drawable.eight, R.drawable.nine
        };

        for(int i = 0; i < 10; i++){
            imageCards[i] = new ImageView(this);
            cards[i] = new Card(resources[i]);
        }

        tenImageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()){
                    case DragEvent.ACTION_DRAG_EXITED:
                        // 10の位の判定値をfalseに
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
                        // 10の位の判定値をtrueに
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
                        // 1の位の判定値をfalseに
                        flagOne = false;
                        Log.d("ACTION_DRAG_EXITED","DragEvent.ACTION_DRAG_EXITED");
                        break;
                    case DragEvent.ACTION_DROP:
                        Log.d("ACTION_DROP","DragEvent.ACTION_DROP");
                        // 10の位の判定値をfalseに
                        flagTen = false;
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // 1の位の判定値をtrueに
                        flagOne = true;
                        Log.d("ACTION_DRAG_ENTERED","DragEvent.ACTION_DRAG_ENTERED");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        // 画面横幅の1/4サイズ分をカードの横幅に設定
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(p.x/4, MP);

        for(int i = 0; i < 10; i++){
            imageCards[i].setImageResource(resources[i]);
            linearLayout.addView(imageCards[i],params);
            final int finalI = i;
            imageCards[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    judgeNumber = finalI;
                    Log.d("finalI", String.valueOf(finalI));
                    ClipData clipData = ClipData.newPlainText("card" + finalI, "Drag");
                    view.startDrag(clipData, new View.DragShadowBuilder(view), view, 0);
                    return false;
                }
            });

            imageCards[i].setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    if(dragEvent.getAction() == DragEvent.ACTION_DRAG_ENDED){

                        Log.d("flagTen", String.valueOf(flagTen));
                        Log.d("flagOne", String.valueOf(flagOne));

                        if(flagTen){

                            // 前のカードを選択可能(表に)に
                            if(cards[tenNumber].isStatus()){
                                // 表向きに
                                imageCards[tenNumber].setImageResource(cards[tenNumber].getFront());
                                // 使用状態をfalseに
                                cards[tenNumber].setStatus(false);
                            }

                            tenImageView.setImageResource(resources[judgeNumber]);
                            Log.d("resources(Ten)", String.valueOf(judgeNumber));

                            tenNumber = judgeNumber;


                            // 裏面にする処理
                            changeView(judgeNumber);

                        }
                        if(flagOne){

                            // 前のカードを選択可能(表に)に
                            if(cards[oneNumber].isStatus()){
                                // 表向きに
                                imageCards[oneNumber].setImageResource(cards[oneNumber].getFront());
                                // 使用状態をfalseに
                                cards[oneNumber].setStatus(false);
                            }

                            oneImageView.setImageResource(resources[judgeNumber]);
                            Log.d("resources(One)", String.valueOf(judgeNumber));
                            oneNumber = judgeNumber;

                            // 裏面にする処理
                            changeView(judgeNumber);

                        }

                        // 合計数字の算出
                        sumNumber = tenNumber * 10 + oneNumber;
                        Log.d("sum", String.valueOf(sumNumber));

                        return false;
                    }
                    return true;
                }
            });

            imageCards[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    // 既に使用済みのカードは使用不可にする
                    if(cards[finalI].isStatus()) {
                        Log.d("Error","そのカードは選択できません");
                        return true;
                    }
                    return false;
                }

            });
        }
    }

    // 選択したカードを裏返しにし、前のカードを復活させる
    public void changeView(int index){
        ImageView imageView = (ImageView) linearLayout.getChildAt(index);
        // statusをtrueにする
        cards[index].setStatus(true);
        imageView.setImageResource(cards[index].getBack());
    }

}
