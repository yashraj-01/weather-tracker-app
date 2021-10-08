# weather-tracker-app

* The app basially stores the checked locations and weather inormation of those locations in a SQLite Database and then shows it on the map. The weather info is obtained from ![OpenWeatherMap](https://openweathermap.org/current#geo) API.

### Instructions to build the app

* Clone this repo locally and open it in **Android Studio**.
* Follow the instructions ![here](https://developers.google.com/maps/documentation/android-sdk/overview) to get the API key for Maps SDK to work.
* Copy this API Key to root level local.propoerties file as shown:
![](https://user-images.githubusercontent.com/63469151/133932452-5149467a-0439-4b6d-a17b-4d4a608d3b8f.png)

  ![](https://user-images.githubusercontent.com/63469151/133932530-a939fb52-851e-46a4-8050-70695826664a.jpg)

* Keep the name as **MAPS_API_KEY** only.
* The app is now ready to build.

### How to use the app?

* There are several fragments in this app which can be accessed using the navigation drawer.

#### MapFragment

* When clicked on the FAB button, the app requests for location permissions if it is not granted and if the permissions are granted then it will fetch the current location of the app. This location and its weather will be shown in the form of a Toast and it will also be stored in the database.
* When the app is reopened, all the stored locations/checkpoints will be shownon the map with **Blue** markers.
* When these markers are clicked, a snackbar will be shown displaying the location and weather stored in the database for that location. There will also be a **DELETE** button on the snackbar which will delete the particular checkpoint form the database and will also remove it from the map.

#### HistoryFragment

* This will display all the records stored in the database in the form of a list. Every item will have a delete button which will delete the record from the database.

#### SettingsFragment

* It has two options -- i. Enable/disable the feature to save the checkpoints to the database. ii. Delete database.
  1. When this option is disabled then the checkpoints won't be saved when we fetch the current location in **MapFragment**. Otherwise, the checkpoint is saved.
  2. This option will delete all the records from the database.
  
#### AboutFragment

* It displays general information about the app such as app name, app developer, app version and an about section.
