package edu.unl.cse.activitygraph;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import edu.unl.cse.activitygraph.interfaces.ITimedEvent;
import edu.unl.cse.activitygraph.util.CoordMapper;




/**
 * 
 * ShiftCalculator is a class which calculates how far the nodes should be
 * shifted to the left and which ticks should be hidden when the x-axis is 
 * compressed to just show active events. 
 *
 */
public class ShiftCalculator {
	
	@SuppressWarnings("serial")
	public class ShiftCalculatorEmptyException extends RuntimeException {
		
	}

	
	
	/*
	 * Internal class used for tracking intervals where developer is active
	 */
	static class WorkInterval{

		private Date startTime;
		private Date endTime;
		

		public WorkInterval(ITimedEvent event) {
			this.startTime = (Date) event.getStartTime().clone();
			this.endTime = (Date) event.getEndTime().clone();
		}
		
		public WorkInterval(Date startTime, Date endTime) {
			this.startTime = startTime;
			this.endTime = endTime;
		}
		
		public void addFirstEvent(ITimedEvent event){
			this.startTime=(Date) event.getStartTime().clone();
		}
		public void addLastEvent(ITimedEvent event){
			this.endTime=(Date) event.getEndTime().clone();
		}
		public Date GetStartTime(){
			return this.startTime;
		}
		
		@Override
		public String toString() {
			
			return "[" + String.format("%tD %tR",startTime,startTime) + "," + 
			             String.format("%tD %tR",endTime,endTime) + "]";
			
		}


		@Override
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((endTime == null) ? 0 : endTime.hashCode());
			result = PRIME * result + ((startTime == null) ? 0 : startTime.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final WorkInterval other = (WorkInterval) obj;
			if (endTime == null) {
				if (other.endTime != null)
					return false;
			} else if (!endTime.equals(other.endTime))
				return false;
			if (startTime == null) {
				if (other.startTime != null)
					return false;
			} else if (!startTime.equals(other.startTime))
				return false;
			return true;
		}
		
		
	}
	
	protected Date firstEvent = null;

	private int paddingMinutes;
	private int bigGapMinutes;
	
	private LinkedList<WorkInterval> workIntervals;


	/*
	 * @param bigGapMinutes the minimum gap size that should be compressed, in minutes
	 * @param paddingMinutes the amount of padding to be used when compressing, in minutes
	 */
	public ShiftCalculator(int bigGapMinutes, int paddingMinutes) {
		this.bigGapMinutes = bigGapMinutes; 
		this.paddingMinutes = paddingMinutes;
		this.workIntervals = new LinkedList<WorkInterval>();
	}
	
	/*
	 * Returns the intervals that contain activity.  
	 * 
	 * @return a list of the work intervals.  
	 */
	public List<WorkInterval> getWorkIntervals() {
		return this.workIntervals;
	}
	
	/**
	 * Add an event to the shift calculator. 
	 * @param event the event to be added
	 */
	public void addEvent(ITimedEvent event) {
		if(this.firstEvent==null || event.getStartTime().before(this.firstEvent)) {
			this.firstEvent = (Date)event.getStartTime().clone();
		}

		if(this.workIntervals.isEmpty()) {
			this.workIntervals.add(new WorkInterval(event));
		} else {
			/*
			 * Possibilities:
			 *  1. Event is before first work interval
			 *  2. Event is after last work interval
			 *  3. if event start from gap
			 *     two branches: *event start from gap and end in gap(including whole event is within gap)
			 *     				 *event start from gap and end in interval 
			 *  4. if event start from the interval
			 *     two branches: *event start from interval end in interval(including whole event is withing the interval)
			 *     				 *event start from interval end in gap
			 */  
			   
			Date eventStartTime = (Date) event.getStartTime().clone();
			Date eventEndTime = (Date) event.getEndTime().clone();
			Date workIntervalsStartTime = (Date) this.workIntervals.getFirst().startTime.clone();
			Date workIntervalsEndTime = (Date) this.workIntervals.getLast().endTime.clone();
			
			
			//  Case 1: (before *first*)
			//  Including that the event is on the front edge of first work interval
			
			if(eventEndTime.getTime()<=workIntervalsStartTime.getTime()){
				long gap = (workIntervalsStartTime.getTime()-eventEndTime.getTime())/1000/60;
				
				/*   If *event* is  more than bigGapMinutes before *first*, create 
				 *       a new work interval, insert it before *first*
				 */
				/*   If *event* is within or equal to bigGapMinutes, extend *first* to 
				 *      include *event*
				 */
				
				if(gap>this.bigGapMinutes){
					this.workIntervals.addFirst(new WorkInterval(event));
				}
				else{
					this.workIntervals.getFirst().addFirstEvent(event);
				}
				
			}
			 
			    
			 //  Case 2: (after *last*)
			//   Including that the evnet is on the end edge of last work interval
			if(eventStartTime.getTime()>=workIntervalsEndTime.getTime()){
				 long gap = (eventStartTime.getTime()-workIntervalsEndTime.getTime())/1000/60;
				 /*
				  * If *event* is more than bigGapMinutes after *last*,
				  * create a new work interval, insert it after *last*
				  * 
				  * If *event* is within or equal to bigGapMinutes, extend *last* to
				  * incldue *event*
				  */  
					if(gap>this.bigGapMinutes){
						this.workIntervals.addLast(new WorkInterval(event));
					}
					else{
						this.workIntervals.getLast().addLastEvent(event);
					}
					
			}
				
			
			 
		//  Case 3: (between *prev* and *next)
			else{
		
				int loop1Num=0;
				int j;
				for(loop1Num=0;loop1Num<(this.workIntervals.size()-1);loop1Num++){
					long wIEndTimeForNum1 = this.workIntervals.get(loop1Num).endTime.getTime();
					long wIStartTimeForNum1 = this.workIntervals.get(loop1Num).startTime.getTime();
					//event start from gap
					int loop2Num=loop1Num+1;
					if((eventStartTime.getTime()>this.workIntervals.get(loop1Num).endTime.getTime())&(eventStartTime.getTime()<this.workIntervals.get(loop2Num).startTime.getTime())){
						
						for(loop2Num=loop1Num+1;loop2Num<(this.workIntervals.size());loop2Num++){
							long wIEndTimeForNum2 = this.workIntervals.get(loop2Num).endTime.getTime();
							long wIStartTimeForNum2 = this.workIntervals.get(loop2Num).startTime.getTime();
							//event end in gap START
							if((eventEndTime.getTime()<wIStartTimeForNum2)&&(eventEndTime.getTime()>this.workIntervals.get(loop2Num-1).endTime.getTime())){
								long gapToPrev=(eventStartTime.getTime()-wIEndTimeForNum1)/1000/60;
								long gapToNext=	(wIStartTimeForNum2-eventEndTime.getTime())/1000/60;
								if ((gapToPrev>this.bigGapMinutes)&&(gapToNext>this.bigGapMinutes)){	
									for(j=(loop1Num+1);j<loop2Num;j++){
										this.workIntervals.remove(loop1Num+1);					
									}
									this.workIntervals.add((loop1Num+1), new WorkInterval(event));
									return;
								}
								if((gapToPrev<=this.bigGapMinutes)&&(gapToNext>this.bigGapMinutes)){
									for(j=loop1Num+1;j<loop2Num;j++){
										this.workIntervals.remove(loop1Num+1);
									}
									this.workIntervals.get(loop1Num).addLastEvent(event);
									return;
								}
								if((gapToPrev>this.bigGapMinutes)&&(gapToNext<=this.bigGapMinutes)){
									
									for(j=loop1Num+1;j<loop2Num;j++){
										this.workIntervals.remove(loop1Num+1);
									}
									this.workIntervals.get(loop1Num+1).addFirstEvent(event);
									
									
									return;
								}
								if((gapToPrev<=this.bigGapMinutes)&&(gapToNext<=this.bigGapMinutes)){
									Date temp =this.workIntervals.get(loop2Num).endTime;;									
									for(j=loop1Num+1;j<=loop2Num;j++){
										this.workIntervals.remove(loop1Num+1);
									}
									this.workIntervals.get(loop1Num).endTime= temp;
									return;
									
								}
							}/***(event end in gap END)***/
																
							//event end in interval START					
							if((eventEndTime.getTime()>=wIStartTimeForNum2)&&(eventEndTime.getTime()<=wIEndTimeForNum2))
							{
								
									long gapToPrev1=(eventStartTime.getTime()-wIEndTimeForNum1)/1000/60;
									if(gapToPrev1>this.bigGapMinutes){
										for(j=loop1Num+1;j<loop2Num;j++){
											this.workIntervals.remove(loop1Num+1);
										}
										this.workIntervals.get(loop1Num+1).addFirstEvent(event);
										return;
										
									}
									else//(gapToPrev1<=this.bigGapMinutes)
									{
										Date temp = this.workIntervals.get(loop2Num).endTime;
										for(j=loop1Num+1;j<=loop2Num;j++){
											this.workIntervals.remove(loop1Num+1);
										}
										this.workIntervals.get(loop1Num).endTime=temp;
										return;
										
									}
									
									
									
									
								}/***(event end in interval END)***/
									
								
							/*else{
									continue;
								}*/
								
							
									
								
							
							
						}/***(end of FOR)***/
						
					}/***(event start from gap END)***/
					
					
					//event start from interval
					if ((eventStartTime.getTime()<=wIEndTimeForNum1)&&(eventStartTime.getTime()>=wIStartTimeForNum1))//eventStartTime.getTime()<numWIEndSeconds
					{
						loop2Num=loop1Num+1;
						for(loop2Num=loop1Num+1;loop2Num<this.workIntervals.size();loop2Num++){
							long wIEndTimeForNum2 = this.workIntervals.get(loop2Num).endTime.getTime();
							long wIStartTimeForNum2 = this.workIntervals.get(loop2Num).startTime.getTime();
							long num2PrevEndTime = this.workIntervals.get(loop2Num-1).endTime.getTime();
							//event end in gap START
							if((eventEndTime.getTime()<wIStartTimeForNum2)&&(eventEndTime.getTime()>num2PrevEndTime)){
								//long gapToPrev=(eventStartTime.getTime()-wIEndTimeForNum1)/1000/60;
								long gapToNext=	(wIStartTimeForNum2-eventEndTime.getTime())/1000/60;
								if(gapToNext>this.bigGapMinutes){
									this.workIntervals.get(loop1Num).addLastEvent(event);
									for(j=loop1Num+1;j<loop2Num;j++){
										this.workIntervals.remove(loop1Num+1);
									}
									
								}
								else//(gapToNext<=this.bigGapMinutes)
								{
									Date temp = this.workIntervals.get(loop2Num).endTime;
									this.workIntervals.get(loop1Num).endTime=temp;
									for(j=loop1Num+1;j<=loop2Num;j++){
										this.workIntervals.remove(loop1Num+1);
									}
									
								}	
							
							}/***(event end in gap END)***/
																
							//event end in interval START
							if((eventEndTime.getTime()>=wIStartTimeForNum2)&&(eventEndTime.getTime()<=wIEndTimeForNum2))//(eventEndTime.getTime()>wIStartTimeForNum2){
							{
								Date temp = this.workIntervals.get(loop2Num).endTime;
								this.workIntervals.get(loop1Num).endTime=temp;
								for(j=loop1Num+1;j<=loop2Num;j++){
									this.workIntervals.remove(loop1Num+1);
								}	
								
							}/***(event end in interval END)***/
															
								
							else{
								continue;
							}	
							
									
							
							
							
						}/***(end of FOR)***/
					}
					else{
						continue;
					}
					
				}/***ENF OF FOR***/
			}
				
			 /*  
			 *  Case 4:
			 *   Nothing to do
			 *  
			 */
		}
	}
	
	/**
	 * 
	 * @param x time instant (in seconds)
	 * @return number of interval if <em>x</em> is in time considered active, 
	 *         -1 otherwise
	 */
	public int inWhichActiveInterval(double x)  {
		this.workIntervals=(LinkedList<WorkInterval>) getWorkIntervals();
		for (int i=0;i<(this.workIntervals.size());i++){
			CoordMapper coordMapper = new CoordMapper(this.workIntervals.get(0).startTime);
			Date startWI = this.workIntervals.get(i).startTime;
			Date endWI = this.workIntervals.get(i).endTime;
			if( (x<=coordMapper.timeToX(endWI))&&(x>=coordMapper.timeToX(startWI))){
				return i;
			}
		}
		return -1;
		
	}
	
	
	
	
	/**
	 * 
	 * @param date
	 * @return the amount of time to shown the node, in minutes
	 */
	public float getShiftMinutes(Date date) {
		
		// First, check if we're empty
		if(this.workIntervals.isEmpty()) {
			throw new ShiftCalculatorEmptyException();
		}
		
		
		/*
		 * Possibilities:
		 * 1. *date* is within first interval
		 * 2. *date* is within an interval other than first
		 * 3. *date* is not inside any interals
		 */ 
		
		float shift = 0;
		int i=0;
		int j=0;
		Date firstWorkIntervalEndTime = (Date) this.workIntervals.get(0).endTime.clone();
		 
		/* Case 1: *date* is within first interval
		 *  shift=0
		 */ 

		if (date.getTime()<=firstWorkIntervalEndTime.getTime()){
			//System.out.println("case 1");
			shift = 0;					
			
		}
		
		 /* Case 2: *date* is within an interval other than first
		 *  shift = length(all previous gaps) - appropriate padding*(number of previous gaps)
		 */
		else{		
			// find the interval the data belongs to
			while (date.getTime()>(workIntervals.get(i).endTime.getTime())) {
				i = i + 1;
			}
			//System.out.println("i="+i);
			if (date.getTime()>=workIntervals.get(i).startTime.getTime()) {

				for (j = 0; j <= i - 1; j++) {
					long Gap = (workIntervals.get(j + 1).startTime.getTime() - workIntervals
							.get(j).endTime.getTime()) / 1000 / 60;
					if (Gap > this.bigGapMinutes) {
						//System.out.println("shift="+shift);						
						shift = shift + (Gap - this.paddingMinutes);
					}
				}

			} else {
				//can't exute to this part
			}
		
		}
		 /* Case 3:
		 *  throw an exception
		 *  
		 *  Note: This method may benefit from some kind of caching if it is expensive to do these calculations.
		 * 
		 */
		
		return shift;
		
	

	}
	
	

}
