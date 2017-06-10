#!/bin/bash

#export VERSION=$(date +"%d-%m-%Y").v${TRAVIS_BUILD_NUMBER}

echo "Make Sure we are in the Build Directory"
cd $TRAVIS_BUILD_DIR

echo "Remove Remotes Added by TravisCI"
git remote rm origin

echo "Add Remote with Secure Key"
git remote add origin https://${GITHUB_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git

echo "Set Git Variables"
git config --global user.email "${GIT_EMAIL}"
git config --global user.name "${GIT_NAME}"
git config --global push.default simple

echo "Make sure we have master branch checked out in Git"
git checkout master

echo "adding $TRAVIS_BUILD_DIR/${JAR}"
git add $TRAVIS_BUILD_DIR/${JAR}

echo "adding $TRAVIS_BUILD_DIR/${DEB}"
git add $TRAVIS_BUILD_DIR/${DEB}

echo "adding $TRAVIS_BUILD_DIR/${EXE}"
git add $TRAVIS_BUILD_DIR/${EXE}

echo "commit all added files for version ${VERSION}"
git commit -am "${VERSION} [ci skip]"
