import React from "react";


function RestaurantProfile(props) {
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
        return `${hour} ${ampm}`;
    }
    return (
        <div className="alidaei col-lg-8 col-xl-6 pe-2">
            <img src={props.img} alt="restaurant-img" className="restaurant-img" />
            <div className="title-restaurant">
                <div className="head-part d-flex justify-content-between">
                    <p className={`restaurant-title ${props.title.length > 20 ? 'long' : ''}`}>{props.title}</p>
                    {isOpen(props.start, props.end) ? (
                        <div className="open text-white text-center">
                            <p>Open!</p>
                        </div>
                    ) : (
                        <div className="close text-white text-center">
                            <p>Closed!</p>
                        </div>
                    )}
                </div>
                <hr className="img-line" />
                <div className="details ">
                    <div className="d-flex justify-content-between">
                    <p className="time-details">
                        <img src="/img/clk.svg" alt="clock-img" className="time-logo" />
                        From {convertToAMPM(props.start)} to {convertToAMPM(props.end)}
                    </p>
                    <p className="review-details">
                        <img src="/img/message.svg" alt="message-logo" className="message-logo" />
                        <img src="/img/black-star.svg" alt="black-star" className="black-star" />
                        {props.reviews} Reviews
                    </p>
                    <p className="type">
                        <img src="/img/knife.svg" alt="knife-img" className="knife-logo" />
                        {props.type}
                    </p>
                    </div>
                    
                    <p className="location-part d-flex align-items-center">
                        <img src="/img/gray-location.svg" alt="location-logo" className="location-logo me-1" />
                        {props.country},  {props.city}, {props.street}
                    </p>
                    <div className="description">
                        <p>{props.description}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default RestaurantProfile;