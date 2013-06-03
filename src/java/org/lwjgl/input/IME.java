package org.lwjgl.input;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.InputImplementation;

/**
 * The IME interface.  Can be used to control native IME functionality (on Windows at least).
 */
public class IME
{

    /** Has the IME been created? */
    private static boolean created;

    /** One time initialization. */
    private static boolean initialized;

    /** The native implementation. */
    private static InputImplementation implementation;

    /**
     * IME cannot be constructed.
     */
    private IME ()
    {
    }

    /**
     * Static initialization
     */
    private static void initialize() {
        if (initialized)
            return;
        Sys.initialize();
        initialized = true;
    }

    /**
     * "Create" the IME with the given implementation. This is used
     * reflectively from AWTInputAdapter.
     *
     * @throws LWJGLException if the ime could not be created for any reason
     */
    private static void create(InputImplementation impl) throws LWJGLException {
        if (created)
            return;
        if (!initialized)
            initialize();
        implementation = impl;
        implementation.createIME();
        created = true;
    }

    /**
     * "Create" the IME. The display must first have been created. The
     * reason for this is so we can attach the IME context to the display.
     *
     * @throws LWJGLException if the ime could not be created for any reason
     */
    public static void create() throws LWJGLException {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Display.isCreated()) throw new IllegalStateException("Display must be created.");

            create(OpenGLPackageAccess.createImplementation());
        }
    }

    /**
     * @return true if the ime has been created
     */
    public static boolean isCreated() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return created;
        }
    }

    /**
     * "Destroy" the ime
     */
    public static void destroy() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!created)
                return;
            created = false;
            implementation.destroyIME();
        }
    }

    /**
     * Set the enabled state of the ime.
     */
    public static void setEnabled (boolean enabled)
    {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!created) {
                throw new IllegalStateException(
                        "IME must be created before you can change enabled state");
            }
            implementation.setIMEEnabled(enabled);
        }
    }

}
