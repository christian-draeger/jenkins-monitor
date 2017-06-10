#!/bin/bash
YEAR=$(date +"%Y")
MONTH=$(date +"%m")
DAY=$(date +"%d")
cd $TRAVIS_BUILD_DIR

#Remove Remotes Added by TravisCI
git remote rm origin

#Add Remote with Secure Key
git remote add origin https://${GITHUB_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git

#List Remotes ONLY DURING testing - do not do this on live repo / possible key leak
#git remote -v

# Set Git Variables
git config --global user.email "${GIT_EMAIL}"
git config --global user.name "${GIT_NAME}"
git config --global push.default simple

# Make sure we have master branch checked out in Git
git checkout master

# Add the modified file and commit it
git add $TRAVIS_BUILD_DIR/"${JAR}"
git add $TRAVIS_BUILD_DIR/"${DEB}"
git add $TRAVIS_BUILD_DIR/"${EXE}"
git commit -am "$DAY-$MONTH-$YEAR.v$TRAVIS_BUILD_NUMBER [ci skip]"
