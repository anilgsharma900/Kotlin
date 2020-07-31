package com.anil.articals.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow


/**
 * Created by Anil 29/6/20
 */
class Util {
    /**
     * @param count in Long
     * @return in formate number
     */
    fun getFormatedNumber(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        return String.format("%.1f %c", count / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
    }

    /**
     * @return String
     * @param dateString param formate "2020-04-17T12:13:44.575Z"
     */
    fun getRelationTime(dateString: String): String {
        var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"))
        var date: Date = sdf.parse(dateString)

        // convert date to millis
        var time: Long = date.time
        var SECOND_MILLIS: Int = 1000;
        var MINUTE_MILLIS: Int = 60 * SECOND_MILLIS
        var HOUR_MILLIS: Int = 60 * MINUTE_MILLIS
        var DAY_MILLIS: Int = 24 * HOUR_MILLIS

        if (time < 1000000000000L) {
            time *= 1000;
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return ""
        }
        val diff: Long = now - time

        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " m"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hr"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else {
            (diff / DAY_MILLIS).toString() + " d"
        }
    }

}