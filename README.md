# ActivityGraph

ActivityGraph is an interactive application for visualizing developer workflow.

![ActivityGraph](activitygraph.png).

Pan by dragging with the left mouse button, zoom by dragging with the right
mouse button.

## Building ActivityGraph

ActivityGraph can be built and run using the included ant script (build.xml).
You'll need Ant version 1.7.0 or higher to run the (JUnit 4.x) unit tests, but earlier
versions of ants should be able to successfully compile and run ActivityGraph.

To build ActivityGraph, type:

    $ ant compile

To run the unit tests, type:

    $ ant test

To run ActivityGraph, type:

    $ ant run

Try opening the XML file data/demo.xml to try out the visualization (using the File menu).

## Packaging ActivityGraph

If you wish to create a package of ActivityGraph for deployment, first make sure
you have a ~/work directory (where a pristine version of ActivityGraph will be
checked out) and a ~/testinstall directory (which will contain the deployed
files).

Also make sure your local subversion sandbox is up to date before generating a
package.

To generate a package, type:

```
$ cd bin
$ sh release_generate.sh
```

This will do the following:

* Check out the latest version of ActivityGraph to ~/work/ActivityGraph
* Run the unit tests on the newly checked out version
* Create a zipfile in ~/work/ActivityGraph/dist
* Unzip this file into ~/testinstall/ActivityGraph-rxx where xx is the revision number returned by the svnversion command



