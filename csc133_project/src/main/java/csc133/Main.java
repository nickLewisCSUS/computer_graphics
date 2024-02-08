

package csc133;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Main {
    // GLFW callbacks
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWFramebufferSizeCallback fbCallback;

    // Window parameters
    private static final int glViewportX = 0;
    private static final int glViewportY = 0;
    private static final int numSamples = 8;
    private long window;
    private static int WIN_WIDTH = 1800, WIN_HEIGHT = 1200;
    static int WIN_POS_X = 30, WIN_POX_Y = 90;

    // Clear color
    float CLEAR_COLOR_RED = 0.0f;
    float CLEAR_COLOR_GREEN = 0.0f;
    float CLEAR_COLOR_BLUE = 1.0f;
    float CLEAR_COLOR_ALPHA = 1.0f;

    // Viewport coordinates
    private static final float squareSize = 10f;
    private static final float halfSquareSize = squareSize / 2f;
    private static final float yOffset = 145f;
    private static final float xOffset = squareSize;

    private static final float xMin = -halfSquareSize + xOffset;
    private static final float xMax = halfSquareSize + xOffset;
    private static final float yMin = -halfSquareSize + yOffset;
    private static final float yMax = halfSquareSize + yOffset;


    // Vertex pointer parameters
    private static final int glVertexPointerSize = 2, glVertexPointerStride = 0;
    private static final long glVertexPointerPoint = 0L;

    // OpenGL matrix size
    private static final int OGL_MATRIX_SIZE = 16;

    // Orthographic projection parameters
    private static final float ORTHO_LEFT = 0f;
    private static final float ORTHO_RIGHT = 200f;
    private static final float ORTHO_BOTTOM = 0f;
    private static final float ORTHO_TOP = 200f;
    private static final float ORTHO_NEAR = 0f;
    private static final float ORTHO_FAR = 10f;

    // Color parameters
    private static final float COLOR_RED = 1.0f;
    private static final float COLOR_GREEN = 0.498f;
    private static final float COLOR_BLUE = 0.153f;

    // Fragment shader color parameters
    private static final float FRAG_COLOR_RED = 0.5f;
    private static final float FRAG_COLOR_GREEN = 0.7f;
    private static final float FRAG_COLOR_BLUE = 0.1f;
    private static final float FRAG_COLOR_ALPHA = 1.0f;

    // Draw parameters
    private static final int DRAW_COUNT = 6;
    private static final long DRAW_OFFSET = 0L;

    // call glCreateProgram() here - we have no gl-context here
    private int shader_program;
    private Matrix4f viewProjMatrix = new Matrix4f();
    private FloatBuffer myFloatBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
    private int vpMatLocation = 0, renderColorLocation = 0;
    public static void main(String[] args) {
        new slWindow().slWindow(WIN_WIDTH, WIN_HEIGHT);
        new Main().render();
    } // public static void main(String[] args)
    void render() {
        try {
            initGLFWindow();
            renderLoop();
            glfwDestroyWindow(window);
            keyCallback.free();
            fbCallback.free();
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    } // void render()
    private void initGLFWindow() {
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
        glfwSetWindowPos(window, WIN_POS_X, WIN_POX_Y);
        glfwMakeContextCurrent(window);
        int VSYNC_INTERVAL = 1;
        glfwSwapInterval(VSYNC_INTERVAL);
        glfwShowWindow(window);
    } // private void initGLFWindow()
    void renderLoop() {
        glfwPollEvents();
        initOpenGL();
        renderObjects();
        /* Process window messages in the main thread */
        while (!glfwWindowShouldClose(window)) {
            glfwWaitEvents();
        }
    } // void renderLoop()
    void initOpenGL() {
        GL.createCapabilities();


        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glViewport(glViewportX, glViewportY, WIN_WIDTH, WIN_HEIGHT);
        glClearColor(CLEAR_COLOR_RED, CLEAR_COLOR_GREEN, CLEAR_COLOR_BLUE, CLEAR_COLOR_ALPHA);
        this.shader_program = glCreateProgram();
        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs,
                "uniform mat4 viewProjMatrix;" +
                        "void main(void) {" +
                        " gl_Position = viewProjMatrix * gl_Vertex;" +
                        "}");
        glCompileShader(vs);
        glAttachShader(shader_program, vs);
        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,
                "uniform vec3 color;" +
                        "void main(void) {" +
                        " gl_FragColor = vec4(" + FRAG_COLOR_RED + ", " + FRAG_COLOR_GREEN + ", " +
                        FRAG_COLOR_BLUE + ", " + FRAG_COLOR_ALPHA + ");" +
                        "}");
        glCompileShader(fs);
        glAttachShader(shader_program, fs);
        glLinkProgram(shader_program);
        glUseProgram(shader_program);
        vpMatLocation = glGetUniformLocation(shader_program, "viewProjMatrix");
        return;
    } // void initOpenGL()
    void renderObjects() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            int MAX_ROWS = 7, MAX_COLS = 5;

            for (int row = 0; row < MAX_ROWS; row++) { // Change 2 to the desired number of rows
                for (int col = 0; col < MAX_COLS; col++) { // Change 5 to the desired number of columns
                    int vbo = glGenBuffers();
                    int ibo = glGenBuffers();
                    int flipRow = 1-row;
                    float padding = 10f;

                    float renderXOffset = col * (squareSize + padding); // Calculate offset for columns
                    float renderYOffset = (flipRow) * (squareSize + padding); // Calculate offset for rows


                    float[] vertices = {
                            xMin + renderXOffset, yMin + renderYOffset,                      // Bottom-left
                            xMax + renderXOffset, yMin + renderYOffset,                      // Bottom-right
                            xMax + renderXOffset, yMax + renderYOffset,                      // Top-right
                            xMin + renderXOffset, yMax + renderYOffset                       // Top-left
                    };

                    int[] indices = {0, 1, 2, 0, 2, 3};

                    glBindBuffer(GL_ARRAY_BUFFER, vbo);
                    glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip(), GL_STATIC_DRAW);
                    glEnableClientState(GL_VERTEX_ARRAY);
                    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
                    glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL_STATIC_DRAW);
                    glVertexPointer(glVertexPointerSize, GL_FLOAT, glVertexPointerStride, glVertexPointerPoint);

                    viewProjMatrix.setOrtho(ORTHO_LEFT, ORTHO_RIGHT, ORTHO_BOTTOM, ORTHO_TOP, ORTHO_NEAR, ORTHO_FAR);
                    glUniformMatrix4fv(vpMatLocation, false, viewProjMatrix.get(myFloatBuffer));
                    glUniform3f(renderColorLocation, COLOR_RED, COLOR_GREEN, COLOR_BLUE);
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    glDrawElements(GL_TRIANGLES, DRAW_COUNT , GL_UNSIGNED_INT, DRAW_OFFSET);

                    glDeleteBuffers(vbo);
                    glDeleteBuffers(ibo);
                }
            }

            glfwSwapBuffers(window);
        }
    } // renderObjects
}
