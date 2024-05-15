import { useState } from "react";

function UserInfo() {
    const [username, setusername] = useState("");
    const [loggedIn, setLoggedIn] = useState(false);
    const [role, setRole] = useState("");
    const [email, setemail] = useState("");
    const [address, setAddress] = useState("");
    
    const SetAllInfo = (user) => {
        console.log(user);
        setLoggedIn(true);
        setusername(user.username);
        setRole(user.role);
        setemail(user.email);
        setAddress(user.address)
    };

    return {
        username,
        setusername, 
        SetAllInfo,
        loggedIn,
        setLoggedIn,
        role,
        email,
        setAddress,
        address
    };
};

export default UserInfo;
