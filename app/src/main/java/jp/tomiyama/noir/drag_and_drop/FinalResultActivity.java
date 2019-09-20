package jp.tomiyama.noir.drag_and_drop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FinalResultActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_final_result);

    Intent intent = new Intent();
    Boolean flag;
    TextView textview;
    textview = findViewById(R.id.textView);
    flag = intent.getBooleanExtra("WINNER",false);

    if(flag){
      textview.setText("Player1\nWin");
    }else{
      textview.setText("Player2\nWin");
    }


  }
}
