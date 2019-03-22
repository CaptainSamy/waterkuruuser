package wssj.co.jp.olioa.widget.likefacebook;

import android.util.Log;

/**
 * Created by Hado on 27-Nov-16.
 */

public class EaseOutBack {

    private final float s;
    private final long duration;
    private final float begin;
    private final float change;
    private final boolean isBegin;

    public EaseOutBack(boolean isBegin, long duration, float begin, float end) {
        this.duration = duration;
        this.isBegin = isBegin;
        this.begin = begin;
        if (isBegin) {
            s = 1.70158f;
            this.change = end - begin + 30;
            Log.d("Tuan", "Change begin " + change);
        } else {
            s = 0.0f;

            this.change = end - begin;
            Log.d("Tuan", "Change end" + change);
        }
    }

    public static EaseOutBack newInstance(long duration, float beginValue, float endValue) {
        return new EaseOutBack(true, duration, beginValue, endValue);
    }

    public static EaseOutBack newInstanceDismiss(long duration, float beginValue, float endValue) {
        return new EaseOutBack(false, duration, beginValue, endValue);
    }

    public float getCoordinateYFromTime(float currentTime) {
        if (isBegin) {
            return (change * ((currentTime = currentTime / duration - 1) * currentTime * ((s + 1) * currentTime + s) + 1) + begin);
        } else {
            return -(change * ((currentTime = currentTime / duration - 1) * currentTime * ((s + 1) * currentTime + s) + 1) + begin);
        }

    }
}
