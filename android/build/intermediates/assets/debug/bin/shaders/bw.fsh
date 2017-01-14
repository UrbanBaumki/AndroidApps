
precision mediump float;
varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_sampler2D;

const float PERCENT = 0.3;

void main(){

    vec4 texColor = texture2D(u_sampler2D, v_texCoords);

    float gray = dot(texColor.rgb, vec3(.2909, 0.587, 0.114));

    //gl_FragColor = mix(texColor.rgba, vec4(gray, gray, gray, texColor.a), 1);
    gl_FragColor = vec4(gray, gray, gray, texColor.a);
}