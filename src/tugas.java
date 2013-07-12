/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author KOLOT
 */
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.Sphere;

import utility.EulerCamera;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.FloatBuffer;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
 
public class tugas {
    private static final int[] WINDOW_DIMENSIONS = {800, 600};
    
    private static final float ASPECT_RATIO = (float) WINDOW_DIMENSIONS[0] / (float) WINDOW_DIMENSIONS[1];
    private static final EulerCamera camera = new EulerCamera.Builder().setPosition(2.5f, 0.0f, //edit 2
            8.5f).setRotation(0, 5, 0).setAspectRatio(ASPECT_RATIO).setFieldOfView(60).build(); //edit 2
    private float rtri;
    private long lastTime;
    private int fps;
    private static boolean flatten = false;
    private static int shaderProgram;
    private static Texture wall;
    private static Texture wall2;
    private static Texture moon;
    private static Texture sand;
    private static int sphereId;
    
    //----------- Variables added for Lighting Test -----------//
    private FloatBuffer matSpecular;
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight; 
    private FloatBuffer lModelAmbient;
    //----------- END: Variables added for Lighting Test -----------//
           
    public static void main(String[] args) {
        tugas main = null;
        try {
            main = new tugas();
            main.create();
            main.run();
        }catch(Exception e){}
        
        if(main != null) {
            main.destroy();
        }
    }
    
    public tugas() {
    
    }
    
    public void create() throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(WINDOW_DIMENSIONS[0],WINDOW_DIMENSIONS[1]));
        Display.setTitle("Tugas Besar");
        Display.setFullscreen(false);
        Display.create();
        
        initGL();
        resizeGL();
        
        try {
            // Load the wood texture from "C:/res/moon.png"
            wall = TextureLoader.getTexture("PNG", new FileInputStream(new File("C:/res/pyramid5.png")));
            wall2 = TextureLoader.getTexture("PNG", new FileInputStream(new File("C:/res/pyramid4.png")));
            moon = TextureLoader.getTexture("PNG", new FileInputStream(new File("C:/res/moon.png")));
            sand = TextureLoader.getTexture("PNG", new FileInputStream(new File("C:/res/sand3.png")));
        } catch (IOException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
    }
    
    public void destroy() {
        Display.destroy();
    }
    
    public void initGL() {
        glShadeModel(GL_SMOOTH);               // Enables Smooth Shading
        glClearColor(0, 0.03f, 0, 0);           // Black Background
        glClearDepth(1.0f);                     // Depth Buffer Setup 
        glEnable(GL_DEPTH_TEST);                // Enables Depth Testing 
        glDepthFunc(GL_LEQUAL);                 // The Type Of Depth Test To Do
        
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);          // Really Nice Perspective Calculations
        
        lastTime = Sys.getTime();
        
        initLightArrays();
	glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
	glMaterialf(GL_FRONT, GL_SHININESS, 10.0f);					// sets shininess
		
	glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
	glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);				// sets specular light to white
	glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);					// sets diffuse light to white
	glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light 
		
	glEnable(GL_LIGHTING);										// enables lighting
	glEnable(GL_LIGHT0);										// enables light0
		
	//glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
	glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
	//----------- END: Variables & method calls added for Lighting Test -----------//
                
    }
       
        //------- Added for Lighting Test----------//
	private void initLightArrays() {
	matSpecular = BufferUtils.createFloatBuffer(4);
	matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
	lightPosition = BufferUtils.createFloatBuffer(4);
	lightPosition.put(1.0f).put(1.0f).put(1.0f).put(8.0f).flip();
		
	whiteLight = BufferUtils.createFloatBuffer(4);
	whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
	
	lModelAmbient = BufferUtils.createFloatBuffer(4);
	lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
    }
           
    public void resizeGL() {
        glViewport(0,0,WINDOW_DIMENSIONS[0],WINDOW_DIMENSIONS[1]);
        
        glMatrixMode(GL_PROJECTION);    // Select The Projection Matrix 
        glLoadIdentity();               // Reset The Projection Matrix 

        // Calculate The Aspect Ratio Of The Window 
        gluPerspective(45.0f,(float)WINDOW_DIMENSIONS[0]/(float)WINDOW_DIMENSIONS[1],0.1f,50.0f); 
        
        glMatrixMode(GL_MODELVIEW);     // Select The Modelview Matrix 
        glLoadIdentity();               // Reset The Modelview Matrix            
    }
    
    public void render() {
        glEnable(GL_TEXTURE_2D);
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        camera.applyTranslations();
        
        glTranslated(4.0f,7.0f,-7.0f);
        sand.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(-15.0F, -8.0F, -15.0f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-15.0F, -8.0F, 15.0f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(15.0F, -8.0F, 15.0f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(15.0F, -8.0F, -15.0f);
        glEnd();
        
        glTranslated(-2.0f,-6.0f,1.0f);      // Translasi piramid ke belakang
        wall.bind();
                
        glBegin(GL_TRIANGLES);              // Start Drawing The Pyramid
        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 2.0f, 0.0f);      // Top Of Triangle (Front)
        glTexCoord2f(0.0f, 0.0f);          // Green
        glVertex3f(-2.0f,-2.0f, 2.0f);      // Left Of Triangle (Front)
        glTexCoord2f(0.0f, 1.0f);          // Blue
        glVertex3f( 2.0f,-2.0f, 2.0f);      // Right Of Triangle (Front)
        
        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 2.0f, 0.0f);      // Top Of Triangle (Right)
        glTexCoord2f(0.0f, 0.0f);          // Blue
        glVertex3f( 2.0f,-2.0f, 2.0f);      // Left Of Triangle (Right)
        glTexCoord2f(0.0f, 1.0f);          // Green
        glVertex3f( 2.0f,-2.0f, -2.0f);     // Right Of Triangle (Right)
        
        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 2.0f, 0.0f);      // Top Of Triangle (Back)
        glTexCoord2f(0.0f, 0.0f);          // Green
        glVertex3f( 2.0f,-2.0f, -2.0f);     // Left Of Triangle (Back)
        glTexCoord2f(0.0f, 1.0f);          // Blue
        glVertex3f(-2.0f,-2.0f, -2.0f);     // Right Of Triangle (Back)

        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 2.0f, 0.0f);      // Top Of Triangle (Left)
        glTexCoord2f(0.0f, 0.0f);          // Blue
        glVertex3f(-2.0f,-2.0f,-2.0f);      // Left Of Triangle (Left)
        glTexCoord2f(0.0f, 1.0f);          // Green
        glVertex3f(-2.0f,-2.0f, 2.0f);      // Right Of Triangle (Left)
        glEnd();                            // Done Drawing The Pyramid
        
        glTranslated(4.0f,-1.0f,-1.0f);      // Translasi piramid ke belakang
        wall2.bind();
                
        glBegin(GL_TRIANGLES);              // Start Drawing The Pyramid
        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 1.0f, 0.0f);      // Top Of Triangle (Front)
        glTexCoord2f(0.0f, 0.0f);          // Green
        glVertex3f(-1.0f,-1.0f, 1.0f);      // Left Of Triangle (Front)
        glTexCoord2f(0.0f, 1.0f);          // Blue
        glVertex3f( 1.0f,-1.0f, 1.0f);      // Right Of Triangle (Front)
        
        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 1.0f, 0.0f);      // Top Of Triangle (Right)
        glTexCoord2f(0.0f, 0.0f);          // Blue
        glVertex3f( 1.0f,-1.0f, 1.0f);      // Left Of Triangle (Right)
        glTexCoord2f(0.0f, 1.0f);          // Green
        glVertex3f( 1.0f,-1.0f, -1.0f);     // Right Of Triangle (Right)
        
        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 1.0f, 0.0f);      // Top Of Triangle (Back)
        glTexCoord2f(0.0f, 0.0f);          // Green
        glVertex3f( 1.0f,-1.0f, -1.0f);     // Left Of Triangle (Back)
        glTexCoord2f(0.0f, 1.0f);          // Blue
        glVertex3f(-1.0f,-1.0f, -1.0f);     // Right Of Triangle (Back)

        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 1.0f, 0.0f);      // Top Of Triangle (Left)
        glTexCoord2f(0.0f, 0.0f);          // Blue
        glVertex3f(-1.0f,-1.0f,-1.0f);      // Left Of Triangle (Left)
        glTexCoord2f(0.0f, 1.0f);          // Green
        glVertex3f(-1.0f,-1.0f, 1.0f);      // Right Of Triangle (Left)
        glEnd();
        
        glTranslated(-1.0f,2.0f,-5.0f);      // Translasi piramid ke belakang
        wall.bind();
                
        glBegin(GL_TRIANGLES);              // Start Drawing The Pyramid
        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 3.0f, 0.0f);      // Top Of Triangle (Front)
        glTexCoord2f(0.0f, 0.0f);          // Green
        glVertex3f(-3.0f,-3.0f, 3.0f);      // Left Of Triangle (Front)
        glTexCoord2f(0.0f, 1.0f);          // Blue
        glVertex3f( 3.0f,-3.0f, 3.0f);      // Right Of Triangle (Front)
        
        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 3.0f, 0.0f);      // Top Of Triangle (Right)
        glTexCoord2f(0.0f, 0.0f);          // Blue
        glVertex3f( 3.0f,-3.0f, 3.0f);      // Left Of Triangle (Right)
        glTexCoord2f(0.0f, 1.0f);          // Green
        glVertex3f( 3.0f,-3.0f, -3.0f);     // Right Of Triangle (Right)
        
        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 3.0f, 0.0f);      // Top Of Triangle (Back)
        glTexCoord2f(0.0f, 0.0f);          // Green
        glVertex3f( 3.0f,-3.0f, -3.0f);     // Left Of Triangle (Back)
        glTexCoord2f(0.0f, 1.0f);          // Blue
        glVertex3f(-3.0f,-3.0f, -3.0f);     // Right Of Triangle (Back)

        glTexCoord2f(1.0f, 0.0f);          // Red
        glVertex3f( 0.0f, 3.0f, 0.0f);      // Top Of Triangle (Left)
        glTexCoord2f(0.0f, 0.0f);          // Blue
        glVertex3f(-3.0f,-3.0f,-3.0f);      // Left Of Triangle (Left)
        glTexCoord2f(0.0f, 1.0f);          // Green
        glVertex3f(-3.0f,-3.0f, 3.0f);      // Right Of Triangle (Left)
        glEnd();
                    
        glTranslated(8.0f, 9.0f, -20.0f); // Translasi bulan (left/right side,top/bottom side,size)
        
        //glRotatef(rtri,0.0f,-1.0f,0.0f);
        renderSphere(-2f, -0.5f, -1f);
        glCallList(sphereId);
        
        glBegin(GL_POINTS);
        glVertex2d(-2.75, +2.75);

        glVertex2d(+3.75, -5.75);
        glVertex2d(-4.75, +8.75);
        glVertex2d(+5.75, +7.75);
        glVertex2d(+2.75, +2.75);
        glVertex2d(+1.25, +1.75);
        glVertex2d(-7.75, +7.75);
         glVertex2d(+7.75, -7.75);
         glVertex2d(-8.75, +3.75);
          glVertex2d(-10.75, +2.75);
           glVertex2d(-15.75, +7.75);
            glVertex2d(-20.75, +5.75);
             glVertex2d(-18.75, +2.75);
              glVertex2d(-30.75, +4.75);
               glVertex2d(-28.75, +1.75);
                glVertex2d(-25.75, +8.75);
                glVertex2d(-25.75, -4.75);
                glVertex2d(-32.75, -2.75);
                glVertex2d(-30.75, -6.75);
                glVertex2d(-25.75, -6.75);
                glVertex2d(-15.75, -2.75);
                glVertex2d(-20.75, -2.75);
         glVertex2d(-22.75, +1.75);
         glVertex2d(+22.75, +1.75);
         glVertex2d(0.75, +4.75);
         glVertex2d(0.25, +7.75);
          glVertex2d(-8.75, -2.75);
           glVertex2d(+8.75, -0.75);
           glVertex2d(+15.75, -2.75);
           glVertex2d(+20.75, +3.75);
           glVertex2d(+16.75, +2.75);
           glVertex2d(+20.75, -5.75);
       glEnd();
       
        rtri+=2.1f;
  }    
    
  private void renderSphere(float x, float y, float z) {
        Sphere sphere = new Sphere();
        sphere.setDrawStyle(GLU_FILL);
        sphere.setTextureFlag(true);
        sphere.setNormals(GLU_SMOOTH);
        sphereId = glGenLists(1);
        glNewList(sphereId, GL_COMPILE);
        moon.bind();
        sphere.draw(1.0f, 70, 70);
        glEndList();
  } 
  
  private static void input() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
                    flatten = !flatten;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_L) {
                    // Reload the shaders and the heightmap data.
                    glUseProgram(0);
                    glDeleteProgram(shaderProgram);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_P) {
                    // Switch between normal mode, point mode, and wire-frame mode.
                    int polygonMode = glGetInteger(GL_POLYGON_MODE);
                    if (polygonMode == GL_LINE) {
                        glPolygonMode(GL_FRONT, GL_FILL);
                    } else if (polygonMode == GL_FILL) {
                        glPolygonMode(GL_FRONT, GL_POINT);
                    } else if (polygonMode == GL_POINT) {
                        glPolygonMode(GL_FRONT, GL_LINE);
                    }
                }
            }
        }
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        } else if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }
        if (Mouse.isGrabbed()) {
            camera.processMouse(1, 80, -80);
        }
        camera.processKeyboard(25, 1);
  }  
  
  private static void setUpMatrices() {
        camera.applyPerspectiveMatrix();
  }
  
  private static void setUpStates() {
        camera.applyOptimalStates();
        glPointSize(2);
        // Enable the sorting of shapes from far to near
        glEnable(GL_DEPTH_TEST);
        // Remove the back (bottom) faces of shapes for performance
        glEnable(GL_CULL_FACE);
  }
  
  public void run() {
        while(!Display.isCloseRequested()) {
            if(Display.isVisible()) {
                update();
                render();
                input();
                setUpStates();
                setUpMatrices();
            }
            else {
                if(Display.isDirty()) {
                    render();
                }
                try {
                    Thread.sleep(100);
                }
                catch(InterruptedException ex) {
                }
            }
            Display.update();
            Display.sync(60); //fps --> 60
        }
    } 
  
    public void update() {
        updateFPS();
    }
    
    public void updateFPS(){
        if(Sys.getTime()-lastTime>1000){
            Display.setTitle("Tugas Besar 3D OpenGL fps: "+fps);
            fps = 0;
            lastTime = Sys.getTime();
        }
        fps++;
    }

    private static class Point {

        final float x;
        final float y;
        final float z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
