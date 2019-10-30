package edu.uwstout.p2pchat.room;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Instructs Room how to store Date values.
 */
public class Converters
{
    /**
     * Converts unix timestamp stored as Long into Date.
     * @param value unix timestamp to be converted
     * @return Date representation of value
     */
    @TypeConverter
    public final Date fromTimestamp(final Long value)
    {
        return value == null ? null : new Date(value);
    }

    /**
     * Converts Date to unix timestamp
     * @param date Date to be converted.
     * @return unix timestamp of date
     */
    @TypeConverter
    public final Long dateToTimestamp(final Date date)
    {
        if (date == null)
        {
            return null;
        }
        else
        {
            return date.getTime();
        }
    }
}
