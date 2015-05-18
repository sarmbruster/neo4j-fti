package org.neo4j.contrib.fti;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("populate")
public class IndexPopulationUnamanagedExtension {

    @Context
    private GraphDatabaseService graphDatabaseService;

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @POST
    @Path("{indexName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response populate(@PathParam("indexName") String indexName,
                               @QueryParam("label") String label,
                               @QueryParam("properties") List<String> properties,
                             @QueryParam("batchSize") @DefaultValue("10000") int batchSize
            ) throws IOException {
        Transaction tx = graphDatabaseService.beginTx();
        try {
            IndexManager indexManager = graphDatabaseService.index();
            if (!indexManager.existsForNodes(indexName)) {
                throw new IllegalArgumentException("index " + indexName + " does not exist");
            }

            long opsCount = 0;
            Index<Node> index = indexManager.forNodes(indexName);
            try (ResourceIterator<Node> iter = graphDatabaseService.findNodes(DynamicLabel.label(label))) {

                while (iter.hasNext()) {
                    opsCount++;
                    if (opsCount % batchSize == 0) {
                        tx.success();
                        tx.close();
                        tx = graphDatabaseService.beginTx();
                    }
                    if (opsCount % 100_000 == 0) {
                        System.out.printf("indexed " + opsCount + " nodes\n");
                    }

                    Node node = iter.next();

                    for (String prop: properties) {
                        Object value = node.getProperty(prop, null);
                        if (value!=null) {
                            index.add(node, prop, value);
                        }
                    }
                }
            }

/*
            IndexHits<Node> hits = index.query(new RegexQuery(new Term(field, regex)));

            List<Long> result = new ArrayList<>();
            for (Node node: hits) {
                result.add(node.getId());
            }
*/

            tx.success();
            return Response.status(200)
                    .entity(OBJECT_MAPPER.writeValueAsBytes(opsCount))
                    .type(MediaType.APPLICATION_JSON).build();
        } finally {
            tx.close();
        }
    }

}
