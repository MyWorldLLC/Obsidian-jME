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

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.Texture2D;

public class ObsidianCompositor extends Filter {

    protected Texture2D uiTexture;

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "myworld/obsidian/jme/ObsidianCompositor.j3md");

        if(uiTexture != null){
            setUITexture(uiTexture);
        }
    }

    public void setUITexture(Texture2D uiTexture){
        this.uiTexture = uiTexture;
        if(material != null){
            material.setTexture("UITexture", uiTexture);
        }
    }

    @Override
    protected Material getMaterial() {
        return material;
    }
}
