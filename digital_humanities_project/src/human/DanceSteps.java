package human;





import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import common.Resources;



import common.J3DShape;





/**
 * Simple Java 3D program that can be run as an application or as an applet.
 */

public class DanceSteps extends javax.swing.JPanel  implements MouseListener, MouseMotionListener,MouseWheelListener {
	
	//////////////////////////GUI componenet ///////////////////////////

	private javax.swing.JPanel simulationPanel;
    private String[] Dance_step_names;
    JLabel Mudra_name ;
    JLabel image_lbl;
    J3DShape m_j3d = new J3DShape();
   
    public DanceSteps(Container container) {
        // Initialize the GUI components
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        initComponents();

        centerPanel(container);

    }

  
    public static class MyApplet extends JApplet {
        DanceSteps mainPanel;

        public void init() {
            setLayout(new BorderLayout());
          
            mainPanel = new DanceSteps(this);
            add(mainPanel, BorderLayout.CENTER);
                        
        }

        public void destroy() {
            mainPanel.destroy();
        }
    }

 

    private static class MyFrame extends JFrame {
        MyFrame() {
            setLayout(new BorderLayout());
            setResizable(false);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setTitle("Hasta Mudras");
            getContentPane().add(new DanceSteps(this), BorderLayout.CENTER);
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
    
  
    public void destroy() {
		// TODO Auto-generated method stub
		
	}

   private void initComponents() 
	{
        
    	  intialise();

        setLayout(new java.awt.BorderLayout());
        simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
        
        simulationPanel.setLayout(new java.awt.BorderLayout());
        add(simulationPanel, java.awt.BorderLayout.CENTER);
        
	}
	 public void intialise()
	 {
		 Dance_step_names =new String[24];
		 Dance_step_names[0]="AlaPadma";
		 Dance_step_names[1]="Aral";
		 Dance_step_names[2]="ArdhaChandra";
		 Dance_step_names[3]="ArdhaPataka";
		 Dance_step_names[4]="Bhramara";
		 Dance_step_names[5]="Chandrakala";
		 Dance_step_names[6]="Chatura";
		 Dance_step_names[7]="Hausasyya";
		 Dance_step_names[8]="Kangul";
		 Dance_step_names[9]="Kapitha";
		 Dance_step_names[10]="KartariMukh";
		 Dance_step_names[11]= "KataKaMukh";
		 Dance_step_names[12]="Mayur";
		 Dance_step_names[13]= "MrugShirsha";
		 Dance_step_names[14]= "Mukula";
		 Dance_step_names[15]="Mushti";
		 Dance_step_names[16]="PadmaKosh";
		 Dance_step_names[17]="Pataka";
		 Dance_step_names[18]="SarpaShirsh";
		 Dance_step_names[19]="Shikhar";
		 Dance_step_names[20]= "ShukaTunda";
		 Dance_step_names[21]= "SinhaMukh";
		 Dance_step_names[22]= "TamraChud";
		 Dance_step_names[23]= "TriPataka";
	 }
     private void centerPanel(Container container){
    	
   	 	simulationPanel.setPreferredSize(new java.awt.Dimension(500, 400));
        simulationPanel.setLayout(new java.awt.BorderLayout());
       
        javax.swing.JPanel guiPanel = new javax.swing.JPanel();
        guiPanel.setBackground(new Color(100,100,100));
         JLabel title = new JLabel("Dance Steps", JLabel.CENTER);
         title.setFont(new Font("Arial", Font.BOLD, 18));
        
        title.setForeground(Color.orange);
        //lbl.setBackground(Color.BLACK);
        guiPanel.add(title);
        simulationPanel.add(guiPanel, BorderLayout.NORTH);
        
        JPanel center = center_panel();
        
        
       
        simulationPanel.add(center, BorderLayout.CENTER);
        
		
       
        JPanel btmPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        simulationPanel.add(btmPanel, BorderLayout.SOUTH);
        Mudra_name = new JLabel("AlaPadma");
        Mudra_name.setFont(new Font("Arial", Font.BOLD, 13));
        Mudra_name.setForeground(Color.WHITE);
        guiPanel = new javax.swing.JPanel();
        guiPanel.setBackground(new Color(100,100,100));        
        guiPanel.add( Mudra_name);
        btmPanel.add(guiPanel);
        
        
        guiPanel = new javax.swing.JPanel();
        guiPanel.setBackground(new Color(100,100,100));
               
        simulationPanel.add(guiPanel, BorderLayout.WEST);
        
        guiPanel = new javax.swing.JPanel();
        guiPanel.setBackground(new Color(100,100,100));
        
                
        simulationPanel.add(guiPanel, BorderLayout.EAST);
        
        // Create the content branch and add it to the universe
       
     
        
        Mudra_name = new JLabel("Mudra ", JLabel.LEFT);
        Mudra_name.setFont(new Font("Arial", Font.BOLD, 13));
        Mudra_name.setForeground(Color.WHITE);
        guiPanel = new javax.swing.JPanel();
        guiPanel.setBackground(new Color(100,100,100));        
        guiPanel.add( Mudra_name);
        btmPanel.add(guiPanel,BorderLayout.NORTH);
                        
        guiPanel = new javax.swing.JPanel(); // 
        guiPanel.setBackground(new Color(130,169,193));
        guiPanel.setBorder(BorderFactory.createLineBorder(new Color(67,143,205),4));
        guiPanel.add(createDropdownPanel());
        
        btmPanel.add(guiPanel,BorderLayout.SOUTH);
        //mix panel
        guiPanel=new JPanel();
        guiPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 40, 4, 4);
        guiPanel.setBackground(new Color(219,226,238));
        guiPanel.setBorder(BorderFactory.createLineBorder(new Color(241,241,235),4));
	      
        	   	
	     btmPanel.add(guiPanel,BorderLayout.CENTER);
   }
   
    
    
    
   private JPanel createDropdownPanel(){
   	
	java.awt.GridBagConstraints gridBagConstraints;
	JPanel ioparm = new JPanel(new java.awt.GridLayout(1,1));
   	//ioparm.setPreferredSize(new java.awt.Dimension(600, 100));
   	ioparm.setBackground(new Color(130,169,193));
   	JPanel inparm = new JPanel();
   	inparm.setLayout(new java.awt.GridBagLayout());
   	
	
   
   	Insets insets =  new java.awt.Insets(4, 4, 4, 4);  
   	inparm.setBackground(new Color(130,169,193));
   	
   
	 
   	JLabel lbl = new JLabel("Select Hasta Mudra");
	gridBagConstraints = new java.awt.GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.EAST,GridBagConstraints.NONE,insets,0,0);
	inparm.add(lbl,gridBagConstraints);
	
	
	
	JComboBox dance_steps = new JComboBox(Dance_step_names);
	gridBagConstraints = new java.awt.GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0);
	inparm.add(dance_steps ,gridBagConstraints);
	
	dance_steps.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			JComboBox jcmbType = (JComboBox) e.getSource();
			String step_name = (String) jcmbType.getSelectedItem();
			image_lbl.setIcon(getIcon(step_name) );
			Mudra_name.setText( step_name);
			
			
		}
	} );
	
	
   ioparm.add(inparm );
   return  ioparm;
       
   }
   private ImageIcon getIcon(String name)
   {
	   ImageIcon icon = m_j3d.createImageIcon("resources/images/"+name+".png");
	   return icon;
	   
	   
   }
   
   
    private JPanel center_panel()
    {
    	
    	JPanel panel = new JPanel();
    	panel.setBorder(BorderFactory.createLineBorder(new Color(241,241,235),4));
    	 
    	panel.setLayout(new java.awt.GridBagLayout());
    	
    	panel.setBackground(new Color(97,115,152));
    	Insets insets = new Insets(4,4,4,4);
    	GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
    	
    	
    	
    	image_lbl = new JLabel("");
    	
    	image_lbl.setBackground(new Color(97,115,152));
    	image_lbl.setOpaque(true);
    	 
    
    	ImageIcon icon = m_j3d.createImageIcon("resources/images/Chandrakala.png");
    	image_lbl.setIcon(icon );
    
    	insets = new Insets(4,4,4,4);
    	gridBagConstraints = new java.awt.GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.NONE,insets,0,0);

    	
    	panel.add(image_lbl,gridBagConstraints);
    	
    	
    	return panel;
    	
    }


public void mouseClicked(MouseEvent arg0) {
	
	
}



public void mouseEntered(MouseEvent arg0) {

	
}


public void mouseExited(MouseEvent arg0) {

	
}



public void mousePressed(MouseEvent arg0) {
	
}



public void mouseReleased(MouseEvent arg0) {
	
	
}



public void mouseDragged(MouseEvent arg0) {
	
	
}



public void mouseMoved(MouseEvent arg0) {
	
}



public void mouseWheelMoved(MouseWheelEvent arg0) {
	
}
   
  
   
   
    
    
    
   

  
    
   
  
   
	 
}




