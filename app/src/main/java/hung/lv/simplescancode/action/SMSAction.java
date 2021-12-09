package hung.lv.simplescancode.action;

import android.content.Intent;
import android.net.Uri;

import com.huawei.hms.ml.scan.HmsScan;

public class SMSAction {

    public static Intent getSMSInfo(HmsScan.SmsContent smsContent) {
        Uri uri = Uri.parse("smsto:" + smsContent.getDestPhoneNumber());
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", smsContent.getMsgContent());
        return intent;
    }
}
