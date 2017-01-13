#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 vTexCoord;

uniform sampler2D u_sampler2D;

const float PERCENT = 0.3;

void main(){

    vec4 texColor = texture2D(u_sampler2D, vTexCoord);

    float gray = dot(texColor.rgb, vec3(.2909, 0.587, 0.114));
    //float gray = dot(texColor.rgb, vec3(PERCENT , PERCENT, PERCENT ));

    gl_FragColor = mix(texColor.rgba, vec4(gray, gray, gray, texColor.a), 1);

}