package com.race604.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class VerticalMarqueeTextView extends TextView {

	private String mText = null;
	private List<String> mLines = new ArrayList<String>();
	// private String mPreLine;
	private TextPaint mPaint;
	private int mWidth;
	private int mHeight;
	private Rect mClipRect = new Rect();
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
//		mPaint = new Paint();
//		mPaint.setAntiAlias(true);
//		mPaint.setColor(getCurrentTextColor());
//		mPaint.setTextSize(mTextSize);
//		mPaint.setTypeface(Typeface.SERIF);
		
		mTextSize = getTextSize();
		mPaint = getPaint();
	}
	
	private int measureViewWidth() {
		ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) getLayoutParams();
		if (params == null) {
			params = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
		}

		int widthSpec = ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(params.width,
				MeasureSpec.UNSPECIFIED), 0, params.width);
		int heightSpec;
		int height = params.height;
		if (height > 0) {
			heightSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else {
			heightSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.UNSPECIFIED);
		}
		measure(widthSpec, heightSpec);

		return getMeasuredWidth();
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
		
		int idx = 0;

		do {
			int size = mPaint.breakText(mText, idx, len, true, mClipRect.width(), null);

			if (size <=0) {
				size = 1;
			}
			
			mLines.add(mText.substring(idx, Math.min(len, idx + size)));
			idx += size;
		} while (idx < len);
		
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
		mHandler.removeCallbacks(mUpdateResults);
		mHandler.post(mUpdateResults);
	}
	
	Runnable mUpdateResults = new Runnable() {
		@Override
		public void run() {
			// while (true) {
				invalidate();
				mHandler.removeCallbacks(mUpdateResults);
				mPadingY -= 2;
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
