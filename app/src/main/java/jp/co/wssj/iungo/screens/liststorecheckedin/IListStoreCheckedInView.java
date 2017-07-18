package jp.co.wssj.iungo.screens.liststorecheckedin;

import java.util.List;

import jp.co.wssj.iungo.model.stamp.ListStoreCheckedResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public interface IListStoreCheckedInView extends IFragmentView {

    void onGetListStoreCheckedInSuccess(List<ListStoreCheckedResponse.StoreCheckedIn> listStores);

    void onGetListStoreCheckedInFailure(String message);

    void onAllowAccessLocation();

    void onRequestLocationPermission();
}
