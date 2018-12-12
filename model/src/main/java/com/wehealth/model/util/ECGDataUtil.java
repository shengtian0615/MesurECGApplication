package com.wehealth.model.util;

import com.wehealth.model.domain.model.ECGData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ECGDataUtil
{

	public static class ECGDataResultHelper
	{
		/** code:name */
		private Map<String, String> classfy = new HashMap<String, String>();
		/** code:name */
		private Map<String, String> result = new HashMap<String, String>();
		
		public Map<String, String> getClassfy()
		{
			return classfy;
		}
		public void setClassfy(Map<String, String> classfy)
		{
			this.classfy = classfy;
		}
		public Map<String, String> getResult()
		{
			return result;
		}
		public void setResult(Map<String, String> result)
		{
			this.result = result;
		}
		
	}
	
	
	public static ECGDataResultHelper decodeResult(String result) throws Exception
	{
		JSONObject obj;
		try
		{
			obj = new JSONObject(result);
		}
		catch(Exception ex)
		{
			throw new Exception("Not Json Format");
		}
		
		ECGDataResultHelper helper = new ECGDataResultHelper();
		
		boolean hasClassfy = obj.has("classfy");
		if(hasClassfy)
		{
			JSONArray classArray = obj.getJSONArray("classfy");
			for(int i = 0; i < classArray.length(); i++)
			{
				JSONObject element = classArray.getJSONObject(i);
				helper.getClassfy().put(element.getString("code"), element.getString("name"));
			}
		}
		
		boolean hasResult = obj.has("result");
		if(hasResult)
		{
			JSONArray resArray = obj.getJSONArray("result");
			for(int i = 0; i < resArray.length(); i++)
			{
				JSONObject element = resArray.getJSONObject(i);
				helper.getResult().put(element.getString("code"), element.getString("name"));
			}
		}
		
		return helper;
	}
	
	/**
	 * 
	 * 结果是JSON格式的字符串，格式的样式如
		{
			"result":[
				{"name":"心动过速","code":"8001"},
				{"name":"房颤","code":"8002"},
				{"name":"心率不齐","code":"8003"}
			],
			"classfy":[
				{"name":"正常心电图","code":"800"},
				{"name":"正常心电图+1","code":"900"},
				{"name":"正常心电图+2","code":"1000"}
			]
		}
	*
	*/
	public static String encodeResult(ECGDataResultHelper helper)
	{
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			if(!helper.getClassfy().isEmpty())
			{
				JSONArray array = new JSONArray();
				obj.put("classfy", array);
				
				Map<String, String> classfy = helper.getClassfy();
				for(String code : classfy.keySet())
				{
					JSONObject element = new JSONObject();
					array.put(element);
					
					String name = classfy.get(code);
					element.put("code", code);
					element.put("name", name);
				}
			}
			
			if(!helper.getResult().isEmpty())
			{
				JSONArray array = new JSONArray();
				obj.put("result", array);
				
				Map<String, String> result = helper.getResult();
				for(String code : result.keySet())
				{
					JSONObject element = new JSONObject();
					array.put(element);
					
					String name = result.get(code);
					element.put("code", code);
					element.put("name", name);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj.toString();
	}
	
	/**将心电分析类的Json解析**/
	public static String getClassByJson(String classStr){
		String classRes = "";
		try {
			Iterator<Entry<String, String>> itc = decodeResult(classStr).getClassfy().entrySet().iterator();
			while(itc.hasNext()){
				Entry<String, String> entry = itc.next();
				classRes += entry.getValue()+" ";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return classRes;
		}
		return classRes;
	}
	
	/**将心电分析结论的Json格式解析**/
	public static String getResuByJson(String resultStr){
		String result = "";
		try {
			Iterator<Entry<String, String>> itc = decodeResult(resultStr).getResult().entrySet().iterator();
			while(itc.hasNext()){
				Entry<String, String> entry = itc.next();
				result += entry.getValue()+" ";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	public static void main(String[] args)
	{
		
		//Test
		ECGDataResultHelper helper = new ECGDataResultHelper();
		helper.getClassfy().put("800", "正常心电图");
		helper.getClassfy().put("900", "正常心电图+1");
		helper.getClassfy().put("1000", "正常心电图+2");
		
		helper.getResult().put("8001", "心动过速");
		helper.getResult().put("8002", "房颤");
		helper.getResult().put("8003", "心率不齐");
		
		String result = encodeResult(helper);
		try
		{
			ECGDataResultHelper res = decodeResult(result);
			res.getClassfy();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(result);
	}
	
	
	public static ECGData gZip(ECGData from)
	{
		from.setV1(ZipUtil.gZip(from.getV1()));
		from.setV2(ZipUtil.gZip(from.getV2()));
		from.setV3(ZipUtil.gZip(from.getV3()));
		from.setV4(ZipUtil.gZip(from.getV4()));
		from.setV5(ZipUtil.gZip(from.getV5()));
		from.setV6(ZipUtil.gZip(from.getV6()));
		from.setvI(ZipUtil.gZip(from.getvI()));
		from.setvII(ZipUtil.gZip(from.getvII()));
		from.setvIII(ZipUtil.gZip(from.getvIII()));
		from.setaVr(ZipUtil.gZip(from.getaVr()));
		from.setaVl(ZipUtil.gZip(from.getaVl()));
		from.setaVf(ZipUtil.gZip(from.getaVf()));
		
		return from;
	}
	
	
	public static ECGData ungZip(ECGData from)
	{
		try
		{
			from.setV1(ZipUtil.unGZip(from.getV1()));
			from.setV2(ZipUtil.unGZip(from.getV2()));
			from.setV3(ZipUtil.unGZip(from.getV3()));
			from.setV4(ZipUtil.unGZip(from.getV4()));
			from.setV5(ZipUtil.unGZip(from.getV5()));
			from.setV6(ZipUtil.unGZip(from.getV6()));
			from.setvI(ZipUtil.unGZip(from.getvI()));
			from.setvII(ZipUtil.unGZip(from.getvII()));
			from.setvIII(ZipUtil.unGZip(from.getvIII()));
			from.setaVr(ZipUtil.unGZip(from.getaVr()));
			from.setaVl(ZipUtil.unGZip(from.getaVl()));
			from.setaVf(ZipUtil.unGZip(from.getaVf()));
		}
		catch(Exception e)
		{
			System.out.println("Not in GZip Format");
			return from;
		}
		return from;
	}
	
	/*
	private static void copyFrom(ECGData from, ECGData to)
	{
		to.setId(from.getId());
		to.setEquipmentSerialNo(from.getEquipmentSerialNo());
		to.setTime(from.getTime());
		to.setAutoDiagnosisResult(from.getAutoDiagnosisResult());
		to.setPatientId(from.getPatientId());
		to.setRegisteredUserId(from.getRegisteredUserId());
		to.setRequestedDiagnosisType(from.getRequestedDiagnosisType());
		to.setDoctorId(from.getDoctorId());
		to.setManulDiagnosisResult(from.getManulDiagnosisResult());
		to.setLatitude(from.getLatitude());
		to.setLongitude(from.getLongitude());
		to.setHeartRate(from.getHeartRate());
		to.setEcgFilter(from.getEcgFilter());
		to.setUpdateTime(from.getUpdateTime());
		to.setDoctorComment(from.getDoctorComment());
	}*/
}
