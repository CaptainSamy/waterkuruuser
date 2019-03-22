package wssj.co.jp.olioa.screens.calender;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;
import jp.co.recruit_mp.android.lightcalendarview.LightCalendarView;
import jp.co.recruit_mp.android.lightcalendarview.MonthView;
import jp.co.recruit_mp.android.lightcalendarview.accent.Accent;
import jp.co.recruit_mp.android.lightcalendarview.accent.DotAccent;

/**
 * Created by Thanghn on 12/19/2017.
 */

public class CalenderFragment extends BaseFragment<ICalenderView, CalenderPresenter> implements ICalenderView {

    private static String TAG = "CalenderFragment";
    private Map<Integer, Integer> mapDay;
    private ListView listview;
    private LightCalendarView calendarView;
    private TextView txtMonthCalender;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    List<ListDateSort> listDateSorts;
    private List<Integer> listDateHasPush;
    private AdapterCalender mAdaptePush;
    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CALENDER;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_calendar;
    }

    @Override
    protected CalenderPresenter onCreatePresenter(ICalenderView view) {
        return new CalenderPresenter(view);
    }

    @Override
    protected ICalenderView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        listview = (ListView) rootView.findViewById(R.id.listViewItem);
        calendarView = (LightCalendarView) rootView.findViewById(R.id.calendarView);
        txtMonthCalender=(TextView)rootView.findViewById(R.id.txtMonthCalender);
        txtMonthCalender.setText(formatter.format(calendarView.getMonthCurrent()));
        super.initViews(rootView);
    }

    @Override
    protected void initData() {
        super.initData();
        mapDay = new HashMap<>();
        getPresenter().getListPushNotificationForCalendar(0, 0, Constants.EMPTY_STRING, "2017-12-01 00:00:00", "2017-12-31 00:00:00");
    }

    @Override
    protected void initAction() {
        super.initAction();
        final List<NotificationMessage> list = new ArrayList<NotificationMessage>();
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        Calendar calNow = Calendar.getInstance();
        calFrom.set(Calendar.MONTH, 0);
        calTo.set(Calendar.MONTH, 11);
        calendarView.setMonthFrom(calFrom.getTime());
        calendarView.setMonthTo(calTo.getTime());
        calendarView.setMonthCurrent(calNow.getTime());
        calendarView.setOnStateUpdatedListener(new LightCalendarView.OnStateUpdatedListener() {
            @Override
            public void onMonthSelected(@NotNull final Date date, @NotNull final MonthView monthView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtMonthCalender.setText(formatter.format(calendarView.getMonthCurrent()));
                        Logger.d(TAG, "onMonthSelected " + date.toGMTString() + " to timestamp " + date.getTime());
                        Calendar cal = Calendar.getInstance();
                        List<Date> dates = new ArrayList<Date>();
                        for (int i = 0; i < 31; i++) {
                            if (listDateSorts != null && listDateSorts.size() > 0) {
                                for (ListDateSort dateSort : listDateSorts) {
                                    if (i == dateSort.getStringDate()) {
                                        cal.set(monthView.getMonth().getYear() + 1900, monthView.getMonth().getMonth(), i);
                                        dates.add(cal.getTime());
                                    }
                                }
                            }
                        }
                        //chấm số điểm
                        HashMap<Date, List<Accent>> map = new HashMap<>();
                        for (Date date2 : dates) {
                            List<Accent> accents = new ArrayList<>();
                            if (mapDay.get(date2.getDate()) == null) {
                                continue;
                            }
                            Integer numLoop = mapDay.get(date2.getDate());
                            if (numLoop != null && numLoop > 3) {
                                numLoop = 3;
                            }
                            if (numLoop == null) {
                                numLoop = 0;
                            }
                            for (int i = 0; i < numLoop; i++) {
                                accents.add(new DotAccent(6f, null, formatter.format(date2) + "-" + 0));
                            }
                            map.put(date2, accents);
                        }
                        monthView.setAccents(map);
                    }
                }, 1200);
            }

            @Override
            public void onDateSelected(@NotNull Date date) {
//                Logger.d(TAG, "onDateSelected " + date.toGMTString() + "--" + date.getDate() + " to timestamp " + date.getTime());
//                if (listDateHasPush != null && listDateHasPush.size() > 0) {
//                    for (int i = 0; i < listDateHasPush.size(); i++) {
//                        if (date.getDate() == listDateHasPush.get(i)) {
//                            Logger.d("Bello", "display list");
//                        }
//                    }
//                }
                if (mAdaptePush == null) {
                    mAdaptePush = new AdapterCalender(getActivityContext(), R.layout.item_push_notification, new ArrayList<NotificationMessage>());
                }
                if (listDateSorts != null && listDateSorts.size() > 0) {
                    Logger.d("SizelistDataSorts", listDateSorts.size() + "");

                    List<NotificationMessage> listNotification =null;
                    for (ListDateSort listDateSort : listDateSorts) {
                            if (date.getDate() == listDateSort.getStringDate()) {
                                listNotification = listDateSort.getmListNotification();
                            }
                    }
                    mAdaptePush.updateList(listNotification);
                    listview.setAdapter(mAdaptePush);
                }
            }
        });
//click item lisview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bundle bundle=new Bundle();
//                bundle.putString("titleCalender",notificationMessage.getTitle());
//                bundle.putString("timeCalender", String.valueOf(notificationMessage.getPushTime()));
//                bundle.putString("bodyCalender", notificationMessage.getMessage());
//                getActivityCallback().displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
                NotificationMessage message = (NotificationMessage) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PushNotificationDetailFragment.NOTIFICATION_ARG, message);
                getActivityCallback().displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
            }
        });
    }
    @Override
    public void showListPushNotification(List<NotificationMessage> list, int page, int totalPage) {
        //tra ra cac ngay pussh va so luong push
        if (list != null && list.size() > 0) {
            mapDay.clear();
            for (NotificationMessage notificationMessage : list) {
                Integer objectMap = mapDay.get(notificationMessage.getDay());
                if (objectMap == null) {
                    mapDay.put(notificationMessage.getDay(), 0);
                    objectMap = mapDay.get(notificationMessage.getDay());
                }
                objectMap++;
                mapDay.put(notificationMessage.getDay(), objectMap);
            }
            listDateHasPush = new ArrayList();
            for (Map.Entry<Integer, Integer> entry : mapDay.entrySet()) {
                Logger.d("Bello", "Day: " + entry.getKey() + " - " + entry.getValue());
                listDateHasPush.add(entry.getKey());
            }
            listDateSorts = new ArrayList<>();
            int dateConvertOld = 0;
            ListDateSort dateSort = null;
            for (NotificationMessage notificationMessage : list) {
                int dateConvertNew = notificationMessage.getDay();
                if (dateConvertOld == 0) {
                    dateSort = new ListDateSort();
                    if (!TextUtils.isEmpty(String.valueOf(dateConvertNew))) {
                        dateSort.setStringDate(dateConvertNew);
                    }
                    dateSort.getmListNotification().add(notificationMessage);
                    listDateSorts.add(dateSort);
                    dateConvertOld = dateConvertNew;
                } else {
                    if (dateConvertNew != dateConvertOld) {
                        dateSort = new ListDateSort();
                        dateConvertOld = dateConvertNew;
                        dateSort.setStringDate(dateConvertNew);
                        listDateSorts.add(dateSort);
                    } else {
                        dateSort.getmListNotification().add(notificationMessage);
                    }
                }
            }
            Log.d("size", list.size() + " " + page + " " + totalPage);
        }

        Log.d("size", list.size() + " " + page + " " + totalPage);
    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {

    }

}
