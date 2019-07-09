package jp.tomiyama.noir.drag_and_drop;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    // カードリソース
    private int[] resources;

    // 0-6までのImageView
    private ImageView[] imageCards;

    // 10の位、1の位のImageView
    private ImageView tenImageView;
    private ImageView oneImageView;

    // ドラッグしているものが一の位(or 十の位)の画像中にあるかを判定
    private boolean flagTen = false;
    private boolean flagOne = false;

    // 選択しているカードの数
    private int chooseNumber = 0;

    // 現在の数字
    private int tenNumber = 0;
    // Note: 実装方法考える
    private int oneNumber = 6; // 初期値を6とする
    private int sumNumber = 6; // 初期値を6とする

    // カードの状態を保存する変数
    private Card[] cards;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 関連付け
        tenImageView = findViewById(R.id.tenImageView);
        oneImageView = findViewById(R.id.oneImageView);

        // LayoutにaddするImageView
        imageCards = new ImageView[6];

        int[] imageIds = new int[]{
                R.id.imageView, R.id.imageView2, R.id.imageView3,
                R.id.imageView4, R.id.imageView5, R.id.imageView6
        };

        // 関連付け
        for(int i = 0; i < 6; i++){
            imageCards[i] = findViewById(imageIds[i]);
        }

        // 7つ目に空を入れる
        cards = new Card[7];

        // 0-5までのカードを用意 + 空
        resources = new int[]{
                R.drawable.zero,  R.drawable.one,   R.drawable.two,
                R.drawable.three, R.drawable.four,  R.drawable.five,
                R.drawable.empty
        };

        // Cardクラス変数のインスタンス生成
        for(int i = 0; i < 7; i++){
            cards[i] = new Card(resources[i]);
        }

        // カードの範囲内かどうか判定
        // isCards[0] -> 10の位 / isCards[1] -> 1の位 を示す
        final boolean[] isCards = {false, false};

        // 10の位のドラッグ処理
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
                        float x = dragEvent.getX();
                        float y = dragEvent.getY();

                        Log.d("(x,y)","(" + x + "," + y + ")");
                        Log.d("ACTION_DROP","DragEvent.ACTION_DROP");
                        // 1の位の判定値をfalseに
                        flagOne = false;

                        // カードが範囲内かどうか判定
                        isCards[0] = true; // 10の位 -> true
                        isCards[1] = false; // 1の位 -> false

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

        // 1の位のドラッグ処理
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

                        // カードが範囲内かどうか判定
                        isCards[0] = false; // 10の位 -> false
                        isCards[1] = true;  // 1の位 -> true

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


        for(int i = 0; i < 6; i++){
            imageCards[i].setImageResource(resources[i]);
            final int finalI = i;
            // 長押しの際のリスナー登録
            imageCards[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    chooseNumber = finalI;
                    Log.d("finalI", String.valueOf(finalI));
                    ClipData clipData = ClipData.newPlainText("card" + finalI, "Drag");
                    view.startDrag(clipData, new View.DragShadowBuilder(view), view, 0);
                    return false;
                }
            });

            // ドラッグの際のリスナー登録
            imageCards[i].setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    // ドラッグの終了 かつ カード範囲内であったら
                    if(dragEvent.getAction() == DragEvent.ACTION_DRAG_ENDED
                            && (isCards[0] || isCards[1]) ){

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

                            tenImageView.setImageResource(resources[chooseNumber]);
                            Log.d("resources(Ten)", String.valueOf(chooseNumber));
                            tenNumber = chooseNumber;

                            // 裏面にする処理
                            changeView(chooseNumber);

                        }

                        if(flagOne){

                            // 前のカードを選択可能(表に)に
                            if(cards[oneNumber].isStatus()){
                                // 表向きに
                                imageCards[oneNumber].setImageResource(cards[oneNumber].getFront());
                                // 使用状態をfalseに
                                cards[oneNumber].setStatus(false);
                            }

                            oneImageView.setImageResource(resources[chooseNumber]);
                            Log.d("resources(One)", String.valueOf(chooseNumber));
                            oneNumber = chooseNumber;

                            // 裏面にする処理
                            changeView(chooseNumber);

                        }

                        // 合計数字の算出 (tenNumber と oneNumberが6のとき、要対応)
                        // 二桁入っていないと、次に進めないようにすればOK
                        sumNumber = tenNumber * 10 + oneNumber;
                        Log.d("sum", String.valueOf(sumNumber));

                        isCards[0] = false;
                        isCards[1] = false;

                        return false;
                    }
                    return true;
                }
            });

            imageCards[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    // 既に使用中のカードは使用不可にする
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
        // statusをtrueにする
        cards[index].setStatus(true);
        imageCards[index].setImageResource(cards[index].getBack());
    }

}
