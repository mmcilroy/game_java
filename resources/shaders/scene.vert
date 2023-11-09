#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 color;
layout (location=2) in vec3 normal;

out vec3 outColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform vec3 lightDirection;
uniform vec3 lightColour;
uniform vec2 lightBias;

vec3 calculateLighting()
{
    float brightness = max(dot(-lightDirection, normal), 0.0);
    return (lightColour * lightBias.x) + (brightness * lightColour * lightBias.y);
}

void main()
{
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);

    outColor = color;

    vec3 lighting = calculateLighting();
    outColor = color * lighting;
}
