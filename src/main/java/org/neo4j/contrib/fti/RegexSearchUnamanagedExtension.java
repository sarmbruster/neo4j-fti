package org.neo4j.contrib.fti;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.regex.RegexQuery;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("{indexName}")
public class RegexSearchUnamanagedExtension {

    @Context
    private GraphDatabaseService graphDatabaseService;

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @GET
    @Path("{field}/{regex}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doSearch(@PathParam("indexName") String indexName,
                               @PathParam("field") String field,
                               @PathParam("regex") String regex) throws IOException {
        try (Transaction tx = graphDatabaseService.beginTx()) {
            IndexManager indexManager = graphDatabaseService.index();
            if (!indexManager.existsForNodes(indexName)) {
                throw new IllegalArgumentException("index " + indexName + " does not exist");
            }

            Index<Node> index = indexManager.forNodes(indexName);
            IndexHits<Node> hits = index.query(new RegexQuery(new Term(field, regex)));

            List<Long> result = new ArrayList<>();
            for (Node node: hits) {
                result.add(node.getId());
            }

            return Response.status(200)
                    .entity(OBJECT_MAPPER.writeValueAsBytes(result))
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }



}
