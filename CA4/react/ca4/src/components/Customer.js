import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";


function Customer() {
    const UserInfo = useContext(UserContext);

    return (
        <div class="container main-part">
            <div class="mail-part d-flex justify-content-between align-items-center mx-1">
                <p class="mail-text mx-4">Your reservations are also emailed to <a href="#">{UserInfo.email}</a> </p>
                <div>
                <p class="address mx-4">Address: {UserInfo.address.city}, {UserInfo.address.country}</p>
                </div>
            </div>
            <div class="table-par mx-1 mt-5">
                <div class="table-header d-flex align-items-center">
                    <p class="title mx-4">My Reservations</p>
                </div>
            </div>
            <div class="reserve d-flex justify-content-between align-items-center">
                <div class="d-flex justify-content-between w-75">
                    <p class="date ">2024-06-22 16:00</p>
                    <p class="rest-name "><a href="Restaurant.html"> Ali Daei Dizy</a></p>
                    <p class="table-number">Table-12</p>
                    <p class="number-seats">4 Seats</p>
                </div>
                <p class="reserve-action flex-shrink-0 text-end">Cancel</p>
            </div>
            <hr />
        </div>
    );
}

export default Customer;