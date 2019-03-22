package wssj.co.jp.olioa.widget.likefacebook;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.utils.Constants;

import static wssj.co.jp.olioa.widget.likefacebook.CommonDimen.DIVIDE;

/**
 * Created by Hado on 26-Nov-16.
 */

public class ReactionView extends View {

    enum StateDraw {
        BEGIN,
        CHOOSING,
        NORMAL,
        DISMISS
    }

    public static final long DURATION_ANIMATION = 200;

    public static final long DURATION_BEGINNING_EACH_ITEM = 180;

    public static final long DURATION_BEGINNING_ANIMATION = 600;

    private IListenerClickIconLike mItemClickIconLike;

    private EaseOutBack easeOutBack;

    private Board board;

    private static Emotion[] emotions = new Emotion[6];

    private StateDraw state = StateDraw.BEGIN;

    private int currentPosition = 0;

    public ReactionView(Context context) {
        super(context);
        init();
    }

    public ReactionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReactionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        board = new Board(getContext());
        setLayerType(LAYER_TYPE_SOFTWARE, board.boardPaint);
        if (ReactionView.emotions[0] == null) {
            ReactionView.emotions[0] = new Emotion(getContext(), Constants.Like.LIKE, R.drawable.like);
            ReactionView.emotions[1] = new Emotion(getContext(), Constants.Like.LOVE, R.drawable.love);
            ReactionView.emotions[2] = new Emotion(getContext(), Constants.Like.HAHA, R.drawable.haha);
            ReactionView.emotions[3] = new Emotion(getContext(), Constants.Like.WOW, R.drawable.wow);
            ReactionView.emotions[4] = new Emotion(getContext(), Constants.Like.CRY, R.drawable.cry);
            ReactionView.emotions[5] = new Emotion(getContext(), Constants.Like.ANGRY, R.drawable.angry);
        }
        initElement();
    }

    private void initElement() {
        board.currentY = CommonDimen.HEIGHT_VIEW_REACTION + 10;
        for (Emotion e : ReactionView.emotions) {
            e.currentY = board.currentY + DIVIDE;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (state != null) {
            board.drawBoard(canvas);
            ReactionView.emotions[0].drawEmotion(canvas);
            ReactionView.emotions[1].drawEmotion(canvas);
            ReactionView.emotions[2].drawEmotion(canvas);
            ReactionView.emotions[3].drawEmotion(canvas);
            ReactionView.emotions[4].drawEmotion(canvas);
            ReactionView.emotions[5].drawEmotion(canvas);
//            for (Emotion emotion : ReactionView.emotions) {
//                emotion.drawEmotion(canvas);
//            }
        }
    }

    private void calculateInSessionDismiss(float interpolatedTime) {
        float currentTime = interpolatedTime * 300;

//        if (currentTime > 0) {
        board.currentY = board.endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 120, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 100) {
        ReactionView.emotions[0].currentY = ReactionView.emotions[0].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 100, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 200) {
        ReactionView.emotions[1].currentY = ReactionView.emotions[1].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 80, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 300) {
        ReactionView.emotions[2].currentY = ReactionView.emotions[2].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 60, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 400) {
        ReactionView.emotions[3].currentY = ReactionView.emotions[3].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 40, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 500) {
        ReactionView.emotions[4].currentY = ReactionView.emotions[4].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 20, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 600) {
        ReactionView.emotions[5].currentY = ReactionView.emotions[5].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime, DURATION_BEGINNING_EACH_ITEM));
//        }

        invalidate();
    }

    private void beforeAnimateDismiss() {
        board.beginHeight = Board.BOARD_HEIGHT_NORMAL;
        board.endHeight = Board.BOARD_HEIGHT_NORMAL;

        board.beginY = Board.BOARD_BOTTOM;
        board.endY = Board.BOARD_BOTTOM - 10;

        easeOutBack = EaseOutBack.newInstanceDismiss(DURATION_BEGINNING_EACH_ITEM, Math.abs(board.beginY - board.endY), 0);

        for (int i = 0; i < ReactionView.emotions.length; i++) {
            ReactionView.emotions[i].beginY = Board.BASE_LINE - 10;//Board.BOARD_BOTTOM + 250;
            ReactionView.emotions[i].endY = Board.BOARD_BOTTOM;//Board.BASE_LINE + 250;
            ReactionView.emotions[i].currentX = i == 0 ? Board.BOARD_X + DIVIDE : ReactionView.emotions[i - 1].currentX + ReactionView.emotions[i - 1].currentSize + DIVIDE;
        }
    }

    private void beforeAnimateBeginning() {
        board.beginHeight = Board.BOARD_HEIGHT_NORMAL;
        board.endHeight = Board.BOARD_HEIGHT_NORMAL;

        board.beginY = Board.BOARD_BOTTOM + 10;
        board.endY = Board.BOARD_Y;

        easeOutBack = EaseOutBack.newInstance(DURATION_BEGINNING_EACH_ITEM, Math.abs(board.beginY - board.endY), 0);

        for (int i = 0; i < ReactionView.emotions.length; i++) {
            ReactionView.emotions[i].endY = Board.BASE_LINE - Emotion.NORMAL_SIZE;
            ReactionView.emotions[i].beginY = Board.BOARD_BOTTOM + 10;
            ReactionView.emotions[i].currentX = i == 0 ? Board.BOARD_X + DIVIDE : ReactionView.emotions[i - 1].currentX + ReactionView.emotions[i - 1].currentSize + DIVIDE;
        }
    }

    private void beforeAnimateChoosing() {
        board.beginHeight = board.getCurrentHeight();
        board.endHeight = Board.BOARD_HEIGHT_MINIMAL;

        for (int i = 0; i < ReactionView.emotions.length; i++) {
            ReactionView.emotions[i].beginSize = ReactionView.emotions[i].currentSize;

            if (i == currentPosition) {
                ReactionView.emotions[i].endSize = Emotion.CHOOSE_SIZE;
            } else {
                ReactionView.emotions[i].endSize = Emotion.MINIMAL_SIZE;
            }
        }
    }

    private void beforeAnimateNormalBack() {
        board.beginHeight = board.getCurrentHeight();
        board.endHeight = Board.BOARD_HEIGHT_NORMAL;

        for (int i = 0; i < ReactionView.emotions.length; i++) {
            ReactionView.emotions[i].beginSize = ReactionView.emotions[i].currentSize;
            ReactionView.emotions[i].endSize = Emotion.NORMAL_SIZE;
        }
    }

    private void calculateInSessionChoosingAndEnding(float interpolatedTime) {
        board.setCurrentHeight(board.beginHeight + (int) (interpolatedTime * (board.endHeight - board.beginHeight)));

        for (int i = 0; i < ReactionView.emotions.length; i++) {
            ReactionView.emotions[i].currentSize = calculateSize(i, interpolatedTime);
            ReactionView.emotions[i].currentY = Board.BASE_LINE - ReactionView.emotions[i].currentSize;
        }
        calculateCoordinateX();
        invalidate();
    }

    private void calculateInSessionBeginning(float interpolatedTime) {
        float currentTime = interpolatedTime * 300;

        float calculate = Math.min(currentTime, DURATION_BEGINNING_EACH_ITEM);
//        float additionY = easeOutBack.getCoordinateYFromTime(calculate);
//        Log.d("Tuan", "calculateInSessionBeginning " + calculate + " - " + additionY);
////        if (currentTime > 0) {
//        board.currentY = board.endY + additionY;
////        }
//
////        if (currentTime >= 100) {
//        ReactionView.emotions[0].currentY = ReactionView.emotions[0].endY + additionY;
////        }
//
////        if (currentTime >= 200) {
//        ReactionView.emotions[1].currentY = ReactionView.emotions[1].endY + additionY;
////        }
//
////        if (currentTime >= 300) {
//        ReactionView.emotions[2].currentY = ReactionView.emotions[2].endY + additionY;
////        }
//
////        if (currentTime >= 400) {
//        ReactionView.emotions[3].currentY = ReactionView.emotions[3].endY + additionY;
////        }
//
////        if (currentTime >= 500) {
//        ReactionView.emotions[4].currentY = ReactionView.emotions[4].endY + additionY;
////        }
//
////        if (currentTime >= 600) {
//        ReactionView.emotions[5].currentY = ReactionView.emotions[5].endY + additionY;
////        }

//        if (currentTime > 0) {
        board.currentY = board.endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime, DURATION_BEGINNING_EACH_ITEM));
//        }
//
//        if (currentTime >= 20) {
        ReactionView.emotions[0].currentY = ReactionView.emotions[0].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 20, DURATION_BEGINNING_EACH_ITEM));
//        }
//
//        if (currentTime >= 40) {
        ReactionView.emotions[1].currentY = ReactionView.emotions[1].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 40, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 60) {
        ReactionView.emotions[2].currentY = ReactionView.emotions[2].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 60, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 80) {
        ReactionView.emotions[3].currentY = ReactionView.emotions[3].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 80, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 100) {
        ReactionView.emotions[4].currentY = ReactionView.emotions[4].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 100, DURATION_BEGINNING_EACH_ITEM));
//        }

//        if (currentTime >= 120) {
        ReactionView.emotions[5].currentY = ReactionView.emotions[5].endY + easeOutBack.getCoordinateYFromTime(Math.min(currentTime - 120, DURATION_BEGINNING_EACH_ITEM));
//        }

        invalidate();
    }

    private int calculateSize(int position, float interpolatedTime) {
        int changeSize = ReactionView.emotions[position].endSize - ReactionView.emotions[position].beginSize;
        return ReactionView.emotions[position].beginSize + (int) (interpolatedTime * changeSize);
    }

    private void calculateCoordinateX() {
        ReactionView.emotions[0].currentX = Board.BOARD_X + DIVIDE;
        ReactionView.emotions[ReactionView.emotions.length - 1].currentX = Board.BOARD_X + Board.BOARD_WIDTH - DIVIDE - ReactionView.emotions[ReactionView.emotions.length - 1].currentSize;

        for (int i = 1; i < currentPosition; i++) {
            ReactionView.emotions[i].currentX = ReactionView.emotions[i - 1].currentX + ReactionView.emotions[i - 1].currentSize + DIVIDE;
        }

        for (int i = ReactionView.emotions.length - 2; i > currentPosition; i--) {
            ReactionView.emotions[i].currentX = ReactionView.emotions[i + 1].currentX - ReactionView.emotions[i].currentSize - DIVIDE;
        }

        if (currentPosition != 0 && currentPosition != ReactionView.emotions.length - 1) {
            if (currentPosition <= (ReactionView.emotions.length / 2 - 1)) {
                ReactionView.emotions[currentPosition].currentX = ReactionView.emotions[currentPosition - 1].currentX + ReactionView.emotions[currentPosition - 1].currentSize + DIVIDE;
            } else {
                ReactionView.emotions[currentPosition].currentX = ReactionView.emotions[currentPosition + 1].currentX - ReactionView.emotions[currentPosition].currentSize - DIVIDE;
            }
        }
    }

    public void show() {
        state = StateDraw.BEGIN;
        setVisibility(VISIBLE);
        beforeAnimateBeginning();
        startAnimation(new BeginningAnimation());
    }

    public void dismiss() {
        state = StateDraw.DISMISS;
        setVisibility(INVISIBLE);
        beforeAnimateDismiss();
        startAnimation(new DismissAnimation());
    }

    private void selected(int position) {
        if (currentPosition == position && state == StateDraw.CHOOSING) return;
        state = StateDraw.CHOOSING;
        currentPosition = position;
        startAnimation(new ChooseEmotionAnimation());
    }

    public void backToNormal() {
        state = StateDraw.NORMAL;
        startAnimation(new ChooseEmotionAnimation());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handled = true;
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < ReactionView.emotions.length; i++) {
                    if (event.getX() > ReactionView.emotions[i].currentX && event.getX() < ReactionView.emotions[i].currentX + ReactionView.emotions[i].currentSize) {
                        selected(i);
                        break;
                    }
                }
                handled = true;
                break;
            case MotionEvent.ACTION_UP:
                Log.d("onTouchEvent", "ACTION_UP");
                for (int i = 0; i < ReactionView.emotions.length; i++) {
                    if (event.getX() > ReactionView.emotions[i].currentX && event.getX() < ReactionView.emotions[i].currentX + ReactionView.emotions[i].currentSize) {
                        selected(i);
                        Log.d("onTouchEvent", "ACTION_UP---------");
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                state = StateDraw.DISMISS;
                                setVisibility(INVISIBLE);
                                beforeAnimateDismiss();
                                startAnimation(new DismissAnimation());
                            }
                        }, 600);
                        if (mItemClickIconLike != null) {
                            mItemClickIconLike.onItemClick(i + 1);
                        }
                    }
                }
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        backToNormal();
                    }
                }, DURATION_ANIMATION);
                handled = true;
                break;
        }
        return handled;
    }

    class ChooseEmotionAnimation extends Animation {

        public ChooseEmotionAnimation() {
            if (state == StateDraw.CHOOSING) {
                beforeAnimateChoosing();
            } else if (state == StateDraw.NORMAL) {
                beforeAnimateNormalBack();
            }
            setDuration(DURATION_ANIMATION);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            calculateInSessionChoosingAndEnding(interpolatedTime);
        }
    }

    class BeginningAnimation extends Animation {

        public BeginningAnimation() {
            beforeAnimateBeginning();
            setDuration(DURATION_BEGINNING_ANIMATION);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Log.d("Tuan", "#applyTransformation " + interpolatedTime);
            calculateInSessionBeginning(interpolatedTime);
        }
    }

    class DismissAnimation extends Animation {

        public DismissAnimation() {
            beforeAnimateDismiss();
            setDuration(DURATION_BEGINNING_ANIMATION);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            calculateInSessionDismiss(interpolatedTime);
        }
    }

    public interface IListenerClickIconLike {

        void onItemClick(int itemId);
    }

    public void setItemIconLikeClick(IListenerClickIconLike itemClickIconLike) {
        this.mItemClickIconLike = itemClickIconLike;
    }
}
