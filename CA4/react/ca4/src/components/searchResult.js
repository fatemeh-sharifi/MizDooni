import React from "react";
import { useLocation } from "react-router-dom";
import Card from "./Card";

function SearchResult() {
    const locationUse = useLocation();
    const { state: searchData, params: { name: paramName, type: paramType, location: paramLocation } } = locationUse;

    function createCard(restaurant, index) {
        return (
            <Card
                key={index}
                id= {restaurant.id}
                rating={restaurant.rating}
                start={restaurant.startTime}
                end={restaurant.enTime}
                img={restaurant.img}
                title={restaurant.title}
                reviews={restaurant.reviews}
                type={restaurant.type}
                city={restaurant.adress.city}
            />
        );
    }
    return (
        <div>
            <div class="container topRestaurant">
                <p class="title1">Results for {paramName? paramName : (paramType ? paramType : (paramLocation ? paramLocation : ''))}</p>
                <div class="row justify-content-between">
                    {searchData.map(createCard)}
                </div>
            </div>
        </div>
    );
}

export default SearchResult