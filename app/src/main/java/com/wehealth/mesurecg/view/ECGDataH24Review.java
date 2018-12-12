package com.wehealth.mesurecg.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.activity.ReviewECGWaveActivity;
import com.wehealth.model.domain.model.ECGDataLong2Device;
import com.wehealth.model.util.DataUtil;
import com.wehealth.model.util.SampleDotIntNew;
import com.wehealth.model.util.StringUtil;

/**24小时 1*1ch显示**/
public class ECGDataH24Review extends SurfaceView implements Callback, Runnable {

	private final int LEADNUM = 12;
	private Thread thread;
	private SurfaceHolder holder;
	private Canvas canvas;
	private boolean isDraWave;
	private Paint wavePaint;
	private Paint bigBGPaint, smallBGPaint;
	private int actionOldX, actionOldY, actionCurrentX, actionCurrentY, waveSpeed, xGridNum, yGridNum;//, offSetX
	private long fileDataIndex = 0;
	public static final int fileDataCount = 2500 * 4;
	private RandomAccessFile[] file;
	private byte[] oncePoints = new byte[fileDataCount*2];
	private int[] buffer24H;
	private SampleDotIntNew sampleDot[];
	private String leadName[] = { "I", "II", "III", "avR", "avL", "avF", "V1", "V2", "V3", "V4", "V5", "V6" };
	private int oldY[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	final static int MARGIN = 4;
	final static int GRID_SIZE = 5;
	private int heartRate;
	private int screenHeight;
	private int baseY[], baseX[];
	private final String filePath = StringUtil.getSDPath()+"/ECGDATA/Data2Device/Hours24/";
	private ReviewECGWaveActivity ctx;
	private int h24_LeadName;
	private String detailFilePath;
	
	@SuppressLint("UseSparseArrays")
	public ECGDataH24Review(ReviewECGWaveActivity context, ECGDataLong2Device edl2Device) {
		super(context);
		ctx = context;
		holder = getHolder();
		holder.addCallback(this);
		initPaint();
		try {
			if (edl2Device!=null) {
				int size = (edl2Device.getTotalTime()/60);
				if (edl2Device.getTotalTime()%60 > 0) {
					size += 1;
				}
				detailFilePath = edl2Device.getH24_path();
				h24_LeadName = edl2Device.getH24_leadName();
				file = new RandomAccessFile[size];
				for (int i = 0; i < size; i++) {
					file[i] = new RandomAccessFile(filePath+detailFilePath+"_"+i+".txt", "r");
					Log.e(VIEW_LOG_TAG, "文件长度："+file[i].length());
				}
				file[0].seek(fileDataIndex);
				fileDataIndex = file[0].read(oncePoints);
				buffer24H = DataUtil.toIntArray(oncePoints);
				heartRate = edl2Device.getHeartRate();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sampleDot = new SampleDotIntNew[LEADNUM];
		for (int i = 0; i < LEADNUM; i++) {
			sampleDot[i] = new SampleDotIntNew(500);//, 126 / (waveSpeed + 1)
		}
		initDeSampleDot(126 / (waveSpeed + 1));
	}

	private void initPaint() {
		bigBGPaint = new Paint();
		bigBGPaint.setStrokeWidth((float) 1.5);
		bigBGPaint.setColor(getResources().getColor(R.color.ecg_back_blue));

		wavePaint = new Paint();
		wavePaint.setAntiAlias(true);
		
		smallBGPaint = new Paint();
		smallBGPaint.setColor(getResources().getColor(R.color.ecg_back_little));
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		isDraWave = true;
		screenHeight = height;
		InitYValue(width, height);
		CalXYGridNum(5, width, height);
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isDraWave = false;
	}
	
	private void initDeSampleDot(int desDot){
		for (int i = 0; i < LEADNUM; i++) {
			sampleDot[i].setDesSampleDot(desDot);
		}
	}

	@Override
	public void run() {
//		while (isDraWave) {
			drawECGWave();
//		}
	}

	private void drawECGWave() {
		canvas = holder.lockCanvas();
        canvas.drawColor(ctx.getResources().getColor(R.color.ecg_back_color));
		DrawVerticalLine(5);
		DrawHorizontalLine(5);
		wavePaint.setColor(Color.WHITE);
		DrawWaveTag();
		wavePaint.setColor(Color.GREEN);
		DrawECGWave();
		if (canvas!=null) {
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	private void DrawECGWave() {
		List<Integer> leadListData = sampleDot[0].SnapshotSample(buffer24H);
		int oldX = 0;
		int y = 0;
		for (int j = 0; j < leadListData.size(); j++) {
			y = (baseY[5] + ( - leadListData.get(j)) / (105 * 2) * (screenHeight / 550));
			if (j!=0) {
				canvas.drawLine(oldX + 5*MARGIN + baseX[5],
						oldY[5],
						j + 5*MARGIN + baseX[5],
						y,
						wavePaint);
			}
			oldY[5] = y;
			oldX = j;
		}
	}

	private void DrawWaveTag() {
		canvas.drawText(leadName[h24_LeadName], 10 + baseX[5], baseY[5], wavePaint);
		long minute = fileDataIndex/(60*500*4)+1;
		if (minute>1440) {
			minute = 1440;
		}
		canvas.drawText("第 "+minute+" 分钟", 1000, 100, wavePaint);
		canvas.drawText(heartRate+" bpm", 900, 100, wavePaint);
	}

	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int dateResult = -1;
		if (event.getAction()==MotionEvent.ACTION_DOWN && event.getPointerCount()==1) {
			actionOldX = (int) event.getX();
			actionOldY = (int) event.getY();
		}
		if (event.getAction()==MotionEvent.ACTION_UP) {
			actionCurrentX = (int) event.getX();
			actionCurrentY = (int) event.getY();
			if (actionOldX-actionCurrentX > 200) {//继续往前看
				dateResult = getFileData(false);
				if (dateResult>=0) {
					drawECGWave();
				}else {
					Toast.makeText(ctx, "没有数据了", Toast.LENGTH_SHORT).show();
				}
			} else if(actionCurrentX-actionOldX > 200){//返回往后看
				dateResult = getFileData(true);
				if (dateResult>=0) {
					drawECGWave();
				}else {
					Toast.makeText(ctx, "没有数据了", Toast.LENGTH_SHORT).show();
				}
			}
			
			if (actionCurrentY-actionOldY > 200) {
				ctx.setTopLayoutVisible(true);
			}
			if (actionOldY-actionCurrentY > 200) {
				ctx.setTopLayoutVisible(false);
			}
		}
		return true;
	}
	
	private void InitYValue(int width, int height) {
		baseY = new int[LEADNUM];
		baseX = new int[LEADNUM];
		for (int i = 0; i < LEADNUM; i++) {
			baseY[i] = height / LEADNUM * (i % 12 + 1) - 20;
		}
		for (int i = 0; i < LEADNUM; i++) {
			baseX[i] = 0;
		}
	}
	
	private void CalXYGridNum(int step, int width, int height) {
		xGridNum = (width - MARGIN) - (width - MARGIN) % (GRID_SIZE * 5);
		yGridNum = (height - MARGIN) - (height - MARGIN) % (GRID_SIZE * 5);
	}

	private void DrawVerticalLine(int step) {
		int j = 0;

		for (int i = 0; i <= yGridNum; i += step) {
			if (j == 0) {
				canvas.drawLine(MARGIN, i + MARGIN, xGridNum + step, i + MARGIN, bigBGPaint);
			} else {
				canvas.drawLine(MARGIN, i + MARGIN, xGridNum + step, i + MARGIN, smallBGPaint);
			}

			j++;
			if (j >= 5)
				j = 0;
		}
	}

	private void DrawHorizontalLine(int step) {
		int j = 0;

		for (int i = 0; i <= xGridNum; i += step) {
			if (j == 0) {
				canvas.drawLine(i + MARGIN, MARGIN, i + MARGIN, yGridNum + step, bigBGPaint);
			} else {
				canvas.drawLine(i + MARGIN, MARGIN, i + MARGIN, yGridNum + step, smallBGPaint);
			}
			j++;
			if (j >= 5)
				j = 0;
		}
	}

	/**
	 * 取数据  
	 * @param b 为true时表示：；为false时表示
	 * @return -1表示没有数据了
	 */
	private int getFileData(boolean b) {
		int ret = -1;
		if (!b) {//继续往后
//			byte[] buffer = new byte[fileDataCount*2];
			try {
				ret = positionFiles();
				
//				if (fileDataIndex==0) {
//					h24FIS.seek(fileDataIndex);
//				}
//				ret = h24FIS.read(buffer);
				if (ret<0) {
					return ret;
				}
				if (ret < fileDataCount*2) {
					byte[] temp = new byte[ret];
					for (int j = 0; j < temp.length; j++) {
						temp[j] = oncePoints[j];
					}
					buffer24H = DataUtil.toIntArray(temp);
				}else {
					fileDataIndex += ret;
					buffer24H = DataUtil.toIntArray(oncePoints);
				}
				Log.e(VIEW_LOG_TAG, "read index : "+ fileDataIndex);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (b) {//wanghuikan
//			byte[] buffer = new byte[fileDataCount*2];
			try {
				fileDataIndex = fileDataIndex - (fileDataCount*2);
				if (fileDataIndex < 0) {
					fileDataIndex = 0;
					return ret;
				}
//				h24FIS.seek(fileDataIndex);
//				ret = h24FIS.read(buffer);
				ret = positionFiles();
				if (ret<0) {
					return ret;
				}
				if (ret < fileDataCount*2) {
					byte[] temp = new byte[ret];
					for (int j = 0; j < temp.length; j++) {
						temp[j] = oncePoints[j];
					}
					buffer24H = DataUtil.toIntArray(temp);
				}else {
					buffer24H = DataUtil.toIntArray(oncePoints);
				}
				Log.e(VIEW_LOG_TAG, "read index : "+ fileDataIndex);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	private int positionFiles() {
		int readIosition = -1;
		long fileMinite = fileDataIndex / (60*500*4);
		int path = (int) (fileMinite/60);
		if (path==0) {
			try {
				file[path].seek(fileDataIndex);
				readIosition = file[path].read(oncePoints);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				file[path].seek(fileDataIndex-path*60*60*500*4);
				Log.e(VIEW_LOG_TAG, "数据："+fileDataIndex);
				Log.e(VIEW_LOG_TAG, "数据："+path*60*60*500*4);
				readIosition = file[path].read(oncePoints);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return readIosition;
	}

	public void setOffsetIndex(int offsetIndex) {
		offsetIndex = offsetIndex-1;
		if (offsetIndex >= 0) {
			fileDataIndex = offsetIndex * 500 * 60 * 4;
			Log.e(VIEW_LOG_TAG, "定位查看数据：fileDataIndex = "+fileDataIndex+"分钟："+(offsetIndex+1));
//			try {
//				h24FIS.seek(fileDataIndex);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			int dateResult = getFileData(false);
			if (dateResult>=0) {
				drawECGWave();
			}else {
				Toast.makeText(ctx, "没有数据了", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
