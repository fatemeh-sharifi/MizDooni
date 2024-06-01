// import { useState } from "react";

// function UserInfo() {
//     const [username, setusername] = useState("");
//     const [loggedIn, setLoggedIn] = useState(false);
//     const [role, setRole] = useState("");
//     const [email, setemail] = useState("");
//     const [address, setAddress] = useState("");
    
//     const SetAllInfo = (user) => {
//         console.log(user);
//         setLoggedIn(true);
//         setusername(user.username);
//         setRole(user.role);
//         setemail(user.email);
//         setAddress(user.address)
//     };

//     return {
//         username,
//         setusername, 
//         SetAllInfo,
//         loggedIn,
//         setLoggedIn,
//         role,
//         email,
//         setAddress,
//         address
//     };
// };

// export default UserInfo;
import { useState, useEffect } from "react";

const LOCAL_STORAGE_KEY = 'userInfo';

function UserInfo() {
    // Initialize state with default values or values from localStorage
    const [username, setUsername] = useState(() => {
        const savedUser = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY));
        return savedUser?.username || "";
    });
    const [loggedIn, setLoggedIn] = useState(() => {
        const savedUser = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY));
        return savedUser?.loggedIn || false;
    });
    const [role, setRole] = useState(() => {
        const savedUser = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY));
        return savedUser?.role || "";
    });
    const [email, setEmail] = useState(() => {
        const savedUser = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY));
        return savedUser?.email || "";
    });
    const [address, setAddress] = useState(() => {
        const savedUser = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY));
        return savedUser?.address || "";
    });

    // Function to update all user info and save it to localStorage
    const SetAllInfo = (user) => {
        console.log(user);
        setLoggedIn(true);
        setUsername(user.username);
        setRole(user.role);
        setEmail(user.email);
        setAddress(user.address);

        // Save user info to localStorage
        localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify({
            username: user.username,
            loggedIn: true,
            role: user.role,
            email: user.email,
            address: user.address
        }));
    };

    // Optional: Use useEffect to save the state to localStorage whenever it changes
    useEffect(() => {
        const userInfo = { username, loggedIn, role, email, address };
        localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(userInfo));
    }, [username, loggedIn, role, email, address]);

    return {
        username,
        setUsername, 
        SetAllInfo,
        loggedIn,
        setLoggedIn,
        role,
        email,
        setEmail,
        address,
        setAddress
    };
};

export default UserInfo;