# GLOBE - Grocery Tracker - Android Application #

This is a working directory for an Android application, coded in Java.

The purpose of the application is to enable our client, the GLOBE research institute at Deakin University to easily capture grocery receipt purchases from research participants.

All features have been fully implemented, and an overview of the progress it took to get the application from start to completion can be found in the changelog.txt file.

###
* The BitBucket link for the project can be found [here](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse)
* The application has also been published as an internal app test to [Google Play](https://play.google.com/apps/testing/com.stevecrossin.grocerytracker)

## Getting Started ##

As this is an Android application, it can be run several different ways.

The BitBucket repo can be cloned or downloaded in a ZIP format from GitHub and compiled in Android Studio and then run on an emulator, or a physical device.

You can do so by performing this operation.
```
$ git clone https://bitbucket-students.deakin.edu.au/scm/globe/globe---t319.git
```
The application can also be downloaded from Google Play once you are enrolled in the application demo.

## Directory structure ##
The root of the project contains:

[/app](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app) - contains all source files for the app. Broken down further, it contains:

* [/documentation](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/documentation) - contains all project documentation, such as the guide to integrate with Google Sheets & Firebase, the user and technical documents, and a list of tutorials for new developers.

[Java files](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/src/main/java/com/globe/grocerytracker) - contains all source code for the application. Further broken down into:
* [/adapters](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/src/main/java/com/globe/grocerytracker/adapters) - contains the code for all the elements that make adapters and viewholders that are contained in the application function
* [/database](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/src/main/java/com/globe/grocerytracker/database) - contains the data repositories for the application and entities that exist in that database
* [entities](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/src/main/java/com/globe/grocerytracker/entities) - contains the information for each database table in the application, and the SQL operations for each database
* [models](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/src/main/java/com/globe/grocerytracker/models) - contains the source code that handles parsing of grocery receipts
* [/screens](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/src/main/java/com/globe/grocerytracker/screens) - contains the files that dictates how each activity in the application functions and the operations of those activities
* [/utils](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/src/main/java/com/globe/grocerytracker/utils) - contains java files not elsewhere classified, which includes the password scrambling, validation of text input, and definition of app state.

[/res folder](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/src/main/res) - contains all layout XML files for the app, colours, strings, styles and drawable objects. Also contains a raw directory which holds all text files.

Additionally, the root of the project contains:
* [bugs.txt](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/bugs.txt) - a list of all bugs that were encountered while developing the application. All were fixed, and method to fix in most cases were noted for future reference.
* [changelog.txt](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/changelog.txt) - list of all changes made in the application

## Core Features & Technical Information ##
For a full breakdown of the application features, please review the [technical documentation](https://bitbucket-students.deakin.edu.au/projects/GLOBE/repos/globe---t319/browse/app/documentation/GLOBE%20-%20Technical%20Documentation.pdf)

## Permission ##
While this application is a student project created by the GLOBE Capstone squad at Deakin for the GLOBE research institute at Deakin University, the code has been written by the students and should be attributed as such.


