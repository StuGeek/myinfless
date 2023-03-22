// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.tools;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.text.ParseException;
import java.util.regex.Pattern;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateFormats
{
    private SimpleDateFormat sdf;
    private SimpleDateFormat sdf1;
    private Calendar calendar;
    private static DateFormats dateFormat;
    
    static {
        DateFormats.dateFormat = null;
    }
    
    private DateFormats() {
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.sdf1 = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        this.calendar = Calendar.getInstance();
    }
    
    public static synchronized DateFormats getInstance() {
        if (DateFormats.dateFormat == null) {
            DateFormats.dateFormat = new DateFormats();
        }
        return DateFormats.dateFormat;
    }
    
    public String getNowDate() {
        final Date d = new Date();
        try {
            return this.sdf.format(d);
        }
        catch (Exception e) {
            return "";
        }
    }
    
    public static void main(final String[] args) {
        System.out.println(new DateFormats().getNowDate1());
        System.out.println(new DateFormats().getNowDate1());
    }
    
    public String getNowDate1() {
        final Date d = new Date();
        try {
            return this.sdf1.format(d);
        }
        catch (Exception e) {
            return "";
        }
    }
    
    public long dateStringToTime(final String date) {
        try {
            final String hourEL = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}$";
            final String dateEL = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
            final String mouthEL = "^[0-9]{4}-[0-9]{2}$";
            final Pattern ph = Pattern.compile(hourEL);
            final Matcher mh = ph.matcher(date);
            final boolean dateFlagH = mh.matches();
            final Pattern pd = Pattern.compile(dateEL);
            final Matcher md = pd.matcher(date);
            final boolean dateFlagD = md.matches();
            final Pattern pm = Pattern.compile(mouthEL);
            final Matcher mm = pm.matcher(date);
            final boolean dateFlagM = mm.matches();
            SimpleDateFormat s = null;
            Date d = null;
            if (dateFlagH) {
                s = new SimpleDateFormat("yyyy-MM-dd HH");
                d = s.parse(date);
            }
            else if (dateFlagD) {
                s = new SimpleDateFormat("yyyy-MM-dd");
                d = s.parse(date);
            }
            else if (dateFlagM) {
                s = new SimpleDateFormat("yyyy-MM");
                d = s.parse(date);
            }
            else {
                s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                d = s.parse(date);
            }
            return d.getTime();
        }
        catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }
    
    public String LongToDate(final long now) {
        this.calendar.setTimeInMillis(now);
        return this.sdf.format(this.calendar.getTime());
    }
    
    public int getDaysByYearMonth(final String year, String month) {
        if (month.length() == 2 && month.startsWith("0")) {
            month = month.substring(1, 2);
        }
        this.calendar.set(1, Integer.parseInt(year));
        this.calendar.set(2, Integer.parseInt(month) - 1);
        this.calendar.set(5, 1);
        this.calendar.roll(5, -1);
        return this.calendar.get(5);
    }
    
    public int getDaysByYear(final String year) {
        return new GregorianCalendar().isLeapYear(Integer.parseInt(year)) ? 366 : 365;
    }
}
