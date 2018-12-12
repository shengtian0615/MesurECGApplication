package com.wehealth.mesurecg.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class PreferUtils {

    public static PreferUtils instance;
    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "saveInfo";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;
    private PreferUtils(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt){
        if(instance == null){
            instance = new PreferUtils(cxt);
        }
    }
    public static PreferUtils getIntance(){
        if (instance == null){
            new RuntimeException("请先初始化");
        }
        return instance;
    }

    private String PREFER_ADDRESS = "prefer_address";
    private String PREFER_BDLOC_LATITUDE = "prefer_bdloc_latitude";
    private String PREFER_BDLOC_LONGITUDE = "prefer_bdloc_longitude";
    private String PREFER_SERIALNO = "prefer_serialno";
    private String PREFER_IDCARDNO = "prefer_idcardno";
    private String PREFER_ECGCHECK_FEE = "prefer_ecgcheckfee";
    private String PREFER_ECGCHECK_FEENAME = "prefer_ecgcheck_feename";
    private String PREFER_THIRDAGENCY = "prefer_thirdagency";
    private String PREFER_SERVERURL = "prefer_serverurl";
    private String PREFER_HOSPIAL= "prefer_hospial";
    private String PREFER_VERSION = "prefer_new_version";
    private String PREFER_SAVE_ECGSTYLE = "prefer_save_ecgstyle";
    private String PREFER_DISPALY_STYLE = "prefer_display_ecgwave";
    private String PREFER_PLAY_VIDEO = "prefer_play_video";
    private String PREFER_BPMAC_PRESS_ADDRESS = "prefer_bpmac_press_address";
    private String PREFER_ECGDEVICE_ADDMAC = "prefer_ecgdevice_addmac";
    private String PREFER_ECGWAVE_PACE = "prefer_ecgwave_pace";

    public void setAddress(String address){
        editor.putString(PREFER_ADDRESS, address).commit();
    }
    public String getAddress(){
        return mSharedPreferences.getString(PREFER_ADDRESS, "");
    }

    public void setSerialNo(String address){
        editor.putString(PREFER_SERIALNO, address).commit();
    }
    public String getSerialNo(){
        return mSharedPreferences.getString(PREFER_SERIALNO, "");
    }

    public void setIdCardNo(String address){
        editor.putString(PREFER_IDCARDNO, address).commit();
    }
    public String getIdCardNo(){
        return mSharedPreferences.getString(PREFER_IDCARDNO, "");
    }

    public void setECGCheckFeeName(String feeName){
        editor.putString(PREFER_ECGCHECK_FEENAME, feeName).commit();
    }
    public String getECGCheckFeeName(){
        return mSharedPreferences.getString(PREFER_ECGCHECK_FEENAME, "");
    }

    public void setThirdAgency(String serialNo, String feeName){
        editor.putString(serialNo+"THIRD_AGENCY", feeName).commit();
    }
    public String getThirdAgency(String serialNo){
        return mSharedPreferences.getString(serialNo+"THIRD_AGENCY", "");
    }

    public void setECGCheckFee(int feeName){
        editor.putInt(PREFER_ECGCHECK_FEE, feeName).commit();
    }
    public int getECGCheckFee(){
        return mSharedPreferences.getInt(PREFER_ECGCHECK_FEE, 20);
    }

    public void setAgreECG(String key, boolean agree){
        editor.putBoolean(key+"AGREE_ECG", agree).commit();
    }
    public boolean isAgreECG(String key){
        return mSharedPreferences.getBoolean(key+"AGREE_ECG", true);
    }

    public void setPlayVideo(String key, boolean playVideo){
        editor.putBoolean(key+"PLAY_VIDEO", playVideo).commit();
    }
    public boolean isPlayVideo(String key){
        return mSharedPreferences.getBoolean(key+"PLAY_VIDEO", true);
    }

    public void setServerUrl(String url) {
        editor.putString(PREFER_SERVERURL, url).commit();
    }

    public String getServerUrl(){
        return mSharedPreferences.getString(PREFER_SERVERURL,"https://api.5wehealth.com/ECGPlatformService/jaxrs");
    }

    public void setPassword(String idCardNo, String password) {
        if (TextUtils.isEmpty(password)){
            editor.remove(idCardNo).commit();
        }else{
            editor.putString(idCardNo, password).commit();
        }

    }

    public String getPassword(String idCardNo){
        return mSharedPreferences.getString(idCardNo,"");
    }

    public String getHospial() {
        return mSharedPreferences.getString(PREFER_HOSPIAL,"");
    }

    public void setHospial(String hospial){
        editor.putString(PREFER_HOSPIAL, hospial).commit();
    }

    public void setVersion(boolean haveVersion) {
        editor.putBoolean(PREFER_VERSION, haveVersion).commit();
    }

    public boolean getVersion(){
        return mSharedPreferences.getBoolean(PREFER_VERSION, false);
    }

    /**
     * 	设置 保存文件的方式：2为24小时模式；  1为人工Manual； 0为自动 Auto:10秒数据
     * @param saveFileStyle
     */
    public void setSaveFileStyle(int saveFileStyle){
        editor.putInt(PREFER_SAVE_ECGSTYLE, saveFileStyle).commit();
    }

    /**
     * 	设置 保存文件的方式：2为24小时模式；  1为人工Manual； 0为自动 Auto:10秒数据
     * @return
     */
    public int getSaveFileStyle(){
        return mSharedPreferences.getInt(PREFER_SAVE_ECGSTYLE, 0);
    }

    /**
     * 设置为0, 6ch*2 模式; 为1, 12ch*1模式；   为2， 1ch*1模式
     */
    public void setDisplayStyle(int ecgWave) {
        editor.putInt(PREFER_DISPALY_STYLE, ecgWave).commit();
    }

    /**
     * 返回为0, 6ch*2 模式; 返回为1, 12ch*1模式；   返回为2， 1ch*1模式
     */
    public int getDisplayStyle() {
        return mSharedPreferences.getInt(PREFER_DISPALY_STYLE, 0);
    }

    public boolean getPlayVideo() {
        return mSharedPreferences.getBoolean(PREFER_PLAY_VIDEO, false);
    }

    public void setPlayVideo(boolean isPlayVideo){
        editor.putBoolean(PREFER_PLAY_VIDEO, isPlayVideo).commit();
    }

    public String getBPBlueToothMac() {
        return mSharedPreferences.getString(PREFER_BPMAC_PRESS_ADDRESS, "");
    }

    public void setBPBlueToothMac(String bpMacAddress) {
        editor.putString(PREFER_BPMAC_PRESS_ADDRESS, bpMacAddress).commit();
    }

    public String getLatitude() {
        return mSharedPreferences.getString(PREFER_BDLOC_LATITUDE,"");
    }

    public void setLatitude(String latitude){
        editor.putString(PREFER_BDLOC_LATITUDE, latitude).commit();
    }

    public String getLongitude(){
        return mSharedPreferences.getString(PREFER_BDLOC_LONGITUDE,"");
    }

    public void setLongitude(String longitude){
        editor.putString(PREFER_BDLOC_LONGITUDE, longitude).commit();
    }

    public void setECGDeviceBTMAC(String ecgDeviceBTMAC) {
        editor.putString(PREFER_ECGDEVICE_ADDMAC, ecgDeviceBTMAC).commit();
    }

    public String getECGDeviceBTMAC() {
        return mSharedPreferences.getString(PREFER_ECGDEVICE_ADDMAC, "");
    }

    public int getPace() {
        return mSharedPreferences.getInt(PREFER_ECGWAVE_PACE,0);
    }

    public void setPace(int pace){
        editor.putInt(PREFER_ECGWAVE_PACE, pace).commit();
    }

}
