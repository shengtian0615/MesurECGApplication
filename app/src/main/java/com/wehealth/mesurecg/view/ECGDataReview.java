package com.wehealth.mesurecg.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**手动模式6*2ch显示**/
public class ECGDataReview extends SurfaceView implements Callback, Runnable {

	private final int LEADNUM = 12;
	private Thread thread;
	private SurfaceHolder holder;
	private Canvas canvas;
	private Paint wavePaint, textPaint;
	private Paint bigBGPaint, smallBGPaint;
	private int actionOldX, actionOldY, actionCurrentX, actionCurrentY, waveSpeed, xGridNum, yGridNum;//, offSetX
	private long[] fileDataIndexArray = new long[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	public static int fileDataCount;
	private RandomAccessFile[] fis = new RandomAccessFile[LEADNUM];
	private Map<Integer, int[]> bufferMap;
	private SampleDotIntNew sampleDot[];
	private String leadName[] = { "I", "II", "III", "avR", "avL", "avF", "V1", "V2", "V3", "V4", "V5", "V6" };
	private int oldY[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	final static int MARGIN = 4;
	final static int GRID_SIZE = 5;
	private int heartRate, fenbianlv;
	private int screenHeight, screenWidth;
	private int baseY[], baseX[];
	private final String filePath = StringUtil.getSDPath()+"/ECGDATA/Data2Device/";
	private ReviewECGWaveActivity ctx;
	private int hPixels, wPixels;
	
	@SuppressLint("UseSparseArrays")
	public ECGDataReview(ReviewECGWaveActivity context, ECGDataLong2Device edl2Device) {
		super(context);
		ctx = context;
		hPixels = ctx.getResources().getDisplayMetrics().heightPixels;
		wPixels = ctx.getResources().getDisplayMetrics().widthPixels;
		CalXYGridNum(5, wPixels, hPixels);
		if (hPixels<=800) {
			fenbianlv = 1;
			fileDataCount = (xGridNum-25)*2 * 4 * 2;
		}else if (hPixels>=1000) {
			fenbianlv = 2;
			fileDataCount = xGridNum * 4 * 2;
		}
		holder = getHolder();
		holder.addCallback(this);
		initPaint();
		bufferMap = new HashMap<Integer, int[]>();
		try {
			if (edl2Device!=null) {
					fis[0] = new RandomAccessFile(filePath+edl2Device.getI_path(), "r");
					fis[1] = new RandomAccessFile(filePath+edl2Device.getII_path(), "r");
					fis[2] = new RandomAccessFile(filePath+edl2Device.getIII_path(), "r");
					fis[3] = new RandomAccessFile(filePath+edl2Device.getAVR_path(), "r");
					fis[4] = new RandomAccessFile(filePath+edl2Device.getAVL_path(), "r");
					fis[5] = new RandomAccessFile(filePath+edl2Device.getAVF_path(), "r");
					fis[6] = new RandomAccessFile(filePath+edl2Device.getV1_path(), "r");
					fis[7] = new RandomAccessFile(filePath+edl2Device.getV2_path(), "r");
					fis[8] = new RandomAccessFile(filePath+edl2Device.getV3_path(), "r");
					fis[9] = new RandomAccessFile(filePath+edl2Device.getV4_path(), "r");
					fis[10] = new RandomAccessFile(filePath+edl2Device.getV5_path(), "r");
					fis[11] = new RandomAccessFile(filePath+edl2Device.getV6_path(), "r");
					Log.e(VIEW_LOG_TAG, "文件长度："+fis[0].length());
					long availcount0 = fis[0].length();
					if ( availcount0 < fileDataCount) {
						int availCount = (int) availcount0;
						for (int i = 0; i < LEADNUM; i++) {
							byte[] buffer = new byte[availCount];
							fis[i].seek(fileDataIndexArray[i]);
							fileDataIndexArray[i] = fis[i].read(buffer);
							bufferMap.put(i, DataUtil.toIntArray(buffer));
						}
					}else { //获得初始化的数据
						for (int i = 0; i < LEADNUM; i++) {
							byte[] buffer = new byte[fileDataCount];
							fis[i].seek(fileDataIndexArray[i]);
							fileDataIndexArray[i] = fis[i].read(buffer);
							bufferMap.put(i, DataUtil.toIntArray(buffer));
						}
					}
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
		if (fenbianlv==1) {
			initDeSampleDot(125);
		}else if (fenbianlv==2) {
			initDeSampleDot(250);
		}
	}

	private void initPaint() {
		bigBGPaint = new Paint();
		bigBGPaint.setStrokeWidth((float) 1.5);
		bigBGPaint.setColor(getResources().getColor(R.color.ecg_back_blue));

		wavePaint = new Paint();
		wavePaint.setAntiAlias(true);
		
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(20f);
		
		smallBGPaint = new Paint();
		smallBGPaint.setColor(getResources().getColor(R.color.ecg_back_little));
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		screenHeight = height;
		screenWidth = width;
		InitYValue(width, height);
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	private void initDeSampleDot(int desDot){
		for (int i = 0; i < LEADNUM; i++) {
			sampleDot[i].setDesSampleDot(desDot);
		}
	}

	@Override
	public void run() {
		drawECGWave();
	}

	private void drawECGWave() {
		canvas = holder.lockCanvas();
        canvas.drawColor(ctx.getResources().getColor(R.color.ecg_back_color));
		DrawVerticalLine(fenbianlv*5);
		DrawHorizontalLine(fenbianlv*5);
		
		DrawWaveTag();
		wavePaint.setColor(Color.GREEN);
		DrawECGWave();
		
		if (canvas!=null) {
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	private void DrawECGWave() {
		for (int i = 0; i < LEADNUM; i++) {
			int[] bufferDatas = bufferMap.get(i);
			List<Integer> leadListData = sampleDot[i].SnapshotSample(bufferDatas);
			if (i==0) {
				canvas.drawText("总共 "+bufferDatas.length+" 点"+"  抽了 "+leadListData.size(), xGridNum-600, 500, textPaint);
			}
			int oldX = 0;
			int y = 0;
			for (int j = 0; j < leadListData.size(); j++) {
				y = (baseY[i] + ( - leadListData.get(j)) / (105 * 2) * fenbianlv);//(screenHeight / 550)
				if (j!=0) {
					canvas.drawLine(oldX + 5*MARGIN + baseX[i],
							oldY[i],
							j + 5*MARGIN + baseX[i],
							y,
							wavePaint);
				}
				oldY[i] = y;
				oldX = j;
			}
		}
	}

	private void DrawWaveTag() {
		for (int i = 0; i < LEADNUM; i++) {
			canvas.drawText(leadName[i], 10 + baseX[i], baseY[i], textPaint);
		}
//		long minute = fileDataIndexArray[0]/(60*500*4)+1;
//		if (minute>1440) {
//			minute = 1440;
//		}
		
		canvas.drawText("wPixels="+wPixels, xGridNum-200, 200, textPaint);
		canvas.drawText("xGridNum="+xGridNum, xGridNum-200, 300, textPaint);
		canvas.drawText("sWidth="+screenWidth, xGridNum-200, 400, textPaint);
		canvas.drawText("hPixels="+hPixels, 900, 260, textPaint);
		canvas.drawText(heartRate+" bpm", 900, 60, textPaint);
	}

	
	@SuppressLint("ClickableViewAccessibility") @Override
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
				for (int i = 0; i < fis.length; i++) {
					byte[] buffer = new byte[fileDataCount];
					try {
						if (fileDataIndexArray[i] == 0) {
							fis[i].seek(fileDataIndexArray[i]);
						}
						ret = fis[i].read(buffer);
						if (ret<0) {
							return ret;
						}
						if (ret<fileDataCount) {
							byte[] temp = new byte[ret];
							for (int j = 0; j < temp.length; j++) {
								temp[j] = buffer[j];
							}
							int[] ints = DataUtil.toIntArray(temp);
							bufferMap.put(i, ints);
						}else {
							fileDataIndexArray[i] += ret;
							int[] ints = DataUtil.toIntArray(buffer);
							bufferMap.put(i, ints);
						}
						Log.e(VIEW_LOG_TAG, "读了数据 = "+fileDataIndexArray[i]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}else if (b) {//wanghuikan
				for (int i = 0; i < fis.length; i++) {
					byte[] buffer = new byte[fileDataCount];
					try {
						fileDataIndexArray[i] = fileDataIndexArray[i] - fileDataCount;
						if (fileDataIndexArray[i] < 0) {
							fileDataIndexArray[i] = 0;
							return ret;
						}
						fis[i].seek(fileDataIndexArray[i]);
						ret = fis[i].read(buffer);
						if (ret<0) {
							return ret;
						}
						if (ret<fileDataCount) {
							byte[] temp = new byte[ret];
							for (int j = 0; j < temp.length; j++) {
								temp[j] = buffer[j];
							}
							int[] ints = DataUtil.toIntArray(temp);
							bufferMap.put(i, ints);
						}else {
							int[] ints = DataUtil.toIntArray(buffer);
							bufferMap.put(i, ints);
						}
						Log.e(VIEW_LOG_TAG, "读了数据 = "+fileDataIndexArray[i]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
		return ret;
	}

	public void setOffsetIndex(int offsetIndex) {
		offsetIndex = offsetIndex-1;
		if (offsetIndex >= 0) {
			for (int i = 0; i < fileDataIndexArray.length; i++) {
				fileDataIndexArray[i] = offsetIndex * 500 * 60 * 4;
				try {
					fis[i].seek(fileDataIndexArray[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			int dateResult = getFileData(false);
			if (dateResult>=0) {
				drawECGWave();
			}else {
				Toast.makeText(ctx, "没有数据了", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
