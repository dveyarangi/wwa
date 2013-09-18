uniform sampler2D sceneTex; // 0
uniform vec4 min; // minimal included value
uniform vec4 max; // maximal included value
uniform vec4 target; // maximal included value
uniform vec4 overflow; // maximal included value
uniform vec4 underflow; // maximal included value
void main()
{
	vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;

	vec4 res;
	float offset = 0.3;
	for(int i = 0; i < 4; i ++) {
		if(tc[i] < min[i]) { res[i] = underflow[i]; }
		else 
		if(tc[i] > max[i]) { res[i] = overflow[i]; } 
		else {
			res[i] = target[i]; }
	}

	gl_FragColor=res;

}