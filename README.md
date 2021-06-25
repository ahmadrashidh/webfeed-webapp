# Webfeed

Webfeed is RESTful application built on Java 11 and Google Cloud Project which provides services applications can use to create a simple social media where your users can post their status while others can interact by commenting and liking the post. Webfeed also authenticates user using Google OAuth feature which is secure and customizable from your end. You can build own frontend to call these services and build into a social media app.

## Features provided
- Post whats on mind.
- Like other peoples post
- Comment on other post

Webfeed also employed deep-nested comments so youre users stays conversation unending.

## Setting up a project

### Datastore Emulator

First thing you should set up is set up Google Cloud Project and link it with this project in pom.xml. Next, you need to execute these commands in Command Line under project root directory to run datastore emulator.

```bash

 gcloud beta emulators datastore start 

```

Once ran the above command, run the below command in another command line

```bash

 gcloud beta emulators datastore env-init > set_vars.cmd && set_vars.cmd 

```
### Secret Manager

Webfeed also uses Secret Manager to retrieve Google OAuth Client Secret . Visit Credentials section in Google Cloud Console to create one and also create secret for it. You can replace that by setting Client Secret as environmental variable.Make sure you git ignore that




