package wssj.co.jp.obis.model.baseapi;

/**
 * Created by DaiKySy on 3/15/2019.
 */
public interface IOlioaCallback<D> {

    void onAction(int type, D... data);

}
