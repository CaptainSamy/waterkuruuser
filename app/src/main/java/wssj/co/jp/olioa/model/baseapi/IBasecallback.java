package wssj.co.jp.olioa.model.baseapi;

/**
 * Created by DaiKySy on 3/15/2019.
 */
public interface IBasecallback<D> {

    void onAction(int type, D... data);

}
