import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom"
import Card from "./Card";
import "../css/home.css"

function Home() {
    const [locations, setLocations] = useState({});
    const [type, setType] = useState([]);
    const [topRestaurants, setTopRestaurants] = useState([]);
    const [sameLocation, setSameLocation] = useState([]);
    const [selectedType, setSelectedType] = useState('');
    const [selectedLocation, setSelectedLocation] = useState('');
    const [selectedName, setSelectedName] = useState('');
    const UserInfo = useContext(UserContext);
    const navigate = useNavigate();

    function getTopRestaurants() {
        axios.get("http://localhost:8080/topRestaurants").then(
            (response) => {
                console.log("top :", response.data);
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
        axios.get("http://localhost:8080/types").then(
            (response) => {
                console.log("type :", response.data);
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
        axios.get("http://localhost:8080/location").then(
            (response) => {
                console.log("loc :", response.data);
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
        const params = { username: UserInfo.username }
        axios.get("http://localhost:8080/topRestaurants", null, { params: params }).then(
            (response) => {
                console.log("same loc :", response.data);
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
        const params = { name: selectedName || '', type: selectedType || '', city: selectedLocation || '' }
        axios.get("http://localhost:8080/restaurants",null, {params : params}).then(
            (response) => {
                if (response.status === 200) {
                    console.log("search : ", response.data);
                    navigate("/SearchResualt", { state: { data : response.data, name : params.name, type : params.type , city : params.city} });
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
                id={restaurant.id}
                rating={restaurant.overallAvg}
                start={restaurant.startTime}
                end={restaurant.enTime}
                img={restaurant.image}
                title={restaurant.name}
                reviews={restaurant.feedbacks.length}
                type={restaurant.type}
                city={restaurant.address.city}
            />
        );
    }

    return (
        <div>
            <div className="img-background ">
                <div className="main-part">
                    <img src="img/Logo.svg" alt="logo-img" className="logo-img" />
                    <form className="d-flex justify-content-between searchForm">
                        <select className="form-select" value={selectedLocation} onChange={handleLocationChange}>
                            <option value="" disabled>Location</option>
                            {locations && Object.entries(locations).map(([country, cities], index) => (
                                <optgroup key={index} label={country} >
                                    {cities.map((city, cityIndex) => (
                                        <option key={`${index}-${cityIndex}`} value={city}>{city}</option>
                                    ))}
                                </optgroup>
                            ))}
                        </select>
                        <select className="form-select" value={selectedType} onChange={handleTypeChange}>
                            <option value="" disabled>Restaurant</option>
                            {type && type.map((typ, index) => (
                                <option key={index} value={typ}>{typ}</option>
                            ))}
                        </select>
                        <input type="text" placeholder="Restaurant Name" value={selectedName} onChange={handleNameChange} className="form-control" />
                        <button type="submit" className="text-white searchBtn" onClick={handleSearch}>Search</button>
                    </form>

                </div>
            </div>
            <div className="container w-75 mt-5">
                <p className="title1">Top Restaurants in Mizdooni</p>
                <div className="row">
                    {topRestaurants && topRestaurants.map(createCard)}
                </div>
            </div>
            <div className="container w-75 mt-3">
                <p className="title1">You Might Also Like</p>
                <div className="row">
                    {sameLocation && sameLocation.map(createCard)}
                </div>
            </div>
            <div className="container w-75 d-flex justify-content-between my-5">
                <img src="img/Banner.svg" alt="table-img" className="table-img" />
                <div className="paragraph">
                    <p className="about-mizdooni">About Mizdooni</p>
                    <p className="about-paragraph">Are you tired of waiting in long lines at restaurants or struggling to find a table at your favorite eatery?</p>
                    <p className="about-paragraph"> Look no further than Mizdooni – the ultimate solution for all your dining reservation needs.</p>
                    <p className="about-paragraph">Mizdooni is a user-friendly website where you can reserve a table at any restaurant, from anywhere, at a specific time. Whether you're craving sushi, Italian, or a quick bite to eat, Mizdooni has you covered.</p>
                    <p className="about-paragraph">With a simple search feature, you can easily find a restaurant based on cuisine or location.</p>
                    <p className="about-paragraph"><span className="question-part">The best part?</span> There are no fees for making a reservation through Mizdooni. Say goodbye to the hassle of calling multiple restaurants or showing up only to find there's a long wait. With Mizdooni, you can relax knowing that your table is secured and waiting for you.</p>
                    <p className="about-paragraph">Don't let dining out be a stressful experience. Visit Mizdooni today and start enjoying your favorite meals without the headache of making reservations. Reserve your table with ease and dine with peace of mind.</p>
                </div>
            </div>
        </div>
    );
}

export default Home;