package ecolocation.ecolocation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by Chandler on 12/29/2017.
 */

public class CustomGestureDetector extends Activity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gesture);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
            this.mDetector.onTouchEvent(event);
            // Be sure to call the superclass implementation
            return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Toast.makeText(this, "onDown", Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG, "onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
//            Toast.makeText(this, "onFling", Toast.LENGTH_SHORT).show();
            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
//        Toast.makeText(this, "onLongPress", Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
     }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                float distanceY) {
//        Toast.makeText(this, "onScroll", Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
        return true;
     }

    @Override
    public void onShowPress(MotionEvent event) {
//        Toast.makeText(this, "onShowPress", Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
//        Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Toast.makeText(this, "onDoubleTap", Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
            return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Toast.makeText(this, "on double tap event", Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Toast.makeText(this, "on single tap confirmed", Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }
}
