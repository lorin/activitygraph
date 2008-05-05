#!/bin/bash 
# Use the -x flag to echo output

# First, check that this is an unmodified subversion repository

rev=`svnversion`

if [[ "$rev" == *[M:]* ]]
then
    echo "Current revision ($rev) needs commits or updates"
    exit 1
fi

rm -rf activitygraph/

# Get a fresh version
svnroot=`svn info | grep "Repository Root" | sed -e 's/Repository Root: //;'`
svn export $svnroot/trunk activitygraph
cd activitygraph

# We don't distribute with the deploy directory (just used for packaging)
# or the misc directory 
rm -r deploy/ misc/

# Build the jar and copy it to the root, and then remove the classes files
ant jar
mv build/jar/ActivityGraph.jar .
ant clean

# Zip it all up
cd ..
zip -r activitygraph-r$rev activitygraph
echo activitygraph-r$rev is ready for upload