package com.github.caciocavallosilano.cacio.ctc;

import sun.misc.Unsafe;

import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;

public class CTCPreloadAgent {
    static {
        try {
            Field toolkit = Toolkit.class.getDeclaredField("toolkit");
            toolkit.setAccessible(true);
            toolkit.set(null, new CTCToolkit());

            Field defaultHeadlessField = java.awt.GraphicsEnvironment.class.getDeclaredField("defaultHeadless");
            defaultHeadlessField.setAccessible(true);
            defaultHeadlessField.set(null,Boolean.TRUE);
            Field headlessField = java.awt.GraphicsEnvironment.class.getDeclaredField("headless");
            headlessField.setAccessible(true);
            headlessField.set(null,Boolean.TRUE);

            Class<?> geCls = Class.forName("java.awt.GraphicsEnvironment$LocalGE");
            Field ge = geCls.getDeclaredField("INSTANCE");
            ge.setAccessible(true);
            defaultHeadlessField.set(null, Boolean.FALSE);
            headlessField.set(null,Boolean.FALSE);

            Class<?> smfCls = Class.forName("sun.java2d.SurfaceManagerFactory");
            Field smf = smfCls.getDeclaredField("instance");
            smf.setAccessible(true);
            smf.set(null, null);

            setFinalStatic(ge, new CTCGraphicsEnvironment());

            String propertyFontManager = System.getProperty("cacio.font.fontmanager");
            if (propertyFontManager != null) {
                FontManagerUtil.setFontManager(propertyFontManager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setProperty("swing.defaultlaf", MetalLookAndFeel.class.getName());
    }
    // https://stackoverflow.com/a/71465198
    public static void setFinalStatic(Field field, Object value) throws Exception{
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        Object fieldBase = unsafe.staticFieldBase(field);
        long fieldOffset = unsafe.staticFieldOffset(field);

        unsafe.putObject(fieldBase, fieldOffset, value);
    }
    public static void premain(String args, Instrumentation inst) {

    }
}
