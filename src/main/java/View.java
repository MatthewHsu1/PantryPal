
// Utils 
import java.util.List;
import java.util.stream.Collectors;
// Event Handling
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
// Padding and Alignment
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
// imports to make the HBOX grow 
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage; // Might need to open new stage (new window) 
import javafx.scene.text.Font;


public class View {
    private AppFrame appframe; // This is a scene
    private CreateFrame createframe;
    private VoiceInputFrame voiceInputFrame;
    private Stage mainStage;
    private Scene currentScene;

    public View(Stage primaryStage) {
        this.appframe = new AppFrame();
        this.createframe = new CreateFrame();
        this.voiceInputFrame = new VoiceInputFrame();

        this.mainStage = primaryStage;
        this.currentScene = new Scene(this.appframe, 1280, 720);

        mainStage.setScene(currentScene);
        mainStage.setTitle("PantryPal");
        mainStage.setResizable(true);
        mainStage.show();
    }

    public AppFrame getAppFrame() {
        return this.appframe;
    }

    public void returnToAppFrame() {
        this.appframe = new AppFrame(); // Create a new instance of AppFrame
        this.currentScene.setRoot(this.appframe); // Set the new instance as the root
        mainStage.show();
    }

    public CreateFrame getCreateFrame() {
        return this.createframe;
    }

    public VoiceInputFrame getVoiceInputFrame() {
        return this.voiceInputFrame;
    }

    public void switchScene(Parent node) {
        // Check if the node is already part of a scene
        if (node.getScene() != null) {
            // If it is, use the existing scene
            this.currentScene = node.getScene();
        } else {
            // Otherwise, create a new scene
            this.currentScene = new Scene(node, 1280, 720);
        }
        mainStage.setScene(currentScene);
        mainStage.show();
    }
}

class Header extends HBox {
    Button homeButton;
    Button profileButton;
    Button savedRecipesButton;

    Header() {
        // Style the header background
        this.setPrefSize(1280, 85); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;"); // Color of the Header
        this.setPadding(new Insets(20, 35, 10, 35));

        // Button Styles
        this.homeButton = new Button("Pantry Pal");
        homeButton.setStyle(
                "-fx-padding: 10 20 10 20; -fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 30px; -fx-font-family: 'Verdana'; -fx-text-fill: #5DA9E9 !important;");

        this.profileButton = new Button("My Profile");
        profileButton.setStyle(
                "-fx-padding: 10 20 10 20; -fx-font-family: 'Verdana'; -fx-background-color: transparent; -fx-border-color: transparent; fx-text-fill: 616161; -fx-translate-y: 8;");

        this.savedRecipesButton = new Button("Saved");
        savedRecipesButton.setStyle(
                "-fx-padding: 10 20 10 20; -fx-font-family: 'Verdana';  -fx-background-color: transparent; -fx-border-color: transparent; fx-text-fill: 616161; -fx-translate-y: 8;");
        // A Region is used as a "spacer"
        // occupies all available space between the buttons
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // add all childeren
        this.getChildren().addAll(homeButton, spacer, savedRecipesButton, profileButton);

    }
}

class Footer extends HBox {
    private Button createButton;

    Footer() {
        this.setPrefSize(1280, 90);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setSpacing(15);
        

        createButton = new Button("Create a New Recipe");
        createButton.setStyle("-fx-background-color: #ADD8E6;  -fx-font-family: 'Verdana';  -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 0); -fx-padding: 7px; -fx-border-color: #D5D5D5; -fx-border-width: 0.5px; -fx-border-radius: 7.5px; -fx-background-radius: 7.5px;");

        createButton.setOnMousePressed(e -> {
            createButton.setScaleX(0.95);
            createButton.setScaleY(0.95);
        });
        createButton.setOnMouseReleased(e -> {
            createButton.setScaleX(1.0);
            createButton.setScaleY(1.0);
        });
        this.getChildren().addAll(createButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getCreateButton() {
        return createButton;
    }

    public void setCreateButtonAction(EventHandler<ActionEvent> eventHandler) {
        createButton.setOnAction(eventHandler);
    }
}

class DetailFooter extends HBox {
    private Button saveButton;
    private Button backButton;

    DetailFooter() {
        this.setPrefSize(1280, 90);
        this.setStyle("-fx-background-color: #FFFFFF; ");
        this.setSpacing(15);
        this.setPadding(new Insets(20, 35, 10, 35));

        backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ADD8E6;  -fx-font-family: 'Verdana';  -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 0); -fx-padding: 7px; -fx-border-color: #D5D5D5; -fx-border-width: 0.5px; -fx-border-radius: 7.5px; -fx-background-radius: 7.5px;");
        backButton.setOnMousePressed(e -> {
            backButton.setScaleX(0.95);
            backButton.setScaleY(0.95);
        });
        backButton.setOnMouseReleased(e -> {
            backButton.setScaleX(1.0);
            backButton.setScaleY(1.0);
        });
        saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #ADD8E6;  -fx-font-family: 'Verdana';  -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 0); -fx-padding: 7px; -fx-border-color: #D5D5D5; -fx-border-width: 0.5px; -fx-border-radius: 7.5px; -fx-background-radius: 7.5px;");
        saveButton.setOnMousePressed(e -> {
            saveButton.setScaleX(0.95);
            saveButton.setScaleY(0.95);
        });
        saveButton.setOnMouseReleased(e -> {
            saveButton.setScaleX(1.0);
            saveButton.setScaleY(1.0);
        });
        System.out.println("Hello, this is detail footer");
        this.getChildren().addAll(backButton, saveButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setBackButtonAction(EventHandler<ActionEvent> eventHandler) {
        backButton.setOnAction(eventHandler);
    }

}

/**
 * Class: Recipe List
 * Extends GridPane to display recipes in a grid layout
 * Each recipe is added to the grid, with a maximum of 4 recipes per row
 */
class RecipeList extends GridPane {
    private final int maxColumn = 4; // we will have 4 recipes per column

    public RecipeList() {
        this.setHgap(15); // horizontal gap in the grid
        this.setVgap(15); // vertical gap in the grid
        this.setStyle("-fx-background-color:#E5F4E3");
        this.setPadding(new Insets(25));
        addRecipeCard(new RecipeCard("Recipe 1", "Description of Recipe 1"));
        addRecipeCard(new RecipeCard("Recipe 2", "Description of Recipe 2"));
        addRecipeCard(new RecipeCard("Recipe 3", "Description of Recipe 3"));
        addRecipeCard(new RecipeCard("Recipe 4", "Description of Recipe 4"));
        addRecipeCard(new RecipeCard("Recipe 5", "Description of Recipe 5"));
    }

    public void addRecipeCard(RecipeCard card) {
        int index = getChildren().size();
        int row = index / maxColumn;
        int column = index % maxColumn;

        this.add(card, column, row);
    }

    public List<RecipeCard> getRecipeCards() {
        // Assuming all children of RecipeList are RecipeCards
        return getChildren().stream()
                .filter(node -> node instanceof RecipeCard)
                .map(node -> (RecipeCard) node)
                .collect(Collectors.toList());
    }

}

class RecipeCard extends VBox {
    private String recipeTitle;
    private String recipeDescription;
    private Button detailsButton;

    public RecipeCard(String title, String description) {
        this.recipeTitle = title;
        this.recipeDescription = description;
        // make labels for title and description
        Label titleLabel = new Label(recipeTitle);
        Label descriptionLabel = new Label(recipeDescription);

        // card styles
        this.setPrefSize(300, 200);
        this.setPadding(new Insets(15));
        this.setSpacing(10);
        this.setStyle(
                "-fx-border-color: transparent;  -fx-background-color: #FFFFFF;  -fx-font-family: 'Verdana';  -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

        // details button
        detailsButton = new Button("Details");

        detailsButton.setStyle(
                "-fx-background-color: #ADD8E6;  -fx-font-family: 'Verdana';  -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 0); -fx-padding: 7px; -fx-border-color: #D5D5D5; -fx-border-width: 0.5px; -fx-border-radius: 7.5px; -fx-background-radius: 7.5px;");

        detailsButton.setOnMousePressed(e -> {
            detailsButton.setScaleX(0.95);
            detailsButton.setScaleY(0.95);
        });
        detailsButton.setOnMouseReleased(e -> {
            detailsButton.setScaleX(1.0);
            detailsButton.setScaleY(1.0);
        });
        detailsButton.setAlignment(Pos.BOTTOM_CENTER);
        this.getChildren().addAll(titleLabel, descriptionLabel, detailsButton);
    }

    public void setDetailsButtonAction(EventHandler<ActionEvent> eventHandler) {
        detailsButton.setOnAction(eventHandler);
    }

}

/**
 * here, we need to fetch the recipe details from MongoDB
 */
class RecipeDetailPage extends BorderPane {
    private Label label;
    private Header header; // header
    private DetailFooter detailFooter; // footer

    RecipeDetailPage() {
        this.setStyle("-fx-background-color: #E5F4E3;");

        // Initialize the header and footer
        header = new Header();
        detailFooter = new DetailFooter();

        // Label for content in the center
        label = new Label("HIIIII");

        // Set the header, label, and footer to their positions
        this.setTop(header);
        this.setCenter(label);
        this.setBottom(detailFooter);
    }

    public DetailFooter getDetailFooter() {
        return this.detailFooter;
    }
}

class AppFrame extends BorderPane {
    private Header header;
    private RecipeList recipeList;
    private Footer footer;
    private DetailFooter detailFooter;

    AppFrame() {
        header = new Header(); // Initialise the header Object
        recipeList = new RecipeList(); // Create a recipeList Object to hold the recipes
        footer = new Footer(); // Initialise the Footer Object
        detailFooter = new DetailFooter();

        // Add a Scroller to the Recipe List
        ScrollPane scroller = new ScrollPane(recipeList); // Wrap the task list in a ScrollPane
        scroller.setFitToWidth(true); // Set the width to match the width of the recipeList
        scroller.setFitToHeight(true); // Set the height to match the height of the recipeList

        this.setTop(header); // Add header to the top of the BorderPane
        this.setCenter(scroller); // Add scroller to the centre of the BorderPane
        this.setBottom(footer); // Add footer to the bottom of the BorderPane
    }

    public Header getHeader() {
        return this.header;
    }

    public RecipeList getRecipeList() {
        return this.recipeList;
    }

    public Footer getFooter() {
        return this.footer;
    }

    public DetailFooter getDetailFooter() {
        return this.detailFooter;
    }

    public AppFrame showList() {
        ScrollPane scroller = new ScrollPane(recipeList);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        this.setCenter(scroller);
        this.setBottom(footer);
        return this;
    }

}

class CreateFrame extends BorderPane {
    Button breakfastButton;
    Button lunchButton;
    Button dinnerButton;
    private Region clickableRegion;
    private Button next;
    private String mealType;
    private Button record;

    CreateFrame() {
        this.setPadding(new javafx.geometry.Insets(100));
        //Meal type labels
        Label breakfastButton = new Label("Breakfast");
        Label lunchButton = new Label("Lunch");
        Label dinnerButton = new Label("Dinner");
        breakfastButton.setFont(new Font(20)); 
        lunchButton.setFont(new Font(20)); 
        dinnerButton.setFont(new Font(20));

        HBox hbox = new HBox();
        hbox.setSpacing(100);
        hbox.getChildren().addAll(breakfastButton, lunchButton, dinnerButton);
        hbox.setAlignment(Pos.CENTER);

        //Center prompt
        Label Title = new Label("Please verbally choose a type of meal.");
        Title.setFont(new Font(30));
        this.setTop(Title);
        setAlignment(Title, Pos.CENTER);
        this.setCenter(hbox);

        record = new Button("Record");

         //Next button to get to next scnene.
        next = new Button("Next");
        next.setVisible(false);
        next.setDisable(true);

        //Set up vbox for the bottom part of boaderpane
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);
        vbox.getChildren().addAll(record, next);

        //Insert vox into bottom of borderpane.
        this.setBottom(vbox);
    }

    public Button getRecordButton(){
        return record;
    }

    public void recordPressed(EventHandler<MouseEvent> eventHandler) {
            record.setOnMousePressed(eventHandler);
    }

    public void recordUnpressed(EventHandler<MouseEvent> eventHandler) {
            record.setOnMouseReleased(eventHandler);
    }

    public void nextButton(EventHandler<ActionEvent> eventHandler){
            next.setOnAction(eventHandler);
    }

    public void setMealType(String s){
        this.mealType = s;
    }

    //Will let the next button be visible if a meal type has not been selected.
    public void updateNextButton(){
        if(mealType == null){
            next.setVisible(false);
            next.setDisable(true);
        }
        else{
            next.setVisible(true);
            next.setDisable(false);
        }
    }

     //Returns the ,eal type the user picks.
    public String getMealType(){
        mealType = mealType.toLowerCase();
        if(mealType.contains("breakfast")){
            mealType = "breakfast";
        }
        else if(mealType.contains("lunch")){
            mealType = "lunch";
        }
        else{
            mealType = "dinner";
        }
        return mealType;
    }
}

class VoiceInputFrame extends BorderPane {

    private Label title;
    private Label prompt;
    private ImageView imageView;
    private Button record;


    public VoiceInputFrame(){

        this.setPadding(new javafx.geometry.Insets(100));

        //Setting up the prompt to ask the user to ask for ingredients.
        prompt = new Label("Please list the ingredients you wish to cook with.");
        prompt.setFont(new Font(30));
        this.setCenter(prompt);
        setAlignment(prompt, Pos.CENTER);


        //Micorphone Image
        record = new Button("Record");

        this.setBottom(record);
        setAlignment(record, Pos.CENTER);
    }

    
    public Button getRecordButton(){
        return record;
    }
    
    public void recordPressed(EventHandler<MouseEvent> eventHandler) {
        record.setOnMousePressed(eventHandler);
    }

    public void recordUnpressed(EventHandler<MouseEvent> eventHandler) {
        record.setOnMouseReleased(eventHandler);
    }

    public void updatePrompt(String s){
        this.prompt.setText("Prompt recieved: " + s);
    }

    //This will set the title to indicate you have selcted breakfast, lunch or dinner.
    public void setTitle(String s){
        title = new Label("You have select " + s);
        title.setFont(new Font(30));
        this.setTop(title);
        setAlignment(title, Pos.CENTER);
    }


}