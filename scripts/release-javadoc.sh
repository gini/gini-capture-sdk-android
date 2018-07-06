#!/usr/bin/env bash
#
# Commits the javadoc to gini-vision-library-android's gh-pages branch.
#
# Must be executed from the project root.
#
# Parameters (must be in this order):
#   1. git username
#   2. git password
#
set -e
#set -x

if [ $# -ne 2 ]; then
    echo "Pass in the git username and password"
    exit 0
fi

git_user=$1
git_password=$2

rm -rf gh-pages
git clone -b gh-pages https://"$git_user":"$git_password"@github.com/gini/gini-vision-lib-android.git gh-pages

rm -rf gh-pages/javadoc
rm -rf gh-pages/network/javadoc
mkdir gh-pages/network
cp -a ginivision/build/docs/javadoc gh-pages/
cp -a ginivision-network/build/docs/javadoc gh-pages/network/
cd gh-pages
touch .nojekyll
git add -u
git add .
git diff --quiet --exit-code --cached || git commit -a -m 'Gini Vision Library and Gini Vision Network Library Javadoc'
git push