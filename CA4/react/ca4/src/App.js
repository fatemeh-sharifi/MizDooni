import { HashRouter as Router, Route, Routes } from "react-router-dom";
import React, { createContext } from "react";
import './App.css';
import UserInfo from "./utility/userInfo";
import Navbar from "./components/Navbar";
import Login from "./components/Login";
import Footer from "./components/Footer";

export const UserContext = createContext({});

function App() {

  const user = UserInfo();
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
          <Route path="/" element={<Login />} />
          {/* <Route
            path="/Products/:id"
            element={
              <Protected isLoggedIn={user.loggedIn}>
                <Product />
              </Protected>
            }
          /> */}
          {/* <Route path="/" element={<Protected isLoggedIn={user.loggedIn}><Baloot /></Protected>} /> */}

        </Routes>
        <Footer/>
        </UserContext.Provider>
      </Router>
    </div>
  );
}

export default App;