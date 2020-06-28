package com.jet2tt.articals.util

import android.text.format.DateUtils
import java.util.*
import kotlin.math.ln
import kotlin.math.pow


/**
 * Created by Anil 29/6/20
 */
class Util {
    var  AVERAGE_MONTH_IN_MILLIS:Long = DateUtils.DAY_IN_MILLIS * 30;

    fun getFormatedNumber(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        return String.format("%.1f %c", count / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
    }


     fun getRelationTime(time: Long): String? {
        val now: Long = Date().getTime()
        val delta = now - time
        val resolution: Long
        resolution = if (delta <= DateUtils.MINUTE_IN_MILLIS) {
            DateUtils.SECOND_IN_MILLIS
        } else if (delta <= DateUtils.HOUR_IN_MILLIS) {
            DateUtils.MINUTE_IN_MILLIS
        } else if (delta <= DateUtils.DAY_IN_MILLIS) {
            DateUtils.HOUR_IN_MILLIS
        } else if (delta <= DateUtils.WEEK_IN_MILLIS) {
            DateUtils.DAY_IN_MILLIS
        } else return if (delta <= AVERAGE_MONTH_IN_MILLIS) {
            Integer.toString((delta / DateUtils.WEEK_IN_MILLIS).toInt()) + " weeks(s) ago"
        } else if (delta <= DateUtils.YEAR_IN_MILLIS) {
            Integer.toString((delta / AVERAGE_MONTH_IN_MILLIS) as Int) + " month(s) ago"
        } else {
            Integer.toString((delta / DateUtils.YEAR_IN_MILLIS).toInt()) + " year(s) ago"
        }
        return DateUtils.getRelativeTimeSpanString(time, now, resolution).toString()
    }
}