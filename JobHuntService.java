
import org.neo4j.driver.*;

import static org.neo4j.driver.Values.parameters;


public class JobHuntService {
    public static final int EXPIRE_SECONDS = 60;
    private final Driver driver;
    private final CacheClient cache;

    public JobHuntService(String uri, String user, String password, CacheClient cache) {
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        System.out.println("Driver created");
        this.cache = cache;
    }

    void addPerson(String name, int yearsExperience, String workArea, boolean status) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> tx.run("CREATE (p:Person {name: $name, yearsExperience: $yearsExperience, workArea: $workArea, status: $status})",
                    parameters("name", name, "yearsExperience", yearsExperience, "workArea", workArea, "status", status)));
            System.out.println("SUCCESS");
        } catch (Exception e) {
            System.out.println("FAILURE");
        }
    }


    /*
     * This method should output either QUALIFIED or NOT_QUALIFIED depending on
     * whether the sum of year_experience's of the references for name who work in
     * workArea is greater than threshold.
     * @param name The name of the person to check for references.
     * @param workArea The work area to check for references.
     * @param threshold The threshold to compare the sum of year_experience's against.
     *
     */
    void qualified(String name, String workArea, int threshold) {
        //create cache key by concatenating and removing space in name and workArea
        String keyname = name;
        String keyworkArea = workArea;
        keyname = keyname.replace(" ", "");
        keyworkArea = keyworkArea.replace(" ", "");
        String key = (keyname + keyworkArea);
        if (cache.exists(key)) {
            int totalYears = (int) cache.get(key);
            if (totalYears > threshold) {
                System.out.println("QUALIFIED");
            } else {
                System.out.println("NOT_QUALIFIED");
            }
            return;
        }
        try (Session session = driver.session()) {
            int totalYears = session.readTransaction(new TransactionWork<Integer>() {
                @Override
                public Integer execute(Transaction tx) {
                    Result result = tx.run("MATCH (p:Person {name: $name})<-[:WORKED_WITH]-(r:Person {workArea: $workArea}) RETURN sum(r.yearsExperience) as totalYears",
                                parameters("name", name, "workArea", workArea));
                    return result.single().get("totalYears").asInt();
                }
            });
            System.out.println("total years: " + totalYears);
            if (totalYears > threshold) {
                System.out.println("QUALIFIED");
            } else {
                System.out.println("NOT_QUALIFIED");
            }
            cache.set(key, EXPIRE_SECONDS, totalYears);
        } catch (Exception e) {
            System.out.println("Error e: " + e);
            System.out.println("FAILURE");
            //cache.set(key, 60, 0);
        }
    }

    //Person1 is acting as a reference to the person2
    void addReference(String person1, String person2) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> tx.run("MATCH (p1:Person {name: $person1}) MATCH (p2:Person {name: $person2}) MERGE (p1)-[:WORKED_WITH]->(p2)",
                    parameters("person1", person1, "person2", person2)));
            System.out.println("SUCCESS");
        } catch (Exception e) {
            System.out.println("FAILURE");
        }
    }

    // TODO Implement mightSupport methods here
    /*
    * This method should output the integer number of persons who are not currently employed for which there is some path
    * to that person via references beginning with the person named name
    * @param name The name of the person to check for references.
    * @return The number of persons who are not currently employed for which there is some path to that person via references beginning with the person named name
     */
    void mightSupport(String name) {
        //create cache key by concatenating and removing space in name
        String key = name;
        key = key.replace(" ", "");
        //check the cache
        if (cache.exists(key)) {
            int total = (int) cache.get(key);
            System.out.println("total (from cache): " + total);
            return;
        }
        try (Session session = driver.session()) {
            int total = session.readTransaction(new TransactionWork<Integer>() {
                @Override
                public Integer execute(Transaction tx) {
                    Result result = tx.run("MATCH (p:Person {name: $name})-[:WORKED_WITH*]->(r:Person) WHERE r.status = false RETURN count(r) as total",
                            parameters("name", name));
                    return result.single().get("total").asInt();
                }
            });
            System.out.println("total: " + total);
            cache.set(key, EXPIRE_SECONDS, total);
        } catch (Exception e) {
            System.out.println("Error e: " + e);
            System.out.println("FAILURE");
        }
    }
}

