package wssj.co.jp.olioa.screens.base;

import wssj.co.jp.olioa.model.BaseModel;

/**
 * Created by HieuPT on 3/26/2017.
 */

public abstract class FragmentPresenter<V extends IFragmentView> extends BasePresenter<V> {

    protected FragmentPresenter(V view) {
        super(view);
    }

    protected void onFragmentAttach() {
    }

    protected void onFragmentCreate() {
    }

    protected void onFragmentCreateView() {
        onViewAttached();
    }

    protected void onFragmentViewCreated() {
    }

    protected void onActivityCreated() {
    }

    protected void onFragmentStart() {
        for (BaseModel model : getModels()) {
            model.resumeWorkerTask();
        }
    }

    protected void onFragmentResume() {
    }

    protected void onFragmentPause() {
    }

    protected void onFragmentStop() {
        for (BaseModel model : getModels()) {
            model.stopWorkerTask();
        }
    }

    protected void onFragmentDestroyView() {
        getView().hideProgress();
        onViewDetached();
    }

    protected void onFragmentDestroy() {
        for (BaseModel model : getModels()) {
            model.release();
        }
    }

    protected void onFragmentDetach() {
    }
}
