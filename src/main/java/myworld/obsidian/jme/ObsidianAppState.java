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
import myworld.obsidian.ObsidianUI;
import myworld.obsidian.display.skin.chipmunk.ChipmunkSkinLoader;

public class ObsidianAppState extends BaseAppState {

    protected ObsidianUI ui;
    protected JmeInputListener listener;

    @Override
    protected void initialize(Application application) {
        var framebuffer = application.getViewPort().getOutputFrameBuffer();
        ui = ObsidianUI.createForGL(
                framebuffer.getWidth(),
                framebuffer.getHeight(),
                framebuffer.getId());
        try {
            ui.registerSkin(ChipmunkSkinLoader.loadFromClasspath(ChipmunkSkinLoader.DEFAULT_SKIN));
            ui.useSkin("Obsidian");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        listener = new JmeInputListener(ui);
    }

    @Override
    protected void cleanup(Application application) {
        ui.cleanup();
    }

    @Override
    protected void onEnable() {
        getApplication().getInputManager().addRawInputListener(listener);
    }

    @Override
    protected void onDisable() {
        getApplication().getInputManager().removeRawInputListener(listener);
    }
}
