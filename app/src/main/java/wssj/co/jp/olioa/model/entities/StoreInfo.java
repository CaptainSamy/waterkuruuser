package wssj.co.jp.olioa.model.entities;

/**
 * Created by DaiKySy on 3/25/19.
 */

public class StoreInfo {

    private int id;

    private int serviceCompanyId;

    private String name;

    private String logo;

    private String checkInCode;

    public int getId() {
        return id;
    }

    public int getServiceCompanyId() {
        return serviceCompanyId;
    }

    public void setServiceCompanyId(int serviceCompanyId) {
        this.serviceCompanyId = serviceCompanyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCheckInCode() {
        return checkInCode;
    }

    public void setCheckInCode(String checkInCode) {
        this.checkInCode = checkInCode;
    }
}
