import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import { Link } from "react-router-dom";
import Rating from "./Rating";
import axios from "axios";

function Reservation(props) {
    const UserInfo = useContext(UserContext);
    const [foodQualityRating, setFoodQualityRating] = useState(null);
    const [serviceRating, setServiceRating] = useState(null);
    const [ambienceRating, setAmbienceRating] = useState(null);
    const [overallRating, setOverallRating] = useState(null);
    const [comment, setComment] = useState("");

    const [agree, setAgree] = useState(false);
    function handleCheckboxChange(event) {
        setAgree(event.target.checked);
    }

    function handleCancel(reservationNumber, tableNumber, name) {
        const params = {
            username: UserInfo.username, tableNumber: tableNumber,
            restaurantName: name, reservationNumber: reservationNumber
        };

        console.log("cancel params :", params)

        axios.post("http://localhost:8080/cancelReservation", null, { params: params }).then(
            (response) => {
                console.log(response.data)
                props.setIsCanceled(true);
            }
        ).catch((error) => {
            console.log(error);
        })
    }

    function handleSubmitReview() {
        axios.post("http://localhost:8080/reviews", null, {
            params: {
                username: UserInfo.username,
                restaurantName: props.reservation.restaurantName,
                foodRate: foodQualityRating,
                serviceRate: serviceRating,
                ambianceRate: ambienceRating,
                overallRate: overallRating,
                comment: comment
            }
        }).then(
            (response) => {
                console.log(response);
            }
        ).catch((error) => {
            console.log(error);
        });
    }

    function extractHourAndMinute (timeString) {
        const [hour, minute] = timeString.split(':');
        return `${hour}:${minute}`;
    };

    return (
        <div className="reserve d-flex justify-content-between align-items-center">
            <div className="d-flex justify-content-between w-75">
                <p className={`date ${props.reservation.canceled ? 'canceled disableReserv' : ''} ${props.after ? 'disable' : ''}`}>{props.reservation.date} {extractHourAndMinute(props.reservation.time)}</p>
                <Link to={'/restaurant/' + String(props.reservation.restaurantId)}>
                    <p className={`rest-name ${props.reservation.canceled ? 'canceled' : ''}`}  >{props.reservation.restaurantName}</p>
                </Link>
                <p className={`table-number ${props.reservation.canceled ? 'canceled disableReserv' : ''} ${props.after ? 'disable' : ''}`}>Table-{props.reservation.tableNumber}</p>
                <p className={`number-seats ${props.reservation.canceled ? 'canceled disableReserv' : ''} ${props.after ? 'disable' : ''}`}>{props.reservation.tableSeat} Seats</p>
            </div>
            {!props.after ? (
                <button className={`reserve-action flex-shrink-0 text-end  ${props.reservation.canceled ? 'btnDisabled' : ''}`} 
                disabled={props.reservation.canceled} data-bs-toggle="modal" 
                data-bs-target={"#cancelModal"+props.reservation.reservationNumber}>Cancel</button>
            ) : (
                <button className="reserve-action flex-shrink-0" data-bs-toggle="modal" 
                data-bs-target={"#addReviewModal"+props.reservation.reservationNumber}>Add Comment</button>
            )}
            <div className="modal fade" id={"cancelModal"+props.reservation.reservationNumber} tabIndex="-1"
                aria-labelledby="cancelModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="cancelModalLabel">Cancel Reservation at <span className="modalRestaurantName">{props.reservation.restaurantName}</span></h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                        </div>
                        <div className="modal-body mb-4">
                            <div className="d-flex flex-column">
                                <p className="text-muted note mb-5">Note: Once you hit the Cancel button, your reserve will be canceled.</p>
                                <div className="form-check">
                                    <input className="form-check-input" type="checkbox" value="" id="flexCheckDefault" onChange={handleCheckboxChange} />
                                    <label className="form-check-label" htmlFor="flexCheckDefault">
                                        I agree
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer align-items-center">
                            <button type="button" disabled={!agree} className={`btn closeBtn w-100 mx-3 ${!agree ? 'disabled' : ''}`}
                                onClick={() => props.reservation && handleCancel(props.reservation.reservationNumber, props.reservation.tableNumber, props.reservation.restaurantName)} 
                                data-bs-dismiss="modal">Cancel
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div className="modal fade" id={"addReviewModal"+props.reservation.reservationNumber} tabindex="-1" aria-labelledby="addReviewModalLabel" aria-hidden="true" >
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="addReviewModalLabel">Add Review for <span className="modalRestaurantName">{props.reservation && props.reservation.restaurantName}</span></h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <div className="d-flex flex-column">
                                <p className="text-muted note mb-5">Note: Reviews can only be made by diners who have eaten at this restaurant </p>
                                <div className="reservDetails mx-3">
                                    <Rating
                                        name={"Food Quality"}
                                        rating={foodQualityRating}
                                        setRating={setFoodQualityRating}
                                    />
                                    <Rating
                                        name={"Service"}
                                        rating={serviceRating}
                                        setRating={setServiceRating}
                                    />
                                    <Rating
                                        name={"Ambience"}
                                        rating={ambienceRating}
                                        setRating={setAmbienceRating}
                                    />
                                    <Rating
                                        name={"Overall"}
                                        rating={overallRating}
                                        setRating={setOverallRating}
                                    />
                                    <div className="d-flex align-items-center justify-content-start mb-2">
                                        <p className="mb-0">Comment</p>
                                    </div>
                                </div>
                                <textarea
                                    className="form-control w-100"
                                    rows="5"
                                    placeholder="Type your review..."
                                    value={comment}
                                    onChange={(e) => setComment(e.target.value)}
                                ></textarea>
                            </div>
                        </div>
                        <div className="modal-footer align-items-center">
                            <button type="button" data-bs-dismiss="modal" disabled={!foodQualityRating || !serviceRating || !overallRating || !ambienceRating} className={`btn submitReview w-100 mx-3 ${(!foodQualityRating || !serviceRating || !overallRating || !ambienceRating) ? 'disabled' : ''}`} onClick={handleSubmitReview}>Submit Review</button>
                            <button type="button" className="btn cancleBtn closeBtn w-100 mx-3" data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Reservation;