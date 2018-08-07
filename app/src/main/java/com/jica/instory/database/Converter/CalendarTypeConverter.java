package com.jica.instory.database.Converter;

import android.annotation.TargetApi;
import android.arch.persistence.room.TypeConverter;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;

// 나중에 아마 이걸로 바꾸는 게 좋을 듯

public class CalendarTypeConverter {

    @TargetApi(Build.VERSION_CODES.N)
    @TypeConverter
    public static Calendar toDate(Long number){
        return number == null ? null : Calendar.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @TypeConverter
    public static Long toLong(Calendar calendar){
        return calendar == null ? null : calendar.getTimeInMillis();
    }
}
