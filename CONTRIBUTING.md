# Contributing to GlowStone #
[Glowstone](http://glowstone.net) is a lightweight, open source Minecraft server written in Java. It supports plugins written for the Bukkit API.

For those who wish to contribute, we encourage you to fork the repository and submit pull requests. Below you will find guidelines that will explain this process in further detail.

## Quick Guide ##
1. Create or find an issue on the [Issue Tracker](https://github.com/SpaceManiac/GlowStone/issues).
3. Fork GlowStone if you haven't done so already.
4. Create a branch dedicated to your change.
5. Write code addressing your feature or bug.
6. Commit your change according to the [committing guidelines](#committing-your-changes).
7. Push your branch and submit a pull request.

## Getting Started ##
* Find an issue to fix or a feature to add.
* Search the issue tracker for your bug report or feature.
* Large changes should always have a separate issue to allow discussion.
  * If your feature or bug incorporates a large change, file a new issue, so the feature and its implementation may be tracked separately. This way, the nature of the issue may be discussed before time is spent addressing the issue. 
* Fork the repository on GitHub.
 
## Making Changes ##
* Create a topic branch from where you want to base your work.
  * This is usually the master branch.
  * Name your branch something relevant to the change you are going to make.
  * To quickly create a topic branch based on master, use `git checkout master` followed by `git checkout -b <name>`. Avoid working directly on the `master` branch.
* Make sure your change meets our [code requirements](#code-requirements).

### Code Requirements ###
* All submitted code must follow [Java Conventions](http://www.oracle.com/technetwork/java/codeconventions-150003.pdf).
* The indentation should solely consist of 4 spaces instead of tabs.
* No trailing whitespaces for code lines, comments or configuration files.
* No CRLF line endings, only LF is allowed.
  * For Windows-based machines, you can configure Git to do this for your by running `git config --global core.autocrlf true`.
  * If you're running a Linux or Mac OSX, you should run `git config --global core.autocrlf input` instead.
  * For more information about line feeds. See this [this article](http://adaptivepatchwork.com/2012/03/01/mind-the-end-of-your-line/).
* Files must always end with a newline.
* No 80 character line limit or 'weird' midstatement newlines.
* Avoid nested code structures.
* Use 4 spaces instead of tabs.
* All files must end with a new line.

## Committing your changes ##
* Check for unnecessary whitespace with `git diff --check` before committing.
* Describe your changes in the commit description.
* For a prolonged description, continue on a new line.
* If your change addresses a pre-existing issue:
  * The first description line should contain either:
    * For a bug-related issue: "Fixes _#issue_".
    * For a feature request: "Resolves _#issue_".
    * Where "#issue" is the issue number which your change addresses.

#### Example commit message ####
> Changed wgen to treat 128 as world height in all cases (fixes #151).

## Submitting Your Changes ##
* Push your changes to the topic branch in your fork of the repository.
* Submit a pull request to this repository.
  * Explain in detail what each one of your commits changes and point out large changes and your motives behind these.
  * If your PR is not finished, but you are looking for review anyway, prefix title with [WIP].
* Await peer review and feedback.
* Revise PR based on feedback.

## How to get your pull request accepted ##
* Ensure your change does not solely consist of formatting changes.
* Be concise and to the point in your pull request title.
* Address your changes in detail. Explain why you made each change.
* Run `gradlew licenseFormat` to add/update license headers. PRs with header-less files will not be accepted.
* The code must be your work or you must accredit those whom you have use code from appropriately.


## Additional Resources ##
* [GlowStone website](http://glowstone.net)
* [Bug tracker](https://github.com/SpaceManiac/GlowStone/issues)
* [GlowStone builds](http://ci.chrisgward.com/job/Glowstone/)
* [Travis CI](https://travis-ci.org/SpaceManiac/Glowstone)
* IRC: #glowstone and #glowstonedev on EsperNet (support, development)
* [General GitHub documentation](http://help.github.com/)
* [GitHub pull request documentation](http://help.github.com/send-pull-requests/)