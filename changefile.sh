#!/bin/bash
YEAR=$(date +"%Y")
MONTH=$(date +"%m")
cd $TRAVIS_BUILD_DIR

#Remove Remotes Added by TravisCI
git remote rm origin

#Add Remote with Secure Key
git remote add origin https://${GITPERM}@github.com/${TRAVIS_REPO_SLUG}.git

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
git commit -am "V.$YEAR.$MONTH.$TRAVIS_BUILD_NUMBER [ci skip]"
