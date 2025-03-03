import {Modal, Calendar, Button, Table, Menu, message, Space, Tooltip} from "antd";
import {LineChartOutlined, LogoutOutlined,} from "@ant-design/icons";
import { useNavigate,} from "react-router-dom";
import React, {useEffect, useState} from "react";
import SockJS from "sockjs-client";
import { Client } from '@stomp/stompjs';
import "../styles/Table.css";
import "../styles/Header.css"
import { LineChart } from '@mui/x-charts/LineChart';
import {MicroserviceType} from "../global_manager/BaseUrl";
import {MicroserviceUtils} from "../global_manager/BaseUrl";
import UserNavBar from "./UserNavBar";

export default function User() {
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem('user'));
    const [devices, setDevices] = useState([]);
    const [deviceCache, setDeviceCache] = useState({});
    const [selectedDevice, setSelectedDevice] = useState(null);
    const [selectedDate, setSelectedDate] = useState(null);
    const [chartData, setChartData] = useState([]);
    const [isNoData, setIsNoData] = useState(false);

    const [isModalVisible, setIsModalVisible] = useState(false);

    const deviceServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.Device);
    const monitorServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.Monitor);




    useEffect(() => {
        if (!user) {
            message.error("User is not available.");
            return;
        }

        if (deviceCache[user.id]) {
            setDevices(deviceCache[user.id]);
            return;
        }

        const fetchDevices = async () => {
            const token = localStorage.getItem("jwtToken");
            console.log("Token being sent for user:", token);
            try {
                const response = await fetch(`${deviceServiceUrl}/device/person/${user.id}`, {
                    method: "GET",
                    headers: { "Content-Type": "application/json" ,
                        "Authorization": `Bearer ${token}`,
                    }});
                const data = await response.json();

                if (Array.isArray(data)) {
                    setDevices(data);
                    setDeviceCache(prevCache => ({ ...prevCache, [user.id]: data }));
                } else {
                    setDevices([]);
                    message.error("Invalid data format");
                }
            } catch (err) {
                message.error("Error fetching devices");
            }
        };

        fetchDevices();
    }, [user, deviceCache]);


    useEffect(() => {

        const socket = new SockJS(monitorServiceUrl+"/ws");
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                client.subscribe(`/topic/user-${user.id}`, (notification) => {
                    const notificationMessage = JSON.parse(notification.body);

                    const formattedMessage = notificationMessage.message.split('\n').map((line, index) => (
                        <div key={index} >{line}</div>
                    ));
                    message.info(<div>{formattedMessage}</div>);

                });
            },
            onStompError: (error) => {
                console.error("STOMP error:", error);
            },
        });
        client.activate();
        return () => {
            client.deactivate();
        };
    }, [user]);

    //for chart
    const fetchEnergyData = async (deviceId, date) => {
        if (!deviceId || !date) return;

        try {
            const response = await fetch(
                `${monitorServiceUrl}/measurement/consumption?deviceId=${deviceId}&date=${date}`
            );

            const data = await response.json();
            if (data && Array.isArray(data)) {
                // Check if all measurementValue for the day are 0
                const nonZeroData = data.filter(entry => entry.measurementValue !== 0);

                // Check if all entries have been filtered out (i.e., no valid data left)
                if (nonZeroData.length === 0) {
                    setIsNoData(true); // Show "No data available" message
                } else {
                    setIsNoData(false);
                    // Transform non-zero data for the chart
                    const transformedData = nonZeroData.map(entry => ({
                        hour: new Date(entry.timestamp).toLocaleTimeString('en-GB', { timeZone: 'UTC', hour: '2-digit', minute: '2-digit' }),
                        consumption: entry.measurementValue,
                    }));
                    setChartData(transformedData); // Save only non-zero data
                    console.log(chartData);

                }
            }
        } catch (err) {
            message.error("Error fetching energy data");
        }
    };



    const handleDateSelect = (date) => {
        const formattedDate = date.format("YYYY-MM-DD");
        setSelectedDate(formattedDate);
        fetchEnergyData(selectedDevice, formattedDate);
    };

    const openModal = (deviceId) => {
        setSelectedDevice(deviceId);
        setIsModalVisible(true);
    };

    const closeModal = () => {
        setIsModalVisible(false);
        setSelectedDevice(null);
        setSelectedDate(null);
        setChartData([]);
    };




    const columns = [
        { title: "#ID", dataIndex: "id", key: "id", align: "center" },
        { title: "Device", dataIndex: "name", key: "name", align: "center" },
        { title: "Description", dataIndex: "description", key: "description", align: "center" },
        { title: "Address", dataIndex: "address", key: "address", align: "center" },
        { title: "MHC", dataIndex: "mhc", key: "mhc", align: "center"},
        {
            title: "Actions",
            key: "actions",
            align: "center",
            render: (_, record) => (
                <Tooltip title ="View Consumption">
                    <Button type="primary" onClick={() => openModal(record.id)} >
                        <LineChartOutlined />
                    </Button>
                </Tooltip>
            ),
        },
    ];

    return (
        <>
            <UserNavBar/>
            <div className={"my-devices-header"}>Devices </div>

            <div className="table-container">
                <Table
                    columns={columns}
                    dataSource={devices || []}
                />
            </div>

            <Modal
                centered
                title="Select a Day to View Energy Consumption"
                visible={isModalVisible}
                onCancel={closeModal}
                footer={null}
            >
                <Calendar fullscreen={false} onSelect={handleDateSelect} />
                    <div className="chart-container">
                        <h3>Energy Consumption for {selectedDate}</h3>
                        <div style={{ width: "90%", height: "400px", margin: "0 auto" }}>
                            <div>
                                {isNoData ? (
                                    <p>No data available for the selected date.</p>
                                ) : (
                                    <div>
                                        {isNoData ? (
                                            <p>No data available for the selected date.</p>
                                        ) : (
                                            (() => {
                                                // Map hours to numbers for X-axis
                                                const hourToNumber = hourString => {
                                                    const [hours, minutes] = hourString.split(':').map(Number);
                                                    return hours; // Just return the hour part (as a number)
                                                };

                                                // Transform `chartData` for the chart
                                                const xAxisData = chartData.map(entry => hourToNumber(entry.hour)); // Map hours to numbers
                                                const seriesData = chartData.map(entry => entry.consumption || 0); // Fallback to 0 if undefined

                                                // Debugging logs
                                                console.log("xAxis Data:", xAxisData);
                                                console.log("Series Data:", seriesData);

                                                // Render the chart
                                                return (
                                                    <LineChart
                                                        xAxis={[
                                                            {
                                                                data: xAxisData, // Numerical hours for X-axis
                                                            },
                                                        ]}
                                                        series={[
                                                            {
                                                                data: seriesData, // Consumption for Y-axis
                                                            },
                                                        ]}
                                                        width={500}
                                                        height={300}
                                                    />
                                                );
                                            })()
                                        )}
                                    </div>
                                )}
                            </div>

                        </div>
                    </div>

            </Modal>
        </>
    );
}
