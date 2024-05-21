import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Reservation from "./Reservation";
import "../css/Customer.css"


function Customer() {
    const UserInfo = useContext(UserContext);
    const [reservations, setReservations] = useState([]);
    const [isCanceld, setIsCanceled] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            fetchUserData(token);
        }
    }, []);
    function fetchUserData(token) {
        axios.get('http://localhost:8080/user', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(
            (response) => {
                if (response.status === 200) {
                    const userData = response.data;
                    UserInfo.setAllInfo(userData);
                }
            },
            (error) => {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: error.response ? error.response.data.message : 'An error occurred. Please try again.',
                });
            }
        );
    }
    function getReservations() {
        const params = { username: UserInfo.username }
        axios.get("http://localhost:8080/reservations", { params: params }).then(
            (response) => {
                console.log("reservations : ", response.data);
                setReservations(response.data);
            }
        ).catch((error) => {
            console.log(error);
        })
    }

    useEffect(() => {
        setReservations(null);
        getReservations();
        setIsCanceled(false);
    }, [isCanceld])

    function isPassedReservation(date, time) {
        const reservationDateTime = new Date(date + ' ' + time);
        const currentTime = new Date();
        return currentTime > reservationDateTime;
    }

    // function createReservation(reservation, index, isLast) {
    //     return (
    //         <div>
    //             <Reservation
    //                 key={index}
    //                 date={reservation.date}
    //                 time={reservation.time}
    //                 id={reservation.restaurantId}
    //                 name={reservation.restaurantName}
    //                 tableNumber={reservation.tableNumber}
    //                 canceled={reservation.canceled}
    //                 after={isPassedReservation(reservation.date, reservation.time)}
    //                 seatNumber={reservation.tableSeat}
    //                 isCanceld = {isCanceld}
    //                 setIsCanceled={setIsCanceled}
    //                 reservationNumber = {reservation.reservationNumber}
    //             />
    //             {!isLast && <hr/>}
    //         </div>
    //     );
    // }

    function createReservation(reservation, index, isLast) {
        return (
            <div>
                {reservation && <Reservation
                    key={index}
                    reservation={reservation}
                    after={isPassedReservation(reservation.date, reservation.time)}
                    isCanceld={isCanceld}
                    setIsCanceled={setIsCanceled}
                />}
                {!isLast && <hr />}
            </div>
        );
    }

    return (
        <div className="container w-100 p-0 main-part-customer">
            <div className="mail-part d-flex justify-content-between align-items-center mx-1">
                <p className="mail-text mx-4">Your reservations are also emailed to <a href="#">{UserInfo.email}</a> </p>
                <div className="d-flex flex-wrap gap-1">
                    <p className="address ">Address: {UserInfo.address.city}, {UserInfo.address.country}</p>
                    <button type="button" className="logout text-white me-3" onClick={() => { UserInfo.setLoggedIn(false); localStorage.removeItem("jwtToken"); }}>Logout</button>
                </div>
            </div>
            <div className="table-par mx-1 mt-4">
                <div className="table-header d-flex align-items-center">
                    <p className="title mx-4">My Reservations</p>
                </div>
            </div>
            {reservations && reservations.map((reservation, index) => createReservation(reservation, index, index === reservations.length - 1))}            <hr />
        </div>
    );
}

export default Customer;