package human;

import common.J3DShape;

import common.Resources;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;


/**
 *  
 * Human_joints.java
 * Purpose: This class will create all human bodies and also gives functions to rotate and translate parts of the body.
 *
 * @author Veera Reddy
 * @version 1.5 
 */
public class Human_joints extends javax.swing.JPanel  implements MouseListener, MouseMotionListener,MouseWheelListener{
	//  Variables declaration - do not modify//GEN-BEGIN:variables
	//////////////////////////GUI componenet ///////////////////////////
	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel simulationPanel;
	private javax.swing.JPanel bottomPanel;
	private javax.swing.JPanel rightPanel;
	
        /* different human parts */
	
	HumanPart hip_part =null;
	HumanPart neck_head_part =null;
	HumanPart head_part=null;
	HumanPart right_hand =null;
	HumanPart right_hand_below_part=null;
	HumanPart right_hand_palm  = null;
	HumanPart right_hand_fingers=null;
	
	
	
	HumanPart bottom_part =null;
	HumanPart right_leg =null;
	HumanPart right_leg_below_part = null;
	HumanPart right_foot =null;
	
	
	HumanPart left_leg =null;
	HumanPart left_leg_below_part =null;
	HumanPart left_foot =null;
	
	
	HumanPart left_hand =null;
	HumanPart left_hand_below_part=null;
	HumanPart left_hand_palm  = null;
	HumanPart left_hand_fingers=null;
	
	
	
	HumanPart total_body =null;

	

   
	

	//private GraphPlotter         graphPlotter;
	////////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse      univ = null;                  // Simple Universe Java3D
	private BranchGroup         scene = null;                 // BranchGroup Scene graph
	private Switch 				switchGroup=null;			 //
	


	  // Shape3D
	


	private HashMap 			hm = new HashMap();
	private J3DShape 			m_j3d	= new J3DShape();
	
	
	private double zval=2.41;



	private JLabel m_Objective= new JLabel("Objective:");

	
	
    // This is the timer that calls the timerActionPerfomed Function
	private Timer timer=null;
	
	
	// Timer for simulation    
	
	



	
	

	
	
	//Colors  to the Human Body 
	 float color = 0.2f + 15 * (0.4f / 20.0f);
  	 Color3f ambient = new Color3f(0.1f, 0.1f, 0.2f);
  	 Color3f emmisive = new Color3f(0.3f,0.4f, 0.5f);
  	 Color3f diffuse =new Color3f(color,color,color);
  	 Color3f specular = new Color3f(0.1f, 0.1f, 0.1f);
  	 float shine=1.0f;
	
  	 double speed =0.8;  
  	 double  hand_rot_amount =0;
  	 double adding_factor=speed;
  	 int action_steps=0;
  	 
  	 Boolean current_action_done = false;
  	 Vector3d rotate=new Vector3d(0,0,0);
	 
	 
		
	
	
	/**
	 *  it will create the scenegraph for the virtual world  
	 *  
	 *  @return it will return scenegraph created .
	 * */
	public BranchGroup createSceneGraph() 
	{
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND );
		objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability( BranchGroup.ALLOW_DETACH );
		
		
		
		objRoot.addChild(createHuman());
		objRoot.addChild(m_j3d.createBox(new Vector3d(-0.0,-0.49, 0.2),new Vector3d(1,-.03,1),new Vector3d(0,0,0), new Color3f(1f, 1f, 1f),"resources/images/table.jpg"));
        objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4, -1),new Vector3d(1.7,1.5,.1), new Vector3d(0f, 0f,0f),new Color3f(0.5f,0.6f,0.72f)));
	    
		return objRoot;
	}

	/**
	 *  it will create the Universe of the virtual world  
	 *  
	 *  @return it will return canvas3d on which whole virtual universe is  created .
	 * */
    private Canvas3D createUniverse(Container container) 
    {
        GraphicsDevice graphicsDevice;
        if (container.getGraphicsConfiguration() != null) {
            graphicsDevice = container.getGraphicsConfiguration().getDevice();
        } else {
            graphicsDevice =
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        }
        GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
        GraphicsConfiguration config = graphicsDevice.getBestConfiguration(template);

        Canvas3D c = new Canvas3D(config);

        univ = new SimpleUniverse(c);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        
        ViewingPlatform viewingPlatform = univ.getViewingPlatform();
        setLight();
        
        univ.getViewingPlatform().setNominalViewingTransform();
        

       ViewingPlatform vp = univ.getViewingPlatform();
   	    TransformGroup steerTG = vp.getViewPlatformTransform();
   	    Transform3D t3d = new Transform3D();
   	    steerTG.getTransform(t3d);
   	    Vector3d s = new Vector3d();
   	    Vector3f currPos=new Vector3f();
   	    t3d.get(currPos); 
   	    
   	   // System.out.println("current Pos:" + currPos);
   	  
   	    /* camera */
   	    t3d.lookAt( new Point3d(0, 0, 2.41 ), new Point3d(0,0,0), new Vector3d(0,1,0));
   	    t3d.invert();  
   	    
   	    //t3d.setTranslation(new Vector3d(0,0,8));
   	    steerTG.setTransform(t3d); 


        // Ensure at least 5 msec per frame (i.e., < 200Hz)
        univ.getViewer().getView().setMinimumFrameCycleTime(5);
        timer.start();

        return c;
    }
   
    /** 
     * 
     *  This function responsible for setting up the light to the virtual universe  
     * 
     * */ 
    private void setLight() {
            BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
            PlatformGeometry pg = new PlatformGeometry();


            Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
            AmbientLight ambientLightNode = new AmbientLight(ambientColor);
            ambientLightNode.setInfluencingBounds(bounds);
            pg.addChild(ambientLightNode);


            Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
            Vector3f light1Direction  = new Vector3f(1.0f, 0.2f, 1.0f);
            Color3f light2Color = new Color3f(1.0f, 1.0f, 1.0f);
            Vector3f light2Direction  = new Vector3f(0.0f, -0.2f, -1.0f);

            DirectionalLight light1
                    = new DirectionalLight(light1Color, light1Direction);
            light1.setInfluencingBounds(bounds);
         pg.addChild(light1);

            DirectionalLight light2
                    = new DirectionalLight(light2Color, light2Direction);
            light2.setInfluencingBounds(bounds);
            pg.addChild(light2);

            ViewingPlatform viewingPlatform = univ.getViewingPlatform();
            viewingPlatform.setPlatformGeometry( pg );


    }

    
    private void destroy() {
        univ.cleanup();
    }

 
        
    /** 
     * this function will create the whole human body .
     * 
	 *  
	 *  @return it  returns the transform group which is the parent to the whole body parts .
	 * 
     * */
    private Group createHuman() 
    {    	
    	
    	Transform3D td = new Transform3D();
		 // td.setTranslation(new Vector3d(0,0.2,0));
      
    	
    	TransformGroup Human = new TransformGroup(td);
		Human.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		Human.setCapability(Group.ALLOW_CHILDREN_READ);
		Human.setCapability(Group.ALLOW_CHILDREN_WRITE);
		Human.setCapability(BranchGroup.ALLOW_DETACH);
		Human.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
 		Human.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	
    	
      
     	/* creating upper middle body */	
        Human.addChild(create_upper_middle_body());
        
        /*creating lower middle boyd */
        Human.addChild(create_lower_middle_body());
        
        
        // Total body is object that represents the whole Body 
        total_body =new HumanPart("total_body",Human, new Vector3d(0,0,0));
        total_body.set_to_intial(0,0,0);
    	return Human;
    	
   }
   /**
    * This function creates the whole upper middle body
    * @return it  returns the transform group that represents the whole upper middle body.
    *  
    *  */
     private TransformGroup create_upper_middle_body()
  {
	  Transform3D td = new Transform3D();
	  Vector3d position = new Vector3d(0,0,1);
	  td.setTranslation(position);
	  TransformGroup hip = new_transformgroup(td);
	  // Body from Hip to Neck
	  hip.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,0.105f,0), new Vector3d(0.8,2.19,0.5), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
	  
	  //Body from Neck and Head
	  hip.addChild(create_neck_head(new Vector3d(0,0.21,0)));
	  //Left Hand
	  hip.addChild(create_left_hand(new Vector3d(0.091,0.2,0)));
	  
	  //Right Hand
	  hip.addChild(create_right_hand(new Vector3d(-0.091,0.2,0)));
	  
	  //Hip_part is the object that represents the hip 
	  hip_part = new HumanPart("hip", hip,position);
	  hip_part.set_to_intial(0,0,0);
	  
	  return hip;
  }

   /** This functions create the head
    * 
    * @param position  It is the position of the head relative to the Neck Joint
    * @return It returns TransformGroup that represents head part
    *
    * */
   private TransformGroup create_head(Vector3d position)
   {
		  Transform3D td = new Transform3D();
		  td.setTranslation(position);
	   
	   TransformGroup head=new_transformgroup(td);
	   //Head
	   head.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.05f, ambient,emmisive,diffuse,specular,shine,"head",hm));
	   
	// human head object 
	   head_part = new HumanPart("head",head,position); 
	   head_part.set_to_intial(0,0,0);
	   
	   return head;
	   
   }
   
  /*private  TransformGroup create_neck()
  {
	  TransformGroup neck=new TransformGroup();
	  neck.addChild(m_j3d.createCylinderWithMatProp(new Vector3d, scale, rot, colr, ambientColor, emissiveColor, diffuseColor, specularColor, shininess, hm, str))
  }*/
  
 /** 
  * This function creates left hand
  *  @param position  it is the positon of left hand top joint , relative to hip [middle of the body]
  *  @return it returns the Transformgroup that represents left hand
  *  
  * */
  private TransformGroup create_left_hand(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  td.rotZ(Math.toRadians(25));
	  td.setTranslation(position);
	  TransformGroup hand = new_transformgroup(td);
          /* upper joint at shoulder */
	  hand.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.012f, ambient,emmisive,diffuse,specular,shine,"head",hm)) ;
	  
          /* upper part of hand */
	  hand.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,-0.07,0), new Vector3d(0.1,1.3,0.1), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
      
          /* this will create the lower part of the hand */
	  hand.addChild(hand_bottom_part(new Vector3d(0,-0.128,0)));
	  
          // this is the object to the left hand 
	  left_hand = new HumanPart("left_hand",hand,position);
	  left_hand.set_to_intial(0, 0, 25);
	  return hand;
  }
  
  /** 
   * This function creates Right hand
   *  @param position  it is the positon of right hand top joint , relative to hip [middle of the body]
   *  @return it returns the Transformgroup that represents Right hand
   *  
   * */
  
  private TransformGroup create_right_hand(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  td.rotZ(Math.toRadians(-45));
	  td.setTranslation(position);
	  TransformGroup hand = new_transformgroup(td);
      
	       /* upper joint at shoulder */
      hand.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.012f, ambient,emmisive,diffuse,specular,shine,"head",hm)) ;
	   
          /* upper part of hand */
	  hand.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,-0.07,0), new Vector3d(0.1,1.3,0.1), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
      
          /* this will create the lower part of the hand */
	  hand.addChild(right_hand_bottom_part(new Vector3d(0,-0.128,0)));
	  
	  /*right_hand is the object that represents whole right hand */
	  right_hand = new HumanPart("right_hand",hand,position);
	  right_hand.set_to_intial(0, 0, 25);
	  
	  return hand;
  }
 
  /** 
   *  This function creates  bottom part of right hand [from hand middle joint to below ] 
   *  @param position  it is the positon of right hand middle joint , relative to right hand top joint 
   *  @return it returns the Transformgroup that represents Right hand bottom part
   *  
   * */
  private TransformGroup right_hand_bottom_part(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  td.rotZ(Math.toRadians(210));
	  td.setTranslation(position);
	  TransformGroup hand_below_part = new_transformgroup(td);
          /* hand middle joint */
	  hand_below_part.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.012f, ambient,emmisive,diffuse,specular,shine,"head",hm));
	  
          /* hand lower part */
	  hand_below_part.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,-0.07,0), new Vector3d(0.1,1.3,0.1), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));

          /*this will create palm */
	  hand_below_part.addChild(create_palm(new Vector3d(0,-0.128,0),"right_hand"));
	  
	  /* this is the class object that represents the part that is below to middle joint */
	  right_hand_below_part =new HumanPart("right_hand_below_part",hand_below_part,position);
	  
	  return hand_below_part;
	  
	  
  }
  /** 
   *  This function creates  bottom part of left hand 
   *  @param position  it is the positon of left hand middle joint , relative to ri hand top joint 
   *  @return it returns the Transform group that represents left hand bottom part
   *  
   * */
  
  private TransformGroup hand_bottom_part(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	 
	   td.rotZ(Math.toRadians(125)); // this is the intial angle how much you want to rotate the hand 
	  td.setTranslation(position);
	  TransformGroup hand_below_part = new_transformgroup(td);

          /* this will create middle  joint of the hand  */
	  hand_below_part.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.012f, ambient,emmisive,diffuse,specular,shine,"head",hm));
	  
          /* this is the lower part of the hand */
	  hand_below_part.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,-0.07,0), new Vector3d(0.1,1.3,0.1), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));

          /* this will create the palm for the left hand */
	  hand_below_part.addChild(create_palm(new Vector3d(0,-0.128,0),"left_hand"));
	  
	  /* This is the object that represents the left hand below_part */
	  left_hand_below_part = new HumanPart("left_hand_below_part",hand_below_part,position);
	  
	  return hand_below_part;
	  
	  
  }
  /** 
   *  This function creates  palm of the  hand 
   *  @param position  it is the positon of palm  , relative to hand middle joint 
   *  @param hand_name it is to denote to which hand this palm is created , if hand_name is left_hand that means , the palm is for left_hand , otherwise for right hand
   *  @return it returns the Transform group that represents palm
   *  
   * */
  private TransformGroup create_palm(Vector3d position,String hand_name)
  {
	  Transform3D td = new Transform3D();
	  
	  td.setTranslation(position);
	  TransformGroup palm = new_transformgroup(td);
	 /* This is the wrist joint */ 
      palm.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.012f, ambient,emmisive,diffuse,specular,shine,"head",hm));
	  
      /* this will create plam middle  part which is in trepizium format */
	  palm.addChild(m_j3d.createtrep(new Vector3d(0,-0.02,0),new Vector3d(0.025,0.027,0.02) ,new Vector3d(0,0,0), 0.3f,0f, 0.3f,new Color3f(1f,0,0),"resources/images/table.jpg","cool",hm));
       /* this will create the fingers */
	  palm.addChild(create_fingers(new Vector3d(0,-0.038,0)));
	  
	  if(hand_name.equals("left_hand"))
	  {
		   left_hand_palm = new HumanPart("left_hand_palm",palm,position);
	  }
	  else
	  {
		 right_hand_palm =   new HumanPart("right_hand_palm",palm,position);
	  }
	  return palm;
	  
  }
  /** 
   *  This function creates  fingers  of the  hand 
   *  @param position  it is the positon of wrist   
   *  @return it returns the Transform group that represents all fingers 
   *  
   * */
  private TransformGroup create_fingers(Vector3d position)
  {
	  
      Transform3D td = new Transform3D();
      td.setTranslation(position);
	  TransformGroup fingers = new_transformgroup(td);
      
          /* creating thumb */
	  fingers.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0.015,-0.007,0), new Vector3d(0.029,0.2,0.029), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
          /*creating forefinger */
	  fingers.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0.007,-0.009,0), new Vector3d(0.031,0.29,0.031), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
          /*creating middle finger*/
	  fingers.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.001,-0.011,0), new Vector3d(0.031,0.34,0.031), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));

	  
	  /*creating ring finger*/
	  fingers.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.009,-0.009,0), new Vector3d(0.031,0.29,0.031), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
          /*creating 5th one which is small finger */
	  fingers.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.018,-0.003,0), new Vector3d(0.035,0.19,0.035), new Vector3d(0,0,-10),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
	  
	  
	  
	  return fingers;
  }
  
  /**
   * this function creates the lower middle body
   * @return it returns the Transformgroup that represents lower middle body 
   * */
  private TransformGroup create_lower_middle_body()
  {
	  Transform3D td = new Transform3D();
	  Vector3d position = new Vector3d(0,0,1);
	  td.setTranslation(position);
	  TransformGroup hip_bottom = new_transformgroup(td);
	 
	 
	
          /* this will create right leg*/
	  hip_bottom.addChild(create_leg(new Vector3d(-0.05,-0.015,0), "right"));
          /* this will create left leg*/
	  hip_bottom.addChild(create_leg(new Vector3d(0.05,-0.015,0),"left"));
	  double y=-0.28;
	  
	/* object that refers to hip bottom  part */
	  
	  bottom_part = new HumanPart("Bottom_part",hip_bottom,position); // object that refers to hip bottom  part 
	  bottom_part.set_to_intial(0,0,0);
	  
	  
	  return hip_bottom;
	  
  }
  /** 
   * This will creates leg
   * @param position it is the position of the leg top joint relative to the Hip 
   * @param leg_name  it denotes which leg it created wether left or right .
   *         leg_name ="left" for left leg. 
   *         leg_name ="right" for right leg.
   *          
   * @return it returns the Transformgroup that represents leg
   *  
   * */
  private TransformGroup create_leg(Vector3d position, String leg_name)
  {
	  Transform3D td = new Transform3D();
	  td.setTranslation(new Vector3d(position));
	  TransformGroup leg = new_transformgroup(td);
          /* creates leg upper part*/
	  leg.addChild(create_leg_upper_part());
          /*creates leg knee joint*/
	 // leg.addChild(create_middle_joint(new Vector3d(0,-0.13,0)));
          /* creates bottom leg part */
	  leg.addChild(create_leg_bottom_part(new Vector3d(0,-0.13,0),leg_name));
	  

	  
	  if(leg_name.equals("right"))
	  {
		 right_leg= new HumanPart("right_leg",leg,position);
		 right_leg.set_to_intial(0,0,0);
	  }
	  else
	  {
		  left_leg= new HumanPart("left_leg",leg,position);
		  left_leg.set_to_intial(0,0,0);
	  }
	  
	  return leg;
  }
  
  /**
   * it creates the bottom part of the leg
   * @param position this is position of the middle joint relative to the hip
   * @param leg_name This to tell for which leg the bottom is created .
   *         leg_name = "left" for left leg
   *         leg_name ="right"  for right leg
   *           
   * @return It returns the TransfromGroup that represents the leg_bottom_part
   * 
   *  */
  private TransformGroup create_leg_bottom_part(Vector3d position,String leg_name)
  { 
	  Transform3D td = new Transform3D();
      td.setTranslation(position);
	  
	  TransformGroup lower_leg = new_transformgroup(td);
          /*knee joint */
	  lower_leg.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.012f, ambient,emmisive,diffuse,specular,shine,"head",hm));
             
          /* lower part of the leg */
	  lower_leg.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,-0.065f,0), new Vector3d(0.1,1.3,0.1), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
	     
	      /* foot of the leg */
	  lower_leg.addChild(create_right_foot(new Vector3d(0,-0.254+0.13,0)));
	  
	  if(leg_name.equals("right"))
	  {
		  /* object that represents right leg part from joint to below  */
		  right_leg_below_part = new HumanPart("right_leg_below_part",lower_leg,position);
	  }
	  else
	  {
		  /* object that represents left leg, part from joint to below  */
		  
		  left_leg_below_part = new HumanPart("left_leg_below_part",lower_leg,position); 
	  }
	 
	  return lower_leg;
  }
  /**
   * it creates the upper part of the leg 
   * @return It returns the TransfromGroup that represents the leg_ upper _part
   *  */
  
  private TransformGroup create_leg_upper_part()
  {
	  
	  TransformGroup upper_leg = new_transformgroup(null);
	  /* creates upper leg part*/
	  upper_leg.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.012f, ambient,emmisive,diffuse,specular,shine,"head",hm));
	  upper_leg.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,-0.065f,0), new Vector3d(0.1,1.3,0.1), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
	  return upper_leg;
  }
  /**
   * it creates the Knee joint of  the leg 
   * @return It returns the TransfromGroup that represents the joint of the leg
   *  */
  
  private TransformGroup create_middle_joint(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  td.setTranslation(new Vector3d(position));
	  TransformGroup joint = new_transformgroup(td);
          /*knee joint */
	joint.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.012f, ambient,emmisive,diffuse,specular,shine,"head",hm));
	  //joint.addChild( m_j3d.createConeWithMatProp1(new Vector3d(0,0,0),new Vector3d(0,0,0), new Color3f(1f,1f,1f), 0.03, 0.02, ambient,emmisive,diffuse,specular,shine));
	  
	  //joint.addChild(m_j3d.createSphere1(new Vector3d(0,-0.015,0),new Vector3d(1,1,1),0.01f, ambient,emmisive,diffuse,specular,shine,"head",hm));
	 // joint.addChild( m_j3d.createConeWithMatProp1(new Vector3d(0,-0.025,0),new Vector3d(0,0,180), new Color3f(1f,1f,1f), 0.03, 0.02, ambient,emmisive,diffuse,specular,shine));
	  
	  
	  
	  return joint;
  }
  /**
   * This function will create the foot of the leg
   * @param position it is the position of the ankle relative to the knee joint of the leg 
   * @return it returns transformation group that represent  foot of the leg 
   * */
  private TransformGroup create_right_foot(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  
	  //td.rotY(Math.toRadians(-90));
	  td.setTranslation(new Vector3d(position));
	  TransformGroup foot = new_transformgroup(td);

          /*creates ankle */
          foot.addChild(m_j3d.createSphere1(new Vector3d(0,0,0),new Vector3d(1,1,1),0.012f, ambient,emmisive,diffuse,specular,shine,"head",hm));
          /*creats foot middle long  part*/
      foot.addChild(m_j3d.createBox_with_material_properites(new Vector3d(0,-0.003,0.04), new Vector3d(0.018,0.004,0.03), new Vector3d(0,0,0), ambient, emmisive, diffuse, new Color3f(1f,0f,0f), shine));
      /*creates thumb */
	  foot.addChild( create_thumb(new Vector3d(-0.016,0,0.07)));
          /*creates forefinger */
	  foot.addChild(create_foot_fore_finger(new Vector3d(-0.005,0,0.07)));
          /*it creats middle finger */
	  foot.addChild( create_foot_middle_finger(new Vector3d(0.005,0,0.07)));
	  
	  
      
      
	  return foot;
	  
  }
  

  /**
   *  This function creates the middle finger of the foot 
   * @param position it is the relative position of the middle finger with respect to the foot square part end .
   * @return It returns the transfromation group that represents the middle finger
   */
  private TransformGroup create_foot_middle_finger(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  
	  td.setTranslation(new Vector3d(position));
	  TransformGroup fore_finger = new_transformgroup(td);

	  fore_finger.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,-0.003,0.008), new Vector3d(0.03, 0.187,0.03), new Vector3d(90,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
	  fore_finger.addChild(m_j3d.createSphere1(new Vector3d(0,-.003,0.012),new Vector3d(1,1,1),0.003f, ambient,emmisive,diffuse,specular,shine,"head",hm));

	  
	  return fore_finger;
  }
  /**
   *  This function creates the fore finger of the foot 
   * @param position it is the relative position of the fore finger with respect to the foot square part end .
   * @return It returns the transfromation group that represents the fore finger
   */
  
  private TransformGroup create_foot_fore_finger(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  
	  td.setTranslation(new Vector3d(position));
	  TransformGroup fore_finger = new_transformgroup(td);
	  fore_finger.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,-0.003,0.008), new Vector3d(0.04, 0.19,0.04), new Vector3d(90,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
	  fore_finger.addChild(m_j3d.createSphere1(new Vector3d(0,-.003,0.012),new Vector3d(1,1,1),0.003f, ambient,emmisive,diffuse,specular,shine,"head",hm));

	  
	  return fore_finger;
  }
  /**
   *  This function creates the thumb finger of the foot 
   * @param position it is the relative position of the thumb finger with respect to the foot square part end .
   * @return It returns the transfromation group that represents the thumb finger
   */
  
  private TransformGroup create_thumb(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  
	  td.setTranslation(new Vector3d(position));
	  TransformGroup  thumb = new_transformgroup(td);
	  
	  thumb.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,-0.003,0.008), new Vector3d(0.04, 0.19,0.04), new Vector3d(90,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none"));
	  thumb.addChild(m_j3d.createSphere1(new Vector3d(0,-.003,0.012),new Vector3d(1,1,1),0.005f, ambient,emmisive,diffuse,specular,shine,"head",hm));
      return thumb;
  }
  
  
  
  private TransformGroup create_left_leg(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  td.setTranslation(new Vector3d(position));
	  TransformGroup left_leg = new_transformgroup(td);
	  return left_leg;
  }
  
  
  /** This function creats neck and head combined part having joint neck bottom . 
   * @param position it is the positon relative to the hip 
   *  @return it returns the transformation group that represents whole neck and head part
   * */
  private TransformGroup create_neck_head(Vector3d position)
  {
	  Transform3D td = new Transform3D();
	  td.setTranslation(new Vector3d(position));
	  TransformGroup head_part =  new_transformgroup(td);
	  head_part.addChild(m_j3d.createLine1(new Point3d(-0.2,0,0), new Point3d(0.2,0,0),new Vector3d(0,0,0), new Vector3d(1,1,1), new Color3f(1f,1f,0f), 3, hm, "cool"));
	  head_part.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0,0.025f,0), new Vector3d(0.2,.5,0.2), new Vector3d(0,0,0),new Color3f(1f,1f,1f), ambient, emmisive, diffuse,new Color3f(0.8f,0f,0f), shine,hm,"none")) ;
	  /* create  head */
	  head_part.addChild(create_head(new Vector3d(0,0.07,0)));
	  
	  neck_head_part = new HumanPart("neck",head_part,position);
	  neck_head_part.set_to_intial(0,0,0);
	  
	  return head_part;
  }
  /**
   * It creates a new Transfromation group with the given Transform3d 
   * @param td create transfromation group with this Transfrom3d 
   * @return it returns new TransfromationGroup
   */
  private TransformGroup new_transformgroup(Transform3D td)
  {
	  TransformGroup temp;
 		if(td!=null)
 			temp = new TransformGroup(td);
 		else
 			 temp = new TransformGroup();
 		
 		temp.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
 		temp.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
 		temp.setCapability(TransformGroup.ALLOW_PICKABLE_READ);
 		temp.setCapability(TransformGroup.ALLOW_PICKABLE_WRITE);
 		temp.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
 		
 		return temp;
  }
   
    
    
    
    
    
    /**
     * Creates new Human body
     */
    public Human_joints(Container container) {
        // Initialize the GUI components
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        initComponents();

        centerPanel(container);
//        scene.addChild(bgleg);
    }

    
    // ----------------------------------------------------------------
    
   

    public static class MyApplet extends JApplet {
        Human_joints mainPanel;

        public void init() {
            setLayout(new BorderLayout());
            mainPanel = new Human_joints(this);
            add(mainPanel, BorderLayout.CENTER);
                        
        }

        public void destroy() {
            mainPanel.destroy();
        }
    }

    // Application framework

    private static class MyFrame extends JFrame {
        MyFrame() {
            setLayout(new BorderLayout());
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            
            getContentPane().add(new Human_joints(this), BorderLayout.CENTER);
            pack();
        }
    }


   
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MyFrame().setVisible(true);
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        
        intialise();
//      new GridLayout(2, 1)
        setLayout(new java.awt.BorderLayout());
        
        bottomPanel = new javax.swing.JPanel(); 	// input from user at bottom
        simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
        topPanel= new javax.swing.JPanel();    		// Pause, resume, Next
        rightPanel = new javax.swing.JPanel();    	// Graph and Input and Output Parameter
                
         
        topPanel();
        
        simulationPanel.setPreferredSize(new java.awt.Dimension(1000, 500));
        simulationPanel.setLayout(new java.awt.BorderLayout());
         
        bottomPanel();
        
        rightPanel();
        
//      Set Alignment
        //add(guiPanel, java.awt.BorderLayout.NORTH);
        add(topPanel, java.awt.BorderLayout.NORTH);
        add(simulationPanel, java.awt.BorderLayout.CENTER);
        add(bottomPanel, java.awt.BorderLayout.SOUTH);
        add(rightPanel, java.awt.BorderLayout.EAST); 
        
       
        
        timer = new Timer(50,new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //...Perform a task...
            	timerActionPerformed(evt);
            }
        });
        
                            

        
    }// </editor-fold>//GEN-END:initComponents
    /**
     * In this function we intialise colors for setting up light in the virtual universal world 
     */
    public void intialise()
    {
    	color = 0.2f + 15 * (0.4f / 20.0f);
   	    ambient = new Color3f(1f,0f,0f);
   	   emmisive = new Color3f(0f,0f, 0f);
   	   diffuse =new Color3f(1f,0f,0f);
   	  specular = new Color3f(1f, 1f, 1f);
   	  shine=64f;
    }
    private void topPanel() {
      }
    
    /**
     *  add components to right panel 
     */
    private void rightPanel() {
        
    	rightPanel.setLayout(new java.awt.GridLayout(2,1,0,1));
    	//rightPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),8));
        
        
        rightPanel.setVisible(true);
    }
    
    /**
     * Add components to the center panel . We add virtual universal world to this panel
     * @param container
     */
    private void centerPanel(Container container){
    	
   	 	simulationPanel.setPreferredSize(new java.awt.Dimension(1024, 660));
        simulationPanel.setLayout(new java.awt.BorderLayout());
       
        javax.swing.JPanel guiPanel = new javax.swing.JPanel();
        guiPanel.setBackground(new Color(100,100,100));
        JLabel lbl = new JLabel("Human Skelton", JLabel.CENTER);
        lbl.setFont(new Font("Book Antiqua", Font.BOLD, 18));

        lbl.setForeground(Color.orange);
        //lbl.setBackground(Color.BLACK);
        guiPanel.add(lbl);
        simulationPanel.add(guiPanel, BorderLayout.NORTH);
        
        Canvas3D c = createUniverse(container);
        simulationPanel.add(c, BorderLayout.CENTER);
        c.addMouseMotionListener(this);
		c.addMouseWheelListener(this);
		c.addMouseListener(this);

        JPanel btmPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        simulationPanel.add(btmPanel, BorderLayout.SOUTH);
        
        guiPanel = new javax.swing.JPanel();
        guiPanel.setBackground(new Color(100,100,100));         
        simulationPanel.add(guiPanel, BorderLayout.EAST);
        
        guiPanel = new javax.swing.JPanel();
        guiPanel.setBackground(new Color(100,100,100));         
        simulationPanel.add(guiPanel, BorderLayout.WEST);
        
        // Create the content branch and add it to the universe
        scene = createSceneGraph();
        univ.addBranchGraph(scene);
        
    
        m_Objective = new JLabel("", JLabel.LEFT);
        m_Objective.setFont(new Font("Arial", Font.BOLD, 13));
        m_Objective.setForeground(Color.WHITE);
        guiPanel = new javax.swing.JPanel();
        guiPanel.setBackground(new Color(100,100,100));        
        guiPanel.add(m_Objective);
        btmPanel.add(guiPanel,BorderLayout.NORTH);
        
        guiPanel = new javax.swing.JPanel(); //          
        guiPanel.setBackground(new Color(235,233,215));
        guiPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);  

        
        
        
     

        
        btmPanel.add(guiPanel,BorderLayout.CENTER);
                
        guiPanel = new javax.swing.JPanel(); // 
        guiPanel.setBackground(new Color(130,169,193));
        guiPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),4));
       guiPanel.add(createInputOutputPanel());
      // guiPanel.add(DifferentButtons());
       btmPanel.add(guiPanel,BorderLayout.SOUTH);
   }
   
   private JPanel createInputOutputPanel(){
   	
		
   	JPanel ioparm = new JPanel(new java.awt.GridLayout(1,2,30,0));
   	//ioparm.setPreferredSize(new java.awt.Dimension(600, 100));
   //	ioparm.setBackground(new Color(67,143,205));
    
   	
   	return  ioparm;
   	  
   }
   
   
  
	   
    private void bottomPanel()
    {
    	
    	bottomPanel  = new JPanel(new java.awt.GridLayout(1,2,40,0));
     }
    
    
    
    private void initInputControlsField(int flag) {
		
    	
    	
       	
    	
		
	}

	private  void enable(Container root, boolean enable) {
	    Component children[] = root.getComponents();
	    for(int i = 0; i < children.length; i++) 
			    children[i].setEnabled(enable);
    }
    
  
    
    private void onNextStage()
    {
    
    	
    	
    	
    	
    
    	
    	
    	
    	
    	
   }
    
    private void enableStage(int s){
    	switch(s){
    	
    	}
    	
    }
    
    public void enableSliders(int s)
    {   
    
	   
    	
    	
    }
    
  
   
    
  
//    private void bottomPanel()
//    {
//    	bottomPanel.setLayout(new java.awt.GridLayout(1,3));
//    }

    
    
    // Resume Button Action
    private void startSimulation(java.awt.event.ActionEvent evt)
    {
//    	if (!rightPanel.isVisible()){
//    		rightPanel.setVisible(true);
//    		bottomPanel.setVisible(true);
//    	}
    	    	
    
        
      
        
   
        
    	timer.start();
        System.out.println("Timer started");
        
              
    }
    
   
    /**
     * What ever you want to do in the running time , we need to write that code here . This functions is getting called 50 times per millisecond .
     * 
     * @param evt
     */
    private void timerActionPerformed(java.awt.event.ActionEvent evt)
    {
    	//hand_rotation_steps(-150,0,left_hand_below_part);
    	
    	
        //do_excercise_routine();
    	
    	do_dance_steps();
    	
    	
        return;            
    }
    public void do_dance_steps()
    {
    	if(action_steps==0)
    		put_hand_below_parts_to_normal();
    	else if(action_steps==1)
    		first_step();
    	else if(action_steps==2)
            second_step();
    		
    	
    	
    }
    public void second_step()
    {
    	
    	
    }
    public void first_step()
    {
    	left_hand.speed = new Vector3d(2,2,2);
    	if(left_hand.current_action_done ==false)
    		left_hand.rotate_multiple_axis( new Vector3d(0,0,0),new Vector3d(0,0,60));
    	
    	left_hand_below_part.speed = new Vector3d(2*(100.0/60.0),2*(100.0/60.0),2*(100.0/60.0));
    	if(left_hand_below_part.current_action_done ==false)
    		left_hand_below_part.rotate_multiple_axis(new Vector3d(0,0,0),new Vector3d(0,0,-100));
    	
    	
    	
    	left_leg.speed = new Vector3d(left_hand.speed.x*(30.0/60.0),left_hand.speed.y*(30.0/60.0),left_hand.speed.z*(30.0/60.0));
    	
    	left_leg_below_part.speed = new Vector3d(left_hand.speed.x*(50.0/60.0),left_hand.speed.y*(50.0/60.0),left_hand.speed.z*(50.0/60.0)) ;
         if(left_leg.current_action_done ==false)
         {
        	 left_leg.rotate_multiple_axis(new Vector3d(0,0,0), new Vector3d(0,0,30));
         }
         if(left_leg_below_part.current_action_done ==false)
         {
        	 left_leg_below_part.rotate_multiple_axis(new Vector3d(0,0,0), new Vector3d(0,0,-50));
         
         }
        	 
    	
    	
    	right_step1();
    	
    	if(check_action_complete_or_not())
    	{
    		action_steps++;
    	}
    	
    	
    }
    public Boolean check_action_complete_or_not()
    {
    	Boolean done = false;
    	if(left_hand.current_action_done == true && right_hand.current_action_done ==true && left_hand_below_part.current_action_done==true && left_leg.current_action_done ==true && left_leg_below_part.current_action_done ==true && right_leg.current_action_done ==true && right_leg_below_part.current_action_done ==true)
    		done = true;
    		
    		
    		return done;
    }
     
    public void right_step1()
    {
    	right_hand.speed = new Vector3d(left_hand.speed.x*(80.0/60.0),left_hand.speed.y*(80.0/60.0),(left_hand.speed.z)*(80.0/60.0));
    	right_hand_below_part.speed = new Vector3d(right_hand.speed.x*(30.0/80.0),right_hand.speed.y*(30.0/80.0),(right_hand.speed.z*(30.0/80.0)));
    	if(right_hand.current_action_done ==false)
    		right_hand.rotate_multiple_axis(new Vector3d(0,0,0),new Vector3d(0,0,-80));
    	if(right_hand_below_part.current_action_done ==false)
    		right_hand_below_part.rotate_multiple_axis( new Vector3d(0,0,0), new Vector3d(0,0,-30));
    	
    }
    public void do_excercise_routine()
    {
    	if(action_steps==0)
    		put_hand_below_parts_to_normal();
    	else if(action_steps==1)
    		put_hands_front(); // putting horizontally
    	else if(action_steps==2)
    	     excercise_step2();
    	else if(action_steps==3)
    		excercise_step3();
    		
    	
    
    	
    	
    	
    }
    public void excercise_step3()
    {
    	put_hands_back_to_normal_position();
    	put_legs_back_to_normal_position();
    	move_body_up();
    	
    	if(left_hand.current_action_done==true && right_hand.current_action_done == true  && right_leg.current_action_done ==true && left_leg.current_action_done ==true && total_body.current_action_done == true)
    	{
    		action_steps=0;
    		left_hand.current_action_done =false;
    		right_hand.current_action_done =false;
    		right_leg.current_action_done = false;
    		left_leg.current_action_done = false; 
    		total_body.current_action_done = false;
    		
    	}
    	
    }
    public void move_body_up()
    {
    	if(total_body.current_action_done == false)
    		total_body.move(new Vector3d(0,-0.05,0), new Vector3d(0,0,0)); 
    }
    public void put_legs_back_to_normal_position()
    {
    	left_leg.speed= new Vector3d(left_hand.speed.x *(20.0/90.0),left_hand.speed.x *(20.0/90.0),left_hand.speed.x *(20.0/90.0));
        right_leg.speed = left_leg.speed;
        if(left_leg.current_action_done == false)
        	left_leg.rotate_multiple_axis(new Vector3d(0,0,20),new Vector3d(0,0,0));
        if(right_leg.current_action_done == false)
        	right_leg.rotate_multiple_axis(new Vector3d(0,0,-20),new Vector3d(0,0,0));
    }
    public void put_hands_back_to_normal_position()
    {
    	if(left_hand.current_action_done ==false)
    		left_hand.rotate_multiple_axis(new Vector3d(0,0,90),new Vector3d(0,0.,5));
    	if(right_hand.current_action_done == false)
    		right_hand.rotate_multiple_axis(new Vector3d(0,0,-90),new Vector3d(0,0,-5));
    }
    public void excercise_step2()
    {
    	put_hands_horizontal();
    	put_legs_wide();
    	move_body_down();
    	if(left_hand.current_action_done==true && right_hand.current_action_done == true  && right_leg.current_action_done ==true && left_leg.current_action_done ==true && total_body.current_action_done == true)
    	{
    		action_steps=3;
    		left_hand.current_action_done =false;
    		right_hand.current_action_done =false;
    		right_leg.current_action_done = false;
    		left_leg.current_action_done = false; 
    		total_body.current_action_done = false;
    		
    	}
    	
    }
    public void move_body_down()
    {
    	if(total_body.current_action_done == false)
    		total_body.move(new Vector3d(0,0,0), new Vector3d(0,-0.05,0)); 
    }
    public void put_hands_horizontal()
    {
    	if(left_hand.current_action_done == false)
    		left_hand.rotate_multiple_axis(new Vector3d(-90,0,0),new Vector3d(-90,90,0));
    	if(right_hand.current_action_done == false)
    		right_hand.rotate_multiple_axis(new Vector3d(-90,0,0),new Vector3d(-90,-90,0));
    	
    }
    public void put_legs_wide()
    {   left_leg.speed= new Vector3d(left_hand.speed.x *(20.0/90.0),left_hand.speed.x *(20.0/90.0),left_hand.speed.x *(20.0/90.0));
        right_leg.speed = left_leg.speed;
    	left_leg.rotate_multiple_axis(new Vector3d(0,0,0),new Vector3d(0,0,20));
    	right_leg.rotate_multiple_axis(new Vector3d(0,0,0),new Vector3d(0,0,-20));
    }
    
    public void put_hand_below_parts_to_normal()
    {
    	left_hand_below_part.rotate(0,0);
    	right_hand_below_part.rotate(0,0);
    	action_steps+=1;
    	
    }
    
    public void put_hands_front()
    {
    	hand_rotation_steps(-90,10,left_hand,"min");
    	hand_rotation_steps(-90,10,right_hand,"min");
    	
    }
    public void hand_rotation(double start_angle, double end_angle, HumanPart hand,int axis)
    {
    	double rot=start_angle;
    	while(true)
    	{
    		if(start_angle > end_angle)
    		{
    			rot-=speed;
    			if(rot < end_angle)
    				break;
    		}
    		else
    		{
    			rot+=speed;
    			if(rot > end_angle)
    				break;
    		}
    		hand.rotate(rot, 0);
    		
    	}
    }
    
    public void hand_rotation_steps(double min , double max,HumanPart hand, String min_or_max)
    {
    	if(hand_rot_amount > max)
    		adding_factor = -speed;
    	else if(hand_rot_amount<min)
    		adding_factor = +speed;
    	
    	hand_rot_amount+=adding_factor;
    	hand_rotation(hand_rot_amount,0,hand);
    	
    	if(min_or_max.equals("min"))
    	{
    		if(hand_rot_amount < min)
    		   action_steps++;
    		
    	}
    	else
    	{
    		if(hand_rot_amount > max )
     		   action_steps++;
     		
    	}
    	
    	
    	
    	
    	System.out.print(hand_rot_amount+"\n");
    }
    public void hand_rotation(double angle,int axis,HumanPart part)
    {
    	 part.rotate(angle, axis);
    }
    public void set_speed(double speed_val)
    {
    	speed = speed_val;
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		ViewingPlatform vp = univ.getViewingPlatform();
	    TransformGroup steerTG = vp.getViewPlatformTransform();
	    Transform3D t3d = new Transform3D();
	    steerTG.getTransform(t3d);
	    //Vector3d s = new Vector3d();
	    Vector3f currPos=new Vector3f();
	    t3d.get(currPos); 
	   
	    
	   // System.out.println("current Pos:" + currPos);
	    int notches = e.getWheelRotation();
	     double movement =0.005;
	   if (notches < 0)
	   {
		   movement=movement*-1;
		
		
	   }
	   zval=zval+movement;
	   
	 
	   
	   
	  
	    
	   
	   
	    
	    
	    t3d.lookAt( new Point3d(0,0,zval), new Point3d(0,0,0), new Vector3d(0,1,0));
	    t3d.invert();
	    
	    steerTG.setTransform(t3d); 
		
		
		
	
	}
    
	

	
    /**
     * This is class for each human part . It will have different parameters and fucntions required to move or rotate
     * 
     *  */
	class HumanPart {
		
	   private String name;
	   private Vector3d joint_position;
	   public TransformGroup Tg =new TransformGroup();
	   private String hashmap_key;
	   private double max_torsion_force; // to specify how much force part can handle .
	   
	   
	// intial_angle[0] -> intial_angle with x 
	// intial_angle[1] -> similarly 1 for y and 2 for z axis intial angles 
	   
	   private double[] intial_angle= new double[3];
	   
	   // rotation_limits[0][0]-> min_x_rotation
	   //rotation_limits[0][1]-> max_x_rotation; similarly 1 for y and 2 for z axis 
	   public double[][] rotation_limits =new double[3][2];
	   
	   /* this variable is to maintain current rotation */
	   public Vector3d current_rotation = new Vector3d(0,0,0);
	   
	   /* this is to maintain current translation */
	   public Vector3d current_translation = new Vector3d(0,0,0);
	   /* this is to verify wether current action that , this part is performing is completed or not .
	    * if it false that means it is still performing the current task 
	    * if it true , complete rotation or translation is done  
	    */
	   public Boolean current_action_done =false;
	   
	   /* this variable is used to control the rotation speed of this part */
	   public Vector3d speed = new Vector3d(0.8,0.8,0.8);
	   
	   public int  function_call_count=0;
	   
	   /*this variable is used to control the translation speed of this part */
	   public Vector3d translate_speed = new Vector3d(0.0001,0.0005,0.0001);
	   /**
	    * Constructor for HumanPart Class
	    * @param Part_name name of the part 
	    * @param temp Transformation group that represents the part 
	    * @param position position of that part . This position is necessary later for doing roation and transformation
	    */
	   public HumanPart(String Part_name,TransformGroup temp,Vector3d position)
	   {
			 name = Part_name;
			 hashmap_key= Long.toHexString(Double.doubleToLongBits(Math.random()));
			 joint_position= position;
			 Tg= temp;
			 


	   }
	   /**
	    *  set the transfromgroup to the current part of the body 
	    * @param temp Transformation group to be set 
	    **/
	   public void  set_transformGroup(TransformGroup temp)
	   {
		   Tg=temp;
		   hm.put(hashmap_key,Tg);
	   }
	   /**
	    * This function is used to set the translation speed
	    * @param trans_speed trans_speed.x is the translation speed in x_direction;
	    * 		 trans_speed.y is the translation speed in y_direction
	    *        trans_speed.z is the translation speed in z_direction
	    *                      
	    **/
	   public void set_translation_speed(Vector3d trans_speed)
	   {
		   translate_speed =trans_speed;
	   }
        /** 
         * this will sets for the current part what is the joint positon
         * @param  position position of the joint with respect to the position of current_part 
            
        **/
	   public void set_joint_position(Vector3d position)
	   {
		   joint_position=new Vector3d();
		   joint_position.x=position.x;
		   joint_position.y=position.y;
		   joint_position.z=position.z;
	  }
	   /**
	    *  This function is used to rotate the current human part about particular axis
	    *  
        *  @param deg amount of rotation in degrees
        *  @param axis rotation about which axis .   
              
        *         if axis=0 means rotation about x-axis.
	    *         if axis=1 means rotation about y-axis.
	    *         if axis=2 means rotation about z-axis .
	    **/
	   
	   
	  public void rotate(double deg,int axis)
	  {
		  /*degree to radian conversion */
		  float rad = (float)Math.PI/180;		 
		  Transform3D temp= new Transform3D();
		  Tg.getTransform(temp);
		  Vector3d s = new Vector3d();
		  // Get Scale
		  temp.getScale(s );			
		  if(axis==0)
		  {
			  temp.rotX(rad*deg);
		  }
		  else if(axis==1)
		  {
			  temp.rotY(rad*deg);
		  }
		  else if(axis==2)
		  {
			  temp.rotZ(rad*deg);
		  }
			 
		  
		  temp.setScale(s);
		  
		  temp.setTranslation(joint_position);
		 
		  Tg.setTransform(temp); 
		  
	  } 
      /** 
       * This function is used to rotate the current part about multiple axis
       * @param rotate rotate.x is the amount of rotation you want about X-axis .
       *               rotate.y is the amount of rotation you want about Y-axis .
       *               rotate.z is the amount of rotation you want about Z-axis
       * */
	  
	  public void rotate_about_multiple_axis(Vector3d rotate)
	  {
		  float rad = (float)Math.PI/180;		 

		  Transform3D   trans = new Transform3D();
		  Transform3D temp= new Transform3D();
		  
		  temp.rotX(rotate.x *rad);
		  trans.rotY(rotate.y *rad);
		  trans.mul(temp);
		  
		  temp.rotZ(rotate.z*rad);
		  trans.mul(temp);
		  
		  trans.setTranslation(joint_position);
		  
		  Tg.setTransform(trans);
		  
	 }
     /** 
      * this function is used to displace[translate] the part from one position to another positon .
      * @param displacement It is the new position that you want to translate current part  
     **/
	 public void set_transform(Vector3d displacement)
	 {
		 float rad = (float)Math.PI/180;		 
		 Transform3D temp= new Transform3D();
		 Tg.getTransform(temp);
		 temp.setTranslation(displacement);
		 Tg.setTransform(temp);
		 
     }
	 public void calculate_torsion()
	 {
		 
	 }
	 /**
	  * After rotating if you want make the current_body_part to come to its intail state by rotating you can use this function
	  * @param x_rot  double x_rotation in degrees
	  * @param y_rot  double x_rotation in degrees
	  * @param z_rot  double x_rotation in degrees
	  */
	 public void set_to_intial(double x_rot,double y_rot,double z_rot)
	 {
		 intial_angle[0] = x_rot;
		 intial_angle[1] = y_rot;
		 intial_angle[2] = z_rot;
		 
		 
	 }
	 /**
	  * This function is used to change  the current rotation of the part 
	  * @param rotate  new rotation that you want to set 
	  */
	 public void set_part_current_rotation(Vector3d rotate)
	 {
		 current_rotation  = rotate; 
	 }
	 /**
	  *  This function is used to rotate the part , from particular start angle to end angle incrementally each time you call this function 
	  * @param rot_start   start angles [ rot_start.x is the start angle about x axis , similarly for other axes ]
	  * @param rot_end     End angle  [ rot_end.x is the end angle about x axis , similarly for other axes ]
	  */
	 public void rotate_multiple_axis(Vector3d rot_start,Vector3d rot_end)
	 {
         /*check is the first time this function is calling , if that is the case set current_rotation to start angle of the roation */
		 if(function_call_count==0)
			   current_rotation = rot_start;
		   function_call_count++;
		   
		   /*  Steps for each axis 
		    *  if start angle is greater than the end angle , decrement the current_rotation with speed value 
		    *       if the current_rotation reaches end angle , stop the rotation  and make current action complete 
		    *  if start angle is greater than the start angle , increment the current_rotation with speed value 
		    *       if the current_rotation reaches end angle , stop the rotation  and make current action complete   
		    * 
		    *  if both start and end are equal then ,current_rotation = end value 
		    *  */
	    	if(rot_start.x> rot_end.x )
			{
	    		current_rotation.x-=speed.x;
	    		
				if(current_rotation.x < rot_end.x )
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else if(rot_start.x< rot_end.x)
			{
				current_rotation.x+=speed.x;
				if(current_rotation.x > rot_end.x)
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else
			{
				current_rotation.x=rot_end.x;
			}
	    	
	    	
	    	
	    	if(rot_start.y> rot_end.y )
			{
	    		current_rotation.y-=speed.y;
				if(current_rotation.y < rot_end.y )
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else if(rot_start.y< rot_end.y)
			{
				current_rotation.y+=speed.y;
				if(current_rotation.y > rot_end.y)
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else
			{
				current_rotation.y=rot_end.y;
			}
	    	
	    	
	    	if(rot_start.z> rot_end.z )
			{
	    		current_rotation.z-=speed.z;
				if(current_rotation.z < rot_end.z )
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else if(rot_start.z< rot_end.z)
			{
				current_rotation.z+=speed.z;
				if(current_rotation.z > rot_end.z)
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else
			{
				current_rotation.z=rot_end.z;
			}
	    	
	    	
	    	/* once you increment or decrement the current_rotation , rotate the part to that value */
	    	rotate_about_multiple_axis(current_rotation);
	}
	 
	 /**
	  * This function is used to Translate  the part , from particular start position to end position incrementally each time you call this function 
	  * @param start   starting position [ start.x is the position x co-ordinate , similarly for other co-ordinates]
	  * @param end     Ending position  [ end.x is the position x co-ordinate , similarly for other co-ordinates]
	  */
	 
	 public void move(Vector3d start,Vector3d end)
	 {
		 /*check is the first time this function is calling , if that is the case set current_translation to start position  */
		 if(function_call_count==0)
		 {
			   current_translation = start;
			   current_action_done=false;
		 }
		   function_call_count++; 
		   
		   /*  Steps for each co-ordinate do this 
		    *  if start co-ordinate  is greater than the end co-ordinate  , decrement the current_translation with speed value 
		    *       if the current_translation reaches end co-ordinate , stop the translation  and make current action complete 
		    *  if start co-ordinate  is greater than the end co-ordinate  , increment the current_translation with speed value 
		    *       if the current_rotation reaches end angle , stop the rotation  and make current action complete   
		    * 
		    *  if both start and end co-ordinates are equal  then ,current_translation.co-ordinate value = end.co-ordinate value  
		    *  */
		   
	    	if(start.x> end.x )
			{
	    		current_translation.x-=translate_speed.x;
				if(current_translation.x < end.x )
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else if(start.x< end.x)
			{
				current_translation.x+=translate_speed.x;
				if(current_translation.x > end.x)
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else
			{
				current_translation.x=end.x;
			}
	    	
	    	
	    	if(start.y> end.y )
			{
	    		current_translation.y-=translate_speed.y;
				if(current_translation.y < end.y )
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else if(start.y< end.y)
			{
				current_translation.y+=translate_speed.y;
				if(current_translation.y > end.y)
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else
			{
				current_translation.y=end.y;
			}
	    	
	    	
	    	if(start.z> end.z )
			{
	    		current_translation.z-=translate_speed.z;
				if(current_translation.z < end.z )
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else if(start.z< end.z)
			{
				current_translation.z+=translate_speed.z;
				if(current_translation.z > end.z)
				{
					current_action_done=true;
					function_call_count=0;
				}
			}
			else
			{
				current_translation.z=end.z;
			}
	    	/* Once you increment or decrement the current_translation , then move the body to that position */ 
	    	set_transform(current_translation);
	    	
	    	
		 
	 }
		 
	 
	 
		 
		 
	}


	
	
    
}///////////////// Defination COmplete

     





