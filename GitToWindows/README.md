# Show notifications from git on Windows

A console application, which checks last commit in a common `git` repo, and if there is something new, shows notification.

## Configuration of project

1. Clone your repo manually to some `GIT_DIRECOTRY`.
2. Set github user/password, git directory and github URL in [Program.cs](https://github.com/Hixon10/NotificatiionsFromAndroidToWindows/blob/main/GitToWindows/Program.cs#L10).
3. Compile the program and configure periodic execution for it. 