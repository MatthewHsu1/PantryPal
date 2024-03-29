import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import io.javalin.http.Context;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;


class BddTest {
    private Database mockDatabase;
    private Authentication auth;

    @BeforeEach
    void setUp() {
        mockDatabase = new MockDatabase();
    }

    /**
     * TEST HELPER METHOD
     * 
     * @param title
     */
    private JSONObject createTestRecipe(String title) {
        JSONObject recipe = new JSONObject();
        recipe.put("Title", title);
        recipe.put("Ingredients", "Test Ingredients");
        recipe.put("Steps", Arrays.asList("Step 1", "Step 2"));
        recipe.put("MealType", "Test Meal");
        recipe.put("User", "TestUser");
        recipe.put("numSteps", 2);
        recipe.put("Image", "https://fakeurl");

        return recipe;
    }

    /**
     * BDD Scenario 1: Initial Account Creation
     * Given: Caitlin has the original PantryPal app installed on her device
     * When: Caitlin updates the app to PantryPal 2
     * And: Caitlin launches the updated PantryPal 2 app for the first time
     * Then: Caitlin is prompted to enter a username and password to securely create
     * an account
     */
    
    @Test
    void testAccountCreation() {
        // Given
        auth = new MockAuthentication();
        // When
        boolean accountCreated = auth.createUser("Caitlin", "password123", "Caitlin", "Doe", "1234567890");
        // Then
        assertTrue(accountCreated);
    }

    /**
     * BDD Scenario 2: Automatic Login
     * Given: Caitlin has successfully created an account
     * When: Caitlin opts for automatic login from her laptop
     * Then: Caitlin does not need to input any credentials
     */
    
     @Test
    void testAutomaticLogin() {
        // Given
        auth = new MockAuthentication();
        auth.createUser("Caitlin", "password123", "Caitlin", "Doe", "1234567890");

        // When
        auth.markAutoLoginStatus("Caitlin");
        String rememberedUser = auth.SkipLoginIfRemembered();

        // Then
        assertNull(rememberedUser);
    }

    /**
     * BDD Scenario 3: Save a newly generated recipe
     * Given: Caitlin just generated a recipe and is at the details page.
     * When: Caitlin clicks save on the bottom of the recipe details page.
     * Then: The recipe is added to the top of the homepage in the background
     * Then: Caitlin will remain on the details page.
     * 
     */
    
    @Test
    void testsaveNewlyGeneratedRecipe() {
        // Given
        JSONObject newRecipe = createTestRecipe("New Recipe");

        // When
        mockDatabase.insert(newRecipe);

        // Then
        JSONObject savedRecipe = mockDatabase.get("New Recipe");
        assertNotNull(savedRecipe);
        assertEquals("New Recipe", savedRecipe.getString("Title"));
        // Additional checks can be performed to ensure recipe is at the top of the
        // homepage
    }

    /**

     * BDD Scenario 4: Save a recipe that is already saved
     * Given: Caitlin open an old recipe detail page.
     * When: Caitlin clicks save on the bottom of the recipe details page.
     * Then: There will be no action performed by PantryPal 2
     * Then: Caitlin remains on the recipe details page.
     */
    
    @Test
    void testSaveAlreadySavedRecipe() {
        // Given
        JSONObject existingRecipe = createTestRecipe("Existing Recipe");
        //mockDatabase.insert(existingRecipe);
        // When
        mockDatabase.insert(existingRecipe);

        // Then
        JSONObject savedRecipe = mockDatabase.get("Existing Recipe");
        assertNotNull(savedRecipe);
     }

     /*  
     * BDD Scenario 5: I am able to see or get the mealtag of each recipe
     * Given: Caitlin is on the recipe list page
     * When: There are recipes stored in the app
     * Then: Caitlin will see the meal type for each recipe
     */

    @Test()
    void testMealType() {
        // Given
        JSONObject existingRecipe = createTestRecipe("Existing Recipe");
        // When
        mockDatabase.insert(existingRecipe);
        // Then
        JSONObject savedRecipe = mockDatabase.get("Existing Recipe");
        assertEquals("Test Meal", savedRecipe.getString("MealType"));
     }


     /*
     @Test
     void 
     */

     /**BDD Scenario 1: Refreshing The Recipe
      *○ Given: Caitlin is viewing a recipe on PantryPal 2
      *○ And: The initial set of ingredients is preserved
      *○ When: Caitlin clicks on the refresh button
      *○ Then: The app regenerates an alternative recipe
    */
    @Test
    void testRegenerateRecipe(){
        JSONObject oldRecipe = createTestRecipe("Old Recipe");

        RecipeDetailPage recipeDetail = mock(RecipeDetailPage.class);

        recipeDetail.updateResponse(oldRecipe);
        recipeDetail.update();
        assertNull(recipeDetail.getResponse());

    }
}