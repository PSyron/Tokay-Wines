
package pl.tokajiwines.jsonresponses;

public class SearchResultResponse {

    public int success;
    public String message;
    public WineListItem wine;
    public int wineCount;
    public ProducerListItem producer;
    public int producerCount;
    public HotelListItem hotel;
    public int hotelCount;
    public RestaurantListItem restaurant;
    public int restaurantCount;

    /*public SearchResultResponse(WineListItem[] wines, ProducerListItem[] producers,
            HotelListItem[] hotels, RestaurantListItem[] restaurants) {
        // if (wines != null && wines.length > 0) {
        wine = wines[0];
        wineCount = wines.length;
        // }
        //if (producers != null && producers.length > 0) {
        producer = producers[0];
        producerCount = producers.length;
        // }
        // if (hotels != null && hotels.length > 0) {
        hotel = hotels[0];
        hotelCount = hotels.length;
        // }
        // if (restaurants != null && restaurants.length > 0) {
        restaurant = restaurants[0];
        restaurantCount = restaurants.length;
        //}

    }*/
    public SearchResultResponse() {
    };
}
