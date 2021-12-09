package hung.lv.simplescancode.action;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;

import com.huawei.hms.ml.scan.HmsScan;

public class LocationAction {

    private static String TAG = "LocationAction";

    public final static String GAODE_PKG = "com.autonavi.minimap";

    public static boolean checkMapAppExist(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(GAODE_PKG, 0);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }

    }

    public static Intent getLoactionInfo(HmsScan.LocationCoordinate locationCoordinate) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("androidamap://viewMap?lat=" + locationCoordinate.getLatitude() + "&lon=" + locationCoordinate.getLongitude()));
        return intent;
    }
}
