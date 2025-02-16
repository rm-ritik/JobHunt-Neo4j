# JobHunt-Neo4j

This project simulates a job-hunting social network where users can manage job references and evaluate qualifications based on years of experience in specific fields. Built with Neo4j for graph-based storage and Java for application logic, the project allows for efficient relationship handling and querying. Memcached is integrated to cache query results, optimizing performance and reducing database load.

Features:
Add people to the database with attributes like name, years of experience, and work area.
Add references between individuals, representing professional recommendations.
Query qualifications to check if a person qualifies for a job based on references and experience.
Check how many un-employed people might support a given person based on references.
Caching with memcached to improve query speed and efficiency.
