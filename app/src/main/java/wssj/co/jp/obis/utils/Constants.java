package wssj.co.jp.obis.utils;

import wssj.co.jp.obis.BuildConfig;

/**
 * Created by HieuPT on 3/23/2017.
 */

public final class Constants {

    public static final String EMPTY_STRING = "";

    public static final String UTF8_ENCODING = "UTF-8";

    public static final String BASE_URL = BuildConfig.HOST;

    public static final String PREFIX_QR_STORE = "store";

    public static final int TIME_DELAYS = 1000;

    public static final int TIME_WAITING_SPLASH = 1000;

    public static final int TIME_DELAY_CLOSED_NAVIGATION_MENU = 100;

    public static final int INIT_PAGE = 0;

    public static final int LIMIT = 20;

    public static final int LIMIT_TIMELINE = 10;


    public static final int DURATION_DIALOG_NOTIFICATION = 350;


    public static final int MARGIN_LEFT = 110;

    public static final int MARGIN_BOTTOM = 130;

    public static final int MARGIN_RIGHT = 8;

    public static final String ACTION_REFRESH_LIST_PUSH = "refresh_list_push";

    public static final String SPLIT = "\\.";

    public static final int TIME_OUT_DEFAULT = 5000;

    public static final int TIME_OUT_CUSTOM = 60000;

    public static final String URL_CONTACT = BASE_URL + "/api/client/users/save-user-contact";

    public static final String KEY_SERVICE_COMPANY_ID = "KEY_SERVICE_COMPANY_ID";

    public static final int TIME_DELAY_GET_LIST_CHAT = 5000;

    public static final String KEY_VIDEO = "video_iungo";

    public static final String KEY_SPLIT_FRAME_VIDEO = "|";

    public static final int APP_ID = 2;

    public static final String[] mArrayAge = {"１０代", "２０代", "３０代", "４０代", "５０代", "６０代"};

    public static final String KEY_GEN_AUTO_LOGIN = "1abcc71e-9864-4b6e-af84-144ec74aea6d";//"eb62190e-c4d2-476f-b108-992bf30367cf";//

    public static final int SUCCESSFULLY = 1;

    public static final int FAILURE = 2;


    public static final int MAX_ITEM_PAGE = 30;

    public static final int MAX_ITEM_PAGE_CHAT = 50;

    public static final String APP_CERTIFICATE = "2iu34huksdfnasjkay2ualsdmkk";

    public static final String TEST = "http://133.130.73.227:8745/olioa/";
    private Constants() {
    }


    public static final class Register {

        public static final int MIN_AGE = 10;

        public static final int MALE = 1;

        public static final int FE_MALE = 0;

        public static final String ACCOUNT = "obis_user";

        public static final String PASSWORD = "obis_password";

        public static final String NAME = "OBIS User";

        public static final String EMAIL = "obis@gmail.com";
    }

//    public static final class CheckInStatus {
//
//        public static final String STATUS_WAIT_CONFIRM = "wait_confirm";
//
//        public static final String STATUS_CHECKED_IN = "checked_in";
//
//        public static final String STATUS_CHECKED_OUT = "check_out";
//
//        public static final String STATUS_CANCEL = "cancel";
//
//        private CheckInStatus() {
//        }
//    }

//    public static final class CardTypes {
//
//        public static final String CARD_TYPE_UNUSED = "unused";
//
//        public static final String CARD_TYPE_CAN_USE = "can_use";
//
//        public static final String CARD_TYPE_USED = "used";
//
//        private CardTypes() {
//        }
//    }

    public static final class PushNotification {

        public static final String TYPE_QUESTION_NAIRE = "action_question_naire";

    }

    public static final class TypePush {

        public static final int TYPE_ALL_PUSH = 0;

        public static final int TYPE_LIKED_PUSH = 1;

        public static final int TYPE_QUESTION_NAIRE_PUSH = 2;

        public static final int TYPE_PUSH_ANNOUNCE = 3;
    }

//    public static final class MemoConfig {
//
//        public static final int NUMBER_LINE = 10;
//
//        public static final int TIME_DELAY_SHOW_VIEW = 200;
//
//        public static final int TYPE_SHORT_TEXT = 1;
//
//        public static final int TYPE_LONG_TEXT = 2;
//
//        public static final int TYPE_IMAGE = 3;
//
//        public static final int TYPE_COMBO_BOX = 4;
//
//        public static final int TYPE_SWITCH = 5;
//
//        public static final int TYPE_LEVEL = 6;
//    }

//    public static final class Like {
//
//        public static final String LIKE = "いいね！";
//
//        public static final String LOVE = "超いいね！";
//
//        public static final String HAHA = "うけるね";
//
//        public static final String WOW = "すごいね";
//
//        public static final String CRY = "悲しいね";
//
//        public static final String ANGRY = "ひどいね";
//
//    }
}
