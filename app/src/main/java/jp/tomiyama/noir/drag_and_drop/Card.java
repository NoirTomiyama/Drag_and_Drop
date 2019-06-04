package jp.tomiyama.noir.drag_and_drop;

import android.widget.ImageView;

public class Card {

    // カードが表か裏か
    private boolean status;

    private int front;
    private int back;

    public Card(int front) {
        this.front = front;

        // 初期値代入
        status = false; // 未使用
        back = R.drawable.back;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getFront() {
        return front;
    }

    public int getBack() {
        return back;
    }
}
