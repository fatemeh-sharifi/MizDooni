import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import { Link } from "react-router-dom";
import axios from "axios";

function Reservation(props) {
    const [agree , setAgree] = useState(false);
    function handleCheckboxChange  (event){
        setAgree(event.target.checked);
    };
    return (
        <div class="reserve d-flex justify-content-between align-items-center">
            <div class="d-flex justify-content-between w-75">
                <p class="date ">{props.date}</p>
                <Link to={'/restaurant/' + String(props.id)}>
                    <p class="rest-name " >{props.name}</p>
                </Link>
                <p class="table-number">Table-{props.tableNumber}</p>
                <p class="number-seats">{props.seatNumber} Seats</p>
            </div>
            <button class="reserve-action flex-shrink-0 text-end" data-bs-toggle="modal" data-bs-target="#cancelModal">Cancel</button>
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
                                    <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault" onChange={handleCheckboxChange}/>
                                        <label class="form-check-label" for="flexCheckDefault">
                                            I agree
                                        </label>
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer align-items-center">
                            <button type="button" disabled={!agree} className="btn closeBtn w-100 mx-3"
                                data-bs-dismiss="modal">Cancel
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Reservation;