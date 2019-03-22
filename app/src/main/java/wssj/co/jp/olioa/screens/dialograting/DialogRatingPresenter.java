package wssj.co.jp.olioa.screens.dialograting;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.stamp.StampModel;
import wssj.co.jp.olioa.screens.base.BasePresenter;

/**
 * Created by Nguyen Huu Ta on 23/6/2017.
 */

public class DialogRatingPresenter extends BasePresenter<IDialogRatingView> {

    protected DialogRatingPresenter(IDialogRatingView view) {
        super(view);
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void reviewServiceByStamp(int stampId, float rating, String note) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(StampModel.class).reviewServiceByStamp(token, stampId, rating, note, new StampModel.IOnReviewServiceResponse() {

            @Override
            public void onSuccess(String message) {
                getView().reviewServiceSuccess(message);
            }

            @Override
            public void onFailure(ErrorMessage errorMessage) {
                getView().reviewServiceFailure(errorMessage.getMessage());
            }
        });
    }
}
