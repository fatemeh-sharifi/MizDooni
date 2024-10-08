import { HashRouter as Router, Route, Routes } from "react-router-dom";
import React, { createContext } from "react";
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
export const UserContext = createContext({});

function App() {

  const user = UserInfo();
  console.log(user);
  return (
    <div>
      <Router>
      <UserContext.Provider value={{ ...user }}>
        <Navbar />
        <Routes>
          {/* <Route
            path="/Profile"
            element={
              <Protected isLoggedIn={user.loggedIn}>
                <Profile />
              </Protected>
            }
          /> */}
          {/* <Route path="/Provider/:id" element={<Protected isLoggedIn={user.loggedIn}><Provider /></Protected>} /> */}
          {/* <Route path="/Register" element={<Register />} /> */}
          <Route path="/Login" element={<Login />} />
          {/* <Route
            path="/Products/:id"
            element={
              <Protected isLoggedIn={user.loggedIn}>
                <Product />
              </Protected>
            }
          /> */}
          <Route path="/" element={<Protected isLoggedIn={user.loggedIn}><Home /></Protected>} />
          <Route path="/searchResualt" element={<Protected isLoggedIn={user.loggedIn}><SearchResult /></Protected>} />
          <Route path="/restaurant/:id" element={<Protected isLoggedIn={user.loggedIn}><Restaurant /></Protected>} />
          <Route path="/reservations" element={<Protected isLoggedIn={user.loggedIn}><ProtectedClient role={user.role}><Customer /></ProtectedClient></Protected>} />
        </Routes>
        <Footer/>
        </UserContext.Provider>
      </Router>
    </div>
  );
}

export default App;