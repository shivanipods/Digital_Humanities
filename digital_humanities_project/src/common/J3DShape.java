package common;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleArray;
import javax.swing.ImageIcon;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.image.TextureLoader;

public class J3DShape extends javax.swing.JPanel{

	private boolean spin = false;
	private boolean noTriangulate = false;
	private boolean noStripify = false;
	private double creaseAngle = 60.0;

	/** Returns an ImageIcon, or null if the path was invalid. */
	public  ImageIcon createImageIcon(String path) {
		URL filename = Resources.getResource(path);
		if (filename != null) {
			return new ImageIcon(filename);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

    /** Returns the Color3f 
     * @param r red value [max 255]
     * @param g green value [max 255]
     * @param b blue value [max 255]
     * @return it retuns the Color3f corresponding to the r,g,b value
     * */
	public Color3f getColor3f(int r,int g,int b){
		return new Color3f(r/255.0f,g/255.0f,b/255.0f);
	}
	/** 
	 * This function used to load obj file to the virtual world 
	 * @param  objfile path to the obj file relative to the common folder
	 * @param  scale   how much you want to scale the image .
	 * @param   rot    Rotation about axis . rot.x is the rotation about x-axis similarly for other axes
	 * @return  It returns the transformgroup that has loaded obj file as child . 
	 * 
	 */
	@SuppressWarnings("restriction")
	
	public Group loadObjectFile(String objfile,Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr) {


		//objScale.addChild(objTrans);

		int flags = ObjectFile.RESIZE;
		if (!noTriangulate) flags |= ObjectFile.TRIANGULATE;
		if (!noStripify) flags |= ObjectFile.STRIPIFY;

		ObjectFile f = new ObjectFile(flags,
				(float)(creaseAngle * Math.PI / 180.0));
		Scene s = null;
		URL filename = Resources.getResource(objfile);
		//pendulum
		try {
			s = f.load(filename);


		}	catch (FileNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (ParsingErrorException e) {
			System.err.println(e);
			System.exit(1);
		}catch (IncorrectFormatException e) {
			System.err.println(e);
			System.exit(1);
		}

		Transform3D t = new Transform3D();
		float rad = (float)Math.PI/180;
		if(rot.x != 0)
			t.rotX(rad*rot.x);
		else if(rot.y != 0)
			t.rotY(rad*rot.y);
		else if(rot.z != 0)
			t.rotZ(rad*rot.z);
		t.setScale(scale);        
		t.setTranslation(pos);

		TransformGroup objTrans = new TransformGroup(t);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		

		BranchGroup bg = s.getSceneGroup();
		BoundingBox boundingBox = new BoundingBox(s.getSceneGroup().getBounds());
		Point3d lower = new Point3d();
		boundingBox.getLower(lower); 
		//	   			Shape3D newShape = (Shape3D) bg.getChild(0);
		//	   			bg.removeChild(0);
		//	   			Appearance app = new Appearance();
		//	   			Color3f objColor = new Color3f(0.7f, 0.2f, 0.8f);
		//	   			Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		//	   			app.setMaterial(new Material(objColor, black, objColor, black, 80.0f));
		//
		//	   			newShape.setAppearance(app);
		//
		//	   			objTrans.addChild(newShape);

		//	   			Map<String, Shape3D> nameMap = s.getNamedObjects();   
		//	   			   
		//	   			for (String name : nameMap.keySet()) {  
		//	   			        System.out.printf("Name: %s\n", name);   
		//	   			} 
		//bg.addChild(trans);
		//objTrans.addChild(bg);
		objTrans.addChild(s.getSceneGroup());


		return objTrans;
	}

	
    /**
     * This functions creates box 
     * @param pos   Position of the center of box 
     * @param scale  Scaling
     * @param rot    Rotation about 3 axes
     * @param colr   Color of the box 
     * @return      Transformation group that has the Box created 
     */
	public Group createBox(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr) {
		// Create a transform group node to scale and position the object.
		//new Point3d(0.0, 0.0, 0.0)
		Transform3D t = new Transform3D();
		float rad = (float)Math.PI/180;
		if(rot.x != 0)
			t.rotX(rad*rot.x);
		else if(rot.y != 0)
			t.rotY(rad*rot.y);
		else if(rot.z != 0)
			t.rotZ(rad*rot.z);
		t.setScale(scale);        
		t.setTranslation(pos);

		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		// Create a simple shape leaf node and add it to the scene graph
		//Shape3D shape = new Box(1.0, 1.0, 1.0);       

		// Create a new ColoringAttributes object for the shape's
		// appearance and make it writable at runtime.
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(colr);
		app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		app.setColoringAttributes(ca);

		objtrans.addChild(new Box(1.0f, 1.0f, 1.0f,app));

		return objtrans;
	}

	
  /** This will create box with material properites.
   *   
   *      The Material object defines the appearance of an object under illumination. If the Material object in an Appearance object is null, lighting is disabled for all nodes that use that Appearance object.
	
	*       The properties that can be set for a Material object are:
	
           * @param pos position of the center of the box 
	       * @param scale Scaling
	       * @param rot   rotation about 3-axes
	       * @param ambientColor   the ambient RGB color reflected off the surface of the material. The range of values is 0.0 to 1.0. The default ambient color is (0.2, 0.2, 0.2).
	       * @param emissiveColor  the RGB color of the light the material emits, if any. The range of values is 0.0 to 1.0. The default emissive color is (0.0, 0.0, 0.0).
	       * @param diffuseColor   the RGB color of the material when illuminated. The range of values is 0.0 to 1.0. The default diffuse color is (1.0, 1.0, 1.0).
           * @param specularColor  the RGB specular color of the material (highlights). The range of values is 0.0 to 1.0. The default specular color is (1.0, 1.0, 1.0).
           * @param shininess      the material's shininess, in the range [1.0, 128.0] with 1.0 being not shiny and 128.0 being very shiny. Values outside this range are clamped. The default value for the material's shininess is 64.
           * 
           * @return it returns Transformgroup that has box as a child 
	       * */
	
	
	
	public Group createBox_with_material_properites(Vector3d pos,Vector3d scale,Vector3d rot,Color3f ambientColor, Color3f emissiveColor, Color3f diffuseColor, Color3f specularColor, float shininess) {
		// Create a transform group node to scale and position the object.
		//new Point3d(0.0, 0.0, 0.0)
		Transform3D t = new Transform3D();
		float rad = (float)Math.PI/180;
		if(rot.x != 0)
			t.rotX(rad*rot.x);
		else if(rot.y != 0)
			t.rotY(rad*rot.y);
		else if(rot.z != 0)
			t.rotZ(rad*rot.z);
		t.setScale(scale);        
		t.setTranslation(pos);

		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		// Create a simple shape leaf node and add it to the scene graph
		//Shape3D shape = new Box(1.0, 1.0, 1.0);       

		// Create a new ColoringAttributes object for the shape's
		// appearance and make it writable at runtime.
		Appearance app = new Appearance();
		
		app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		app.setMaterial(new Material(ambientColor,emissiveColor,diffuseColor,specularColor,shininess));
		

		objtrans.addChild(new Box(1.0f, 1.0f, 1.0f,app));

		return objtrans;
	}

	
	/** It creates cylinder 
	 * default cylinder of radius of 1.0 and height of 2.0.
	 * @param r  radius of cylinder
	 * @param h  height of cylinder
	 * @param pos position of the center of the cylinder
	 * @param scale scaling
	 * @param rot  rotation about the axes
	 * @param colr Color of the cylinder
	 * @param id   hash id 
	 * @param hmap Hashmap
	 * @return Transformation group that has cylinder as child 
	 */
	public Group createCylinder(float r, float h,Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String id,HashMap hmap) {
		// Create a transform group node to scale and position the object.
		//new Point3d(0.0, 0.0, 0.0)
		Transform3D t = new Transform3D();
		float rad = (float)Math.PI/180;
		if(rot.x != 0)
			t.rotX(rad*rot.x);
		else if(rot.y != 0)
			t.rotY(rad*rot.y); 
		else if(rot.z != 0)
			t.rotZ(rad*rot.z);
		t.setScale(scale);        
		t.setTranslation(pos);

		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		// Create a simple shape leaf node and add it to the scene graph
		//Shape3D shape = new Box(1.0, 1.0, 1.0);       

		// Create a new ColoringAttributes object for the shape's
		// appearance and make it writable at runtime.
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(colr);
		app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		app.setColoringAttributes(ca);
		objtrans.addChild(new Cylinder(r,h,app));

		if(id!=null)
			hmap.put(id, objtrans);

		return objtrans;
	}

	
	
	  /** This will create cylinder with material properites.
	   *   
	   *      The Material object defines the appearance of an object under illumination. If the Material object in an Appearance object is null, lighting is disabled for all nodes that use that Appearance object.
		
		*       The properties that can be set for a Material object are:
		
	           * @param pos position of the center of the cylinder 
		       * @param scale Scaling
		       * @param rot   rotation about 3-axes
		       * @param ambientColor   the ambient RGB color reflected off the surface of the material. The range of values is 0.0 to 1.0. The default ambient color is (0.2, 0.2, 0.2).
		       * @param emissiveColor  the RGB color of the light the material emits, if any. The range of values is 0.0 to 1.0. The default emissive color is (0.0, 0.0, 0.0).
		       * @param diffuseColor   the RGB color of the material when illuminated. The range of values is 0.0 to 1.0. The default diffuse color is (1.0, 1.0, 1.0).
	           * @param specularColor  the RGB specular color of the material (highlights). The range of values is 0.0 to 1.0. The default specular color is (1.0, 1.0, 1.0).
	           * @param shininess      the material's shininess, in the range [1.0, 128.0] with 1.0 being not shiny and 128.0 being very shiny. Values outside this range are clamped. The default value for the material's shininess is 64.
	           * @param colr          color of the cylinder
	           * @param hm             Hashmap
	           * @param str            Hashkey 
	           * @return it returns Transformgroup that has cylinder with material properties as child 
		       * */
		

	public Group createCylinderWithMatProp(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,Color3f ambientColor, Color3f emissiveColor, Color3f diffuseColor, Color3f specularColor, float shininess,HashMap hm,String str) {
		// Create a transform group node to scale and position the object.
		//new Point3d(0.0, 0.0, 0.0)
		Transform3D t = new Transform3D();
		float rad = (float)Math.PI/180;
		if(rot.x != 0)
			t.rotX(rad*rot.x);
		else if(rot.y != 0)
			t.rotY(rad*rot.y); 
		else if(rot.z != 0)
			t.rotZ(rad*rot.z);
		t.setScale(scale);        
		t.setTranslation(pos);

		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		// Create a simple shape leaf node and add it to the scene graph
		//Shape3D shape = new Box(1.0, 1.0, 1.0);       

		// Create a new ColoringAttributes object for the shape's
		// appearance and make it writable at runtime.
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(colr);
		app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		app.setColoringAttributes(ca);
		//       Material(Color3f ambientColor, Color3f emissiveColor, Color3f diffuseColor, Color3f specularColor, float shininess) 
		app.setMaterial(new Material(ambientColor,emissiveColor,diffuseColor,specularColor,shininess));
		Cylinder myCylinder=new Cylinder(0.1f,0.1f,app);
		objtrans.addChild(myCylinder);


		myCylinder.getShape(Cylinder.BODY).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		myCylinder.getShape(Cylinder.TOP).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		myCylinder.getShape(Cylinder.BOTTOM).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		myCylinder.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE | Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE|Shape3D.ALLOW_APPEARANCE_READ);	   

		if(str!=null)
		{
			hm.put(str,myCylinder);
		}

		return objtrans;
	}

	/** This will create sphere 
	   *   
	   *      The Material object defines the appearance of an object under illumination. If the Material object in an Appearance object is null, lighting is disabled for all nodes that use that Appearance object.
		
		*       The properties that can be set for a Material object are:
		
	           * @param pos position of the center of the cylinder 
		       * @param radius Radius of the sphere 
		       * @param rot   rotation about 3-axes
		       * @param ambient   the ambient RGB color reflected off the surface of the material. The range of values is 0.0 to 1.0. The default ambient color is (0.2, 0.2, 0.2).
		       * @param emissive  the RGB color of the light the material emits, if any. The range of values is 0.0 to 1.0. The default emissive color is (0.0, 0.0, 0.0).
		       * @param diffuse   the RGB color of the material when illuminated. The range of values is 0.0 to 1.0. The default diffuse color is (1.0, 1.0, 1.0).
	           * @param specular  the RGB specular color of the material (highlights). The range of values is 0.0 to 1.0. The default specular color is (1.0, 1.0, 1.0).
	           * @param shine      the material's shininess, in the range [1.0, 128.0] with 1.0 being not shiny and 128.0 being very shiny. Values outside this range are clamped. The default value for the material's shininess is 64.
	           
	           * @return it returns Transformgroup that has Sphere  with material properties as child 
		       * */
		



	public Group createSphere(Vector3d pos,float radius, Color3f ambient, Color3f emissive,Color3f diffuse,Color3f specular,float shine)
	{
		Transform3D tr2 = new Transform3D();


		float rad = (float)Math.PI/180;



		tr2.setTranslation(pos);
		TransformGroup sphereGroup = new TransformGroup(tr2);
		sphereGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		Appearance app = new Appearance();
		app.setMaterial(new Material(ambient, emissive,diffuse, specular, shine));
		Sphere cool=new Sphere(radius, Sphere.GENERATE_NORMALS, 120,app);

		sphereGroup.addChild(cool);
		app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		return sphereGroup;

	}



    
	

    
    
    /** creates the sphere with material properties that can be stored  in the hash map 
     * 
     * @param pos  Position of the center of the Sphere 
     * @param scale Scaling
     * @param radius Radius of the sphere 
     * @param ambient    the ambient RGB color reflected off the surface of the material. The range of values is 0.0 to 1.0. The default ambient color is (0.2, 0.2, 0.2).      
     * @param emissive the RGB color of the light the material emits, if any. The range of values is 0.0 to 1.0. The default emissive color is (0.0, 0.0, 0.0).
     * @param diffuse   the RGB color of the material when illuminated. The range of values is 0.0 to 1.0. The default diffuse color is (1.0, 1.0, 1.0).
     * @param specular  the RGB specular color of the material (highlights). The range of values is 0.0 to 1.0. The default specular color is (1.0, 1.0, 1.0).
     * @param shine     the material's shininess, in the range [1.0, 128.0] with 1.0 being not shiny and 128.0 being very shiny. Values outside this range are clamped. The default value for the material's shininess is 64.
     * @param str       Hash id 
     * @param hm        Hash map in which you want store the sphere object 
     * @return    It returns the transformation group that has the required sphere as child 
     */
	public Group createSphere1(Vector3d pos,Vector3d scale,float radius, Color3f ambient, Color3f emissive,Color3f diffuse,Color3f specular,float shine,String str,HashMap hm)
	{



		Transform3D tr2 = new Transform3D();

		
		float rad = (float)Math.PI/180;


        tr2.setScale( scale);
		tr2.setTranslation(pos);
		TransformGroup sphereGroup = new TransformGroup(tr2);
		sphereGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		Appearance app = new Appearance();
		app.setMaterial(new Material(ambient, emissive,diffuse, specular, shine));
		app.setName(str);

		Sphere cool=new Sphere(radius, Sphere.GENERATE_NORMALS, 120,app);
		cool.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		cool.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE | Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE|Shape3D.ALLOW_APPEARANCE_READ);
		if(str!=null)
		{
			hm.put(str, cool);
		}
		sphereGroup.addChild(cool);
		app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		
		//sphereGroup.setCapability(TransformGroup.ALLOW_PICKABLE_READ | TransformGroup.ALLOW_PICKABLE_WRITE);
		//sphereGroup.setPickable(true);
		
		return sphereGroup;

	}
	
	/** Create Box with given image file as texture 
	 * 
	 * @param pos    Position of the center of the Box
	 * @param scale  Scaling 
	 * @param rot    Rotation about axis 
	 * @param colr   color of the box 
	 * @param texfile Texture image path relative to the common folder 
	 * @return It returns the Transformation group that has box with texture as child 
	 */
	public Group createBox(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String texfile) {
		// Create a transform group node to scale and position the object.
		//new Point3d(0.0, 0.0, 0.0)
		Transform3D t = new Transform3D();
		float rad = (float)Math.PI/180;
		if(rot.x != 0)
			t.rotX(rad*rot.x);
		else if(rot.y != 0)
			t.rotY(rad*rot.y);  
		else if(rot.z != 0)
			t.rotZ(rad*rot.z);
		t.setScale(scale);        
		t.setTranslation(pos);

		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		// Create a simple shape leaf node and add it to the scene graph
		//Shape3D shape = new Box(1.0, 1.0, 1.0);       

		// Create a new ColoringAttributes object for the shape's
		// appearance and make it writable at runtime.
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(colr);
		//app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		app.setColoringAttributes(ca);

		if(texfile!=null){
			Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
			app.setTexture(tex);
			TextureAttributes texAttr = new TextureAttributes();
			texAttr.setTextureMode(TextureAttributes.MODULATE);
			app.setTextureAttributes(texAttr);
		}


		objtrans.addChild(new Box(1.0f, 1.0f, 1.0f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_NORMALS, app));


		return objtrans;
	}
    /**
     * creates line between point1 and point2 
     * @param p1  first point 
     * @param p2  second point
     * @param pos  Position relative to which , p1 and p2 given
     * @param scale scaling 
     * @param colr Color of the line 
     * @return Return the Transform Group that has Line as child 
     */
	public Group createLine(Point3d p1, Point3d p2,Vector3d pos, Vector3d scale,Color3f colr) { //,
		// Create a transform group node to scale and position the object.

		//Anti-clockwise
		int size=2;

		LineArray line =new LineArray(size,LineArray.COORDINATES | LineArray.COLOR_3);

		for(int i=0; i< size; i++)    	line.setColor(i,colr);


		Point3d line_verts[] = new Point3d[size];

		line_verts[0] = p1;
		line_verts[1] = p2;

		line.setCoordinates(0, line_verts);

		Appearance app = new Appearance();
		LineAttributes la = new LineAttributes();
		la.setLineWidth(1);
		app.setLineAttributes(la);

		Shape3D shape = new Shape3D(line,app);

		Transform3D ts = new Transform3D();
		TransformGroup objTrans = new TransformGroup();
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		ts.setScale(scale);
		ts.setTranslation(pos);
		objTrans.setTransform(ts);
		objTrans.addChild(shape);

		return objTrans;
	}
	/** 
	 *  Create line between given two points with given width
	 * @param p1    First Point 
	 * @param p2    Second Point 
	 * @param pos   Position relative to which , p1 and p2 given
	 * @param scale scaling
	 * @param colr  Color of the line 
	 * @param width Width of th line 
	 * @param hm    Hashmap in which you want to store this line object 
	 * @param str   Hash key 
	 * @return     it returns the transfromGroup that has the line as child 
	 */
	public Group createLine1(Point3d p1, Point3d p2,Vector3d pos, Vector3d scale,Color3f colr,int width,HashMap hm,String str) { //,
		// Create a transform group node to scale and position the object.

		//Anti-clockwise
		int size=2;

		LineArray line =new LineArray(size,LineArray.COORDINATES | LineArray.COLOR_3);

		for(int i=0; i< size; i++)    	line.setColor(i,colr);


		Point3d line_verts[] = new Point3d[size];

		line_verts[0] = p1;
		line_verts[1] = p2;

		line.setCoordinates(0, line_verts);

		Appearance app = new Appearance();
		LineAttributes la = new LineAttributes();
		la.setLineWidth(width);
		app.setLineAttributes(la);

		Shape3D shape = new Shape3D(line,app);
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE | Shape3D.ALLOW_GEOMETRY_READ );
		shape.setPickable(false);

		Transform3D ts = new Transform3D();
		TransformGroup objTrans = new TransformGroup();
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		ts.setScale(scale);
		ts.setTranslation(pos);
		objTrans.setTransform(ts);
		objTrans.addChild(shape);

		if(str!=null)
			hm.put(str,shape);
		return objTrans;
	}
	/**
	 * creates Trapizium 
	 * @param pos   Center of the trapizium
	 * @param scale  Scaling
	 * @param rot    rotation about axes 
	 * @param xleftdisp  how much displacemet you want along left side of x-axis more than correspondin the parallel side 
	 * @param ydisp      how much displacemet you want along  y-axis more than corresponding the parallel side 
	 * @param xrightdisp how much displacemet you want along left side of x-axis more than the correspondin parallel side 
	 * @param colr       color of the trapizum
	 * @param texfile    Image path of the texure 
	 * @param id         Hash key
	 * @param hmap       HashMap in which you want to store this trapizium object 
	 * @return It returns transformation group that has Trapizium as Child \
	 * 
	 */
	public Group createtrep(Vector3d pos,Vector3d scale ,Vector3d rot,float xleftdisp,float ydisp,float xrightdisp,Color3f colr,String texfile,String id,HashMap hmap){

		Transform3D t = new Transform3D();
		float rad = (float)Math.PI/180;
		if(rot.x != 0)
			t.rotX(rad*rot.x);
		else if(rot.y != 0)
			t.rotY(rad*rot.y);
		else if(rot.z != 0)
			t.rotZ(rad*rot.z); 	     
		t.setTranslation(pos);
		t.setScale(scale);

		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		Appearance app = new Appearance();
		RenderingAttributes cool = new  RenderingAttributes();
		cool.setVisible(false);

		// app.setRenderingAttributes(cool);
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(colr);
		//app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		app.setColoringAttributes(ca);

		if(texfile !=null){
			Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
			app.setTexture(tex);
			TextureAttributes texAttr = new TextureAttributes();
			texAttr.setTextureMode(TextureAttributes.MODULATE);
			app.setTextureAttributes(texAttr);
		}

		Shape3D shape = new Shape3D(creatrepGeom(xleftdisp,ydisp,xrightdisp),app);
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE | Shape3D.ALLOW_GEOMETRY_READ);


		objtrans.addChild(shape);		

		if(id!=null)
			hmap.put(id, objtrans);

		return objtrans;

	}



	
	public Geometry creatrepGeom( float leftxdisp,float ydisp,float rightxdisp)
	{
		double max = 0.5;
		double min = -max;
		float disp= leftxdisp;

		QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
		//
		Point3d verts[] = new Point3d[24];
		//
		int i=0;
		// front face
		verts[i++] = new Point3d(max+rightxdisp, min, max);
		verts[i++] = new Point3d(max , max+ydisp, max);
		verts[i++] = new Point3d(min , max, max);
		verts[i++] = new Point3d(min-disp, min, max);
		//   // back face
		verts[i++] = new Point3d(min-disp, min, min);
		verts[i++] = new Point3d(min , max, min);
		verts[i++] = new Point3d(max, max+ydisp, min);
		verts[i++] = new Point3d(max+rightxdisp, min, min);
		// right face
		verts[i++] = new Point3d(max+rightxdisp, min, min);
		verts[i++] = new Point3d(max , max+ydisp, min);
		verts[i++] = new Point3d(max , max+ydisp, max);
		verts[i++] = new Point3d(max+rightxdisp , min, max);
		// left face
		verts[i++] = new Point3d(min-disp, min, max);
		verts[i++] = new Point3d(min , max, max);
		verts[i++] = new Point3d(min , max, min);
		verts[i++] = new Point3d(min-disp , min, min);
		// top face
		verts[i++] = new Point3d(max , max+ydisp, max);
		verts[i++] = new Point3d(max, max+ydisp, min);
		verts[i++] = new Point3d(min , max, min);
		verts[i++] = new Point3d(min, max, max);
		// bottom face
		verts[i++] = new Point3d(min  - disp, min, max);
		verts[i++] = new Point3d(min-disp, min, min);
		verts[i++] = new Point3d(max+rightxdisp , min, min);
		verts[i++] = new Point3d(max+rightxdisp, min, max);
		//
		box.setCoordinates(0, verts);
		i=0;	   
		// Texture co-ordinate
		TexCoord2f q = new TexCoord2f();
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);

		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);

		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);
		//	    
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);
		//	    
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);
		//	    
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);	 		 


		return box;

	}

	
	
	
	
	
	/**
	 *
//Return one of the 8 corner points.  The points are numbered as follows:
  //
//              (m,x,m)                        (x,x,m)
//                ------------------------------ 
//               /|  6                        /| 7
//              / |                          / |
//             /  |                         /  |
//            /   |                        /   |
//           /    |                       /    |
//          /     |                      /     |
//         /      |                     /      |
//        /       |                    /       |
//       /        |                   /        |
/(m,x,m)/  3      |         (x,x,x)  /         |
//     /----------------------------/   2      |
//     |          |                 |          |
//     |          |                 |          |      +Y
//     | (m,m,m)) |  5              |          | 8
//     |          |-----------------|----------|      |
//     |         /                  |         /(x,m,m)|
//     |        /                   |        /        |       -Z
//     |       /                    |       /         |
//     |      /                     |      /          |     /
//     |     /                      |     /           |    /
//     |    /                       |    /            |   /
//     |   /                        |   /             |  /
//     |  /                         |  /              | /
//     | /                          | /               |/
//     |/                           |/                ----------------- +X
//     ------------------------------
//    (m,m,x)   4                    (x,m,x) 1
  //
	 */
	public Geometry createBoxGeom(float top,float botm){

		double max = 0.5;
		double min = -max;

		QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
		//
		Point3d verts[] = new Point3d[24];
		//
		int i=0;
		// front face
		verts[i++] = new Point3d(max + botm, min, max);
		verts[i++] = new Point3d(max + top, max, max);
		verts[i++] = new Point3d(min + top, max, max);
		verts[i++] = new Point3d(min +botm, min, max);
		//   // back face
		verts[i++] = new Point3d(min + botm, min, min);
		verts[i++] = new Point3d(min + top, max, min);
		verts[i++] = new Point3d(max + top, max, min);
		verts[i++] = new Point3d(max + botm, min, min);
		// right face
		verts[i++] = new Point3d(max + botm, min, min);
		verts[i++] = new Point3d(max + top, max, min);
		verts[i++] = new Point3d(max + top, max, max);
		verts[i++] = new Point3d(max + botm, min, max);
		// left face
		verts[i++] = new Point3d(min + botm, min, max);
		verts[i++] = new Point3d(min + top, max, max);
		verts[i++] = new Point3d(min + top, max, min);
		verts[i++] = new Point3d(min + botm, min, min);
		// top face
		verts[i++] = new Point3d(max + top, max, max);
		verts[i++] = new Point3d(max + botm, max, min);
		verts[i++] = new Point3d(min + top, max, min);
		verts[i++] = new Point3d(min + botm, max, max);
		// bottom face
		verts[i++] = new Point3d(min + top, min, max);
		verts[i++] = new Point3d(min + botm, min, min);
		verts[i++] = new Point3d(max + top, min, min);
		verts[i++] = new Point3d(max + botm, min, max);
		//
		box.setCoordinates(0, verts);
		i=0;

		// Texture co-ordinate
		TexCoord2f q = new TexCoord2f();
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);

		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);

		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);
		//	    
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);
		//	    
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);
		//	    
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);	 		 


		return box;

	}

	public Geometry createBoxGeom(Vector3d dim,float top,float botm){

		double x = dim.x*0.5;
		double y = dim.y*0.5;
		double z = dim.z*0.5;
		double nx = -x;
		double ny = -y;
		double nz = -z;

		QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
		//
		Point3d verts[] = new Point3d[24];
		//
		int i=0;
		// front face
		verts[i++] = new Point3d(x + botm, ny, z);
		verts[i++] = new Point3d(x + top, y, z);
		verts[i++] = new Point3d(nx + top, y, z);
		verts[i++] = new Point3d(nx +botm, ny, z);
		//	   // back face
		verts[i++] = new Point3d(nx + botm, ny, nz);
		verts[i++] = new Point3d(nx + top, y, nz);
		verts[i++] = new Point3d(x + top, y, nz);
		verts[i++] = new Point3d(x + botm, ny, nz);
		// right face
		verts[i++] = new Point3d(x + botm, ny, nz);
		verts[i++] = new Point3d(x + top, y, nz);
		verts[i++] = new Point3d(x + top, y, z);
		verts[i++] = new Point3d(x + botm, ny, z);
		// left face
		verts[i++] = new Point3d(nx + botm, ny, z);
		verts[i++] = new Point3d(nx + top, y, z);
		verts[i++] = new Point3d(nx + top, y, nz);
		verts[i++] = new Point3d(nx + botm, ny, nz);
		// top face
		verts[i++] = new Point3d(x + top, y, z);
		verts[i++] = new Point3d(x + botm, y, nz);
		verts[i++] = new Point3d(nx + top, y, nz);
		verts[i++] = new Point3d(nx + botm, y, z);
		// bottom face
		verts[i++] = new Point3d(nx + top, ny, z);
		verts[i++] = new Point3d(nx + botm, ny, nz);
		verts[i++] = new Point3d(x + top, ny, nz);
		verts[i++] = new Point3d(x + botm, ny, z);
		//
		box.setCoordinates(0, verts);
		i=0;

		// Texture co-ordinate
		TexCoord2f q = new TexCoord2f();
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);

		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);

		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);
		//		    
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);
		//		    
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);
		//		    
		q.set(0.0f, 1.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(0.0f, 0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,0.0f);
		box.setTextureCoordinate(0, i++, q);
		q.set(1.0f,1.0f);
		box.setTextureCoordinate(0, i++, q);	 		 


		return box;

	}

	

	




}
