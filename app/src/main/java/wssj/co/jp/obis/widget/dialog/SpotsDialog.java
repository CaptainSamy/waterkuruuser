package wssj.co.jp.obis.widget.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;

import wssj.co.jp.obis.R;

/**
 * Created by Maxim Dybarsky | maxim.dybarskyy@gmail.com
 * on 13.01.15 at 14:22
 */
public class SpotsDialog {

    private static final int DELAY = 150;

    private static final int DURATION = 1500;

    private int size;

    private AnimatedView[] spots;

    private AnimatorPlayer animator;

    ProgressLayout mProgress;

    private Context mContext;

    public SpotsDialog(Context context, ProgressLayout progress) {
        mContext = context;
        mProgress = progress;
        initProgress();
        onStart();
    }

    public void onStart() {
        animator = new AnimatorPlayer(createAnimations());
        animator.play();
    }

    public void onStop() {
        animator.stop();
    }

    private void initProgress() {
        size = mProgress.getSpotsCount();

        spots = new AnimatedView[size];
        int size = mContext.getResources().getDimensionPixelSize(R.dimen.spot_size);
        int progressWidth = mContext.getResources().getDimensionPixelSize(R.dimen.progress_width);
        for (int i = 0; i < spots.length; i++) {
            AnimatedView v = new AnimatedView(mContext);
            v.setBackgroundResource(R.drawable.dmax_spots_spot);
            v.setTarget(progressWidth);
            v.setXFactor(-1f);
            mProgress.addView(v, size, size);
            spots[i] = v;
        }
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[size];
        for (int i = 0; i < spots.length; i++) {
            Animator move = ObjectAnimator.ofFloat(spots[i], "xFactor", 0, 1);
            move.setDuration(DURATION);
            move.setInterpolator(new HesitateInterpolator());
            move.setStartDelay(DELAY * i);
            animators[i] = move;
        }
        return animators;
    }
}
