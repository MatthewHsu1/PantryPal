import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.eclipse.jetty.server.Authentication;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* 
 Upon launching PantryPal 2, Caitlin is prompted to create an account for a more personalized 
 experience. Entering a username and password, she clicks "Create Account," ensuring her login 
credentials are secure. Impressed by the app's consideration, she opts for automatic login on her 
laptop for added convenience. With the account set up, Caitlin explores the new features. 
Caitlin's eyes light up as she notices a cool change while creating a new recipe - each 
recipe comes with an image of the potential dish, offering a visual preview of the delicious 
dish awaiting her. While impressed by this innovative addition, Caitlin encounters a moment 
where a suggested recipe doesn't quite fit her taste. However, a newfound solution reveals 
itself – a simple click on the refresh button regenerates the recipe, proposing an alternative 
while preserving the initial set of ingredients. After reviewing the revised recipe, she decides 
to save it to her collection. With these new features, Caitlin feels a fresh wave of excitement 
for her time in the kitchen. PantryPal 2, now with visual goodies and handy suggestions, fits 
perfectly into Caitlin's cooking routine. It's not just about recipes; it's about making her 
semester delicious. Caitlin's got a kitchen sidekick in PantryPal 2, ready for all the tasty twists.
*/


class E2ETest {

    @Test
    void testEnd2EndScenario() throws IOException, InterruptedException, URISyntaxException {
        MockAuthentication auth = new MockAuthentication();
        Model model = mock(Model.class);

        auth.createUser("test", "test", "test", "test", "test");
        Boolean verifyUser = auth.verifyUser("test", "test");
        assertTrue(verifyUser);

        auth.markAutoLoginStatus("test");
        String autoLogin = auth.SkipLoginIfRemembered();
        assertEquals(autoLogin, null);

        MockChatGPT chatGPT = new MockChatGPT();

        JSONObject recipe = chatGPT.generateRecipe("Ingredients");
        assertNotNull(recipe);

        RecipeDetailPage recipeDetailPage = mock(RecipeDetailPage.class);
        recipeDetailPage.updateResponse(recipe);
        recipeDetailPage.update();

        assertNull(recipeDetailPage.getResponse());
        //String image = chatGPT.generateImage("Image");

        RecipeCard recipeCard = mock(RecipeCard.class);
        //recipeCard.setImage(image);
        
        recipeCard.addRecipeDetail(recipeDetailPage);
        assertNull(recipeCard.getRecipeDetailPage());
        
        JSONObject recipe2 = chatGPT.generateRecipe("Recipe2");
        
        recipeDetailPage.updateResponse(recipe2);
        recipeDetailPage.update();
        recipeCard.addRecipeDetail(recipeDetailPage);
        assertNull(recipeDetailPage.getResponse());
        assertNull(recipeCard.getRecipeDetailPage());

        MockDatabase database = new MockDatabase();
        database.insert(recipe2);

        JSONObject retrievedRecipe = database.get("test");
        assertNull(retrievedRecipe);      
    }
}