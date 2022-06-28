package myworld.obsidian.jme;

import com.jme3.app.Application;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;
import myworld.obsidian.ObsidianPixels;
import myworld.obsidian.RedrawListener;

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
