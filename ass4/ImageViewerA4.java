/* ImageViewerA4.java
   CSC 225 - Summer 2015
   Image Viewer for Programming Assignment 4
   

   B. Bird - 03/25/2015

   Modified by B. Jiao - 07/09/2015 (Added extra zoom functionality)
*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JColorChooser;
import javax.swing.JCheckBox;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.EtchedBorder;


import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class ImageViewerA4 {

	private JFrame viewerWindow;
	private final JPanel lowerPanel = new JPanel();
	private final JPanel lowerLeftPanel = new JPanel();
	
	private String image_filename;
	private Color currentColour;
	private ImageCanvas imageCanvas;
	private BufferedImage currentImage;
	private JLabel swatchLabel;
	private boolean samplingColour;
	private JCheckBox animateCheckBox;
	private AnimateHelper animator;
	
	private ImageViewerA4(String input_filename) {
		image_filename = input_filename;
		currentColour = new Color(0,0,0);
		samplingColour = false;
		animator = null;
		initialize();
		changeColour(new Color(0,0,0));
		reloadImage();
	}
	
	public void setPixel(int x, int y, Color colour){
		int width = currentImage.getWidth();
		int height = currentImage.getHeight();
		if (x < 0 || x >= width)
			ErrorAbort("Invalid x coordinate %d",x);
		if (y < 0 || y >= height)
			ErrorAbort("Invalid y coordinate %d",y);
		currentImage.setRGB(x, y, colour.getRGB());
		
		
		if (animator != null){
			animator.setPixel(x, y); //In animation mode, use the AnimationHelper to schedule refreshes
		}else{
			imageCanvas.refreshImage(); //Otherwise, use refreshImage (which schedules a refresh at an idle moment).
		}

	}
	
	private void outline(){
		int width = currentImage.getWidth();
		int height = currentImage.getHeight();
		
		if (animateEnabled())
			animator = new AnimateHelper(width,height);
		else
			animator = null;
			
		Color[][] imagePixels = new Color[width][height];
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				imagePixels[x][y] = new Color(currentImage.getRGB(x,y));

		PixelGraph G = new PixelGraph(imagePixels);
		
		OutlineBoundary.OutlineBoundary(G,this,currentColour);
		if (animator != null)
			imageCanvas.refreshImage();
	
	}
	
	private void loadImage(){
		BufferedImage inputImage = null;
		currentImage = null;
		try{
			System.out.printf("Reading image from %s\n",image_filename);
			inputImage = ImageIO.read(new File(image_filename));
		} catch(java.io.IOException e){
			ErrorExit("Unable to open %s\n",image_filename,e.getMessage());
		}
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		currentImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				currentImage.setRGB(x,y,inputImage.getRGB(x,y));
		System.out.printf("Read a %d by %d image\n",width,height);
	}
	
	private void reloadImage(){
		loadImage();
		imageCanvas.refreshImage();
	}

	
	private void setSwatch(){
		swatchLabel.setIcon(new ImageIcon(new ColourSwatch(25,25,currentColour)));
	}
	
	private void changeColour(Color newColour){
		currentColour = newColour;
		setSwatch();
	}
	
	private void chooseColour(){
		Color newColour = JColorChooser.showDialog(null, "Choose a colour.", currentColour);
		if (newColour != null)
			changeColour(newColour);
	}
	
	private void clickPoint(int x, int y){
		if (x >= currentImage.getWidth() || y >= currentImage.getHeight())
			return;
		if (samplingColour){
			int iCol = currentImage.getRGB(x,y);
			changeColour(new Color(iCol));
			samplingColour = false;
			return;
		}
		
	}
	
	private boolean animateEnabled(){
		return animateCheckBox.isSelected();
	}
	

	private void initialize() {
		viewerWindow = new JFrame();
		viewerWindow.setTitle("CSC 225 - Summer 2015 - Image Viewer");
		viewerWindow.setBounds(100, 100, 800, 600);
		viewerWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		viewerWindow.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		imageCanvas = new ImageCanvas();
		scrollPane.setViewportView(imageCanvas);
		viewerWindow.getContentPane().add(lowerPanel, BorderLayout.SOUTH);
		lowerPanel.setLayout(new BorderLayout(0, 0));
		lowerPanel.add(lowerLeftPanel, BorderLayout.WEST);

		
		JPanel swatchPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) swatchPanel.getLayout();
		flowLayout.setVgap(1);
		flowLayout.setHgap(1);
		swatchPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lowerLeftPanel.add(swatchPanel);
		
		swatchLabel = new JLabel("");
		swatchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		swatchPanel.add(swatchLabel);
		
		JButton chooseColourButton = new JButton("Choose Colour");
		chooseColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseColour();
			}
		});
		lowerLeftPanel.add(chooseColourButton);
		chooseColourButton.setHorizontalAlignment(SwingConstants.LEFT);
		
		JButton sampleColourButton = new JButton("Sample Colour");
		sampleColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				samplingColour = true;
			}
		});
		lowerLeftPanel.add(sampleColourButton);
		
		JButton reloadButton = new JButton("Reload Image");
		reloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadImage();
			}
		});
		lowerLeftPanel.add(reloadButton);
		
		animateCheckBox = new JCheckBox("Animate?");
		lowerLeftPanel.add(animateCheckBox);
		
		JPanel lowerRightPanel = new JPanel();
		lowerPanel.add(lowerRightPanel, BorderLayout.EAST);
		
		JButton outlineButton = new JButton("Outline Boundary");
		outlineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outline();
			}
		});
		lowerRightPanel.add(outlineButton);
	}
	
	class ImageCanvas extends JComponent{
	
		private static final long serialVersionUID = 1L;
		
		private double scaleFactor;

		public ImageCanvas(){
			setSize(100,100);
			scaleFactor = 1;
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					translateClick(x,y);
				}
			});
			addMouseWheelListener(new MouseWheelListener() {
				public void mouseWheelMoved(MouseWheelEvent e) {
					int z = e.getWheelRotation();
					if (z < 0)
						zoomIn(-z);
					else
						zoomOut(z);
				}
			});
			
			class ZoomIn extends AbstractAction {
				public void actionPerformed(ActionEvent e) {
					zoomIn(1);
				} 
			}
			
			ZoomIn zoomInAction = new ZoomIn();
			
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "zoom_in");
			getActionMap().put("zoom_in", zoomInAction);

			
			class ZoomOut extends AbstractAction {
				public void actionPerformed(ActionEvent e) {
					zoomOut(1);
				} 
			}

			ZoomOut zoomOutAction = new ZoomOut();

			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "zoom_out");
			getActionMap().put("zoom_out", zoomOutAction);
		}

		private int imageWidth(){
			return currentImage.getWidth();
		}
		private int imageHeight(){
			return currentImage.getHeight();
		}
		private int canvasWidth(){
			return (int)(imageWidth()*scaleFactor);
		}
		private int canvasHeight(){
			return (int)(imageHeight()*scaleFactor);
		}
		
		private void translateClick(int x, int y){
			
			//Reject the point if it's out of bounds
			if (x < 0 || x >= canvasWidth())
				return;
			if (y < 0 || y >= canvasHeight())
				return;
			//Determine the image coordinates of the (x,y) point
			
			int tx = (int)(x/scaleFactor);
			int ty = (int)(y/scaleFactor);
			
			clickPoint(tx,ty);
		}
		
		private void zoomIn(int z){
			setScale(scaleFactor/Math.pow(0.9, z));
		}
		private void zoomOut(int z){
			setScale(scaleFactor*Math.pow(0.9, z));
		}
		
		public void refreshImage(){
			int width = canvasWidth();
			int height = canvasHeight();
			Dimension d = new Dimension(width,height);
			setSize(d);
			setPreferredSize(d);
			repaint();
		}
		public void refreshRectangle(Rectangle r){
			//Scale the rectangle to accommodate the current scale factor
			int x = (int)(r.x*scaleFactor);
			int y = (int)(r.y*scaleFactor);
			int w = (int)Math.ceil(r.width*scaleFactor);
			int h = (int)Math.ceil(r.height*scaleFactor);
			Rectangle scaledRectangle = new Rectangle(x,y,w,h);
			paintImmediately(scaledRectangle);
		}
		public void setScale(double s){
			scaleFactor = s;
			refreshImage();
		}

		@Override
		public void paintComponent(Graphics g){
			if (currentImage == null)
				return;
			int width = canvasWidth();
			int height = canvasHeight();
			g.drawImage(currentImage, 0, 0, width,height,null);
		}
		
	}
	
	class AnimateHelper{
		Rectangle currentBounds = null;
		int pixelCount = 0;
		double pixelsPerMillisecond;
		AnimateHelper(int w, int h){
			double totalPixels = w*h;
			//Aim for 2% of all pixels per second (i.e. 50 seconds for fill)
			pixelsPerMillisecond = ((totalPixels*0.02)/1000);
		}
		void setPixel(int x, int y){
			pixelCount++;
			if (currentBounds == null)
				currentBounds = new java.awt.Rectangle(x, y, 1, 1);
			else
				currentBounds.add(new Rectangle(x,y,1,1));
			if (pixelCount >= pixelsPerMillisecond*10){
				pixelCount = 0;
				imageCanvas.refreshRectangle(currentBounds);
				currentBounds = null;
				//Sleep for 10 milliseconds.
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					return;
				} 
				
			}
		}
		
	}
	
	static class ColourSwatch extends BufferedImage{
		public ColourSwatch(int width, int height, Color colour){
			super(width,height,BufferedImage.TYPE_INT_RGB);
			int iCol = (colour.getRed()<<16) + (colour.getGreen()<<8) + colour.getBlue();
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++)
					setRGB(x, y, iCol);
					
		}
	}
	
	
	public static void spawn(final String input_filename) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageViewerA4 window = new ImageViewerA4(input_filename);
					window.viewerWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String[] args){
		if (args.length < 1){
			System.out.printf("Usage: ImageViewerA4 <input image>\n");
			return;
		}
		
		String input_filename = args[0];
		
		if (!input_filename.toLowerCase().endsWith(".png"))
			ErrorExit("Input file must be a PNG image.\n");

		spawn(input_filename);
		
	}
	/* Prints an error message and exits (intended for user errors) */
	private static void ErrorExit(String errorMessage, Object... formatArgs){
		System.err.printf("ERROR: " + errorMessage + "\n",formatArgs);
		System.exit(0);
	}
	/* Throws a runtime error (intended for logic errors) */
	private static void ErrorAbort(String errorMessage, Object... formatArgs){
		throw new Error(String.format(errorMessage,formatArgs));
	}

}

