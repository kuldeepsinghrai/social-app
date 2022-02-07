# Social App

Social media demo app which can let people to Register and Login. User can store his picture on the database and we are fetching user data in his Profile.
User can create post and it will be stored in database. We are fetching all user's posts data in Home, where user can like and comment on the picture.
In search, we are showing all users from app where the current user can follow another users. We are showing current user's follower's Profile picture in his own profile.

## Screenshots
<img src="screenshots/ss_login.jpg" width=25% height=25%> <img src="screenshots/ss_signup.jpg" width=25% height=25%>




## Technologies Used

 - [Firebase Authentication](https://firebase.google.com/docs/auth)

 - [Firebase Realtime Database](https://firebase.google.com/docs/database/)

 - [Firebase Storage](https://firebase.google.com/docs/storage)

 - [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics)


## Dependencies Added

 ### [Glide](https://github.com/bumptech/glide) 
 Glide is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.


```gradle
repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation 'com.github.bumptech.glide:glide:4.12.0'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
}
```

 ### [RoundedImageView](https://github.com/vinc3m1/RoundedImageView) 
A fast ImageView (and Drawable) that supports rounded corners (and ovals or circles) based on the original [example from Romain Guy](http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/). It supports many additional features including ovals, rounded rectangles, ScaleTypes and TileModes.


```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.makeramen:roundedimageview:2.3.0'
}
```


 ### [CircleImageView](https://github.com/hdodenhof/CircleImageView) 
A fast circular ImageView perfect for profile images. This is based on [RoundedImageView from Vince Mi](https://github.com/vinc3m1/RoundedImageView) which itself is based on [techniques recommended by Romain Guy](http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/).

```gradle
dependencies {
   implementation 'de.hdodenhof:circleimageview:3.1.0'
}
```


 ### [DiagonalLayout](https://github.com/florent37/DiagonalLayout) 
With Diagonal Layout explore new styles and approaches on material design

```gradle
dependencies {
    implementation 'com.github.florent37:diagonallayout:1.0.9'
}
```

 ### [TimeAgo](https://github.com/marlonlom/timeago) 
Simple java library for displaying dates as relative time ago language.

```gradle
dependencies {
    implementation 'com.github.marlonlom:timeago:4.0.3'
}
```

 ### [ShimmerRecyclerView](https://github.com/sharish/ShimmerRecyclerView) 
A custom recycler view with shimmer views to indicate that views are loading. The recycler view has a built-in adapter to control the shimmer appearance and provide two methods -

* showShimmerAdapter() - set up a demo adapter a predefined number of child demo views.
* hideShimmerAdapter() - restores your adapter to show the actual child elements.


```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.sharish:ShimmerRecyclerView:v1.3'
}

```

 ### [StoryView](https://github.com/OMARIHAMZA/StoryView) 

```gradle
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.OMARIHAMZA:StoryView:1.0.2-alpha'
}

```

 ### [CircularStatusView](https://github.com/3llomi/CircularStatusView) 
A Simple View that draws portions of a Circle depending on a number , just like what WhatsApp has done for Status


```gradle
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
  implementation 'com.github.3llomi:CircularStatusView:V1.0.2'
}

```


