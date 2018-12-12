package com.wehealth.mesurecg.utils;

import com.wehealth.mesurecg.activity.ECGMeasureActivity;
import com.wehealth.model.util.DataUtil;
import com.wehealth.model.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ByteFileUtil {

	/**将byte数组保存在文件中**/
	public static void saveByteContent(String fileName, byte[] buffer){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName, true);
			fos.write(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if (fos!=null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**读取数据到byte数组里**/
	public static byte[] readByteContent(String fileName, int byteOffset, int byteCount){
		byte[] buffer = new byte[byteCount];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			fis.read(buffer, byteOffset, byteCount);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if (fis!=null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}
	/**
	 * 0:I; 1:II; 2:III; 3:aVR; 4:aVL 5:aVF; 6:v1; 7:v2; 8:v3; 9:v4; 10:v5; 11:v6
	 * **/
	public static void saveInts(long ecg2Device_time, Map<Integer, int[]> datas) {
		if (ecg2Device_time < 0) {
			return;
		}
		String path = StringUtil.getSDPath()+"/ECGDATA/Data2Device/";
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		File fileV1 = new File(path+ecg2Device_time+"_v1.txt");
		File fileV2 = new File(path+ecg2Device_time+"_v2.txt");
		File fileV3 = new File(path+ecg2Device_time+"_v3.txt");
		File fileV4 = new File(path+ecg2Device_time+"_v4.txt");
		File fileV5 = new File(path+ecg2Device_time+"_v5.txt");
		File fileV6 = new File(path+ecg2Device_time+"_v6.txt");
		File fileAVF = new File(path+ecg2Device_time+"_avf.txt");
		File fileAVL = new File(path+ecg2Device_time+"_avl.txt");
		File fileAVR = new File(path+ecg2Device_time+"_avr.txt");
		File fileI = new File(path+ecg2Device_time+"_i.txt");
		File fileII = new File(path+ecg2Device_time+"_ii.txt");
		File fileIII = new File(path+ecg2Device_time+"_iii.txt");
		saveByteContent(fileI.getAbsolutePath(), DataUtil.toByteArray(datas.get(0)));
		saveByteContent(fileII.getAbsolutePath(), DataUtil.toByteArray(datas.get(1)));
		saveByteContent(fileIII.getAbsolutePath(), DataUtil.toByteArray(datas.get(2)));
		saveByteContent(fileAVR.getAbsolutePath(), DataUtil.toByteArray(datas.get(3)));
		saveByteContent(fileAVL.getAbsolutePath(), DataUtil.toByteArray(datas.get(4)));
		saveByteContent(fileAVF.getAbsolutePath(), DataUtil.toByteArray(datas.get(5)));
		saveByteContent(fileV1.getAbsolutePath(), DataUtil.toByteArray(datas.get(6)));
		saveByteContent(fileV2.getAbsolutePath(), DataUtil.toByteArray(datas.get(7)));
		saveByteContent(fileV3.getAbsolutePath(), DataUtil.toByteArray(datas.get(8)));
		saveByteContent(fileV4.getAbsolutePath(), DataUtil.toByteArray(datas.get(9)));
		saveByteContent(fileV5.getAbsolutePath(), DataUtil.toByteArray(datas.get(10)));
		saveByteContent(fileV6.getAbsolutePath(), DataUtil.toByteArray(datas.get(11)));
		StringUtil.writException2File("/sdcard/manual.txt", "\n 文件是否存在："+fileV1.exists()+"   文件长度="+fileV1.length());
	}
	
	/**
	 * 0:I; 1:II; 2:III; 3:aVR; 4:aVL 5:aVF; 6:v1; 7:v2; 8:v3; 9:v4; 10:v5; 11:v6
	 * **/
	public static void saveIntegers(long ecg2Device_time, Map<Integer, Integer[]> datas) {
		if (ecg2Device_time < 0) {
			return;
		}
		String path = StringUtil.getSDPath()+"/ECGDATA/Data2Device/";
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		File fileV1 = new File(path+ecg2Device_time+"_v1.txt");
		File fileV2 = new File(path+ecg2Device_time+"_v2.txt");
		File fileV3 = new File(path+ecg2Device_time+"_v3.txt");
		File fileV4 = new File(path+ecg2Device_time+"_v4.txt");
		File fileV5 = new File(path+ecg2Device_time+"_v5.txt");
		File fileV6 = new File(path+ecg2Device_time+"_v6.txt");
		File fileAVF = new File(path+ecg2Device_time+"_avf.txt");
		File fileAVL = new File(path+ecg2Device_time+"_avl.txt");
		File fileAVR = new File(path+ecg2Device_time+"_avr.txt");
		File fileI = new File(path+ecg2Device_time+"_i.txt");
		File fileII = new File(path+ecg2Device_time+"_ii.txt");
		File fileIII = new File(path+ecg2Device_time+"_iii.txt");
		saveByteContent(fileI.getAbsolutePath(), DataUtil.toByteArray(datas.get(0)));
		saveByteContent(fileII.getAbsolutePath(), DataUtil.toByteArray(datas.get(1)));
		saveByteContent(fileIII.getAbsolutePath(), DataUtil.toByteArray(datas.get(2)));
		saveByteContent(fileAVR.getAbsolutePath(), DataUtil.toByteArray(datas.get(3)));
		saveByteContent(fileAVL.getAbsolutePath(), DataUtil.toByteArray(datas.get(4)));
		saveByteContent(fileAVF.getAbsolutePath(), DataUtil.toByteArray(datas.get(5)));
		saveByteContent(fileV1.getAbsolutePath(), DataUtil.toByteArray(datas.get(6)));
		saveByteContent(fileV2.getAbsolutePath(), DataUtil.toByteArray(datas.get(7)));
		saveByteContent(fileV3.getAbsolutePath(), DataUtil.toByteArray(datas.get(8)));
		saveByteContent(fileV4.getAbsolutePath(), DataUtil.toByteArray(datas.get(9)));
		saveByteContent(fileV5.getAbsolutePath(), DataUtil.toByteArray(datas.get(10)));
		saveByteContent(fileV6.getAbsolutePath(), DataUtil.toByteArray(datas.get(11)));
		StringUtil.writException2File("/sdcard/manual.txt", "\n 文件是否存在："+fileV1.exists()+"   文件长度="+fileV1.length());
	}
	
//	/**
//	 * 24小时数据保存
//	 * **/
//	public static void save24HInts(long ecg2Device_time, List<Integer> datas) {
//		if (ecg2Device_time < 0) {
//			return;
//		}
//		try {
//			String path = StringUtils.getSDPath()+"/ECGDATA/Data2Device/";
//			File fileDir = new File(path);
//			if (!fileDir.exists()) {
//				fileDir.mkdirs();
//			}
//			File file24h = new File(path+ecg2Device_time+"_h24.txt");
//			Integer[] data = new Integer[datas.size()];
//			Integer[] da = datas.toArray(data);
//			saveByteContent(file24h.getAbsolutePath(), DataUtil.toByteArray(da));
////			StringUtils.writException2File("/sdcard/h24.txt", "\n数据的大小："+datas.size());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 24小时数据保存
	 * **/
	public static void save24HInts(long ecg2Device_time, Integer[] datas) {
		if (ecg2Device_time < 0) {
			return;
		}
		try {
			String path = StringUtil.getSDPath()+"/ECGDATA/Data2Device/Hours24/";
			File fileDir = new File(path);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			int count = ECGMeasureActivity.measureActivity.saveFileManualCount;
			count = count - 1;
			int c = count/60;
			path = path+ecg2Device_time+"_"+c+".txt";
			String content = "序号:"+(count-c*60)+path+" saveFileManualCount="+count+"\n ";
			StringUtil.writException2File("/sdcard/h24_path16.txt", content);
			File file24h = new File(path);
			saveByteContent(file24h.getAbsolutePath(), DataUtil.toByteArray(datas));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**将intNew的数组移入source数组的后面，source数组前面的丢弃**/
	public static int[] offsetDatas(int[] source, int[] intNew){
		for (int i = 0; i < source.length; i++) {
			if ((i+intNew.length) < source.length) {
				source[i] = source[i+intNew.length];
			}else {
				source[i] = intNew[i+intNew.length-source.length];
			}
		}
		return source;
	}
	
	/**将intNew的集合中数据移入source集合的后面，source数组前面的丢弃**/
	public static List<Integer> offsetDatas(List<Integer> source, List<Integer> intNew){
		for (int i = 0; i < source.size(); i++) {
			if ((i+intNew.size()) < source.size()) {
				source.add(i, source.get(i + intNew.size()));
			}else {
				source.add(intNew.get(i + intNew.size() - source.size()));
			}
		}
		return source;
	}

//	/**读取流中的数据到集合中**/
//	public static List<int[]> obtainInitData(FileInputStream[] fis) {
//		List<int[]> list = new ArrayList<int[]>();
//		int byteCount = 2500 * 4;
//		try {
//			for (int i = 0; i < fis.length; i++) {
//				byte[] buffer = new byte[byteCount];
//				fis[i].read(buffer, 0, byteCount);
//				list.add(i, DataUtil.toIntArray(buffer));	
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
}
