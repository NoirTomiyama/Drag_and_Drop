package jp.tomiyama.noir.drag_and_drop;

class Card {

    // カードが表か裏か
    private boolean status;

    private int front;
    private int back;

    Card(int front, int back) {
        this.front = front;
        this.back = back;

        // 未使用
        status = false;
    }

    boolean isStatus() {
        return status;
    }

    void setStatus(boolean status) {
        this.status = status;
    }

    int getFront() {
        return front;
    }

    int getBack() {
        return back;
    }
}
