package jp.tomiyama.noir.drag_and_drop;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import maes.tech.intentanim.CustomIntent;

public class SecondPlayerActivity extends AppCompatActivity {

  // パラメータ変数
  private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
  private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

  // レイアウト
  HorizontalScrollView horizontalScrollView;
  LinearLayout linearLayout;

  // カードリソース
  // 0-9までのカードを用意
  private int[] resources = new int[]{
      R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three,
      R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
      R.drawable.eight, R.drawable.nine
  };

  // 10の位、1の位のImageView
  private ImageView tenImageView, oneImageView;

  // 選択しているカードの数
  private int chooseNumber = 0;

  // 現在の数字
  private int tenNumber,oneNumber,sumNumber;

  // カードの状態を保存する変数
  private Card[] cards;

  // 両方ともに数字が入っているのかチェックする変数
  private boolean isUsedTen = false;
  private boolean isUsedOne = false;

  private SharedPreferences pref;
  private final String PLAYER2_INDEX = "player2_index_";
  private final String PLAYER2_SCORE = "player2_score";

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second_player);

    // SharedPreferencesの準備
    pref = getSharedPreferences("numeric_app",MODE_PRIVATE);

    // 関連付け
    horizontalScrollView = findViewById(R.id.horizontalScrollView);
    linearLayout = findViewById(R.id.linearLayout);
    tenImageView = findViewById(R.id.tenImageView);
    oneImageView = findViewById(R.id.oneImageView);

    // 画面サイズの取得
    Display display = getWindowManager().getDefaultDisplay();
    Point p = new Point();
    display.getSize(p);

    // LayoutにaddViewするImageView
    final ImageView[] imageCards = new ImageView[10];
    cards = new Card[10];

    // Cardクラス変数のインスタンス生成
    for (int i = 0; i < 10; i++) {
      imageCards[i] = new ImageView(this);
      cards[i] = new Card(resources[i], R.drawable.back);
    }

    // カードの範囲内かどうか判定
    // isCards[0] -> 10の位 / isCards[1] -> 1の位 を示す
    final boolean[] isCards = {false, false};

    // 10の位のドラッグ処理
    tenImageView.setOnDragListener(new View.OnDragListener() {
      @Override
      public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
          case DragEvent.ACTION_DRAG_EXITED:
            Log.d("ACTION_DRAG_EXITED", "DragEvent.ACTION_DRAG_EXITED");
            break;
          case DragEvent.ACTION_DROP:
            float x = dragEvent.getX();
            float y = dragEvent.getY();
            Log.d("(x,y)", "(" + x + "," + y + ")");
            Log.d("ACTION_DROP", "DragEvent.ACTION_DROP");

            // ドラッグされたカードが範囲内かどうか判定
            isCards[0] = true;
            isCards[1] = false;

            break;
          case DragEvent.ACTION_DRAG_ENTERED:
            Log.d("ACTION_DRAG_ENTERED", "DragEvent.ACTION_DRAG_ENTERED");
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
        switch (dragEvent.getAction()) {
          case DragEvent.ACTION_DRAG_EXITED:
            Log.d("ACTION_DRAG_EXITED", "DragEvent.ACTION_DRAG_EXITED");
            break;
          case DragEvent.ACTION_DROP:
            Log.d("ACTION_DROP", "DragEvent.ACTION_DROP");

            // カードが範囲内かどうか判定
            isCards[0] = false;
            isCards[1] = true;

            break;
          case DragEvent.ACTION_DRAG_ENTERED:
            Log.d("ACTION_DRAG_ENTERED", "DragEvent.ACTION_DRAG_ENTERED");
            break;
          default:
            break;
        }
        return true;
      }
    });

    // 画面横幅の1/6サイズ分をカードの横幅に設定
    LinearLayout.LayoutParams params
        = new LinearLayout.LayoutParams((int) ((p.x - convertDp2Px(16, this)) / 6), MP);

    float margin = convertDp2Px(4, this);
    params.setMargins(0, (int) margin, 0, (int) margin);

    for (int i = 0; i < 10; i++) {
      imageCards[i].setImageResource(resources[i]);
      linearLayout.addView(imageCards[i], params);
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
          // ドラッグの終了 および カード範囲内であったら
          if (dragEvent.getAction() == DragEvent.ACTION_DRAG_ENDED
              && (isCards[0] || isCards[1])) {

            Log.d("------------","------------");
            Log.d("isCards[0]", String.valueOf(isCards[0]));
            Log.d("isCards[1]", String.valueOf(isCards[1]));
            Log.d("------------","------------");

            // 10の位の数字のとき
            if (isCards[0]) {

              // 前のカードを選択可能(表に)に
              if (cards[tenNumber].isStatus() && isUsedTen) {
                // 表向きに
                imageCards[tenNumber].setImageResource(cards[tenNumber].getFront());
                // 使用状態をfalseに
                cards[tenNumber].setStatus(false);
              }

              tenImageView.setImageResource(resources[chooseNumber]);
              Log.d("resources(Ten)", String.valueOf(chooseNumber));
              tenNumber = chooseNumber;
              isUsedTen = true;

              // 裏面にする処理
              changeView(chooseNumber);

            }

            // 1の位の数字のとき
            if (isCards[1]) {

              // 前のカードを選択可能(表に)に
              if (cards[oneNumber].isStatus() && isUsedOne) {
                // 表向きに
                imageCards[oneNumber].setImageResource(cards[oneNumber].getFront());
                // 使用状態をfalseに
                cards[oneNumber].setStatus(false);
              }

              oneImageView.setImageResource(resources[chooseNumber]);
              Log.d("resources(One)", String.valueOf(chooseNumber));
              oneNumber = chooseNumber;
              isUsedOne = true;

              // 裏面にする処理
              changeView(chooseNumber);

            }

            // 合計数字の算出
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
          // 既に使用済みのカードは使用不可にする
          if (cards[finalI].isStatus()) {
            Log.d("Error", "そのカードは選択できません");
            return true;
          }
          return false;
        }
      });
    }

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SecondPlayerActivity.this);
        builder.setMessage("あなたの数字は「" + sumNumber +"」です。\nよろしければOKを押してください。結果画面に遷移します。")
            .setNegativeButton("Cancel",null)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                // ボタンをクリックしたときの動作
                // ステータスの保存
                SharedPreferences.Editor editor = pref.edit();
                for(int i = 0; i < 10; i++){
                  String key = PLAYER2_INDEX + i;
                  editor.putBoolean(key, cards[i].isStatus());
                }
                editor.putInt(PLAYER2_SCORE,sumNumber);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                startActivity(intent);
                CustomIntent.customType(SecondPlayerActivity.this,"left-to-right");

                finish();


              }
            });
        builder.show();
      }
    });

    // 初期化処理
    init();

  }

  public void init(){

    // カードの初期化(2枚とも白紙に戻す)
    tenImageView.setImageResource(R.drawable.empty);
    oneImageView.setImageResource(R.drawable.empty);

    // 各パラメータの初期化
    tenNumber = oneNumber = sumNumber = 0;
    isUsedOne = isUsedTen = false;

    // 保存したステータス情報を元に裏返しの処理

    for(int i = 0; i < 10; i++){
      boolean tempStatus = pref.getBoolean(PLAYER2_INDEX+i,false);
      Log.d("(fab)index_"+i, String.valueOf(tempStatus));
      if(tempStatus){
        changeView(i);
      }
    }
  }

  // 選択したカードを裏返しにする
  public void changeView(int index) {
    ImageView imageView = (ImageView) linearLayout.getChildAt(index);
    // statusをtrueにする
    cards[index].setStatus(true);
    imageView.setImageResource(cards[index].getBack());
  }



  /**
   * dpからpixelへの変換
   *
   * @param dp
   * @param context
   * @return float pixel
   */
  public static float convertDp2Px(float dp, Context context) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return dp * metrics.density;
  }
}
