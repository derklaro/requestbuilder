# RequestBuilder [![Discord](https://img.shields.io/discord/499666347337449472.svg?color=7289DA&label=discord)](https://discord.gg/uskXdVZ) [![](https://jitpack.io/v/derklaro/requestbuilder.svg)](https://jitpack.io/#derklaro/requestbuilder)
This repository provides a simple request builder for url connections completely based on the java provided
classes without any external dependencies (main goal).

## Support our work
If you like the request builder and want to support our work you can **star** :star2: and join our [Discord](https://discord.gg/uskXdVZ).

## Found a bug or have a proposal?
Please
[**open an issue**](https://github.com/derklaro/reformcloud2-prefix-system/issues/new)
and ***describe the bug/proposal as detailed as possible*** and **look into your email if we have replied to your issue
and answer upcoming questions**.

## Dependencies
To include the project in yours you may use the following dependencies:

Maven repository:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Maven dependency:
```xml
<dependency>
    <groupId>com.github.derklaro</groupId>
    <artifactId>requestbuilder</artifactId>
    <version>1.0.5</version>
</dependency>
```

Gradle repository:
```groovy
maven {
    name 'jitpack.io'
    url 'https://jitpack.io'
}
```

Gradle dependency:
```groovy
compile group: 'com.github.derklaro', name: 'requestbuilder', version: '1.0.5'
```

## The builder class
```java
public class Main {
    public static void main(String[] args) {
        // Creates a simple builder with 'https://google.de' as target and no proxy
        RequestBuilder requestBuilder = RequestBuilder.newBuilder("https:google.de", Proxy.NO_PROXY);

        // We are now only accepting 'application/json' as result mime type
        requestBuilder.accepts(MimeTypes.getMimeType("json"));

        // We are sending the mime type 'application/json'
        requestBuilder.setMimeType(MimeTypes.getMimeType("json"));

        // We are following the redirects the server will make
        requestBuilder.enableRedirectFollow();

        // We are now able to use the output stream of the connection
        requestBuilder.enableOutput();

        // We are allowing the user to interact with the connection
        requestBuilder.enableUserInteraction();
    
        // We will now bypass the caches of the jvm
        requestBuilder.disableCaches();

        // We will now not accepting any incoming data
        requestBuilder.disableInput();

        // After 5 seconds the connect should time out
        requestBuilder.setConnectTimeout(5, TimeUnit.SECONDS);

        // We will make a get request. Possibilities are get, post, head, options, put, delete, trace
        requestBuilder.setRequestMethod(RequestMethod.GET);

        // We are setting the maximum amount of time the client will read from the connection
        requestBuilder.setReadTimeOut(5, TimeUnit.SECONDS);
    
        // Sets the specified cookie during the request
        requestBuilder.addCookies(new HttpCookie("AName", "AValue"));

        // Adds a header to the connection
        requestBuilder.addHeader("AKey", "AValue");

        // Adds a body to the request
        requestBuilder.addBody("AKey", "AValue");

        try (RequestResult requestResult = requestBuilder.fireAndForget()) {
            // now we can handle the result of the connection
        }

        requestBuilder.fireAndForgetAsynchronously().thenAccept(result -> {
            // handle the result of the request
        });
    }
}
```
## The request result
```java
public class Main {
    public static void main(String[] args) {
        RequestResult requestResult = requestBuilder.fireAndForget();
        
        // The status of the connection as an integer
        requestResult.getStatusCode();

        // The status of the request as wrapped object
        requestResult.getStatus();

        // Returns the result as string if the status code is == 200 else it will end up throwing an exception
        requestResult.getSuccessResultAsString();

        // Returns the result as string if the status code is != 200 else it will end up throwing an exception
        requestResult.getErrorResultAsString();

        // Returns always a string and decides between error or normal input
        requestResult.getResultAsString();

        // Get all cookies the server has set
        requestResult.getCookies();

        // Get all cookies the server has set in the specified header field
        requestResult.getCookies("MyHeader");

        // Get the output stream which is direction -> to server
        requestResult.getOutputStream();

        // Get the target input stream (direction -> to client)
        // Error -> returns the error stream (status != 200)
        // Default -> returns the default input stream (status == 200)
        // Choose -> chooses between error and default by result state
        requestResult.getStream(StreamType.CHOOSE);

        // If the connection to server has failed
        requestResult.hasFailed();

        // If the client is still connected to server
        requestResult.isConnected();

        // Closes the request and disconnects from the server
        try {
            requestResult.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
```

## Build this project
```
git clone https://github.com/derklaro/requestbuilder.git
cd requestbuilder/
mvn clean package
```
