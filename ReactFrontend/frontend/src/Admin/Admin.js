import {Menu, Space,} from "antd";
import {LogoutOutlined} from "@ant-design/icons";
import {Link, } from "react-router-dom";
import "../styles/Header.css"
import "../styles/Admin.css"

import { useNavigate } from "react-router-dom";


export default function Admin() {
    const navigate = useNavigate();
    const handleLogout = () => {
        localStorage.removeItem("jwtToken");
        localStorage.removeItem("user");
        navigate('/');
    };
    const menuItems = [
        {
            label: <Link to="/admin" style={{ color: 'white' }} > Home</Link>,
            key: "home",
        },
        {
            label: <Link to="/admin/users" style={{ color: 'white' }} > Users</Link>,
            key: "dashboardUsers",
        },
        {
            label: <Link to="/admin/devices" style={{ color: 'white' }}> Devices</Link>,
            key: "dashboardDev",
        },
        {
            label: <Link to="/admin/chat" style={{ color: 'white' }}> Chat</Link>,
            key:"chat",
        },
        {
            label: (
                <span onClick={handleLogout} style={{ cursor: 'pointer', color: 'white' }}>
                    <Space>
                        <LogoutOutlined /> Log out
                    </Space>
                </span>
            ),
            key: "profile",
        },
    ];

    return (
<div className="all-container">
    <div className="menu-container">
        <Menu mode="horizontal" style={{ flex: 1 }}>
            <div className="menu-left">
                {menuItems.slice(0, 4).map(item => (
                    <Menu.Item key={item.key}>
                        {item.label}
                    </Menu.Item>
                ))}
            </div>
            <div className="menu-right">
                {menuItems.slice(4).map(item => (
                    <Menu.Item key={item.key}>
                        {item.label}
                    </Menu.Item>
                ))}
            </div>
        </Menu>
    </div>
</div>


    );
}
