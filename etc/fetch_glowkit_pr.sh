#!/bin/bash

[ "$1" == "false" ] && echo "Skipping glowkit build - not a PR" && exit 0

# exit at the first failure
set -e

PR_ID=$1


# public repo access token, to bypass rate limiting. will probably revoke later
ACCESS_TOKEN=053b27d9bf05b86706b531e8cd63afcf1af3bada:x-oauth-basic

# Fetch the body of this PR
PR_BODY=$(curl -u $ACCESS_TOKEN -s https://api.github.com/repos/GlowstoneMC/Glowstone/pulls/$PR_ID | jq -r '.body' | sed 's/\r$//')

echo "Glowstone PR: $PR_ID. Body:"
echo "-------------------------------------------------------------------------"
echo "$PR_BODY"
echo "-------------------------------------------------------------------------"
echo

# Extract from body
GLOWKIT_PR=$(echo $PR_BODY | egrep -o 'Glowkit/pull/[0-9]+' | sed 's#Glowkit/pull/##')

echo "PR links found:" $GLOWKIT_PR
echo

# Validate
if [ -z "$GLOWKIT_PR" ]; then
    echo "No PR links found. Aborting."
    exit 0
elif [ $(echo "$GLOWKIT_PR" | wc -l) != 1 ]; then
    echo "More than one PR link found. Aborting"
    exit 0
fi

# Fetch the PR from the other repo just like travis-ci does
git clone --depth=50 git://github.com/GlowstoneMC/Glowkit.git glowkit-temp
cd glowkit-temp
git fetch origin +refs/pull/$GLOWKIT_PR/merge:
git checkout -qf FETCH_HEAD

# Actually build it!
mvn clean install -Dmaven.javadoc.skip=true
