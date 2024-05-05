import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Reservation from "./Reservation";
import "../css/Customer.css"


function Customer() {
    const UserInfo = useContext(UserContext);
    const [reservations, setReservations] = useState([]);
    const [isCanceld, setIsCanceled] = useState(false);

    function getReservations() {
        axios.get("https://localhost:8080/reservations/" + String(UserInfo.username)).then(
            (response) => {
                setReservations(response.data);
            }
        ).catch((error) => {
            console.log(error);
        })
    }

    useEffect(() => {
        setReservations(null);
        getReservations();
    }, [isCanceld])

    function isPassedReservation(date,time){
        const reservationDateTime = new Date(date + ' ' +time); 
        const currentTime = new Date(); 
         return currentTime > reservationDateTime;
    }

    function createReservation(reservation, index) {
        return (
            <Reservation
                key={index}
                date={reservation.date}
                time = {reservation.time}
                id={reservation.restaurantId}
                name={reservation.restaurantName}
                tableNumber={reservation.tableNumber}
                canceled = {reservation.canceled}
                after = {isPassedReservation(reservation.date,reservation.time)}
                seatNumber = {reservation.tableSeat}
                setIsCanceled = {setIsCanceled}
            />
        );
    }

    return (
        <div class="container main-part">
            <div class="mail-part d-flex justify-content-between align-items-center mx-1">
                <p class="mail-text mx-4">Your reservations are also emailed to <a href="#">{UserInfo.email}</a> </p>
                <div>
                    <p class="address mx-4">Address: {UserInfo.address.city}, {UserInfo.address.country}</p>
                    <button type="button" className="logout text-white" onClick={() => { UserInfo.setLoggedIn(false) }}>Logout</button>
                </div>
            </div>
            <div class="table-par mx-1 mt-5">
                <div class="table-header d-flex align-items-center">
                    <p class="title mx-4">My Reservations</p>
                </div>
            </div>
            {reservations && reservations.map(createReservation)}
            <hr />
        </div>
    );
}

export default Customer;