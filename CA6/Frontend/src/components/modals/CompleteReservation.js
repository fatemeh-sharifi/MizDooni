import React from "react";

function CompleteReservation(props) {
    return (
        <div className="modal fade" id="completeModal" tabindex="-1" aria-labelledby="completeModalLabel" aria-hidden="true">
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="completeModalLabel">Reservation Detail</h5>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div className="modal-body mb-4">
                        <div className="d-flex flex-column">
                            <p className="text-muted note mb-5">Note:  Please Arrive at Least 15 Minutes Early.</p>
                            <div className="reservDetails mx-3">
                                <div className="d-flex justify-content-between">
                                    <p>Table Number</p>
                                    <p>{props.tableNumber}</p>
                                </div>
                                <div className="d-flex justify-content-between">
                                    <p>Time</p>
                                    <p>{props.time}</p>
                                </div>
                                <div className="d-flex justify-content-start">
                                    <p>Address</p>
                                </div>
                            </div>
                            <p className="text-muted addressDetail mx-3">{props.address.country},  {props.address.city}, {props.address.street}</p>
                        </div>
                    </div>
                    <div className="modal-footer align-items-center">
                        <button type="button" className="btn closeBtn w-100 mx-3" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CompleteReservation;