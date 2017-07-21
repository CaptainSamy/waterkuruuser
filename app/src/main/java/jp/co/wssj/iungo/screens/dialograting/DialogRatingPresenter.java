package jp.co.wssj.iungo.screens.dialograting;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.stamp.StampModel;
import jp.co.wssj.iungo.screens.base.BasePresenter;

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
