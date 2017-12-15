# **User and development manual**  

We hosted our application on a open repository on Github. There you can access both our API and our Android project. We use Android Studio to run our project and Pycharm to run our API. Note that because the project runs on localhost then the Android studio emulator is needed to run the app and connect to the database.

You can download a zip from github containing the application.
You could also clone either repository using the command below in bash.

The Android project.
```
git clone https://github.com/Meat-Pie-Guys/hreysti_app.git
```
The API.
```
git clone git@github.com:Meat-Pie-Guys/hreysti_api.git
```

The following things are needed to run the app on a local machine.  
* Git 
* Android studio 
* Flask
* Python 

## **Setting up the environment**  

* Install and set up Git, go to *https://help.github.com/articles/set-up-git*  
* Install and set up Android studio with an emulator, go to *https://developer.android.com/studio/index.html*  


## **Running up the project**

Clone this project to your computer, run this command in bash.  
```
git clone https://github.com/Meat-Pie-Guys/hreysti_app.git
```

In order to get the API running you need to have python installed on your local machine. If not you can run the following command in command promt (assuming you are running on windows) to install a python package manager called Pip.
```
pip install
```

You also need to install these third party libraries in command prompt in order to get the API running properly.

-Flask is a micro webdevelopment framework for Python.
```
pip install Flask
```
-Flask-SQLAlchemy is a SQLAlchemy extension for Flask.
```
pip install Flask-SQLAlchemy
```
-UUID object and generation functions (Python 2.3 or higher)
```
pip install uuid
```
-JSON Web Token implementation in Python
```
pip install PyJWT
```
-Dateparser provides modules to easily parse localized dates in almost any string formats commonly found on web pages.
```
 pip install dateparser
```

The API itself can be run on a shell or in an IDE framwork for python, we recommend Pycharm from JetBrains (*https://www.jetbrains.com/pycharm/specials/pycharm/pycharm.html?gclid=Cj0KCQiAgs7RBRDoARIsANOo-Hg0tOk4-l10909CqqzPQFeVnWBDMT621rWsLwTNpek1clshnANSXnUaAhK-EALw_wcB&gclsrc=aw.ds.ds&dclid=CIX07_SjjNgCFdO6GwodImUInA*)

