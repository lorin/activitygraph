#!/usr/bin/env python
"""
csv2xml.py

Convert the data files from CSV to XML.

csv2xml.py is used to convert the CSV files to XML format. They may require
some manual editing afterwards to delete Series nodes that have no children.

To run it, you need the following libraries:

ElementTree
dateutil


ElementTree comes with Python 2.5, but the import statement needs to be changed. 
ElementTree can be downloaded from http://effbot.org/zone/element-index.htm

dateutil can be downloaded from http://labix.org/python-dateutil


Uses python-dateutil, which you can download at:
http://labix.org/python-dateutil


"""
import csv
import sys

import elementtree.ElementTree as ET
import dateutil.parser as parser
import time

def str2timestamp(s):
    """Convert a time string to time since UNIX epoch, in seconds"""
    return time.mktime(parser.parse(s).timetuple())

def makeactivity(root, name, activityfile):
    """Make activity elements (truth or heuristic)"""
    node = ET.SubElement(root,"SeriesGroup",attrib={'name' :name })
    # Make the individual series
    seriesmap = {
    'Th' : ET.SubElement(node,"Series", attrib={'name' : 'thinking'}),
    'Se' : ET.SubElement(node,"Series", attrib={'name' : 'serial'}),
    'Pa' : ET.SubElement(node,"Series", attrib={'name' : 'parallel'}),
    'De' : ET.SubElement(node,"Series", attrib={'name' : 'debugging'}),
    'Te' : ET.SubElement(node,"Series", attrib={'name' : 'testing'}),
    'Ot' : ET.SubElement(node, "Series", attrib={'name' : 'other'}),
    }
    reader = csv.reader(activityfile)
    for row in reader:
        (start,end,time,activity) = row[:4]
        ET.SubElement(seriesmap[activity],"Interval", 
                        attrib= {'start' : str(int(str2timestamp(start))), 
                                 'end' : str(int(str2timestamp(end))),
                                 }
                     )
                     
def makeevents(root,eventsfile):
    """Make event elements"""
    node = ET.SubElement(root,"SeriesGroup", attrib={'name' : 'Events'})
    types = ['viewing','filecommand','documentation','other','editing','building','unknown','running','process']
    seriesmap = {}
    for t in types:
        seriesmap[t] = ET.SubElement(node,"Series", attrib={'name' : t})

    reader = csv.reader(eventsfile)
    for row in reader:
        timestamp =  row[0]
        if row[2] != '0' : continue
        activity = row[4]
        ET.SubElement(seriesmap[activity],"Point", attrib={'time' : timestamp})
    
        
def main():
    """Main function"""
    root = ET.Element("ActivityGraph")
    makeactivity(root,'truth',open('Activity.csv'))
    makeactivity(root,'heuristic',open('heuristics.csv'))
    makeevents(root,open('events.csv'))

    #events = ET.SubElement(root,"SeriesGroup", attrib={'name' : 'Events'})
    tree = ET.ElementTree(root)
    tree.write(sys.stdout)
    


if __name__ == '__main__':
    main()