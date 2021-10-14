package principal;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jsoup.Jsoup;

public class Main {

    public static void main(String[] args) throws InterruptedException
    {
        String urlWiki = "http://fr.wikipedia.org/wiki/Ain_(d%C3%A9partement)";
        HttpClient client = new HttpClient(urlWiki);
        client.start();
        client.join();
        String result = client.getResponse();

        System.out.println(result);


        try {
            //pour l'instalation local
            MongoClient clientMongo = new MongoClient();

            MongoDatabase db = clientMongo.getDatabase("dep"); //Je choisis la base de données = use dep
            MongoCollection coll = db.getCollection("dep"); // Je choisis la colletion

            //Bson filter = new BasicDBObject("region","Occitaine");

            MongoCursor<Document> cursor = coll.find().cursor(); //tous les deps

            while(cursor.hasNext()) {
                System.out.println(cursor.next().get("nom")); //boucle pour voir tous les deps
            }
            System.out.println("Ok");

            /*
            String html = "<html><head><title>Essai</title></head></html>";
            org.jsoup.nodes.Document docHtml = Jsoup.parse(html);

            String docHtmlToString = docHtml.getElementsByTag("title").get(0).text();
            System.out.println(docHtmlToString);
            */

            org.jsoup.nodes.Document html = Jsoup.connect("http://fr.wikipedia.org/wiki/Ain_(d%C3%A9partement)").get();
            String docHtmlTitleToString = html.getElementsByTag("title").get(0).text();
            String docHtmlToString = html.getElementsByTag("main").get(0).text();

            //System.out.println(docHtmlTitleToString);
            //System.out.println(docHtmlToString);

            //ajout du champ description
            BasicDBObject filtre = new BasicDBObject("no_dept", "01");
            Document doc = new Document("$set", new Document("description", docHtmlTitleToString + docHtmlToString));
            db.getCollection("dep").updateOne(filtre, doc);


        } catch (Exception e) {
            e.printStackTrace();
            }
        }
    }
