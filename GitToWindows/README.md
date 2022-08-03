# Show notifications from git on Windows

A console application, which checks last commit in a common `git` repo, and if there is something new, shows notification.

## Configuration of project

1. Clone your repo manually to some `GIT_DIRECOTRY`.
2. Set github user/password, git directory and github URL in [Program.cs](https://github.com/Hixon10/NotificatiionsFromAndroidToWindows/blob/main/GitToWindows/Program.cs#L10).
3. Compile the program and configure periodic execution for it. To do it you need to create a `task` in `Task Scheduler` Windows application. You need schedule execution of `runtask.vbs` with help of `wscript.exe`. Please read [https://serverfault.com/a/9039/190769](https://serverfault.com/a/9039/190769) for details. **Note!** `runtask.vbs` should contains `\r\n` for newline, and use `windows-1251` encoding.  