package org.lwjgl.opengl;

/**
 * This is the Windows implementation of the IME handling.
 */

import org.lwjgl.LWJGLException;

final class WindowsIME {
    private final long hwnd;
    private final long himc;

    WindowsIME (long hwnd)
        throws LWJGLException
    {
        this.hwnd = hwnd;
        himc = CreateContext();
        setEnabled(true);
    }

    public void destroy ()
    {
        setEnabled(false);
        DestroyContext(himc);
    }

    void setEnabled (boolean enabled)
    {
        if (_enabled != enabled) {
            _enabled = enabled;
            AssociateContext(hwnd, enabled ? himc : 0L);
        }
    }

    private static native long CreateContext ();
    private static native boolean DestroyContext (long himc);
    private static native long AssociateContext (long hwnd, long himc);

    private boolean _enabled = false;

}
