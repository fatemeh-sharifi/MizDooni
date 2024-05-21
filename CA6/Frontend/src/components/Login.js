import React, { useContext, useState } from 'react';
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import "../css/Login.css";

function Login() {
    const [justifyActive, setJustifyActive] = useState('tab1');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [city, setCity] = useState('');
    const [country, setCountry] = useState('');
    const [role, setRole] = useState('');
    const navigate = useNavigate();
    const UserInfo = useContext(UserContext);

    const handleJustifyClick = (value) => {
        if (value === justifyActive) {
            return;
        }
        setJustifyActive(value);
    };

    function handleSignIn(event) {
        event.preventDefault();
        const params = { username: username, password: password };
        axios.post("http://localhost:8080/login", null, { params: params }).then(
            (response) => {
                if (response.status === 200) {
                    const token = response.data.token; // Assuming the token is returned in response.data.token
                    console.log(response.data.token)
                    localStorage.setItem('jwtToken', token);
                    UserInfo.SetAllInfo(response.data.user);
                    navigate("/")
                    // axios.get("http://localhost:8080/users/" + String(username), {
                    //     headers: {
                    //         'Authorization': `Bearer ${token}`
                    //     }
                    // }).then(
                    //     (response) => {
                    //         // console.log(response.data.user)
                            
                    //     },
                    //     (error) => {
                    //         Swal.fire({
                    //             icon: 'error',
                    //             title: error.response.data.message.split(":")[1],
                    //             text: "Sign in failed! Please try again.",
                    //         });
                    //     }
                    // );
                }
            },
            (error) => {
                if (error.response.status === 401) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Unauthorized',
                        text: "Invalid username or password. Please try again.",
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: "Sign in failed! Please try again later.",
                    });
                }
            }
        ).catch((error) => {
            console.log(error);
        });
    }

    function handleSignUp(event) {
        event.preventDefault();
        const params = { username: username, password: password, email: email, city: city, country: country, role: role };
        axios.post("http://localhost:8080/signup", null, { params: params }).then(
            (response) => {
                if (response.status === 200) {
                    console.log("signup : ", response.data);
                    const token = response.data.token; // Assuming the token is returned in response.data.token
                    localStorage.setItem('jwtToken', token);
                    UserInfo.SetAllInfo(response.data);
                    navigate("/");
                }
            },
            (error) => {
                Swal.fire({
                    icon: 'error',
                    title: error.response.data.message.split(":")[1],
                    text: "Please try again.",
                });
            }
        );
    }

    return (
        <div className="container p-3 d-flex flex-column">
            <ul className="nav nav-pills justify-content-between mb-3">
                <li className="nav-item col-6">
                    <button className={`nav-link w-100 ${justifyActive === 'tab1' ? 'active bg-danger' : 'bg-light text-secondary'}`} onClick={() => handleJustifyClick('tab1')}>
                        Login
                    </button>
                </li>
                <li className="nav-item col-6">
                    <button className={`nav-link w-100 ${justifyActive === 'tab2' ? 'active bg-danger' : 'bg-light text-secondary'}`} onClick={() => handleJustifyClick('tab2')}>
                        Register
                    </button>
                </li>
            </ul>
            <div className="tab-content">
                <div className={`tab-pane fade ${justifyActive === 'tab1' ? 'show active' : ''}`}>
                    <input className="form-control w-100 mb-4" type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
                    <input className="form-control w-100 mb-4" type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                    <button className="btn btn-primary mb-4 w-100 bg-danger border-0" onClick={handleSignIn}>Sign in</button>
                </div>
                <div className={`tab-pane fade ${justifyActive === 'tab2' ? 'show active' : ''}`}>
                    <input className="form-control w-100 mb-4" type="text" placeholder="Username" onChange={(e) => setUsername(e.target.value)} />
                    <input className="form-control w-100 mb-4" type="email" placeholder="Email" onChange={(e) => setEmail(e.target.value)} />
                    <input className="form-control w-100 mb-4" type="text" placeholder="Country" onChange={(e) => setCountry(e.target.value)} />
                    <input className="form-control w-100 mb-4" type="text" placeholder="City" onChange={(e) => setCity(e.target.value)} />
                    <input className="form-control w-100 mb-4" type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} />
                    <div className="d-flex justify-content-around mb-4">
                        <p className="me-2">I'm a new</p>
                        <div className="form-check">
                            <input className="form-check-input" type="checkbox" name="userType" id="manager" defaultChecked={role === 'manager'} onClick={() => { setRole('manager') }} />
                            <label className="form-check-label" htmlFor="manager">
                                Manager
                            </label>
                        </div>
                        <div className="form-check">
                            <input className="form-check-input" type="checkbox" name="userType" id="client" defaultChecked={role === 'client'} onClick={() => { setRole('client') }} />
                            <label className="form-check-label" htmlFor="client">
                                Client
                            </label>
                        </div>
                    </div>
                    <button className="btn btn-primary mb-4 w-100 bg-danger border-0" onClick={handleSignUp}>Sign up</button>
                </div>
            </div>
            <a href={`https://accounts.google.com/o/oauth2/auth?client_id=318976450429-mlodo6eolob4l5a2mfnb33l6pr8gl3h0.apps.googleusercontent.com&redirect_uri=http://localhost:3000/callback&response_type=code&scope=email%20profile`}>
                <img src="https://developers.google.com/identity/images/btn_google_signin_dark_normal_web.png" alt="Google sign-in" />
            </a>
        </div>
    );
}

export default Login;
