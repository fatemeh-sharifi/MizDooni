import React from 'react'
import { Navigate } from 'react-router-dom'
function Protected({ isLoggedIn, children }) {
    if (!localStorage.getItem("jwtToken")) {
        return <Navigate to="/Login" replace />
    }
    return children
}
export default Protected