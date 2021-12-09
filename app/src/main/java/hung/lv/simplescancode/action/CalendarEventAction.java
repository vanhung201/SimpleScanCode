package hung.lv.simplescancode.action;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.huawei.hms.ml.scan.HmsScan;

public class CalendarEventAction {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Intent getCalendarEventIntent(HmsScan.EventInfo calendarEvent) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        try {
            intent.setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getTime(calendarEvent.beginTime))
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getTime(calendarEvent.closeTime))
                    .putExtra(CalendarContract.Events.TITLE, calendarEvent.getTheme())
                    .putExtra(CalendarContract.Events.DESCRIPTION, calendarEvent.getAbstractInfo())
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, calendarEvent.getPlaceInfo())
                    .putExtra(CalendarContract.Events.ORGANIZER, calendarEvent.getSponsor())
                    .putExtra(CalendarContract.Events.STATUS, calendarEvent.getCondition());
        } catch (NullPointerException e) {
            Log.w("getCalendarEventIntent", e);
        }
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static long getTime(HmsScan.EventTime calendarDateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarDateTime.getYear(), calendarDateTime.getMonth() - 1, calendarDateTime.getDay(),
                calendarDateTime.getHours(), calendarDateTime.getMinutes(), calendarDateTime.getSeconds());
        return calendar.getTime().getTime();
    }
}
