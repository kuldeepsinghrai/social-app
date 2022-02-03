# Social App

Social media demo app which can let people to Register and Login. User can store his picture on the database and we are fetching user data in his Profile.
User can create post and it will be stored in database. We are fetching all user's posts data in Home, where user can like and comment on the picture.
In search, we are showing all users from app where the current user can follow another users. We are showing current user's follower's Profile picture in his own profile.


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





