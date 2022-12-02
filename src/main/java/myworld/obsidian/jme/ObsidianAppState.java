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
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import myworld.obsidian.ObsidianUI;
import myworld.obsidian.display.Colors;
import myworld.obsidian.display.skin.chipmunk.ChipmunkSkinLoader;
import myworld.obsidian.geometry.Dimension2D;

import java.util.function.Consumer;

public class ObsidianAppState extends BaseAppState {

    public static final int DEFAULT_UI_SAMPLES = 4;

    protected final ObsidianUI ui;
    protected ObsidianContext ctx;
    protected JmeInputListener listener;
    protected Consumer<ObsidianUI> readyListener;

    protected FilterPostProcessor filters;

    protected final Texture2D sampleTex;
    protected final ObsidianCompositor compositor;

    public ObsidianAppState(){
        ui = ObsidianUI.createHeadless();
        compositor = new ObsidianCompositor();
        sampleTex = new Texture2D();
    }

    public void setReadyListener(Consumer<ObsidianUI> readyListener) {
        this.readyListener = readyListener;
    }

    @Override
    protected void initialize(Application application) {
        listener = new JmeInputListener();
        listener.setUI(ui);

        filters = new FilterPostProcessor(application.getAssetManager());
        filters.addFilter(compositor);

        ctx = new ObsidianContext(application, ui);
        ctx.init(getExpectedDimensions(), AntiAliasing.STANDARD); // TODO - configurable AA
        updateSampleTexture();

        ui.clearColor().set(Colors.TRANSPARENT);
        try {
            ui.registerSkin(ChipmunkSkinLoader.loadFromClasspath(ChipmunkSkinLoader.DEFAULT_SKIN));
            ui.useSkin("Obsidian");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(readyListener != null){
            readyListener.accept(ui);
        }

    }

    @Override
    public void update(float tpf){
        ui.update(tpf);
    }

    @Override
    public void render(RenderManager rm){

        if(needsResize()){
            ctx.resize(getExpectedDimensions());
            updateSampleTexture();
        }

        ctx.render();

        /*if(ui != null) {

            if (needsResize()) {
                createSurface();
            }

            var renderer = rm.getRenderer();
            renderer.setFrameBuffer(renderBuffer);
            renderer.clearBuffers(true, true, true);
            ui.render();
            glFinish();

            // This is required to resolve MSAA and sample from the texture for compositing. It should
            // not be necessary in modern OpenGL versions, but for some reason it seems to be required
            // regardless of jME renderer version.
            renderer.setFrameBuffer(uiFrameBuffer);
            renderer.copyFrameBuffer(renderBuffer, uiFrameBuffer, true, false);

        }*/
    }

    protected void updateSampleTexture(){
        var size = getExpectedDimensions();
        var image = new Image();
        image.setId(ctx.getTextureHandle());
        image.setFormat(Image.Format.RGBA8);
        image.setWidth((int)size.width());
        image.setHeight((int)size.height());
        image.setColorSpace(ColorSpace.sRGB);

        // Need to set this as soon as we create it so jME doesn't
        // try to overwrite the OpenGL state
        image.clearUpdateNeeded();

        sampleTex.setImage(image);
        compositor.setUITexture(sampleTex);
    }

/*    protected void createSurface(){
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
        // Initialize the framebuffers - without this, the handle will
        // not be set when the rendering framebuffer is passed to Obsidian
        renderer.setFrameBuffer(uiFrameBuffer);
        renderer.setFrameBuffer(renderBuffer);

        // This will run once when the UI is first initialized
        if(ui == null){
            var oldUI = ui;

            //ui = ObsidianUI.createHeadless();
            //ui = ObsidianUI.createForGL(width, height, DEFAULT_UI_SAMPLES, renderBuffer.getId());
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
    }*/

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
        ui.cleanup();
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
