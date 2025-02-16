To run the JobHunt.java program, you first need to export the dependencies into the classpath. The following command line example uses the same version that we used in making this program. Please edit accordingly.


export CLASSPATH=/Path/To/neo4j-java-driver-4.3.4.jar:/Path/To/spymemcached-2.12.3.jar:/Path/To/reactive-streams-1.0.3.jar:.


We then used this command to compile the program 


javac -classpath $CLASSPATH JobHunt.java JobHuntService.java CacheClient.java



Now, to run the program with your neo4j settings, please edit the constant in the JobHunt.java file
final String URI = "bolt://localhost:7687";//JobHuntService.URI;
final String USER = "neo4j";//JobHuntService.USER;
final String PASSWORD = "password";//JobHuntService.PASSWORD;
final String HOSTNAME = "localhost";//CacheClient.HOSTNAME;
final int CACHE_PORT = 11211;//CacheClient.CACHE_PORT;


The constructors of the JobHuntService and CacheClient uses the following format
 CacheClient(String hostname, int port)
 JobHuntService(String uri, String user, String password, CacheClient cache);


