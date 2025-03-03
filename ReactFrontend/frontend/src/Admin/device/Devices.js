import {Button, message, Space, Tooltip} from "antd";
import { useEffect, useState } from "react";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import CustomTable from "../../components/CustomTable";
import { Outlet} from "react-router-dom";
import AddDevice from "./AddDeviceDialog";
import UpdateDevice from "./UpdateDeviceDialog";
import AdminNavBar from "../AdminNavBar";
import {MicroserviceType} from "../../global_manager/BaseUrl";
import {MicroserviceUtils} from "../../global_manager/BaseUrl";

const initialDeviceDto = {
    id: 0,
    name: "",
    description: "",
    address: "",
    mhc: 0.0
};
export default function DevicesDash() {
    const [devices, setDevices] = useState([]);
    const [rowData, setRowData] = useState(initialDeviceDto)
    const [isModalAddOpen, setIsModalAddOpen] = useState(false);
    const [isModalUpdateOpen, setIsModalUpdateOpen] = useState(false);
    const deviceServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.Device);
    const userServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.User);
    const token = localStorage.getItem("jwtToken");


    const showModalAdd = () => {
        setIsModalAddOpen(true);
    };

    const hideModalAdd = () => {
        setIsModalAddOpen(false);
    };
    const showModalUpdate = (record) => {
        setRowData(record);
        setIsModalUpdateOpen(true);
    };
    const hideModalUpdate = () => {
        setIsModalUpdateOpen(false);
    };

    const fetchPersonById = async (personId) => {
        if(personId>0)
        {
            try {
                const response = await fetch(`${userServiceUrl}/person/${personId}`, {
                    method: "GET",
                    headers: { "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`,
                    },
                });
                if (response.ok) {
                    const personData = await response.json();
                    return personData.username;
                } else {
                    console.error(`Failed to fetch person with ID ${personId}`);
                    return "Device free";
                }
            } catch (error) {
                console.error("Error fetching person:", error);
                return "Unknown";
            }

        }
        else
        {
            return "Unknown";
        }

    };
    const fetchDevices = async () => {
        try {
            const response = await fetch(deviceServiceUrl+"/device/all", {
                method: "GET",
                headers: { "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
            });
            const devicesData = await response.json();

            const devicesWithPersonNames = await Promise.all(
                devicesData.map(async (device) => {
                    const personName = await fetchPersonById(device.personId);
                    return { ...device, personName };
                })
            );
            setDevices(devicesWithPersonNames);
        } catch (error) {
            console.error("Error fetching devices:", error);
        }
    };

    useEffect(() => {
        fetchDevices();
    }, []);

    // Declare refreshUsers function
    const refreshDevices = () => {
        fetchDevices(); // Call the fetch function
    };

    const handleDeviceOnRow = (receivedRowData) => {
        console.log("Row data:", receivedRowData);
    };

    const handleDelete = (userDevice) => {
        fetch(`${deviceServiceUrl}/device/delete/${userDevice}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
        })
            .then((response) => {
                if (response.ok) {
                    message.success("Device deleted successfully");
                    refreshDevices(); // Refresh table after delete
                } else {
                    message.error("Failed to delete device");
                }
            })
            .catch((error) => {
                console.error("Error deleting device:", error);
                message.error("An error occurred while deleting the device");
            });
    };

    const columns = [
        { title: "Id", dataIndex: "id", key: "id", align: "center" },
        { title: "Device", dataIndex: "name", key: "name", align: "center" },
        { title: "Description", dataIndex: "description", key: "description", align: "center" },
        { title: "Address", dataIndex: "address", key: "address", align: "center" },
        { title: "MHC", dataIndex: "mhc", key: "mhc", align: "center"},
        { title: "Person", dataIndex: "personName", key: "personName", align: "center" }, // Display personName

        { title: "Action",
            key: "action",
            align: "center",
            render: (_, record) => (
                <Space>
                    <Tooltip title="Delete">
                        <Button type={"primary"} onClick={() => handleDelete(record.id)}><DeleteOutlined /></Button>
                    </Tooltip>
                    <Tooltip title="Update">
                        <Button type={"primary"} onClick={() => showModalUpdate(record)}><EditOutlined /></Button>
                    </Tooltip>
                </Space>
            ),
        },
    ];
    return (
        <>
            <AdminNavBar/>
            <Outlet />
            <div className={"container-button-add"}>
                <Button type="primary" className={"button-add"} onClick={showModalAdd}> Add Device</Button>
            </div>
            <CustomTable columns={columns} list={devices} handleOnRow={handleDeviceOnRow} />
            {isModalAddOpen &&<AddDevice isModalOpen={isModalAddOpen} handleClose={hideModalAdd} refreshDevices={refreshDevices}/>}
            {isModalUpdateOpen && <UpdateDevice device={rowData} isModalOpen={isModalUpdateOpen} handleClose={hideModalUpdate} refreshDevices={refreshDevices}/>}
        </>
    );
}
