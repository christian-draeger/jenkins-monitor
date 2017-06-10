#!/bin/bash
DATE=$(date +"%d-%m-%Y")

# Make Sure we are in the Build Directory
cd $TRAVIS_BUILD_DIR

# Create Version Number
export GIT_TAG=$DATE.v$TRAVIS_BUILD_NUMBER

# Tag release
git tag $GIT_TAG -am "$DATE.v$TRAVIS_BUILD_NUMBER"

# Push commit and tags back to the repo
sudo git push origin master && git push origin master --tags
