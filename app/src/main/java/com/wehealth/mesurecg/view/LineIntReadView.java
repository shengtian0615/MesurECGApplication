/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun 2012-3-21
 */

package com.wehealth.mesurecg.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画出心电曲线图
 */
public class LineIntReadView extends View {
    private List<Point> mListPoint = new ArrayList<Point>();
      
    Paint mPaint = new Paint();
    Paint pacePaint = new Paint();
    Paint wgPaint = new Paint();
    private int MaxValue;  //绘制数据最大值
	private int BaseLine;  //中间值
	private int WaveGain;  //波形增益
	private boolean style;
	private int paceMarker = 2147483647;
	
	public void SetDrawMaxValue(int value){
		MaxValue = value;
	}
	
	public void SetBaseLine(int value){
		BaseLine = value;
	}
	
	public void SetWaveGain(int value){
		WaveGain = value;
	}
	
	public boolean isStyle() {
		return style;
	}

	public void setStyle(boolean style) {
		this.style = style;
	}
	
	private float changeToScreenPosition(int data) {
		float scrheight = this.getMeasuredHeight();
		return ((scrheight - ((data  - BaseLine) * scrheight * WaveGain) / MaxValue / 210) - (scrheight / 2));
	}
      
    public LineIntReadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }  
  
    public LineIntReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
      
    public LineIntReadView(Context context) {
        super(context);
    }
      
    @SuppressLint("DrawAllocation") @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);
        
        wgPaint.setColor(Color.YELLOW);
        wgPaint.setStrokeJoin(Paint.Join.ROUND);
        wgPaint.setStrokeCap(Paint.Cap.ROUND);
        wgPaint.setStrokeWidth(3);
        pacePaint.setColor(Color.RED);
        canvas.drawBitmap(drawBackground(30, this.getMeasuredHeight(), this.getMeasuredWidth()), 0, 0, wgPaint);
        
        if (isStyle()) {
        	mPaint.setColor(0xff01A312);
		}else {
			mPaint.setColor(Color.GREEN);
		}
        mPaint.setAntiAlias(true);
       
        Point lastPoint = null;
        for (int index=0; index<mListPoint.size(); index++) {
        	Point currentPoint = mListPoint.get(index);
            if (index > 0) {
            	if (currentPoint.y==paceMarker) {
            		canvas.drawLine(currentPoint.x , lastPoint.y-20,
    						currentPoint.x , lastPoint.y+20, pacePaint);
    				canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
    						Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
    				currentPoint = lastPoint;
				}else {
					canvas.drawLine(lastPoint.x , lastPoint.y,
							currentPoint.x , currentPoint.y, mPaint);
					canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
							Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
				}
            	
            }
			lastPoint = currentPoint;
        }
    }
    
    /**
     * 绘制背景
     * @param cellSize
     * @param height
     * @param widht
     * @return
     */
    public Bitmap drawBackground(int cellSize, int height, int widht) {

		int tmpSize;
		Bitmap bitmap = Bitmap.createBitmap(widht, height, Config.ARGB_8888);
		Canvas cv = new Canvas(bitmap);
		Paint background = new Paint();
		if (isStyle()) {
			background.setColor(0xFFFFFFFF);
		}else {
			background.setColor(0xff1e2331);
		}
	
		cv.drawRect(0, 0, widht, height, background);
		background.setAntiAlias(true);
	
		Paint rect = new Paint();
		rect.setStyle(Style.STROKE);
		rect.setStrokeWidth(4);
		rect.setColor(0xff303749);
		RectF outerRect = new RectF(4, 4, widht - 4, height - 4);
        cv.drawRoundRect(outerRect, 10, 10, rect);
		
        if (isStyle()) {
        	background.setColor(0xffD3D3D3);
		}else {
			background.setColor(0xff303749);
		}
        
        //画最小的分格
		tmpSize = (cellSize / 5);
		for(int i = 1; i < widht / tmpSize; i++){
			cv.drawLine(tmpSize * i, 5, tmpSize * i , height - 5, background);
		}
		for(int i = 1; i < height / tmpSize; i++){
			cv.drawLine(5, tmpSize * i, widht - 5, tmpSize * i, background);
		}
		
	
		//画大的分格
		background.setStrokeWidth(3);
		for (int i = 1; i < (widht / cellSize + 1); i++) {
			cv.drawLine(cellSize * i, 5, cellSize * i, height - 5 , background);
		}
		for (int i = 1; i < height / cellSize; i++) {
			cv.drawLine(5, cellSize * i, widht - 5, cellSize * i, background);
		}
		
		return bitmap;
	}
    
    
    /**
     * 往点的集合里添加一个点
     * @param curX x轴坐标
     * @param curY y轴坐标
     */
    public void setLinePoint(float curX, int curY) {
        mListPoint.add(new Point((int)curX, (int)curY));  
        invalidate(); //刷新视图
    }

    /**
     * 把数组添加到集合中，并刷新视图
     * @param ecg
     */
	public void setLinePoint(int[] ecg) {
		if (null==ecg) {
			return;
		}
		for (int i = 0; i < ecg.length; i++) {
			if (ecg[i] == paceMarker) {
				mListPoint.add(new Point(i, paceMarker));
			}else {
				mListPoint.add(new Point(i, (int) changeToScreenPosition(ecg[i])));  
			}
		}
		invalidate(); //刷新视图
	}
	
	
	public void setLinePoint(short[] ecg) {
		if (null==ecg) {
			return;
		}
		for (int i = 0; i < ecg.length; i++) {
			if (ecg[i] == paceMarker) {
				mListPoint.add(new Point(i, paceMarker));
			}else {
				mListPoint.add(new Point(i, (int) changeToScreenPosition(ecg[i])));
			}
		}
		invalidate(); //刷新视图
	}
	
	public void clearView(){
		mListPoint.clear();
		invalidate();
	}
}


