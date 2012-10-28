package com.uit.ocr.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint("ViewConstructor")
public class CustomImageView extends ImageView {
	
	private float currentPointX, currentPointY, lastPointX, lastPointY;
	
	private final int NONE = 0;
	private final int DRAG = 1;
	private final int ZOOM_LEFT = 2;
	private final int ZOOM_RIGHT = 3;
	private final int ZOOM_TOP = 4;
	private final int ZOOM_BOT = 5;
	private int mode = NONE;
	
	
	private float scale;
	private float redundantYSpace, redundantXSpace;	
	
	private Bitmap mBitmap;
	
	//private float mTop = 0;
	//private float mBot = 0;
	//private float mLeft = 0;
	//private float mRight = 0;

	private final Paint paint;
	float mTop, mBot, mLeft, mRight;
	public boolean mFlag;
	
	//private Bitmap buttonProcess;
	//private Bitmap buttonCancel;
	
	private int screenW, screenH;
	
	public CustomImageView(Context context) {
		super(context);		
		paint = new Paint();		
		//buttonProcess = BitmapFactory.decodeResource(getResources(), R.id.iv_imagecrop_btnProcess);
		//buttonCancel = BitmapFactory.decodeResource(getResources(), R.id.);
				
		mFlag = false;
	}
	
	public CustomImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		paint = new Paint();		
		//buttonProcess = BitmapFactory.decodeResource(getResources(), R.drawable.my_button_background_normal);
		//buttonCancel = BitmapFactory.decodeResource(getResources(), R.drawable.my_button_background_normal);
				
		mFlag = false;
	}
	
	public void setBitmap(Bitmap bitmap)
	{
		this.mBitmap = bitmap;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);		 
	    
	    // Draw the framing rect corner UI elements
	    if(mFlag == true)
	    {
	    	if(mTop < redundantYSpace)
		    	mTop = redundantYSpace;
		    if(mBot > screenH - redundantYSpace)
		    	mBot = screenH - redundantYSpace;
		    if(mLeft < redundantXSpace)
		    	mLeft = redundantXSpace;
		    if(mRight > screenW - redundantXSpace)
		    	mRight = screenW - redundantXSpace;
		    
		    paint.setColor(0xffffff00);		    
		    canvas.drawCircle(mLeft, mTop + (mBot - mTop)/2, 10, paint);
		    canvas.drawCircle(mRight, mTop + (mBot - mTop)/2, 10, paint);
		    canvas.drawCircle(mLeft + (mRight - mLeft)/2, mTop, 10, paint);
		    canvas.drawCircle(mLeft + (mRight - mLeft)/2, mBot, 10, paint);
		    
		    paint.setColor(0xffff000b);
		    paint.setStyle(Style.STROKE);
		    canvas.drawCircle(mLeft, mTop + (mBot - mTop)/2, 10, paint);
		    canvas.drawCircle(mRight, mTop + (mBot - mTop)/2, 10, paint);
		    canvas.drawCircle(mLeft + (mRight - mLeft)/2, mTop, 10, paint);
		    canvas.drawCircle(mLeft + (mRight - mLeft)/2, mBot, 10, paint);
		    
	    }

	    // Draw the exterior (i.e. outside the framing rect) darkened
	    paint.setColor(0x60000000);
	    paint.setStyle(Style.FILL);
	    canvas.drawRect(0, 0, screenW, mTop, paint);
	    canvas.drawRect(0, mTop, mLeft, mBot + 1, paint);
	    canvas.drawRect(mRight + 1, mTop, screenW, mBot + 1, paint);
	    canvas.drawRect(0, mBot + 1, screenW, screenH, paint);

	    
	    // Draw a two pixel solid border inside the framing rect
	    paint.setAlpha(0);
	    paint.setStyle(Style.FILL);
	    paint.setColor(0xffd6d6d6);
	    canvas.drawRect(mLeft, mTop, mRight + 1, mTop + 2, paint);
	    canvas.drawRect(mLeft, mTop + 2, mLeft + 2, mBot - 1, paint);
	    canvas.drawRect(mRight - 1, mTop, mRight + 1, mBot - 1, paint);
	    canvas.drawRect(mLeft, mBot - 1, mRight + 1, mBot + 1, paint);
	    
	    // Draw button	   
	    //canvas.drawBitmap(buttonProcess, 20, screenH - 30, null);
	    //canvas.drawBitmap(buttonCancel, screenW - 50, screenH - 30, null);	    
	    
	}
		
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
				
		switch (event.getAction()) 
    	{
			case MotionEvent.ACTION_DOWN:
				lastPointX = event.getX();
				lastPointY = event.getY();	
				
				if(lastPointX > mLeft + 10 && lastPointX < mRight - 10 && lastPointY > mTop + 10 && lastPointY < mBot - 10)
				{
					mode = DRAG;
				}
				else 
					if(lastPointX > mLeft - 10 && lastPointX < mLeft + 10 && lastPointY > mTop && lastPointY < mBot)
					{
						mode = ZOOM_LEFT;
						mFlag = true;
						invalidate();
					}
					else
						if(lastPointX > mRight - 10 && lastPointX < mRight + 10 && lastPointY > mTop && lastPointY < mBot)
						{
							mode = ZOOM_RIGHT;
							mFlag = true;
							invalidate();
						}
						else
							if(lastPointY < mTop + 10 && lastPointY > mTop - 10 && lastPointX > mLeft && lastPointX < mRight)
							{
								mode = ZOOM_TOP;
								mFlag = true;
								invalidate();
							}
							else if(lastPointY < mBot + 10 && lastPointY > mBot - 10 && lastPointX > mLeft && lastPointX < mRight)
							{
								mode = ZOOM_BOT;
								mFlag = true;
								invalidate();
							}								
				
				return true;
			case MotionEvent.ACTION_MOVE:
				currentPointX = event.getX();
				currentPointY = event.getY();
				float dx = currentPointX - lastPointX;
				float dy = currentPointY - lastPointY;		
				
				switch (mode) {
				case DRAG:
					if((mTop <= redundantYSpace && dy < 0) || (mBot >= screenH - redundantYSpace && dy > 0))
					{
						if(dy < 0)
						{
							mBot -= (mTop - redundantYSpace);
							mTop = redundantYSpace;
						}
						else
						{
							mTop -= (mBot - (screenH - redundantYSpace));
							mBot = screenH - redundantYSpace;
						}
					}
					else
					{
						mTop += dy;
						mBot += dy;
					}	
						
					if((mLeft <= redundantXSpace && dx < 0) || (mRight >= screenW - redundantXSpace && dx > 0))
					{
						if(dx < 0)
						{
							mRight -= mLeft - redundantXSpace;
							mLeft = redundantXSpace;
						}
						else
						{
							mLeft -= (mRight - (screenW - redundantXSpace));
							mRight = screenW - redundantXSpace;
						}
					}
					else
					{
						mLeft += dx;
						mRight += dx;
					}
					
					break;
				case ZOOM_LEFT:	
					if(mLeft >= mRight - 40 && dx > 0)
					{
						
					}
					else
					{
						mLeft += dx;							
						mRight -= dx;
					}
					break;
				case ZOOM_RIGHT:
					if(mLeft >= mRight - 40 && dx < 0)
					{
						
					}
					else
					{
						mLeft -= dx;
						mRight += dx;
					}
					break;
				case ZOOM_TOP:
					if(mTop >= mBot - 40 && dy > 0)
					{
						
					}
					else
					{
						mTop += dy;
						mBot -= dy;
					}
					break;
				case ZOOM_BOT:
					if(mTop >= mBot - 40 && dy < 0)
					{
						
					}
					else
					{
						mTop -= dy;
						mBot +=dy;
					}
					break;
				default:
					break;
				}
										
				invalidate();	
				lastPointX = currentPointX;
				lastPointY = currentPointY;
				return true;
			case MotionEvent.ACTION_UP:
				mode = NONE;
				mFlag = false;
				invalidate();
				return true;
			
		}					
		return false;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		screenW = MeasureSpec.getSize(widthMeasureSpec);
	    screenH = MeasureSpec.getSize(heightMeasureSpec);
	    //Fit to screen.	    
	    float scaleX =  (float)screenW / (float)mBitmap.getWidth();
	    float scaleY = (float)screenH / (float)mBitmap.getHeight();
	    scale = Math.min(scaleX, scaleY);
	    
	    redundantYSpace = (float)screenH - (scale * (float)mBitmap.getHeight()) ;
	    redundantXSpace = (float)screenW - (scale * (float)mBitmap.getWidth());
	    redundantYSpace /= (float)2;
	    redundantXSpace /= (float)2;	
	    
	    mTop = redundantYSpace + 100;
		mBot = mTop + 100;
		mLeft = redundantXSpace + 100;
		mRight = mLeft + 100;
	}
	
	
	public Bitmap cropBitmap()
	{
		Bitmap cropResult = null;
		cropResult = Bitmap.createBitmap(mBitmap, (int)((mLeft - redundantXSpace)/scale), (int)((mTop - redundantYSpace)/scale), (int)((mRight - mLeft)/scale), (int)((mBot - mTop)/scale));
		return cropResult;
	}

}
