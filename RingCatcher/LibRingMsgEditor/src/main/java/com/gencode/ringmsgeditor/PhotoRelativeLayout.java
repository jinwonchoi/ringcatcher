package com.gencode.ringmsgeditor;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gencode.ringmsgeditor.task.AsyncGetBitmapDrawableFromUrl;
import com.gencode.ringmsgeditor.task.IGetBitmapDrawableTask;

/**
 * Created by Administrator on 2016-06-06.
 */
public class PhotoRelativeLayout extends ViewGroup implements View.OnTouchListener, IGetBitmapDrawableTask {
    final String TAG = this.getClass().getName();
    int deviceWidth;
    int mWidthMeasureSpec;

    //<--- touch resize
    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float[] lastEvent = null;
    ////////
    // touch resize --->
    private boolean isPreview = false;

    private Drawable mDrawable;
    private int mImgRes;

    private OnModeSwitchListener mOnModeSwitcherListener;
    private OnImageSizeChangeListener mOnImageSizeChangeListener;

    ImageView imgView;
    Button btnClose;

    public PhotoRelativeLayout(Context context) {
        super(context);
    }

    public PhotoRelativeLayout(Context context, OnModeSwitchListener onModeSwitchListener, OnImageSizeChangeListener imageSizeChangeListener) {
        super(context);
        mOnModeSwitcherListener = onModeSwitchListener;
        mOnImageSizeChangeListener = imageSizeChangeListener;
    }

    public PhotoRelativeLayout(Context context, OnModeSwitchListener onModeSwitchListener, OnImageSizeChangeListener imageSizeChangeListener, int imgRes) {
        this(context,onModeSwitchListener, imageSizeChangeListener);
        mImgRes = imgRes;
    }

    public PhotoRelativeLayout(Context context, OnModeSwitchListener onModeSwitchListener, OnImageSizeChangeListener imageSizeChangeListener, Drawable imgDrawable) {
        this(context,onModeSwitchListener, imageSizeChangeListener);
        mDrawable = imgDrawable;
    }
    /**
     * uriStr : uri;xscale;yscale  ex: android.resource://my_app_package/drawable/drawable_name;.2;0,45
     */
    public PhotoRelativeLayout(Context context, OnModeSwitchListener onModeSwitchListener, OnImageSizeChangeListener imageSizeChangeListener, String uriStr) {
        this(context,onModeSwitchListener, imageSizeChangeListener);
        parseUri(uriStr);
        this.setTag(uriStr);
    }

    /**
     * uriStr : uri;xscale;yscale  ex: android.resource://my_app_package/drawable/drawable_name;.2;0,45
     */
    public PhotoRelativeLayout(Context context, OnModeSwitchListener onModeSwitchListener, OnImageSizeChangeListener imageSizeChangeListener, String uriStr, float xScale, float yScale) {
        this(context,onModeSwitchListener, imageSizeChangeListener);
        parseUri(uriStr);
        this.setTag(uriStr);
        matrix.postScale(xScale,yScale, 0,0);
        isPreview = true;
    }

    /**
     * uriStr : uri;xscale;yscale  ex: android.resource://my_app_package/drawable/drawable_name;.2;0,45
     */
    public PhotoRelativeLayout(Context context, OnModeSwitchListener onModeSwitchListener, OnImageSizeChangeListener imageSizeChangeListener, int imgRes, float xScale, float yScale) {
        this(context,onModeSwitchListener, imageSizeChangeListener);
        mImgRes = imgRes;
        matrix.postScale(xScale,yScale, 0,0);
        isPreview = true;
    }

    private void parseUri(String uriStr) {
        try {
            String[] uriArr = uriStr.split(";");
            if (uriArr.length == 3) {
                Uri uri = Uri.parse(uriArr[0]);
                AsyncGetBitmapDrawableFromUrl asyncGet = new AsyncGetBitmapDrawableFromUrl(this, this.getContext());
                asyncGet.execute(uriArr[0]);
                float fx = Float.parseFloat(uriArr[1]);
                float fy = Float.parseFloat(uriArr[2]);
                matrix.postScale(fx,fy, 0,0);
                isPreview = true;
            } else {
                AsyncGetBitmapDrawableFromUrl asyncGet = new AsyncGetBitmapDrawableFromUrl(this, this.getContext());
                asyncGet.execute(uriArr[0]);
            }
        } catch (Exception e) {
            Log.e(TAG, "wrong Url info(url;xscale;yscale)",e);
        }
    }

    public PhotoRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //super.onLayout(changed, left, top, right, bottom);
//        View child = new Button(getContext());
//
//        //child measuring
//        int childWidthSpec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, 0, LayoutParams.WRAP_CONTENT); //mWidthMeasureSpec is defined in onMeasure() method below
//        int childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);//we let child view to be as tall as it wants to be
//        child.measure(childWidthSpec, childHeightSpec);
//
//        //find were to place checkpoint Button in FrameLayout over SeekBar
//        int childLeft = (getWidth() * checkpointProgress) / getMax() - child.getMeasuredWidth();
//
//        LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param.gravity = Gravity.TOP;
//        param.setMargins(childLeft, 0, 0, 0);
//
//        //specifying 'param' doesn't work and is unnecessary for 1.6-2.1, but it does the work for 2.3
//        parent.addView(child, firstCheckpointViewIndex + i, param);
//
//        //this call does the work for 1.6-2.1, but does not and even is redundant for 2.3
//        child.layout(childLeft, 0, childLeft + child.getMeasuredWidth(), child.getMeasuredHeight());


            imgView.layout(0, 0, right - left, bottom - top);


//        if (btnClose == null) {
////        Matrix matrixImg = imgView.getImageMatrix();
//            btnClose = new Button(getContext());
//            Drawable drClose = ContextCompat.getDrawable(getContext(), R.drawable.ic_check_box_black_24dp);
//            Bitmap bMap = ((BitmapDrawable) drClose).getBitmap();
//
//            btnClose.setBackground(drClose);
//
//            LinearLayout.LayoutParams rlBtn = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            rlBtn.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//            rlBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//
//            btnClose.setLayoutParams(rlBtn);
//            this.addView(btnClose);
//            Log.d("CHOI_DEBUG", bMap.getWidth() + ":" + right);
//            btnClose.layout(right - bMap.getWidth(), bMap.getHeight(), right, bMap.getHeight() + bMap.getHeight());//right-24,0,right,24);//(right-bMap.getWidth(),bottom-bMap.getHeight(),right,bottom);
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidthMeasureSpec = widthMeasureSpec;
        if (imgView == null) {
            imgView = new ImageView(getContext());
            Log.d("CHOI_DEBUG", "ImageView created");
            if (mDrawable == null) {
                imgView.setImageResource(android.R.color.transparent);
            } else
                imgView.setImageDrawable(mDrawable);

            imgView.setScaleType(ImageView.ScaleType.MATRIX);
            imgView.setPadding(1, 1, 1, 1);
            // Defining the layout parameters of the TextView
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);

            // Setting the parameters on the TextView
            imgView.setLayoutParams(lp);
            //matrix.postScale(0.39f,0.39f, 0,0);
            imgView.setImageMatrix(matrix);
            imgView.setOnTouchListener(this);
            resizeLayout(imgView);
            this.addView(imgView);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        try {
            if (isPreview) return true;
            // handle touch events here
            ImageView view = (ImageView) v;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    lastEvent = null;

                    if (view.getBackground() == null) {
                        view.setBackgroundResource(R.drawable.image_border);
                        view.setPadding(2, 2, 2, 2);
                        mOnModeSwitcherListener.OnModeSwitchChange(this, true);
                        Log.d("CHOI_DEBUG", "onClick set view");
                    } else {
                        view.setBackground(null);
                        view.setPadding(0, 0, 0, 0);
                        mOnModeSwitcherListener.OnModeSwitchChange(this, false);
                        Log.d("CHOI_DEBUG", "onClick set null");
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = (newDist / oldDist);
                            matrix.postScale(scale, scale, 0, 0);
                        }
                    }
                    break;
            }


            view.setImageMatrix(matrix);
            if (!savedMatrix.equals(matrix)) {
                resizeLayout(view);
                view.requestLayout();
                requestLayout();
                mOnImageSizeChangeListener.OnImageSizeChanged(this);
            }
            invalidate();

            Log.d("CHOI_DEBUG", "invalidate");
        } catch (Exception e) {
            Log.e(TAG, "OnTouch Error",e);
        }
        return true;
    }

    void resizeLayout(ImageView view) {
        LinearLayout.LayoutParams imgViewParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        float[] f = new float[9];
        matrix.getValues(f);
        float displayedWidth = view.getDrawable().getIntrinsicWidth() * f[Matrix.MSCALE_X]+2;
        float displayedHeight = view.getDrawable().getIntrinsicHeight() * f[Matrix.MSCALE_Y]+2;

        imgViewParams.height = (int)displayedHeight;
        imgViewParams.width = (int)displayedWidth;
        view.setLayoutParams(imgViewParams);
        Log.d("CHOI_DEBUG","view.setLayoutParams {"+ imgViewParams.height+"}{"+  imgViewParams.width+"}"+"xscale:"+f[Matrix.MSCALE_X]+",yscale:"+f[Matrix.MSCALE_Y]);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        params.height = (int)displayedHeight+2;
        params.width = (int)displayedWidth+2;
        this.setLayoutParams(params);
        Log.d("CHOI_DEBUG","this.setLayoutParams {"+params.height+"}{"+params.width+"}");
    }

    public String getUri() {
        float[] f = new float[9];
        matrix.getValues(f);
        if (f[Matrix.MSCALE_X] == 1f) f[Matrix.MSCALE_X]= 0.99f;
        if (f[Matrix.MSCALE_Y] == 1f) f[Matrix.MSCALE_Y]= 0.99f;
        return String.format("%s;%f;%f",this.getTag(),f[Matrix.MSCALE_X],f[Matrix.MSCALE_Y]);
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    @Override
    public void onTaskCompleted(Drawable drawable) {
        if (drawable == null) return;
        Log.d(TAG, "onTaskCompleted x:"+drawable.getIntrinsicWidth()+" y:"+drawable.getIntrinsicHeight());
        mDrawable = drawable;
        imgView.setImageDrawable(mDrawable);
        resizeLayout(imgView);
    }
}
