# Github Searcher - AsanaRebel
### Summary

Android app capable of searching GitHub repositories by keywords as well as any qualifiers using the
v4 GraphQL API.

#### Architecture

This application was implemented using MVP architecture. I also used the repository pattern to 
provide access to a cache of objects which are kept in sync with a local data storage and a remote
 data storage. The local data storage provides persistence when the app subsequently opens with out
  internet. 

### Libraries/Dependencies
* [Dagger2](http://google.github.io/dagger/) and 
[Dagger-Android](https://google.github.io/dagger//android.html) for dependency injection to achieve
 a decoupled more testable logic
* [Room](https://developer.android.com/topic/libraries/architecture/room) for storing data locally 
creating redundancy on network failure
* [Retrofit2](http://square.github.io/retrofit/) for retrieving remote data
* [Retrofit2 converter-gson](https://github.com/square/retrofit/tree/master/retrofit-converters/gson)
 for converting json to java classes automatically
* [Picasso](http://square.github.io/picasso/) for loading images efficiently into the views and 
some transformations
* [Mockito](https://github.com/mockito/mockito)  for mocking classes for unit testing



