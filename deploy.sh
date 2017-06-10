#!/bin/bash
YEAR=$(date +"%Y")
MONTH=$(date +"%m")
DAY=$(date +"%d")

# Make Sure we are in the Build Directory
cd $TRAVIS_BUILD_DIR

# Create Version Number
export GIT_TAG=$DAY-$MONTH-$YEAR.v$TRAVIS_BUILD_NUMBER

# Tag release
git tag $GIT_TAG -a -m "$DAY-$MONTH-$YEAR.v$TRAVIS_BUILD_NUMBER"

# Push commit and tags back to the repo
sudo git push origin master && git push origin master --tags
