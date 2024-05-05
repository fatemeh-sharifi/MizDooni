import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Rating from "./Rating";
import ReviewPart from "./ReviewPart";



function Review(props) {
    const [foodQualityRating, setFoodQualityRating] = useState(null);
    const [serviceRating, setServiceRating] = useState(null);
    const [ambienceRating, setAmbienceRating] = useState(null);
    const [overallRating, setOverallRating] = useState(null);
    const [comment, setComment] = useState("");
    const [allowed, setAllowed] = useState(true);
    const UserInfo = useContext(UserContext);
    const [showModal, setShowModal] = useState(false);

    function handleSubmitReview(event) {
        axios.post("http://localhost:8080/reviews", null, {
            params: {
                username: UserInfo.username,
                restaurantName: props.restaurant.name,
                foodRate: foodQualityRating,
                serviceRate: serviceRating,
                ambianceRate: ambienceRating,
                overallRate: overallRating,
                comment: comment
            }
        }).then(
            (response) => {
                console.log(response);
                if (response.status === 200) {
                    // setShowModal(false);
                    // document.getElementById('addReviewModal').hide()
                    setShowModal(false);
                    props.setReviewSubmitted(true);
                }
            }
        ).catch((error) => {
            if (error.message === "Request failed with status code 400") {
                event.preventDefault();
                setAllowed(false);
            }
            console.log(error);
        });
    }
    function createReviewPart(review, index) {
        return (
            <ReviewPart
                key={index}
                username={review.username}
                overallRate={review.overallRate}
                foodRate={review.foodRate}
                serviceRate={review.serviceRate}
                ambianceRate={review.ambianceRate}
                comment={review.comment}
                dateTime={review.dateTime}
            />
        );
    }

    function renderStars(rating) {
        const stars = [];
        const filledStars = Math.floor(rating);
        const emptyStars = 5 - filledStars;
        for (let i = 0; i < filledStars; i++) {
            stars.push(<img key={i} src="img/full-star.svg" alt="star-img" />);
        }
        for (let i = 0; i < emptyStars; i++) {
            stars.push(<img key={filledStars + i} src="img/empty-star.svg" alt="empty-star-img" />);
        }

        return stars;
    }

    return (
        <div>
            <div className="review-part d-flex justify-content-between py-3 px-4">
                <div className="top-part d-flex flex-column">
                    <p className="review-start">What {props.restaurant && props.restaurant.feedbacks.length} people are saying</p>
                    <p className="rating-star">
                        {props.restaurant && renderStars(props.restaurant.overallAvg)}
                        {props.restaurant && props.restaurant.overallAvg.toFixed(2)} based on recent ratings
                    </p>
                </div>
                <div className="d-flex justify-content-between align-items-center">
                    <div className="d-flex flex-column text-center mx-5">
                        <p className="rating-name">Food</p>
                        <p className="rating-number">{props.restaurant && props.restaurant.foodAvg.toFixed(2)}</p>
                    </div>
                    <div className="d-flex flex-column text-center mx-5">
                        <p className="rating-name">Service</p>
                        <p className="rating-number">{props.restaurant && props.restaurant.serviceAvg.toFixed(2)}</p>
                    </div>
                    <div className="d-flex flex-column text-center mx-5">
                        <p className="rating-name">Ambience</p>
                        <p className="rating-number">{props.restaurant && props.restaurant.ambianceAvg.toFixed(2)}</p>
                    </div>
                    <div className="d-flex flex-column text-center ms-5">
                        <p className="rating-name">Overall</p>
                        <p className="rating-number">{props.restaurant && props.restaurant.overallAvg.toFixed(2)}</p>
                    </div>
                </div>
            </div>
            <div className="add-review">
                <div className="d-flex justify-content-between">
                    <p className="reviews-num">{props.restaurant && props.restaurant.feedbacks.length} Reviews</p>
                    <button className="add-btn text-white" data-bs-toggle="modal" data-bs-target="#addReviewModal">Add Review</button>
                    <div className="modal fade" id="addReviewModal" tabindex="-1" aria-labelledby="addReviewModalLabel" aria-hidden="true" style={{ display: showModal ? 'block' : 'none' }}>
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
                                    {!allowed && <p className="reviewErr mb-0 mt-3 mx-3">You had no reservation at {props.restaurant && props.restaurant.name} Restaurant before.</p>}
                                </div>
                                <div className="modal-footer align-items-center">
                                    <button type="button" data-bs-dismiss={allowed ? "modal" : undefined} disabled={!foodQualityRating || !serviceRating || !overallRating || !ambienceRating || !allowed} className={`btn submitReview w-100 mx-3 ${(!foodQualityRating || !serviceRating || !overallRating || !ambienceRating || !allowed) ? 'disabled' : ''}`} onClick={handleSubmitReview}>Submit Review</button>
                                    <button type="button" className="btn cancleBtn closeBtn w-100 mx-3" data-bs-dismiss="modal">Cancel</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                {props.restaurant && props.restaurant.feedbacks && props.restaurant.feedbacks.map(createReviewPart)}
                {props.restaurant && props.restaurant.feedbacks.length===0 && <p className="noReview text-center">No Reviews yet!</p>}
            </div>
        </div>
    );
}

export default Review;