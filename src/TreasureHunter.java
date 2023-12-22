import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private static boolean easyMode;
    private boolean normalMode;
    private static boolean samuraiMode;

    public boolean testMode;

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode = false;
        normalMode = false;
        testMode = false;
        samuraiMode = false;
    }
    public static boolean getEasyMode(){
        return easyMode;
    }
    public static boolean getSamuraiMode(){
        return samuraiMode;
    }



    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = SCANNER.nextLine().toLowerCase();

        System.out.print("Hard mode/Normal mode/Easy Mode/Test mode/Samurai Mode? (h/n/e/t/s): ");
        String option = SCANNER.nextLine().toLowerCase();
        if (option.equals("h")) {
            hardMode = true;
        }
        if (option.equals("e")) {
            easyMode = true;
        }
        if (option.equals("n")) {
            normalMode = true;
        }
        if (option.equals("t")){
            testMode = true;
        }
        if (option.equals("s")){
            samuraiMode = true;
        }

        // set hunter instance variable
        if(testMode) {
            hunter = new Hunter(name, 100);
            hunter.addItemTest("horse");
            hunter.addItemTest("boat");
            hunter.addItemTest("machete");
            hunter.addItemTest("water");
            hunter.addItemTest("rope");
            hunter.addItemTest("shovel");
        } else {
            if(easyMode) {
                hunter = new Hunter(name, 20);
            }
            else {
                hunter = new Hunter(name, 10);
            }
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            markdown = 0.25;
            toughness = 0.75;
        }
        if (normalMode) {
            markdown = 0.5;
            toughness = 0.4;
        }
        if (samuraiMode) {
            markdown = 1;
            toughness = 0.1;
        }

        if (easyMode) {
            markdown = 1;
            toughness = 0.1;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";

        while (!choice.equals("x")) {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            System.out.println("***");
            System.out.println(hunter);
            System.out.println(currentTown);
            System.out.println("(B)uy something at the shop.");
            System.out.println("(S)ell something at the shop.");
            System.out.println("(M)ove on to a different town.");
            System.out.println("(L)ook for trouble!");
            System.out.println("(H)unt for treasure!");
            System.out.println("(D)ig for gold!");
            System.out.println("Give up the hunt and e(X)it.");
            System.out.println();
            if(hunter.gameOver){
                choice = "x";
                if(hunter.getTreasureCount() == 3){
                    System.out.println("You collected every treasure, so you win!");
                } else {
                    System.out.println("You couldn't pay the gold, so you lose!");
                }
            } else {
            System.out.print("What's your next move? ");
                choice = SCANNER.nextLine().toLowerCase();
                processChoice(choice);
            }
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("h")){
            currentTown.treasureHunt();
        } else if (choice.equals("x")) {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        }else if(choice.equals("d")){
            currentTown.diggingForGold();
        }else {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }

    public boolean getTestMode(){
        return testMode;
    }

}
