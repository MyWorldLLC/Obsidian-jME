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
import com.jme3.app.state.BaseAppState;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import myworld.obsidian.ObsidianUI;
import myworld.obsidian.display.ColorRGBA;
import myworld.obsidian.display.Colors;
import myworld.obsidian.display.DisplayEngine;
import myworld.obsidian.display.skin.chipmunk.ChipmunkSkinLoader;
import myworld.obsidian.geometry.Dimension2D;

import java.util.function.BiConsumer;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

public class ObsidianAppState extends BaseAppState {

    public static final int DEFAULT_UI_SAMPLES = 4;

    protected ObsidianUI ui;
    protected JmeInputListener listener;
    protected BiConsumer<ObsidianUI, ObsidianUI> readyListener;

    protected FilterPostProcessor filters;

    protected FrameBuffer renderBuffer;
    protected FrameBuffer uiFrameBuffer;
    protected Texture2D uiTex;
    protected final ObsidianCompositor compositor = new ObsidianCompositor();

    public BiConsumer<ObsidianUI, ObsidianUI> getReadyListener() {
        return readyListener;
    }

    public void setReadyListener(BiConsumer<ObsidianUI, ObsidianUI> readyListener) {
        this.readyListener = readyListener;
    }

    @Override
    protected void initialize(Application application) {
        listener = new JmeInputListener();

        filters = new FilterPostProcessor(application.getAssetManager());
        filters.addFilter(compositor);
        createSurface();
    }

    @Override
    public void update(float tpf){
        if(ui != null){
            ui.update(tpf);
        }
    }

    @Override
    public void render(RenderManager rm){
        if(ui != null) {

            if (needsResize()) {
                createSurface();
            }

            ui.render();

            // This is required to resolve MSAA and sample from the texture for compositing. It should
            // not be necessary in modern OpenGL versions, but for some reason it seems to be required
            // regardless of jME renderer version.
            rm.getRenderer().copyFrameBuffer(renderBuffer, uiFrameBuffer, true, false);

        }
    }

    protected void createSurface(){
        var dim = getExpectedDimensions();

        int width = (int) dim.width();
        int height = (int) dim.height();

        uiFrameBuffer = new FrameBuffer(width, height, 1);
        uiTex = new Texture2D(width, height, 1, Image.Format.RGBA8);
        uiFrameBuffer.addColorTarget(FrameBuffer.FrameBufferTarget.newTarget(uiTex));
        compositor.setUITexture(uiTex);

        renderBuffer = new FrameBuffer(width, height, DEFAULT_UI_SAMPLES);
        renderBuffer.addColorTarget(FrameBuffer.FrameBufferTarget.newTarget(Image.Format.RGBA8));


        var renderer = getApplication().getRenderer();
        // Initialize the framebuffer - without this, the handle will
        // not be set when the framebuffer is passed to Obsidian
        renderer.setFrameBuffer(uiFrameBuffer);
        renderer.setFrameBuffer(renderBuffer);

        if(ui == null){
            // This will run once when the UI is first initialized
            var oldUI = ui;

            ui = ObsidianUI.createForGL(width, height, DEFAULT_UI_SAMPLES, renderBuffer.getId());
            ui.clearColor().set(Colors.TRANSPARENT);
            try {
                ui.registerSkin(ChipmunkSkinLoader.loadFromClasspath(ChipmunkSkinLoader.DEFAULT_SKIN));
                ui.useSkin("Obsidian");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            listener.setUi(ui);

            if(readyListener != null){
                readyListener.accept(oldUI, ui);
            }
        }else{
            ui.display().ifSet(DisplayEngine::close);
            ui.setDisplay(DisplayEngine.createForGL((int)dim.width(), (int)dim.height(), DEFAULT_UI_SAMPLES, renderBuffer.getId()));
        }
    }

    protected Dimension2D getExpectedDimensions(){
        var cam = getApplication().getGuiViewPort().getCamera();
        return new Dimension2D(cam.getWidth(), cam.getHeight());
    }

    protected Dimension2D getActualDimensions(){
        return ui != null ? ui.getDisplay().getDimensions().get() : null;
    }

    protected boolean needsResize(){
        var actual = getActualDimensions();
        var expected = getExpectedDimensions();
        return actual.width() != expected.width() || actual.height() != expected.height();
    }

    @Override
    protected void cleanup(Application application) {
        if(ui != null){
            ui.cleanup();
            uiFrameBuffer = null;
            uiTex = null;
        }
    }

    @Override
    protected void onEnable() {
        getApplication().getInputManager().addRawInputListener(listener);
        getApplication().getGuiViewPort().addProcessor(filters);
    }

    @Override
    protected void onDisable() {
        getApplication().getInputManager().removeRawInputListener(listener);
        getApplication().getGuiViewPort().removeProcessor(filters);
    }
}
