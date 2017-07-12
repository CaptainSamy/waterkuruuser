package wssj.co.jp.point.utils;

import wssj.co.jp.point.BuildConfig;

/**
 * Created by HieuPT on 3/23/2017.
 */

public final class Constants {

    public static final String EMPTY_STRING = "";

    public static final String UTF8_ENCODING = "UTF-8";

    public static final String BASE_URL_AWS = BuildConfig.HOST;

    public static final String PREFIX_QR_STORE = "store";

    public static final int TIME_DELAYS = 1000;

    public static final int TIME_WAITING_SPLASH = 2000;

    public static final int TIME_DELAY_CLOSED_NAVIGATION_MENU = 100;

    public static final int INIT_PAGE = 1;

    public static final int LIMIT = 20;

    public static final String ACTION_SERVICE_ACTIVITY = "WATERKURU";

    public static final String SPLIT = "\\.";

    private Constants() {
    }

    public static final class CheckInStatus {

        public static final String STATUS_WAIT_CONFIRM = "wait_confirm";

        public static final String STATUS_CHECKED_IN = "checked_in";

        public static final String STATUS_CANCEL_OR_CHECKED_OUT = "来店中の顧客ではありません";

        private CheckInStatus() {
        }
    }

    public static final class CardTypes {

        public static final String CARD_TYPE_UNUSED = "unused";

        public static final String CARD_TYPE_CAN_USE = "can_use";

        public static final String CARD_TYPE_USED = "used";

        private CardTypes() {
        }
    }

    public static final class PushNotification {

        public static final String TYPE_NOTIFICATION = "action_push";

        public static final String TYPE_REMIND = "action_remind";

        public static final String TYPE_REQUEST_REVIEW = "action_request_review";
    }

    public static final class MemoConfig {

        public static final int NUMBER_LINE = 10;

        public static final String KEY_TRUE = "true";

        public static final String KEY_FALSE = "false";

        public static final int TYPE_SHORT_TEXT = 1;

        public static final int TYPE_LONG_TEXT = 2;

        public static final int TYPE_IMAGE = 3;

        public static final int TYPE_COMBO_BOX = 4;

        public static final int TYPE_SWITCH = 5;

        public static final int TYPE_LEVEL = 6;


    }
}
