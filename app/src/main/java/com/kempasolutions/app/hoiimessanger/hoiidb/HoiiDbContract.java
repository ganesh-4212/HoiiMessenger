package com.kempasolutions.app.hoiimessanger.hoiidb;

import android.provider.BaseColumns;

/**
 * Created by Archana on 8/8/2016.
 */

public final class HoiiDbContract {
    public HoiiDbContract() {
    }

    public static abstract class Roaster implements BaseColumns {
        public static final String TABLE_NAME = "roster";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_LAST_SEEN = "last_seen";
        public static final String COLUMN_NAME_PROFILE_PIC = "profile_pic";
    }

    public static abstract class Message implements BaseColumns {
        public static final String TABLE_NAME = "messages";
        public static final String COLUMN_NAME_SENT_RECIEVE = "sent_recieve";
        public static final String COLUMN_NAME_FROM_TO = "from_to";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_BODY = "body";
        public static final String COLUMN_NAME_DATE_TIME = "date_time";
    }
}
