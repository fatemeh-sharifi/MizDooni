import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { useParams } from "react-router-dom";
import "../css/restaurant.css"
import Review from "./Review";
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
    const [maxLimit, setMaxLimit] = useState('');
    const UserInfo = useContext(UserContext);
    const [reviewSubmitted, setReviewSubmitted] = useState(false);

    function getRestaurant() {
        console.log("id : ", id);
        axios.get("http://localhost:8080/restaurants/" + String(id)).then(
            (response) => {
                console.log("restaurant : ", response.data);
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
        // setModalShow(true);
        const params = { username: UserInfo.username, time: selectedTime, table: selectedTable, restaurantId: id };
        axios.post("http://localhost:8080/reservation", null, { params: params }).then(
            (response) => {
                console.log(response.data);
            }
        ).catch((error) => {
            console.log(error);
        })
    }



    function handleTimeChange(time, number) {
        setSelectedTime(time);
        setSelectedTable(number);
    }



    useEffect(() => {
        function getAvailableTimes() {
            const params = { date: selectedDate, numberOfPeople: selectedPeople, restaurantName: restaurant.name };
            console.log("params : ", params);
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
        console.log("reviewSubmited");
    }, [reviewSubmitted]);

    // useEffect(() => {
    //     setRestaurant(null);
    //     getRestaurant();
    // }, []);

    return (
        <div>
            <div className="container w-100 first-part">
                <div className="row justify-content-around">
                    {restaurant && <RestaurantProfile
                        title={restaurant.name}
                        start={restaurant.startTime}
                        end={restaurant.endTime}
                        img={restaurant.image}
                        description={restaurant.description}
                        reviews={restaurant.feedbacks.length}
                        country={restaurant.address.country}
                        city={restaurant.address.city}
                        street={restaurant.address.street}
                        type={restaurant.type}
                    />}
                    <div className="table-reserve col-lg-8 col-xl-6 ps-5">
                        <p className="form-title">Reserve Table</p>
                        <form className="reserve-form">
                            <p className="selection w-100 d-flex align-items-center">
                                For
                                <select className="form-select mx-1" value={selectedPeople}
                                        onChange={handleSelectedPeople}>
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
                                <input type="date" name="date" id="date" className="calendar ms-2 px-2"
                                       value={selectedDate} onChange={handleSelectedDate}/>
                            </p>
                            {availableTimes && availableTimes.length > 0 && (
                                <p className="time-title">Available Times</p>
                            )}
                            <div>
                                {(availableTimes && !maxLimit) && (
                                    availableTimes.map((timeData, index) => (
                                        <div className="time-checkbox text-center">
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
                                        </div>
                                    ))
                                )}
                            </div>
                            {
                                maxLimit ? (
                                    <p className="reserve-warning">The maximum possible date for reservation
                                        is {maxLimit}.</p>
                                ) : (
                                    (!selectedDate || !selectedPeople) ? (
                                        <p className="reserve-warning">Select the number of people and date.</p>
                                    ) : (
                                        !availableTimes ? (
                                            <p className="notAvailable">No Table is Available on this date.</p>
                                        ) : (
                                            <p className="reserve-warning">You will reserve this table only
                                                for <u>one</u> hour, for more time please contact the restaurant.</p>
                                        )
                                    )
                                )}
                            <button type="button" disabled={!availableTimes || maxLimit}
                                    className={`final-reserve text-white ${(!availableTimes || maxLimit) ? 'disabled' : ''}`}
                                    onClick={handleReservation} data-bs-toggle="modal" data-bs-target="#completeModal">
                                {(maxLimit || !selectedDate || !selectedPeople) ? (
                                    "Select a date"
                                ) : (
                                    "Complete the Reservation"
                                )}
                            </button>
                            {/* {modalShow && (
                                <CompleteReservation
                                    tableNumber={selectedTable}
                                    time={selectedTime}
                                    address={restaurant.address}
                                />
                            )} */}
                            {/*<div className="modal fade" id="completeModal" tabindex="-1"*/}
                            {/*     aria-labelledby="completeModalLabel" aria-hidden="true">*/}
                            {/*    <div className="modal-dialog modal-dialog-centered">*/}
                            {/*        <div className="modal-content">*/}
                            {/*            <div className="modal-header">*/}
                            {/*                <h5 className="modal-title" id="completeModalLabel">Reservation Detail</h5>*/}
                            {/*                <button type="button" className="btn-close" data-bs-dismiss="modal"*/}
                            {/*                        aria-label="Close"></button>*/}
                            {/*            </div>*/}
                            {/*            <div className="modal-body mb-4">*/}
                            {/*                <div className="d-flex flex-column">*/}
                            {/*                    <p className="text-muted note mb-5">Note: Please Arrive at Least 15*/}
                            {/*                        Minutes Early.</p>*/}
                            {/*                    <div className="reservDetails mx-3">*/}
                            {/*                        <div className="d-flex justify-content-between">*/}
                            {/*                            <p>Table Number</p>*/}
                            {/*                            <p>{selectedTable}</p>*/}
                            {/*                        </div>*/}
                            {/*                        <div className="d-flex justify-content-between">*/}
                            {/*                            <p>Time</p>*/}
                            {/*                            <p>{selectedTime}</p>*/}
                            {/*                        </div>*/}
                            {/*                        <div className="d-flex justify-content-start">*/}
                            {/*                            <p>Address</p>*/}
                            {/*                        </div>*/}
                            {/*                    </div>*/}
                            {/*                    <p className="text-muted addressDetail mx-3">{restaurant.address.country}, {restaurant.address.city}, {restaurant.address.street}</p>*/}
                            {/*                </div>*/}
                            {/*            </div>*/}
                            {/*            <div className="modal-footer align-items-center">*/}
                            {/*                <button type="button" className="btn closeBtn w-100 mx-3"*/}
                            {/*                        data-bs-dismiss="modal">Close*/}
                            {/*                </button>*/}
                            {/*            </div>*/}
                            {/*        </div>*/}
                            {/*    </div>*/}
                            {/*</div>*/}
                            <div className="modal fade" id="completeModal" tabIndex="-1"
                                 aria-labelledby="completeModalLabel" aria-hidden="true">
                                <div className="modal-dialog modal-dialog-centered">
                                    <div className="modal-content">
                                        <div className="modal-header">
                                            <h5 className="modal-title" id="completeModalLabel">Reservation Detail</h5>
                                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                        </div>
                                        <div className="modal-body mb-4">
                                            <div className="d-flex flex-column">
                                                <p className="text-muted note mb-5">Note: Please Arrive at Least 15
                                                    Minutes Early.</p>
                                                <div className="reservDetails mx-3">
                                                    <div className="d-flex justify-content-between">
                                                        <p>Table Number</p>
                                                        <p>{selectedTable}</p>
                                                    </div>
                                                    <div className="d-flex justify-content-between">
                                                        <p>Time</p>
                                                        <p>{selectedTime}</p>
                                                    </div>
                                                    <div className="d-flex justify-content-start">
                                                        <p>Address</p>
                                                    </div>
                                                </div>
                                                {/* Conditionally render the address details */}
                                                {restaurant && restaurant.address && (
                                                    <p className="text-muted addressDetail mx-3">{restaurant.address.country}, {restaurant.address.city}, {restaurant.address.street}</p>
                                                )}
                                            </div>
                                        </div>
                                        <div className="modal-footer align-items-center">
                                            <button type="button" className="btn closeBtn w-100 mx-3"
                                                    data-bs-dismiss="modal">Close
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
                <Review
                    restaurant={restaurant}
                    setReviewSubmitted={setReviewSubmitted}
                />
            </div>
        </div>
    );
}

export default Restaurant;