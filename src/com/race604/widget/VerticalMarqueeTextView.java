package com.race604.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.widget.TextView;

public class VerticalMarqueeTextView extends TextView {

	private String mText = null;
	private List<String> mLines = new ArrayList<String>();
	// private String mPreLine;
	private Paint mPaint;
	private int mWidth;
	private int mHeight;
	private Rect mClipRect = new Rect();
	private Rect mTextBound = new Rect();
	private int mCurent;
	private float mPadingY = 0f;
	private Handler mHandler = new Handler();
	private int mNumLine = 0;
	private float mTextSize = 0.0f;
	private static final int LINE_GAP = 2;
	
	public VerticalMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		
	}

	public VerticalMarqueeTextView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		
		mPaint.setColor(getCurrentTextColor());
		mTextSize = getTextSize();
		mPaint.setTextSize(mTextSize);
		
		mPaint.setTypeface(Typeface.SERIF);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		
		mClipRect.top = getPaddingTop();
		mClipRect.left = getPaddingLeft();
		mClipRect.right = mWidth - getPaddingRight();
		mClipRect.bottom = mHeight - getPaddingBottom();
		mPadingY = mClipRect.top + LINE_GAP;
		
		splitLines();
		startMarquee();
	}
	
	
//	@Override
//	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//		super.onSizeChanged(w, h, oldw, oldh);
//		mWidth = w;
//		mHeight = h;
//	}

	private void splitLines() {
		
		mLines.clear();
		mNumLine = 0;
		if (mText == null || mWidth == 0) {
			return;
		}
		
		int len = mText.length();
		mPaint.getTextBounds(mText, 0, len, mTextBound);
		int width = mClipRect.width();
		if (mTextBound.width() > width) {
			int size = (int) FloatMath.floor((float)len * width / mTextBound.width());
			int indx = 0;
			while(indx < len) {
				mLines.add(mText.substring(indx, Math.min(len, indx + size)));
				indx += size;
			}
		} else {
			mLines.add(mText);
		}
		mNumLine = mLines.size();
		mCurent = 0;
	}

	public void setText(String text) {
		mText = text;
		splitLines();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.clipRect(mClipRect);
		
		float y = mPadingY;
		int next = mCurent;
		while(y - mTextSize < mClipRect.bottom ) {
			canvas.drawText(mLines.get(next), mClipRect.left, y, mPaint);
			next++;
			if (next >= mNumLine) {
				next = 0;
			}
			 y += mTextSize + 2 * LINE_GAP;
		}
		
	}

	public void startMarquee() {
		mHandler.post(mUpdateResults);
	}
	
	Runnable mUpdateResults = new Runnable() {
		@Override
		public void run() {
			// while (true) {
				invalidate();
				mPadingY -= 3;
				long delay = 40;
				if (mPadingY <= mClipRect.top - LINE_GAP) {
					mCurent++;
					if (mCurent == mNumLine) {
						mCurent = 0;
					}
					mPadingY = mClipRect.top + LINE_GAP + mTextSize;
					delay = 1500;
				}
				
				if (mNumLine > 1) {
					mHandler.postDelayed(mUpdateResults, delay);
				}
				
			}
			
		};
		
	
}
