import {Menu, Space} from "antd";
import React from "react";
import {LogoutOutlined} from "@ant-design/icons";
import {Link, useNavigate} from "react-router-dom";

export default function UserNavBar() {
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem('user'));
    const userName = user?.username || "User";

    const handleLogout = () => {
        localStorage.removeItem("jwtToken");
        localStorage.removeItem("user");
        navigate('/');
    };

    const menuItems = [{
        label: (
            <span style={{ cursor: 'default', color: 'white', marginRight: 'auto', fontWeight:'bold' , fontSize: '22px'}}> {/* Left-align with marginRight: 'auto' */}
                Hello, {userName}
                </span>
        ),
        key: "greeting",
        },
        {
            label: <Link to="/user/chat" style={{ color: 'white' }}> Chat</Link>,
            key:"chat",
        },
        { label: (
                <span onClick={handleLogout} style={{ cursor: 'pointer', color: 'white' }}> {/* Change Link to span */}
                    <Space>
                    <LogoutOutlined /> Log out
                </Space>
            </span>
            ),
            key: "profile"
        },
    ];

    return( <div className="menu-container">
        <Menu mode="horizontal" style={{ flex: 1 }}>

                <div className="menu-left">
                    {menuItems.slice(0, 2).map(item => (
                        <Menu.Item key={item.key}>
                            {item.label}
                        </Menu.Item>
                    ))}
                </div>
            <div className="menu-right">
                <Menu.Item key={menuItems[2].key}>
                    {menuItems[2].label}
                </Menu.Item>
            </div>

        </Menu>
    </div>)
}