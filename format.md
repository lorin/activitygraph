# ActivityGraph XML file format

An ActivityGraph XML file looks something like this:

```
<ActivityGraph timezeone="America/New_York">
 <SeriesGroup name="Truth">
	<Series name="thinking" color="green">
		<Interval start="1176500214" end="1176004215" text="a description of the element" note="A note on this element" />
		<Interval start="1186550957" end="1186552054" text="another description" note="Another note" />
		...
	</Series>
	<Series name="serial code">
	...
	</Series>
 </SeriesGroup>
<SeriesGroup name="Events">
	<Series name="filecommand" color="blue">
		<Point time="1166050237" />
		<Point time="1240202570" />
		...
	</Series>
	<Series name="building">
		...
	</Series>
</SeriesGroup>
</ActivityGraph>
```

A Series of data elements is made up of either Points (that occur in a single
instant in time) or Intervals (that have a start and end time). Series are
organized into SeriesGroups. An optional "text" and "note" attribute can be
added to any Point or Interval element, which will appear as a tooltip. "text"
is intended for objective information about the element, and "note" is intended
for more subjective annotations.

Times are specified as seconds since the epoch (Jan. 1, 1970 00:00:00 UTC), also
known as [Unix time](http://en.wikipedia.org/wiki/Unix_time). (Note that the
Java standard library uses milliseconds since the start of the Unix epoch
instead of seconds).

A Series may have an optional color attribute, using one of the following
colors: black, blue, cyan, darkGray, gray, green, lightGray, magenta, orange,
pink, red, white, yellow

The timezone attribute in the ActivityGraph element is optional. Its useful
when the data that you are visualizing was recorded in a different
time zone than the one your computer is set to.

There is also an [ActivityGraph DTD](data/activitygraph.dtd).

