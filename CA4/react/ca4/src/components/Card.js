import React from "react";
import "../assets/css/home.css"


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
        <div class="card mx-2 my-3 p-0">
            <img class="card-img-top" src={props.img} alt="Card-img" />
            <div class="star-part">
                {renderStars(props.rating)}
            </div>
            <div class="card-body">
                <h4 class="card-title">{props.title}</h4>
                <p class="card-text"><small class="text-muted">{props.reviews} reviews</small></p>
                <p class="card-text"><small class="about">{props.type}</small></p>
                <p class="card-text"><img src="img/location.svg" alt="location-img" class="location-img" /><small class="restaurant-data">{props.city}</small></p>
                {isOpen(props.start, props.end) ? (
                    <p class="card-text">
                        <small class="open-text">Open</small>
                        <img src="./assets/img/dot.svg" alt="dot-img" class="dot-img" />
                        <small class="restaurant-data">Closes at {convertToAMPM(props.end)}</small>
                    </p>
                ) : (
                    <p class="card-text">
                    <small class="close-text">Closed</small>
                    <img src="./assets/img/dot.svg" alt="dot-img" class="dot-img"/>
                    <small class="restaurant-data">Opens at {convertToAMPM(props.start)}</small></p>
                )}
            </div>
        </div>
    );
}

export default Card;