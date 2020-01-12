## GLOBE - Grocery Tracker - Trimester 3 2019.

This is the start of the ReadMe document for the GLOBE grocery tracker. More information will follow soon, but in the interim, please get familiar with the below.
SourceTree (https://www.sourcetreeapp.com/) is the recommended repository tool for this project. Please install it, and then let me know if you need assistance setting it up.

Tutorial on setting up Android Studio to connect to BitBucket: http://theworkingdad.it/2018/10/04/quickly-setup-android-studio-with-git-and-bitbucket/

To get started you will need to run these commands in your terminal.
New to Git? [Learn the basic Git commands](http://docs.atlassian.com/bitbucketserver/docs-061/Basic+Git+commands?utm_campaign=in-app-help&amp;utm_medium=in-app-help&amp;utm_source=stash)

### Configure Git for the first time

    git config --global user.name "Your Name"
    git config --global user.email "yourusername@deakin.edu.au"

### Working with your repository

#### **How do I obtain a Repository Git URL ?**

#### Browse to the Repository browse page location that you wish to clone/fork from the server
[https://bitbucket-students.deakin.edu.au](https://bitbucket-students.deakin.edu.au)

Ensure you have entered the repository page browser and then: hover, find and click on the &#39;Clone&#39; button from the repository left menu.

This should show you the full URL of the repository which you can use.


#### **I just want to clone a repository**

If you want to simply clone a repository then visit the repo page, find and copy the repository&#39;s git URL. The git URL should end with .git

You can then use a git terminal to type
git clone &quot;Insert-your-git-url-here&quot;

  
    **Example only:** git clone https://bitbucket-students.deakin.edu.au/scm/test/testing.git

#### **I just want to check commit in this project**
 
If you want to check who pushed commit 

You can use #git log


#### **I just want to switch the branch**
 
If you want to switch your local branch

You can use #git checkout branchname

**Example only: git checkout working-files 