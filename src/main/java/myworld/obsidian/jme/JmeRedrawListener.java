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
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;
import myworld.obsidian.ObsidianPixels;
import myworld.obsidian.jfx.RedrawListener;

import java.nio.ByteOrder;

public class JmeRedrawListener implements RedrawListener {

    protected Texture2D uiTexture;
    protected final Application app;
    protected final UICompositor compositor;

    public JmeRedrawListener(Application app, UICompositor compositor){
        this.app = app;
        this.compositor = compositor;
    }

    public Application getApp(){
        return app;
    }

    public UICompositor getCompositor(){
        return compositor;
    }

    @Override
    public void onRedraw(ObsidianPixels pixels, Runnable onComplete) {
        app.enqueue(() -> {
            ensureTexture(pixels);
            var buffer = uiTexture.getImage().getData(0);
            buffer.put(0, pixels.rawBuffer(), 0, pixels.rawBuffer().capacity());
            uiTexture.getImage().setData(0, buffer);
            onComplete.run();
        });
    }

    protected void ensureTexture(ObsidianPixels pixels){
        if(uiTexture != null &&
                uiTexture.getImage().getHeight() == pixels.getVPixels() &&
                uiTexture.getImage().getWidth() == pixels.getWPixels()){
            return;
        }

        uiTexture = new Texture2D(pixels.getWPixels(), pixels.getVPixels(), getFormat());
        uiTexture.setMinFilter(Texture.MinFilter.Trilinear);
        uiTexture.setMagFilter(Texture.MagFilter.Bilinear);
        uiTexture.setAnisotropicFilter(0);
        uiTexture.setWrap(Texture.WrapMode.EdgeClamp);
        uiTexture.getImage().setData(BufferUtils.createByteBuffer(pixels.getWPixels() * pixels.getVPixels() * 4));
        uiTexture.getImage().setColorSpace(ColorSpace.sRGB);
        compositor.setUITexture(uiTexture);
    }

    protected Image.Format getFormat(){
        return ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)
                ? Image.Format.RGBA8 : Image.Format.ABGR8;
    }
}
