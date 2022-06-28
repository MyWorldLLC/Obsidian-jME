package myworld.obsidian.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.Texture2D;
import myworld.obsidian.swapchain.Swapchain;

public class UICompositor extends Filter {

    public static final int PIXEL_FORMAT_ARGB = 0;
    public static final int PIXEL_FORMAT_BGRA_PRE = 1;

    protected Swapchain.Format format = Swapchain.Format.ARGB8;

    protected Texture2D uiTexture;

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "myworld/client/gui/jfx/UICompositor.j3md");
        material.setInt("PixelFormatARGB", PIXEL_FORMAT_ARGB);
        material.setInt("PixelFormatBGRA_PRE", PIXEL_FORMAT_BGRA_PRE);
        if(format != null){
            setPixelFormat(format);
        }
        if(uiTexture != null){
            setUITexture(uiTexture);
        }
    }

    public void setPixelFormat(Swapchain.Format format){
        this.format = format;
        if(material != null){
            material.setInt("PixelFormat", switch (format){
                case ARGB8 -> PIXEL_FORMAT_ARGB;
                case BGRA8_PRE -> PIXEL_FORMAT_BGRA_PRE;
            });
        }
    }

    public void setUITexture(Texture2D uiTexture){
        this.uiTexture = uiTexture;
        if(material != null){
            material.setTexture("UITexture", uiTexture);
        }
    }

    public Swapchain.Format getFormat(){
        return format;
    }

    @Override
    protected Material getMaterial() {
        return material;
    }
}
