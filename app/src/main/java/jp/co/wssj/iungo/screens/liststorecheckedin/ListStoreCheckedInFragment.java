package jp.co.wssj.iungo.screens.liststorecheckedin;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.stamp.ListStoreCheckedResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.liststorecheckedin.adapter.ListStoreCheckedInAdapter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public class ListStoreCheckedInFragment extends BaseFragment<IListStoreCheckedInView, ListStoreCheckedInPresenter> implements IListStoreCheckedInView, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "ListStoreCheckedInFragment";

    private TextView mTitleListView;

    private ListView mListStoreCheckedIn;

    private ListStoreCheckedInAdapter mAdapter;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private double currentLatitude;

    private double currentLongitude;

    private int mServiceCompanyId;

    public static ListStoreCheckedInFragment newInstance(Bundle args) {
        ListStoreCheckedInFragment fragment = new ListStoreCheckedInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_list_store_checked_in);
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_LIST_STORE_CHECKED_IN;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_list_store_checked_in;
    }

    @Override
    protected ListStoreCheckedInPresenter onCreatePresenter(IListStoreCheckedInView view) {
        return new ListStoreCheckedInPresenter(view);
    }

    @Override
    protected IListStoreCheckedInView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mListStoreCheckedIn = (ListView) rootView.findViewById(R.id.lvListStoreCheckedIn);
        View view = LayoutInflater.from(getActivityContext()).inflate(R.layout.header_list_store_checked, null);
        mTitleListView = (TextView) view.findViewById(R.id.titleListStoreChecked);
        mListStoreCheckedIn.addHeaderView(view);
    }

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mServiceCompanyId = bundle.getInt(Constants.KEY_SERVICE_COMPANY_ID);
            getPresenter().checkAccessLocationPermission();
        }
    }

    public void initGoogleAPI() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivityContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
        mGoogleApiClient.connect();
    }

    @Override

    public void onAllowAccessLocation() {
        initGoogleAPI();
    }

    @Override
    public void onRequestLocationPermission() {
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        }, mServiceCompanyId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0 && requestCode == mServiceCompanyId) {
            boolean isGranted = true;
            for (int permissionGrantResult : grantResults) {
                isGranted = permissionGrantResult == PackageManager.PERMISSION_GRANTED;
                if (!isGranted) {
                    break;
                }
            }
            if (isGranted) {
                initGoogleAPI();
            } else {
                getPresenter().getListStoreCheckedIn(mServiceCompanyId, currentLatitude, currentLongitude);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logger.d(TAG, "#onConnected " + bundle);
        if (ContextCompat.checkSelfPermission(getActivityContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivityContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }
            getPresenter().getListStoreCheckedIn(mServiceCompanyId, currentLatitude, currentLongitude);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Logger.d(TAG, "#onConnectionSuspended " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logger.d(TAG, "#onConnectionFailed");
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        getPresenter().getListStoreCheckedIn(mServiceCompanyId, currentLatitude, currentLongitude);
    }

    @Override
    public void onLocationChanged(Location location) {
        Logger.d(TAG, "#onLocationChanged");
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    @Override
    public void onGetListStoreCheckedInSuccess(List<ListStoreCheckedResponse.StoreCheckedIn> listStores) {
        mAdapter = new ListStoreCheckedInAdapter(getActivityContext(), listStores);
        mListStoreCheckedIn.setAdapter(mAdapter);
    }

    @Override
    public void onGetListStoreCheckedInFailure(String message) {
        showToast(message);
    }
}
