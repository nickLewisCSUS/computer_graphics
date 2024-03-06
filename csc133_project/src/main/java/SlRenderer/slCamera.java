package SlRenderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class slCamera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;

    private float f_left;
    private float f_right;
    private float f_bottom;
    private float f_top;
    private float f_near;
    private float f_far;



    private void setCamera() {

    }

    public slCamera(Vector3f camera_position){

    }
    public slCamera(){

    }

    public void setProjectionOrtho(){

    }
    public void setProjectionOrtho(float left, float right, float bottom, float top, float near, float far) {
        this.f_left = left;
        this.f_right = right;
        this.f_bottom = bottom;
        this.f_top = top;
        this.f_near = near;
        this.f_far = far;

        this.projectionMatrix = new Matrix4f();
        this.projectionMatrix.setOrtho(f_left, f_right, f_bottom, f_top, f_near, f_far);

    }
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }




}
