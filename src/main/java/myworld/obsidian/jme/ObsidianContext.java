package myworld.obsidian.jme;

import com.jme3.app.Application;
import com.jme3.system.lwjgl.LwjglContext;
import com.jme3.system.lwjgl.LwjglWindow;
import myworld.obsidian.geometry.Dimension2D;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL32;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

/**
 * Manages a dedicated OpenGL context & rendering target for Obsidian
 */
public class ObsidianContext {

    protected final Application app;
    protected Dimension2D size;
    protected AntiAliasing msaa;

    protected long osr;

    public ObsidianContext(Application app){
        this.app = app;
    }

    public void init(Dimension2D size, AntiAliasing msaa){

        this.msaa = msaa;
        this.size = size;

        var window = getWindow();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_STENCIL_BITS, 0);
        glfwWindowHint(GLFW_DEPTH_BITS, 0);

        // This is crucial - without this, antialiasing will be disabled on the framebuffer
        // and any Skia operations involving clipping will result in major flickering and image
        // corruption across the entire drawing canvas.
        glfwWindowHint(GLFW_SAMPLES, msaa.getSamples());

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        // Share OpenGL resources with the application window
        // We're not going to use this window's framebuffer, so make it small
        osr = glfwCreateWindow(1, 1, "Obsidian OSR", 0, window.getWindowHandle());
        createRenderSurface();
    }

    public void resize(Dimension2D size){
        this.size = size;
        createRenderSurface();
    }

    public void close(){
        glfwDestroyWindow(osr);
    }

    protected void createRenderSurface(){
        // Clean up any previously created OpenGL resources & re-create them.
    }

    protected LwjglWindow getWindow(){
        return (LwjglWindow) app.getContext();
    }

}
