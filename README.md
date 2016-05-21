# Forage [![LICENSE](https://img.shields.io/badge/License-Mozilla-blue.svg?style=flat)](http://mozilla.org/MPL/2.0/) [![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

<img src="app/src/main/res/drawable/icon_large.png" align="left"
width="200">

Forage is an open source Material Design geocaching Android app using MVP architecture and modern libraries. Forage  integrates with the [OpenCaching API](http://www.opencaching.us/okapi/introduction.html). 

**This project is currently under heavy development!**

This is my first Android project in order to learn the Android SDK and relevant Android libraries. Please critique my code! If you are just starting with Android check out [this wiki page](https://github.com/Plastix/Forage/wiki/Android-Learning-Resources) for a comphrensive list of learning resources that I used. 

## Libraries
* [Dagger 2](http://google.github.io/dagger/)
* [Retrofit 2](http://square.github.io/retrofit/)
* [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [Butterknife](http://jakewharton.github.io/butterknife/)
* [Realm](https://realm.io/)
* [Icepick](https://github.com/frankiesardo/icepick)
* [Signpost](https://github.com/mttkay/signpost) and [OkHttp-Signpost](https://github.com/pakerfeldt/okhttp-signpost)
* [Material Dialogs](https://github.com/afollestad/material-dialogs)
* [Google Support Libraries](http://developer.android.com/tools/support-library/index.html)
* [Retrolambda](https://github.com/evant/gradle-retrolambda)
* [Google Play Location Services](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary)
* [Google Play Maps](https://developers.google.com/maps/documentation/android-api/)

## Testing Libraries
* [JUnit](http://junit.org/junit4/)
* [Truth](https://github.com/google/truth)
* [Mockito](http://mockito.org/)
* [Powermock](https://github.com/jayway/powermock)
* [Roboelectric](http://robolectric.org/)
* [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/index.html)
* [UI Automator](http://developer.android.com/tools/testing-support-library/index.html#UIAutomator)

## Requirements
To compile and run the project you'll need:

- [Android SDK](http://developer.android.com/sdk/index.html)
- [Android Marshmallow (API 23)](http://developer.android.com/tools/revisions/platforms.html)
- Android SDK Tools
- Android SDK Build Tools `23.0.3`
- Android Support Repository
- Android Support Libraries
- JDK 7
- Valid API keys

API keys are not stored in the repo! Thus, you will need the following API keys:

1. [OkApi (US) key](http://www.opencaching.us/)
2. [Google Maps Android API key](https://developers.google.com/maps/documentation/android-api/)

Add the following to your global `gradle.properties` file. On OSX this file is stored at `~/.gradle/gradle.properties`.
```INI
OKAPI_US_CONSUMER_KEY=PUT OKAPI CONSUMER KEY HERE
OKAPI_US_CONSUMER_SECRET=PUT OKAPI CONSUMER SECRET HERE
GOOGLE_MAPS_API_KEY=PUT GOOGLE MAPS KEY HERE
```


## License
```
This Source Code is subject to the terms of the Mozilla Public License, v. 2.0. 
If a copy of the MPL was not distributed with this file, You can obtain one at 
http://mozilla.org/MPL/2.0/.
```
