#!/bin/bash
YEAR=$(date +"%Y")
MONTH=$(date +"%m")

# Make Sure we are in the Build Directory
cd $TRAVIS_BUILD_DIR

# Create Version Number
export GIT_TAG=V.$YEAR-$MONTH.$TRAVIS_BUILD_NUMBER

# Tag release
git tag $GIT_TAG -a -m "V.$YEAR.$MONTH.$TRAVIS_BUILD_NUMBER"

# Push commit and tags back to the repo
sudo git push origin master && git push origin master --tags
