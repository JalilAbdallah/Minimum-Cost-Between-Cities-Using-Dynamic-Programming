package ProjectOne;

/**
 * The {@code City} class represents a city with attributes relevant for travel and logistics planning.
 * It holds details about the name, stage in the journey, hotel costs, and petrol costs to other cities.
 */
public class City {
    private String name; // Name of the city
    private int stage; // Stage of the journey this city represents
    private int hotelCost; // Cost of staying in a hotel in this city
    private int[] petrolCost; // Array of petrol costs from this city to other cities

    /**
     * Constructs a new City instance.
     *
     * @param name       the name of the city
     * @param stage      the stage of the journey this city is on
     * @param hotelCost  the cost of a hotel stay in this city
     * @param petrolCost an array of costs for petrol to travel from this city to others
     */
    public City(String name, int stage, int hotelCost, int[] petrolCost) {
        this.name = name;
        this.stage = stage;
        this.hotelCost = hotelCost;
        this.petrolCost = petrolCost;
    }

    /**
     * Retrieves the name of the city.
     *
     * @return the name of the city
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the city.
     *
     * @param name the new name of the city
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the stage of the city in the journey.
     *
     * @return the stage number
     */
    public int getStage() {
        return stage;
    }

    /**
     * Sets the stage of the city in the journey.
     *
     * @param stage the new stage number
     */
    public void setStage(int stage) {
        this.stage = stage;
    }

    /**
     * Retrieves the cost of a hotel stay in the city.
     *
     * @return the hotel cost
     */
    public int getHotelCost() {
        return hotelCost;
    }

    /**
     * Sets the cost of a hotel stay in the city.
     *
     * @param hotelCost the new hotel cost
     */
    public void setHotelCost(int hotelCost) {
        this.hotelCost = hotelCost;
    }

    /**
     * Retrieves the array of petrol costs from this city to other cities.
     *
     * @return the array of petrol costs
     */
    public int[] getPetrolCost() {
        return petrolCost;
    }

    /**
     * Sets the array of petrol costs from this city to other cities.
     *
     * @param petrolCost the new array of petrol costs
     */
    public void setPetrolCost(int[] petrolCost) {
        this.petrolCost = petrolCost;
    }

}
