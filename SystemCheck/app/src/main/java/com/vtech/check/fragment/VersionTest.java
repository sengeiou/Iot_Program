package com.vtech.check.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtech.check.R;

import static android.os.Build.UNKNOWN;


public class VersionTest  extends BaseTest {

    View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootview=inflater.inflate(R.layout.version,container,false);

        setVersionInfo(rootview);
        return rootview;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }

    private void setVersionInfo(View rootview) {
        TextView codename = (TextView) rootview.findViewById(R.id.version_software_codename);
        TextView incremental = (TextView) rootview.findViewById(R.id.version_software_incremental);
        TextView release = (TextView) rootview.findViewById(R.id.version_software_release);
        TextView sdk = (TextView) rootview.findViewById(R.id.version_software_sdk);
        TextView baseband = (TextView) rootview.findViewById(R.id.version_software_baseband);
        TextView display = (TextView) rootview.findViewById(R.id.version_software_display);

  /*      TextView tv_cs=(TextView) rootview.findViewById(R.id.tv_cs);
        String str= SystemProperties.get("baseband_version", "");
        String str1= SystemProperties.get("ro.sys.cputype", "");
        String str2= SystemProperties.get("ro.product.firmware", "");*/


/*        private static final String KEY_LEGAL_CONTAINER = "legal_container";

        private static final String KEY_SOFTWARE_VERSION = "software_version";
        private static final String KEY_CPU_TYPE = "cpu_type";
        private static final String PROPERTY_CPUTYPE = "ro.sys.cputype";
        private static final String PROPETY_FIRMWARE_VERSION = "ro.product.firmware";
        private static final String KEY_BASEBAND_VERSION = "baseband_version";*/


//        tv_cs.setText(
//                       str+"\n"+
//                               str1+"\n"+
//                               str2+"\n"
//                        Build.getRadioVersion()+"\n"+
//                        Build.BOARD+"\n"+
//                        Build.BOOTLOADER+"\n"+
//                        Build.BRAND+"\n"+
//                        Build.DEVICE+"\n"+
//                        Build.DISPLAY+"\n"+
//                        Build.FINGERPRINT+"\n"+
//                        Build.HARDWARE+"\n"+
//                        Build.HOST+"\n"+
//                        Build.ID+"\n"+
//                        Build.MANUFACTURER+"\n"+
//                        Build.VERSION.CODENAME+"\n"+
//                        Build.SERIAL+"\n"+
//                        Build.MODEL+"\n"+
//                        Build.PRODUCT+"\n"+
//                        Build.TAGS+"\n"+
//                        Build.VERSION.SDK+"\n"+
//                        Build.MODEL+"\n"+
//                        Build.VERSION.RELEASE+"\n"+
//                        Build.VERSION.SDK_INT+"\n"
//                );


        codename.setText(Build.VERSION.CODENAME);
        incremental.setText(Build.VERSION.INCREMENTAL);
        release.setText(Build.VERSION.RELEASE);
        sdk.setText(Build.VERSION.SDK_INT + "");
        baseband.setText(Build.getRadioVersion());
        display.setText(getString("ro.build.display.vtechid"));

        TextView board = (TextView) rootview.findViewById(R.id.version_hardware_board);
        TextView model = (TextView) rootview.findViewById(R.id.version_hardware_model);
        TextView device = (TextView) rootview.findViewById(R.id.version_hardware_device);
        TextView manufacture = (TextView) rootview.findViewById(R.id.version_hardware_manufacture);

        board.setText(Build.BOARD);
        model.setText(Build.MODEL);
        device.setText(Build.DEVICE);
        manufacture.setText(Build.MANUFACTURER);
    }


    private static String getString(String property) {
        return SystemProperties.get(property, UNKNOWN);
    }


    @Override
    public String getTestName() {
        return getContext().getString(R.string.version_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("version", true);
    }


}
