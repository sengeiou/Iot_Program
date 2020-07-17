package com.vtech.app.largeuidb;

import com.vtech.app.R;

public class Constants {
    //item type
    //fixed type
    public static final int ITEM_TYPE_COUNT = 11;
    public static final int ITEM_TYPE_SOS = 0;
    public static final int ITEM_TYPE_DIALER = 1;
    public static final int ITEM_TYPE_CHAT = 2;
    public static final int ITEM_TYPE_HEALTH = 3;
    public static final int ITEM_TYPE_MSG = 4;
//    public static final int ITEM_TYPE_NEAR = 5;
//    public static final int ITEM_TYPE_IOT_ALARM = 6;
//    public static final int ITEM_TYPE_SETTINGS = 7;
//    public static final int ITEM_TYPE_CONTACTS = 8;

    public static final int ITEM_TYPE_NEAR = -1;
    public static final int ITEM_TYPE_IOT_ALARM = 5;
    public static final int ITEM_TYPE_SETTINGS = 6;
    public static final int ITEM_TYPE_CONTACTS = 7;
    public static final int ITEM_TYPE_CHAT_WHATSAPP = 8;
    public static final int ITEM_TYPE_CHAT_WX = 9;
    //non fixed type
    public static final int ITEM_TYPE_NORMAL = 20;

    public static final String[] DEFAULT_CHAT_APPS_PACKAGE_NAME = new String[] {
            "com.whatsapp",
            "com.tencent.mm",
    };

    public static final String[] DEFAULT_ADD_APP_PACKAGE_NAME = new String[] {
            "", //sos
            "",//"com.android.dialer",
            "",//"com.whatsapp",//"com.tencent.mm",
            "com.vtech.vhealth",
            "com.android.messaging",
            //"", //nearby
            "com.vtech.homesecurity",
            "com.android.settings",
            "",//"com.android.contacts",
    };

    public static final Integer[] DEFAULT_APP_ITEM_BG_COLOR = new Integer[] {
            R.color.item_app_color_mode_0,
            R.color.item_app_color_mode_1,
            R.color.item_app_color_mode_2,
            R.color.item_app_color_mode_3,
            R.color.item_app_color_mode_4,
            R.color.item_app_color_mode_5
    };

    //contact type
    public static final int CONTACT_TYPE_SOS = 21;
    public static final int CONTACT_TYPE_NORMAL = 22;

    //action
    public static final String APP_ITEM_COUNT_CHANGED_ACTION = "com.vtech.app.APP_ITEM_COUNT_CHANGED_ACTION";
    public static final String APP_ITEM_COUNT_PAGE_CHANGED_ACTION = "com.vtech.app.APP_ITEM_COUNT_PAGE_CHANGED_ACTION";

    //PAGE APP MAX COUNT
    public static final int PAGE_APP_MAX_COUNT = 6;
    public static final int PAGE_COLUMN = 2;

    public static final String CONTACT_LIST_FLUSH = "contact_list_flush";

    public static final String RECORD_LIST_FLUSH = "record_list_flush";

    public static final String SOS_IMPORT_CONTACT_LIST = "sos_import_contact_list";

    public static final String SOS_SET_CONTACT_LIST = "sos_set_contact_list";
}
