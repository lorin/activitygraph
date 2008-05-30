package edu.unl.cse.activitygraph;

import java.util.LinkedList;
import java.util.List;

import edu.unl.cse.activitygraph.interfaces.ITimedEvent;

public abstract class AbstractTimedEvent implements ITimedEvent {
	
	private List<ITimedEvent> forwardLinks;
	private List<ITimedEvent> reverseLinks;
	
	protected AbstractTimedEvent() {
		forwardLinks = new LinkedList<ITimedEvent>();
		reverseLinks = new LinkedList<ITimedEvent>();
	}

	public List<ITimedEvent> getForwardLinks() {
		return forwardLinks;
	}

	public void addForwardLink(AbstractTimedEvent event) {
		forwardLinks.add(event);		
	}

	public List<ITimedEvent> getReverseLinks() {
		return reverseLinks;
	}

	public void addReverseLink(ITimedEvent event) {
		reverseLinks.add(event);
	}

	public static void addLinks(AbstractTimedEvent source, AbstractTimedEvent target) {
		source.addForwardLink(target);
		target.addReverseLink(source);
		
	}

}
