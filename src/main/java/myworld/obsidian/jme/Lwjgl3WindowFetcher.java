package myworld.obsidian.jme;

import com.jme3.app.Application;
import com.jme3.system.lwjgl.LwjglWindow;

public class Lwjgl3WindowFetcher {

    public static LwjglWindow getWindow(Application app){
        return ((LwjglWindow)app.getContext());
    }
}
