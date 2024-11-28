package com.payby.pos.ecr.bluetooth;

import javax.bluetooth.*;
import java.util.Vector;

public class ServiceDiscovery {

    private static final UUID SPP_UUID = new UUID("00001101-0000-1000-8000-00805F9B34FB".replaceAll("-", ""), false); // 默认的串口通信定义

    private DiscoveryAgent discoveryAgent;

    private final Object discoveryLock = new Object();

    private int transId;
    private Vector<String> servicesList = new Vector<>();

    private OnServiceDiscoveryListener listener;

    public void setOnServiceDiscoveryListener(OnServiceDiscoveryListener l) {
        this.listener = l;
    }

    public void startDiscoveryService(RemoteDevice device) {
        try {
            int[] attrIds = new int[] { 0x0100 };               // 服务名的属性
            UUID[] searchUuidSet = new UUID[] { SPP_UUID };     // 通常为 Serial Port UUID
            // 搜索特定设备的服务
            synchronized (discoveryLock) {
                String bluetoothAddress = device.getBluetoothAddress();
                String friendlyName = device.getFriendlyName(false);
                System.out.println("Finding service... " + bluetoothAddress + " ---> " + friendlyName);
                LocalDevice localDevice = LocalDevice.getLocalDevice();
                discoveryAgent = localDevice.getDiscoveryAgent();
                transId = discoveryAgent.searchServices(attrIds, searchUuidSet, device, discoveryListener);
                discoveryLock.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopDiscoveryService() {
        synchronized (discoveryLock) {
            discoveryLock.notifyAll();
        }
        if (discoveryAgent == null) return;
        try {
            System.out.println("Stop find service...");
            discoveryAgent.cancelServiceSearch(transId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final DiscoveryListener discoveryListener = new DiscoveryListener() {

        @Override
        public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
            System.out.println("Find Device: " + remoteDevice.getBluetoothAddress() + " --->");
        }

        @Override
        public void servicesDiscovered(int transID, ServiceRecord[] serviceRecords) {
            System.out.println("Find Service: " + transID);
            for (ServiceRecord serviceRecord : serviceRecords) {
                String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                if (url == null || url.trim().length() == 0) continue;
                servicesList.add(url);
                DataElement dataElement = serviceRecord.getAttributeValue(0x0100);
                if (dataElement != null) {
                    String serviceName = (String) dataElement.getValue();
                    System.out.println("Find Service: " + serviceName + " ---> " + url);
                } else {
                    System.out.println("Find Service: " + url);
                }
            }
        }

        @Override
        public void serviceSearchCompleted(int transID, int respCode) {
            System.out.println("Find Service completed: " + transID + ", " + respCode);
            synchronized (discoveryLock) {
                discoveryLock.notifyAll();
            }
            if (listener != null) {
                listener.onServiceDiscovered(servicesList);
            }
        }

        @Override
        public void inquiryCompleted(int discType) {
            System.out.println("Find Device completed: " + discType);
        }

    };

    interface OnServiceDiscoveryListener {

        void onServiceDiscovered(Vector<String> list);

    }

}
