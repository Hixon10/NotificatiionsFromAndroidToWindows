# NotificatiionsFromAndroidToWindows

There are 2 applications, which show notifications, which you receive on your android phone, on your Windows PC.

1. `NotificationsToGit` sends notifications from `android` to `git` (we use `git` as one of the bests database). 
2. `GitToWindows` polls configured `git` repo and shows notifications on `Windows`. 

## Why this project uses Git as a Database
1. It is free.
2. It is easy to support several clients, which read the same database (let's say you have `Windows PC`, and `Windows Laptop; you want to get notifications on all devices; this case is supported automatically, because each device has own local git/database). 
3. If you need, you can get log of ordered events (a-ka notifications) out of the box, thanks to `git log`.
4. You can easily implement `at least once` semantic (let's say you perform `git fetch origin`, after that you send notification to user, and finally move event log cursor (`git merge origin/main`).  