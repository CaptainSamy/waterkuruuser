package wssj.co.jp.obis.model.firebase;

import wssj.co.jp.obis.model.GsonSerializable;
import wssj.co.jp.obis.model.ResponseData;

/**
 * Created by tuanle on 6/1/17.
 */

public class UploadTokenReponse extends ResponseData<UploadTokenReponse.DataUploadToken> {
    class DataUploadToken implements GsonSerializable {
    }
}
