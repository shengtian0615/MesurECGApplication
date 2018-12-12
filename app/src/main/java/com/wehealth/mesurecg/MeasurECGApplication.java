package com.wehealth.mesurecg;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.wehealth.mesurecg.dao.OAuthTokenDao;
import com.wehealth.mesurecg.service.EaseUI;
import com.wehealth.mesurecg.utils.LocationService;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.urion.BluetoothService;
import com.wehealth.urion.IBean;
import com.wehealth.urion.ICallback;
import com.wehealth.urion.Msg;
import com.wehealth.urion.Error;

public class MeasurECGApplication extends Application {

    public LocationService locationService;
    private AuthToken token = null;
    private static MeasurECGApplication instance;
    private String serverHost, serverHostPath;
    private RegisteredUser registerUser;
    private BDLocation lastLocation = null;

    private BluetoothService service;

    private ICallback call;

    private Handler mmHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        /**
         初始化定位sdk，建议在Application中创建
         **/
        locationService = new LocationService(getApplicationContext());

        PreferUtils.init(instance);

        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        serverHost = applicationInfo.metaData.getString("SERVER_HOST");
        serverHostPath = applicationInfo.metaData.getString("SERVER_HOST_URL");
        PreferUtils.getIntance().setServerUrl(serverHost+serverHostPath);

//        initSDK();

        EaseUI.getInstance().init(instance);
        createHandler();
        setupChat();
        service.setHandler(mmHandler);
    }

//    private void initSDK() {
//
//        HHSDKOptions options = HHSDKOptions.defaultSoundOption("8241"); //productId是和缓分配的产品Id
//        options.isDebug = true;
////        options.mImei = "xxxxxxxxxxxxxx";//设备编号，多用于音箱设备对接
//        options.dev = true;
//        options.mOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//
//        HHDoctor.init(getApplicationContext(), options);
//    }

    public static MeasurECGApplication getInstance() {
        return instance;
    }

    public AuthToken getToken() {
        String idCardNo = PreferUtils.getIntance().getIdCardNo();
        if (token==null && !TextUtils.isEmpty(idCardNo)) {
            token = OAuthTokenDao.getIntance(idCardNo).getAuthToken();
        }
        return token;
    }

    public void setToken(AuthToken token) {
        this.token = token;
        String idCardNo = PreferUtils.getIntance().getIdCardNo();
        if (!TextUtils.isEmpty(idCardNo)){
            OAuthTokenDao.getIntance(idCardNo).saveAuthToken(token);
        }
    }

    public RegisteredUser getRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(RegisteredUser registerUser) {
        this.registerUser = registerUser;
    }

    public BDLocation getLocation() {
        return lastLocation;
    }

    public void setLocation(BDLocation location){
        this.lastLocation = location;
    }

    public void setupChat(){
        if (service == null)
            service = new BluetoothService();
    }

    public ICallback getCall() {
        return call;
    }

    public void setCall(ICallback call) {
        this.call = call;
    }

    public BluetoothService getService() {
        return service;
    }


    private void createHandler() {
        mmHandler = new Handler() {
            public void handleMessage(Message msg) {
                IBean bean = msg.getData().getParcelable("bean");
                if (call != null)
                    switch (msg.what) {
                        case IBean.ERROR:
                            getCall().onError((Error) bean);
                            break;
                        case IBean.MESSAGE:
                            getCall().onMessage((Msg) bean);
                            break;
                        case IBean.DATA:
                            getCall().onReceive(bean);
                            break;
                    }
            }
        };
    }

}
