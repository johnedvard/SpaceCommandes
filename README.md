SpaceCommandes
==============

A multiplayer space game with lasers!


Getting started
-----------

This is a brief description of how to get everything setup properly.

### Basics

* Install Eclipse IDE Juno or Kepler, prefereably JEE version, but other versions may work too.

Start Eclipse with a fresh workpace

* Install Android SDK and target device 2.3.3
* Install GWT plugin and SDK (you do not need the Google App Egnine)

###  Specifics

clone [LibGDX][1]  
build it from source (basically ant -f fetch.xml; ant)
import the following projects into Eclipse:
* gdx
* gdx-backend-lwjgl
* gdx-jnigen
* gdx-openal

clone [spine-runtimes][2]  
import the following project into Eclipse:
* spine-libgdx

clone [SpaceCommandes][3]  
import the following projects into Eclipse:
* SpaceCommandes
* SpaceCommandes-android
* SpaceCommandes-desktop
* SpaceCommandes-html


If you're lucky, everything, save the HTML-project, will compile.  
If you're unlucky, there will be other problems. If so, `<insert preferred course of action here>`


[1]: https://github.com/libgdx/libgdx "LibGDX"
[2]: https://github.com/EsotericSoftware/spine-runtimes "Spine Runtimes"
[3]: https://github.com/johnedvard/SpaceCommandes "Space Commandes"
