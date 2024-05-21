//import { HashRouter as Router, Route, Routes } from "react-router-dom";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import React, { createContext, useEffect } from "react";
import './App.css';
import UserInfo from "./utility/userInfo";
import Navbar from "./components/Navbar";
import Login from "./components/Login";
import Footer from "./components/Footer";
import Protected from "./components/Protected";
import Home from "./components/Home";
import SearchResult from "./components/SearchResult";
import Restaurant from "./components/Restaurant";
import ProtectedClient from "./components/ProtectedClient";
import Customer from "./components/Customer";
import Callback from "./components/Callback"
export const UserContext = createContext({});

function App() {

  const user = UserInfo();
  // useEffect(() => {
  //   localStorage.setItem("user", JSON.stringify(user));
  //   console.log("local_storage", localStorage);
  //   console.log("first", user);
  // }, [user]);

  

  // console.log(user);
  return (
    <div>
      <Router>
        <UserContext.Provider value={{ ...user }}>
          <Navbar />
          <Routes>
            <Route path={"/callback"} element={<Callback />} />
            <Route path="/Login" element={<Login />} />
            <Route path="/" element={<Protected isLoggedIn={user.loggedIn}><Home /></Protected>} />
            <Route path="/searchResualt" element={<Protected isLoggedIn={user.loggedIn}><SearchResult /></Protected>} />
            <Route path="/restaurant/:id" element={<Protected isLoggedIn={user.loggedIn}><Restaurant /></Protected>} />
            <Route path="/reservations" element={<Protected isLoggedIn={user.loggedIn}><ProtectedClient role={user.role}><Customer /></ProtectedClient></Protected>} />
          </Routes>
          <Footer />
        </UserContext.Provider>
      </Router>
    </div>
  );
}
export default App;
