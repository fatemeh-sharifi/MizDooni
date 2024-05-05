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

    function handleCancel(){
        const params = { username: UserInfo.username, tableNumber: props.tableNumber,
                restaurantName : props.name ,time: props.time,date: props.date};

        axios.post("http://localhost:8080/cancelReservation", null , {params : params}).then(
            (response) => {
                props.setIsCanceled(true);
            }
        ).catch((error)=>{
            console.log(error);
        })
    }

    function handleSubmitReview() {
        axios.post("http://localhost:8080/reviews", null, {
            params: {
                username: UserInfo.username,
                restaurantName: props.name,
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
    return (
        <div class="reserve d-flex justify-content-between align-items-center">
            <div class="d-flex justify-content-between w-75">
                <p class={`date ${props.canceled ? 'canceled' : ''} ${props.after ? 'disable' : ''}`}>{props.date} {props.time}</p>
                <Link to={'/restaurant/' + String(props.id)}>
                    <p class={`rest-name ${props.canceled ? 'canceled' : ''}`}  >{props.name}</p>
                </Link>
                <p class={`table-number ${props.canceled ? 'canceled' : ''} ${props.after ? 'disable' : ''}`}>Table-{props.tableNumber}</p>
                <p class={`number-seats ${props.canceled ? 'canceled' : ''} ${props.after ? 'disable' : ''}`}>{props.seatNumber} Seats</p>
            </div>
            {!props.after ? (
                <button class="reserve-action flex-shrink-0 text-end" data-bs-toggle="modal" data-bs-target="#cancelModal">Cancel</button>
            ) : (
                <button class="reserve-action flex-shrink-0" data-bs-toggle="modal" data-bs-target="#addReviewModal">Add Comment</button>
            )}
            <div className="modal fade" id="cancelModal" tabIndex="-1"
                aria-labelledby="cancelModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="cancelModalLabel">Cancel Reservation at <span className="modalRestaurantName">{props.name}</span></h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                        </div>
                        <div className="modal-body mb-4">
                            <div className="d-flex flex-column">
                                <p className="text-muted note mb-5">Note: Once you hit the Cancel button, your reserve will be canceled.</p>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault" onChange={handleCheckboxChange} />
                                    <label class="form-check-label" for="flexCheckDefault">
                                        I agree
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer align-items-center">
                            <button type="button" disabled={!agree} className={`btn closeBtn w-100 mx-3 ${!agree ? 'disabled' : ''}`} onClick={handleCancel}
                                data-bs-dismiss="modal">Cancel
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div className="modal fade" id="addReviewModal" tabindex="-1" aria-labelledby="addReviewModalLabel" aria-hidden="true" >
                        <div className="modal-dialog modal-dialog-centered">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title" id="addReviewModalLabel">Add Review for <span className="modalRestaurantName">{props.restaurant && props.restaurant.name}</span></h5>
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
                                    <button type="button" data-bs-dismiss= "modal" disabled={!foodQualityRating || !serviceRating || !overallRating || !ambienceRating } className={`btn submitReview w-100 mx-3 ${(!foodQualityRating || !serviceRating || !overallRating || !ambienceRating ) ? 'disabled' : ''}`} onClick={handleSubmitReview}>Submit Review</button>
                                    <button type="button" className="btn cancleBtn closeBtn w-100 mx-3" data-bs-dismiss="modal">Cancel</button>
                                </div>
                            </div>
                        </div>
                    </div>
        </div>
    );
}

export default Reservation;