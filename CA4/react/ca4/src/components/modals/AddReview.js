import React, { useState,useContext , useEffect } from "react";
import axios from "axios";
import Rating from "../Rating";
import { UserContext } from "../App";


function AddReview(props) {
    const [foodQualityRating, setFoodQualityRating] = useState(null);
    const [serviceRating, setServiceRating] = useState(null);
    const [ambienceRating, setAmbienceRating] = useState(null);
    const [overallRating, setOverallRating] = useState(null);
    const [comment, setComment] = useState("");
    const [showModal, setShowModal] = useState(true);
    const [allowed, setAllowed] = useState(true);
    const UserInfo = useContext(UserContext);

    function handleSubmitReview(){
        const params = {username : UserInfo.username , restaurant : props.name ,food : foodQualityRating , service: serviceRating, ambience : ambienceRating, overall : overallRating , review : comment}
        axios.post("http://localhost:8080/reviews", params).then(
            (response) => {
                console.log(response);
                if(response.status === 200){
                    setShowModal(false);
                }
            }
        ).catch((error) => {
            console.log(error);
        });
    }

    useEffect(()=>{
        setAllowed(true);
        isAble();
    }, [])

    function isAble(){
        const params = {username : UserInfo.username , restaurant : props.name }
        axios.post("http://localhost:8080/reviews", params).then(
            (response) => {
                console.log(response);
                if(response.status !== 200){
                    setAllowed(false);
                }
            }
        ).catch((error) => {
            console.log(error);
        });
    }
    return (
        <div className={`modal fade ${showModal ? 'show' : ''}`} id="addReviewModal" tabindex="-1" aria-labelledby="addReviewModalLabel" aria-hidden="true">
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="addReviewModalLabel">Add Review for <span className="modalRestaurantName">{props.name}</span></h5>
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
                        <button type="button" disabled ={!foodQualityRating || !serviceRating || !overallRating || !ambienceRating || !allowed} className="btn submitReview w-100 mx-3"onClick={handleSubmitReview}>Submit Review</button>
                        <button type="button" className="btn cancleBtn closeBtn w-100 mx-3" data-bs-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AddReview;