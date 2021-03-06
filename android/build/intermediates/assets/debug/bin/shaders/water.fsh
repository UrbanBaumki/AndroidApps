precision mediump float;
varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_texture_perlin;
uniform float u_deltatime;
uniform float u_playerposition;
uniform float u_speed;

void main(){

    float offsetX = mod(v_texCoords.x + u_playerposition * u_speed, 1.0);

    vec2 displace = texture2D( u_texture_perlin,  vec2(offsetX, v_texCoords.y) ).xy;

    //vec2 displace = texture2D( u_texture_perlin,   v_texCoords ).xy;
    float t = ( v_texCoords.y + displace.y *0.085  - 0.15 )+ (sin(v_texCoords.x  * 60.0 + u_deltatime) * 0.005);


    gl_FragColor = v_color * texture2D( u_texture, vec2(v_texCoords.x, t));
}