# Webfeed

Webfeed is RESTful application built on Java 11 that simulate simple social media where users can post their status while others can interact by comment threads and liking the post. The app authenticates user using Google OAuth. The app is a Google Cloud Project which is hosted in Google App Engine and Google Datastore as database.


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

Webfeed also uses Secret Manager to retrieve Google OAuth Client Secret. Visit Credentials section in Google Cloud Console to create one and also create secret for it. You can replace that by setting Client Secret as environmental variable.Make sure you git ignore that




