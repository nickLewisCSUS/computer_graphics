package SlRenderer;



import csc133.slWindow;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL20.*;


public class slSingleBatchRenderer {

    private static slWindow window;

    // Clear color
    float CLEAR_COLOR_RED = 0.5f;
    float CLEAR_COLOR_GREEN = 0.1f;
    float CLEAR_COLOR_BLUE = 0.5f;
    float CLEAR_COLOR_ALPHA = 1.0f;


    // Fragment shader color parameters
    private static final float FRAG_COLOR_RED = 0.5f;
    private static final float FRAG_COLOR_GREEN = 0.7f;
    private static final float FRAG_COLOR_BLUE = 0.1f;
    private static final float FRAG_COLOR_ALPHA = 1.0f;

    // Draw parameters
    private static final int DRAW_COUNT = 6;
    private static final long DRAW_OFFSET = 0L;

    private int shader_program;
    private Matrix4f viewProjMatrix = new Matrix4f();
    private FloatBuffer myFloatBuffer = BufferUtils.createFloatBuffer(SPOT.OGL_MATRIX_SIZE);
    private int vpMatLocation = 0, renderColorLocation = 0;

    public slSingleBatchRenderer() {
        window = new slWindow(SPOT.WIN_WIDTH, SPOT.WIN_HEIGHT, SPOT.WIN_POS_X, SPOT.WIN_POS_Y);
    } // public static void main(String[] args)

    public void render() {
        try {
            renderLoop();
            window.getKeyCallback().free();
            window.getFbCallback().free();
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
        window.destroy();
    } // void render()
    void renderLoop() {
        glfwPollEvents();
        initOpenGL();
        renderObjects();
        /* Process window messages in the main thread */
        while (!glfwWindowShouldClose(window.getWindowID())) {
            glfwWaitEvents();
        }
    } // void renderLoop()
    void initOpenGL() {
        GL.createCapabilities();


        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glViewport(SPOT.glViewportX, SPOT.glViewportY, SPOT.WIN_WIDTH, SPOT.WIN_HEIGHT);
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
        while (!glfwWindowShouldClose(window.getWindowID())) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            for (int row = 0; row < SPOT.MAX_ROWS; row++) { // Change 2 to the desired number of rows
                for (int col = 0; col < SPOT.MAX_COLS; col++) { // Change 5 to the desired number of columns
                    int vbo = glGenBuffers();
                    int ibo = glGenBuffers();
                    int flipRow = 1-row;

                    float renderXOffset = col * (SPOT.squareSize + SPOT.padding); // Calculate offset for columns
                    float renderYOffset = (flipRow) * (SPOT.squareSize + SPOT.padding); // Calculate offset for rows


                    float[] vertices = {
                            SPOT.xMin + renderXOffset, SPOT.yMin + renderYOffset,                      // Bottom-left
                            SPOT.xMax + renderXOffset, SPOT.yMin + renderYOffset,                      // Bottom-right
                            SPOT.xMax + renderXOffset, SPOT.yMax + renderYOffset,                      // Top-right
                            SPOT.xMin + renderXOffset, SPOT.yMax + renderYOffset                       // Top-left
                    };

                    int[] indices = {0, 1, 2, 0, 2, 3};

                    glBindBuffer(GL_ARRAY_BUFFER, vbo);
                    glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip(), GL_STATIC_DRAW);
                    glEnableClientState(GL_VERTEX_ARRAY);
                    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
                    glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL_STATIC_DRAW);
                    glVertexPointer(SPOT.glVertexPointerSize, GL_FLOAT, SPOT.glVertexPointerStride, SPOT.glVertexPointerPoint);

                    //viewProjMatrix.setOrtho(ORTHO_LEFT, ORTHO_RIGHT, ORTHO_BOTTOM, ORTHO_TOP, ORTHO_NEAR, ORTHO_FAR);
                    slCamera my_cam = new slCamera();
                    my_cam.setProjectionOrtho(SPOT.ORTHO_LEFT, SPOT.ORTHO_RIGHT, SPOT.ORTHO_BOTTOM, SPOT.ORTHO_TOP, SPOT.ORTHO_NEAR, SPOT.ORTHO_FAR);
                    viewProjMatrix = my_cam.getProjectionMatrix();

                    glUniformMatrix4fv(vpMatLocation, false, viewProjMatrix.get(myFloatBuffer));
                    glUniform3f(renderColorLocation, SPOT.COLOR_BLUE, SPOT.COLOR_GREEN, SPOT.COLOR_BLUE);
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    glDrawElements(GL_TRIANGLES, DRAW_COUNT , GL_UNSIGNED_INT, DRAW_OFFSET);

                    glDeleteBuffers(vbo);
                    glDeleteBuffers(ibo);
                }
            }

            glfwSwapBuffers(window.getWindowID());
        }
    } // renderObjects

}
