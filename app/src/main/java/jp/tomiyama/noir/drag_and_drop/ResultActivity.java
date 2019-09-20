package jp.tomiyama.noir.drag_and_drop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class ResultActivity extends AppCompatActivity {

  // TODO
  // 勝敗の管理
  // 遷移先の決定

  private final String PLAYER1_SCORE = "player1_score";
  private final String PLAYER2_SCORE = "player2_score";

  private final String WIN_NUMBER = "win_number";
  private final String LOSE_NUMBER = "lose_number";

  private final String TURN_NUMBER = "turn_number";

  private TextView score1_textView;
  private TextView score2_textView;

  private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
  private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

  // player1の状態だけ管理
  private int winNumber = 0;
  private int loseNumber = 0;

  private int turnNumber = 0;

  private Handler handler;

  private int[] resources = new int[]{
      R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three,
      R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
      R.drawable.eight, R.drawable.nine
  };

  private ImageView[] imageViews;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    // 関連付け
    score1_textView = findViewById(R.id.player1_score);
    score2_textView = findViewById(R.id.player2_score);

    imageViews = new ImageView[]{
        findViewById(R.id.imageView),
        findViewById(R.id.imageView2),
        findViewById(R.id.imageView3),
        findViewById(R.id.imageView4)
    };

    // SharedPreferencesの準備
    SharedPreferences pref = getSharedPreferences("numeric_app", MODE_PRIVATE);

    final int[] player1_score = {pref.getInt(PLAYER1_SCORE, 0)};
    final int[] player2_score = {pref.getInt(PLAYER2_SCORE,0)};

    winNumber = pref.getInt(WIN_NUMBER,0);
    loseNumber = pref.getInt(LOSE_NUMBER,0);
    turnNumber = pref.getInt(TURN_NUMBER,0);

//    Display display = getWindowManager().getDefaultDisplay();
//    Point p = new Point();
//    display.getSize(p);
//
//    Log.d("p", String.valueOf(p));


    if(player1_score[0] > player2_score[0]){
      winNumber ++;
    }else{
      loseNumber ++;
    }
    turnNumber++;

    handler = new Handler();

    // TODO 時間差で表示
    handler.postDelayed(() -> {

      if(player1_score[0] > player2_score[0]){
        score1_textView.setText("Player1 Win");
        score2_textView.setText("Player2 Lose");
      }else{
        score1_textView.setText("Player1 Lose");
        score2_textView.setText("Player2 Win");
      }

      List<Integer> digits = new ArrayList<>();

      while(player1_score[0] > 0) {
        digits.add(player1_score[0] % 10);
        player1_score[0] /= 10;
      }

      while(player2_score[0] > 0) {
        digits.add(player2_score[0] % 10);
        player2_score[0] /= 10;
      }

      for(int i = 0; i < 4; i++){
        imageViews[i].setImageResource(resources[digits.get(i)]);
      }

      digits.clear();

      // 画像に置き換える
    },1500);


    Button button = findViewById(R.id.button);

    if(turnNumber == 5) button.setText("Final Result");

    button.setOnClickListener(v -> {

      // 5回勝負したら
      if(turnNumber == 5){
        // 終了
        // finishActivityに遷移

      }else{
        Intent intent = new Intent(getApplicationContext(), FirstPlayerActivity.class);
        startActivity(intent);
        CustomIntent.customType(ResultActivity.this,"right-to-left");

        // win,loseの数の保存

      }

      Log.d("win", String.valueOf(winNumber));
      Log.d("lose", String.valueOf(loseNumber));

      SharedPreferences.Editor editor = pref.edit();
      editor.putInt(WIN_NUMBER,winNumber);
      editor.putInt(LOSE_NUMBER,loseNumber);
      editor.putInt(TURN_NUMBER,turnNumber);
      editor.apply();

      finish();

    });

  }

}
