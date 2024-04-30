import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom"
import Card from "./Card";
import "../assets/css/home.css"

function Home() {
    const [locations, setLocations] = useState('');
    const [type, setType] = useState('');
    const [topRestaurants, setTopRestaurants] = useState('');
    const [sameLocation, setSameLocation] = useState('');
    const [selectedType, setSelectedType] = useState('');
    const [selectedLocation, setSelectedLocation] = useState('');
    const [selectedName, setSelectedName] = useState('');
    const UserInfo = useContext(UserContext);
    const navigate = useNavigate();

    function getTopRestaurants() {
        axios.get("//localhost:8080/topRestaurants").then(
            (response) => {
                setTopRestaurants(response.data);
            },
            (error) => {
                Swal.fire({
                    icon: error,
                    title: error.response.data.message.split(":")[1],
                    text: "Please try again.",
                })
            }
        )
            .catch((error) => {
                console.log(error);
            });
    }

    function getTypes() {
        axios.get("//localhost:8080/types").then(
            (response) => {
                setType(response.data);
            },
            (error) => {
                Swal.fire({
                    icon: error,
                    title: error.response.data.message.split(":")[1],
                    text: "Please try again.",
                })
            }
        )
            .catch((error) => {
                console.log(error);
            });
    }

    function getLocations() {
        axios.get("//localhost:8080/locations").then(
            (response) => {
                setLocations(response.data);
            },
            (error) => {
                Swal.fire({
                    icon: error,
                    title: error.response.data.message.split(":")[1],
                    text: "Please try again.",
                })
            }
        )
            .catch((error) => {
                console.log(error);
            });
    }

    function getSameLocation() {
        axios.get("//localhost:8080/topRestaurants/" + String(UserInfo.username)).then(
            (response) => {
                setSameLocation(response.data);
            },
            (error) => {
                Swal.fire({
                    icon: error,
                    title: error.response.data.message.split(":")[1],
                    text: "Please try again.",
                })
            }
        )
            .catch((error) => {
                console.log(error);
            });
    }

    function handleSearch(event) {
        event.preventDefault();
        const params = { name: selectedName, type: selectedType, location: selectedLocation }
        axios.post("//localhost:8080/search", params).then(
            (response) => {
                if (response.status === 200) {
                    navigate("/searchResualt", { state: response.data, params: params });
                }
            },
            (error) => {
                Swal.fire({
                    icon: error,
                    title: error.response.data.message.split(":")[1],
                    text: "Please try again.",
                })
            }
        )
    }

    const handleTypeChange = (e) => {
        setSelectedType(e.target.value);
    }

    const handleLocationChange = (e) => {
        setSelectedLocation(e.target.value);
    }
    const handleNameChange = (e) => {
        setSelectedName(e.target.value);
    }


    useEffect(() => {
        setTopRestaurants(null);
        setSameLocation(null);
        setType(null);
        setLocations(null);
        getLocations();
        getSameLocation();
        getTopRestaurants();
        getTypes();
    }, []);

    function createCard(restaurant, index) {
        return (
            <Card
                key={index}
                id = {restaurant.id}
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
            <div calssName="img-background ">
                <div calssName="main-part">
                    <img src="img/Logo.svg" alt="logo-img" calssName="logo-img" />
                    <form calssName="d-flex justify-content-between searchForm">
                        <select className="form-select" value={selectedLocation} onChange={handleLocationChange}>
                            <option disabled>Location</option>
                            {Object.entries(locations).map(([country, cities], index) => (
                                <optgroup key={index} label={country} disabled>
                                    {cities.map((city, cityIndex) => (
                                        <option key={`${index}-${cityIndex}`} value={city}>{city}</option>
                                    ))}
                                </optgroup>
                            ))}
                        </select>
                        <select calssName="form-select" value={selectedType} onChange={handleTypeChange}>
                            <option disabled>Restaurant</option>
                            {type.map((typ, index) => (
                                <option key={index} value={typ}>{typ}</option>
                            ))}
                        </select>
                        <input type="text" placeholder="Restaurant Name" value={selectedName} onChange={handleNameChange} calssName="form-control" />
                        <button type="submit" calssName="text-white searchBtn" onClick={handleSearch}>Search</button>
                    </form>
                </div>
            </div>
            <div calssName="container topRestaurant mt-5">
                <p calssName="title1">Top Restaurants in Mizdooni</p>
                <div calssName="row">
                    {topRestaurants.map(createCard)}
                </div>
            </div>
            <div calssName="container mt-3">
                <p calssName="title1">You Might Also Like</p>
                <div calssName="row">
                    {sameLocation.map(createCard)}
                </div>
            </div>
            <div calssName="container d-flex justify-content-between my-5">
                <img src="img/Banner.svg" alt="table-img" calssName="table-img" />
                <div calssName="paragraph">
                    <p calssName="about-mizdooni">About Mizdooni</p>
                    <p calssName="about-paragraph">Are you tired of waiting in long lines at restaurants or struggling to find a table at your favorite eatery?</p>
                    <p calssName="about-paragraph"> Look no further than Mizdooni – the ultimate solution for all your dining reservation needs.</p>
                    <p calssName="about-paragraph">Mizdooni is a user-friendly website where you can reserve a table at any restaurant, from anywhere, at a specific time. Whether you're craving sushi, Italian, or a quick bite to eat, Mizdooni has you covered.</p>
                    <p calssName="about-paragraph">With a simple search feature, you can easily find a restaurant based on cuisine or location.</p>
                    <p calssName="about-paragraph"><span calssName="question-part">The best part?</span> There are no fees for making a reservation through Mizdooni. Say goodbye to the hassle of calling multiple restaurants or showing up only to find there's a long wait. With Mizdooni, you can relax knowing that your table is secured and waiting for you.</p>
                    <p calssName="about-paragraph">Don't let dining out be a stressful experience. Visit Mizdooni today and start enjoying your favorite meals without the headache of making reservations. Reserve your table with ease and dine with peace of mind.</p>
                </div>
            </div>
        </div>
    );
}

export default Home;