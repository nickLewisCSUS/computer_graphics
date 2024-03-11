package SlRenderer;

import csc133.slWindow;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.nio.FloatBuffer;

public class SPOT {

    // Global variables throughout program
    // Window parameters
    public static final int WIN_WIDTH = 800, WIN_HEIGHT = 800;
    public static final int WIN_POS_X = 30, WIN_POS_Y = 90;
    public static final int glViewportX = 0;
    public static final int glViewportY = 0;

    // Vertex pointer parameters
    public static final int glVertexPointerSize = 2, glVertexPointerStride = 0;
    public static final long glVertexPointerPoint = 0L;

    // Orthographic projection parameters
    public static final float ORTHO_LEFT = 0f;
    public static final float ORTHO_RIGHT = 200f;
    public static final float ORTHO_BOTTOM = 0f;
    public static final float ORTHO_TOP = 200f;
    public static final float ORTHO_NEAR = 0f;
    public static final float ORTHO_FAR = 10f;

    // Viewport coordinates
    public static final float squareSize = 5f;
    public static final float padding = 2f;
    public static final int MAX_ROWS = 20, MAX_COLS = 18;
    public static final float halfSquareSize = SPOT.squareSize / 2f;
    public static final float yOffset = 160;
    public static final float xOffset = 40;

    public static final float xMin = -halfSquareSize + xOffset;
    public static final float xMax = halfSquareSize + xOffset;
    public static final float yMin = -halfSquareSize + yOffset;
    public static final float yMax = halfSquareSize + yOffset;

    public static final int OGL_MATRIX_SIZE = 16;

    // Color parameters
    public static final float COLOR_RED = 1.0f;
    public static final float COLOR_GREEN = 0.498f;
    public static final float COLOR_BLUE = 0.153f;

    // Fragment shader color parameters
    public static final float FRAG_COLOR_RED = 0.5f;
    public static final float FRAG_COLOR_GREEN = 0.7f;
    public static final float FRAG_COLOR_BLUE = 0.1f;
    public static final float FRAG_COLOR_ALPHA = 1.0f;

    // Draw parameters
    public static final int DRAW_COUNT = 6;
    public static final long DRAW_OFFSET = 0L;

    // Clear color
    public static final float CLEAR_COLOR_RED = 0.5f;
    public static final float CLEAR_COLOR_GREEN = 0.1f;
    public static final float CLEAR_COLOR_BLUE = 0.5f;
    public static final float CLEAR_COLOR_ALPHA = 1.0f;

    public static int shader_program;
    public static Matrix4f viewProjMatrix = new Matrix4f();
    public static FloatBuffer myFloatBuffer = BufferUtils.createFloatBuffer(SPOT.OGL_MATRIX_SIZE);
    public static int vpMatLocation = 0, renderColorLocation = 0;
    // GLFW callbacks
    public static GLFWErrorCallback errorCallback;
    public static GLFWKeyCallback keyCallback;
    public static GLFWFramebufferSizeCallback fbCallback;

    // Window object
    public static slWindow window;

    public static Matrix4f projectionMatrix;
    public static Matrix4f viewMatrix;

    // vertices for slCamera
    public static float f_left;
    public static float f_right;
    public static float f_bottom;
    public static float f_top;
    public static float f_near;
    public static float f_far;

}
