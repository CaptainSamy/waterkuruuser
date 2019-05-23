package wssj.co.jp.obis.screens.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wssj.co.jp.obis.model.BaseModel;

/**
 * Created by HieuPT on 4/13/2017.
 */

public abstract class BasePresenter<V extends IView> {

    private boolean mIsViewAttached;

    private final V mView;

    private final Map<Class<? extends BaseModel>, ModelWrapper<?>> mModelMap = new HashMap<>();

    protected BasePresenter(V view) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null");
        }
        mView = view;
    }

    protected final boolean isViewAttached() {
        return mIsViewAttached;
    }

    public final void onViewAttached() {
        mIsViewAttached = true;
    }

    public final void onViewDetached() {
        mIsViewAttached = false;
    }

    @SuppressWarnings("unchecked")
    protected final <M extends BaseModel> M getModel(Class<M> clazz) {
        ModelWrapper<M> modelWrapper = (ModelWrapper<M>) mModelMap.get(clazz);
        if (modelWrapper != null) {
            return modelWrapper.mModel;
        } else {
            throw new IllegalArgumentException("This model type has not been registered");
        }
    }

    @SuppressWarnings("unchecked")
    protected final List<? extends BaseModel> getModels() {
        List<ModelWrapper<?>> modelWrappers = new ArrayList<>(mModelMap.values());
        List modelList = new ArrayList<>();
        for (ModelWrapper<?> modelWrapper : modelWrappers) {
            modelList.add(modelWrapper.mModel);
        }
        return modelList;
    }

    protected final <M extends BaseModel> void unregisterModel(Class<M> clazz) {
        if (clazz != null) {
            mModelMap.remove(clazz);
        }
    }

    protected final void unregisterAll() {
        mModelMap.clear();
    }

    protected final <M extends BaseModel> void registerModel(M model) {
        if (model == null) {
            throw new IllegalArgumentException("Model must not be null");
        }
        Class clazz = model.getClass();
        if (!mModelMap.containsKey(clazz)) {
            mModelMap.put(clazz, new ModelWrapper<>(model));
        }
    }

    protected V getView() {
        return mView;
    }

    private static class ModelWrapper<M extends BaseModel> {

        private final M mModel;

        private ModelWrapper(M model) {
            mModel = model;
        }
    }
}