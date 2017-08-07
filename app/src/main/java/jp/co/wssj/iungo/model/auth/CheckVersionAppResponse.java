package jp.co.wssj.iungo.model.auth;

import com.google.gson.annotations.SerializedName;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 7/8/2017.
 */

public class CheckVersionAppResponse extends ResponseData<CheckVersionAppResponse.CheckVersionAppData> {

    public class CheckVersionAppData implements GsonSerializable {

        @SerializedName("has_update")
        private boolean mHasUpdate;

        @SerializedName("server_information")
        private ServerInformation mServerInfo;

        @SerializedName("version_info")
        private VersionInfo mVersionInfo;

        public class ServerInformation {

            @SerializedName("is_maintain")
            private boolean isMaintain;

            @SerializedName("message")
            private String mMessage;

            @SerializedName("status")
            private String mStatus;

            public boolean isMaintain() {
                return isMaintain;
            }

            public String getMessage() {
                return mMessage;
            }

            public String getStatus() {
                return mStatus;
            }
        }

        public class VersionInfo {

            @SerializedName("change_log")
            private String mChangeLog;

            @SerializedName("is_required")
            private boolean isRequired;

            @SerializedName("is_send_notify")
            private boolean isSendNotify;

            @SerializedName("version_code")
            private int mVersionCode;

            @SerializedName("version_name")
            private String mVersionName;

            @SerializedName("warning")
            private String mWarning;

            public String getChangeLog() {
                return mChangeLog;
            }

            public boolean isRequired() {
                return isRequired;
            }

            public boolean isSendNotify() {
                return isSendNotify;
            }

            public int getVersionCode() {
                return mVersionCode;
            }

            public String getVersionName() {
                return mVersionName;
            }

            public String getWarning() {
                return mWarning;
            }
        }

        public boolean isHasUpdate() {
            return mHasUpdate;
        }

        public ServerInformation getServerInfo() {
            return mServerInfo;
        }

        public VersionInfo getVersionInfo() {
            return mVersionInfo;
        }
    }
}
