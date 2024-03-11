package SlRenderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static SlRenderer.SPOT.*;

public class slCamera {

    private void setCamera() {

    }

    public slCamera(Vector3f camera_position){

    }
    public slCamera(){

    }

    public void setProjectionOrtho(){

    }
    public void setProjectionOrtho(float left, float right, float bottom, float top, float near, float far) {
        f_left = left;
        f_right = right;
        f_bottom = bottom;
        f_top = top;
        f_near = near;
        f_far = far;

        SPOT.projectionMatrix = new Matrix4f();
        SPOT.projectionMatrix.setOrtho(f_left, f_right, f_bottom, f_top, f_near, f_far);

    }
    public Matrix4f getViewMatrix() {
        return SPOT.viewMatrix;
    }
    public Matrix4f getProjectionMatrix(){
        return SPOT.projectionMatrix;
    }




}
