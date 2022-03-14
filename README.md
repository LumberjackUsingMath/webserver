# Multithreaded webserver implemented using Java


### Description: 
This is a Maven project that uses the Maven standard project structure. It implements a webserver that serves files, which 
can be requested by HTTP requests of version 1.1 . As I focused on making a proof of concept, I only implemented 
the GET request. In particular, the GET request specifies the requested .txt file in the header
"requestedFile". If the file is available in the directory "Data", the server will send it out. 

### Program Structure 

The main function creates a server instance of the class 
[Server](https://github.com/LumberjackUsingMath/webserver/blob/master/src/main/java/Server.java) 
that takes the port, number of workers, and the interface 
[WebApplication](https://github.com/LumberjackUsingMath/webserver/blob/master/src/main/java/Applications/WebApplication.java) 
as argument. In this case, the web-application [FileServerApp](https://github.com/LumberjackUsingMath/webserver/blob/master/src/main/java/Applications/FileServerApp.java)
is taken. Inside the main class, the server is started by executing the method run, which in turn lets the server wait 
for incoming requests. If a request comes, the server forwards it to the thread pool by wrapping it in a new instance of 
the class [ClientHandler](https://github.com/LumberjackUsingMath/webserver/blob/master/src/main/java/ClientHandler.java), 
which implements the interface Runnable. The ClientHandler defines how to process the request. In particular, the 
input stream of the request is parsed to an instance of [HttpRequest](https://github.com/LumberjackUsingMath/webserver/blob/master/src/main/java/Http/HttpRequest.java).
Then, the request is forwarded to the web-application, which returns a instance of a extension of the abstract class 
[HttpResponse](https://github.com/LumberjackUsingMath/webserver/blob/master/src/main/java/Http/FileHttpResponse.java). 
That object sends then the response to the client back. Depending on the header "Connection" of the request,
the ClientHandler keeps the request alive, or closes it. If it is kept alive, it will be closed by timeout. 






## Main Resource:
The main resource for this implementation is this [repository](https://github.com/warchildmd/webserver).
I adjusted the code a little, in order to contribute. The major changes are:
1. Reduced functionality 
2. Request file by header
3. Different logging 
4. New project structure 
5. Added keep-alive behaviour 

### My learning resources
[Make a Simple HTTP Server in Java](https://www.youtube.com/watch?v=FNUdLeGfShU&list=PLAuGQNR28pW56GigraPdiI0oKwcs8gglW) (Youtube series)

[Thread Pools in Java](https://www.geeksforgeeks.org/thread-pools-java/)

[HttpClient Quick Start](https://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html)

[Apache Maven Tutorial: Einführung in Build-Prozess & Abhängigkeiten für Anfänger ohne Vorkenntnisse](https://www.youtube.com/watch?v=ExKq23bNABk)

[Java für Fortgeschrittene](https://www.youtube.com/playlist?list=PLNmsVeXQZj7oirQMpjPjrmNx4vcVIGIGY) (Youtube series)



### My prerequisites :
1. I did not know what a Maven project is.
2. My Java skills were from a university course in 2019, so my code is very "junior". 
3. I used an REST API via Python once
4. I did not know the HTTP protocol 

Now, I know all of them. So, thank you very much for the challenge. 

