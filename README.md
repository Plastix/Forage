Forage [![LICENSE](https://img.shields.io/badge/License-Mozilla-blue.svg?style=flat)](http://mozilla.org/MPL/2.0/) [![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
======
> A geocaching Android app using MVP architecture and modern libraries 

_This project is currently under heavy development!_

This is my first Android project in order to learn the Android SDK and relevant Android libraries. Please critique my code! If you are just starting with Android check out [this wiki page](https://github.com/Plastix/Forage/wiki/Android-Learning-Resources) for a comphrensive list of learning resources that I used. 

Libraries
-------
* [Dagger 2](http://google.github.io/dagger/)
* [Retrofit 2](http://square.github.io/retrofit/)
* [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [Butterknife](http://jakewharton.github.io/butterknife/)
* [Realm](https://realm.io/)
* [Icepick](https://github.com/frankiesardo/icepick)
* [Google Support Libraries](http://developer.android.com/tools/support-library/index.html)
* [Google Play Location Services](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary)
* [Google Play Maps](https://developers.google.com/maps/documentation/android-api/)

Testing Libraries
-------
* [JUnit](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)
* [Powermock](https://github.com/jayway/powermock)
* [Roboelectric](http://robolectric.org/)

Requirements
-------
To compile and run the project you'll need:

- [Android SDK](http://developer.android.com/sdk/index.html).
- [Android Marshmallow (API 23)](http://developer.android.com/tools/revisions/platforms.html).
- Android SDK Tools
- Android SDK Build Tools 23.0.2
- Android Support Repository
- Android Support Libraries
- JDK 7
- Valid API keys

API keys are not stored in the repo! Thus, you will need the following API keys:

1. [OkApi (US) key](http://www.opencaching.us/)
2. [Google Maps Android API key](https://developers.google.com/maps/documentation/android-api/)

Create a `local.properties` file in the root directory of the project and add the following to it:
```
okapi_us_consumer_key=PUT OKAPI CONSUMER KEY HERE
okapi_us_consumer_secret=PUT OKAPI CONSUMER SECRET HERE
google_maps_api_key=PUT GOOGLE MAPS KEY HERE
```


License
-------
```
This Source Code is subject to the terms of the Mozilla Public License, v. 2.0. 
If a copy of the MPL was not distributed with this file, You can obtain one at 
http://mozilla.org/MPL/2.0/.
```
