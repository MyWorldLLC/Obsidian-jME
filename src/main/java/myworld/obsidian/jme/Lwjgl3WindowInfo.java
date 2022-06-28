/*
 *    Copyright 2022 MyWorld, LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
