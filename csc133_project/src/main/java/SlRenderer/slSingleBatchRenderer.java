package SlRenderer;
import csc133.slWindow;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import javax.swing.*;
import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static csc133.slWindow.getDeltaTime;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL20.*;


public class slSingleBatchRenderer {
    private slGoLBoard goLBoard;
    public slSingleBatchRenderer() {
        SPOT.window = new slWindow(SPOT.WIN_WIDTH, SPOT.WIN_HEIGHT, SPOT.WIN_POS_X, SPOT.WIN_POS_Y);
        goLBoard = new slGoLBoard(SPOT.MAX_ROWS, SPOT.MAX_COLS);
        initOpenGL();
    } // public static void main(String[] args)
    public void render() {
        try {
            renderLoop();
            SPOT.window.getKeyCallback().free();
            SPOT.window.getFbCallback().free();
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
        SPOT.window.destroy();
    }
    void renderLoop() {
        while (!glfwWindowShouldClose(SPOT.window.getWindowID())) {
            glfwPollEvents();

            if (!SPOT.haltRendering) {
                goLBoard.updateBoard();
                renderObjects();
                glfwSwapBuffers(SPOT.window.getWindowID());
            }
            if (SPOT.window.isKeyPressed(GLFW_KEY_SPACE)) {
                goLBoard.initializeBoard();
                SPOT.restartRendering = true;
                SPOT.haltRendering = false;
            }
            if (SPOT.window.isKeyPressed(GLFW_KEY_R) && !SPOT.rKeyPressedLastFrame) {
                goLBoard.initializeBoard();
                SPOT.restartRendering = true;
                SPOT.rKeyPressedLastFrame = true;
            } else if (!SPOT.window.isKeyPressed(GLFW_KEY_R)) {
                SPOT.rKeyPressedLastFrame = false;
            }
            if (!SPOT.restartRendering) {
                goLBoard.updateBoard();
            }
            if (SPOT.window.isKeyPressed(GLFW_KEY_D) && !SPOT.dKeyPressedLastFrame) {
                SPOT.toggleDelay = !SPOT.toggleDelay;
                SPOT.dKeyPressedLastFrame = true;
            } else if (!SPOT.window.isKeyPressed(GLFW_KEY_D)) {
                SPOT.dKeyPressedLastFrame = false;
            }
            if (SPOT.toggleDelay) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (SPOT.window.isKeyPressed(GLFW_KEY_H)) {
                SPOT.haltRendering = true;
            }
            // Toggle frame rate in console if 'f' key is pressed
            if (SPOT.window.isKeyPressed(GLFW_KEY_F) && !SPOT.fKeyPressedLastFrame) {
                SPOT.showFrameRate = !SPOT.showFrameRate;
                SPOT.fKeyPressedLastFrame = true;
            } else if (!SPOT.window.isKeyPressed(GLFW_KEY_F)) {
                SPOT.fKeyPressedLastFrame = false;
            }
            if (SPOT.showFrameRate) {
                System.out.println("Frame rate: " + (int) (1 / getDeltaTime()));
            }
            if (SPOT.window.isKeyPressed(GLFW_KEY_S) && !SPOT.sKeyPressedLastFrame) {
                saveStatusToFile();
                SPOT.sKeyPressedLastFrame = true;
            } else if (!SPOT.window.isKeyPressed(GLFW_KEY_S)) {
                SPOT.sKeyPressedLastFrame = false;
            }
            if (SPOT.window.isKeyPressed(GLFW_KEY_L) && !SPOT.lKeyPressedLastFrame) {
                loadStatusFromFile();
                SPOT.lKeyPressedLastFrame = true;
            } else if (!SPOT.window.isKeyPressed(GLFW_KEY_L)) {
                SPOT.lKeyPressedLastFrame = false;
            }
            if (SPOT.window.isKeyPressed(GLFW_KEY_SLASH) && !SPOT.questionKeyPressedLastFrame) {
                SPOT.showUsage = true;
                SPOT.questionKeyPressedLastFrame = true;
            } else if (!SPOT.window.isKeyPressed(GLFW_KEY_SLASH)) {
                SPOT.questionKeyPressedLastFrame = false;
            }
            if (SPOT.showUsage) {
                printUsage();
                SPOT.showUsage = false;
            }
            if (SPOT.window.isKeyPressed(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(SPOT.window.getWindowID(), true);
            }
            if (SPOT.restartRendering) {
                SPOT.restartRendering = false;
            }
        }
    }
     void initOpenGL() {
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glViewport(SPOT.glViewportX, SPOT.glViewportY, SPOT.WIN_WIDTH, SPOT.WIN_HEIGHT);
        glClearColor(SPOT.CLEAR_COLOR_RED, SPOT.CLEAR_COLOR_GREEN, SPOT.CLEAR_COLOR_BLUE, SPOT.CLEAR_COLOR_ALPHA);

        SPOT.shader_program = glCreateProgram();
        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs,
                "uniform mat4 viewProjMatrix;" +
                        "void main(void) {" +
                        " gl_Position = viewProjMatrix * gl_Vertex;" +
                        "}");
        glCompileShader(vs);
        glAttachShader(SPOT.shader_program, vs);
        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,
                "uniform vec3 color;" +
                        "void main(void) {" +
                        " gl_FragColor = vec4(color, 1.0);" +
                        "}");
        glCompileShader(fs);
        glAttachShader(SPOT.shader_program, fs);
        glLinkProgram(SPOT.shader_program);
        glUseProgram(SPOT.shader_program);

        SPOT.vpMatLocation = glGetUniformLocation(SPOT.shader_program, "viewProjMatrix");
        SPOT.renderColorLocation = glGetUniformLocation(SPOT.shader_program, "color");
    }
    void renderObjects() {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            for (int row = 0; row < SPOT.MAX_ROWS; row++) {
                for (int col = 0; col < SPOT.MAX_COLS; col++) {
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
                    glVertexPointer(SPOT.glVertexPointerSize, GL_FLOAT, SPOT.glVertexPointerStride, SPOT.glVertexPointerPoint);

                    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
                    glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL_STATIC_DRAW);

                    // Update color based on cell state
                    if (goLBoard.isCellAlive(row, col)) {
                        glUniform3f(SPOT.renderColorLocation, 0.0f, 1.0f, 0.0f); // Green for live cell
                    } else {
                        glUniform3f(SPOT.renderColorLocation, 1.0f, 0.0f, 0.0f); // Red for dead cell
                    }

                    slCamera my_cam = new slCamera();
                    my_cam.setProjectionOrtho(SPOT.ORTHO_LEFT, SPOT.ORTHO_RIGHT, SPOT.ORTHO_BOTTOM, SPOT.ORTHO_TOP, SPOT.ORTHO_NEAR, SPOT.ORTHO_FAR);
                    SPOT.viewProjMatrix = my_cam.getProjectionMatrix();
                    glUniformMatrix4fv(SPOT.vpMatLocation, false, SPOT.viewProjMatrix.get(SPOT.myFloatBuffer));

                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    glDrawElements(GL_TRIANGLES, SPOT.DRAW_COUNT , GL_UNSIGNED_INT, SPOT.DRAW_OFFSET);

                    glDeleteBuffers(vbo);
                    glDeleteBuffers(ibo);
                }
            }

            glfwSwapBuffers(SPOT.window.getWindowID());
    }
    private void saveStatusToFile() {
        String status = slGoLBoard.getStatus();

        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            if (!filePath.endsWith(".ca")) {
                filePath += ".ca";
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.println(status);
                JOptionPane.showMessageDialog(null, "Status saved to " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving status to " + filePath);
            }
        }
    }
    private void loadStatusFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a file to load status from");

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
                String line;
                int row = 0;
                int numRows = 0;
                while ((line = reader.readLine()) != null && row < numRows) {
                    int numCols = 0;
                    for (int col = 0; col < numCols && col < line.length(); col++) {
                        goLBoard.setCellState(row, col, line.charAt(col) == '1');
                    }
                    row++;
                }
                JOptionPane.showMessageDialog(null, "Status loaded successfully from: " + fileToLoad.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void printUsage() {
        System.out.println("Usage:");
        System.out.println("Space: Restart rendering");
        System.out.println("R: Randomize arrays and restart rendering");
        System.out.println("D: Toggle artificial frame rate delay");
        System.out.println("H: Halt rendering");
        System.out.println("F: Toggle frame rate display in console");
        System.out.println("S: Save status to file");
        System.out.println("L: Load status from file");
        System.out.println("?: Print usage instructions to console");
    }
}
