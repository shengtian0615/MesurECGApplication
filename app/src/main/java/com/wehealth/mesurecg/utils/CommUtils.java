package com.wehealth.mesurecg.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.util.NetWorkService;

public class CommUtils {

    public static AuthToken refreshToken(){
        AuthToken token = MeasurECGApplication.getInstance().getToken();
        AuthToken authToken = NetWorkService.getAuthToken(token, PreferUtils.getIntance().getServerUrl());
        if (authToken!=null){
            MeasurECGApplication.getInstance().setToken(authToken);
            token = authToken;
        }
        return token;
    }

    /**
     * 检测该包名所对应的应用是否存在
     * @param packageName
     * @return
     */
    public static boolean checkPackage(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static int getVersionCode(Context context, String packageName){
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info==null){
            return -1;
        }
        return info.versionCode;
    }
}
