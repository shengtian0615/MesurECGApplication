package com.wehealth.mesurecg.view;

import java.util.List;

import com.wehealth.mesurecg.R;
import com.wehealth.model.domain.enumutil.ViewStyle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
/**自定义带有横纵坐标的散列图**/
public class GraphicView extends View {
	/** 公共部分*/
	private static final int CIRCLE_SIZE = 10;


	private Context mContext;
	private Paint mPaint;
	private Resources res;
	private DisplayMetrics dm;

	/** 线的风格**/
	private ViewStyle mStyle;

	private int canvasHeight;
	private int canvasWidth;
	private int bheight = 0;
	private int blwidh;
	private boolean isMeasure = true;
	/** Y轴最大值*/
	private int maxYValue;
	/** Y轴间距值*/
	private int averageYValue;
	private int marginTop = 20;
	private int marginBottom = 50;
	
	private String title;

	/** 曲线上总点数**/
	private PointF[] mPointFs;
	/** 纵坐标值  **/
	private Double[] yRawSet;
	private Double[] xRawSet;
	
	/** x轴最大值**/
	private int maxXValue;
	/** 横坐标标签**/
	private List<String> xRawLabels;
	private int spacingHeight;

	public GraphicView(Context context) {
		this(context, null);
		this.mContext = context;
		initView();
	}

	public GraphicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	private void initView() {
		this.res = mContext.getResources();
		this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (isMeasure) {
			this.canvasHeight = getHeight();
			this.canvasWidth = getWidth();
			if (bheight == 0)
				bheight = (int) (canvasHeight - dip2px(marginBottom));
			blwidh = dip2px(30);
			isMeasure = false;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.setColor(res.getColor(R.color.color_f2f2f2));

		if (yRawSet==null) {
			canvas.drawColor(mContext.getResources().getColor(R.color.page_background_color), PorterDuff.Mode.CLEAR);
			return;
		}
		
//		if (isUpdata) {
			//画直线(横向)
			drawAllXLine(canvas);
			// 画直线（纵向）
			drawAllYLine(canvas);
//		}
		
		drawXLabel(canvas);
		
		// 点的操作设置
		mPointFs = getPointFs();

		mPaint.setColor(res.getColor(R.color.color_ff4631));
		mPaint.setStrokeWidth(dip2px(2.5f));
		mPaint.setStyle(Style.STROKE);
		if (mStyle == ViewStyle.Scatter) {//散点图
//			
		} else if(mStyle == ViewStyle.Line) {//折线图
			drawLine(canvas);
		}else {//平滑曲线图
			drawScrollLine(canvas);
		}

		mPaint.setStyle(Style.FILL);
		mPaint.setTextSize(12);
		for (int i = 0; i < mPointFs.length; i++) {
			canvas.drawCircle(mPointFs[i].x, mPointFs[i].y, CIRCLE_SIZE / 2, mPaint);
		}
		mPaint.setTextSize(16);
		for (int i = 0; i < mPointFs.length; i++) {
//			if (yRawSet[i]>20) {
//				canvas.drawText(yRawSet[i]+"(较大值)", mPointFs[i].x-dip2px(8.0f), blwidh / 2 - dip2px(3), mPaint);
//			}else {
				canvas.drawText(yRawSet[i]+"", mPointFs[i].x-dip2px(8.0f), mPointFs[i].y-dip2px(10.0f), mPaint);
//			}
		}
		
	}

	/**
	 * 画所有横向表格，包括纵坐标的标签
	 */
	private void drawAllXLine(Canvas canvas) {
		for (int i = 0; i < spacingHeight + 1; i++) {
			canvas.drawLine(blwidh, 
					bheight - (bheight / spacingHeight) * i + marginTop, 
					(canvasWidth - blwidh), 
					bheight - (bheight / spacingHeight) * i + marginTop, mPaint);// Y坐标
			drawText(String.valueOf(averageYValue * i), 
					blwidh / 2 - dip2px(3), 
					bheight - (bheight / spacingHeight) * i + dip2px(marginTop),
					canvas,
					15);
		}
	}

	private void drawXLabel(Canvas canvas) {
		drawText(title, 
				blwidh + (canvasWidth - blwidh)-dip2px(18),
				bheight + dip2px(30), 
				canvas,
				15);
	}
	
	/**
	 * 画所有纵向表格，包括横坐标的标签
	 */
	private void drawAllYLine(Canvas canvas) {
//		boolean ji_ou = false;//x轴的最大值奇偶
//		if (maxXValue%2==0) {
//			ji_ou = true;
//		}
		for (int i = 0; i < maxXValue; i++) {
			canvas.drawLine(blwidh + (canvasWidth - blwidh) / maxXValue * i, 
					marginTop,
					blwidh + (canvasWidth - blwidh) / maxXValue * i,
					bheight + marginTop,
					mPaint);
			drawText(xRawLabels.get(i), 
					blwidh + (canvasWidth - blwidh) / maxXValue * i - dip2px(11),
					bheight + dip2px(30), 
					canvas,
					15);// X坐标
//			if (ji_ou) {//偶数
//				if (i%2==0) {
//					drawText(xRawLabels.get(i), 
//							blwidh + (canvasWidth - blwidh) / maxXValue * i,
//							bheight + dip2px(26), 
//							canvas);// X坐标
//				}
//			}else {//奇数
//				if (i%2!=0) {
//					drawText(xRawLabels.get(i), 
//							blwidh + (canvasWidth - blwidh) / maxXValue * i,
//							bheight + dip2px(26), 
//							canvas);// X坐标
//				}
//			}
			
		}
	}

	/**平滑曲线**/
	private void drawScrollLine(Canvas canvas) {
		PointF startp = new PointF();
		PointF endp = new PointF();
		for (int i = 0; i < mPointFs.length - 1; i++) {
			startp = mPointFs[i];
			endp = mPointFs[i + 1];
			float wt = (startp.x + endp.x) / 2;
			PointF p3 = new PointF();
			PointF p4 = new PointF();
			p3.y = startp.y;
			p3.x = wt;
			p4.y = endp.y;
			p4.x = wt;

			Path path = new Path();
			path.moveTo(startp.x, startp.y);
			path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
			canvas.drawPath(path, mPaint);
		}
	}
	
	/**折线**/
	private void drawLine(Canvas canvas) {
		PointF startp = new PointF();
		PointF endp = new PointF();
		for (int i = 0; i < mPointFs.length - 1; i++) {
			startp = mPointFs[i];
			endp = mPointFs[i + 1];
			canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
		}
	}
	/**
	 * 将文字画在图上
	 * @param text
	 * @param x
	 * @param y
	 * @param canvas
	 * @param textSize
	 */
	private void drawText(String text, int x, int y, Canvas canvas, int textSize) {
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setTextSize(dip2px(textSize));
		p.setColor(res.getColor(R.color.color_999999));
		p.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(text, x, y, p);
	}

	private PointF[] getPointFs() {
		/**x轴分为maxXValue份，每份占了多少值**/
		float averageX = (canvasWidth - blwidh) / maxXValue;
		PointF[] points = new PointF[yRawSet.length];
		for (int i = 0; i < yRawSet.length; i++) {
			float pY = bheight - (float) (bheight * (yRawSet[i] / maxYValue));
			float pX = blwidh + (float) (averageX * xRawSet[i]);
			points[i] = new PointF(pX, pY + marginTop);
		}
		return points;
	}

	/**
	 * 
	 * @param yRawData 点的纵坐标
	 * @param xRawData 点的横坐标
	 * @param xLabels 横坐标的标签
	 * @param maxYValue 纵坐标的最大值
	 * @param averageValue 纵坐标的平均值
	 * @param maXValue 横坐标的最大值
	 * @param viewStyle 图的风格
	 */
	public void setData(Double[] yRawData, Double[] xRawData, List<String> xLabels,
			int maxYValue, int averageValue, int maXValue, ViewStyle viewStyle, String xTitle) {
		this.maxYValue = maxYValue;
		this.maxXValue = maXValue;
		this.averageYValue = averageValue;
		if (yRawData!=null) {
			this.mPointFs = new PointF[yRawData.length];
		}
		this.xRawLabels = xLabels;
		this.yRawSet = yRawData;
		this.xRawSet = xRawData;
		if (averageValue!=0) {
			this.spacingHeight = maxYValue / averageValue;
		}
//		this.isUpdata = update;
		this.title = xTitle;
		
		mStyle = viewStyle;
	}

//	public void setTotalvalue(int maxYValue) {
//		this.maxYValue = maxYValue;
//	}
//
//	public void setPjvalue(int averageYValue) {
//		this.averageYValue = averageYValue;
//	}
//
//	public void setMargint(int marginTop) {
//		this.marginTop = marginTop;
//	}
//
//	public void setMarginb(int marginBottom) {
//		this.marginBottom = marginBottom;
//	}
//
//	public void setMstyle(ViewStyle mStyle) {
//		this.mStyle = mStyle;
//	}
//
//	public void setBheight(int bheight) {
//		this.bheight = bheight;
//	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	private int dip2px(float dpValue) {
		return (int) (dpValue * dm.density + 0.5f);
	}

}