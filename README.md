Forage (In Development) [![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
======
A geocaching Android app using MVP architecture and modern libraries 

Libraries
-------
* [Dagger 2](http://google.github.io/dagger/)
* [Retrofit 2](http://square.github.io/retrofit/)
* [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [Butterknife](http://jakewharton.github.io/butterknife/)
* [Realm](https://realm.io/)
* [Android Iconics](https://github.com/mikepenz/Android-Iconics)
* [Google Support Libraries](http://developer.android.com/tools/support-library/index.html)
* [Google Play Location Services](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary)
* [Google Play Maps](https://developers.google.com/maps/documentation/android-api/)

Requirements
-------
API keys are not stored in the repo! If you want to build the project yourself you need the following:

1. OkApi (US) key
2. Google Maps Android API key

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
