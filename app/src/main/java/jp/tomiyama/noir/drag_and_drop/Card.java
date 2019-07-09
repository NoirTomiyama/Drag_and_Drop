package jp.tomiyama.noir.drag_and_drop;

import android.widget.ImageView;

public class Card {

    // カードが表か裏か(未使用->false)
    private boolean status;

    private int front;
    private int back;

    public Card(int front) {
        this.front = front;

        status = false;
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
