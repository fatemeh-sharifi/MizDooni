import React, { useEffect, useContext } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { UserContext } from "../App";


function Callback() {
    console.log("I AM HERE!")
    const navigate = useNavigate();
    const UserInfo = useContext(UserContext);
    useEffect(() => {
        console.log("callback!")
        const fetchData = async () => {
            let search = window.location.search;
            let params = new URLSearchParams(search.split('?')[1]);
            console.log('params:', params);

            if (localStorage.getItem("jwtToken") != null) {
                navigate('/');
                return; ////
            }

            const codeParam = { code: params.get('code') };

            console.log("before try", codeParam)

            try {
                console.log("in the try")
                const response = await axios.get("/callback", {params: codeParam});
                const token = response.data.token; // Assuming the token is returned in response.data.token
                console.log(response.data.token)
                localStorage.setItem('jwtToken', token);
                UserInfo.SetAllInfo(response.data.user);
                console.log('Response:', response);
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