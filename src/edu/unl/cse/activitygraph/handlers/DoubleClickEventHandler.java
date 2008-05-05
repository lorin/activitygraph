package edu.unl.cse.activitygraph.handlers;


import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PText;
import edu.unl.cse.activitygraph.Series;
import edu.unl.cse.activitygraph.nodes.SeriesGroupNode;

/**
 * Handles double-clicking on series groups
 *
 */
public class DoubleClickEventHandler extends PBasicInputEventHandler {

	private SeriesGroupNode seriesGroupNode;
	private Series series;
	private boolean visibleBoolean;
	
	public DoubleClickEventHandler(SeriesGroupNode seriesGroupNode,Series series) {
		this.seriesGroupNode = seriesGroupNode;
		this.series=series;
		this.visibleBoolean=true;
	}

	@Override
	public void mouseClicked(PInputEvent evt) {
		if(evt.getClickCount()==2)
		{
			this.seriesGroupNode.toggle();
			
				PText yLabel = series.yLabel;	
			
				if (visibleBoolean==true){
					visibleBoolean=false;
					yLabel.setVisible(false);
				}else{
					visibleBoolean=true;
					yLabel.setVisible(true);
					
				}
			
		}
		super.mouseClicked(evt);
	}
	
}
