package jp.co.wssj.iungo.screens.transition;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;

/**
 * Created by Nguyen Huu Ta on 2/10/2017.
 */

public class TransitionFragment extends Fragment {

    private static final String EXTRA_INITIAL_ITEM_POS = "initial_item_pos";

    private static final String EXTRA_ANIMAL_ITEMS = "animal_items";

    public TransitionFragment() {
        // Required empty public constructor
    }

    public static TransitionFragment newInstance(int currentItem, List<String> animalItems) {
        TransitionFragment transitionFragment = new TransitionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_INITIAL_ITEM_POS, currentItem);
        bundle.putStringArrayList(EXTRA_ANIMAL_ITEMS, (ArrayList<String>) animalItems);
        transitionFragment.setArguments(bundle);
        return transitionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
        setSharedElementReturnTransition(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animal_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int currentItem = getArguments().getInt(EXTRA_INITIAL_ITEM_POS);
        ArrayList<String> animalItems = getArguments().getStringArrayList(EXTRA_ANIMAL_ITEMS);

        TransitionAdapter animalPagerAdapter = new TransitionAdapter(getChildFragmentManager(), animalItems);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.animal_view_pager);
        viewPager.setAdapter(animalPagerAdapter);
        viewPager.setCurrentItem(currentItem);
    }
}