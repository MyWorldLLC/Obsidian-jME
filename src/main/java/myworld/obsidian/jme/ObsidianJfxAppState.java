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
import myworld.obsidian.jfx.ObsidianJfxUI;

public class ObsidianJfxAppState extends BaseAppState {

    protected final ObsidianJfxUI ui;
    protected final JmeJfxInputListener listener;

    public ObsidianJfxAppState(ObsidianJfxUI ui){
        this.ui = ui;
        listener = new JmeJfxInputListener(ui);
    }

    @Override
    protected void initialize(Application app) {
    }

    @Override
    protected void cleanup(Application app) {

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
