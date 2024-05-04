import React from "react";

function ReviewPart(props) {

    function getProfile() {
        if (props.username.includes('_')) {
            const [firstName, lastName] = props.username.split('_');
            return `${firstName.charAt(0).toUpperCase()}${lastName ? lastName.charAt(0).toUpperCase() : ''}`;
        } else {
            return `${props.username.charAt(0).toUpperCase()}`;
        }
    }

    function formatDate(date) {
        const months = [
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
        ];

        const day = date.getDate();
        const monthIndex = date.getMonth();
        const year = date.getFullYear();

        return `${months[monthIndex]} ${day}, ${year}`;
    }

    function renderStars(rating) {
        const stars = [];
        const filledStars = Math.floor(rating);
        const emptyStars = 5 - filledStars;
        for (let i = 0; i < filledStars; i++) {
            stars.push(<img key={i} src="img/full-star.svg" alt="star-img" />);
        }
        for (let i = 0; i < emptyStars; i++) {
            stars.push(<img key={filledStars + i} src="img/empty-star.svg" alt="empty-star-img" />);
        }

        return stars;
    }

    return (
        <div>
            <div className="one-reviews d-flex justify-content-between">
                <div className="right-part d-flex gap-3">
                    <div className="profile text-center flex-shrink-0">
                        <p>{getProfile()}</p>
                    </div>
                    <div className="comment d-flex flex-column">
                        <p className="username">{props.username}</p>
                        <p className="user-rating">
                            Overall <span>{props.overallRate}</span>
                            <img src="img/dot.svg" alt="dot-img" className="dot" />
                            Food <span>{props.foodRate}</span>
                            <img src="img/dot.svg" alt="dot-img" className="dot" />
                            Service <span>{props.serviceRate}</span>
                            <img src="img/dot.svg" alt="dot-img" className="dot" />
                            Ambience <span>{props.ambianceRate}</span>
                        </p>
                        <p className="comment-text">{props.comment}</p>
                    </div>
                </div>
                <div className="date-star d-flex flex-wrap justify-content-end">
                    <div className="stars flex-shrink-0">
                        {renderStars(props.overallRate)}
                    </div>
                    <p className="comment-date flex-shrink-0">
                        {props.dateTime ? (
                            "Dined on " + String(props.dateTime)
                        ) :(
                            "Dined on " + formatDate(new Date())
                        )}
                    </p>
                </div>
            </div>
            <hr />
        </div>
    );
}

export default ReviewPart;