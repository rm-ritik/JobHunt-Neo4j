
import java.io.IOException;

public class JobHunt {

    public static final String URI = "bolt://localhost:7687";//JobHuntService.URI;
    public static final String USER = "neo4j";//JobHuntService.USER;
    public static final String PASSWORD = "password";//JobHuntService.PASSWORD;
    public static final String HOSTNAME = "localhost";//CacheClient.HOSTNAME;
    public static final int CACHE_PORT = 11211;//CacheClient.CACHE_PORT;

    public static void main(String[] args) throws IOException {
        CacheClient cacheClient = new CacheClient(HOSTNAME, CACHE_PORT);
        System.out.println("Cache created");
        JobHuntService service = new JobHuntService(URI, USER, PASSWORD, cacheClient);
        if (args.length > 0) {
            switch (args[0]) {
                case "add-person":
                    if (args.length < 5) {
                        System.out.println("Insufficient arguments for add-person.");
                        break;
                    }
                    try {
                        String name = args[1];
                        int yearsExperience = Integer.parseInt(args[2]);
                        String workArea = args[3];
                        boolean isEmployed = Boolean.parseBoolean(args[4]);
                        service.addPerson(name, yearsExperience, workArea, isEmployed);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing numerical values: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;
                case "qualified":
                    if (args.length < 4) {
                        System.out.println("Insufficient arguments for qualified.");
                        break;
                    }
                    try {
                        String name = args[1];
                        String workArea = args[2];
                        int threshold = Integer.parseInt(args[3]);
                        service.qualified(name, workArea, threshold);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing numerical values: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;
                case "add-reference":
                    if (args.length < 3) {
                        System.out.println("Insufficient arguments for add-reference.");
                        break;
                    }
                    try {
                        String refName = args[1];
                        String name = args[2];
                        service.addReference(refName, name);
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;

                case "might-support":
                    if (args.length < 2) {
                        System.out.println("Insufficient arguments for might-support.");
                        break;
                    }
                    try {
                        String name = args[1];
                        service.mightSupport(name);
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;

                default:
                    System.out.println("Unknown command: " + args[0]);
                    break;
            }
        } else {
            System.out.println("No command provided.");
        }
	System.exit(0);
    }
}
