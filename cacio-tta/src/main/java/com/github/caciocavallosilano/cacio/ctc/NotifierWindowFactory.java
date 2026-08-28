package com.github.caciocavallosilano.cacio.ctc;


import com.github.caciocavallosilano.cacio.peer.*;
import com.github.caciocavallosilano.cacio.peer.managed.FullScreenWindowFactory;
import com.github.caciocavallosilano.cacio.peer.managed.PlatformScreen;

public class NotifierWindowFactory implements PlatformWindowFactory {
    private final PlatformWindowFactory parentFactory;

    public NotifierWindowFactory(PlatformScreen screen, CacioEventSource s) {
        parentFactory = new FullScreenWindowFactory(screen, s);
    }

    @Override
    public PlatformWindow createPlatformWindow(CacioComponent awtComponent, PlatformWindow parent) {
        return parentFactory.createPlatformWindow(awtComponent, parent);
    }

    @Override
    public PlatformToplevelWindow createPlatformToplevelWindow(CacioComponent component) {
        onToplevelWindowCreated();
        return parentFactory.createPlatformToplevelWindow(component);
    }

    @Override
    public CacioEventPump<?> createEventPump() {
        return parentFactory.createEventPump();
    }

    @Override
    public PlatformWindow createPlatformToplevelWindow(CacioComponent component, PlatformWindow owner) {
        return parentFactory.createPlatformToplevelWindow(component, owner);
    }

    private static native void onToplevelWindowCreated();
}
