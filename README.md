# Popular Movies

- Project for [Android Developer Nanodregree](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801) by [Udacity](https://www.udacity.com/).
- In this project, you will build an app to help users discover popular and recent movies. You will build a clean UI, sync to a server, and present information to the user. 

# Navigate

  - [Popular Movies](#popular-movies)
  
  - [Project Meets Required Specifications](#project-meets-required-specifications)
    - [Common Project Requirements](#common-project-requirements)
    - [User Interface - Layout](#user-interface---layout)
    - [User Interface - Function](#user-interface---function)
    - [Network API Implementation](#network-api-implementation)
    - [Data Persistence](#data-persistence)

  - [Extras](#extras)
  
  - [Getting Started](#getting-started)
  
  - [Art](#art)
    - [App Poster](#app-poster)
    - [Nexus 5 Screens](#nexus-5-screens)
    - [Nexus 7 Screens](#nexus-7-screens)
    - [Pixel C Screens](#pixel-c-screens)

  - [License Apache 2.0](#license)

# Project Meets Required Specifications

## Common Project Requirements

- [x] App is written solely in the Java Programming Language.
- [x] Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
- [x] UI contains an element (i.e a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
- [x] UI contains a screen for displaying the details for a selected movie.
- [x] Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.
- [x] App conforms to common standards found in the [Android Nanodegree General Project Guidelines.](http://udacity.github.io/android-nanodegree-guidelines/core.html)

## User Interface - Layout

- [x] UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
- [x] Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
- [x] UI contains a screen for displaying the details for a selected movie.
- [x] Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
- [x] Movie Details layout contains a section for displaying trailer videos and user reviews.

## User Interface - Function

- [x] When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
- [x] When a movie poster thumbnail is selected, the movie details screen is launched.
- [x] When a trailer is selected, app uses an Intent to launch the trailer.
- [x] In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

## Network API Implementation

- [x] In a background thread, app queries the ```/movie/popular``` or ```/movie/top_rated``` API for the sort criteria specified in the settings menu.
- [x] App requests for related videos for a selected movie via the ```/movie/{id}/videos``` endpoint in a background thread and displays those details when the user selects a movie.
- [x] App requests for user reviews for a selected movie via the ```/movie/{id}/reviews``` endpoint in a background thread and displays those details when the user selects a movie.

## Data Persistence

- [x] The titles and ids of the user's favorite movies are stored in a 
```ContentProvider``` backed by a SQLite database. This ```ContentProvider``` is updated whenever the user favorites or unfavorites a movie.
- [x] When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the ```ContentProvider```.

# Extras

Some extra features added:
  - Short by upcomig option addded.
  - Store favorites, top rated, most popular and upcoming movies into separeated databases.

# Getting Started

  - Popular Movies App uses ```The Movie Database API```. In order to run the app you must create your own API key.
  - [Click to create an API key](https://www.themoviedb.org/account/signup)
  - When you get it, just set it in: ```gradle.properties``` -> ```MyTheMovieDBApiToken="INSERT_YOUR_KEY_HERE"```


# Art

## App Poster
![screen](../master/art/popular_movies_base_poster.jpg)

## Nexus 5 Screens
![screen](../master/art/popular_movies_nexus5_poster.jpg)

## Nexus 7 Screens
![screen](../master/art/popular_movies_nexus7_poster.jpg)

## Pixel C Screens
![screen](../master/art/popular_movies_pixelC_poster.jpg)

# License

```Copyright 2017 Endika Aguilera

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at```

    http://www.apache.org/licenses/LICENSE-2.0

```Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.```
