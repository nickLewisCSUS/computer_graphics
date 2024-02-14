
package csc133;


import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class slWindow {

    // GLFW callbacks
    private GLFWErrorCallback errorCallback;
    GLFWKeyCallback keyCallback;
    private GLFWFramebufferSizeCallback fbCallback;

    // Window parameters
    private int numSamples = 8;
    private int WIN_WIDTH, WIN_HEIGHT;
    private long window;
    static int WIN_POS_X, WIN_POS_Y;

    public slWindow(int win_width, int win_height, int win_x, int win_y) {

        System.out.println("Call to slWindow:: (width, height) == ("
                        + win_width + ", " + win_height +") received!");

        this.WIN_WIDTH = win_width;
        this.WIN_HEIGHT = win_height;
        this.WIN_POS_X = win_x;
        this. WIN_POS_Y = win_y;
        initGLFWindow();
    }

    public void initGLFWindow() {
        glfwSetErrorCallback(errorCallback =
                GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, numSamples);
        window = glfwCreateWindow(WIN_WIDTH, WIN_HEIGHT, "CSC 133", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int
                    mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
            }
        });
        glfwSetFramebufferSizeCallback(window, fbCallback = new
                GLFWFramebufferSizeCallback() {
                    @Override
                    public void invoke(long window, int w, int h) {
                        if (w > 0 && h > 0) {
                            WIN_WIDTH = w;
                            WIN_HEIGHT = h;
                        }
                    }
                });
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, WIN_POS_X, WIN_POS_Y);
        glfwMakeContextCurrent(window);
        int VSYNC_INTERVAL = 1;
        glfwSwapInterval(VSYNC_INTERVAL);
        glfwShowWindow(window);
    } // private void initGLFWindow()


    public void destroy(){
        glfwDestroyWindow(window);
    }
    public long getWindowID(){
        return window;
    }
    public GLFWErrorCallback getErrorCallback(){
        return errorCallback;
    }
    public GLFWKeyCallback getKeyCallback(){
        return keyCallback;
    }
    public GLFWFramebufferSizeCallback getFbCallback(){
        return fbCallback;
    }

}
