<p align="center">
  <img src="https://admin.pngme.com/logo.png" alt="Pngme" width="100" height="100">
  <h3 align="center">Pngme Android (Kotlin) SDK Guide</h3>
</p>

This documentation covers how to use the Pngme SDK with Kotlin.

You can find similar documentation for [Expo](https://github.com/pngme/sample-android-app-react-native-expo), [Flutter-Kotlin](https://github.com/pngme/sample-android-app-flutter), [Flutter-Java](https://github.com/pngme/sample-android-app-flutter_java) and [React Native](https://github.com/pngme/sample-android-app-react-native).

## Setup

1. The SDK supports Android API version 16+
1. The SDK enables your app to:
   1. Register a mobile phone user with Pngme
   1. Periodically send data to Pngme to analyze financial events
1. Using the SDK requires an **SDK Token**
   - [**Sign up for a free Pngme Dashboard account**](https://admin.pngme.com) then access your SDK token from the [Keys page](https://admin.pngme.com/keys)
   - Use the `test` SDK token during development but replace with the `production` SDK token before deploying your app to the Google Play store
   - A custom consent dialog for requesting SMS permissions from the user. You can follow the design guide [here](https://drive.google.com/file/d/1SAc4Wt62mYUleDfSme3GMG9yyDT8omwP/view) to create a custom dialog.

<p align="center">
  <img src="https://raw.githubusercontent.com/pngme/sample-android-app-flutter/main/.docs/webconsole_keys.png" width=450 height=300/>
</p>

After integrating the SDK, financial data will be accessible in the [Pngme Dashboard](https://admin.pngme.com/users) and via the [Pngme REST APIs](https://developers.api.pngme.com/reference/).

## Integrating the SDK

### Step 1

Add the JitPack package manager to `/app/build.gradle`.

```groovy
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
```

### Step 2

Add the following dependencies to `/app/build.gradle`.

```groovy
    dependencies {
    implementation 'com.github.pngme:android-sdk:v7.0.4'
    }
```

### Step 3

Add your **SDK Token** to `local.properties`.

```text
PNGME_SDK_TOKEN=XXXXXXXXXX
```

> ⚠️ We recommend that additional measures be taken to protect the **SDK Token** when implementing in a production app. See here for some recommended methods: [How to secure secrets in Android](https://blog.kotlin-academy.com/how-to-secure-secrets-in-android-android-security-01-a345e97c82be).

### Step 4

Call the `PngmeSdk.go() `  in your app.

```kotlin
fun go(
    activity: AppCompatActivity,
    clientKey: String,
    firstName: String = "",
    lastName: String = "",
    email: String = "",
    phoneNumber: String = "",
    externalId: String,
    companyName: String,
    onComplete: Callback? = null,
    dialogStyle: PngmeDialogStyle? = null
)
```

The `go` method performs three tasks.

1. register a `user` in Pngme's system using an Android Onetime Worker
2. check for new SMS messages and send them to Pngme's system every 30 minutes using an Android Background Worker

| Field       | Description                                                                                                        |
| ----------- | ------------------------------------------------------------------------------------------------------------------ |
| activity    | a reference to the current Activity                                                                                |
| clientKey   | the SDK Token from the [Pngme Dashboard Keys page](https://admin.pngme.com/keys)                                   |
| firstName | the mobile phone user's first name (optional)                                                   |
| lastName   | the mobile phone user's last name (optional)                               |
| email | the mobile phone user's email address (optional)                                              |
| phoneNumber | the mobile phone user's phone number, example `"23411234567"`                                                      |
| externalId  | a unique identifier provided by your app (if none available, pass an empty string `""`)                            |
| companyName | your company's name; this is used in the display header of the [Permission Dialog Flow](.docs/permission_flow.gif) |
| hasAcceptedTerms | Set the value to 'true' if the user has accepted the terms and conditions when invoking the 'goWithCustomDialog' method. Defaults to false |
| onComplete  | a callback function that is called when the `go` method has completed                                              |
| dialogStyle  |optional custom styling for the dialog                                       |

## Customizable Dialog Styling

The SDK allows you to customize the appearance of the Pngme dialog to match your app's branding. All styling properties are optional, allowing you to customize only the elements you want to change.

### PngmeDialogStyle Configuration Reference

| Property | Type | Description | Default Behavior |
|----------|------|-------------|------------------|
| **Colors** | | | |
| `primaryColor` | `Int?` | Main brand color used for icons, checkboxes, and link highlighting | Uses Pngme brand color |
| `backgroundColor` | `Int?` | Background color of the entire dialog | White background |
| `textColor` | `Int?` | Color for all regular text content | Dark gray/black text |
| `buttonBackgroundColor` | `Int?` | Background color of the main action button | Uses `primaryColor` or Pngme brand color |
| `buttonTextColor` | `Int?` | Text color for the main action button | White text |
| `linkTextColor` | `Int?` | Color for privacy policy and EULA links | Uses `primaryColor` if null |
| **Typography** | | | |
| `titleTextSize` | `Float?` | Title text size in SP (scale-independent pixels) | System default title size |
| `bodyTextSize` | `Float?` | Body text size in SP for descriptions | System default body text size |
| `buttonTextSize` | `Float?` | Button text size in SP | System default button text size |
| `customFontFamily` | `Int?` | Font resource ID for custom typography | System default font |
| `customTypeface` | `Typeface?` | Direct Typeface object for custom fonts | System default typeface |
| **Header & Branding** | | | |
| `headerLogoDrawable` | `Drawable?` | Replace the default Pngme logo in header | Pngme logo |
| `headerLogoWidth` | `Int?` | Custom header logo width in dp | Auto-sized |
| `headerLogoHeight` | `Int?` | Custom header logo height in dp | Auto-sized |
| `closeIconDrawable` | `Drawable?` | Custom close/dismiss icon | Default X icon |
| `closeIconTint` | `Int?` | Tint color for the close icon | Dark gray |
| **Company Branding** | | | |
| `companyLogo` | `Drawable?` | Your company logo displayed below header | No company logo |
| `companyLogoWidth` | `Int?` | Company logo width in dp | Auto-sized |
| `companyLogoHeight` | `Int?` | Company logo height in dp | Auto-sized |
| **Content Icons** | | | |
| `smsIcon` | `Drawable?` | Custom icon for SMS permission section | Default SMS icon |
| `smsIconTint` | `Int?` | Tint color for SMS icon | Uses `primaryColor` |
| `privacyIcon` | `Drawable?` | Custom icon for privacy section | Default privacy/shield icon |
| `privacyIconTint` | `Int?` | Tint color for privacy icon | Uses `primaryColor` |
| **Custom Text Content** | | | |
| `customTitle` | `String?` | Override the main dialog title | "Help [CompanyName] understand your finances" |
| `customSmsDescription` | `String?` | Override SMS permission explanation text | Standard SMS permission description |
| `customPrivacyDescription` | `String?` | Override privacy section description | Standard privacy protection description |
| `customButtonText` | `String?` | Override main action button text | "Continue" |
| `customPrivacyPolicyText` | `String?` | Completely replace terms acceptance text | Standard terms text with links |
| **Button Styling** | | | |
| `buttonCornerRadius` | `Float?` | Button corner radius in dp | System default button radius |
| `buttonElevation` | `Float?` | Button shadow/elevation in dp | Material Design default |
| **Layout & URLs** | | | |
| `contentPadding` | `Int?` | Content padding around dialog elements in dp | Standard material padding |
| `customScrollbarDrawable` | `Int?` | Custom scrollbar drawable resource | System default scrollbar |
| `privacyPolicyUrl` | `String?` | Custom privacy policy URL | Pngme privacy policy |
| `eulaUrl` | `String?` | Custom End User License Agreement URL | Pngme EULA |

### Creating a Custom Style

```kotlin
val customStyle = PngmeDialogStyle(
    // Brand Colors
    primaryColor = ContextCompat.getColor(this, R.color.your_brand_color),
    backgroundColor = ContextCompat.getColor(this, R.color.your_background_color),
    textColor = ContextCompat.getColor(this, R.color.your_text_color),
    
    // Button Styling
    buttonBackgroundColor = ContextCompat.getColor(this, R.color.your_button_color),
    buttonTextColor = ContextCompat.getColor(this, R.color.white),
    buttonCornerRadius = 8f, // Rounded corners
    buttonElevation = 4f,    // Material shadow
    
    // Company Branding
    headerLogoDrawable = ContextCompat.getDrawable(this, R.drawable.your_company_logo),
    companyLogo = ContextCompat.getDrawable(this, R.drawable.your_brand_logo),
    
    // Custom Content
    customTitle = "Help us understand your finances",
    customSmsDescription = "We analyze your financial messages to provide personalized insights.",
    customPrivacyDescription = "Your data is encrypted and protected following industry standards.",
    customButtonText = "I agree to continue",
    
    // Typography
    titleTextSize = 20f,
    bodyTextSize = 16f,
    buttonTextSize = 16f,
    customFontFamily = R.font.your_custom_font,
    
    // Custom Links
    privacyPolicyUrl = "https://yourcompany.com/privacy",
    eulaUrl = "https://yourcompany.com/terms",
    linkTextColor = ContextCompat.getColor(this, R.color.your_link_color),
    
    // Icon Customization
    smsIcon = ContextCompat.getDrawable(this, R.drawable.custom_sms_icon),
    privacyIcon = ContextCompat.getDrawable(this, R.drawable.custom_privacy_icon),
    closeIconDrawable = ContextCompat.getDrawable(this, R.drawable.custom_close_icon)
)
```

### Using Custom Styling

#### Option 1: Set a global style for all dialogs

```kotlin
// Set a default style that will apply to all dialogs
PngmeSdk.setDefaultStyle(customStyle)

// Later, when launching the SDK, it will use the default style
PngmeSdk.go(
    activity = this,
    clientKey = "your-sdk-token",
    // Other parameters...
)
```

#### Option 2: Style a specific dialog

```kotlin
// Apply style to a specific dialog instance
PngmeSdk.go(
    activity = this,
    clientKey = "your-sdk-token",
    firstName = "John", 
    lastName = "Doe",
    // Other parameters...
    dialogStyle = customStyle  // Pass your custom style here
)
```

### Styling Best Practices

1. **Color Accessibility**: Ensure sufficient contrast between text and background colors for accessibility compliance.

2. **Brand Consistency**: Use your app's existing color palette and typography to maintain brand consistency.

3. **Icon Guidelines**: 
   - Use vector drawables when possible for crisp scaling
   - Ensure icons are legible at different screen densities
   - Consider using monochrome icons that work well with tinting

4. **Text Sizing**: 
   - Use SP (scale-independent pixels) for text sizes to respect user accessibility settings
   - Test with different system font sizes

5. **Custom URLs**: When providing custom privacy policy and EULA URLs, ensure they're mobile-optimized and accessible.

### Returning to Default Styling

To clear the default style and return to the original Pngme styling:

```kotlin
PngmeSdk.clearDefaultStyle()
```

### Complete Styling Example

Here's a comprehensive example showing how to create a fully branded dialog:

```kotlin
val brandedStyle = PngmeDialogStyle(
    // Core brand colors
    primaryColor = ContextCompat.getColor(this, R.color.brand_primary),
    backgroundColor = Color.WHITE,
    textColor = Color.parseColor("#333333"),
    
    // Branded button
    buttonBackgroundColor = ContextCompat.getColor(this, R.color.brand_accent),
    buttonTextColor = Color.WHITE,
    buttonCornerRadius = 12f,
    buttonElevation = 6f,
    
    // Company branding
    headerLogoDrawable = ContextCompat.getDrawable(this, R.drawable.company_logo),
    headerLogoWidth = 120,
    headerLogoHeight = 40,
    
    // Custom messaging
    customTitle = "${getString(R.string.app_name)} Financial Insights",
    customSmsDescription = "We'll analyze your transaction messages to provide personalized financial insights and help you make better money decisions.",
    customPrivacyDescription = "Your SMS data is processed securely on-device and encrypted before transmission. We never store your personal messages.",
    customButtonText = "Enable Financial Insights",
    
    // Typography
    titleTextSize = 22f,
    bodyTextSize = 16f,
    buttonTextSize = 18f,
    customFontFamily = R.font.roboto_medium,
    
    // Custom policies
    privacyPolicyUrl = "https://yourcompany.com/privacy-policy",
    eulaUrl = "https://yourcompany.com/user-agreement",
    
    // Branded icons
    smsIcon = ContextCompat.getDrawable(this, R.drawable.ic_message_branded),
    privacyIcon = ContextCompat.getDrawable(this, R.drawable.ic_security_branded),
    smsIconTint = ContextCompat.getColor(this, R.color.brand_primary),
    privacyIconTint = ContextCompat.getColor(this, R.color.brand_primary)
)

// Apply the branded styling
PngmeSdk.go(
    activity = this,
    clientKey = BuildConfig.PNGME_SDK_TOKEN,
    firstName = userFirstName,
    lastName = userLastName,
    email = userEmail,
    phoneNumber = userPhoneNumber,
    externalId = userId,
    companyName = getString(R.string.company_name),
    dialogStyle = brandedStyle,
    onComplete = {
        // Handle completion
    }
)
```

## SDK Methods Reference

### go()

Standard implementation with default Pngme dialog flow:

```kotlin
fun go(
    activity: AppCompatActivity,
    clientKey: String,
    firstName: String = "",
    lastName: String = "",
    email: String = "",
    phoneNumber: String = "",
    externalId: String,
    companyName: String,
    onComplete: Callback? = null,
    dialogStyle: PngmeDialogStyle? = null
)
```

### `isPermissionGranted()`
Check if permissions have been granted:

```kotlin
fun isPermissionGranted(context: Context): Boolean
```

| Field   | Description             |
| ------- | ----------------------- |
| context | the current app Context |

This indicates if the user has accepted the SMS permissions request:

- Returns `true` if the user has accepted the SMS permission request.
- Returns `false` if the user has denied the SMS permission request.

### Sending test data

This can be tested in a sample app running in the local emulator,
assuming the emulated app is running with a valid SDK token.

Android Emulator can simulate incoming SMS messages, and we can use this to test the Pngme SDK locally.

The following text message is of a recognized format for the Stanbic bank sender: `Stanbic`.

```text
Acc:XXXXXX1111
CR:NGN4,000.00
Desc:HELLO WORLD! SAMPLE MESSAGE
Bal:NGN50,000.00
```

You can inject this fake SMS into the emulated phone by following these steps.

> Once the app gets the permissions form the user it will instantly start sending existing SMS messages to the Pngme system. This results in messages being seen much sooner than SMS received after the app was installed.
>
> The background worker processes new messages every 30 minutes, so new messages will take at least 30 minutes to appear in the webconsole.

![Inject Fake SMS](.docs/inject_fake_sms.png)

1. Open the `more` window in the emulator settings
2. Navigate to the `phone` section
3. Set the sender to the string `Stanbic` or one of the senders from our [supported institutions](https://developers.api.pngme.com/reference/supported-institutions)
4. Copy/Paste the above same message into the message box
5. Hit `Send Message`

After following the above steps to send a fake SMS, run the sample app.
The fake SMS will be sent to the Pngme system using the SDK token from your Pngme account.
If the sample app runs successfully, the financial data in the text message will be accessible
via the [Pngme REST APIs](https://developers.api.pngme.com/reference/getting-started-with-your-api) or in the [Pngme webconsole](https://admin.pngme.com).

## Next steps

See [Going Live with the SDK](https://developers.api.pngme.com/docs/going-live-with-the-sdk) to learn more about the whitelisting process with the Google Play store.
