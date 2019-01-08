package hcs.neo

import org.neo4j.graphdb.factory.GraphDatabaseFactory
import java.io.File

fun main() {
    val db = GraphDatabaseFactory().newEmbeddedDatabase(File("/tmp/c"))
    db.execute("CREATE (test:User {name:'John'})")
    val res = db.execute("MATCH (user: User {name:'John'}) RETURN user")
    println(res.resultAsString())
}
