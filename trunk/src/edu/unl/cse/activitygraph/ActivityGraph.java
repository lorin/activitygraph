package edu.unl.cse.activitygraph;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.tmatesoft.svn.core.SVNException;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.event.PZoomEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.swing.PScrollPane;
import edu.umd.cs.piccolox.swing.PViewport;
import edu.unl.cse.activitygraph.SeriesGroup.SeriesGroupEmptyException;
import edu.unl.cse.activitygraph.ShiftCalculator.WorkInterval;
import edu.unl.cse.activitygraph.dialogs.OpenAuthenticatedUrlDialog;
import edu.unl.cse.activitygraph.handlers.DoubleClickEventHandler;
import edu.unl.cse.activitygraph.handlers.HorizontalPanEventHandler;
import edu.unl.cse.activitygraph.handlers.HorizontalZoomEventHandler;
import edu.unl.cse.activitygraph.hpcs.DatabaseConnectionException;
import edu.unl.cse.activitygraph.interfaces.IDataSource;
import edu.unl.cse.activitygraph.interfaces.ITimedEvent;
import edu.unl.cse.activitygraph.nodes.IntervalNode;
import edu.unl.cse.activitygraph.nodes.PointNode;
import edu.unl.cse.activitygraph.nodes.SeriesGroupNode;
import edu.unl.cse.activitygraph.sources.HpcsDbDataSource;
import edu.unl.cse.activitygraph.sources.SVNDataSource;
import edu.unl.cse.activitygraph.sources.XMLDataSource;
import edu.unl.cse.activitygraph.util.ColorCycler;
import edu.unl.cse.activitygraph.util.CoordMapper;
import edu.unl.cse.activitygraph.SeriesGroup;
import edu.unl.cse.activitygraph.Series;

/**
 * ActivityGraph is the main class that implements the activitygraph
 * application.
 *
 */
public class ActivityGraph extends AGFrame {

	/**
	 * For serialization, although this probably will never be used 
	 */
	private static final long serialVersionUID = -145542972936219240L;

	private IDataSource dataSource;
	
	private CoordMapper coordMapper;
	
	private PAffineTransform initialView;
	
	private ColorCycler cycler;
	
	private float xAxisOffset; // How high above the x-axis to start drawing
	
	private float intvlHeight; // How high the intervals should be 
	private float ptRadius;   // Radius of data points
	
	private float seriesDist; // y distance between series
	
	private float seriesGroupDist;//y distance between seriesGroup
	
	private PNode stationaryParent;

	private float yDisto;//y distance from legend to origin
	
	private Point2D.Float initialViewOffset; // Where to initially move the camera

	private String seriesName;
	
	float xTickLabelYOffset;
			

	Hashtable<String, Float> yLabelTable = new Hashtable<String, Float>();//<yLabelName,yPosition>

	ArrayList <IntervalNode> intervalNodes;
	ArrayList <PointNode> pointNodes;
	
	ArrayList<PPath> tickArrayList;
	ArrayList<TickLabel> TickLabelArrayList;
	ArrayList<PTextStretched> PTextStretchedArrayList;

	private boolean isCompressed;
	
	private boolean is1dPanZoom; // If true, pan/zoom is 1d mode, else in 2d mode


	ShiftCalculator shiftCalculator;

	private int bigGapMinutes; // Big gap used for shift calculator

	private int paddingMinutes; // Minutes of padding used for shift calculator

	private JButton compressExpandButton;
	
	private JButton panZoom1d2dToggleButton;

	private PPath xAxisNode;
	
	public ActivityGraph(IDataSource dataSource) {
		super();
		//setExtendedState(AGFrame.MAXIMIZED_BOTH);
		this.dataSource = dataSource;
		

		/*
		 * Because of how threading works with Piccolo, we do all
		 * other initialization in the initialize method
		 */
	}
	
	
	public void initialize() {

		
		this.xAxisOffset = -10;
		this.intvlHeight = 10; 
		this.ptRadius = 5;
		this.seriesDist = -20; 
		this.seriesGroupDist = -40;
		this.xTickLabelYOffset=10;
		
		this.initialViewOffset = new Point2D.Float(150,430);
		
		
		this.seriesName="";
		this.yDisto=70;
				
		this.bigGapMinutes = 2*60; // Big gap = 2 hours
		this.paddingMinutes = 60; // 10 minutes of padding
		
		this.isCompressed = false;
			
		
		// Set zoom and pan to be X only
		this.is1dPanZoom = true;
		PCanvas c = getCanvas();
		c.setPanEventHandler(new HorizontalPanEventHandler());
		c.setZoomEventHandler(new HorizontalZoomEventHandler());
		
		
		final PScrollPane scrollPane = new PScrollPane(c);
		getContentPane().add(scrollPane);
		
		
		final PViewport viewport = (PViewport) scrollPane.getViewport();
		
		viewport.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				ActivityGraph.this.updateStatus();
			}
		});
			

		// Create the buttons at the top
		this.createToolbar();
		
		// create the menus
		this.createMenus();

		
		// Move the camera so it is centered on the diagram
		PCamera cam = getCanvas().getCamera();
		cam.translateView(this.initialViewOffset.getX(), this.initialViewOffset.getY());
		// Save the initial camera view so we can return to it later if desired		
		saveInitialView();
		
		if(this.dataSource != null) {
			try {
				setStatus(this.dataSource.getFirstEventTime() + " - " + this.dataSource.getLastEventTime());
				drawGraph();
			} catch(SeriesGroupEmptyException ex) {
				JOptionPane.showMessageDialog(ActivityGraph.this,"Error: A SeriesGroup in the input is empty: " + ex.emptySeriesGroup.getName(),"Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
		
		
	}//end of initialize()

	/*
	 * This code is to be implemented. The goal is to call updateTicks after a zoom/pan/scroll
	 * to draw ticks that were not previously visible
	 * 
	private void updateTicks() {
		PCanvas canvas = getCanvas();
		PCamera cam = canvas.getCamera();
		PBounds rect = cam.getViewBounds();
	
	}
	*/
	
	protected void updateStatus() {
		// If the coordmapper hasn't been defined yet, we can't do this calculation
		if(coordMapper==null) {return;}
		// In compressed mode, we can't specify the the date range
		if(isCompressed) {return;}
		PCanvas canvas = getCanvas();
		PCamera cam = canvas.getCamera();
		PBounds rect = cam.getViewBounds(); 
		setStatus(coordMapper.xToTime((float)rect.getMinX()).toString() + "-" + 
				  coordMapper.xToTime((float)rect.getMaxX()).toString());
	}



	/**
	 * Draw the axes and data
	 *
	 */
	private void drawGraph() throws SeriesGroup.SeriesGroupEmptyException {
		if(this.dataSource.isEmpty()) {
			JOptionPane.showMessageDialog(this,"No data to display!");
			return;
		}
		this.clear();

		PCamera camera = getCanvas().getCamera(); 
		camera.setViewTransform(initialView);
		this.stationaryParent = new PNode();
		camera.addChild(this.stationaryParent);
				
				
		this.cycler = new ColorCycler();
		this.coordMapper = new CoordMapper(dataSource.getFirstEventTime());
		
		this.compressExpandButton.setEnabled(true); 

		float ymax = drawData();
	
		
		//draw xAxis  yAxis and unit mark on x Axis
		float xmax = this.coordMapper.timeToX(this.dataSource.getLastEventTime());
		float xmin = this.coordMapper.timeToX(this.dataSource.getFirstEventTime());
		Calendar first = Calendar.getInstance();
		first.clear();
		first.setTime(this.dataSource.getFirstEventTime());
		Calendar last = Calendar.getInstance();
		last.clear();
		last.setTime(this.dataSource.getLastEventTime());
		drawAxes(xmin,xmax, ymax,first,last);
		
		//add tooltips
		setTooltips();
	}
	
	
	/**
	 * Remove all of the drawn objects
	 *
	 */
	private void clear() {
		this.getCanvas().getLayer().removeAllChildren();
		getCanvas().getCamera().removeAllChildren();
		
		this.shiftCalculator = new ShiftCalculator(bigGapMinutes, paddingMinutes);
		this.intervalNodes = new ArrayList<IntervalNode>();
		this.pointNodes = new ArrayList<PointNode>();
		this.tickArrayList= new ArrayList<PPath>();
		this.TickLabelArrayList= new ArrayList<TickLabel>();
		this.PTextStretchedArrayList= new ArrayList<PTextStretched>();
	}


	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem item;
		this.setJMenuBar(menuBar);
	
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenu openMenu = new JMenu("Open");
		fileMenu.add(openMenu);

		item = new JMenuItem("XML file");
		openMenu.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					JFileChooser fc = new JFileChooser();
					XMLFileFilter filter = new XMLFileFilter();
					fc.setFileFilter(filter);
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int returnVal = fc.showOpenDialog(ActivityGraph.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						ActivityGraph.this.dataSource = new XMLDataSource(fc.getSelectedFile());
						ActivityGraph.this.drawGraph();
					}
				} catch(SeriesGroupEmptyException e) {
					JOptionPane.showMessageDialog(ActivityGraph.this,"Error: A SeriesGroup in the input is empty: " + e.emptySeriesGroup.getName(),"Error", JOptionPane.ERROR_MESSAGE);
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(ActivityGraph.this,ex.toString());
				}
			}
		});

		
		item = new JMenuItem("Subversion repository");
		openMenu.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OpenAuthenticatedUrlDialog dlg = 
					new OpenAuthenticatedUrlDialog(ActivityGraph.this,
							"Connect to Subversion","","anonymous","anonymous");
				dlg.setVisible(true);
				if(dlg.userClickedOk()) {
					try {
						ActivityGraph.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						ActivityGraph.this.dataSource = new SVNDataSource(dlg);
						ActivityGraph.this.drawGraph();
					} catch(SVNException e) {
						JOptionPane.showMessageDialog(ActivityGraph.this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					} catch(SeriesGroupEmptyException e) {
						JOptionPane.showMessageDialog(ActivityGraph.this,"Error: A SeriesGroup in the input is empty: " + e.emptySeriesGroup.getName(),"Error", JOptionPane.ERROR_MESSAGE);
					} finally {
						ActivityGraph.this.setCursor(Cursor.getDefaultCursor());
					}
				}				
			}
		});
		item = new JMenuItem("Experiment Manager class");
		openMenu.add(item);
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				OpenAuthenticatedUrlDialog dlg = new OpenAuthenticatedUrlDialog(ActivityGraph.this,"Connect to HPCS database",
													  "localhost:5432/hpcs-dev","lorin","");
				dlg.setVisible(true);
				if(dlg.userClickedOk()) {
					ActivityGraph.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					try {
					
					ActivityGraph.this.dataSource = new HpcsDbDataSource(dlg.getUrl(),dlg.getLogin(),dlg.getPassword());
					ActivityGraph.this.drawGraph();
					ActivityGraph.this.setCursor(Cursor.getDefaultCursor());
					} catch(SeriesGroupEmptyException e) {
						JOptionPane.showMessageDialog(ActivityGraph.this,"Error: A SeriesGroup in the input is empty: " + e.emptySeriesGroup.getName(),"Error", JOptionPane.ERROR_MESSAGE);
					} catch(DatabaseConnectionException e) {
						ActivityGraph.this.setCursor(Cursor.getDefaultCursor());
						JOptionPane.showMessageDialog(ActivityGraph.this,"Database error:\n" + e.reason,"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
		});
		item = new JMenuItem("Trac repository");
		openMenu.add(item);
		item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(ActivityGraph.this,"Not yet implemented!","Error",JOptionPane.ERROR_MESSAGE);
				/*
				OpenAuthenticatedUrlDialog dlg = new OpenAuthenticatedUrlDialog(ActivityGraph.this,"Trac RSS feed", "http://cse.unl.edu:8080/projects/ActivityGraph/timeline?milestone=on&ticket=on&changeset=on&wiki=on&max=100000&daysback=100000&format=rss","","");
				dlg.setVisible(true);
				if(dlg.userClickedOk()) {
					try {
						ActivityGraph.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						ActivityGraph.this.dataSource = new TracDataSource(dlg.getUrl());
						ActivityGraph.this.drawGraph();
						ActivityGraph.this.setCursor(Cursor.getDefaultCursor());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(ActivityGraph.this,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE);
						ActivityGraph.this.setCursor(Cursor.getDefaultCursor());
					}
				}
				*/
			}
		});
		
		item = new JMenuItem("Exit");
		fileMenu.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		this.validate();

	
	}



private void setTooltips()
{
	
	final PCamera camera = getCanvas().getCamera();
	final PText tooltipNode = new PText();
	tooltipNode.setPickable(false);
	camera.addChild(tooltipNode);
	camera.addInputEventListener(new PBasicInputEventHandler() {
		public void mouseMoved(PInputEvent event) {
			updateToolTip(event);
		}

		public void mouseDragged(PInputEvent event) {
			updateToolTip(event);
		}
		

		public void updateToolTip(PInputEvent event) {
			PNode n = event.getInputManager().getMouseOver()
					.getPickedNode();
			String tooltipString = (String) n.getAttribute("tooltip");
			Point2D p = event.getCanvasPosition();

			event.getPath().canvasToLocal(p, camera);

			tooltipNode.setText(tooltipString);
			tooltipNode.setOffset(p.getX() + 15, p.getY() + 8);

		}

	});
	
}
	private void drawAxes(float xmin,float xmax, float ymax,Calendar first,Calendar last) 
	{
		
		PNode node = this.getCanvas().getLayer();
		
		//draw x axis
		this.xAxisNode=PPath.createLine(0,0,xmax,0);
		node.addChild(this.xAxisNode);

		/*
		 * We want the y-axis to be stationary relative to the camera.
		 * The tricky part is figuring out the right coordinates to know where
		 * to put it 
		 */

		PCamera camera = this.getCanvas().getCamera();
		
		Point2D.Float from = new Point2D.Float(0,0);
		Point2D.Float to = new Point2D.Float(0,ymax);
		camera.viewToLocal(from);
		camera.viewToLocal(to);
		//y axis
		this.stationaryParent.addChild(PPath.createLine((float)from.getX(),(float)from.getY(),(float)to.getX(),(float)to.getY()));
	
		/*
		 * draw the marks on xAxis
		 *  
		 */
		drawTicks(xmin, first, last, node);
	}


	private void drawTicks(float xmin, Calendar first, Calendar last,
			PNode parent) {
		// Fast forward last up to the next hour if it's not already at an hour
		if (last.get(Calendar.MINUTE) != 0) {
			last.add(Calendar.MINUTE, 60 - last.get(Calendar.MINUTE));

		}

		Calendar cal;
		float xval;
		for (xval = xmin, cal = (Calendar) first.clone(); cal.before(last)
				|| cal.equals(last); cal.add(Calendar.MINUTE, 1), ++xval) 
		{
			PPath tick = TickMark.createTick(xval, 0, cal);
			if(tick != null) {
				parent.addChild(tick);
				this.tickArrayList.add(tick);
				TickLabel lab = TickLabel.createLabel(xval, 0 + this.xTickLabelYOffset,
						cal);
				parent.addChild(lab);
				this.TickLabelArrayList.add(lab);
				if (cal.get(Calendar.HOUR_OF_DAY)==12 ){ 
					if(cal.get(Calendar.MINUTE)==0){
					String m = String.valueOf(cal.get(Calendar.MONTH)+1);
					String d = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
					String date =m+"/"+d;
					PTextStretched p= new PTextStretched(date,xval,25 + this.xTickLabelYOffset);
					p.setX(xval-p.getWidth()/2);
					p.setY(25 + this.xTickLabelYOffset);
					//p.centerFullBoundsOnPoint(xval, 25 + this.xTickLabelYOffset);
					//p.setWidth(p.getWidth()*2);
					//TickLabel p= new TickLabel(date,0.1f,xval,25 + this.xTickLabelYOffset);
					p.setPaint(Color.white);
					parent.addChild(p);
					this.PTextStretchedArrayList.add(p);
					}		
				}
			}
		}
	}
	

	private void drawYLabel(float xOrigin, float yOrigin,SeriesGroup sg){
		float yLabelPos;
		PText yLabel;
		String text = sg.getName();
		PText serialGroupName = new PText(text);
		serialGroupName.setPaint(Color.LIGHT_GRAY);
		serialGroupName.setX(xOrigin-130);
		serialGroupName.setY(yOrigin+sg.getYmean());
		this.stationaryParent.addChild(serialGroupName);
		for(Series series : sg.getSeries())
		{
			String name = series.getName();
			yLabelPos=series.getyLabelPos();
			Float yPos = yLabelPos+yOrigin-5;
			
			yLabel = new PText(name);
			yLabel.setX(xOrigin - yLabel.getWidth() - 1);
			yLabel.setY(yPos);
			yLabel.setPaint(Color.white);
			this.stationaryParent.addChild(yLabel);
			series.yLabel=yLabel;
			Point2D.Float from = new Point2D.Float(0,0);
			Point2D.Float to = new Point2D.Float(0,yPos);
			PCamera cam = this.getCanvas().getCamera();
			cam.viewToLocal(from);
			cam.viewToLocal(to);			
		}




}

	/**
	 * Draw all of the data onto the canvas
	 * 
	 * @return the maximum y value used
	 */
	private float drawData() throws SeriesGroup.SeriesGroupEmptyException {
		float y = this.xAxisOffset;
		float yLegendDis = this.yDisto;
		for(SeriesGroup seriesGroup : this.dataSource.getSeriesGroups()) {
			if(seriesGroup.isEmpty()) {
				throw new SeriesGroup.SeriesGroupEmptyException(seriesGroup);
			}
			y = this.drawSeriesGroup(seriesGroup,y);	
			y +=this.seriesGroupDist;
			//draw legend
			drawLegend(this.initialViewOffset.x,this.initialViewOffset.y,seriesGroup,yLegendDis);
			yLegendDis += 50;
			//draw y axis label
			drawYLabel(this.initialViewOffset.x,this.initialViewOffset.y,seriesGroup);

		}	
		return y;
		
	}
	
	/**
	 * Draw a series group
	 * 
	 * @param seriesGroup the series group to draw
	 * @param y the y-value to start drawing at
	 * @return the last y value drawn 
	 */
	private float drawSeriesGroup(SeriesGroup seriesGroup, float y) {
		PNode layer = getCanvas().getLayer();
		SeriesGroupNode seriesGroupNode = new SeriesGroupNode();

		Color color = this.cycler.getNextColor();
		for(Series series : seriesGroup.getSeries()) {
			String seriesName = series.getName();
			//java.util.Date start= series.getEvents().get(0).getStartTime();
			//String hint = String.valueOf(Math.round(start));
			
			drawSeries(series,seriesGroupNode,y,color);
			this.yLabelTable.put(seriesName, y);
			seriesGroupNode.addInputEventListener(new DoubleClickEventHandler(seriesGroupNode,series));
			
			// Set the y-value
			y += this.seriesDist;
			color = this.cycler.getNextColor();
			
		}
		layer.addChild(seriesGroupNode);
		return y;
	}
	
	private void drawSeries(Series series, SeriesGroupNode seriesGroupNode, float y, Color color) {
		series.setColor(color);
		series.setyLabelPos(y);
		PNode seriesNode = new PNode();
		seriesGroupNode.addChild(seriesNode);
		for(ITimedEvent event : series.getEvents()) {
			drawEvent(event, seriesNode, y, color);
		}
		
	}

	/**
	 * Draw a timed event onto the canvas
	 * @param event the event to be drawn
	 * @param seriesNode the series associated with the event
	 * @param y the y-value of the event
	 * @param color the color to use for the event
	 */
	private void drawEvent(ITimedEvent event, PNode seriesNode, float y,
			Color color) {
		PNode eventNode;
		String time=event.toString();
		String note=event.getNote();
		String hint = "";
		if(note!=null) {
			hint +=note + "\n";
		}			
		hint +=time;
		
		if(event.isInterval()) {
			eventNode = createIntervalNode((Interval)event, y, color);		
			this.intervalNodes.add((IntervalNode) eventNode);
			eventNode.addAttribute("tooltip",hint);
			
			
		} else {
			eventNode = createPointNode((Point)event, y, color);
			this.pointNodes.add((PointNode) eventNode);
			eventNode.addAttribute("tooltip",hint);
		}
		seriesNode.addChild(eventNode);
		shiftCalculator.addEvent(event);
	}
	

	private PNode createPointNode(Point point, float y, Color color) {
		float x = this.coordMapper.timeToX(point.getTime());
		return PointNode.createPointNode(point,x,y,this.ptRadius,color);
	}


	private PNode createIntervalNode(Interval interval, float y, Color color) {
		
		float x = this.coordMapper.timeToX(interval.getStartTime());
		float width = interval.getLengthMin();
		float height = this.intvlHeight;
		return new IntervalNode(interval, x,y,width,height,color);
		
	}

	private void drawLegend(float xLegendPos,float yLegendPos,SeriesGroup sg,float yDisto){
		float startx = xLegendPos;
		float yTextLegend = yLegendPos+yDisto;
		float yTruthLegend = yTextLegend+15; //y aXis for truth legend
		float blank = 40; //blank between legend
		float wideth = 20; //legend wideth
		float h=10; //legend height
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		
		String text = sg.getName();
		PText truthPos = new PText(text);
		truthPos.setX(startx-truthPos.getWidth());
		truthPos.setY(yTruthLegend);
		stationaryParent.addChild(truthPos);
		

		for(Series series : sg.getSeries())
		{
			seriesName = series.getName();
			
			
			 Color color = series.getColor();
			 String truthPercent;
			 
			 /*
			  *  If a series group is only events, it will have no length.
			  *  In that case, count the events instead
			  */
			 float groupLength = sg.getLengthMin();
			 if(groupLength > 0) {
				 truthPercent = "(" + percentFormat.format(series.getLengthMin() / groupLength) + ")";
			 } else {
				 truthPercent = "(" + percentFormat.format((float)series.getEventCount()/ sg.getGroupEventCount()) + ")";
			 }
			 //draw legend node
			 PNode legendNode = new PNode();
			 legendNode.setBounds(startx, yTextLegend, wideth, h);
			 legendNode.setPaint(color);
			 //draw legend text
			 PText legendText = new PText(seriesName);
			 legendText.setX(startx + wideth);
			 legendText.setY(yTextLegend);
			 
			 //draw percent --- lower position than legend text
			 PText truthLegentPercent = new PText(truthPercent);
			 truthLegentPercent.setX(startx + wideth);
			 truthLegentPercent.setY(yTruthLegend);
			 
			 //add nodes
			 stationaryParent.addChild(legendNode);
			 stationaryParent.addChild(legendText);
			 stationaryParent.addChild(truthLegentPercent);
			 
			 startx = startx + wideth + (float)legendText.getWidth() + blank;
		}
		
		
		
		
	}
	private void createToolbar() {
		JToolBar toolBar = new JToolBar();
		JButton reset = new JButton("Reset view");
		toolBar.add(reset);
		toolBar.setFloatable(false);


		// Behavior of reset button
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// If we are in 1d-pan mode, have to switch to switch to 2d mode to allow the
				// axis to move back to where it should be
				int duration = 1000;
				PCamera cam = getCanvas().getCamera(); 
				
				cam.animateViewToTransform(initialView,duration);
				 
				if(is1dPanZoom) {
					// We're in 1D, so it's attached to the camera. Here the trick 
					// is just to animate it to the original position
					PAffineTransform identityTransform = new PAffineTransform();
					stationaryParent.animateToTransform(identityTransform, duration);
				}else {
					/*
					 * We're in 2D, so this is a little bit trickier.
					 * 
					 * We need to determine the right coordinates
					 * 
					 */ 
					AffineTransform tf = null;
					try {
						tf = initialView.createInverse();
					} catch (NoninvertibleTransformException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stationaryParent.animateToTransform(tf, duration);
				}

			}
		});
		
		this.compressExpandButton = new JButton("Compress");
		toolBar.add(compressExpandButton);
		compressExpandButton.setEnabled(false);
		
		
		// Behavior of compress button
		compressExpandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				toggleCompressExpand();
			}
		});
		
		this.panZoom1d2dToggleButton = new JButton("switch to 2D pan/zoom");
		toolBar.add(this.panZoom1d2dToggleButton);
		
		this.panZoom1d2dToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					togglePanZoom1d2d();
				} catch (NoninvertibleTransformException e) {
					JOptionPane.showMessageDialog(ActivityGraph.this,
							"View transform cannot be inverted",
							"Graphics error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		
		getContentPane().add(toolBar,BorderLayout.NORTH);
		getContentPane().validate();
		
	}


	private void hideMarksOnAxis(){
	
		//hide ticks
		for(PPath tick : this.tickArrayList){
			int i =this.shiftCalculator.inWhichActiveInterval(tick.getX());
			if(i>=0){
				WorkInterval wI = this.shiftCalculator.getWorkIntervals().get(i);
				float xShift = this.shiftCalculator.getShiftMinutes(wI.GetStartTime());
				tick.animateToPositionScaleRotation(-xShift,0,1,0,1000);
				
			}
			
			else{
				
				tick.setVisible(false);	
			}
			
			
		}
		
		//hide hour labels
		for(TickLabel TickLabel : this.TickLabelArrayList){
			
			int i =this.shiftCalculator.inWhichActiveInterval(TickLabel.getX());
			if(i>=0){
				WorkInterval wI = this.shiftCalculator.getWorkIntervals().get(i);
				float xShift = this.shiftCalculator.getShiftMinutes(wI.GetStartTime());
				TickLabel.animateToPositionScaleRotation(-xShift,0,1,0,1000);
				
			}else{
				TickLabel.setVisible(false);	
			}

			
		}
		//hide day lables
		for(PTextStretched PTextStretched : this.PTextStretchedArrayList){
			int i =this.shiftCalculator.inWhichActiveInterval(PTextStretched.getX());
			if(i>=0){
				WorkInterval wI = this.shiftCalculator.getWorkIntervals().get(i);
				float xShift = this.shiftCalculator.getShiftMinutes(wI.GetStartTime());
				PTextStretched.animateToPositionScaleRotation(-xShift,0,1,0,1000);
				
			}else{
				PTextStretched.setVisible(false);	
			}

			
		}
		
		
	}
	
	private void visibleMarksOnAxis(){

		//visible ticks
		for(PPath tick : this.tickArrayList){
			tick.animateToPositionScaleRotation(0,0,1,0,1000);
			tick.setVisible(true);
			
		}
		//visible hour labels
		for(TickLabel TickLabel : this.TickLabelArrayList){
			TickLabel.animateToPositionScaleRotation(0,0,1,0,1000);
			TickLabel.setVisible(true);
			
		}
		//visible day labels
		for(PTextStretched PTextStretched : this.PTextStretchedArrayList){
			PTextStretched.animateToPositionScaleRotation(0,0,1,0,1000);
			PTextStretched.setVisible(true);
			
		}
		
		
	}
		
	protected void toggleCompressExpand() {
		
	 if(!isCompressed) {
		this.compressExpandButton.setText("Expand");
		this.isCompressed = true;
		hideMarksOnAxis();
		setStatus("(compressed)");
		
		
	
		for(IntervalNode node : this.intervalNodes) {
			float xShift = shiftCalculator.getShiftMinutes(node.getStartTime());
			node.animateToPositionScaleRotation(-xShift,0,1,0,1000);
		}
		
		for(PointNode node : this.pointNodes) {
			float xShift = shiftCalculator.getShiftMinutes(node.getStartTime());
			node.animateToPositionScaleRotation(-xShift,0,1,0,1000);
		} 
		
		

	 } else {
			this.compressExpandButton.setText("Compress");
			this.isCompressed = false;
			visibleMarksOnAxis();
			
			// Reset the status bar
			updateStatus();

		
			for(IntervalNode node : this.intervalNodes) {
				node.animateToPositionScaleRotation(0,0,1,0,1000);
			}

			for(PointNode node : this.pointNodes) {
				node.animateToPositionScaleRotation(0,0,1,0,1000);
			}
		}
	}
	
	//@SuppressWarnings("unchecked")
	protected void togglePanZoom1d2d() throws NoninvertibleTransformException {
		PCanvas c = getCanvas();

		if(is1dPanZoom) {
			// Switch to the default (2D) pan and zoom behavior
			c.setPanEventHandler(new PPanEventHandler());
			c.setZoomEventHandler(new PZoomEventHandler());
			
			/*
			 * All of the nodes that are usually stationary relative to the camera 
			 * (e.g. y-axis label, legends) now have to become children of the canvas
			 */
			transferNodeFromCameraToCanvas(stationaryParent);

			this.panZoom1d2dToggleButton.setText("switch to 1D pan/zoom");
			
		} else {
			// Switching to the horizontal-only pan and zoom behavior
			c.setPanEventHandler(new HorizontalPanEventHandler());
			c.setZoomEventHandler(new HorizontalZoomEventHandler());
			
			/**
			 * The nodes that are usually stationary relative to the camera
			 * must become children of the camera again
			 */			
			transferNodeFromCanvasToCamera(stationaryParent);			

			this.panZoom1d2dToggleButton.setText("switch to 2D pan/zoom");			
		}
		
		// Toggle the state variable
		is1dPanZoom = !is1dPanZoom;
	}
	
	/**
	 * Take nodes that are stationary relative to the camera, and transfer them
	 * to the canvas so that they can move
	 * @param node
	 * @throws NoninvertibleTransformException thrown if the camera view reference is not invertible
	 */
	private void transferNodeFromCameraToCanvas(PNode node) throws NoninvertibleTransformException {
		PCanvas c = getCanvas();
		PCamera cam = c.getCamera();
		PNode layer = c.getLayer();
		
		/**
		 * To preserve the look of the node from the user's perspective
		 * we need to premultiply it by the inverse of the camera transform.
		 * 
		 * The camera applies the view transform to all nodes in the canvas before displaying them.
		 *   x  - coordinate of a point on the canvas (in canvas coordinate system)
		 *   V  - view transform of the camera
		 *   Vx - coordinate of the point in the camera's view
		 *   
		 *   y       - coordinate of a point that is stationary relative to the camera (in camera view coordinates)
		 *   V       - view transform of the camera
		 *   V^-1 y  - coordinate of that point if it was in canvas coordinates
		 */
	
		node.reparent(layer);
		node.getTransformReference(true).preConcatenate(cam.getViewTransformReference().createInverse());
	}
	
	/**
	 * Inverse of transferNodeFromCanvasToCamera. Takes a node in the canvas and transfers it to be
	 * a child of the camera so it remains stationary relative to the camera during pans and zooms 
	 * @param node
	 */
	private void transferNodeFromCanvasToCamera(PNode node) {
		PCanvas c = getCanvas();
		PCamera cam = c.getCamera();

		node.reparent(cam);
		node.getTransformReference(true).preConcatenate(cam.getViewTransform());
	}

	private void saveInitialView() {
		PCamera cam = getCanvas().getCamera();
		initialView = cam.getViewTransform();		
	}
	
	static class XMLFileFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if(f.isDirectory()) {
				return true; // Must allow directories so user can navigate into them
			}
			
			String ext = getExtension(f);
			return ext.equals("xml");
			
		}
		
		private static String getExtension(File f) {
	        String ext = "";
	        String s = f.getName();
	        int i = s.lastIndexOf('.');

	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        return ext;
		}

		@Override
		public String getDescription() {
			return "XML files";
		}
		
	}


	public static void main(String args[])  {

		// XML
		String fname = "data/demo.xml";
		try {
			new ActivityGraph(new XMLDataSource(fname));
		} catch(nu.xom.ParsingException e) {
			System.out.println("Problem parsing the XML file: " + fname + "\n");
			e.printStackTrace();
		} catch(IOException e) {
			System.out.println("Problem reading the XML file: " + fname + "\n");
			e.printStackTrace();
		}
	}
	

}
