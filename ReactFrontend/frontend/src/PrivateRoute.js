import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ element: Component, allowedRoles }) => {
    const user = JSON.parse(localStorage.getItem("user"));
    const token = localStorage.getItem("jwtToken");

    if (!user || !token) {
        return <Navigate to="/" replace />;
    }

    const role = user.isAdmin ? 'admin' : 'user';

    if (!allowedRoles.includes(role)) {
        return <Navigate to="/" replace />;
    }

    return Component;
};


export default PrivateRoute;
