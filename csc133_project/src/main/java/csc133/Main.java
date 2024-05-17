package csc133;
import SlRenderer.slSingleBatchRenderer;
/**
 * The Main class serves as the entry point for the application,
 * initializing and executing the single batch renderer.
 */
public class Main {
    public static void main(String[] args) {
        slSingleBatchRenderer myRenderer = new slSingleBatchRenderer();
        myRenderer.render();
    }
}
