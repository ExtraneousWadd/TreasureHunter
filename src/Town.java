/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if(hunter.hasTreasure(terrain.getTreasure())){
            System.out.println("You have already acquired this town's treasure.");
            terrain.setSearched();
        }

        if (toughTown) {
            printMessage += Colors.CYAN + "\nIt's pretty rough around here, so watch yourself." + Colors.RESET;
        } else {
            printMessage += Colors.CYAN + "\nWe're just a sleepy little town with mild mannered folk." + Colors.RESET;
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item;
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance) {
                printMessage += Colors.RED + "Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                printMessage += Colors.RED + "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold." + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                printMessage += Colors.RED + "\nYou lost the brawl and pay " + goldDiff + " gold." + Colors.RESET;
                hunter.changeGold(-goldDiff);
            }
        }
    }

    public void treasureHunt() {
        if (terrain.getTreasure().equals("dust")) {
            System.out.println("All you found was some dust.");
        } else {
            if(!terrain.isSearched()) {
                System.out.println("You found a " + terrain.getTreasure());
                hunter.addTreasure(terrain.getTreasure());
            } else {
                System.out.println("You already have this town's treasure!");
            }
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        int rnd = (int)(Math.random() * 6) + 1;
        if (rnd == 1) {
            return new Terrain("Mountains", "Rope",treasureRandom());
        } else if (rnd == 2) {
            return new Terrain("Ocean", "Boat",treasureRandom());
        } else if (rnd == 3) {
            return new Terrain("Plains", "Horse",treasureRandom());
        } else if (rnd == 4) {
            return new Terrain("Desert", "Water",treasureRandom());
        } else if (rnd == 5) {
            return new Terrain("Jungle", "Machete",treasureRandom());
        } else {
            return new Terrain("Marsh", "Boots",treasureRandom());
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private String treasureRandom(){
        String treasure = "";
        int rnd = (int)(Math.random() * 4) + 1;
        if(rnd == 1){
            treasure = "crown";
        } else if (rnd == 2){
            treasure = "dust";
        } else if (rnd == 3){
            treasure = "gem";
        } else {
            treasure = "trophy";
        }
        return treasure;
    }

    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}
