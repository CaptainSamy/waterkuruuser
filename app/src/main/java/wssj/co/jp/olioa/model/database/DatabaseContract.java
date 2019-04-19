package wssj.co.jp.olioa.model.database;

import android.provider.BaseColumns;

/**
 * Created by Nguyen Huu Ta on 16/10/2017.
 */

final class DatabaseContract {

    static class PushNotification implements BaseColumns {

        static final String TABLE_NAME = "push";

        static final String COLUMN_PUSH_ID = "push_id";

        static final String COLUMN_PUSH_TIME = "push_time";

        static final String COLUMN_TITLE_PUSH = "title_push";

        static final String COLUMN_CONTENT_PUSH = "content_push";

        static final String COLUMN_ACTION_PUSH = "action_push";

        static final String COLUMN_IMAGE_STORE = "image_store";

        static final String COLUMN_STATUS_READ = "status_read";

        static final String COLUMN_STORE_ANNOUNCE = "store_announce";

        static final String COLUMN_LIKE = "like";

    }

    static class ChatColumns implements BaseColumns {

        static final String TABLE_NAME = "chat";

        static final String COLUMN_CHAT_ID = "chatId";

        static final String COLUMN_STORE_ID = "storeId";

        static final String COLUMN_CONTENT = "content";

        static final String COLUMN_CONTENT_TYPE = "contentType";

        static final String COLUMN_IS_USER = "isUser";

        static final String COLUMN_CREATED = "created";
    }

    static class StoreColumn implements BaseColumns {

        static final String TABLE_NAME = "Store";

        static final String COLUMN_STORE_ID = "StoreId";

        static final String STORE_NAME = "StoreName";

        static final String COLUMN_LAST_MESSAGE = "LastMessage";

        static final String COLUMN_LAST_TIME_MESSAGE = "LastTimeMessage";

        static final String COLUMN_LOGO_STORE = "LogoStore";

    }

}
