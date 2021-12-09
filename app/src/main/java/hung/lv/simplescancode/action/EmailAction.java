package hung.lv.simplescancode.action;

import android.content.Intent;
import android.net.Uri;

import com.huawei.hms.ml.scan.HmsScan;

public class EmailAction {

    public static Intent getEmailInfo(HmsScan.EmailContent emailContent) {
        Uri uri = Uri.parse("mailto:" + emailContent.addressInfo);
        String[] tos = {emailContent.addressInfo};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_EMAIL, tos);
        intent.putExtra(Intent.EXTRA_SUBJECT, emailContent.subjectInfo);
        intent.putExtra(Intent.EXTRA_TEXT, emailContent.bodyInfo);
        return intent;
    }
}
