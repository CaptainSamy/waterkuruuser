package wssj.co.jp.point.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wssj.co.jp.point.R;
import wssj.co.jp.point.utils.Logger;

public class CircularProgressBar extends View {

    public interface IOnAnimateListener {

        public void onAnimateDone();
    }

    /**
     * TAG constant for logging
     */
    private static final String TAG = CircularProgressBar.class.getSimpleName();

    /**
     * used to save the super state on configuration change
     */
    private static final String INSTANCE_STATE_SAVEDSTATE = "saved_state";

    /**
     * used to save the progress on configuration changes
     */
    private static final String INSTANCE_STATE_PROGRESS = "progress";

    /**
     * used to save the marker progress on configuration changes
     */
    private static final String INSTANCE_STATE_MARKER_PROGRESS = "marker_progress";

    /**
     * used to save the background color of the progress
     */
    private static final String INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR
            = "progress_background_color";

    /**
     * used to save the color of the progress
     */
    private static final String INSTANCE_STATE_PROGRESS_COLOR = "progress_color";

    /**
     * used to save and restore the visibility of the thumb in this instance
     */
    private static final String INSTANCE_STATE_THUMB_VISIBLE = "thumb_visible";

    /**
     * used to save and restore the visibility of the marker in this instance
     */
    private static final String INSTANCE_STATE_MARKER_VISIBLE = "marker_visible";

    /**
     * The rectangle enclosing the circle.
     */
    private final RectF mCircleBounds = new RectF();

    /**
     * the rect for the thumb square
     */
    private final RectF mSquareRect = new RectF();

    /**
     * the paint for the background.
     */
    private Paint mBackgroundColorPaint = new Paint();

    /**
     * The stroke width used to paint the circle.
     */
    private int mCircleStrokeWidth = 7;

    /**
     * The gravity of the view. Where should the Circle be drawn within the given bounds
     * <p/>
     * {@link #computeInsets(int, int)}
     */
    private int mGravity = Gravity.CENTER;

    /**
     * The Horizontal inset calcualted in {@link #computeInsets(int, int)} depends on {@link
     * #mGravity}.
     */
    private int mHorizontalInset = 0;

    /**
     * true if not all properties are set. then the view isn't drawn and there are no errors in the
     * LayoutEditor
     */
    private boolean mIsInitializing = true;

    /**
     * flag if the marker should be visible
     */
    private boolean mIsMarkerEnabled = false;

    /**
     * indicates if the thumb is visible
     */
    private boolean mIsThumbEnabled = true;

    /**
     * The Marker color paint.
     */
    private Paint mMarkerColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    ;

    /**
     * The Marker progress.
     */
    private float mMarkerProgress = 0.0f;

    /**
     * the overdraw is true if the progress is over 1.0.
     */
    private boolean mOverrdraw = false;

    /**
     * The current progress.
     */
    private float mProgress = 0.3f;

    /**
     * The color of the progress background.
     */
    private int mProgressBackgroundColor;

    /**
     * the color of the progress.
     */
    private int mProgressColor;

    /**
     * paint for the progress.
     */
    private Paint mProgressColorPaint;

    /**
     * Radius of the circle
     * <p/>
     * <p> Note: (Re)calculated in {@link #onMeasure(int, int)}. </p>
     */
    private float mRadius;

    /**
     * The Thumb color paint.
     */
    private Paint mThumbColorPaint = new Paint();

    /**
     * The Thumb pos x.
     * <p/>
     * Care. the position is not the position of the rotated thumb. The position is only calculated
     * in {@link #onMeasure(int, int)}
     */
    private float mThumbPosX;

    /**
     * The Thumb pos y.
     * <p/>
     * Care. the position is not the position of the rotated thumb. The position is only calculated
     * in {@link #onMeasure(int, int)}
     */
    private float mThumbPosY;

    /**
     * The pointer width (in pixels).
     */
    private int mThumbRadius = 20;

    /**
     * The Translation offset x which gives us the ability to use our own coordinates system.
     */
    private float mTranslationOffsetX;

    /**
     * The Translation offset y which gives us the ability to use our own coordinates system.
     */
    private float mTranslationOffsetY;

    /**
     * The Vertical inset calcualted in {@link #computeInsets(int, int)} depends on {@link
     * #mGravity}..
     */
    private int mVerticalInset = 0;

    private int mPinWidth, mPinHeight;

    private boolean mIsFirstTime = true;

    private Bitmap mPinOn, mPinOff;

    private IOnAnimateListener mOnAnimateListener;

    /**
     * Instantiates a new holo circular progress bar.
     *
     * @param context the context
     */
    public CircularProgressBar(final Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new holo circular progress bar.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CircularProgressBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.circularProgressBarStyle);
    }

    /**
     * Instantiates a new holo circular progress bar.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public CircularProgressBar(final Context context, final AttributeSet attrs,
                               final int defStyle) {
        super(context, attrs, defStyle);

        // load the styled attributes and set their properties
        final TypedArray attributes = context
                .obtainStyledAttributes(attrs, R.styleable.HoloCircularProgressBar,
                        defStyle, 0);
        mCircleStrokeWidth = convertDpToPixel(context, 3);
        if (attributes != null) {
            try {
                setProgressColor(attributes
                        .getColor(R.styleable.HoloCircularProgressBar_progress_color, Color.CYAN));
                setProgressBackgroundColor(attributes
                        .getColor(R.styleable.HoloCircularProgressBar_progress_background_color,
                                Color.GREEN));
                setProgress(
                        attributes.getFloat(R.styleable.HoloCircularProgressBar_progress, 0.0f));
                setMarkerProgress(
                        attributes.getFloat(R.styleable.HoloCircularProgressBar_marker_progress,
                                0.0f));
//                setWheelSize((int) attributes
//                        .getDimension(R.styleable.HoloCircularProgressBar_stroke_width, 5));
                setThumbEnabled(attributes
                        .getBoolean(R.styleable.HoloCircularProgressBar_thumb_visible, true));
                setMarkerEnabled(attributes
                        .getBoolean(R.styleable.HoloCircularProgressBar_marker_visible, true));

                mGravity = attributes
                        .getInt(R.styleable.HoloCircularProgressBar_android_gravity,
                                Gravity.CENTER);
            } finally {
                // make sure recycle is always called.
                attributes.recycle();
            }
        }

        mThumbRadius = mCircleStrokeWidth * 2;


        initBitmapPin();


        updateBackgroundColor();

        updateMarkerColor();

        updateProgressColor();
        mIsFirstTime = false;
//        startAnimationAddPin();

        // the view has now all properties and can be drawn
        mIsInitializing = false;

    }

    private void initBitmapPin() {
        if (mPinOn != null) {
            return;
        }
        mPinOn = BitmapFactory.decodeResource(getResources(),
                R.drawable.circle_on);
        mPinOff = BitmapFactory.decodeResource(getResources(),
                R.drawable.circle_off);
        mPinWidth = mPinOn.getWidth();
        mPinHeight = mPinOn.getHeight();
    }

    private List<PositionPin> mListPosition = new ArrayList<>();

    private void initPositionPin() {
        if (mListPosition.size() > 0) {
            return;
        }
        drawCirclePoints(10, mRadius);
    }

    void drawCirclePoints(int points, double radius) {
        double slice = 2 * Math.PI / points;
        for (int i = 0; i < points; i++) {
            double angle = slice * i - Math.PI / 2;
            Log.d(TAG, "angle " + angle);
            int newX = (int) (radius * Math.cos(angle)) - mPinWidth / 2;
            int newY = (int) (radius * Math.sin(angle)) - mPinHeight / 2;
            mListPosition.add(new PositionPin(newX, newY));
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {

        // All of our positions are using our internal coordinate system.
        // Instead of translating
        // them we let Canvas do the work for us.
        canvas.translate(mTranslationOffsetX, mTranslationOffsetY);

        final float progressRotation = getCurrentRotation();

//        // draw the background
//        if (!mOverrdraw) {
//            canvas.drawArc(mCircleBounds, 270, -(360 - progressRotation), false,
//                    mBackgroundColorPaint);
//        }

        // draw the progress or a full circle if overdraw is true
        canvas.drawArc(mCircleBounds, 270, mOverrdraw ? 360 : progressRotation, false,
                mProgressColorPaint);

        // draw the marker at the correct rotated position
        if (true) {
            canvas.save();
            mMarkerColorPaint.setAntiAlias(true);
            if (mProgressPin <= 10) {
                if (mProgressPin >= 1) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(0).getLeft(), mListPosition.get(0).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(0).getLeft(), mListPosition.get(0).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 2) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(1).getLeft(), mListPosition.get(1).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(1).getLeft(), mListPosition.get(1).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 3) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(2).getLeft(), mListPosition.get(2).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(2).getLeft(), mListPosition.get(2).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 4) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(3).getLeft(), mListPosition.get(3).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(3).getLeft(), mListPosition.get(3).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 5) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(4).getLeft(), mListPosition.get(4).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(4).getLeft(), mListPosition.get(4).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 6) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(5).getLeft(), mListPosition.get(5).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(5).getLeft(), mListPosition.get(5).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 7) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(6).getLeft(), mListPosition.get(6).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(6).getLeft(), mListPosition.get(6).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 8) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(7).getLeft(), mListPosition.get(7).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(7).getLeft(), mListPosition.get(7).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 9) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(8).getLeft(), mListPosition.get(8).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(8).getLeft(), mListPosition.get(8).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 10) {
                    canvas.drawBitmap(mPinOn, mListPosition.get(9).getLeft(), mListPosition.get(9).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOff, mListPosition.get(9).getLeft(), mListPosition.get(9).getTop(), mMarkerColorPaint);
                }
            } else {

                if (mProgressPin >= 11) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(0).getLeft(), mListPosition.get(0).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(0).getLeft(), mListPosition.get(0).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 12) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(1).getLeft(), mListPosition.get(1).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(1).getLeft(), mListPosition.get(1).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 13) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(2).getLeft(), mListPosition.get(2).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(2).getLeft(), mListPosition.get(2).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 14) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(3).getLeft(), mListPosition.get(3).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(3).getLeft(), mListPosition.get(3).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 15) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(4).getLeft(), mListPosition.get(4).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(4).getLeft(), mListPosition.get(4).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 16) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(5).getLeft(), mListPosition.get(5).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(5).getLeft(), mListPosition.get(5).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 17) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(6).getLeft(), mListPosition.get(6).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(6).getLeft(), mListPosition.get(6).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 18) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(7).getLeft(), mListPosition.get(7).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(7).getLeft(), mListPosition.get(7).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 19) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(8).getLeft(), mListPosition.get(8).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(8).getLeft(), mListPosition.get(8).getTop(), mMarkerColorPaint);
                }

                if (mProgressPin >= 20) {
                    canvas.drawBitmap(mPinOff, mListPosition.get(9).getLeft(), mListPosition.get(9).getTop(), mMarkerColorPaint);
                } else {
                    canvas.drawBitmap(mPinOn, mListPosition.get(9).getLeft(), mListPosition.get(9).getTop(), mMarkerColorPaint);
                }
            }
            canvas.restore();
        }
    }

    private Random mRandom = new Random();

    class PositionPin {

        public float mLeft;

        public float mTop;

        public PositionPin(float left, float top) {
            mLeft = left;
            mTop = top;
        }

        public float getLeft() {
            return mLeft;
        }

        public float getTop() {
            return mTop;
        }
    }

    public void setAnimateListener(IOnAnimateListener callback) {
        mOnAnimateListener = callback;
        isCallback = true;
    }

    private boolean isDrawPin;

    private boolean isCallback;

    private int mProgressPin;

    private int mDurationAddPin = 5000;

    private int mDurationProgress = 8000;

    public void setDurationAddPin(int durationAddPin) {
        mDurationAddPin = durationAddPin;
    }

    public void setDurationProgress(int durationProgress) {
        mDurationProgress = durationProgress;
    }

    private void startAnimationAddPin() {
        isDrawPin = true;
        setProgress(0.0f);
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(0, 21);
        valueAnimator.setDuration(mDurationAddPin);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                if (value == 10) {
                    if (mRepeatCount == 0) {
                        Logger.d(TAG, "#AnimationAddPin -> repeat count 0 -> 10");
                        mProgressPin = value;
                        invalidate();
                        mOnAnimateListener.onAnimateDone();
                        valueAnimator.removeAllUpdateListeners();
                        valueAnimator.cancel();
                    }
                }
                switch (value) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                        invalidate();
                        mProgressPin = value;
                        break;
                    case 21:

                        if (mRepeatCount != 0) {
                            mRepeatCount--;
                            startAnimationAddPin();
                            Logger.d(TAG, "#AnimationAddPin -> repeat count " + mRepeatCount);
                        } else {
                            startAnimationAddPin();
                            Logger.d(TAG, "#AnimationAddPin -> repeat count 0 -> 21");
                        }
                        break;
                }
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private void startAnimationDone() {
        final ValueAnimator valueAnimator = ObjectAnimator.ofInt(0, 11);
        valueAnimator.setDuration(mDurationAddPin / 2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) valueAnimator.getAnimatedValue();
                switch (value) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        invalidate();
                        mProgressPin = value;
                        break;
                    case 11:
                        mOnAnimateListener.onAnimateDone();
                        break;
                }
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private int mRepeatCount;

    public static final int REPEAD_INFINITY = -1;

    public void repeatAnimation(int repeatCount) {
        mRepeatCount = repeatCount;
        mIsFirstTime = true;
        startAnimationAddPin();

    }

    public void setRepeat(int repeatCount) {
        mRepeatCount = repeatCount;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int height = getDefaultSize(
                getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom(),
                heightMeasureSpec);
        final int width = getDefaultSize(
                getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight(),
                widthMeasureSpec);

        final int diameter;
        if (heightMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // ScrollView
            diameter = width;
            computeInsets(0, 0);
        } else if (widthMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // HorizontalScrollView
            diameter = height;
            computeInsets(0, 0);
        } else {
            // Default
            diameter = Math.min(width, height);
            computeInsets(width - diameter, height - diameter);
        }

        setMeasuredDimension(diameter, diameter);

        final float halfWidth = diameter * 0.5f;

        // width of the drawed circle (+ the drawedThumb)
        final float drawedWith;
        if (isThumbEnabled()) {
            drawedWith = mThumbRadius * (5f / 6f);
        } else if (isMarkerEnabled()) {
            drawedWith = mCircleStrokeWidth * 1.4f;
        } else {
            drawedWith = mCircleStrokeWidth / 2f;
        }

        // -0.5f for pixel perfect fit inside the viewbounds
        mRadius = halfWidth - drawedWith - 0.5f - mPinHeight / 2;
        initPositionPin();
        mCircleBounds.set(-mRadius, -mRadius, mRadius, mRadius);

        mThumbPosX = (float) (mRadius * Math.cos(0));
        mThumbPosY = (float) (mRadius * Math.sin(0));

        mTranslationOffsetX = halfWidth + mHorizontalInset;
        mTranslationOffsetY = halfWidth + mVerticalInset;

    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            setProgress(bundle.getFloat(INSTANCE_STATE_PROGRESS));
            setMarkerProgress(bundle.getFloat(INSTANCE_STATE_MARKER_PROGRESS));

            final int progressColor = bundle.getInt(INSTANCE_STATE_PROGRESS_COLOR);
            if (progressColor != mProgressColor) {
                mProgressColor = progressColor;
                updateProgressColor();
            }

            final int progressBackgroundColor = bundle
                    .getInt(INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR);
            if (progressBackgroundColor != mProgressBackgroundColor) {
                mProgressBackgroundColor = progressBackgroundColor;
                updateBackgroundColor();
            }

            mIsThumbEnabled = bundle.getBoolean(INSTANCE_STATE_THUMB_VISIBLE);

            mIsMarkerEnabled = bundle.getBoolean(INSTANCE_STATE_MARKER_VISIBLE);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE_SAVEDSTATE));
            return;
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE_SAVEDSTATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STATE_PROGRESS, mProgress);
        bundle.putFloat(INSTANCE_STATE_MARKER_PROGRESS, mMarkerProgress);
        bundle.putInt(INSTANCE_STATE_PROGRESS_COLOR, mProgressColor);
        bundle.putInt(INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR, mProgressBackgroundColor);
        bundle.putBoolean(INSTANCE_STATE_THUMB_VISIBLE, mIsThumbEnabled);
        bundle.putBoolean(INSTANCE_STATE_MARKER_VISIBLE, mIsMarkerEnabled);
        return bundle;
    }

    public int getCircleStrokeWidth() {
        return mCircleStrokeWidth;
    }

    /**
     * similar to {@link #getProgress}
     */
    public float getMarkerProgress() {
        return mMarkerProgress;
    }

    /**
     * gives the current progress of the ProgressBar. Value between 0..1 if you set the progress to
     * >1 you'll get progress % 1 as return value
     *
     * @return the progress
     */
    public float getProgress() {
        return mProgress;
    }

    /**
     * Gets the progress color.
     *
     * @return the progress color
     */
    public int getProgressColor() {
        return mProgressColor;
    }

    /**
     * @return true if the marker is visible
     */
    public boolean isMarkerEnabled() {
        return mIsMarkerEnabled;
    }

    /**
     * @return true if the marker is visible
     */
    public boolean isThumbEnabled() {
        return mIsThumbEnabled;
    }

    /**
     * Sets the marker enabled.
     *
     * @param enabled the new marker enabled
     */
    public void setMarkerEnabled(final boolean enabled) {
        mIsMarkerEnabled = enabled;
    }

    /**
     * Sets the marker progress.
     *
     * @param progress the new marker progress
     */
    public void setMarkerProgress(final float progress) {
        mIsMarkerEnabled = true;
        mMarkerProgress = progress;
    }

    /**
     * Sets the progress.
     *
     * @param progress the new progress
     */
    public void setProgress(final float progress) {
        if (progress == mProgress) {
            return;
        }

        if (progress == 1) {
            mOverrdraw = false;
            mProgress = 1;
        } else {

            if (progress >= 1) {
                mOverrdraw = true;
            } else {
                mOverrdraw = false;
            }

            mProgress = progress % 1.0f;
        }

        if (!mIsInitializing) {
            invalidate();
        }
    }

    /**
     * Sets the progress background color.
     *
     * @param color the new progress background color
     */
    public void setProgressBackgroundColor(final int color) {
        mProgressBackgroundColor = color;

        updateMarkerColor();
        updateBackgroundColor();
    }

    /**
     * Sets the progress color.
     *
     * @param color the new progress color
     */
    public void setProgressColor(final int color) {
        mProgressColor = color;

        updateProgressColor();
    }

    /**
     * shows or hides the thumb of the progress bar
     *
     * @param enabled true to show the thumb
     */
    public void setThumbEnabled(final boolean enabled) {
        mIsThumbEnabled = enabled;
    }

    /**
     * Sets the wheel size.
     *
     * @param dimension the new wheel size
     */
    public void setWheelSize(final int dimension) {
        mCircleStrokeWidth = dimension;

        // update the paints
        updateBackgroundColor();
        updateMarkerColor();
        updateProgressColor();
    }

    /**
     * Compute insets.
     * <p/>
     * <pre>
     *  ______________________
     * |_________dx/2_________|
     * |......| /'''''\|......|
     * |-dx/2-|| View ||-dx/2-|
     * |______| \_____/|______|
     * |________ dx/2_________|
     * </pre>
     *
     * @param dx the dx the horizontal unfilled space
     * @param dy the dy the horizontal unfilled space
     */
    @SuppressLint("NewApi")
    private void computeInsets(final int dx, final int dy) {
        int absoluteGravity = mGravity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            absoluteGravity = Gravity.getAbsoluteGravity(mGravity, getLayoutDirection());
        }

        switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                mHorizontalInset = 0;
                break;
            case Gravity.RIGHT:
                mHorizontalInset = dx;
                break;
            case Gravity.CENTER_HORIZONTAL:
            default:
                mHorizontalInset = dx / 2;
                break;
        }
        switch (absoluteGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                mVerticalInset = 0;
                break;
            case Gravity.BOTTOM:
                mVerticalInset = dy;
                break;
            case Gravity.CENTER_VERTICAL:
            default:
                mVerticalInset = dy / 2;
                break;
        }
    }

    /**
     * Gets the current rotation.
     *
     * @return the current rotation
     */
    private float getCurrentRotation() {
        return 360 * mProgress;
    }

    /**
     * Gets the marker rotation.
     *
     * @return the marker rotation
     */
    private float getMarkerRotation() {
        return 360 * mMarkerProgress;
    }

    /**
     * updates the paint of the background
     */
    private void updateBackgroundColor() {
        mBackgroundColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundColorPaint.setColor(mProgressBackgroundColor);
        mBackgroundColorPaint.setStyle(Paint.Style.STROKE);
        mBackgroundColorPaint.setPathEffect(new DashPathEffect(new float[]{convertDpToPixel(getContext(), 8), convertDpToPixel(getContext(), 3)}, 0));
        mBackgroundColorPaint.setStrokeWidth(mCircleStrokeWidth);

        invalidate();
    }

    /**
     * updates the paint of the marker
     */
    private void updateMarkerColor() {

        mMarkerColorPaint.setColor(mProgressBackgroundColor);
        mMarkerColorPaint.setStyle(Paint.Style.STROKE);
        mMarkerColorPaint.setStrokeWidth(mCircleStrokeWidth / 2);

        invalidate();
    }

    /**
     * updates the paint of the progress and the thumb to give them a new visual style
     */
    private void updateProgressColor() {
        mProgressColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressColorPaint.setColor(mProgressColor);
        mProgressColorPaint.setPathEffect(new DashPathEffect(new float[]{convertDpToPixel(getContext(), 12), convertDpToPixel(getContext(), 3)}, 0));
        mProgressColorPaint.setStyle(Paint.Style.STROKE);
        mProgressColorPaint.setStrokeWidth(mCircleStrokeWidth);

        mThumbColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThumbColorPaint.setColor(mProgressColor);
        mThumbColorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mThumbColorPaint.setStrokeWidth(mCircleStrokeWidth);

        invalidate();
    }

    public static int convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
