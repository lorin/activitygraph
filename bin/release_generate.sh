#!/bin/bash
#
# This script checks out the latest version of the code into a directory,
# builds it, runs unit tests, creates a zipfile,
# then unzips it into a different directory for testing.
#
# Excerpted from the book, "Pragmatic Project Automation"
# ISBN 0-9745140-3-9
# Copyright 2004 The Pragmatic Programmers, LLC.  All Rights Reserved.
# Visit www.PragmaticProgrammer.com
#

if [ $# -eq 0 ]
then
  #echo "usage: release_generate.sh <version>"
  #exit 1
  cd ..
  VERSION=r`svnversion`
else
VERSION=$1
fi

NAME=ActivityGraph
SVNROOT=http://activitygraph.googlecode.com/svn
RELEASE=$NAME-$VERSION
WORK_DIR=$HOME/work
TEST_DIR=$HOME/testinstall

# Check out the mainline into the owrk directory
cd $WORK_DIR
rm -rf $NAME
svn co $SVNROOT/trunk $NAME
cd $NAME


# Test the mainline
if ! ant test
then
  echo "Mainline test failed!"
  exit 1
fi


# Create a distribution file

cd $WORK_DIR/$NAME
ant -buildfile package.xml -Dversion=$VERSION

# Test the distribution contents
cd $TEST_DIR
unzip $WORK_DIR/$NAME/dist/$RELEASE.zip
#cd $TEST_DIR/$RELEASE
#jar xvf $NAME.jar

#cd $WORK_DIR/$RELEASE-rb
#if ! ant test \
#     -Dbuild.prod.dir=$TEST_DIR/$RELEASE/lib \
#then
#  echo "Distribution test failed!"
#  exit 1
#fi

# Tag the release
#cd $WORK_DIR/$RELEASE-rb
#svn copy $SVNROOT/trunk $SVNROOT/tags/REL_$VERSION

#scp $WORK_DIR/$NAME/dist/$RELEASE.zip cse.unl.edu:activitygraph
