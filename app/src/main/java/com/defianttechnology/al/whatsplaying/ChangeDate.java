package com.defianttechnology.al.whatsplaying;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Al
 */
public class ChangeDate {
    
    public String changeDate(int myDays) {
        String formatedDate = null;
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, myDays);  // number of days to add
        Integer newDate =     (c.get(Calendar.YEAR) * 10000) + 
                            ((c.get(Calendar.MONTH) + 1) * 100) + 
                            (c.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = originalFormat.parse(newDate.toString());
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
            formatedDate = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }
}
