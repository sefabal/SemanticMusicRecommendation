import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {

        // create an empty model
        Model model = ModelFactory.createDefaultModel();
        String inputFileName = "DB/shorttrack.nt";

        try (InputStream in = new FileInputStream(inputFileName)) {
            RDFParser.create()
                    .source(in)
                    .lang(RDFLanguages.NT)
                    .errorHandler(ErrorHandlerFactory.errorHandlerStrict)
                    .parse(model);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!model.isEmpty()) {
            System.out.println("Model loaded successfully!");

            //model.write(System.out);

            //http://musicbrainz.org/artist/24f1766e-9635-4d58-a4d4-9413f9f98a4c#_

            String queryString = "PREFIX foaf:      <http://xmlns.com/foaf/0.1/>\n" +
                    "SELECT ?artist ?track \n" +
                    "WHERE \n{\n" +
                    "?artist foaf:name \"Johann Sebastian Bach\" .\n" +
                    "?artist foaf:made ?track .\n" +
                    "}";

            System.out.println("Querying : " + queryString);

            Query query = QueryFactory.create(queryString);
            String artistId = "";
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                System.out.println("Result size = " + results.getResourceModel().size());
                for (; results.hasNext(); ) {
                    QuerySolution soln = results.nextSolution();
                    //RDFNode x = soln.get("varName") ;       // Get a result variable by name.

                    Resource r = soln.getResource("artist"); // Get a result variable - must be a resource
                    Resource t = soln.getResource("track");
                    artistId = r.toString();

                    System.out.println(artistId + " " + t.toString());

                }
                //Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
            }
        }
    }

}
