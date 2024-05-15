import React from 'react'
import { Navigate } from 'react-router-dom'
function ProtectedClient({ role, children }) {
    if (role !=="client") {
        return <Navigate to="/" replace />
    }
    return children
}
export default ProtectedClient;