package jp.co.wssj.iungo.model.database;

import android.provider.BaseColumns;

/**
 * Created by Nguyen Huu Ta on 16/10/2017.
 */

public final class DatabaseContract {

    public static class PushNotification implements BaseColumns {

        public static final String TABLE_NAME = "push";

        public static final String COLUMN_PUSH_ID = "push_id";

        public static final String COLUMN_PUSH_TIME = "push_time";

        public static final String COLUMN_TITLE_PUSH = "title_push";

        public static final String COLUMN_CONTENT_PUSH = "content_push";

        public static final String COLUMN_ACTION_PUSH = "action_push";

        public static final String COLUMN_IMAGE_STORE = "image_store";

        public static final String COLUMN_STATUS_READ = "status_read";

        public static final String COLUMN_LIKE = "like";

    }
}
