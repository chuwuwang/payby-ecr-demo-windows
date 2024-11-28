package com.payby.pos.ecr.bluetooth;

import com.payby.pos.ecr.utils.IOHelper;
import com.payby.pos.ecr.utils.ThreadPoolManager;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class BluetoothClient {

    private final DeviceDiscovery devicesDiscovery;
    private final ServiceDiscovery serviceDiscovery;

    private InputStream inputStream;
    private OutputStream outputStream;
    private StreamConnection streamConnection;

    private boolean running = false;

    public BluetoothClient() {
        devicesDiscovery = new DeviceDiscovery();
        devicesDiscovery.setOnDeviceDiscoveryListener(deviceDiscoveryListener);
        serviceDiscovery = new ServiceDiscovery();
        serviceDiscovery.setOnServiceDiscoveryListener(serviceDiscoveryListener);
    }

    public void start() {
        devicesDiscovery.startDiscoveryDevice();
    }

    private final DeviceDiscovery.OnDeviceDiscoveryListener deviceDiscoveryListener = new DeviceDiscovery.OnDeviceDiscoveryListener() {


        @Override
        public void onDeviceDiscovered(ConcurrentHashMap<String, RemoteDevice> map) {
            RemoteDevice remoteDevice = map.get("0C2576939F5E");
            if (remoteDevice != null) {
                serviceDiscovery.startDiscoveryService(remoteDevice);
            }
        }

    };

    private final ServiceDiscovery.OnServiceDiscoveryListener serviceDiscoveryListener = new ServiceDiscovery.OnServiceDiscoveryListener() {

        @Override
        public void onServiceDiscovered(Vector<String> list) {
            devicesDiscovery.stopDiscoveryDevice();
            serviceDiscovery.stopDiscoveryService();
            if (list == null || list.size() == 0) {
                return;
            }
            String url = list.elementAt(0);
            connect(url);
        }

    };

    private void connect(String url) {
        try {
            streamConnection = (StreamConnection) Connector.open(url);
            inputStream = streamConnection.openInputStream();
            outputStream = streamConnection.openOutputStream();
            ThreadPoolManager.INSTANCE.executeCacheTask(this::read);

            IOHelper.delay(2000);
            send("Hello, I'm Android".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read() {
        try {
            running = true;
            int len;
            byte[] bytes;
            byte[] buffer;
            String string;
            while (running && streamConnection != null && inputStream != null) {
                buffer = new byte[4 * 1024];
                len = inputStream.read(buffer);
                while (len != -1) {
                    bytes = new byte[len];
                    System.arraycopy(buffer, 0, bytes, 0, len);
                    string = getString(bytes);
                    System.out.println("---> Windows received: " + string);
//                    if (listener != null) {
//                        listener.onMessage(bytes);
//                    }
                    len = inputStream.read(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }

    public void send(byte[] bytes) {
        String string = getString(bytes);
        System.out.println("<--- Windows send: " + string);
        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        IOHelper.close(inputStream);
        IOHelper.close(outputStream);
        try {
            if (streamConnection != null) {
                streamConnection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        running = false;
        inputStream = null;
        outputStream = null;
        streamConnection = null;
        devicesDiscovery.stopDiscoveryDevice();
        serviceDiscovery.stopDiscoveryService();
    }

    private String getString(byte[] bytes) {
        try {
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
