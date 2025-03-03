import {Button, message, Modal, Table, Tooltip} from "antd";
import React, {useEffect, useState} from "react";
import "../../styles/TableDeviceByUser.css"
import {MinusOutlined} from "@ant-design/icons";
import {MicroserviceType} from "../../global_manager/BaseUrl";
import {MicroserviceUtils} from "../../global_manager/BaseUrl";
export default function DeviceByUser({ user, isModalOpen, handleClose}) {
    const [devices, setDevices] = useState([]);
    const deviceServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.Device);
    const token = localStorage.getItem("jwtToken");


    useEffect(() => {
        const fetchDevices = async () => {
            try {
                const response = await fetch(`${deviceServiceUrl}/device/person/${user.id}`,
                    {
                        method: "GET",
                        headers: { "Content-Type": "application/json",
                            "Authorization": `Bearer ${token}`,
                        },
                    });
                const data = await response.json();
                setDevices(data);
            } catch (err) {
                message.error("Error fetching devices");
            }
        };

        const debounceFetch = setTimeout(() => {
            fetchDevices();
        }, 500); // Delay of 500ms or adjust as needed

        return () => clearTimeout(debounceFetch); // Clean up on dependency change
    }, [user, devices]);


    const handleSubmit = async (record) => {
        try {
            const updatedDevice = {
                id: record.id,
                ...record,
                personId: user.id,
                map: false
            };

            // Send a PUT request to the backend
            const response = await fetch(`${deviceServiceUrl}/device/map`, {
                method: "PUT",
                headers: { "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,},
                body: JSON.stringify(updatedDevice),
            });
            const result = await response.json();


            if (result.message === "success") {
                message.success("Device updated successfully");
            } else {
                message.error("Failed to update the device");
            }
        } catch (error) {
            console.error("Error updating device:", error);
            message.error("An error occurred");
        }
    };

    const columns = [
        { title: "Device", dataIndex: "name", key: "name", align: "center" },
        { title: "Description", dataIndex: "description", key: "description", align: "center" },
        { title: "Address", dataIndex: "address", key: "address", align: "center" },
        { title: "MHC", dataIndex: "mhc", key: "mhc", align: "center"},
        { title: "Action",
            key: "action",
            align: "center",
            render: (_, record) => (
                <Tooltip title="Remove">
                    <Button
                        type="primary"
                        onClick={() => handleSubmit(record)}
                    ><MinusOutlined /></Button>
                </Tooltip>
            ),
        },
    ];

    return (
        <Modal
            centered={true}
            open={isModalOpen}
            onCancel={handleClose}
            footer={null}
            title={<p style={{ textAlign: "center" }}> Devices</p>}
        >
            <div className="table-container2">
                <Table
                    pagination={false}
                    columns={columns}
                    dataSource={devices}
                />
            </div>
        </Modal>
    )
}