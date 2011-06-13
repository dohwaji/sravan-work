package es2.learning;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;

public class LightRenderer implements Renderer {
	
	int iPogAxisId;
	int iAxisPos;
	int iAxisColor;
	
	int iProgId;
	int iPosition;
	int iLightColor;
	int iLightDirection;
	int iVPMatrix;
	int iTexId;
	int iTexLoc;
	int iNormals;	
	int iVNormMat;
	int iTexCoords;
	
	public float xAngle = 0;
	public float yAngle = 0;
	
	float[] m_fProjMatrix = new float[16];
	float[] m_fViewMatrix = new float[16];
	float[] m_fIdentity = new float[16];
	float[] m_fVPMatrix = new float[16];
	
	float[] m_fLightDir = {0f, 0f, -3f};//light direction
	float[] m_fNormalMat = new float[16];//transposed projection matrix
	float[] m_fLightColor = {0f,0.2f,0.6f};//light color
	
	ES2SurfaceView curView;
	
	
	float[] lines = {
			0,0,0, 100,0,0, //+x-axis
			0,0,0, -100,0,0, //-x-axis
			0,0,0, 0,100,0,//+y
			0,0,0, 0,-100,0,//-y
			0,0,0, 0,0,100,
			0,0,0, 0,0,-100
		};
	FloatBuffer fb;
	float[] axiscolors = {
			1,0,0, 1,0,0,
			0,1,0, 0,1,0,
			0,0,1, 0,0,1,
			1,1,0, 1,1,0,
			1,0,1, 1,0,1,
			0,1,1, 0,1,1,			
		};
	FloatBuffer axisColorBuf;
	/*
	
	float[] vertices = {
	            // Front face
	            -2.0f, -2.0f,  2.0f,
	             2.0f, -2.0f,  2.0f,
	             2.0f,  2.0f,  2.0f,
	            -2.0f,  2.0f,  2.0f,
	 
	            // Back face
	            -2.0f, -2.0f, -2.0f,
	            -2.0f,  2.0f, -2.0f,
	             2.0f,  2.0f, -2.0f,
	             2.0f, -2.0f, -2.0f,
	 
	            // Top face
	            -2.0f,  2.0f, -2.0f,
	            -2.0f,  2.0f,  2.0f,
	             2.0f,  2.0f,  2.0f,
	             2.0f,  2.0f, -2.0f,
	 
	            // Bottom face
	            -2.0f, -2.0f, -2.0f,
	             2.0f, -2.0f, -2.0f,
	             2.0f, -2.0f,  2.0f,
	            -2.0f, -2.0f,  2.0f,
	 
	            // Right face
	             2.0f, -2.0f, -2.0f,
	             2.0f,  2.0f, -2.0f,
	             2.0f,  2.0f,  2.0f,
	             2.0f, -2.0f,  2.0f,
	 
	            // Left face
	            -2.0f, -2.0f, -2.0f,
	            -2.0f, -2.0f,  2.0f,
	            -2.0f,  2.0f,  2.0f,
	            -2.0f,  2.0f, -2.0f,
	};
	
	 float[] vertexNormals = {
	                  // Front face
	                   0.0f,  0.0f,  1.0f,
	                   0.0f,  0.0f,  1.0f,
	                   0.0f,  0.0f,  1.0f,
	                   0.0f,  0.0f,  1.0f,
	       
	                  // Back face
	                   0.0f,  0.0f, -1.0f,
	                   0.0f,  0.0f, -1.0f,
	                   0.0f,  0.0f, -1.0f,
	                   0.0f,  0.0f, -1.0f,
	       
	                  // Top face
	                   0.0f,  1.0f,  0.0f,
	                   0.0f,  1.0f,  0.0f,
	                   0.0f,  1.0f,  0.0f,
	                   0.0f,  1.0f,  0.0f,
	       
	                  // Bottom face
	                   0.0f, -1.0f,  0.0f,
	                   0.0f, -1.0f,  0.0f,
	                   0.0f, -1.0f,  0.0f,
	                   0.0f, -1.0f,  0.0f,
	       
	                  // Right face
	                   1.0f,  0.0f,  0.0f,
	                   1.0f,  0.0f,  0.0f,
	                   1.0f,  0.0f,  0.0f,
	                   1.0f,  0.0f,  0.0f,
	       
	                  // Left face
	                  -1.0f,  0.0f,  0.0f,
	                  -1.0f,  0.0f,  0.0f,
	                  -1.0f,  0.0f,  0.0f,
	                  -1.0f,  0.0f,  0.0f
};
	 float[] textureCoords = {
	                      // Front face
	                      0.0f, 0.0f,
	                      1.0f, 0.0f,
	                      1.0f, 1.0f,
	                      0.0f, 1.0f,
	           
	                      // Back face
	                      1.0f, 0.0f,
	                      1.0f, 1.0f,
	                      0.0f, 1.0f,
	                      0.0f, 0.0f,
	           
	                      // Top face
	                      0.0f, 1.0f,
	                      0.0f, 0.0f,
	                      1.0f, 0.0f,
	                      1.0f, 1.0f,
	           
	                      // Bottom face
	                      1.0f, 1.0f,
	                      0.0f, 1.0f,
	                      0.0f, 0.0f,
	                      1.0f, 0.0f,
	           
	                      // Right face
	                      1.0f, 0.0f,
	                      1.0f, 1.0f,
	                      0.0f, 1.0f,
	                      0.0f, 0.0f,
	           
	                      // Left face
	                      0.0f, 0.0f,
	                      1.0f, 0.0f,
	                      1.0f, 1.0f,
	                      0.0f, 1.0f
	 };
	 short[] cubeVertexIndices = {
	                              0, 1, 2,      0, 2, 3,    // Front face
	                              4, 5, 6,      4, 6, 7,    // Back face
	                              8, 9, 10,     8, 10, 11,  // Top face
	                              12, 13, 14,   12, 14, 15, // Bottom face
	                              16, 17, 18,   16, 18, 19, // Right face
	                              20, 21, 22,   20, 22, 23  // Left face
	 						};
	 
	 */
	 
		float[] textureCoords = {
				1,0, 0,0, 0,1, 1,1, 
				0,0, 0,1, 1,1, 1,0,
				1,1, 0,1, 0,0, 1,0,
				0,0, 1,0, 1,1, 0,1,
				0,1, 0,0, 1,0, 1,1,
				0,0, 1,0, 1,1, 0,1,
				
				};
	float[] vertices = {
			2,2,2, -2,2,2, -2,-2,2, 2,-2,2, //0-1-2-3 front
			2,2,2, 2,-2,2,  2,-2,-2, 2,2,-2,//0-3-4-5 right
			2,-2,-2, -2,-2,-2, -2,2,-2, 2,2,-2,//4-7-6-5 back
			-2,2,2, -2,2,-2, -2,-2,-2, -2,-2,2,//1-6-7-2 left
			2,2,2, 2,2,-2, -2,2,-2, -2,2,2, //top
			2,-2,2, -2,-2,2, -2,-2,-2, 2,-2,-2,//bottom
		};
		
		
		
		short[] cubeVertexIndices = {
				0,1,2, 0,2,3,
				4,5,6, 4,6,7,
				8,9,10, 8,10,11,
				12,13,14, 12,14,15,
				16,17,18, 16,18,19,
				20,21,22, 20,22,23,
				
				};
		
		
		 float[] vertexNormals = {
			          0, 0, 1,   0, 0, 1,   0, 0, 1,   0, 0, 1,     //front
			           1, 0, 0,   1, 0, 0,   1, 0, 0,   1, 0, 0,     // right
			           0, 0,-1,   0, 0,-1,   0, 0,-1,   0, 0,-1,     //back
			           -1, 0, 0,  -1, 0, 0,  -1, 0, 0,  -1, 0, 0,     // left
			           0, 1, 0,   0, 1, 0,   0, 1, 0,   0, 1, 0,     //  top		          
			           0,-1, 0,   0,-1, 0,   0,-1, 0,   0,-1, 0,     // bottom
			           
		 };	
		FloatBuffer cubeBuffer = null;
		FloatBuffer normalsBuffer = null;
		ShortBuffer indexBuffer = null;
		FloatBuffer texBuffer = null;
	
	public LightRenderer(ES2SurfaceView view) {
		curView = view;
		cubeBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		cubeBuffer.put(vertices).position(0);
		
		normalsBuffer = ByteBuffer.allocateDirect(vertexNormals.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		normalsBuffer.put(vertexNormals).position(0);
		
		indexBuffer = ByteBuffer.allocateDirect(cubeVertexIndices.length * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
		indexBuffer.put(cubeVertexIndices).position(0);
		
		texBuffer = ByteBuffer.allocateDirect(textureCoords.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		texBuffer.put(textureCoords).position(0);
		
		
		fb = ByteBuffer.allocateDirect(lines.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		fb.put(lines).position(0);
		
		axisColorBuf = ByteBuffer.allocateDirect(axiscolors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		axisColorBuf.put(axiscolors).position(0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		cubeBuffer.position(0);
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, cubeBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);
		
		texBuffer.position(0);
//		GLES20.glUniform2fv(iTexCoords, 1, texBuffer);
		GLES20.glVertexAttribPointer(iTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
		GLES20.glEnableVertexAttribArray(iTexCoords);
		
		normalsBuffer.position(0);
		GLES20.glVertexAttribPointer(iNormals, 3, GLES20.GL_FLOAT, false, 0, normalsBuffer);
		GLES20.glEnableVertexAttribArray(iNormals);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTexId);
		GLES20.glUniform1i(iTexLoc, 0);
		
		GLES20.glUniform3fv(iLightColor, 1, m_fLightColor, 0);
		GLES20.glUniform3fv(iLightDirection, 1, m_fLightDir, 0);
		
		
		Matrix.setIdentityM(m_fIdentity, 0);
		Matrix.rotateM(m_fIdentity, 0, -xAngle, 0, 1, 0);
		Matrix.rotateM(m_fIdentity, 0, -yAngle, 1, 0, 0);
		Matrix.multiplyMM(m_fVPMatrix, 0, m_fViewMatrix, 0, m_fIdentity, 0);
		Matrix.multiplyMM(m_fVPMatrix, 0, m_fProjMatrix, 0, m_fVPMatrix, 0);
		
		Matrix.invertM(m_fNormalMat, 0, m_fVPMatrix, 0);
		Matrix.transposeM(m_fNormalMat, 0, m_fNormalMat, 0);
		GLES20.glUniformMatrix4fv(iVNormMat, 1, false, m_fNormalMat, 0);

		GLES20.glUniformMatrix4fv(iVPMatrix, 1, false, m_fVPMatrix, 0);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

//		DrawAxis();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
//		Matrix.frustumM(m_fProjMatrix, 0, -4, 4, -4, 4, 1, 20);
		Matrix.orthoM(m_fProjMatrix, 0, -10, 10, -10, 10, 1, 20);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0, 0, 0, 1);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glFrontFace(GLES20.GL_CCW);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);
		
		Matrix.setLookAtM(m_fViewMatrix, 0, 0, 0, 5, 0, 0, 0, 0, 1, 0);
		
		String strVShader = "attribute vec4 a_position;" +
				"attribute vec3 a_normals;" +
				"attribute vec2 a_texCoords;" +
				"uniform mat4 u_ModelViewMatrix;" +
				"uniform mat4 u_MVNormalsMatrix;" +
				"varying vec3 u_Normals;" +
				"varying vec2 v_texCoords;" +
				"void main()" +
				"{" +
					"v_texCoords = a_texCoords;" +
					"u_Normals = normalize(mat3(u_ModelViewMatrix) * a_normals);" + //works now, but can cause problem when zoom or scale
//					"u_Normals = normalize(mat3(u_MVNormalsMatrix) * a_normals);" +
					"gl_Position = u_ModelViewMatrix * a_position;" +
				"}";
		String strFShader = "precision mediump float;" +
				"uniform vec3 u_LightDir;" +
				"uniform vec3 u_LightColor;" +				
				"uniform sampler2D u_texId;" +
				"varying vec2 v_texCoords;" +
				"varying vec3 u_Normals;" +
				"void main()" +
				"{" +
					"vec3 LNorm = normalize(u_LightDir);" +
					"vec3 normal = normalize(u_Normals);" +
					"float intensity = max(dot(LNorm, normal),0.0);" +
					"vec4 texColor = texture2D(u_texId, v_texCoords);" +
					"vec3 calcColor = vec3(0.2,0.2,0.2) + u_LightColor * intensity;" +
					"gl_FragColor = vec4(texColor.rgb * calcColor, texColor.a);" +
				"}";
		/*String strVShader =
				"attribute vec4 a_position;" +
				"attribute vec3 a_normals;" +
				"uniform mat4 u_VPMatrix;" +
				"uniform mat4 u_VNormalMat;" +
				"uniform vec3 u_LightDir;" +
				"uniform vec3 u_LightColor;" +
				"attribute vec2 a_texCoords;" +
				"varying vec2 v_texCoords;" +
				"varying vec3 v_calcColor;" +//calculated final light color
				"void main()" +
				"{" +
					"v_texCoords = a_texCoords;" +
					"gl_Position = u_VPMatrix * a_position;" +
//					"vec3 transNorms = mat3(u_VNormalMat) * vec4(a_normals,0.0).xyz;" +
					"vec3 transNorms = vec3(u_VNormalMat * vec4(a_normals,0.0));" +
					"vec3 normLightDir = normalize(u_LightDir);" +
					"float dirLigthweight = max(dot(transNorms,normLightDir),0.0);" +
					"v_calcColor = vec3(0.2,0.2,0.2)+u_LightColor * dirLigthweight;" +
				"}";*/
		
		/*String strFShader = 
				"precision mediump float;" +
				"uniform sampler2D u_texId;" +
				"varying vec2 v_texCoords;" +
				"varying vec3 v_calcColor;" +
				"void main()" +
				"{" +
					"vec4 texColor = texture2D(u_texId, v_texCoords);" +
					"gl_FragColor = vec4(texColor.rgb*v_calcColor,texColor.a);" +
				"}";*/
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		iNormals = GLES20.glGetAttribLocation(iProgId, "a_normals");
		iVPMatrix = GLES20.glGetUniformLocation(iProgId, "u_ModelViewMatrix");
		iVNormMat = GLES20.glGetUniformLocation(iProgId, "u_MVNormalsMatrix");
		iLightColor = GLES20.glGetUniformLocation(iProgId, "u_LightColor");
		iLightDirection = GLES20.glGetUniformLocation(iProgId, "u_LightDir");
		iTexLoc = GLES20.glGetUniformLocation(iProgId, "u_texId");
		iTexCoords = GLES20.glGetAttribLocation(iProgId, "a_texCoords");
		iTexId = Utils.LoadTexture(curView, R.raw.crate);
		
//		LoadAxisShaders();
	}
	
	public void LoadAxisShaders()
	{
		String strVShader =
			"attribute vec4 a_position;" +
			"attribute vec4 a_color;" +
			"varying vec4 v_color;" +
			"void main()" +
			"{" +
				"v_color = a_color;" +
				"gl_PointSize = 5.0;" +
				"gl_Position = a_position;" +
			"}";	
		String strFShader = "precision mediump float;" +
			"varying vec4 v_color;" +
			"void main()" +
			"{" +
				"gl_FragColor = v_color;" +
			"}";
		iPogAxisId = Utils.LoadProgram(strVShader, strFShader);
		iAxisColor= GLES20.glGetAttribLocation(iPogAxisId, "a_color");
		iAxisPos = GLES20.glGetAttribLocation(iPogAxisId, "a_position");
	}
	
	public void DrawAxis()
	{
		GLES20.glUseProgram(iPogAxisId);
		GLES20.glVertexAttribPointer(iAxisPos, 3, GLES20.GL_FLOAT, false, 0, fb);
		GLES20.glEnableVertexAttribArray(iAxisPos);
		
		GLES20.glVertexAttribPointer(iAxisColor, 3, GLES20.GL_FLOAT, false, 0, axisColorBuf);
		GLES20.glEnableVertexAttribArray(iAxisColor);
		
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, 12);
		
		GLES20.glUseProgram(0);
		
	}
	
}
