package dev.codex.miuibackgesturehook.hooks.systemui;

import android.content.Context;

/**
 * Flyme keeps {@code EdgeBackView} as the {@code NavigationEdgeBackPlugin} and feeds
 * {@code EdgeBackGestureHandler} from {@code registerSystemGestureListener}. This adapter
 * must not create AOSP {@code BackPanelController} or a competing InputMonitor.
 */
final class SystemUiFlymeImpl extends SystemUiAndroid16Impl {
    @Override
    String name() {
        return "flyme";
    }

    @Override
    boolean shouldInstallBackInputMonitor() {
        return false;
    }

    @Override
    Class<?> backAnimationParameterClass(ClassLoader classLoader) throws Exception {
        return Class.forName("com.android.wm.shell.back.BackAnimation", false, classLoader);
    }

    @Override
    Object ensureNativeEdgeBackPlugin(Object edgeBackGestureHandler,
                                      Context context) throws Exception {
        return findNativeEdgeBackPlugin(edgeBackGestureHandler);
    }

    @Override
    void prepareNativeBackPanel(Object edgeBackGestureHandler,
                                Object plugin) throws Exception {
        // Flyme EdgeBackView owns its own layout, colors, and trigger animation.
    }

    @Override
    void updateDisplaySize(Object edgeBackGestureHandler,
                           Object plugin) throws Exception {
        try {
            invokeCompatible(edgeBackGestureHandler, "updateDisplaySize");
        } catch (Throwable ignored) {
            invokeCompatible(edgeBackGestureHandler, "updateDisplaySize$1");
        }
    }
}
