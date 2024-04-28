import React, { useContext, useEffect , useState} from "react";
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom"
import Card from "./Card";
import "../assets/css/home.css"

function Home() {
    const [locations,setLocations] = useState('');
    const [type,setType] = useState('');
    const [topRestaurants , setTopRestaurants] = useState('');
    const [sameLocation, setSameLocation] = useState('');
    const [selectedType, setSelectedType] = useState('');
    const [selectedLocation , setSelectedLocation] = useState('');
    const [selectedName , setSelectedName] = useState('');
    const UserInfo = useContext(UserContext);
    const navigate = useNavigate();

    function getTopRestaurants(){
        axios.get("//localhost:8080/topRestaurants").then(
            (response) =>{
                setTopRestaurants(response.data);
            },
            (error)=>{
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

    function getTypes(){
        axios.get("//localhost:8080/types").then(
            (response)=>{
                setType(response.data);
            },
            (error)=>{
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

    function getLocations(){
        axios.get("//localhost:8080/locations").then(
            (response)=>{
                setLocations(response.data);
            },
            (error)=>{
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

    function getSameLocation(){
        axios.get("//localhost:8080/topRestaurants/"+ String(UserInfo.username)).then(
            (response)=>{
                setSameLocation(response.data);
            },
            (error)=>{
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

    function handleSearch(event){
        event.preventDefault();
        const params = {name : selectedName,type  : selectedType , location : selectedLocation }
        axios.post("//localhost:8080/search",params).then(
            (response) => {
                if(response.status === 200) {
                    navigate("/searchResualt" , {state : response.data});
                }
            },
            (error)=>{
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
    },[]);

    function createCard(restaurant, index){
        return(
            <Card
                key ={index}
                rating = {restaurant.rating}
                start ={restaurant.startTime}
                end = {restaurant.enTime}
                img = {restaurant.img}
                title = {restaurant.title}
                reviews = {restaurant.reviews}
                type = {restaurant.type}
                city = {restaurant.adress.city}
            />
        );
    }

    return (
        <div>
            <div class="img-background ">
                <div class="main-part">
                    <img src="img/Logo.svg" alt="logo-img" class="logo-img"/>
                    <form class="d-flex justify-content-between searchForm">
                        <select class="form-select" value={selectedLocation} onChange={handleLocationChange}>
                            <option disabled>Location</option>
                            {locations.map((loc, index) => (
                                <option key={index} value={loc}>{loc}</option>
                            ))}
                        </select>
                        <select class="form-select" value={selectedType} onChange={handleTypeChange}>
                            <option disabled>Type</option>
                            {type.map((typ, index) => (
                                <option key={index} value={typ}>{typ}</option>
                            ))}
                        </select>
                        <input type="text" placeholder="Restaurant Name" value={selectedName} onChange={handleNameChange}  class="form-control"/>
                        <button type="submit" class="text-white searchBtn" onClick={handleSearch}>Search</button>
                    </form>
                </div>
            </div>
            <div class="container topRestaurant mt-5">
                <p class="title1">Top Restaurants in Mizdooni</p>
                <div class="row">
                    {topRestaurants.map(createCard)}
                </div>
            </div>
            <div class="container mt-3">
                <p class="title1">You Might Also Like</p>
                <div class="row">
                    {sameLocation.map(createCard)}
                </div>
            </div>
            <div class="container d-flex justify-content-between my-5">
                <img src="img/Banner.svg" alt="table-img" class="table-img"/>
                <div class="paragraph">
                    <p class="about-mizdooni">About Mizdooni</p>
                    <p class="about-paragraph">Are you tired of waiting in long lines at restaurants or struggling to find a table at your favorite eatery?</p>
                    <p class="about-paragraph"> Look no further than Mizdooni – the ultimate solution for all your dining reservation needs.</p>
                    <p class="about-paragraph">Mizdooni is a user-friendly website where you can reserve a table at any restaurant, from anywhere, at a specific time. Whether you're craving sushi, Italian, or a quick bite to eat, Mizdooni has you covered.</p>
                    <p class="about-paragraph">With a simple search feature, you can easily find a restaurant based on cuisine or location.</p>
                    <p class="about-paragraph"><span class="question-part">The best part?</span> There are no fees for making a reservation through Mizdooni. Say goodbye to the hassle of calling multiple restaurants or showing up only to find there's a long wait. With Mizdooni, you can relax knowing that your table is secured and waiting for you.</p>
                    <p class="about-paragraph">Don't let dining out be a stressful experience. Visit Mizdooni today and start enjoying your favorite meals without the headache of making reservations. Reserve your table with ease and dine with peace of mind.</p>
                </div>
            </div>
        </div>
    );
}

export default Home;