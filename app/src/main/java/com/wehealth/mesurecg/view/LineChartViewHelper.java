package com.wehealth.mesurecg.view;

import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;  
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;  
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.wehealth.mesurecg.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
/**使用开源achartengine图表封装的曲线图**/
public class LineChartViewHelper {
	
	private int margins[];
	private int[] colors;
	PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND, PointStyle.SQUARE}; 
	private XYMultipleSeriesRenderer renderer;
	XYMultipleSeriesDataset dataset;
	XYSeries minSeries;
	XYSeries maxSeries;
	XYSeries heartSeries;
	private GraphicalView barChartView;
	private Context mContext;

	private DisplayMetrics displayMetrics;
	private int heightPixels;

	public LineChartViewHelper(Context context) {
		this.mContext = context;
		colors = new int[]{mContext.getResources().getColor(R.color.color_EF692C),
				mContext.getResources().getColor(R.color.color_FDE114),
				mContext.getResources().getColor(R.color.color_18A5DD)};
		displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		
		if (displayMetrics!=null) {
			heightPixels = displayMetrics.heightPixels;
		}
		if (heightPixels>=1920) {
			margins = new int[] { dip2px(24), dip2px(36), dip2px(20), dip2px(10) };//上 左 下  右
		}else {
			margins = new int[] { dip2px(24), dip2px(36), dip2px(10), dip2px(10) };
		}
	}

	public View getBarChartView() {
		barChartView = ChartFactory.getLineChartView(mContext, dataset, renderer); // Type.STACKED
		barChartView.setBackgroundColor(0x00ffffff);
		barChartView.invalidate();
		return barChartView;
	}

	public void initBarDataSet(String[] titles){
		dataset = new XYMultipleSeriesDataset();
		maxSeries = new XYSeries(titles[0]);
		minSeries = new XYSeries(titles[1]);
		heartSeries = new XYSeries(titles[2]);
		dataset.addSeries(maxSeries);
		dataset.addSeries(minSeries);
		dataset.addSeries(heartSeries);
	}

	/**
	 * 初始化折线图风格
	 */
	public void initRenderer(String title, int xMax, String xtitle, Map<Integer, String> map) {
		renderer = new XYMultipleSeriesRenderer();
		renderer.setMargins(margins);
		renderer.setMarginsColor(0x00ffffff);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);// 设置x，y方向都不可以放大或缩�?
		renderer.setZoomRate(1.0f);
		renderer.setInScroll(false);
		renderer.setBackgroundColor(0x00ffffff);
		renderer.setApplyBackgroundColor(false);
		renderer.setChartTitle(title);//设置图表标题  
		renderer.setChartTitleTextSize(dip2px(18));//设置图表标题文字的大小  
		renderer.setXTitle(xtitle);//设置为X轴的标题  
		renderer.setYTitle("血压(mmHg)");//设置y轴的标题  
		renderer.setXAxisMin(0);//设置y轴最小值是0  
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(300);
		renderer.setAxesColor(Color.BLACK);
		renderer.setLabelsColor(Color.LTGRAY);
		renderer.setXLabels(20);
		renderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）
		renderer.setLabelsTextSize(dip2px(12));//设置x轴y轴的文字大小  
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsPadding(dip2px(1));//设置Y轴刻度的距离
		renderer.setPointSize(dip2px(4));//设置点的大小  
		renderer.setLegendTextSize(dip2px(14));//设置图例文本大小
		renderer.setAxisTitleTextSize(dip2px(12));////设置轴标题文本大小  
		renderer.setBarWidth(23);
		renderer.setBarSpacing(20);
		renderer.setShowGrid(true);//显示网格
		
		if (map==null) {
			renderer.setXLabels(16);
		}else {//按周显示血压曲线图
//			double i = 0;
			for (int j = 0; j < map.size(); j++) {
				renderer.addXTextLabel(j, map.get(j).split(" ")[1]);
			}
//			Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry<Integer, String> m = it.next();
//				String[] name = m.getValue().split(" ");
//				renderer.addXTextLabel(i, name[1]);//将x标签栏目显示如：周一,周二 
//				i++;
//			}
			renderer.setXLabels(0); // 设置 X 轴不显示数字（改用我们手动添加的文字标签）
		}
		
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			 XYSeriesRenderer r = new XYSeriesRenderer(); 
	         r.setColor(colors[i]); 
	         r.setPointStyle(styles[i]); 
	         r.setFillPoints(true); 
	         r.setLineWidth(6);
	         renderer.addSeriesRenderer(r); 
		}
	}
	
	/**
	 * 根据新加的数据，更新曲线，只能运行在主线程
	 * @param x  新加点的x坐标
	 * @param high  新加点的y坐标
	 * @param low
	 * @param heart
	 */
	public void updateChart(double x, double high, double low, double heart) {
		maxSeries.add(x, high);
		minSeries.add(x, low);
		heartSeries.add(x, heart);
		barChartView.invalidate();// 此处也可以调用invalidate()
	}

	/**
	 * 添加新的数据，多组，更新曲线，只能运行在主线程
	 * @param type
	 * @param x
	 * @param y
	 */
	public void updateCharts(String[] type, List<double[]> x, List<double[]> y) {
		int length = type.length;
		for (int i = 0; i < length; i++) {
			double[] xd = x.get(i);
			double[] yd = y.get(i);
			int seriesLength = xd.length;
			for (int k = 0; k < seriesLength; k++) {
				if (i==0) {
					maxSeries.add(xd[k], yd[k]);
				}
				if (i==1) {
					minSeries.add(xd[k], yd[k]);
				}
				if (i==2) {
					heartSeries.add(xd[k], yd[k]);
				}
			}
		}
		
		barChartView.repaint();// 此处也可以调用invalidate()
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	private int dip2px(float dpValue) {
		return (int) (dpValue * displayMetrics.density + 0.5f);
	}
}

