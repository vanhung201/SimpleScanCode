package hung.lv.simplescancode.action;

import android.content.Intent;
import android.net.Uri;

import com.huawei.hms.ml.scan.HmsScan;

public class DialAction {

    public static Intent getDialIntent(HmsScan.TelPhoneNumber telPhoneNumber) {
        Uri uri = Uri.parse("tel:" + telPhoneNumber.getTelPhoneNumber());
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        return intent;
    }
}
