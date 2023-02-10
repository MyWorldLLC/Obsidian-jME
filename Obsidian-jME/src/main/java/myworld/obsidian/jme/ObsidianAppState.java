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
import com.jme3.renderer.ViewPort;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import myworld.obsidian.ObsidianUI;
import myworld.obsidian.display.Colors;
import myworld.obsidian.display.skin.obsidian.ObsidianSkin;
import myworld.obsidian.geometry.Dimension2D;

import java.util.function.Consumer;

public class ObsidianAppState extends BaseAppState {

    public static final AntiAliasing DEFAULT_UI_ANTIALIASING = AntiAliasing.FULL;

    protected final ObsidianUI ui;
    protected final AntiAliasing msaa;
    protected ObsidianContext ctx;
    protected JmeInputListener listener;
    protected Consumer<ObsidianUI> readyListener;
    protected FilterPostProcessor filters;

    protected final Texture2D sampleTex;
    protected final ObsidianCompositor compositor;

    public ObsidianAppState(){
        this(DEFAULT_UI_ANTIALIASING);
    }

    public ObsidianAppState(AntiAliasing msaa){
        ui = ObsidianUI.createHeadless();
        compositor = new ObsidianCompositor();
        sampleTex = new Texture2D();
        this.msaa = msaa;

        listener = new JmeInputListener();
        listener.setUI(ui);
    }

    public void setReadyListener(Consumer<ObsidianUI> readyListener) {
        this.readyListener = readyListener;
    }

    @Override
    protected void initialize(Application application) {
        filters = new FilterPostProcessor(application.getAssetManager());
        filters.addFilter(compositor);

        ctx = new ObsidianContext(application, ui);
        ctx.init(getExpectedDimensions(), msaa);
        updateSampleTexture();

        ui.clearColor().set(Colors.TRANSPARENT);

        ui.registerSkin(ObsidianSkin.create());
        ui.useSkin("Obsidian");

        if(readyListener != null){
            readyListener.accept(ui);
        }

    }

    public JmeInputListener getInputListener(){
        return listener;
    }

    public ObsidianUI getUI(){
        return ui;
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
        ctx.close();
    }

    @Override
    protected void onEnable() {
        getApplication().getInputManager().addRawInputListener(listener);
        getApplication().getViewPort().addProcessor(filters);
    }

    @Override
    protected void onDisable() {
        getApplication().getInputManager().removeRawInputListener(listener);
        getApplication().getViewPort().removeProcessor(filters);
    }
}
