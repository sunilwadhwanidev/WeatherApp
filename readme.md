🌦 Weather App (Android, Kotlin)

This is a simple Weather App built in Kotlin using MVVM architecture, Room Database, Retrofit, and Data Binding/View Binding.
It uses the OpenWeather API to fetch the current weather based on the user’s current location, and stores weather history locally for later viewing.
Add API Key to strings.xml

Open your app/src/main/res/values/strings.xml and add:

<string name="openweather_api_key">7d162e2a921754429f57bbb90aef0473</string>

📱 Features

 -Registration & Sign In (local authentication)

 -Fetch current location (City & Country) dynamically

 -Display current temperature in Celsius

 -Show sunrise and sunset time

 -Display sun/moon icon based on time (after 6PM → moon)

 -Show weather condition icon (rain, clear, etc.)

2 Tabs

-Current Weather (fetch live data)

-Weather History (list stored in Room DB, auto-updated using Flow)

-Room Database used to persist weather history

Tech Stack

-Kotlin

-Android Jetpack Components (ViewModel, LiveData, Lifecycle)

-Room Database (local storage)

-Retrofit2 + Gson (networking)

-Coroutines + Flow (async and reactive DB updates)

-FusedLocationProviderClient (for location)

-View Binding & Data Binding (UI)

-Material Components (TabLayout + ViewPager2)
