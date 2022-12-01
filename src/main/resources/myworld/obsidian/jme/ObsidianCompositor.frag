#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"

uniform COLORTEXTURE m_Texture;
uniform COLORTEXTURE m_UITexture;
uniform DEPTHTEXTURE m_DepthTexture;
varying vec2 texCoord;

vec4 composite(vec4 a, vec4 b){
    return a + b * (1.0 - a.a);
}

vec2 invert(vec2 texCoord){
    return vec2(texCoord.x, 1.0 - texCoord.y);
}

void main() {

    vec4 uiSample = getColor(m_UITexture, invert(texCoord));
    vec4 filterSample = getColor(m_Texture, texCoord);

    gl_FragColor = composite(uiSample, filterSample);
}