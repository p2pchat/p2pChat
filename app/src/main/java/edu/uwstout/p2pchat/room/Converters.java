package edu.uwstout.p2pchat.room;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters
{
    @TypeConverter
    public final Date fromTimestamp(Long value)
    {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public final Long dateToTimestamp(Date date)
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
