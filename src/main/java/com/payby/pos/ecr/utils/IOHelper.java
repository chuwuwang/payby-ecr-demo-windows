package com.payby.pos.ecr.utils;

import java.io.Closeable;

public final class IOHelper {

    private IOHelper() {
        throw new AssertionError("The create IOHelper instance is forbidden");
    }

    public static void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}