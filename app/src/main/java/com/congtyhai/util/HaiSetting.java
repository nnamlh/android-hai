package com.congtyhai.util;

import com.congtyhai.model.receive.AgencyInfo;
import com.congtyhai.model.receive.EventProduct;
import com.congtyhai.model.receive.GeneralInfo;
import com.congtyhai.model.receive.ResultEventInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NAM on 10/12/2016.
 */

public class HaiSetting {

    private static HaiSetting instance = null;
    private List<GeneralInfo> eventCodeResult;
    public String USER;
    public String TOKEN;
    public String ROLE;
    private String AGENCY;
    private String AGENCYNAME;

    private List<EventProduct> PRODUCTEVENT;

    private List<ResultEventInfo> resultEventInfos;

    private List<String> EVENTCODES = new ArrayList<>();

    public static String ROLE_WAREHOUSE = "Warehouse";
    public static String PRODUCT_IMPORT = "NK";
    public static String PRODUCT_EXPORT = "XK";
    public static String PRODUCT_HELP_SCAN = "HELPSCAN";

    protected HaiSetting() {
        LIST_PRODUCT = new ArrayList<>();
    }

    public static HaiSetting getInstance() {
        if (instance == null) {
            instance = new HaiSetting();
        }

        return instance;
    }


    public List<String> LIST_PRODUCT;

    public List<AgencyInfo> LIST_AGENCY;

    public void resetListProduct() {
        LIST_PRODUCT = new ArrayList<>();
    }

    public void addListAgency(AgencyInfo info) {
        if (LIST_AGENCY == null) {
            LIST_AGENCY = new ArrayList<>();
        }

        LIST_AGENCY.add(info);
    }

    public List<String> getEVENTCODES() {
        return this.EVENTCODES;
    }

    public boolean addToEventCode(String id) {

        if (EVENTCODES.contains(id))
            return false;

        EVENTCODES.add(id);

        return true;
    }

    public void resetEventCode() {
        EVENTCODES.clear();
    }

    public List<AgencyInfo> getLIST_AGENCY() {
        if (LIST_AGENCY == null) {
            LIST_AGENCY = new ArrayList<>();
        }

        return LIST_AGENCY;
    }

    public void addListProduct(String item) {
        if (LIST_PRODUCT == null) {
            LIST_PRODUCT = new ArrayList<>();
        }

        LIST_PRODUCT.add(0, item);
    }

    public int countListProduct() {
        return getLIST_PRODUCT().size();
    }

    public List<String> getLIST_PRODUCT() {
        if (LIST_PRODUCT == null) {
            LIST_PRODUCT = new ArrayList<>();
        }

        return LIST_PRODUCT;
    }

    public String[] toProductArrays() {
        String[] arrays = new String[HaiSetting.getInstance().countListProduct()];

        int i = 0;
        for (String item : LIST_PRODUCT) {
            arrays[i] = item;
            i++;
        }

        return arrays;
    }

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


    public static final String ACTION_CHECK_IN = "CHECKIN";
    public static final String ACTION_CHECK_STAFF = "CHECKSTAFF";
    public static final String ACTION_PRODUCT = "PRODUCT";
    public static final String ACTION_NOTIFICATION = "NOTIFICATION";
    public static final String ACTION_SETTING = "SUPPORT";
    public static final String ACTION_EVENT = "EVENTS";

    public static final String KEY_EVENT_COUNT = "COUNTEVENT";
    //public static final String BASEURL = "http://api.nongduochai.vn/api/";
    //public static final String BASEURL_UPLOAD = "http://cskh.nongduochai.vn/";
    public static final String BASEURL = "http://221.133.7.92:802/api/";
    public static final String BASEURL_UPLOAD = "http://221.133.7.92:801/";


    public static final String KEY_USER = "userlogin";
    public static final String KEY_TOKEN = "tokenlogin";
    public static final String KEY_FUNCTION = "functionlogin";
    public static final String KEY_ROLE = "rolelogin";
    public static final String KEY_LIST_AGENCY = "listagency";
    public static final String KEY_LIST_RECEIVE = "listreceive";

    public List<ResultEventInfo> getResultEventInfos() {
        if (resultEventInfos == null) {
            resultEventInfos = new ArrayList<>();
        }
        return resultEventInfos;
    }

    public void addListEvent(ResultEventInfo info) {

        if (resultEventInfos == null) {
            resultEventInfos = new ArrayList<>();
        }

        resultEventInfos.add(info);

    }

    public void clearListEvent() {
        if (resultEventInfos != null) {
            resultEventInfos = new ArrayList<>();
        }
    }

    public String getAGENCY() {
        return AGENCY;
    }

    public void setAGENCY(String AGENCY) {
        this.AGENCY = AGENCY;
    }

    public String getAGENCYNAME() {
        return AGENCYNAME;
    }

    public void setAGENCYNAME(String AGENCYNAME) {
        this.AGENCYNAME = AGENCYNAME;
    }


    public void addListProductEvent(EventProduct eventProduct) {
        if (PRODUCTEVENT == null) {
            PRODUCTEVENT = new ArrayList<>();
        }

        PRODUCTEVENT.add(eventProduct);
    }

    public List<EventProduct> getListProductEvent() {
        if (PRODUCTEVENT == null) {
            PRODUCTEVENT = new ArrayList<>();
        }

        return PRODUCTEVENT;
    }

    public void resetListProductEvent() {
        if (PRODUCTEVENT == null) {
            PRODUCTEVENT = new ArrayList<>();
        }
        PRODUCTEVENT.clear();
    }

    public List<GeneralInfo> getEventCodeResult() {
        return eventCodeResult;
    }

    public void setEventCodeResult(List<GeneralInfo> eventCodeResult) {
        this.eventCodeResult = eventCodeResult;
    }

}
