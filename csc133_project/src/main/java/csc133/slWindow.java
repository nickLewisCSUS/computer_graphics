
package csc133;


import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class slWindow {

    public slWindow(int win_width, int win_height, int win_x, int win_y) {

        System.out.println("Call to slWindow:: (width, height) == ("
                        + win_width + ", " + win_height +") received!");

        SPOT.WIN_WIDTH = win_width;
        SPOT.WIN_HEIGHT = win_height;
        SPOT.WIN_POS_X = win_x;
        SPOT.WIN_POS_Y = win_y;
        initGLFWindow();
    }

    public void initGLFWindow() {
        glfwSetErrorCallback(SPOT.errorCallback =
                GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        // Window parameters
        int numSamples = 8;
        glfwWindowHint(GLFW_SAMPLES, numSamples);
        SPOT.window = glfwCreateWindow(SPOT.WIN_WIDTH, SPOT.WIN_HEIGHT, "CSC 133", NULL, NULL);
        if (SPOT.window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetKeyCallback(SPOT.window, SPOT.keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int
                    mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
            }
        });
        glfwSetFramebufferSizeCallback(SPOT.window, SPOT.fbCallback = new
                GLFWFramebufferSizeCallback() {
                    @Override
                    public void invoke(long window, int w, int h) {
                        if (w > 0 && h > 0) {
                            SPOT.WIN_WIDTH = w;
                            SPOT.WIN_HEIGHT = h;
                        }
                    }
                });
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(SPOT.window, SPOT.WIN_POS_X, SPOT.WIN_POS_Y);
        glfwMakeContextCurrent(SPOT.window);
        int VSYNC_INTERVAL = 1;
        glfwSwapInterval(VSYNC_INTERVAL);
        glfwShowWindow(SPOT.window);
    } // private void initGLFWindow()


    public void destroy(){
        glfwDestroyWindow(SPOT.window);
    }
    public long getWindowID(){
        return SPOT.window;
    }
    public GLFWErrorCallback getErrorCallback(){
        return SPOT.errorCallback;
    }
    public GLFWKeyCallback getKeyCallback(){
        return SPOT.keyCallback;
    }
    public GLFWFramebufferSizeCallback getFbCallback(){
        return SPOT.fbCallback;
    }

    public boolean isKeyPressed(int key) {
        return glfwGetKey(SPOT.window, key) == GLFW_PRESS;
    }
    public static double getDeltaTime() {
        double currentTime = getTime();
        double deltaTime = currentTime - SPOT.lastFrameTime;
        SPOT.lastFrameTime = currentTime;
        return deltaTime;
    }
    public static double getTime() {
        return System.nanoTime() / 1_000_000_000.0; // Convert nanoseconds to seconds
    }



}
