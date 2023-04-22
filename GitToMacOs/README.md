# Show notifications from git on MacOS

A bash script `pollupdates.sh`, which checks last commit in a common `git` repo, and if there is something new, shows notification.

## Configuration of project

1. Clone your repo manually to some `GIT_DIRECOTRY`
2. Set github user/password, git directory and github URL in `pollupdates.sh`
3. Set a correct path to `pollupdates.sh` in `ru.hixon.androidnotifications.plist` (by default, you will have my path, which it not existed on your machine).
4. Move a file `ru.hixon.androidnotifications.plist` to `~/Library/LaunchAgents/ru.hixon.androidnotifications.plist`
5. Start a periodic job with `launchctl load ~/Library/LaunchAgents/ru.hixon.androidnotifications.plist` (this job will try to `pull` events every minute)

## How to stop a periodic job
1. `launchctl unload ~/Library/LaunchAgents/ru.hixon.androidnotifications.plist`
2. `launchctl remove ru.hixon.androidnotifications`

## How to get execution logs
1. Firstly, you need to stop a periodic job.
2. Secondly, you need to replace `/dev/null` to some log file path in `~/Library/LaunchAgents/ru.hixon.androidnotifications.plist`
3. Lastly, you need to start the job again `launchctl load ~/Library/LaunchAgents/ru.hixon.androidnotifications.plist`