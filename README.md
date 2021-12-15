# Pngme Android (Kotlin) SDK & sample App

Welcome to the Pngme v2.x SDK!
This Readme will cover the basics of how the SDK works, get-started basics, and an example Android app.


For documentation on the legacy SDK (v1.0.34) visit [here](https://developers.api.pngme.com/docs).

_For the v2.x *React Native* docs and sample app, visit here (TBD)._

_For the v2.x *Flutter* docs app, visit here (TBD)_

## Kotlin v2.x SDK - the basics

- The SDK supports Android API level 16+
- The SDK exposes two public methods for implementation (see description below)

### SDK behavior
The SDK performs three simple tasks.
1. register a `user` in pngme's system
2. show a dialog flow in the current Activity to request SMS permissions from the user 
   (this _runs the first time, and only the first time_, that the SDK is invoked)
3. periodically send USSD SMS messages from the user's phone to pngme's system

### SDK methods and implementation
TODO: add signature, field description and elaborate on behavior

## get started
To set up your project to use the Pngme SDK, follow these setup steps.

_Step 1_
Resolve the JitPack package manager in your Gradle file.
Add the following to `build.gradle`.
```groovy
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
```

_Step 2_
Add the SDK package to your Gradle file.
Add the following to `build.gradle`.
```groovy
    dependencies {
        implementation 'com.github.pngme:android-sdk:v2.Y.Z'
    }
```

## example app
This repository is an example Android app, which uses the Pngme SDK.

TODO - complete this section
