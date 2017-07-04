package wssj.co.jp.point.screens.liststorecheckedin;

import java.util.List;

import wssj.co.jp.point.model.stamp.ListStoreCheckedResponse;
import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public interface IListStoreCheckedInView extends IFragmentView {

    void onGetListStoreCheckedInSuccess(List<ListStoreCheckedResponse.StoreCheckedIn> listStores);

    void onGetListStoreCheckedInFailure(String message);

    void onAllowAccessLocation();

    void onRequestLocationPermission();
}
