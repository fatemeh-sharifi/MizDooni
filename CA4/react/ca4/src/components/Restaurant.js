import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { Link, useAsyncError, useParams } from "react-router-dom";
import CompleteReservation from "./modals/CompleteReservation";
import AddReview from "./modals/addReview";
import "../css/restaurant.css"

import RestaurantProfile from "./RestaurantProfile";

function Restaurant() {
    const { id } = useParams();
    const [restaurant, setRestaurant] = useState('');
    const [availableTimes, setAvailableTimes] = useState('');
    const [selectedPeople, setSelectedPeople] = useState('1');
    const [selectedDate, setSelectedDate] = useState('');
    const [selectedTime, setSelectedTime] = useState('');
    const [selectedTable, setSelectedTable] = useState('');
    const [modalShow, setModalShow] = useState(false);
    const [reviewModalShow, setReviewModalShow] = useState(false);
    const [maxLimit, setMaxLimit] = useState('');
    const UserInfo = useContext(UserContext);
    function getRestaurant() {
        axios.get("http://localhost:8080/restaurants/" + String(id)).then(
            (response) => {
                setRestaurant(response.data);
            }
        ).catch((error) => {
            console.log(error);
        });
    }

    function handleSelectedPeople(event) {
        setSelectedPeople(event.target.value);
    }

    function handleSelectedDate(event) {
        setSelectedDate(event.target.value);
    }

    function handleReservation() {
        setModalShow(true);
        const params = { username: UserInfo.username, time: selectedTime, table: selectedTable, restaurantId: id };
        axios.post("http://localhost:8080/reservation", null, { params: params }).then(
            (response) => {
                console.log(response.data);
            }
        ).catch((error) => {
            console.log(error);
        })
    }

    function renderStars(rating) {
        const stars = [];
        const filledStars = Math.floor(rating);
        const emptyStars = 5 - filledStars;
        for (let i = 0; i < filledStars; i++) {
            stars.push(<img key={i} src="img/Vector.svg" alt="star-img" />);
        }
        for (let i = 0; i < emptyStars; i++) {
            stars.push(<img key={filledStars + i} src="img/empty-star.svg" alt="empty-star-img" />);
        }

        return stars;
    }

    function handleTimeChange(time, number) {
        setSelectedTime(time);
        setSelectedTable(number);
    };

    useEffect(() => {
        function getAvailableTimes() {
            const params = { date: selectedDate, people: selectedPeople };
            axios.get("http://localhost:8080/availableTimes", null, { params: params }).then(
                (response) => {
                    if (response.status === 200) {
                        setAvailableTimes(response.data);
                    }
                    else {
                        setMaxLimit(response.data);
                    }

                }
            ).catch((error) => {
                console.log(error);
            });
        }
        if (selectedPeople && selectedDate) {
            getAvailableTimes();
        }
    }, [selectedPeople, selectedDate]);

    useEffect(() => {
        setRestaurant(null);
        getRestaurant();
    }, []);

    return (
        <div>
            <div className="container first-part">
                <div className="row justify-content-around">
                    <RestaurantProfile
                        title={restaurant.name}
                        start={restaurant.startTime}
                        end={restaurant.endTime}
                        img={restaurant.image}
                        reviews={restaurant.feedbacks.length}
                        country={restaurant.address.country}
                        city={restaurant.address.city}
                        street={restaurant.address.street}
                        type={restaurant.type}
                    />
                    <div className="table-reserve col-lg-8 col-xl-6 ps-5">
                        <p className="form-title">Reserve Table</p>
                        <form className="reserve-form">
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
                                <input type="date" name="date" id="date" className="calendar ms-2 px-2" value={selectedDate} onChange={handleSelectedDate} />
                            </p>
                            {availableTimes && availableTimes.length > 0 && (
                                <p className="time-title">Available Times</p>
                            )}
                            <div >
                                <div className="time-checkbox text-center">
                                    {(availableTimes && !maxLimit) ? (
                                        availableTimes.map((timeData, index) => (
                                            <label key={index}>
                                                <input
                                                    type="radio"
                                                    name="time"
                                                    value={timeData.time}
                                                    onChange={() => handleTimeChange(timeData.time, timeData.number)}
                                                    checked={selectedTime === timeData.time}
                                                />
                                                <span>{timeData.time}</span>
                                            </label>
                                        ))
                                    ) : (
                                        maxLimit ? (
                                            <p className="maxTime">The maximum possible date for reservation is {maxLimit}.</p>
                                        ) : (
                                            <p className="notAvailable">No Table is Available on this date.</p>
                                        )
                                    )}
                                </div>
                            </div>
                            <p className="reserve-warning">You  will reserve this table  only for <u>one</u>  hour, for more time please contact the restaurant.</p>
                            <button type="button" disabled={!availableTimes || maxLimit} className="final-reserve text-white" onClick={handleReservation} data-bs-toggle="modal" data-bs-target="#completeModal">
                                {(maxLimit || !selectedDate || !selectedPeople) ? (
                                    "Select a date"
                                ) : (
                                    "Complete the Reservation"
                                )}
                            </button>
                            {modalShow && (
                                <CompleteReservation
                                    tableNumber={selectedTable}
                                    time={selectedTime}
                                    address={restaurant.address}
                                />
                            )}
                        </form>
                    </div>
                </div>
                <div className="review-part d-flex justify-content-between py-3 px-4">
                    <div className="top-part d-flex flex-column">
                        <p className="review-start">What {restaurant.feedbacks.length} people are saying</p>
                        <p className="rating-star">
                            {renderStars(restaurant.overallAvg)}
                            {restaurant.overallAvg} based on recent ratings
                        </p>
                    </div>
                    <div className="d-flex justify-content-between align-items-center">
                        <div className="d-flex flex-column text-center mx-5">
                            <p className="rating-name">Food</p>
                            <p className="rating-number">{restaurant.foodAvg}</p>
                        </div>
                        <div className="d-flex flex-column text-center mx-5">
                            <p className="rating-name">Service</p>
                            <p className="rating-number">{restaurant.serviceAvg}</p>
                        </div>
                        <div className="d-flex flex-column text-center mx-5">
                            <p className="rating-name">Ambience</p>
                            <p className="rating-number">{restaurant.ambienceAvg}</p>
                        </div>
                        <div className="d-flex flex-column text-center ms-5">
                            <p className="rating-name">Overall</p>
                            <p className="rating-number">{restaurant.overallAvg}</p>
                        </div>
                    </div>
                </div>
                <div className="add-review">
                    <div className="d-flex justify-content-between">
                        <p className="reviews-num">{restaurant.feedbacks.length} Reviews</p>
                        <button className="add-btn text-white" onClick={() => { setReviewModalShow(true) }}>Add Review</button>
                        {reviewModalShow && (
                            <AddReview
                                name={restaurant.name}
                            />
                        )}
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