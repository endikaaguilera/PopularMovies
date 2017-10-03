# PopularMovies
Project for Android Developer Nanodregree by Udacity.

## Nexus 5 Screens
![screen](../master/art/popular_movies_nexus5_poster.jpg)

## Nexus 7 Screens
![screen](../master/art/popular_movies_nexus7_poster.jpg)

## Pixel C Screens
![screen](../master/art/popular_movies_pixelC_poster.jpg)

# Project Meets Required Specifications:
## Common Project Requirements
- [x] App is written solely in the Java Programming Language.
- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines.

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
- [x] In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
- [x] App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.
- [x] App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

## Data Persistence
- [x] The titles and ids of the user's favorite movies are stored in a <code>ContentProvider</code> backed by a SQLite database. This <code>ContentProvider</code> is updated whenever the user favorites or unfavorites a movie.
- [x] When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the ContentProvider.

# Extras
Some extra features added:
* Short by upcomig option addded.
* Store favorites, top rated, most popular and upcoming movies into separeated databases.
