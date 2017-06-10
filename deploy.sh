#!/bin/bash
echo "Make Sure we are in the Build Directory"
cd $TRAVIS_BUILD_DIR

#echo "Create Version Number"
#export GIT_TAG=$VERSION

echo "Tag release"
git tag ${VERSION} -am "${VERSION}"

echo "Push commit and tags back to the repo"
sudo git push origin master && git push origin master --tags
