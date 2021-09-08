# Release Process

This document describes the release process for a new version of the Gini Capture SDK for Android.

1. Add new features only in separate `feature` branches and merge them into `develop`
2. Create a `release` branch from `develop` or commit directly into `develop`
  * Update the version in `gradle.properties`
  * Update the version in `ginicapture/src/doc/source/getting-started.rst`
  * Update the version in `ginicapture/src/doc/source/integration.rst`
3. Push the `release` or `develop` branch and wait for the Jenkins build to finish
4. If everything is fine create a PR to merge the `release` or `develop` branch into `master`
5. After merging tag the version on `master` and push the tag
6. Start a new build on the `master` branch (if not automatically started) and confirm the release
7. Create a new release on GitHub with a changelog for the release tag
8. Merge `release` branch into `develop` (if applies)
9. Delete the `release` branch (if applies)