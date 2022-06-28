package myworld.obsidian.jme;

import com.jme3.app.Application;
import myworld.obsidian.WindowInfo;
import org.lwjgl.glfw.GLFW;

public class Lwjgl3WindowInfo implements WindowInfo {

    protected final Application app;

    public Lwjgl3WindowInfo(Application app){
        this.app = app;
    }

    @Override
    public int getPosX() {
        var xPos = new int[1];
        GLFW.glfwGetWindowPos(Lwjgl3WindowFetcher.getWindow(app).getWindowHandle(), xPos, null);
        return xPos[0];
    }

    @Override
    public int getPosY() {
        var yPos = new int[1];
        GLFW.glfwGetWindowPos(Lwjgl3WindowFetcher.getWindow(app).getWindowHandle(), null, yPos);
        return yPos[0];
    }

    @Override
    public int getHeight() {
        var height = new int[1];
        GLFW.glfwGetWindowSize(Lwjgl3WindowFetcher.getWindow(app).getWindowHandle(), null, height);
        return height[0];
    }

    @Override
    public int getWidth() {
        var width = new int[1];
        GLFW.glfwGetWindowSize(Lwjgl3WindowFetcher.getWindow(app).getWindowHandle(), width, null);
        return width[0];
    }

    @Override
    public float getScaleX() {
        var xScale = new float[1];
        GLFW.glfwGetWindowContentScale(Lwjgl3WindowFetcher.getWindow(app).getWindowHandle(), xScale, null);
        return xScale[0];
    }

    @Override
    public float getScaleY() {
        var yScale = new float[1];
        GLFW.glfwGetWindowContentScale(Lwjgl3WindowFetcher.getWindow(app).getWindowHandle(), null, yScale);
        return yScale[0];
    }
}
