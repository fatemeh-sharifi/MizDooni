import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { UserContext } from "../App";
import "../css/Navbar.css"
// import "../public"

function Navbar(){
    const UserInfo = useContext(UserContext);
    return (
        <nav className="container-fluid sticky d-flex justify-content-between">
            <div className="d-flex">
                <Link to="/"> 
                    <img src="img/Logo.svg" alt="logo-img" className="logo"/>
                </Link>
                <p className="headTitle mx-3">Reserve Table From Anywhere!</p>
            </div>
            {!UserInfo.loggedIn ? (
                <Link to="/Login">
                <button type="button" className="reserveBtn text-white">Reserve Now!</button>
                </Link>
            ) : (
                <div className="logged-in d-flex align-items-center">
                    {UserInfo.role === "manager" ? (
                        <>
                        <p className="welcome me-2">Welcome, Admin!</p>
                        <Link to="/manager-restaurants">
                            <button type="button" className="reserveBtn text-white">My Restaurants</button>
                        </Link>
                        </>
                    ):(
                        <>
                        <p className="welcome me-2">Welcome, {UserInfo.username}</p>
                        <Link to="/reservations">
                            <button type="button" className="reserveBtn text-white"> My Reservations</button>
                        </Link>
                        </>
                    )}
            </div>
            )}
            
        </nav>
    );
};

export default Navbar;