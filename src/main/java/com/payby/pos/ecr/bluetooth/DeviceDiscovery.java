package com.payby.pos.ecr.bluetooth;

import javax.bluetooth.*;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceDiscovery {

    private DiscoveryAgent discoveryAgent;

    private final Object discoveryLock = new Object();

    private final ConcurrentHashMap<String, RemoteDevice> mapDevice = new ConcurrentHashMap<>();

    private OnDeviceDiscoveryListener listener;

    public void setOnDeviceDiscoveryListener(OnDeviceDiscoveryListener l) {
        this.listener = l;
    }

    public void startDiscoveryDevice() {
        stopDiscoveryDevice();
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            String friendlyName = localDevice.getFriendlyName();
            String bluetoothAddress = localDevice.getBluetoothAddress();
            System.out.println("Local device: " + friendlyName + " (" + bluetoothAddress + ")");
            // 开始设备发现
            synchronized (discoveryLock) {
                discoveryAgent = localDevice.getDiscoveryAgent();
                boolean started = discoveryAgent.startInquiry(DiscoveryAgent.GIAC, discoveryListener);
                if (started) {
                    System.out.println("Finding devices...");
                    discoveryLock.wait();
                } else {
                    System.out.println("Finding devices not started");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopDiscoveryDevice() {
        synchronized (discoveryLock) {
            discoveryLock.notifyAll();
        }
        if (discoveryAgent == null) return;
        try {
            System.out.println("Stop find devices...");
            discoveryAgent.cancelInquiry(discoveryListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final DiscoveryListener discoveryListener = new DiscoveryListener() {

        @Override
        public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
            if (remoteDevice != null) {
                String friendlyName = "";
                String bluetoothAddress = remoteDevice.getBluetoothAddress();
                try {
                    friendlyName = remoteDevice.getFriendlyName(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Find Device: " + friendlyName + " (" + bluetoothAddress + ")");
                mapDevice.put(bluetoothAddress, remoteDevice);
            } else {
                System.out.println("Find Device: null");
            }
        }

        @Override
        public void servicesDiscovered(int transID, ServiceRecord[] serviceRecords) {
            System.out.println("Find Service: " + transID);
        }

        @Override
        public void serviceSearchCompleted(int transID, int respCode) {
            System.out.println("Find Service completed: " + transID + ", " + respCode);
        }

        @Override
        public void inquiryCompleted(int discType) {
            System.out.println("Find  Device completed: " + discType);
            synchronized (discoveryLock) {
                discoveryLock.notifyAll();
            }
            if (listener != null) {
                listener.onDeviceDiscovered(mapDevice);
            }
        }

    };

    interface OnDeviceDiscoveryListener {

        void onDeviceDiscovered(ConcurrentHashMap<String, RemoteDevice> map);

    }

}
