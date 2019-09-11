package jp.tomiyama.noir.drag_and_drop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

  // TODO
  // なんかデータあったら初期化するかどうかユーザーに聞く

  private SharedPreferences pref;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);

    pref = getSharedPreferences("numeric_app",MODE_PRIVATE);

    Button button = findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), FirstPlayerActivity.class);
        startActivity(intent);
      }
    });

    Button button2 = findViewById(R.id.button2);
    button2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO アラートダイアログで確認
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

      }
    });
  }
}
