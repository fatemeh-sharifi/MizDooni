import { HashRouter as Router, Route, Routes } from "react-router-dom";
import React, { createContext } from "react";
import './App.css';
import UserInfo from "./utility/userInfo";
import Navbar from "./Navbar";

export const UserContext = createContext({});

function App() {

  const user = UserInfo();
  return (
    <div>
      <Router>
      <UserContext.Provider value={{ ...user }}>
        <Navbar />
        {/* <Routes>
          <Route
            path="/Profile"
            element={
              <Protected isLoggedIn={user.loggedIn}>
                <Profile />
              </Protected>
            }
          />
          <Route path="/Provider/:id" element={<Protected isLoggedIn={user.loggedIn}><Provider /></Protected>} />
          <Route path="/Register" element={<Register />} />
          <Route path="/Login" element={<Login />} />
          <Route
            path="/Products/:id"
            element={
              <Protected isLoggedIn={user.loggedIn}>
                <Product />
              </Protected>
            }
          />
          <Route path="/" element={<Protected isLoggedIn={user.loggedIn}><Baloot /></Protected>} />
        </Routes> */}
        </UserContext.Provider>
      </Router>
    </div>
  );
}

export default App;