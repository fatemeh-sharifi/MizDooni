import React, { useState,useContext } from "react";
import axios from "axios";
import Rating from "../Rating";
import { UserContext } from "../App";


function AddReview(props) {
    const [foodQualityRating, setFoodQualityRating] = useState(null);
    const [serviceRating, setServiceRating] = useState(null);
    const [ambienceRating, setAmbienceRating] = useState(null);
    const [overallRating, setOverallRating] = useState(null);
    const [comment, setComment] = useState("");
    const UserInfo = useContext(UserContext);

    function handleSubmitReview(){
        const params = {username : UserInfo.username , restaurant : props.name ,food : foodQualityRating , service: serviceRating, ambience : ambienceRating, overall : overallRating , review : comment}
        axios.post("//localhost:8080/reviews", params).then(
            (response) => {
                console.log(response);
            }
        ).catch((error) => {
            console.log(error);
        });
    }
    return (
        <div className="modal fade" id="completeModal" tabindex="-1" aria-labelledby="completeModalLabel" aria-hidden="true">
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="completeModalLabel">Add Review for <span className="modalRestaurantName">{props.name}</span></h5>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div className="modal-body mb-4">
                        <div className="d-flex flex-column">
                            <p className="text-muted note mb-5">Note: Reviews can only be made by diners who have eaten at this restaurant </p>
                            <div className="reservDetails mx-3">
                                <Rating
                                    name="Food Quality"
                                    rating={foodQualityRating}
                                    setRating={setFoodQualityRating}
                                />
                                <Rating
                                    name="Service"
                                    rating={serviceRating}
                                    setRating={setServiceRating}
                                />
                                <Rating
                                    name="Ambience"
                                    rating={ambienceRating}
                                    setRating={setAmbienceRating}
                                />
                                <Rating
                                    name="Overall"
                                    rating={overallRating}
                                    setRating={setOverallRating}
                                />
                                <div className="d-flex justify-content-start">
                                    <p>Comment</p>
                                </div>
                            </div>
                            <textarea
                                className="form-control"
                                rows="5"
                                placeholder="Type your review..."
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                            ></textarea>
                        </div>
                    </div>
                    <div className="modal-footer align-items-center">
                        <button type="button" className="btn submitReview w-100 mx-3" data-bs-dismiss="modal" onClick={handleSubmitReview}>Submit Review</button>
                        <button type="button" className="btn cancleBtn closeBtn w-100 mx-3" data-bs-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AddReview;