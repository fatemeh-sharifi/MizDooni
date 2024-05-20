import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../App";


function Callback() {
    const navigate = useNavigate();
    const UserInfo = useContext(UserContext);
    useEffect(() => {
        console.log("callback!")
        const fetchData = async () => {
            let search = window.location.search;
            let params = new URLSearchParams(search.split('?')[1]);

            console.log('params:', params);

            if (localStorage.getItem("userJWT") != null) {
                navigate('/');
            }

            const codeParam = { code: params.get('code') };

            try {
                const response = await axios.get("/callback", {params: codeParam});
                console.log('Response:', response);

                let userJWT = response.headers.token;
                let username = response.headers.username;
                // Authentication.setUser(userJWT, username);
                UserInfo.setusername(username);
                localStorage.setItem("userJWT", userJWT);
                navigate('/');
            } catch (error) {
                console.error(error);
            }
        };

        fetchData();
    }, []);

    return null;
}

export default Callback;