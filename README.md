# Github Searcher - AsanaRebel
### Summary

Android app capable of searching GitHub repositories by keywords as well as any qualifiers using the
v4 GraphQL API.

#### Architecture

The application uses MVP Architecture together with dependency injection to provide the objects 
needed for each view/presenter dynamically. Also it applies the repository pattern to provide access
 to a cache of objects which are kept in sync with a local data storage and a remote data storage. 
 The local data storage provides persistence when the app subsequently opens without internet.
 
 The main repository list also has infinite scrolling capabilities requesting the github api more
 repositories based on the id of the last repository fetched.

### Libraries/Dependencies
* [Dagger2](http://google.github.io/dagger/) and 
[Dagger-Android](https://google.github.io/dagger//android.html) for dependency injection to achieve 
a decoupled more testable logic
* [Room](https://developer.android.com/topic/libraries/architecture/room) for storing data locally 
creating redundancy on network failure
* [Retrofit2](http://square.github.io/retrofit/) for retrieving remote data
* [Retrofit2 converter-gson](https://github.com/square/retrofit/tree/master/retrofit-converters/gson)
* [okHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
  for intercepting and logging all request and response made using retrofit 
for converting json to java classes automatically
* [Picasso](http://square.github.io/picasso/) for loading images efficiently into the views and some 
transformations
* [Android Debug DB](https://github.com/amitshekhariitbhu/Android-Debug-Database) 
 for live browsing DB content and debugging issues related to local data storage
* [Mockito](https://github.com/mockito/mockito)  for mocking classes for unit testing

### Limitations/Known issues
* For the app to be able to be used out of the box I have included a very limited API key in:
data/source/remote/RepositoriesRemoteDataSource. however this key will be invalidated in the future.
 To solve this eventual problem you will have to 
 [create your own](https://developer.github.com/v4/guides/forming-calls/#authenticating-with-graphql) 
 and change it in the above mentioned class  
* Because of time constraints the user list inside the repository details has currently only the 
users requested on the GetRepositoriesApiRequest, it will not load more items on the fly.
* After extensive testing and integrations attempts I decided not to use the apollo-android library
which seemed to be the best option to query GraphQL backends as I was not able to achieve the validation
of my scheme and queries even after testing then on GitHub explorer. However using retrofit to map
the objects has a direct and easy to understand approach, and the queries/response were also translated
 to a POJO class GetRepositoriesApiRequest/RepositoriesApiResponse 



