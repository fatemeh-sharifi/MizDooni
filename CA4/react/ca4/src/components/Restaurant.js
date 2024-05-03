import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { Link, useParams } from "react-router-dom";
import "../css/restaurant.css"

import RestaurantProfile from "./RestaurantProfile";

function Restaurant() {
    const { id } = useParams();
    const [restaurant , setRestaurant] = useState('');
    const [availableTimes , setAvailableTimes] = useState('');
    const [selectedPeople, setSelectedPeople] = useState('1');
    const [selectedDate, setSelectedDate] = useState('');
    const UserInfo = useContext(UserContext);
    function getRestaurant(){
        axios.get("//localhost:8080/restaurants/" + String(id) ).then(
            (response) => {
                setRestaurant(response.data);
            }
        ).catch((error) => {
            console.log(error);
        });
    }

    function handleSelectedPeople(event){
        setSelectedPeople(event.target.value);
    }

    function handleSelectedDate(event){
        setSelectedDate(event.target.value);
    }

    function getAvailableTimes(){
        const params = { date: selectedDate, people: selectedPeople };
        axios.get("//localhost:8080/availableTimes", params).then(

        ).
    }

    useEffect(() => {
        if (selectedPeople && selectedDate) {
            getAvailableTimes();
        }
    }, [selectedPeople, selectedDate]);

    return (
        <div>
            <div className="container first-part">
                <div className="row justify-content-around">
                    <RestaurantProfile
                        title = {restaurant.title}
                        start = {restaurant.startTime}
                        end= {restaurant.endTime}
                        img = {restaurant.img}
                        reviews = {restaurant.reviews}
                        country = {restaurant.address.country}
                        city = {restaurant.address.city}
                        street = {restaurant.address.stret}
                        type = {restaurant.type}
                    />
                    <div className="table-reserve col-lg-8 col-xl-6 ps-5">
                        <p className="form-title">Reserve Table</p>
                        <form action="#" className="reserve-form">
                            <p className="selection d-flex align-items-center">
                                For
                                <select className="form-select mx-1" value={selectedPeople} onChange={handleSelectedPeople}>
                                    <option value="p1" selected>1</option>
                                    <option value="p2">2</option>
                                    <option value="p3">3</option>
                                    <option value="p4">4</option>
                                    <option value="p5">5</option>
                                    <option value="p6">6</option>
                                    <option value="p7">7</option>
                                    <option value="p8">8</option>
                                    <option value="p9">9</option>
                                    <option value="p10">10</option>
                                </select>
                                people, on date
                                <input type="date" name="date" id="date" className="calendar ms-2 px-2" value={selectedDate} onChange={handleSelectedDate}/>
                            </p>
                            <p className="time-title">Available Times</p>
                            <div >
                                <div className="time-checkbox text-center">
                                    <label>
                                        <input type="checkbox" value="1" /><span>11:00 PM</span>
                                    </label>
                                </div>
                            </div>
                            <p className="reserve-warning">You  will reserve this table  only for <u>one</u>  hour, for more time please contact the restaurant.</p>
                            <button type="button" className="final-reserve text-white">Complete the Reservation</button>
                        </form>
                    </div>
                </div>
                <div className="review-part d-flex justify-content-between py-3 px-4">
                    <div className="top-part d-flex flex-column">
                        <p className="review-start">What 160 people are saying</p>
                        <p className="rating-star">
                            <img src="./assets/img/full-star.svg" alt="star-img" />
                            <img src="./assets/img/full-star.svg" alt="star-img" />
                            <img src="./assets/img/full-star.svg" alt="star-img" />
                            <img src="./assets/img/full-star.svg" alt="star-img" />
                            <img src="./assets/img/empty-star.svg" alt="star-img" className="empty-star" />
                            4 based on recent ratings
                        </p>
                    </div>
                    <div className="d-flex justify-content-between align-items-center">
                        <div className="d-flex flex-column text-center mx-5">
                            <p className="rating-name">Food</p>
                            <p className="rating-number">4.5</p>
                        </div>
                        <div className="d-flex flex-column text-center mx-5">
                            <p className="rating-name">Service</p>
                            <p className="rating-number">4.1</p>
                        </div>
                        <div className="d-flex flex-column text-center mx-5">
                            <p className="rating-name">Ambience</p>
                            <p className="rating-number">3.8</p>
                        </div>
                        <div className="d-flex flex-column text-center ms-5">
                            <p className="rating-name">Overall</p>
                            <p className="rating-number">4</p>
                        </div>
                    </div>
                </div>
                <div className="add-review">
                    <div className="d-flex justify-content-between">
                        <p className="reviews-num">160 Reviews</p>
                        <button className="add-btn text-white">Add Review</button>
                    </div>
                    <div className="one-reviews d-flex justify-content-between">
                        <div className="right-part d-flex gap-3">
                            <div className="profile text-center flex-shrink-0">
                                <p>AD</p>
                            </div>
                            <div className="comment d-flex flex-column">
                                <p className="username">Ali Daei</p>
                                <p className="user-rating">
                                    Overall <span>5</span>
                                    <img src="./assets/img/dot.svg" alt="dot-img" className="dot" />
                                    Food <span>5</span>
                                    <img src="./assets/img/dot.svg" alt="dot-img" className="dot" />
                                    Service <span>5</span>
                                    <img src="./assets/img/dot.svg" alt="dot-img" className="dot" />
                                    Ambience <span>5</span>
                                </p>
                                <p className="comment-text">Excellent pre-theatre meal. Good food and service. Only small criticism is that music was intrusive.</p>
                            </div>
                        </div>
                        <div className="date-star d-flex flex-wrap justify-content-end">
                            <div className="stars flex-shrink-0">
                                <img src="./assets/img/full-star.svg" alt="star-img" />
                                <img src="./assets/img/full-star.svg" alt="star-img" />
                                <img src="./assets/img/full-star.svg" alt="star-img" />
                                <img src="./assets/img/full-star.svg" alt="star-img" />
                                <img src="./assets/img/full-star.svg" alt="star-img" />
                            </div>
                            <p className="comment-date flex-shrink-0">
                                Dined on February 17, 2024
                            </p>
                        </div>
                    </div>
                    <hr />
                </div>
                <nav aria-label="Page navigation">
                    <ul className="pagination justify-content-center">
                        <li className="page-item"><a className="page-link mx-1" href="">1</a></li>
                        <li className="page-item"><a className="page-link mx-1" href="">2</a></li>
                        <li className="page-item"><a className="page-link mx-1" href="">3</a></li>
                        <li className="page-item"><a className="page-link mx-1 disabled" href="">...</a></li>
                        <li className="page-item"><a className="page-link mx-1" href="">10</a></li>
                        <li className="page-item"><a className="page-link mx-1" href="">11</a></li>
                        <li className="page-item"><a className="page-link mx-1" href="">12</a></li>
                    </ul>
                </nav>
            </div>
        </div>
    );
}

export default Restaurant;