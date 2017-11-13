package jp.co.wssj.iungo.utils;

import jp.co.wssj.iungo.BuildConfig;

/**
 * Created by HieuPT on 3/23/2017.
 */

public final class Constants {

    public static final String EMPTY_STRING = "";

    public static final String UTF8_ENCODING = "UTF-8";

    public static final String BASE_URL = BuildConfig.HOST;

    public static final String PREFIX_QR_STORE = "store";

    public static final int TIME_DELAYS = 1000;

    public static final int TIME_WAITING_SPLASH = 2000;

    public static final int TIME_DELAY_CLOSED_NAVIGATION_MENU = 100;

    public static final int INIT_PAGE = 1;

    public static final int LIMIT = 20;

    public static final int LIMIT_TIMELINE = 10;

    public static final int LIMIT_CHAT = 5;

    public static final int DURATION_DIALOG_NOTIFICATION = 350;

    public static final int DELAY_TIME_TRANSFER_FRAGMENT = 200;

    public static final int MARGIN_LEFT = 110;

    public static final int MARGIN_BOTTOM = 130;

    public static final int MARGIN_RIGHT = 8;

    public static final String ACTION_REFRESH_LIST_PUSH = "refresh_list_push";

    public static final String SPLIT = "\\.";

    public static final int TIME_OUT_DEFAULT = 5000;

    public static final int TIME_OUT_CUSTOM = 60000;

    public static final String URL_CONTACT = BASE_URL + "/api/client/users/save-user-contact";

    public static final String HASH_MD5 = "MD5";

    public static final String SALT = "b50b1ffe2e7320b0d97062a9663d47a7adf1379392c58c66fd978171a2be7d65";

    public static final String KEY_SERVICE_COMPANY_ID = "KEY_SERVICE_COMPANY_ID";

    public static final String KEY_FAST = "fast_";

    public static final int STATUS_READ = 1;

    public static final int STATUS_VIEW = 2;

    public static final int TIME_DELAY_GET_LIST_CHAT = 5000;

    public static final String KEY_VIDEO = "video_iungo";

    public static final String KEY_FRAME_VIDEO = "frame_video";

    public static final String KEY_SPLIT_FRAME_VIDEO = "|";

    public static final String[] mArrayAge = {"１０代", "２０代", "３０代", "４０代", "５０代", "６０代"};

    public static final String KEY_GEN_AUTO_LOGIN ="1abcc71e-9864-4b6e-af84-144ec74aea6d";//"eb62190e-c4d2-476f-b108-992bf30367cf";//

    private Constants() {
    }

    public static final class Introduction {

        public static final String TWITTER_KEY = "";

        public static final String TWITTER_SECRET = "";

        public static final int TYPE_DEFAULT = 0;

        public static final int TYPE_GOOGLE = 1;

        public static final int TYPE_FACEBOOK = 2;

        public static final int TYPE_TWITTER = 3;
    }

    public static final class Register {

        public static final int MIN_AGE = 10;

        public static final int MALE = 1;

        public static final int FE_MALE = 0;
    }

    public static final class CheckInStatus {

        public static final String STATUS_WAIT_CONFIRM = "wait_confirm";

        public static final String STATUS_CHECKED_IN = "checked_in";

        public static final String STATUS_CHECKED_OUT = "check_out";

        public static final String STATUS_CANCEL = "cancel";

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

        public static final String TYPE_QUESTION_NAIRE = "action_question_naire";

        public static final String TYPE_TIME_LINE = "type_timeline";

        public static final String TYPE_STEP_PUSH = "type_step_push";
    }

    public static final class TypePush {

        public static final int TYPE_ALL_PUSH = 0;

        public static final int TYPE_LIKED_PUSH = 1;

        public static final int TYPE_QUESTION_NAIRE_PUSH = 2;

        public static final int TYPE_PUSH_ANNOUNCE = 3;
    }

    public static final class MemoConfig {

        public static final int NUMBER_LINE = 10;

        public static final int TIME_DELAY_SHOW_VIEW = 200;

        public static final int TYPE_SHORT_TEXT = 1;

        public static final int TYPE_LONG_TEXT = 2;

        public static final int TYPE_IMAGE = 3;

        public static final int TYPE_COMBO_BOX = 4;

        public static final int TYPE_SWITCH = 5;

        public static final int TYPE_LEVEL = 6;
    }

    public static final class Like {

        public static final String LIKE = "いいね！";

        public static final String LOVE = "超いいね！";

        public static final String HAHA = "うけるね";

        public static final String WOW = "すごいね";

        public static final String CRY = "悲しいね";

        public static final String ANGRY = "ひどいね";

    }
}
