# Notifications to git

This application listens to all android notifications, filter them and execute `git commit + push` to configured repo for needed events.

## Configuration of project

1. Set your token in [ru.hixon.notificationstogit.GitNotificationListener.GITHUB_PERSONAL_TOKEN](https://github.com/Hixon10/NotificatiionsFromAndroidToWindows/blob/main/NotificationsToGit/app/src/main/java/ru/hixon/notificationstogit/GitNotificationListener.java#L35). You can get the token by visiting [https://github.com/settings/tokens](https://github.com/settings/tokens).
2. Set repository API URL in [ru.hixon.notificationstogit.GitNotificationListener.GITHUB_API_URL](https://github.com/Hixon10/NotificatiionsFromAndroidToWindows/blob/main/NotificationsToGit/app/src/main/java/ru/hixon/notificationstogit/GitNotificationListener.java#L36).

## Configuration of yours mobile phone

1. Turn off `battery optimization` for this application. You can do it in a settings of your phone.
2. Allow to receive notification events for this application. When you open the application first time, needed settings menu will be opened. 