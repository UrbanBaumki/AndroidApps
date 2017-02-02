
precision mediump float;
varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_sampler2D;
uniform float u_percent;



void main(){

    vec4 texColor = texture2D(u_sampler2D, v_texCoords);


    gl_FragColor = vec4(texColor.r, texColor.g, texColor.b, texColor.a * u_percent);
}