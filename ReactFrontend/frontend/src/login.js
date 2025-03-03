import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {Button, Form, Input} from "antd";
import "./styles/Login.css"
import {MicroserviceType} from "./global_manager/BaseUrl";
import {MicroserviceUtils} from "./global_manager/BaseUrl";
const Login = (props) => {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [usernameError, setUsernameError] = useState("")
    const [passwordError, setPasswordError] = useState("")

    const navigate = useNavigate();
    const userServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.User);

    const onButtonClick = () => {

        setUsernameError("")
        setPasswordError("")

        if ("" === username) {
            setUsernameError("Please enter your username")
            return
        }

        if ("" === password) {
            setPasswordError("Please enter a password")
            return
        }


        checkAccountExists(accountExists => {
            if (accountExists)
                logIn()
            else
            if (window.confirm("An account does not exist with this username address: " + username + ". Do you want to create a new account?")) {
                logIn()
            }
        })


    }

    const checkAccountExists = (callback) => {
        fetch(userServiceUrl+"/auth/check-account", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({username})
        })
            .then(r => r.json())
            .then(r => {
                callback(r?.userExists)
            })
    }

    const logIn = () => {
        fetch(userServiceUrl+"/auth/login", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({username, password})
        })
            .then(r => r.json())
            .then(r => {
                if (r.token && r.person) {
                    const { token, person } = r;

                    // Save the token in localStorage
                    localStorage.setItem("jwtToken", token);
                    localStorage.setItem("user", JSON.stringify(person));

                    props.setLoggedIn(true);
                    props.setUsername(username);

                    if (person.isAdmin) {
                        navigate("/admin");
                    } else {
                        navigate("/user");
                    }
                } else {
                    window.alert("Wrong username or password");
                }
            })
            .catch(error => {
                console.error("Login failed:", error);
                window.alert("An error occurred during login.");
            });
    }

    return <div className={"mainContainer"}>
        <div className="transparent-container">
            <Form
                className="loginForm"
                name="loginForm"
                layout="vertical"
                onFinish={onButtonClick}
                style={{ maxWidth: 400, margin: "0 auto" }}
            >
                <div className="titleContainer">
                    Login
                </div>

                <Form.Item
                    label="Username"
                    name="username"
                    validateStatus={usernameError ? "error" : ""}
                    help={usernameError}
                    style={{ marginBottom: 16 }}
                >
                    <Input
                        value={username}
                        placeholder="Enter your username"
                        onChange={ev => setUsername(ev.target.value)}
                    />
                </Form.Item>

                <Form.Item
                    label="Password"
                    name="password"
                    validateStatus={passwordError ? "error" : ""}
                    help={passwordError}
                    style={{ marginBottom: 16 }}
                >
                    <Input.Password
                        value={password}
                        placeholder="Enter your password"
                        onChange={ev => setPassword(ev.target.value)}
                    />
                </Form.Item>

                <Form.Item style={{ textAlign:"center" }}>
                    <Button
                        type="primary"
                        htmlType="submit"
                        style={{ width: "70%" }}
                    >
                        Log in
                    </Button>
                </Form.Item>
            </Form>
        </div>
    </div>
}

export default Login