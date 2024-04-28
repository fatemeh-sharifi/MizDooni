import React from "react";
import "../assets/css/home.css"
import { Link } from "react-router-dom";


function Card(props) {
    function renderStars(rating) {
        const stars = [];
        const filledStars = Math.floor(rating);
        const emptyStars = 5 - filledStars;
        for (let i = 0; i < filledStars; i++) {
            stars.push(<img key={i} src="img/Vector.svg" alt="star-img" />);
        }
        for (let i = 0; i < emptyStars; i++) {
            stars.push(<img key={filledStars + i} src="img/empty-star.svg" alt="empty-star-img" />);
        }

        return stars;
    }

    function isOpen(start, end) {
        const date = new Date();
        const currentTime = date.getHours() + ":" + date.getMinutes();
        return currentTime >= start && currentTime <= end;
    }

    function convertToAMPM(time) {
        const [hours, minutes] = time.split(":");
        let hour = parseInt(hours);
        const ampm = hour >= 12 ? 'PM' : 'AM';
        hour = hour % 12 || 12;
        return `${hour}:${minutes} ${ampm}`;
    }

    return (
        <div calssName="card mx-2 my-3 p-0">
            <Link to={"/restaurant/" + String(props.id)}>
                <img calssName="card-img-top" src={props.img} alt="Card-img" />
            </Link>
            <div calssName="star-part">
                {renderStars(props.rating)}
            </div>
            <div calssName="card-body">
                <h4 calssName="card-title">{props.title}</h4>
                <p calssName="card-text"><small calssName="text-muted">{props.reviews} reviews</small></p>
                <p calssName="card-text"><small calssName="about">{props.type}</small></p>
                <p calssName="card-text"><img src="img/location.svg" alt="location-img" calssName="location-img" /><small calssName="restaurant-data">{props.city}</small></p>
                {isOpen(props.start, props.end) ? (
                    <p calssName="card-text">
                        <small calssName="open-text">Open</small>
                        <img src="img/dot.svg" alt="dot-img" calssName="dot-img" />
                        <small calssName="restaurant-data">Closes at {convertToAMPM(props.end)}</small>
                    </p>
                ) : (
                    <p calssName="card-text">
                        <small calssName="close-text">Closed</small>
                        <img src="img/dot.svg" alt="dot-img" calssName="dot-img" />
                        <small calssName="restaurant-data">Opens at {convertToAMPM(props.start)}</small></p>
                )}
            </div>
        </div>
    );
}

export default Card;