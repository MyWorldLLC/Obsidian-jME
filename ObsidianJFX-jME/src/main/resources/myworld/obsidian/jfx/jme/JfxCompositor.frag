#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"

uniform COLORTEXTURE m_Texture;
uniform COLORTEXTURE m_UITexture;
uniform DEPTHTEXTURE m_DepthTexture;
uniform int m_PixelFormat;
varying vec2 texCoord;

vec4 composite(vec4 a, vec4 b){
    return a + b * (1.0 - a.a);
}

vec2 invert(vec2 texCoord){
    return vec2(texCoord.x, 1.0 - texCoord.y);
}

void main() {

    // Our UI Texture will always be presented to this shader with the format
    // RGBA. However, our UI texture will actually have a different color-channel
    // mapping, so we have to swap the channels around to actually be RGBA.

    vec4 uiSample = getColor(m_UITexture, invert(texCoord));
    vec4 filterSample = getColor(m_Texture, texCoord);
    if(m_PixelFormat == FORMAT_ARGB){
        // a=r, r=g, g=b, b=a
        // Swizzle and premultiply so that we can do all compositing as premultiplied alpha
        uiSample = vec4(uiSample.gba * uiSample.r, uiSample.r);
    }else if(m_PixelFormat == FORMAT_BGRA_PRE){
        // b=r, g=g, r=b, a=a
        uiSample = uiSample.bgra;
    }else{
        uiSample = vec4(0.0);
    }

    gl_FragColor = composite(uiSample, filterSample);
}