import React ,{useEffect} from "react";
import { useLocation } from "react-router-dom";
import { UserContext } from "../App";
import Card from "./Card";

function SearchResult() {
    const locationUse = useLocation();
    console.log("locationUse:", locationUse);
    const { state: { data: searchData = {}, name: paramName = '', type: paramType = '', city: paramLocation = '' } = {} } = locationUse;
    console.log("serach data : ", searchData);
    const UserInfo = useContext(UserContext);
    function createCard(restaurant, index) {
        return (
            <Card
                key={index}
                id={restaurant.id}
                rating={restaurant.overallAvg}
                start={restaurant.startTime}
                end={restaurant.endTime}
                img={restaurant.image}
                title={restaurant.name}
                reviews={restaurant.feedbacks.length}
                type={restaurant.type}
                city={restaurant.address.city}
            />
        );
    }
    // useEffect(() => {
    //     const token = localStorage.getItem('jwtToken');
    //     if (token) {
    //         fetchUserData(token);
    //     }
    // }, []);
    // function fetchUserData(token) {
    //     axios.get('http://localhost:8080/user',
    //     {
    //         withCredentials: false,
    //         headers: {
    //             'Authorization': `Bearer ${token}`
    //         },
    //     }).then(
    //         (response) => {
    //             if (response.status === 200) {
    //                 const userData = response.data;
    //                 UserInfo.setAllInfo(userData);
    //             }
    //         },
    //         (error) => {
    //             Swal.fire({
    //                 icon: 'error',
    //                 title: 'Error',
    //                 text: error.response ? error.response.data.message : 'An error occurred. Please try again.',
    //             });
    //         }
    //     );
    // }

    return (
        <div>
            <div className="container w-75 topRestaurant">
                <p className="title1">
                    Results for {' '}
                    {paramName && paramName}
                    {paramName && (paramType || paramLocation) && ','}
                    {' '}
                    {paramType && paramType}
                    {paramType && paramLocation && ','}
                    {' '}
                    {paramLocation && paramLocation}
                </p>
                <div className="row justify-content-start">
                    {searchData && searchData.map(createCard)}
                    {searchData.length === 0 && <p className="searchRes text-center ">Nothing is found.</p>}
                </div>
            </div>
        </div>
    );
}


export default SearchResult;