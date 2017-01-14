precision mediump float;
varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_sampler2D;


void main(){


    gl_FragColor = texture2D( u_sampler2D, v_texCoords) * v_color;
}