package csc133;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
/**
 * The SPOT class holds global variables and GLFW callback instances
 * for managing the application's window state and behavior.
 */
public class SPOT {

    // GLFW callbacks
    public static GLFWErrorCallback errorCallback;
    static GLFWKeyCallback keyCallback;
    public static GLFWFramebufferSizeCallback fbCallback;

    // window variables
    public static int WIN_WIDTH, WIN_HEIGHT;
    public static long window;
    public static int WIN_POS_X, WIN_POS_Y;

    public static double lastFrameTime;
}
