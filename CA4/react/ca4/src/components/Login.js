import React, { useContext, useState } from 'react';
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom"
import "../css/Login.css"

function Login() {

    const [justifyActive, setJustifyActive] = useState('tab1');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [city, setCity] = useState('');
    const [country, setCountry] = useState('');
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
        axios.post("//localhost:8080/login", params).then(
            (response) => {
                if (response.status === 200) {
                    axios.get("//localhost:8080/users/" + String(username)).then(
                        (response) => {
                            UserInfo.SetAllInfo(response.data);
                            navigate("/")
                        },
                        (error) => {
                            Swal.fire({
                                icon: error,
                                title: error.response.data.message.split(":")[1],
                                text: "Sign in failed! Please try again.",
                            })
                        }
                    )
                }
            },
            (error) => {
                Swal.fire({
                    icon: error,
                    title: error.response.data.message.split(":")[1],
                    text: "Sign in failed! Please try again.",
                });
            }
        )
    }

    function handleSignUp(event) {
        event.preventDefault();
        const params = { username: username, password: password, email: email, city: city, country: country };
        axios.post("//localhost:8080/signup", params).then(
            (response) => {
                if (response.status === 200) {
                    UserInfo.SetAllInfo(response.data);
                    navigate("/")
                }
            },
            (error) => {
                Swal.fire({
                    icon: error,
                    title: error.response.data.message.split(":")[1],
                    text: "Please try again.",
                });
            }
        )
    }

    return (
        <div className="container p-3  d-flex flex-column ">
            <ul className="nav nav-pills justify-content-between mb-3 ">
                <li className="nav-item col-6">
                    <button className={`nav-link w-100 ${justifyActive === 'tab1' ? 'active bg-danger' : 'bg-light text-secondary'}`} onClick={() => handleJustifyClick('tab1')} >
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
                    <input className="form-control mb-4" type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
                    <input className="form-control mb-4" type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                    <button className="btn btn-primary mb-4 w-100 bg-danger border-0" onClick={handleSignIn}>Sign in</button>
                </div>
                <div className={`tab-pane fade ${justifyActive === 'tab2' ? 'show active' : ''}`}>
                    <input className="form-control mb-4" type="text" placeholder="Username" onChange={(e) => setUsername(e.target.value)} />
                    <input className="form-control mb-4" type="email" placeholder="Email" onChange={(e) => setEmail(e.target.value)} />
                    <input className="form-control mb-4" type="text" placeholder="Country" onChange={(e) => setCountry(e.target.value)} />
                    <input className="form-control mb-4" type="text" placeholder="City" onChange={(e) => setCity(e.target.value)} />
                    <input className="form-control mb-4" type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} />
                    <div className="d-flex justify-content-around mb-4">
                        <p className="me-2">I'm a new</p>
                        <div className="form-check form-check-inline">
                            <input className="form-check-input" type="radio" name="userType" id="manager" value="manager" />
                            <label className="form-check-label" htmlFor="manager">Manager</label>
                        </div>
                        <div className="form-check form-check-inline">
                            <input className="form-check-input" type="radio" name="userType" id="customer" value="customer" defaultChecked />
                            <label className="form-check-label" htmlFor="customer">Customer</label>
                        </div>
                    </div>
                    <button className="btn btn-primary mb-4 w-100 bg-danger border-0" onClick={handleSignUp}>Sign up</button>
                </div>
            </div>
        </div>
    );
}

export default Login;