import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Login from './login'
import './App.css'
import { useState } from 'react'
import Admin from "./Admin/Admin";
import UsersDash from "./Admin/user/Users";
import DevicesDash from "./Admin/device/Devices";
import User from "./user/User";
import PrivateRoute from './PrivateRoute';
import AdminChat from "./Admin/chat/AdminChat";
import UserChat from "./user/UserChat";

function App() {
    const [loggedIn, setLoggedIn] = useState(false)
    const [username, setUsername] = useState('')

    return (
        <div className="App">
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Login setLoggedIn={setLoggedIn} setUsername={setUsername} />} />

                    {/* Admin routes */}
                    <Route path="/admin" element={
                        <PrivateRoute allowedRoles={['admin']} element={<Admin />} />
                    } />
                    <Route path="/admin/users" element={
                        <PrivateRoute allowedRoles={['admin']} element={<UsersDash />} />
                    } />
                    <Route path="/admin/devices" element={
                        <PrivateRoute allowedRoles={['admin']} element={<DevicesDash />} />
                    } />
                    <Route path="/admin/chat" element={
                        <PrivateRoute allowedRoles={['admin']} element={<AdminChat />} />
                    } />
                    {/* User route */}
                    <Route path="/user" element={
                        <PrivateRoute allowedRoles={['user']} element={<User />} />
                    } />
                    <Route path="/user/chat" element={
                        <PrivateRoute allowedRoles={['user']} element={<UserChat />} />
                    } />
                </Routes>
            </BrowserRouter>
        </div>
    )
}

export default App;


