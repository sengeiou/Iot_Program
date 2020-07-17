package com.vtech.check.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vtech.check.R;
import com.vtech.check.utils.NetUtil;

import java.net.SocketException;

import static android.content.ContentValues.TAG;

public class EthernetTest extends BaseTest{

    View rootView;
    private TextView ipAddress;
/*    private TextView dnsAddress;
    private TextView subnetMask;
    private TextView gateAddress;*/
    private Spinner equipment;
    private String net;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            rootView=inflater.inflate(R.layout.ethernet,container,false);
        initView();
        initData();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }



    private void initView() {

        equipment = rootView.findViewById(R.id.equipment);
        ipAddress = rootView.findViewById(R.id.ip_address);
/*      dnsAddress = rootView.findViewById(R.id.dns_address);
        subnetMask = rootView.findViewById(R.id.subnet_mask);
        gateAddress = rootView.findViewById(R.id.net_address);*/

    }

    private void initData() {

        if (NetUtil.getAllNetInterface() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, NetUtil.getAllNetInterface());
            equipment.setAdapter(adapter);
            equipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    net = (String) equipment.getSelectedItem();
/*                    dnsAddress.setText(NetUtil.getLocalDNS(net));
                    subnetMask.setText(NetUtil.getLocalMask(net));
                    gateAddress.setText(NetUtil.getLocalGATE(net));*/
                    try {
                        ipAddress.setText(NetUtil.getIpAddress(net));
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            Toast.makeText(getContext(), "无可用网卡", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.ethernet_test);
    }
    @Override
    public boolean isNeedTest() {
        return getSystemProperties("ethernet",true);
    }



}
