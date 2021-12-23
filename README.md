# Pngme Android (Kotlin) SDK & sample App

Welcome to the Pngme v2.x SDK!
This Readme will cover how the SDK works, get-started basics, and an example Android app.

For documentation on the legacy SDK (v1.0.34) visit [here](https://developers.api.pngme.com/docs).

_For the v2.x *React Native* docs and sample app, visit here (TBD)._

_For the v2.x *Flutter* docs app, visit here (TBD)_

## Kotlin v2.x SDK - the basics

- The SDK accomplishes three tasks:
  - register a user with pngme's identity system
  - request permission for SMS from the user, with a [Permission Dialog Flow](.docs/permission_flow.gif)
  - periodically upload SMS data to pngme's data processing pipeline
- The SDK supports Android API level 16+
- The SDK exposes three methods: a main entrypoint method, and two helper methods
- Using the SDK requires an SDK `clientKey`, 
  available at the [pngme admin webconsole](https://admin.pngme.com)

The Pngme SDK sends a user's SMS messages to Pngme's data processing system.
The data processing system extracts financial and alternative data for the user,
and exposes those for retrieval at Pngme's suite of REST APIs 
(see the [API Reference docs](https://developers.api.pngme.com/reference/getting-started-with-your-api)).

To use the Android SDK and REST APIs, you will need an SDK `clientKey` and REST API `token`.
Both can be found in the [pngme admin webconsole](https://admin.pngme.com).
Signing up for an account and getting started is free!

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

_Step 3_
Implement the `PngmeSdk.go()` method as needed in your app.

## Methods
### `PngmeSdk.go()`

```kotlin
 fun go(
     activity: AppCompatActivity,
     clientKey: String,
     firstName: String,
     lastName: String,
     email: String,
     phoneNumber: String,
     externalId: String,
     isKycVerified: Boolean,
     companyName: String,
     onComplete: Callback? = null
 )
```

The `go` method is the main entrypoint method for invoking the PngmeSdk.
The `go` method is idempotent, and can be invoked multiple times.

The `go` method performs three tasks.
1. register a `user` in pngme's system using an Android Onetime Worker
2. show a dialog flow in the current Activity to request SMS permissions from the user -- 
   this _runs the first time, and only the first time_, that `go` is invoked
3. send USSD SMS messages from the user's phone to pngme's system using an Android Periodic Worker

| var name | description |
| -------- | ----------- |
| activity | a reference to the current Activity |
| clientKey | the Pngme SDK key for your account (see above) |
| firstName | the mobile phone user's first name |
| lastName | the mobile phone user's last name |
| email | the mobile phone user's email address |
| phoneNumber | the mobile phone user's phone number, example `"23411234567"` |
| externalId | a unique identifier provided by your app (if none available, pass an empty string `""`)|
|isKycVerified | a boolean, indicating if your app has verified the user's identity using KYC |
| companyName | your company's name; this is used in the display header of the permissions UI flow |
| onComplete | a callback function that is called when the `go` method has completed |

#### `onComplete` callback
The `go` method should be invoked and left to complete while the `activity` is in a [running state](https://developer.android.com/guide/components/activities/activity-lifecycle).
The `onComplete` callback is a useful callback, for example, 
in determining when it is safe to change the Activity state.
Additionally the `onComplete` callback is a useful callback in determining 
when the Permission Dialog Flow is done using the current `activity` to display the SMS permissions flow.

The `onComplete` will be invoked when three conditions are satisfied:
1. the Onetime Worker for registering a user with Pngme's system has been instantiated
2. the Period Worker for periodically sending SMS data to Pngme's system has bee instantiated
3. the Permission Dialog Flow has exited

### `PngmeSdk.resetPermissionFlow()`

```kotlin
fun resetPermissionFlow(context: Context)
```

As noted above, the Permission Dialog Flow will only run the first time that the `go` method is invoked.
If your app needs to implement logic to show the Dialog Flow again, 
then you can reset the permission flow by calling `resetPermissionFlow`.
The next time you call `go`, the Permission Dialog Flow will show again.

Example:
```kotlin
PngmeSdk.go() // permissions flow runs
PngmeSdk.go()  // permission flow will NOT show again
PngmeSdk.resetPermissionFlow()
PngmeSdk.go()  // permission flow runs
```

See the code snippets in the below documentation on the example app 
for implementations where you might consider using this method to control the Permission Dialog Flow.

| var name | description |
| -------- | ----------- |
| context | the current app Context |

### `isPermissionGranted()`

```kotlin
fun isPermissionGranted(context: Context): Boolean
```

A simple helper function to indicate if the user has accepted the SMS permissions request.
Returns `true` if the user has accepted the SMS permission request.
Returns `false` if the user has denied the SMS permission request.

| var name | description |
| -------- | ----------- |
| context | the current app Context |

## example app
This repository is an example Android app, which uses the Pngme SDK.

TODO - complete this section
