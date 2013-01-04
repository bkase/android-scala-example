# Example Scala project

Follow `Part A` of the guide, then open up the project, or follow both `Part A` and `Part B` to make a project from scratch

# Configuring Android Dev Environment with Scala

Forked from this guide: https://github.com/jberkel/android-plugin/wiki/Getting-started

There are few important steps that the guide omits, but you need to know. In addition, I'm being more specific in order to make configuration go more smoothly:


## Getting started

### Part A

The basic requirements are obviously [sbt](https://github.com/harrah/xsbt/wiki) (0.12.0+) and the
[Android SDK](http://developer.android.com/sdk/index.html) (`brew install sbt android-sdk` when using
[homebrew](http://mxcl.github.com/homebrew/) on OSX).

Also, you're going to want [IntelliJ](http://www.jetbrains.com/idea/) as it is a great Scala/Android IDE. Open up IntelliJ, you'll need two plugins: On the right-hand side of the welcome-screen, click on "Open Plugin Manager"

Install: Scala, and SBT (search for them and then right-click and select install) -- Scala for obvious reasons, SBT plugin so you can use the SBT console while you're within IntelliJ (click open SBT Console on the bottom of the IDE)

### Part B

First, git clone https://github.com/jberkel/android-plugin (there have been bugs that are fixed on trunk but not in the latest released version), then input the following:

  $ cd android-plugin
  $ sbt publish-local

Using a [giter8](https://github.com/n8han/giter8#readme) template is the easiest way to create a new
project that uses the plugin. If you don't have giter8 installed:

    $ curl https://raw.github.com/n8han/conscript/master/setup.sh | sh
    $ ~/bin/cs n8han/giter8

Now create a new project with one of the Android templates:

    $ ~/bin/g8 jberkel/android-app

This will prompt you to customize a few values: 
(press enter to accept defaults).

However, change `scala_version` from the default to 2.10.0

Also, be sure to change the name of the project to one without spaces -- for example do `My_Android_Project` instead of `My Android Project`

Also note that we will be manually overwriting `scalatest_version` in order to get it to work with scala 2.10.0 (since proguard takes super long on 2.9.x and you definitely need proguard running).
    
    $ cd <your app name>

Open up project/Build.scala:

  * Add the following line below `scalaVersion` in the `settings` `Seq` in the `General` object (towards the top of `Build.scala`): (this is to prevent dex errors (we need to output 1.6 bytecode for android))

     ```scala
      javacOptions ++= Seq("-source", "1.6", "-target", "1.6"),
     ```
  * Replace the `libraryDependecies` under `fullAndroidSettings` with: (this is because we need a special version of scala-test for RC5 of scala 2.10)

    ```scala
      libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0.M5b"
    ```
* Open up project/plugins.sbt
  * Replace the `addSbtPlugin` line with: (tell sbt to use our locally published plugin)

    ```scala
      addSbtPlugin("org.scala-sbt" % "sbt-android-plugin" % "0.6.3-SNAPSHOT")
    ```

Now let's add a jar file to our project (for example we will use the android-support jar, which you should always include when supporting older versions), in the root of the project:
    
    $ mkdir lib # Note it MUST be named 'lib'
    $ ln -s /path/to/android-sdk/extras/android/support/v4/android-support-v4.jar android-support-v4.jar

Then, to build the Android package:

    $ export ANDROID_HOME=/path/to/sdk # or ANDROID_SDK_{HOME,ROOT}
    $ sbt 'gen-idea no-classifiers' compile # to generate IntelliJ specific configuration
    $ sbt # enter sbt's interactive mode

    > android:package-debug

Start an emulator (if not already running)

    > android:emulator-start <my_avd>  # use <tab> to get a list of avds

To install and start the main activity in the [Android Emulator][]

    > android:start-emulator

To build a signed package for release into the Marketplace:

    > android:prepare-market

##Launching the emulator from sbt

A developer can now fire up the Android Emulator from the sbt terminal
(hint: you can get a list of all avds with tab completion)

    > android:emulator-start <my_avd>

To list all devices or emulators

    > android:list-devices

To stop the emulator:

    > android:emulator-stop

### Additional configuration for IntelliJ IDEA (tested on 11.1.1)

Create initial IDEA config files (using https://github.com/mpeltonen/sbt-idea) if you haven't already

    $ sbt 'gen-idea no-classifiers'

Open up File -> Project Settings

Select Facets then the +-sign then add an Android facet -- fill out all the fields pointing to your information in src/main/ (do the following if necessary)

    $ cd /path/to/src/main
    $ mkdir libs

Additionally in order to get the interface preview to work when editing res/ files, in Project Settings: Change the module SDK to Android x.x. (in none exists you'll have to configure yout Android SDK root and install something

    $ sudo /opt/android-sdk/tools/android

