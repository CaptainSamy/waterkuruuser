package jp.co.wssj.iungo.screens.transition;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.co.wssj.iungo.R;

/**
 * Created by Nguyen Huu Ta on 2/10/2017.
 */

public class TransitionDetailFragment extends Fragment {

    private static final String EXTRA_ANIMAL_ITEM = "animal_item";

    private static final String EXTRA_TRANSITION_NAME = "transition_name";

    public static TransitionDetailFragment newInstance(String animalItem, String transitionName) {
        TransitionDetailFragment animalDetailFragment = new TransitionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ANIMAL_ITEM, animalItem);
        bundle.putString(EXTRA_TRANSITION_NAME, transitionName);
        animalDetailFragment.setArguments(bundle);
        return animalDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animal_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String animalItem = getArguments().getString(EXTRA_ANIMAL_ITEM);
        String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);
        ImageView imageView = (ImageView) view.findViewById(R.id.animal_detail_image_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(transitionName);
        }
        Glide.with(getContext()).load(animalItem).into(imageView);

//        Picasso.with(getContext())
//                .load(animalItem)
//                .noFade()
//                .into(imageView, new Callback() {
//
//                    @Override
//                    public void onSuccess() {
////                        startPostponedEnterTransition();
//                    }
//
//                    @Override
//                    public void onError() {
////                        startPostponedEnterTransition();
//                    }
//                });
    }
}