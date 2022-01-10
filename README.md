# Pngme Android (Kotlin) SDK & Sample App

*Welcome to the Pngme v2.x Kotlin SDK!*<br>
This Readme will cover how the SDK works, get-started basics, and a sample Android app.

### Legacy SDK
For documentation on the legacy SDK (v1.0.34) visit [here](https://developers.api.pngme.com/docs).

### React Native
For the v2.x *React Native* docs and sample app, visit [here](https://github.com/pngme/sample-android-app-react-native).

### Flutter
For the v2.x *Flutter* docs app, ~~visit her~~ *(COMING SOON)*

## Kotlin v2.x SDK - the basics
1. The SDK accomplishes three tasks:
   - register a mobile phone user with pngme's identity system
   - request permission for SMS from the user, with a [Permission Dialog Flow](.docs/permission_flow.gif)
   - periodically send SMS data to pngme's data processing pipeline
2. The SDK supports Android API level 16+
3. The SDK exposes three methods: a main entrypoint method, and two helper methods
4. Using the SDK requires an SDK `clientKey`. Sign up and get started _for free_ at the [Pngme admin webconsole](https://admin.pngme.com)

When the SDK has been successfully integrated, financial data extracted from a user's SMS will be accessible
in the [Pngme admin Webconsole](https://admin.pngme.com) or
via the Pngme REST APIs
(see the [API Reference docs](https://developers.api.pngme.com/reference/getting-started-with-your-api)).

## Get Started
To set up your project to use the Pngme SDK, follow these setup steps.

### _Step 1_
Resolve the JitPack package manager in your Gradle file.
Add the following to `build.gradle`.
```groovy
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
```

### _Step 2_
Add the SDK package to your Gradle file.
Add the following to `build.gradle`
(substitute the exact SDK version).
```groovy
    dependencies {
        implementation 'com.github.pngme:android-sdk:v2.Y.Z'
    }
```

### _Step 3_
Add your SDK `clientKey` to the project.
In the sample app, the `clientKey` is injected via the `local.properties` file.
*For production applications*, it is highly recommended that developers use a secure method for injecting the `clientKey`.
See here for some recommended methods:
[How to secure secrets in Android](https://blog.kotlin-academy.com/how-to-secure-secrets-in-android-android-security-01-a345e97c82be).

### _Step 4_
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

| var name | description |
| -------- | ----------- |
| activity | a reference to the current Activity |
| clientKey | the Pngme SDK key for your account |
| firstName | the mobile phone user's first name |
| lastName | the mobile phone user's last name |
| email | the mobile phone user's email address |
| phoneNumber | the mobile phone user's phone number, example `"23411234567"` |
| externalId | a unique identifier provided by your app (if none available, pass an empty string `""`)|
|isKycVerified | a boolean, indicating if your app has verified the user's identity using KYC |
| companyName | your company's name; this is used in the display header of the [Permission Dialog Flow](.docs/permission_flow.gif) |
| onComplete | a callback function that is called when the `go` method has completed |

The `go` method is the main entrypoint method for invoking the PngmeSdk.
The `go` method is idempotent, and can be invoked multiple times.

The `go` method performs three tasks.
1. register a `user` in Pngme's system using an Android Onetime Worker
2. show a [Permission Dialog Flow](.docs/permission_flow.gif) in the current Activity to request SMS permissions from the user -- 
   this _runs the first time, and only the first time_, that `go` is invoked
3. check for new SMS messages and send them to Pngme's system every 30 minutes using an Android Periodic Worker

#### The onComplete callback
The `go` method should be invoked and left to complete while the `activity` is in a [running state](https://developer.android.com/guide/components/activities/activity-lifecycle).
The `onComplete` callback is a useful callback, for example, 
in determining when it is safe to change the Activity state.
Additionally, the `onComplete` callback is a useful callback in determining 
when the `activity` is no longer in use by the Permission Dialog Flow.

The `onComplete` callback will be invoked when three conditions are satisfied:
1. the Onetime Worker for registering a user with Pngme's system has been instantiated
2. the Period Worker for periodically sending SMS data to Pngme's system has been instantiated
3. the Permission Dialog Flow has exited

### `PngmeSdk.resetPermissionFlow()`

```kotlin
fun resetPermissionFlow(context: Context)
```

| var name | description |
| -------- | ----------- |
| context | the current app Context |

As noted above, the Permission Dialog Flow will only run the first time that the `go` method is invoked.
If your app needs to implement logic to show the Dialog Flow again, 
then you can reset the permission flow by calling `resetPermissionFlow`.
The next time you call `go`, the Permission Dialog Flow will show again.

Example:
```kotlin
PngmeSdk.go(args) // permissions flow runs
PngmeSdk.go(args)  // permission flow will NOT show again
PngmeSdk.resetPermissionFlow(args)
PngmeSdk.go(args)  // permission flow runs
```

See the [code snippets](#Show Permissions Flow Multiple Times) in the below documentation on the example app 
for implementations where you might consider using this method to control the Permission Dialog Flow.

### `isPermissionGranted()`

```kotlin
fun isPermissionGranted(context: Context): Boolean
```

| var name | description |
| -------- | ----------- |
| context | the current app Context |

A simple helper function to indicate if the user has accepted the SMS permissions request.
Returns `true` if the user has accepted the SMS permission request.
Returns `false` if the user has denied the SMS permission request.

## Sample Android App
This repository is a sample Android app, which uses the Pngme SDK.
This app uses the `local.properties` file to inject the SDK `clientKey`.
Please note that this is for example purposes only.
As noted in [Step 3](#_Step 3_) of the get [started section](#get started), 
it is highly recommended that a production application use a more secure method of injecting the `clientKey` secret.

This app can be compiled and emulated locally, with or without a valid SDK `clientKey`.
If a valid SDK `clientKey` is used, then data can be sent thru to the Pngme system.

### Setup
Add the following to your `local.properties` file:
```text
SHARED_PREF_NAME=<my_pref>
CLIENT_KEY=<my_app_clientkey>
```

### Behavior
The sample app demonstrates a simple flow:
1. user creates an account with the app
2. the user goes to apply for a loan, and has the option of selecting to use the Pngme service
3. if the Pngme service is selected, the SDK is invoked, and the [Permission Flow](.docs/permission_flow.gif) is presented
4. when the permission flow exits, the user is presented with the loan application page

The SDK is implemented in the `PermissionFragment`, when the user clicks on the *Continue* button:
```kotlin
continueButton.setOnClickListener {
            // save state of checkBox
            if (usePngmeCheckBox.isChecked) {
                setPngmeAsChecked()
                val mainActivity = (activity as MainActivity)
                getUser()?.let { user ->
                    PngmeSdk.go(
                        mainActivity,
                        BuildConfig.CLIENT_KEY,
                        user.firstName,
                        user.lastName,
                        user.email,
                        user.phoneNumber,
                        "",
                        false,
                        MainActivity.COMPANY_NAME
                    ) {
                        navigateToLoadApplication()
                    }
                }
            } else {
                navigateToLoadApplication()
            }
        }
```

The app remembers the selection in step 2.
If the user chooses to enable the Pngme service, 
then the checkbox stays selected for all future loan applications.
The [Permission Flow](.docs/permission_flow.gif) is only showed the very first time, 
_regardless of if the user accepts or denies the permissions_.

#### Show Permissions Flow Multiple Times
Alternative behavior is to continue requesting SMS permissions if they were previously denied.
Adding the following snippet will reset the Permission Flow 
if SMS permissions had been previously denied but not [permanently ignored](.docs/permissions.md).

```kotlin
continueButton.setOnClickListener {
            // save state of checkBox
            if (usePngmeCheckBox.isChecked) {
                setPngmeAsChecked()
                if (!smsPermissionGranted() && smsNeverPermanentlyIgnored()) {
                    context?.let {
                        PngmeSdk.resetPermissionFlow(it)
                    }
                }
                val mainActivity = (activity as MainActivity)
                getUser()?.let { user ->
                    PngmeSdk.go(
                        mainActivity,
                        BuildConfig.CLIENT_KEY,
                        user.firstName,
                        user.lastName,
                        user.email,
                        user.phoneNumber,
                        "",
                        false,
                        MainActivity.COMPANY_NAME
                    ) {
                        navigateToLoadApplication()
                    }
                }
            } else {
                navigateToLoadApplication()
            }
        }
```

## Send SMS data locally
As noted above, the primary responsibility of the Pngme SDK is to send SMS data to the Pngme system.
This can be tested in a sample app running in the local emulator, 
assuming the emulated app is running with a valid SDK token.

The following text message is of a recognized format for the Stanbic bank sender: `Stanbic`.
```text
Acc:XXXXXX1111
CR:NGN4,000.00
Desc:HELLO WORLD! SAMPLE MESSAGE
Date:01-01-1970 01:01
Bal:NGN50,000.00
Call 0000 000 000 000
```

You can inject this fake SMS into the emulated phone by following these steps.
It is advisable that you pre-populate the emulated phone with the SMS _before_ running the sample app.

![Inject Fake SMS](.docs/inject_fake_sms.png)

1. Open the `more` window in the emulator settings
2. Navigate to the `phone` section
3. Set the sender to the string `Stanbic`
4. Copy/Paste the above same message into the message box
5. Hit `Send Message`

After following the above steps to send a fake SMS, run the sample app.
The fake SMS will be sent to the Pngme system using the SDK token from your Pngme account.
If the sample app runs successfully, the financial data in the text message will be accessible
via the [Pngme REST APIs](https://developers.api.pngme.com/reference/getting-started-with-your-api) or in the [Pngme webconsole](https://admin.pngme.com).

## Publishing to the Google Store
So you have a working app! Congrats! But... it's not over yet. 
You will still need to whitelist your app with the Google Play store.  
This is a special step necessary for any apps that require SMS permissions from the user.

The whitelisting process is not hard, but if you have never whitelisted an app before, you may want assistance. 
Pngme can offer support in whitelisting your app, free of charge.
Simply [contact us](mailto:whitelisting@pngme.com) 
and also visit our guide: [Going Live](https://developers.api.pngme.com/docs/going-live-with-the-sdk).
We'll help you get your app through the approval process faster than you can say `Hello World!`

If you insist on whitelisting your app without Pngme's assistance, 
please let us know and we will provide you with instructions. 
These will help you avoid setbacks when submitting your app for review.