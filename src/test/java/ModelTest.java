import static com.mongodb.client.model.Filters.eq;
import org.junit.jupiter.api.BeforeEach;
import org.bson.Document;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import static com.mongodb.client.model.Updates.set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.io.File;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


class ModelTest {
     private MockAudioRecorder mockAudioRecorder;

    @BeforeEach
    void setUp() {
        mockAudioRecorder = new MockAudioRecorder();
    }
    //--------------------------------------------------------------------------
    //-------------------------------CHATGPT TESTS------------------------------
    //--------------------------------------------------------------------------
    @Test
    void testGenerateWithValidPrompt() {
        String prompt = "test prompt";
        String expected = "Successfully invoked ChatGPT API for the prompt: " + prompt;
        String actual = "";
        try {
              actual = MockChatGPT.generate(prompt);
        } catch (Exception e) {
            System.out.println("Method generate() failed with an exception: "+ e );
        }
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    void testGenerateWithEmptyPrompt() {
        String prompt = "";
        String expected = "Successfully invoked ChatGPT API for the prompt: " + prompt;
        String actual = "";
        try {
              actual = MockChatGPT.generate(prompt);
        } catch (Exception e) {
            System.out.println("Method generate() failed with an exception: "+ e );
        }
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    
    @Test
    void testFormPromptWithValidInputs() {
        String mealType = "dinner";
        String ingredients = "chicken, rice";
        String expected = "formPrompt(): What is a step-by-step " + mealType + " recipe I can make using " + ingredients + "? Please provide a Title, ingredients, and steps.";
        String actual = MockChatGPT.formPrompt(mealType, ingredients);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    void testFormPromptWithEmptyInputs() {
        String mealType = "";
        String ingredients = "";
        String expected = "formPrompt(): What is a step-by-step " + mealType + " recipe I can make using " + ingredients + "? Please provide a Title, ingredients, and steps.";
        String actual = MockChatGPT.formPrompt(mealType, ingredients);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    void testGenerateRecipeWithValidPrompt() {
        String prompt = "test recipe prompt";
        JSONObject expected = new JSONObject();
        expected.put("Title", "Mock Recipe Title");
        expected.put("Ingredients", "Ingredient 1, Ingredient 2");
        expected.put("Steps", "Step 1. Do something\nStep 2. Do something else");
        expected.put("numSteps", 2);
        JSONObject actual = MockChatGPT.generateRecipe(prompt);
        System.out.println(expected.toString());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void testGenerateRecipeWithEmptyPrompt() {
        String prompt = "";
        JSONObject expected = new JSONObject();
        expected.put("Title", "Mock Recipe Title");
        expected.put("Ingredients", "Ingredient 1, Ingredient 2");
        expected.put("Steps", "Step 1. Do something\nStep 2. Do something else");
        expected.put("numSteps", 2);
        System.out.println(expected.toString());
        JSONObject actual = MockChatGPT.generateRecipe(prompt);
        assertEquals(expected.toString(), actual.toString());
    }

    //--------------------------------------------------------------------------
    //-------------------------AUDIORECORDER TESTS------------------------------
    //--------------------------------------------------------------------------
   

    @Test
    void startRecording_ShouldSetIsRecordingToTrue() {
        mockAudioRecorder.startRecording();
        assertTrue(mockAudioRecorder.isRecording());
    }

    @Test
    void startRecording_ShouldPrintCorrectMessage() {
        // Capture standard output (System.out) to verify the print statement
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        mockAudioRecorder.startRecording();
        assertEquals("Mock startRecording called\n", outContent.toString());

        // Reset System.out to its original stream
        System.setOut(System.out);
    }

    @Test
    void stopRecording_ShouldSetIsRecordingToFalse() {
        // Start and then stop recording
        mockAudioRecorder.startRecording();
        mockAudioRecorder.stopRecording();

        assertFalse(mockAudioRecorder.isRecording());
    }

    @Test
    void stopRecording_ShouldPrintCorrectMessage() {
        // Capture standard output (System.out) to verify the print statement
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        mockAudioRecorder.stopRecording();
        assertEquals("Mock stopRecording called\n", outContent.toString());

        // Reset System.out to its original stream
        System.setOut(System.out);
    }

}




// // @ExtendWith(MockitoExtension.class)
// class ModelTest {
//     @Mock
//     private AudioRecorder mockedAudioRecorder;
//     @Mock
//     private Database mockedDatabase;
//     @Mock
//     private Whisper mockedWhisper;
//     @Mock
//     private Model mockedModel;
//     @Mock
//     private ChatGPT mockedChatGPT;

//     private Authentication auth;
//     private MongoClient mockClient;
//     private MongoDatabase mockDatabase;
//     private MongoCollection<Document> mockCollection;
//     private MongoCollection<Document> mockUserCollection;

//     @BeforeEach
//     void setUp() {

//         MockitoAnnotations.openMocks(this);
//         mockClient = mock(MongoClient.class);
//         mockDatabase = mock(MongoDatabase.class);
//         mockUserCollection = mock(MongoCollection.class);

//         when(mockDatabase.getCollection("Users")).thenReturn(mockUserCollection);
//         when(mockClient.getDatabase("PantryPal")).thenReturn(mockDatabase);
//         auth = new Authentication(mockClient, mockDatabase, mockUserCollection);
//     }

//     @Test
//     void testInsert() {
//         try {
//             JSONObject stepJSON = new JSONObject();
//             stepJSON.put("item1", "item1");
//             stepJSON.put("item2", "item2");
//             stepJSON.put("item3", "item3");
//             ((Database) Mockito.doThrow(new Exception()).when(mockDatabase)).insert(stepJSON);
//         } catch (Exception e) {
//             assertTrue(true);
//         }

//     }

//     // Test method for retrieving data from the database
//     @Test
//     void testGet() {
//         String title = "hey";
        
//         JSONObject json = new JSONObject();
//         json.put("test", "test");
//         when(mockedDatabase.get(title)).thenReturn(json);
//         assertEquals(mockedDatabase.get(title), json);
//     }

//     // Test method for updating steps in the database
//     @Test
//     void testUpdateSteps() {
//         try {
//             List<String> stepList = new ArrayList<String>();
//             stepList.add("TestStep");
//             String title = "TestTitle";
//             Mockito.doThrow(new Exception()).when(mockedDatabase).updateSteps(title, stepList);
//         } catch (Exception e) {
//             assertTrue(true);
//         }

//     }

//     // Test method for deleting an entry from the database
//     @Test
//     void testDelete() {
//         try {
//             String title = "TestTitle";
//             Mockito.doThrow(new Exception()).when(mockedDatabase).delete(title);
//         } catch (Exception e) {
//             assertTrue(true);
//         }
//     }

//     // Test method for stopping audiorecording
//     @Test
//     void testStopRecording() {
//         try {
//             Mockito.doThrow(new Exception()).when(mockedAudioRecorder).stopRecording();
//         } catch (Exception e) {
//             assertTrue(true);
//         }
//     }

//     // Test method for starting audio recoding
//     @Test
//     void testStartRecording() {
//         try {
//             Mockito.doThrow(new Exception()).when(mockedAudioRecorder).startRecording();
//         } catch (Exception e) {
//             assertTrue(true);
//         }
//     }

//     // Given: Caitlin clicks on create account.
//     // When: CAitlin fills in all the info for a new accouNY
//     // And: Caitlin hits create.
//     // Then: A new user is created and stored.
//     @Test
//     void testAuthentication_BDD0() {
//         Authentication authentication = Mockito.mock(Authentication.class);
//         Document test1 = authentication.mockCreateUser("testUser", "testPassword", "Cait", "lastName", "testPhone");
//         ArrayList<Document> list = new ArrayList();
//         list.add(test1);
//         assertTrue(auth.mockCheckUserExists(test1, list));
//     }

//     // Given: Jacob clicks on create account.
//     // When: Jacob fills in all the info for a new accouNY
//     // And: Jacon hits create.
//     // Then: A new user is created and stored.
//     @Test
//     void createUserSuccessfully_BDD1() {
//         when(mockUserCollection.insertOne(any())).thenReturn(null); 

//         boolean result = auth.createUser("username", "password", "Jacob", "Bro", "8582331234");

//         verify(mockUserCollection).insertOne(any());
//         assertTrue(result, "User creation should return true");
//     }

//     /**
//      * Test User exists
//      * BDD Scenario:
//      * Given: Caitlin already has an account
//      * When: Caitlin tries to log in
//      * Then: Her account exists and she logs in.
//      */

//     @Test
//     void testCheckUserExists() {
//         // Mocking the Authentication class
//         Authentication authentication = Mockito.mock(Authentication.class);
//         Document testuser = authentication.mockCreateUser("caitlin", "testPassword", "Caitlin", "Smith", "123456789");
//         ArrayList<Document> list = new ArrayList();
//         list.add(testuser);
//         String testexistingUsername = "Caitlin";

//         // Setting up mock behavior
//         when(authentication.checkUserExists(testexistingUsername)).thenReturn(true);

//         // Act & Assert
//         assertTrue(authentication.checkUserExists(testexistingUsername),
//                 "checkUserExists should return true for the test user");
//         assertTrue(list.contains(testuser));
//     }

// }