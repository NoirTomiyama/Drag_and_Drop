package jp.tomiyama.noir.drag_and_drop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import maes.tech.intentanim.CustomIntent;

public class ResultActivity extends AppCompatActivity {

  // TODO
  // 勝敗の管理
  // 遷移先の決定

  private final String PLAYER1_SCORE = "player1_score";
  private final String PLAYER2_SCORE = "player2_score";

  private TextView score1_textView;
  private TextView score2_textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    // 関連付け
    score1_textView = findViewById(R.id.player1_score);
    score2_textView = findViewById(R.id.player2_score);

    // SharedPreferencesの準備
    SharedPreferences pref = getSharedPreferences("numeric_app", MODE_PRIVATE);

    int player1_score = pref.getInt(PLAYER1_SCORE, 0);
    int player2_score = pref.getInt(PLAYER2_SCORE,0);

    // TODO 時間差
    score1_textView.setText(String.valueOf(player1_score));
    score2_textView.setText(String.valueOf(player2_score));

    Button button = findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), FirstPlayerActivity.class);
        startActivity(intent);
        CustomIntent.customType(ResultActivity.this,"right-to-left");

        finish();
      }
    });

  }
}
