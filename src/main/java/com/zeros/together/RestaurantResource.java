package com.zeros.together;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RestaurantResource {

    @GetMapping("/api/restaurants")
    public List<Restaurant> getRestaurants(@RequestParam("lat") double lat, @RequestParam("lng") double lng) throws InterruptedException, ApiException, IOException {

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBvLjHOMjmRVWELPcxI-YJ43rGJk2-cw2w")
                .build();
        LatLng searchLocation = new LatLng(lat, lng);
        PlacesSearchResponse searchResponse = PlacesApi.nearbySearchQuery(context, searchLocation)
                .radius(100)
                .type(PlaceType.RESTAURANT)
                .await();

        return Stream.of(searchResponse.results).map(result -> {
            Location location = new Location(result.geometry.location.lat, result.geometry.location.lng);
            return new Restaurant(result.name, location);
        }).collect(Collectors.toList());
    }

    class Restaurant {
        private String name;
        private Location location;

        public Restaurant(String name, Location location) {
            this.name = name;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public Location getLocation() {
            return location;
        }
    }

    class Location {
        double lat;
        double lng;

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }

}
