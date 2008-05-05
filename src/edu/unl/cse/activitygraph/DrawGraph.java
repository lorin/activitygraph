package edu.unl.cse.activitygraph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.FileReader;
import java.text.*;

import au.com.bytecode.opencsv.CSVReader;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.event.PZoomEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolox.PFrame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;

import javax.swing.JToolBar;
import javax.swing.JToggleButton;

public class DrawGraph extends PFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<String> startTime;

	private ArrayList<String> stopTime;

	private ArrayList<String> totalTime;

	private ArrayList<String> Activity;

	private ArrayList<String> hstartTime;

	private ArrayList<String> hstopTime;

	private ArrayList<String> htotalTime;

	private ArrayList<String> hActivity;

	private ArrayList<String> sSeconds;

	private ArrayList<String> sStartTime;

	private ArrayList<String> sStopTime;

	private ArrayList<String> sTotalTime;

	private ArrayList<String> sCommCateAndEvent;

	private ArrayList<String> sCommandAndAddNum;

	private ArrayList<String> sCommandDetailAndChangNum;

	private ArrayList<String> sDelNum;

	private int activityLineNumber;

	private int heuristicNumber;

	private int sequenceLineNumber;

	private ArrayList<Float> blankList;

	private ArrayList<String> dateStartList;

	private double thTime, seTime, paTime, teTime, deTime, tuTime, exTime,
			otTime, activityTime;

	private double hthTime, hseTime, hpaTime, hteTime, hdeTime, htuTime,
			hexTime, hotTime, hactivityTime;

	float x0, y0; //origin point

	float xTickLabelYOffset;

	private float xx, yHuristic, yTruth;

	private float yy; //in y axis

	private float h;

	private float xCommandTotal; //in x axis

	private float yCommandTotal; //in y axis 

	private float radius;

	private float ycommandHeight;

	private float blank;

	float xEditStateE;

	Boolean bDashedLine;

	private SimpleDateFormat fmt;

	private Calendar cal;

	PLayer layer;

	PPath path111;

	PNode rootNode;

	PNode recoverNode;

	PNode xLabelNode;

	PNode yLabelNode;

	private PAffineTransform initialView;

	private String editDetail;

	private String truthFile = null;

	private String sequenceFile = null;

	private String heuristicFile = null;

	public DrawGraph() {
		super();

	}

	public DrawGraph(String graphForm) {
		super();

		if (graphForm == "small graph") {
			radius = 1;
			h = 2;

		} else {

		}

	}

	/**
	 * Loads properties from a property file that contains information about the location of data input files
	 * 
	 */
	private Properties loadProperties(String filename)
			throws java.io.IOException {
		Properties props = new Properties();
		InputStream is = new FileInputStream(filename);
		props.load(is);
		return props;
	}

	/**
	 * 
	 * @return The time that the first event was captured or observed 
	 */
	private Calendar getEarliestEventTime() {
		Calendar cal = Calendar.getInstance();
		try {
			Date firstCapturedEvent = fmt.parse(sStartTime.get(0));
			Date firstTrueEvent = fmt.parse(startTime.get(0));
			if (firstCapturedEvent.before(firstTrueEvent)) {
				cal.setTime(firstCapturedEvent);
			} else {
				cal.setTime(firstTrueEvent);
			}
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @return The time that the last event was captured or observed 
	 */
	private Calendar getLatestEventTime() {
		Calendar cal = Calendar.getInstance();
		Date lastCapturedEvent;
		try {
			lastCapturedEvent = fmt
					.parse(sStartTime.get(sStartTime.size() - 1));
			Date lastTrueEvent = fmt.parse(stopTime.get(stopTime.size() - 1));
			if (lastCapturedEvent.after(lastTrueEvent)) {
				cal.setTime(lastCapturedEvent);
			} else {
				cal.setTime(lastTrueEvent);
			}
			return cal;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void initialize() {
		startTime = new ArrayList<String>();
		stopTime = new ArrayList<String>();
		totalTime = new ArrayList<String>();
		Activity = new ArrayList<String>();

		hstartTime = new ArrayList<String>();
		hstopTime = new ArrayList<String>();
		htotalTime = new ArrayList<String>();
		hActivity = new ArrayList<String>();

		sSeconds = new ArrayList<String>();
		sStartTime = new ArrayList<String>();
		sStopTime = new ArrayList<String>();
		sTotalTime = new ArrayList<String>();
		sCommCateAndEvent = new ArrayList<String>();
		sCommandAndAddNum = new ArrayList<String>();
		sCommandDetailAndChangNum = new ArrayList<String>();
		sDelNum = new ArrayList<String>();
		activityLineNumber = 0;
		heuristicNumber = 0;
		sequenceLineNumber = 0;
		blankList = new ArrayList<Float>();
		dateStartList = new ArrayList<String>();

		thTime = seTime = paTime = teTime = deTime = tuTime = exTime = otTime = activityTime = 0;
		hthTime = hseTime = hpaTime = hteTime = hdeTime = htuTime = hexTime = hotTime = hactivityTime = 0;

		x0 = 100;
		y0 = 550;
		xTickLabelYOffset = 10;

		xx = 100;
		yHuristic = y0 - 30;
		yTruth = y0 - 50;
		yy = 100;

		h = 10;

		xCommandTotal = 0;
		yCommandTotal = 450;

		radius = 5;
		ycommandHeight = 450;
		blank = 20;

		fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		layer = getCanvas().getLayer();

		path111 = new PPath();
		rootNode = new PNode();
		recoverNode = new PNode();
		xLabelNode = new PNode();
		yLabelNode = new PNode();

		truthFile = null;
		sequenceFile = null;
		heuristicFile = null;

		// Load the property file that specifies where the truth file and sequence file are
		try {
			Properties props = loadProperties("ActivityGraph.properties");
			truthFile = props.getProperty("truthfile");
			sequenceFile = props.getProperty("sequencefile");
			heuristicFile = props.getProperty("heuristicfile");
			parseActivityfile(truthFile);
			parseSequencefile(sequenceFile);
			parseHeuristicfile(heuristicFile);

		} catch (IOException e) {
			e.printStackTrace();
			// TODO: Figure out how to handle this properliy
		}

		// Save the initial camera view		
		saveInitialView();

		// Create the buttons at the top
		createToolbar();

		drawWhole();

	}// end of initialize

	private void saveInitialView() {
		PCamera cam = getCanvas().getCamera();
		initialView = cam.getTransform();

	}

	/*
	 * Create a toolbar of buttons
	 */
	private void createToolbar() {
		JToolBar toolBar = new JToolBar();
		JButton reset = new JButton("Reset view");
		JToggleButton pan = new JToggleButton("Pan X only", true);
		JToggleButton zoom = new JToggleButton("Zoom X only", true);
		final JButton bswitch = new JButton("Switch to daytime activities");
		toolBar.add(reset);
		toolBar.add(pan);
		toolBar.add(zoom);
		toolBar.add(bswitch);
		toolBar.setFloatable(false);

		// Set zoom and pan x for now
		PCanvas c = getCanvas();
		c.setPanEventHandler(new HorizontalPanEventHandler());
		c.setZoomEventHandler(new HorizontalZoomEventHandler());

		// Behavior of reset button
		bswitch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				if (bswitch.getText() == "Switch to daytime activities") {
					xLabelNode.removeAllChildren();
					rootNode.removeAllChildren();
					layer.removeAllChildren();
					drawDay();
					bswitch.setText("Switch to wholetime activities");

				} else {
					xLabelNode.removeAllChildren();
					rootNode.removeAllChildren();
					layer.removeAllChildren();
					drawWhole();
					bswitch.setText("Switch to daytime activities");

				}

			}
		});

		// Behavior of reset button
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				getCanvas().getCamera().animateViewToTransform(initialView,
						1000);
			}
		});

		// Behavior of pan  button
		pan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				PCanvas c = getCanvas();
				JToggleButton button = (JToggleButton) ae.getSource();
				if (button.isSelected()) {
					c.setPanEventHandler(new HorizontalPanEventHandler());
				} else {
					c.setPanEventHandler(new PPanEventHandler());
				}

			}
		});

		// Behavior of zoom button
		zoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				PCanvas c = getCanvas();
				JToggleButton button = (JToggleButton) ae.getSource();
				if (button.isSelected()) {
					c.setZoomEventHandler(new HorizontalZoomEventHandler());
				} else {
					c.setZoomEventHandler(new PZoomEventHandler());
				}

			}
		});

		getContentPane().add(toolBar, BorderLayout.NORTH);
		getContentPane().validate();

	}

	public void drawDay() {

		drawDayGraph();
		setLegend();
		setTitle();

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

	public void drawDayGraph() {

		int j = 0;
		int m = 0;
		float interCommand = 0;
		float distanceMINUTE = 0;
		int blank = 100;

		blankList.clear();
		dateStartList.clear();

		// calculate the inter time between :
		// the beginning point of truth and command
		// the beginning point of truth and heuristic
		Date commandd1 = null;
		Date commandd2 = null;
		Date commandd3 = null;
		Date commandN = null;
		Date commandP = null;
		try {
			commandd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse(startTime.get(0));
			commandd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse(sStartTime.get(0));
			commandd3 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse(hstartTime.get(0));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		float commandInterTime = (float) ((commandd2.getTime() - commandd1
				.getTime()) / 1000 / 60);
		float xCommand = x0 + commandInterTime + 5;
		commandInterTime = (float) ((commandd3.getTime() - commandd1.getTime()) / 1000 / 60);
		float xHeuristicB = x0 + commandInterTime;

		// initialize cal
		cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(commandd1);

		dateStartList.add(startTime.get(0));

		///////////////////////////draw the command begin////////////////////////
		for (int i = 0; i < (sSeconds.size() - 1); i++) {

			try {
				commandd1 = new SimpleDateFormat("yyyy-MM-dd")
						.parse((String) sStartTime.get(i));
				commandd2 = new SimpleDateFormat("yyyy-MM-dd")
						.parse((String) sStartTime.get(i + 1));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			DrawCommandNode(xCommand, i);

			try {
				commandP = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse((String) sStartTime.get(i));
				commandN = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse((String) sStartTime.get(i + 1));

				interCommand = (float) ((commandN.getTime() - commandP
						.getTime()) / 1000 / 60);

			} catch (ParseException e2) {
				System.out.println("Parse command date error");
			}

			if (commandd2.equals(commandd1)) {//if two times are in the same day
				xCommand = xCommand + interCommand;

			} else {//the two times are in different day
				xCommand = (float) (xCommand + blank);

				blankList.add(new Float(xCommand));

				dateStartList.add(sStartTime.get(i + 1));

			}

			j = i;

		}//end of for	
		j = j + 1;
		DrawCommandNode(xCommand, j);
		///////////////////////////draw the command end//////////////////////////

		/*~~~~~~~~~~~~~~~~~~~~~~~draw the nuit marks and x labels begin~~~~~~~~~~~~~~~*/
		float originalXB = x0;
		float originalXE = 0;
		float mark = 0;
		float yXmark = y0 + 10;
		float yXlabel = y0 + 40;

		for (int i = 0; i < blankList.size(); i++) {
			//draw x axis line
			originalXE = Float.parseFloat(blankList.get(i).toString());
			PPath xPath = PPath.createLine(originalXB, y0, originalXE - blank,
					y0);
			rootNode.addChild(xPath);

			//draw the curly line
			float temp = originalXE - blank;
			xPath = PPath.createLine(temp, y0, temp + blank / 10, y0 - 5);
			rootNode.addChild(xPath);
			xPath = PPath.createLine(temp + blank / 10, y0 - 5, temp + blank
					* 3 / 10, y0 + 5);
			rootNode.addChild(xPath);
			xPath = PPath.createLine(temp + blank * 3 / 10, y0 + 5, temp
					+ blank * 5 / 10, y0 - 5);
			rootNode.addChild(xPath);
			xPath = PPath.createLine(temp + blank * 5 / 10, y0 - 5, temp
					+ blank * 7 / 10, y0 + 5);
			rootNode.addChild(xPath);
			xPath = PPath.createLine(temp + blank * 7 / 10, y0 + 5, temp
					+ blank * 9 / 10, y0 - 5);
			rootNode.addChild(xPath);
			xPath = PPath.createLine(temp + blank * 9 / 10, y0 - 5, temp
					+ blank, y0);
			rootNode.addChild(xPath);

			//draw marks and labels here
			try {
				commandN = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse(dateStartList.get(i).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Calendar calGraph = Calendar.getInstance();
			calGraph.clear();
			calGraph.setTime(commandN);

			int minute = calGraph.get(Calendar.MINUTE);
			int disToHour = 60 - minute;
			mark = originalXB + disToHour;

			while (mark <= (originalXE - blank)) {

				PPath unit = PPath.createLine(mark, y0, mark, y0 - 10);
				xLabelNode.addChild(unit);

				int month = calGraph.get(Calendar.MONTH);
				int day = calGraph.get(Calendar.DATE);
				int fistHour = calGraph.get(Calendar.HOUR_OF_DAY);
				int hour = (fistHour + 1) % 24;
				calGraph.set(Calendar.HOUR_OF_DAY, hour);

				calGraph.set(Calendar.MINUTE, 0);

				String timeString = (month + 1) + "/" + day;

				//draw the origin x axis label

				PTextStretched xLabel = new PTextStretched(String.valueOf(hour));
				xLabel.setJustification(javax.swing.JLabel.CENTER_ALIGNMENT);
				xLabel.setX(mark);
				xLabel.setY(yXmark);
				xLabel.setPaint(Color.white);
				xLabel.addAttribute("tooltip", calGraph.getTime().toString());
				xLabelNode.addChild(xLabel);

				xLabel = new PTextStretched(timeString);
				xLabel.setX(mark - 10);
				xLabel.setY(yXlabel);
				xLabel.setPaint(Color.white);
				xLabelNode.addChild(xLabel);

				//calculate the label hour used next time
				hour = (fistHour + 1) % 24;
				calGraph.set(Calendar.HOUR_OF_DAY, hour);

				mark = mark + 60;

			}

			originalXB = originalXE;

			j = i;
		}//end of for

		//draw the last  x axis segment. xCommand now is the last x coordinate		
		originalXE = xCommand + 50;
		PPath xPath = PPath.createLine(originalXB, y0, originalXE, y0);
		rootNode.addChild(xPath);

		try {
			commandN = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse(dateStartList.get(j + 1).toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calGraph = Calendar.getInstance();
		calGraph.clear();
		calGraph.setTime(commandN);

		int minute = calGraph.get(Calendar.MINUTE);
		int disToHour = 60 - minute;
		mark = originalXB + disToHour;

		while (mark <= (originalXE)) {

			PPath unit = PPath.createLine(mark, y0, mark, y0 - 10);
			xLabelNode.addChild(unit);

			int month = calGraph.get(Calendar.MONTH);
			int day = calGraph.get(Calendar.DATE);
			int fistHour = calGraph.get(Calendar.HOUR_OF_DAY);
			int hour = (fistHour + 1) % 24;
			calGraph.set(Calendar.HOUR_OF_DAY, hour);

			calGraph.set(Calendar.MINUTE, 0);

			String timeString = (month + 1) + "/" + day;

			//draw the origin x axis label

			PText xLabel = new PText(String.valueOf(hour));
			xLabel.setX(mark - 5);
			xLabel.setY(yXmark);
			xLabel.setPaint(Color.white);
			xLabel.addAttribute("tooltip", calGraph.getTime().toString());
			xLabelNode.addChild(xLabel);

			xLabel = new PText(timeString);
			xLabel.setX(mark - 10);
			xLabel.setY(yXlabel);
			xLabel.setPaint(Color.white);
			xLabelNode.addChild(xLabel);

			//calculate the label hour used next time
			hour = (fistHour + 1) % 24;
			calGraph.set(Calendar.HOUR_OF_DAY, hour);
			mark = mark + 60;

		}

		rootNode.addChild(xLabelNode);

		/*~~~~~~~~~~~~~~~~~~~~~~~draw the unit marks and x labels end~~~~~~~~~~~~~~~		
		 /*********************************draw the truth begin***************************/
		xx = x0;
		for (int i = 0; i < (totalTime.size() - 1); i++) {

			j = i;
			try {
				commandd1 = new SimpleDateFormat("yyyy-MM-dd")
						.parse((String) startTime.get(i));
				commandd2 = new SimpleDateFormat("yyyy-MM-dd")
						.parse((String) startTime.get(i + 1));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			distanceMINUTE = Float.parseFloat((String) totalTime.get(i));

			DrawTruthNode(xx, i, yTruth);

			if (commandd2.equals(commandd1)) {//if two times are in the same day

				xx = (float) (xx + distanceMINUTE);

			} else {//the two times are in different day
				xx = Float.parseFloat(blankList.get(m).toString());
				m = m + 1;

			}
		}//end of for	

		j = j + 1;
		// draw the last one
		DrawTruthNode(xx, j, yTruth);

		/**************************draw the truth end***************************/

		/*********************************draw the heuristic begin***************************/
		xx = xHeuristicB;
		m = 0;
		for (int i = 0; i < (htotalTime.size() - 1); i++) {

			j = i;
			try {
				commandd1 = new SimpleDateFormat("yyyy-MM-dd")
						.parse((String) hstartTime.get(i));
				commandd2 = new SimpleDateFormat("yyyy-MM-dd")
						.parse((String) hstartTime.get(i + 1));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			distanceMINUTE = Float.parseFloat((String) htotalTime.get(i));

			drawHeuristicNode(xx, i, yHuristic);

			if (commandd2.equals(commandd1)) {//if two times are in the same day

				xx = (float) (xx + distanceMINUTE);

			} else {//the two times are in different day
				//need improve here 
				xx = Float.parseFloat(blankList.get(m).toString());
				m = m + 1;

			}
		}//end of for	

		j = j + 1;
		// draw the last one

		drawHeuristicNode(xx, j, yHuristic);

		/**************************draw the heuristic end***************************/

		setYLabel(x0);
		setYaxis();

		// Add the y-axis to the camera, so it stays fixed
		getCanvas().getCamera().addChild(yLabelNode);

		layer.addChild(rootNode);

	}

	public void setYaxis() {
		yy = 100;
		PNode node = getCanvas().getCamera();
		node.addChild(PPath.createLine(x0, y0, x0, yy));
		node.addChild(PPath.createLine(x0 - 5, yy + 10, x0, yy));
		node.addChild(PPath.createLine(x0, yy, x0 + 5, yy + 10));

	}

	public void dashedLine(float xdash1, float ydash1, float xdash2,
			float ydash2) {
		PPath line = new PPath();
		float dash[] = { 10.0f };
		Stroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		line = PPath.createLine(xdash1, ydash1, xdash2, ydash2);
		line.setStroke(stroke);
		rootNode.addChild(line);

	}

	public void setDashedLine(boolean b) {
		if (b == true) {
			Float x1, x2, y1, y2;
			y1 = y0;
			y2 = yy;
			for (int i = 0; i < blankList.size(); i++) {
				x1 = x2 = blankList.get(i);
				dashedLine(x1, y1, x2, y2);

			}
		} else {

		}

	}

	public void drawWhole() {

		drawWholeGraph();

		setLegend();
		setTitle();

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

	public void drawWholeGraph() {

		float xCommand = 0;
		//calculate the inter time between the beginning point of truth and command
		// calculate the inter time between :
		// the beginning point of truth and command
		// the beginning point of truth and heuristic
		Date commandd1 = null;
		Date commandd2 = null;
		Date commandd3 = null;
		Date commandN = null;
		Date commandP = null;
		try {
			commandd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse((String) startTime.get(0));
			commandd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse((String) sStartTime.get(0));
			commandd3 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse((String) hstartTime.get(0));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		float commandInterTime = (float) ((commandd2.getTime() - commandd1
				.getTime()) / 1000 / 60);
		xCommand = x0 + commandInterTime + 5;
		commandInterTime = (float) ((commandd3.getTime() - commandd1.getTime()) / 1000 / 60);
		float xHuristicB = x0 + commandInterTime;

		//initialize cal
		cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(commandd1);
		/*****************************draw truth graph begin***************************************************/
		//draw the activity bar
		float xx = x0;
		for (int i = 0; i < (totalTime.size()); i++) {

			DrawTruthNode(xx, i, yTruth);

			//calculate the growed xAis.
			if (i < (activityLineNumber - 1)) {
				try {
					Date d1 = new Date();
					Date d2 = new Date();
					d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.parse((String) startTime.get(i + 1));

					d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.parse((String) startTime.get(i));

					float distanceMINUTE = (float) ((d1.getTime() - d2
							.getTime()) / 1000 / 60);

					xx = (float) (xx + distanceMINUTE);
				} catch (ParseException e2) {
					System.out.println("Parse activity date error");
				}
			} else {

				continue;
			}

		}

		/*****************************draw truth graph end****************************************************/
		/*****************************draw heuristic graph begin***************************************************/
		//draw the activity bar
		xx = xHuristicB;
		for (int i = 0; i < (htotalTime.size()); i++) {

			drawHeuristicNode(xx, i, yHuristic);

			//calculate the growed xAis.
			if (i < (htotalTime.size() - 1)) {
				try {
					Date d1 = new Date();
					Date d2 = new Date();
					d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.parse((String) hstartTime.get(i + 1));

					d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.parse((String) hstartTime.get(i));

					float distanceMINUTE = (float) ((d1.getTime() - d2
							.getTime()) / 1000 / 60);

					xx = (float) (xx + distanceMINUTE);
				} catch (ParseException e2) {
					System.out.println("Parse heuristic start date error");
				}
			} else {

				continue;
			}

		}

		/*****************************draw heuristic graph end****************************************************/
		////////////////////////////draw sequence graph begin////////////////////////////////////////////////////////

		for (int i = 0; i < (sSeconds.size()); i++) {

			DrawCommandNode(xCommand, i);

			if (i < (sSeconds.size() - 1)) {
				try {
					commandP = new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.parse((String) sStartTime.get(i));
					commandN = new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.parse((String) sStartTime.get(i + 1));

					float interCommand = (float) ((commandN.getTime() - commandP
							.getTime()) / 1000 / 60);

					xCommand = xCommand + interCommand;

				} catch (ParseException e2) {
					System.out.println("Parse sequence start date error");
				}

			} else {
				xCommand = xCommand + 0;
			}

		}//end of for	

		/////////////////////////////////////draw sequence graph end//////////////////////////////////////////////////////////////    	

		//the last command's x axis 
		xCommandTotal = xCommand;

		//x and y axis 
		setYLabel(x0);
		setYaxis();
		drawXAxis();
		setXlabelandMask();

		// Add the y-axis to the camera, so it stays fixed
		getCanvas().getCamera().addChild(yLabelNode);

		rootNode.addChild(PPath.createLine(x0, y0, xCommandTotal + 100, y0));// draw x axis

		rootNode.addChild(xLabelNode);
		recoverNode = rootNode;
		layer.addChild(rootNode);

	}

	public void DrawTruthNode(float xOriginal, int i, float yTruth) {

		PNode truthNode = new PNode();
		double interTime = Double.valueOf((String) totalTime.get(i))
				.doubleValue();
		float dis = Float.valueOf((String) totalTime.get(i));
		truthNode.setBounds(xOriginal, yTruth, interTime - 0.5, h);

		activityTime += interTime;
		PPath line = PPath.createLine(xOriginal + dis, yTruth + 1, xOriginal
				+ dis, yTruth + h - 1);
		line.setPaint(Color.gray);
		rootNode.addChild(line);

		if (Activity.get(i).equals("Th")) {
			thTime += interTime;
			truthNode.setPaint(Color.YELLOW);
			truthNode.addAttribute("tooltip", "Thinking" + " "
					+ (String) startTime.get(i) + " "
					+ (String) totalTime.get(i) + "minutes");

		} else if (Activity.get(i).equals("Se")) {
			seTime += interTime;
			truthNode.setPaint(Color.GREEN);
			truthNode.addAttribute("tooltip", "Serial coding" + " "
					+ (String) startTime.get(i) + " "
					+ (String) totalTime.get(i) + "minutes");

		} else if (Activity.get(i).equals("Pa")) {
			paTime += interTime;
			truthNode.setPaint(Color.RED);
			truthNode.addAttribute("tooltip", "Parallelizing the code" + " "
					+ (String) startTime.get(i) + " "
					+ (String) totalTime.get(i) + "minutes");

		} else if (Activity.get(i).equals("Te")) {
			teTime += interTime;
			truthNode.setPaint(Color.BLUE);
			truthNode.addAttribute("tooltip", "Testing" + " "
					+ (String) startTime.get(i) + " "
					+ (String) totalTime.get(i) + "minutes");
		} else if (Activity.get(i).equals("De")) {
			deTime += interTime;
			truthNode.setPaint(Color.PINK);
			truthNode.addAttribute("tooltip", "Debugging" + " "
					+ (String) startTime.get(i) + " "
					+ (String) totalTime.get(i) + "minutes");
		} else if (Activity.get(i).equals("Tu")) {
			tuTime += interTime;
			truthNode.setPaint(Color.MAGENTA);
			truthNode.addAttribute("tooltip", "Tuning" + " "
					+ (String) startTime.get(i) + " "
					+ (String) totalTime.get(i) + "minutes");
		} else if (Activity.get(i).equals("Ex")) {
			exTime += interTime;
			truthNode.setPaint(Color.ORANGE);
			truthNode.addAttribute("tooltip", "Experimenting with envent" + " "
					+ (String) startTime.get(i) + " "
					+ (String) totalTime.get(i) + "minutes");
		} else if (Activity.get(i).equals("Ot")) {
			otTime += interTime;
			truthNode.setPaint(Color.CYAN);
			truthNode.addAttribute("tooltip", "Other" + " "
					+ (String) startTime.get(i) + " "
					+ (String) totalTime.get(i) + "minutes");
		} else {
			System.out
					.println("No such activity code.Please check the category in avtivity file.");

		}
		rootNode.addChild(truthNode);

	}

	public void drawHeuristicNode(float xOriginal, int i, float yTruth) {

		PNode truthNode = new PNode();
		double interTime = Double.valueOf((String) htotalTime.get(i))
				.doubleValue();
		float dis = Float.valueOf((String) htotalTime.get(i));
		truthNode.setBounds(xOriginal, yTruth, interTime - 0.5, h);

		hactivityTime += interTime;
		PPath line = PPath.createLine(xOriginal + dis, yTruth + 1, xOriginal
				+ dis, yTruth + h - 1);
		line.setPaint(Color.gray);
		rootNode.addChild(line);

		if (hActivity.get(i).equals("Th")) {
			hthTime += interTime;
			truthNode.setPaint(Color.YELLOW);
			truthNode.addAttribute("tooltip", "Thinking" + " "
					+ (String) hstartTime.get(i) + " "
					+ (String) htotalTime.get(i) + "minutes");

		} else if (hActivity.get(i).equals("Se")) {
			hseTime += interTime;
			truthNode.setPaint(Color.GREEN);
			truthNode.addAttribute("tooltip", "Serial coding" + " "
					+ (String) hstartTime.get(i) + " "
					+ (String) htotalTime.get(i) + "minutes");

		} else if (hActivity.get(i).equals("Pa")) {
			hpaTime += interTime;
			truthNode.setPaint(Color.RED);
			truthNode.addAttribute("tooltip", "Parallelizing the code" + " "
					+ (String) hstartTime.get(i) + " "
					+ (String) htotalTime.get(i) + "minutes");

		} else if (hActivity.get(i).equals("Te")) {
			hteTime += interTime;
			truthNode.setPaint(Color.BLUE);
			truthNode.addAttribute("tooltip", "Testing" + " "
					+ (String) hstartTime.get(i) + " "
					+ (String) htotalTime.get(i) + "minutes");
		} else if (hActivity.get(i).equals("De")) {
			hdeTime += interTime;
			truthNode.setPaint(Color.PINK);
			truthNode.addAttribute("tooltip", "Debugging" + " "
					+ (String) hstartTime.get(i) + " "
					+ (String) htotalTime.get(i) + "minutes");
		} else if (hActivity.get(i).equals("Tu")) {
			htuTime += interTime;
			truthNode.setPaint(Color.MAGENTA);
			truthNode.addAttribute("tooltip", "Tuning" + " "
					+ (String) hstartTime.get(i) + " "
					+ (String) htotalTime.get(i) + "minutes");
		} else if (hActivity.get(i).equals("Ex")) {
			hexTime += interTime;
			truthNode.setPaint(Color.ORANGE);
			truthNode.addAttribute("tooltip", "Experimenting with envent" + " "
					+ (String) hstartTime.get(i) + " "
					+ (String) htotalTime.get(i) + "minutes");
		} else if (hActivity.get(i).equals("Ot")) {
			hotTime += interTime;
			truthNode.setPaint(Color.CYAN);
			truthNode.addAttribute("tooltip", "Other" + " "
					+ (String) hstartTime.get(i) + " "
					+ (String) htotalTime.get(i) + "minutes");
		} else {
			System.out
					.println("No such activity code.Please check category in heuristic file.");

		}
		rootNode.addChild(truthNode);

	}

	public void DrawCommandNode(float xCommand, int i) {
		PPath commandNode = new PPath();
		Object hhh = sStopTime.get(i);

		if (hhh.equals("0")) {

			if (sCommCateAndEvent.get(i).equals("filecommand")) {
				commandNode = DataPoint.createDataPoint(xCommand,
						ycommandHeight - blank * 0, radius, radius);

				commandNode.setPaint(Color.red);
				commandNode.setStroke(null);
				commandNode.addAttribute("tooltip", (String) sCommandAndAddNum
						.get(i)
						+ " "
						+ (String) sCommandDetailAndChangNum.get(i)
						+ " "
						+ (String) sStartTime.get(i));
			} else if (sCommCateAndEvent.get(i).equals("building")) {
				commandNode = DataPoint.createDataPoint(xCommand,
						ycommandHeight - blank * 1, radius, radius);
				commandNode.setPaint(Color.green);
				commandNode.setStroke(null);
				commandNode.addAttribute("tooltip", (String) sCommandAndAddNum
						.get(i)
						+ " "
						+ (String) sCommandDetailAndChangNum.get(i)
						+ " "
						+ (String) sStartTime.get(i));
			} else if (sCommCateAndEvent.get(i).equals("process")) {
				commandNode = DataPoint.createDataPoint(xCommand,
						ycommandHeight - blank * 2, radius, radius);
				commandNode.setPaint(Color.black);
				commandNode.setStroke(null);
				commandNode.addAttribute("tooltip", (String) sCommandAndAddNum
						.get(i)
						+ " "
						+ (String) sCommandDetailAndChangNum.get(i)
						+ " "
						+ (String) sStartTime.get(i));
			} else if (sCommCateAndEvent.get(i).equals("documentation")) {
				commandNode = DataPoint.createDataPoint(xCommand,
						ycommandHeight - blank * 3, radius, radius);
				commandNode.setPaint(Color.blue);
				commandNode.setStroke(null);
				commandNode.addAttribute("tooltip", (String) sCommandAndAddNum
						.get(i)
						+ " "
						+ (String) sCommandDetailAndChangNum.get(i)
						+ " "
						+ (String) sStartTime.get(i));
			} else if (sCommCateAndEvent.get(i).equals("running")) {
				commandNode = DataPoint.createDataPoint(xCommand,
						ycommandHeight - blank * 4, radius, radius);
				commandNode.setPaint(Color.cyan);
				commandNode.setStroke(null);
				commandNode.addAttribute("tooltip", (String) sCommandAndAddNum
						.get(i)
						+ " "
						+ (String) sCommandDetailAndChangNum.get(i)
						+ " "
						+ (String) sStartTime.get(i));
			} else if (sCommCateAndEvent.get(i).equals("viewing")) {
				commandNode = DataPoint.createDataPoint(xCommand,
						ycommandHeight - blank * 5, radius, radius);
				commandNode.setPaint(Color.magenta);
				commandNode.setStroke(null);
				commandNode.addAttribute("tooltip", (String) sCommandAndAddNum
						.get(i)
						+ " "
						+ (String) sCommandDetailAndChangNum.get(i)
						+ " "
						+ (String) sStartTime.get(i));
			} else if (sCommCateAndEvent.get(i).equals("editing")) {
				commandNode = DataPoint.createDataPoint(xCommand,
						ycommandHeight - blank * 6, radius, radius);
				commandNode.setPaint(Color.orange);
				commandNode.setStroke(null);
				commandNode.addAttribute("tooltip", (String) sCommandAndAddNum
						.get(i)
						+ " "
						+ (String) sCommandDetailAndChangNum.get(i)
						+ " "
						+ (String) sStartTime.get(i));
			} else if (sCommCateAndEvent.get(i).equals("other")) {
				commandNode = DataPoint.createDataPoint(xCommand,
						ycommandHeight - blank * 7, radius, radius);
				commandNode.setPaint(Color.pink);
				commandNode.setStroke(null);
				commandNode.addAttribute("tooltip", (String) sCommandAndAddNum
						.get(i)
						+ " "
						+ (String) sCommandDetailAndChangNum.get(i)
						+ " "
						+ (String) sStartTime.get(i));
			}

			rootNode.addChild(commandNode);

		} else {

			String event = (String) sCommCateAndEvent.get(i);

			float xEditStateE = xCommand;

			PPath diffDetailNode = new PPath();
			float addNum = Float.parseFloat((String) sCommandAndAddNum.get(i));
			float changNum = Float
					.parseFloat((String) sCommandDetailAndChangNum.get(i));
			float delNum = Float.parseFloat((String) sDelNum.get(i));
			if (addNum == 0) {

			} else {
				GetSubString(event, "add");
				diffDetailNode = PPath.createRectangle(xEditStateE,
						ycommandHeight - blank * 10, radius, radius);
				diffDetailNode.setPaint(Color.red);
				diffDetailNode.setStroke(null);
				diffDetailNode.addAttribute("tooltip",
						(String) sCommandAndAddNum.get(i) + "lines" + " "
								+ editDetail);
				rootNode.addChild(diffDetailNode);
			}
			if (changNum == 0) {

			} else {
				GetSubString(event, "change");
				diffDetailNode = PPath.createRectangle(xEditStateE,
						ycommandHeight - blank * 12, radius, radius);
				diffDetailNode.setPaint(Color.green);
				diffDetailNode.setStroke(null);
				diffDetailNode.addAttribute("tooltip",
						(String) sCommandDetailAndChangNum.get(i) + "lines"
								+ " " + editDetail);
				rootNode.addChild(diffDetailNode);
			}
			if (delNum == 0) {

			} else {
				GetSubString(event, "delete");
				diffDetailNode = PPath.createRectangle(xEditStateE,
						ycommandHeight - blank * 14, radius, radius);
				diffDetailNode.setPaint(Color.blue);
				diffDetailNode.setStroke(null);
				diffDetailNode.addAttribute("tooltip", (String) sDelNum.get(i)
						+ "lines" + " " + editDetail);
				rootNode.addChild(diffDetailNode);
			}

		}
	}

	public void setTitle() {
		float xTitle = 250;
		float yTitle = 15;
		PText titleText = new PText("ActivitiesGraph");
		titleText.setX(xTitle);
		titleText.setY(yTitle);
		titleText.scale(2);
		getCanvas().getCamera().addChild(titleText);

	}

	public void setLegend() {
		float yLegend = y0 + 70;
		float yTruthLegend = y0 + 85;
		float yHeuristicLegend = y0 + 100;
		float wideth = 20;
		String truthPercent;
		String heuristicPercent;
		NumberFormat percentFormat = NumberFormat.getPercentInstance();

		String[] truth = { "Thinking", "Serial coding",
				"Parallelizing the code", "Testing", "Debuging", "Tuning",
				"Experimenting", "Other" };
		Color[] colorTruth = { Color.YELLOW, Color.GREEN, Color.RED,
				Color.BLUE, Color.PINK, Color.MAGENTA, Color.ORANGE, Color.CYAN };
		Double[] truthPercentage = { thTime / activityTime,
				seTime / activityTime, paTime / activityTime,
				teTime / activityTime, deTime / activityTime,
				tuTime / activityTime, exTime / activityTime,
				otTime / activityTime };
		Double[] heuristicPercentage = { hthTime / hactivityTime,
				hseTime / hactivityTime, hpaTime / hactivityTime,
				hteTime / hactivityTime, hdeTime / hactivityTime,
				htuTime / hactivityTime, hexTime / hactivityTime,
				hotTime / hactivityTime };

		PNode node = getCanvas().getCamera();

		float startx = x0;
		float blank = 40;

		PText truthPos = new PText("Truth");
		PText heuristicPos = new PText("Heuristic");
		truthPos.setX(startx - truthPos.getWidth());
		truthPos.setY(yTruthLegend);
		heuristicPos.setX(startx - heuristicPos.getWidth());
		heuristicPos.setY(yHeuristicLegend);
		node.addChild(truthPos);
		node.addChild(heuristicPos);

		//draw legend and persent
		for (int i = 0; i < truth.length; i++) {
			PNode legendNode = new PNode();
			PText legendText = new PText(truth[i]);
			truthPercent = "(" + percentFormat.format(truthPercentage[i]) + ")";
			PText truthLegentPercent = new PText(truthPercent);
			heuristicPercent = "("
					+ percentFormat.format(heuristicPercentage[i]) + ")";
			PText heuristicLegentPercent = new PText(heuristicPercent);
			legendNode.setBounds(startx, yLegend, wideth, h);
			legendNode.setPaint(colorTruth[i]);
			legendText.setX(startx + wideth);
			legendText.setY(yLegend);
			truthLegentPercent.setX(startx + wideth);
			truthLegentPercent.setY(yTruthLegend);
			heuristicLegentPercent.setX(startx + wideth);
			heuristicLegentPercent.setY(yHeuristicLegend);

			node.addChild(legendNode);
			node.addChild(legendText);
			node.addChild(truthLegentPercent);
			node.addChild(heuristicLegentPercent);
			startx = startx + wideth + (float) legendText.getWidth() + blank;
		}

	}

	public void drawXAxis() {

		Calendar first = getEarliestEventTime();
		Calendar last = getLatestEventTime();

		// Fast forward last up to the next hour if it's not already at an hour
		if (last.get(Calendar.MINUTE) != 0) {
			last.add(Calendar.MINUTE, 60 - last.get(Calendar.MINUTE));

		}

		Calendar cal;
		float xval;
		for (xval = x0, cal = (Calendar) first.clone(); cal.before(last)
				|| cal.equals(last); cal.add(Calendar.MINUTE, 1), ++xval) {
			PPath tick = TickMark.createTick(xval, y0, cal);
			xLabelNode.addChild(tick);
			TickLabel lab = TickLabel.createLabel(xval, y0 + xTickLabelYOffset,
					cal);
			xLabelNode.addChild(lab);
		}
	}

	public void setXlabelandMask() {

		//draw the first mark

		int minute = cal.get(Calendar.MINUTE);
		int disToHour = 60 - minute;
		float firstMark = x0 + disToHour;
		PPath unit = TickMark.createHourTick(firstMark, y0);
		xLabelNode.addChild(unit);
		//calculate the mark used next time
		float Mark = firstMark + 60;

		//get the first date and draw origin x label

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int fistHour = cal.get(Calendar.HOUR_OF_DAY);

		cal.set(Calendar.HOUR_OF_DAY, fistHour);
		cal.set(Calendar.MINUTE, 0);

		String firstTimeString = year + "-" + (month + 1) + "-" + day;

		//draw the origin x axis label

		PTextStretched xLabel = new PTextStretched(String.valueOf(fistHour));
		xLabel.setX(x0 - 5);
		xLabel.setY(y0 + 10);
		xLabel.setPaint(Color.white);
		xLabel.addAttribute("tooltip", cal.getTime().toString());
		xLabelNode.addChild(xLabel);

		xLabel = new PTextStretched(firstTimeString);
		xLabel.setX(x0 - 10);
		xLabel.setY(y0 + 50);
		xLabel.setPaint(Color.white);
		xLabelNode.addChild(xLabel);

		//calculate the label hour used next time
		int hour = (fistHour + 1) % 24;
		cal.set(Calendar.HOUR_OF_DAY, hour);

		String TimeString = year + "-" + (month + 1) + "-" + day;

		// draw the first x axis label.
		if (hour != 0) {// time is not long enough to be one day

			xLabel = new PTextStretched(String.valueOf(hour));
			xLabel.setX(firstMark - 5);
			xLabel.setY(y0 + 10);
			xLabel.setPaint(Color.white);
			xLabel.addAttribute("tooltip", cal.getTime().toString());
			xLabelNode.addChild(xLabel);

		} else {// time is one day and the date should add 1

			cal.add(Calendar.DATE, 1);
			day = cal.get(Calendar.DATE);
			month = cal.get(Calendar.MONTH);
			year = cal.get(Calendar.YEAR);
			TimeString = year + "-" + (month + 1) + "-" + day;

			xLabel = new PTextStretched(String.valueOf(hour));
			xLabel.setX(firstMark - 5);
			xLabel.setY(y0 + 10);
			xLabel.setPaint(Color.white);
			xLabel.addAttribute("tooltip", cal.getTime().toString());
			xLabelNode.addChild(xLabel);

			xLabel = new PTextStretched(TimeString);
			xLabel.setX(firstMark - 10);
			xLabel.setY(y0 + 50);
			xLabel.setPaint(Color.white);
			xLabelNode.addChild(xLabel);

		}

		//calculate the label used next time
		hour = (hour + 1) % 24;
		cal.set(Calendar.HOUR_OF_DAY, hour);

		// Mark is the unit in minutes		
		//draw the marks and labels except the first ones
		while (Mark <= xCommandTotal + 100) {
			if (hour != 0) {

				PPath unit1 = TickMark.createHourTick(Mark, y0);
				rootNode.addChild(unit1);

			} else {
				PPath unit1 = TickMark.createDayTick(Mark, y0);
				rootNode.addChild(unit1);

			}

			if (hour != 12) {//time is not long enough to be one day

				xLabel = new PTextStretched(String.valueOf(hour));
				xLabel.setX(Mark - 5);
				xLabel.setY(y0 + 10);
				xLabel.setPaint(Color.white);
				xLabel.addAttribute("tooltip", cal.getTime().toString());
				xLabelNode.addChild(xLabel);

			} else {//time is one day and the date should add 1

				cal.add(Calendar.DATE, 1);
				day = cal.get(Calendar.DATE);
				month = cal.get(Calendar.MONTH);
				year = cal.get(Calendar.YEAR);
				TimeString = year + "-" + (month + 1) + "-" + day;

				xLabel = new PTextStretched(String.valueOf(hour));
				xLabel.setX(Mark - 5);
				xLabel.setY(y0 + 10);
				xLabel.setPaint(Color.white);
				xLabel.addAttribute("tooltip", cal.getTime().toString());
				xLabelNode.addChild(xLabel);

				xLabel = new PTextStretched(TimeString);
				xLabel.setX(Mark - 10);
				xLabel.setY(y0 + 50);
				xLabel.setPaint(Color.white);
				xLabelNode.addChild(xLabel);

			}

			Mark = Mark + 60;
			hour = (hour + 1) % 24;
			cal.set(Calendar.HOUR_OF_DAY, hour);

		}
		rootNode.addChild(xLabelNode);

	}

	public void setYLabel(double xPos) {
		PText yLabel = new PText("Heuristic");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yHuristic);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("Truth");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yTruth);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("filecommand");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 5);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("building");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 25);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("process");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 45);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("documentation");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 65);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("running");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 85);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("viewing");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 105);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("editing");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 125);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("other");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 145);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("added number");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 205);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("changed number");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 245);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);

		yLabel = new PText("deleted number");
		yLabel.setX(xPos - yLabel.getWidth() - 1);
		yLabel.setY(yCommandTotal - 285);
		yLabel.setPaint(Color.white);
		yLabelNode.addChild(yLabel);
	}

	public void parseActivityfile(String activityFile) {
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(activityFile));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return;
		}
		String[] nextLine = new String[10];

		try {
			while ((nextLine = reader.readNext()) != null) {

				startTime.add(nextLine[0]);
				stopTime.add(nextLine[1]);
				totalTime.add(nextLine[2]);
				Activity.add(nextLine[3]);
				activityLineNumber++;

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

	}

	public void parseHeuristicfile(String heuristicFile) {
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(heuristicFile));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return;
		}
		String[] nextLine = new String[10];

		try {
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine.length < 2) {
					sequenceLineNumber++;
					continue;
				}

				hstartTime.add(nextLine[0]);
				hstopTime.add(nextLine[1]);
				htotalTime.add(nextLine[2]);
				hActivity.add(nextLine[3]);
				heuristicNumber++;

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void parseSequencefile(String sequenceFile) {
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(sequenceFile));

		} catch (FileNotFoundException e) {

			System.out.println("parseing sequence csv file error");
			e.printStackTrace();
			return;
		}
		String[] nextLine = new String[10];

		try {
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine.length < 6) {
					sequenceLineNumber++;
					continue;
				}

				sSeconds.add(nextLine[0]);
				sStartTime.add(nextLine[1]);
				sStopTime.add(nextLine[2]);
				sTotalTime.add(nextLine[3]);
				sCommCateAndEvent.add(nextLine[4]);
				sCommandAndAddNum.add(nextLine[5]);
				sCommandDetailAndChangNum.add(nextLine[6]);
				sDelNum.add(nextLine[7]);

				sequenceLineNumber++;

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

	}

	public void GetSubString(String Srcstring, String subString) {
		int n = 0;
		int m = 0;
		String detString = "";

		while ((n = Srcstring.indexOf(subString, m)) != -1) {

			m = Srcstring.indexOf("  ", n);
			detString += Srcstring.substring(n, m) + "  ";

		}
		editDetail = detString;

	}

	public static void main(String[] args) {

		new DrawGraph();

	}

}
