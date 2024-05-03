import React ,{ useContext, useEffect, useState } from "react";


function Rating(props) {
    const [hover, setHover] = useState(null);
    const [totalStars, setTotalStars] = useState(5);
    return (
        <div className="d-flex justify-content-between">
            <p>props.name</p>
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
    );
}

export default Rating;