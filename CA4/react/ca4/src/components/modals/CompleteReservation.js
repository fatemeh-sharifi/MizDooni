import React from "react";

function CompleteReservation(props) {
    return (
        <div class="modal fade" id="completeModal" tabindex="-1" aria-labelledby="completeModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="completeModalLabel">Reservation Detail</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body mb-4">
                        <div class="d-flex flex-column">
                            <p class="text-muted note mb-5">Note:  Please Arrive at Least 15 Minutes Early.</p>
                            <div class="reservDetails mx-3">
                                <div class="d-flex justify-content-between">
                                    <p>Table Number</p>
                                    <p>{props.tableNumber}</p>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <p>Time</p>
                                    <p>{props.time}</p>
                                </div>
                                <div class="d-flex justify-content-start">
                                    <p>Address</p>
                                </div>
                            </div>
                            <p class="text-muted addressDetail mx-3">{props.address.country},  {props.address.city}, {props.address.street}</p>
                        </div>
                    </div>
                    <div class="modal-footer align-items-center">
                        <button type="button" class="btn closeBtn w-100 mx-3" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CompleteReservation;