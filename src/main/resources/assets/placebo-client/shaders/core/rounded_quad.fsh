#version 150

in vec2 FragCoord;

uniform vec2 RectPos;
uniform vec2 RectSize;
uniform float Radius;
uniform vec4 Color;

out vec4 fragColor;

float roundedBoxSDF(vec2 p, vec2 b, float r) {
    vec2 d = abs(p) - b + vec2(r);
    return min(max(d.x, d.y), 0.0) + length(max(d, 0.0)) - r;
}

void main() {
    vec2 halfSize = RectSize * 0.5;
    vec2 center = RectPos + halfSize;
    vec2 p = FragCoord - center;

    float dist = roundedBoxSDF(p, halfSize, Radius);

    // 1.0px smooth anti-aliased transition edge
    float alpha = 1.0 - smoothstep(0.0, 1.0, dist);

    if (alpha <= 0.0) {
        discard;
    }

    fragColor = vec4(Color.rgb, Color.a * alpha);
}