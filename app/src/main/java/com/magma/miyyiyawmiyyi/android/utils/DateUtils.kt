package com.magma.miyyiyawmiyyi.android.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {

        /**
         * Default date format
         * */
        public const val usedFormat = "yyyy-MM-dd'T'hh:mm:ss"
        /**
         * All date formats
         * */
        private val formats = arrayOf(
            "EEE MMM dd HH:mm:ss Z yyyy", "dd/MM/yyyy hh.mm aa",
            "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
            "MM/dd/yyyy'T'HH:mm:ss.SSSZ", "MM/dd/yyyy'T'HH:mm:ss.SSS",
            "MM/dd/yyyy'T'HH:mm:ssZ", "MM/dd/yyyy'T'HH:mm:ss",
            "yyyy:MM:dd HH:mm:ss", "MMM dd, yyyy hh:mm:ss aa",
            "dd-MMM-yyyy", "yyyy/MM/dd",
            "yyyy-MM-dd", "yyyyMMdd")

        /**
         * Gets the format of the passed date.
         * @param date: date value
         * */
        fun getFormat(date: String): String{
            for (parse in formats) {
                val sdf = SimpleDateFormat(parse)
                try {
                    sdf.parse(date)
                    return parse
                } catch (e: ParseException) {
                }
            }
            return ""
        }

        fun formatYear(yob: String?): String? {
            var time: Long = 0
            val simpleDateFormat = SimpleDateFormat(usedFormat, Locale.US)
            try {
                time = SimpleDateFormat("yyyy", Locale.US).parse(yob).time
                //  simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            //1949-12-31 10:00:00
            //2004-06-26 00:00:00
            return simpleDateFormat.format(time)
        }

        /**
         * Change format with choosing the both (old & new) formats.
         * */
        fun changeFormatFromTo(oldFormat: String, oldText: String, newFormat: String): String {
            val oldObj = getObjFromStr(oldText, oldFormat)
            return getStringDate(oldObj, newFormat)
        }

        /**
         * Get the date with String type.
         * @param obj: the date of Date type.
         * @param format: the format of the date.
         * */
        fun getStringDate(obj: Date, format: String = usedFormat):String{
            val formatter = SimpleDateFormat(format, Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.format(obj)
        }

        /**
         * Get the date with Date type.
         * @param text: the date of String type.
         * @param format: the format of the date.
         * */
        fun getObjFromStr(text: String, format: String):Date{
            return try{
                val formatter = SimpleDateFormat(format)
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                formatter.parse(text)
            }catch (e: ParseException){
                val newFormat = getFormat(text)
                val formatter = SimpleDateFormat(newFormat)
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                formatter.parse(text)
            }
        }

        fun formatDateTimeToLong(dateTime: String?): Long {
            var time: Long = 0
            //"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            try {
                time = simpleDateFormat.parse(dateTime).time
                //  simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time
        }


        fun formatLongToCountDown(time: Long): String? {
//        time -= 24*60*60*1000;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("  D    |  hh : mm : ss", Locale.US);
////          simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            val c = Calendar.getInstance()
            c.timeZone = TimeZone.getTimeZone("GMT")
            c.timeInMillis = time
            return String.format(
                Locale.US, "%02d:%02d:%02d:%02d", c[Calendar.DAY_OF_YEAR] - 1,
                c[Calendar.HOUR_OF_DAY], c[Calendar.MINUTE], c[Calendar.SECOND]
            )
        }
    }
}