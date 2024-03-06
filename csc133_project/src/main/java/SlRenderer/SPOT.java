package SlRenderer;

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

    // Viewport coordinates
    public static final float squareSize = 5f;
    public static final float padding = 2f;
    public static final int MAX_ROWS = 20, MAX_COLS = 18;
    public static final float halfSquareSize = SPOT.squareSize / 2f;
    public static final float yOffset = 185f;
    public static final float xOffset = SPOT.squareSize + padding;

    public static final float xMin = -halfSquareSize + xOffset;
    public static final float xMax = halfSquareSize + xOffset;
    public static final float yMin = -halfSquareSize + yOffset;
    public static final float yMax = halfSquareSize + yOffset;

    public static final int OGL_MATRIX_SIZE = 16;

    // Orthographic projection parameters
    public static final float ORTHO_LEFT = 0f;
    public static final float ORTHO_RIGHT = 200f;
    public static final float ORTHO_BOTTOM = 0f;
    public static final float ORTHO_TOP = 200f;
    public static final float ORTHO_NEAR = 0f;
    public static final float ORTHO_FAR = 10f;

    // Color parameters
    public static final float COLOR_RED = 1.0f;
    public static final float COLOR_GREEN = 0.498f;
    public static final float COLOR_BLUE = 0.153f;
}
