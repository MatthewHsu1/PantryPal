import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.io.InputStream;

import org.bson.Document;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import static com.mongodb.client.model.Updates.set;

import javax.faces.context.FlashWrapper;
import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.json.JSONException;
import java.util.List;
import java.util.ArrayList;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Model {
    private Database db;
    private Authentication authManager;
    private Logger log = LoggerFactory.getLogger(Model.class);
    private List<UserSession> userList;

    public Model() {
        this.db = new Database();
        this.authManager = new Authentication();
        this.userList = new ArrayList<>();
    }

    public String audioToText() {
        String message = "";

        try {
            message = Whisper.audioToText(new File("recording.wav"));
        } catch (IOException e) {
            e.printStackTrace(); // or handle the exception in an appropriate way
        } catch (URISyntaxException e) {
            e.printStackTrace(); // or handle the exception in an appropriate way
        }

        return message;
    }

    public String audioToText(File file) {
        String message = "";
        try {
            message = Whisper.audioToText(new File("recording.wav"));
        } catch (IOException e) {
            e.printStackTrace(); // or handle the exception in an appropriate way
        } catch (URISyntaxException e) {
            e.printStackTrace(); // or handle the exception in an appropriate way
        }

        return message;
    }

    public JSONObject getNewRecipe(String mealType, String ingredients) {
        String prompt = ChatGPT.formPrompt(mealType, ingredients);
        JSONObject response = ChatGPT.generateRecipe(prompt);
        response.put("MealType", mealType);
        return response;
    }

    public String getNewImage(String prompt) {
        String url = "";
        try {
            url = ChatGPT.generateImage(prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
    
    public Database getDatabase(){
        return db;
    }

    public String skipLogin(){
        return authManager.SkipLoginIfRemembered();
    }

    public boolean login(String username, String password) {
        UserSession us = authManager.login(username, password);
        if (us != null) {
            return true;
        } 
        return false;
    }

    public void markAutoLogin(String username) {
        authManager.markAutoLoginStatus(username);
    }

    public boolean checkUserExist(String username) {
        return authManager.checkUserExists(username);
    }

    public boolean createUser(String username, String password, String firstName, String lastName, String phone) {
        return authManager.createUser(username, password, firstName, lastName, phone);
    }

    public boolean sessionExist(String username) {
        for(UserSession us: userList) {
            if (us.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

}

class ChatGPT {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static final String DALLE_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String API_KEY = "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";
    private static final String MODEL = "gpt-3.5-turbo";
    private static final String DALLE_MODEL = "dall-e-3";

    private static Logger log = LoggerFactory.getLogger(ChatGPT.class);

    public static String generate(String prompt) throws
    IOException, InterruptedException, URISyntaxException {

        HttpClient client = HttpClient.newHttpClient();
        int maxTokens = 500;

        JSONObject requestBody = new JSONObject();
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "user").put("content", prompt));
        requestBody.put("model", MODEL);
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 0.5);

        // Create the request object
        HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", API_KEY))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
        
        JSONObject responseJson = new JSONObject(response.body());

        String generatedText = "";

        try {
            JSONArray choices = responseJson.getJSONArray("choices");
            generatedText = choices.getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");
        }
        catch (org.json.JSONException e) {
            System.out.println(e);
            System.out.println(responseJson);
        }

        return generatedText;
    }

    public static String generateImage(String prompt) throws
    IOException, InterruptedException, URISyntaxException {

        HttpClient client = HttpClient.newHttpClient();

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", DALLE_MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("n", 1);
        requestBody.put("size", "1024x1024");
        requestBody.put("response_format", "url");

        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create(DALLE_ENDPOINT))
            .header("Content-Type", "application/json")
            .header("Authorization", String.format("Bearer %s", API_KEY))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject responseJson = new JSONObject(response.body());
        JSONArray dataArray = responseJson.getJSONArray("data");
        JSONObject firstItem = dataArray.getJSONObject(0);
        return firstItem.getString("url");
    }

    public static String formPrompt(String mealType, String ingredients) {
        String prompt = "What is a step-by-step " + mealType + " recipe I can make using " + ingredients + "? Please provide a Title, ingredients, and steps.";
        return prompt;
    }

    public static JSONObject generateRecipe(String prompt) {
        JSONObject response = new JSONObject();
        try{
            String originalResponse = ChatGPT.generate(prompt);
            String[] parts = originalResponse.split("\n\n", 3);
            String[] tidyParts = new String[] {parts[0].replace("Title: ", ""), parts[1], parts[2].replace("Steps:\n", "")};
            response.put("Title", tidyParts[0]);
            response.put("Ingredients", tidyParts[1]);
            String tidySteps = tidyParts[2].replaceAll("\n+", "\n");
            String[] steps = tidySteps.split("\n");
            for(int i = 0; i < steps.length; i++) {
                if(!steps[i].isEmpty()) {
                    response.put(String.valueOf(i+1), steps[i]);
                }
            }
            response.put("numSteps", steps.length);
        } catch (Exception e) {
            log.error(e.toString());
        }
    
        return response;
    }
}

/**
 * Class: Recipe
 * Store stuff from the Chat GPT response into this object
 * 
 */
class Recipe {
    private String recipeTitle; // name of the recipe
    private String description;
    private String mealType; // type of the meal as selected by the user
    private Map<String, String> ingredients; // a map of ingredients and their quantities
    private List<String> directions; // a list of ingredients

    Recipe() {
    }

    // getters and setters for Recipe
    public String getTitle() {
        return recipeTitle;
    }

    public void setTitle(String title) {
        this.recipeTitle = title;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    } 
}

class Whisper {
    
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";

    private static void writeParameterToOutputStream(
        OutputStream outputStream,
        String parameterName,
        String parameterValue,
        String boundary
    ) 
    throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
            (
                "Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n"
            ).getBytes()
        );
        outputStream.write((parameterValue + "\r\n").getBytes());
    }


    // Helper method to write a file to the output stream in multipart form data format
    private static void writeFileToOutputStream(
        OutputStream outputStream,
        File file,
        String boundary
    ) 
    throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
        (
            "Content-Disposition: form-data; name=\"file\"; filename=\"" +
            file.getName() +
            "\"\r\n"
        ).getBytes()
    );

        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());
    
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }
    

    // Helper method to handle a successful response
    private static String handleSuccessResponse(HttpURLConnection connection)
    throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject responseJson = new JSONObject(response.toString());

        String generatedText = responseJson.getString("text");

        return generatedText;
    }


    // Helper method to handle an error response
    private static void handleErrorResponse(HttpURLConnection connection)
    throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
    }
    
    public static String audioToText(File file) throws IOException, URISyntaxException {

        // Set up HTTP connection
        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
        "Content-Type",
        "multipart/form-data; boundary=" + boundary
        );
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();

        // Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

        // Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);

        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        // Flush and close output stream
        outputStream.flush();
        outputStream.close();

        // Get response code
        int responseCode = connection.getResponseCode();
        String generatedText = "";

        // Check response code and handle response accordingly
        if (responseCode == HttpURLConnection.HTTP_OK) {
            generatedText = handleSuccessResponse(connection);
        } else {
            handleErrorResponse(connection);
        }

        // Disconnect connection
        connection.disconnect();

        return generatedText;
    }
}

class Database {
    private String uri;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> recipeCollection;
    private Logger log = LoggerFactory.getLogger(Database.class);

    public Database(){
        this.uri = "mongodb+srv://team13:CohNSwNGemiYmOOI@cluster0.1nejphw.mongodb.net/?retryWrites=true&w=majority";
        
        // Initialize MongoClient without try-with-resources
        this.client = MongoClients.create(uri);
        this.database = client.getDatabase("PantryPal");
        this.recipeCollection = database.getCollection("Recipe");
        // System.out.println("Successfully connected to MongoDB! :)");
    }

    public void close() {
        if (client != null) {
            this.client.close();
        }
    }

    public void insert(JSONObject recipeJSON) {
        List<Document> stepList = new ArrayList<>();
        for(int i = 1; i <= recipeJSON.getInt("numSteps"); i++) {
            stepList.add(new Document("Step", recipeJSON.get(String.valueOf(i))));
        }

        Document recipe = new Document("_id", new ObjectId());
        recipe.append("Title", recipeJSON.getString("Title"))
              .append("Ingredients", recipeJSON.getString("Ingredients"))
              .append("Steps", stepList)
              .append("MealType", recipeJSON.getString("MealType"))
              .append("User", recipeJSON.getString("User"))
              .append("Time", recipeJSON.getString("Time"))
              .append("Image", recipeJSON.getString("Image"))
              .append("ImageTime", recipeJSON.getString("ImageTime"));

        recipeCollection.insertOne(recipe);
    }

    public JSONObject get(String title) {
        Document recipe = recipeCollection.find(new Document("Title", title)).first();
        // ArrayList<String> recipeDetail = new ArrayList<>();
        JSONObject recipeJSON = new JSONObject();
        if(recipe != null) {
            recipeJSON.put("id", recipe.get("_id").toString());
            recipeJSON.put("Title", recipe.getString("Title"));
            recipeJSON.put("Ingredients", recipe.getString("Ingredients"));
            List<Document> stepList = (List<Document>)recipe.get("Steps");
            for (int i = 0; i < stepList.size(); i++) {
                recipeJSON.put(String.valueOf(i+1), stepList.get(i).getString("Step"));
            }
            recipeJSON.put("MealType", recipe.getString("MealType"));
            recipeJSON.put("User", recipe.getString("User"));
            recipeJSON.put("Time", recipe.getString("Time"));
            recipeJSON.put("numSteps", stepList.size());
            recipeJSON.put("Image", recipe.getString("Image"));
            recipeJSON.put("ImageTime", recipe.getString("ImageTime"));
        }
        else {
            log.error(String.format("Title '%s' not found in database", title));
        }
        return recipeJSON;
    }

    public JSONObject getByID(String id) {
        Document recipe = recipeCollection.find(new Document("_id", new ObjectId(id))).first();
        // ArrayList<String> recipeDetail = new ArrayList<>();
        JSONObject recipeJSON = new JSONObject();
        if(recipe != null) {
            recipeJSON.put("id", recipe.get("_id").toString());
            recipeJSON.put("Title", recipe.getString("Title"));
            recipeJSON.put("Ingredients", recipe.getString("Ingredients"));
            List<Document> stepList = (List<Document>)recipe.get("Steps");
            for (int i = 0; i < stepList.size(); i++) {
                recipeJSON.put(String.valueOf(i+1), stepList.get(i).getString("Step"));
            }
            recipeJSON.put("MealType", recipe.getString("MealType"));
            recipeJSON.put("User", recipe.getString("User"));
            recipeJSON.put("Time", recipe.getString("Time"));
            recipeJSON.put("numSteps", stepList.size());
            recipeJSON.put("Image", recipe.getString("Image"));
            recipeJSON.put("ImageTime", recipe.getString("ImageTime"));
        }
        else {
            log.error(String.format("ID '%s' not found in database", id));
        }
        return recipeJSON;
    }

    public void updateIngredient(String title, String newIngredient) {
        Bson filter = eq("Title", title);
        Bson updateOperation = set("Ingredients", newIngredient);
        UpdateResult updateResult = recipeCollection.updateMany(filter, updateOperation);
        System.out.println(updateResult);
    }

    public void updateSteps(String title, List<String> newSteps) {
        List<Document> stepList = new ArrayList<>();
        for(String newStep: newSteps) {
            stepList.add(new Document("Step", newStep));
        }
        Bson filter = eq("Title", title);
        Bson updateOperation = set("Steps", stepList);
        UpdateResult updateResult = recipeCollection.updateMany(filter, updateOperation);
        System.out.println(updateResult);
    }

    public void updateImage(String title, String newURL) {
        Bson filter = eq("Title", title);
        Bson updateOperation = set("Image", newURL);
        UpdateResult updateResult = recipeCollection.updateMany(filter, updateOperation);
        System.out.println(updateResult);
    }

    public void updateImageTime(String title, String newTime) {
        Bson filter = eq("Title", title);
        Bson updateOperation = set("ImageTime", newTime);
        UpdateResult updateResult = recipeCollection.updateMany(filter, updateOperation);
        System.out.println(updateResult);
    }

    public void delete(String title) {
        Bson filter = eq("Title", title);
        DeleteResult result = recipeCollection.deleteMany(filter);
        System.out.println(result);
    }

    public JSONArray getAllTitles(String username) {
        JSONArray recipes = new JSONArray();
        Bson filter = eq("User", username);
        try (MongoCursor<Document> cursor = recipeCollection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                recipes.put(cursor.next().getString("Title"));
            }
        }
        return recipes;
    }
}

class Authentication{
    private String uri;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> userCollection;

    public Authentication(){
        this.uri = "mongodb+srv://team13:CohNSwNGemiYmOOI@cluster0.1nejphw.mongodb.net/?retryWrites=true&w=majority";

        // Initialize MongoClient without try-with-resources
        this.client = MongoClients.create(uri);
        this.database = client.getDatabase("PantryPal");
        this.userCollection = database.getCollection("Users");
        // System.out.println("Successfully connected to MongoDB! :)");
    }

    public Authentication(MongoClient client, MongoDatabase database, MongoCollection<Document> userCollection){
        this.uri = "mongodb+srv://team13:CohNSwNGemiYmOOI@cluster0.1nejphw.mongodb.net/?retryWrites=true&w=majority";

        this.client= MongoClients.create(uri);; 
        this.database = client.getDatabase("PantryPal");
        this.userCollection = database.getCollection("Users");
    }

    public void close() {
        if (client != null) {
            this.client.close();
        }
    } 

    public boolean createUser(String username, String password, String firstName, String lastName, String phone) {
        try{
            // Generate a bcrypt hash of the password
            String hashedPassword = hashPassword(password);

            // Store the user's information in the database
            Document userDocument = new Document("username", username)
                    .append("password", hashedPassword)
                    .append("phone", phone)
                    .append("firstName", firstName)
                    .append("lastName", lastName);
            userCollection.insertOne(userDocument);
            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    public Document mockCreateUser(String username, String password, String firstName, String lastName, String phone) {
        
            // Store the user's information in the database
            Document userDocument = new Document("username", username)
                    .append("password", password)
                    .append("phone", phone)
                    .append("firstName", firstName)
                    .append("lastName", lastName);
            userCollection.insertOne(userDocument);
            return userDocument;
    }

    public boolean checkUserExists(String username){
        Document userDocument = userCollection.find(new Document("username", username)).first();
        if (userDocument != null){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean mockCheckUserExists(Document doc, ArrayList<Document> list ){
        return list.contains(doc);
    }

    public boolean verifyUser(String username, String password) {
        // Retrieve the user's information from the database
        Document userDocument = userCollection.find(new Document("username", username)).first();

        if (userDocument != null) {
            // Extract the stored hashed password
            String storedHashedPassword = userDocument.getString("password");
            //return storedHashedPassword.equals(password);

            // Verify the provided password against the stored hash
            return verifyPassword(password, storedHashedPassword);
        }
        
        return false; // User not found
    }

    public UserSession login(String username, String password) {
        // Verify the user's credentials
        if (verifyUser(username, password)) {
            return new UserSession(username);
        }
        return null; // Authentication failed
    }

    public void markAutoLoginStatus(String username) {
        long timestamp = System.currentTimeMillis();
        int random = new SecureRandom().nextInt();
        String uniqueToken = timestamp + "_" + random;

        Bson filter = eq("username", username);
        Bson updateOperation = set("token", uniqueToken);
        userCollection.updateOne(filter, updateOperation);

        try{
            FileWriter file = new FileWriter("Device Identifier");
            file.write(uniqueToken);
            file.close();

        }catch (IOException e){
            System.out.println("Error writing to device identifier.");
        }
    }

    public String SkipLoginIfRemembered(){
         try (BufferedReader reader = new BufferedReader(new FileReader("Device Identifier"))) {
                String line;
                if((line = reader.readLine() ) != null){
                    Bson filter = eq("token", line);
                    if(userCollection.find(filter).first() != null){
                        return (String) userCollection.find(filter).first().get("username");
                    }
                }
                else{
                    return null;
                }

        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private String hashPassword(String password) {
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        return bcryptHashString;
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }

}

class UserSession {
    private String username;

    public UserSession(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

