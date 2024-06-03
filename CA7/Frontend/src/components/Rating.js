import React ,{ useContext, useEffect, useState } from "react";


function Rating(props) {
    const [hover, setHover] = useState(null);
    const [totalStars, setTotalStars] = useState(5);
    return (
        <div className="d-flex align-items-center justify-content-between">
            <p className="mb-0">{props.name}</p>
            <div>
            {[...Array(totalStars)].map((star, index) => {
                const currentRating = index + 1;
                return (
                    <label key={index}>
                        <input
                            key={star}
                            type="radio"
                            name="rating"
                            value={currentRating}
                            onChange={() => props.setRating(currentRating)}
                        />
                        <span
                            className="star"
                            style={{
                                color:
                                    currentRating <= (hover || props.rating) ? "#ED3545" : "#e4e5e9",
                                borderColor: "#ED3545",
                            }}
                            onMouseEnter={() => setHover(currentRating)}
                            onMouseLeave={() => setHover(null)}
                        >
                            &#9733;
                        </span>
                    </label>
                );
            })}
            </div>
        </div>
    );
}

export default Rating;