package jp.co.wssj.iungo.screens.transition;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Nguyen Huu Ta on 2/10/2017.
 */

public class TransitionAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> mListUrlImage;

    public TransitionAdapter(FragmentManager fm, ArrayList<String> animalItems) {
        super(fm);
        this.mListUrlImage = animalItems;
    }

    @Override
    public Fragment getItem(int position) {
        String urlImage = mListUrlImage.get(position);

        String fileName = urlImage.substring(urlImage.lastIndexOf("/") + 1);
        return TransitionDetailFragment.newInstance(urlImage, fileName);
    }

    @Override
    public int getCount() {
        return mListUrlImage.size();
    }

}
