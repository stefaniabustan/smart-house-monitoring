import {Button, message, Space, Tag, Tooltip} from "antd";
import { useEffect, useState } from "react";
import {
    DeleteOutlined, DesktopOutlined,
    EditOutlined,
    PlusOutlined
} from "@ant-design/icons";
import CustomTable from "../../components/CustomTable";
import { Outlet} from "react-router-dom";
import "../../styles/Users.css"
import AddUser from "./AddUserDialog";
import UpdateUser from "./UpdateUserDialog";
import DeviceByUser from "./DeviceByUser";
import AddDevice2User from "./AddDevice2User";
import AdminNavBar from "../AdminNavBar";
import {MicroserviceType} from "../../global_manager/BaseUrl";
import {MicroserviceUtils} from "../../global_manager/BaseUrl";



function RolesTag({ role }) {
    const color = role ? "#343a40" : "#6c757d";
    return (
        <Tag color={color} key={role}>
            {role ? "Admin" : "User"}
        </Tag>
    );
}

const initialUserDto = {
    id: 0,
    username: "",
    password: "",
    isAdmin: false
};
export default function UsersDash() {
    const [users, setUsers] = useState([]);
    const [isModalAddOpen, setIsModalAddOpen] = useState(false);
    const [isModalUpdateOpen, setIsModalUpdateOpen] = useState(false);
    const [isModalViewDevOpen, setIsModalViewDevOpen] = useState(false);
    const [isModalAddDevOpen, setIsModalAddDevOpen] = useState(false);

    const [rowData, setRowData] = useState(initialUserDto)

    const deviceServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.Device);
    const userServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.User);


    const handleUserOnRow = (receivedRowData) => {
        setRowData(receivedRowData);
    };

    const showModalUpdate = (record) => {
        setRowData(record);
        setIsModalUpdateOpen(true);
    };
    const hideModalUpdate = () => {
        setIsModalUpdateOpen(false);
    };
    const showModalAdd = () => {
        setIsModalAddOpen(true);
    };

    const hideModalAdd = () => {
        setIsModalAddOpen(false);
    };




    const fetchUsers = () => {
        const token = localStorage.getItem("jwtToken");
        console.log("Token being sent:", token);

        fetch(userServiceUrl + "/person", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
        })
            .then((response) => {
                console.log("Response status:", response.status);
                if (!response.ok) {
                    throw new Error('Unauthorized or Failed to fetch data');
                }
                return response.json();
            })
            .then((data) => setUsers(data))
            .catch((error) => console.error("Error fetching users:", error));
    };




    useEffect(() => {
        fetchUsers();
    }, []);

    const refreshUsers = () => {
        fetchUsers();
    };

    const handleDelete = (userId) => {
        const token = localStorage.getItem("jwtToken");

        fetch(`${userServiceUrl}/person/delete/${userId}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
        })
            .then((response) => {
                if (response.ok) {
                    message.success("user deleted successfully");
                }
                refreshUsers();

            })
            .catch((error) => {
                console.error("Error deleting user:", error);
            });
    };

    const columns = [
        { title: "ID", dataIndex: "id", key: "id", align: "center" },
        { title: "Username", dataIndex: "username", key: "username", align: "center" },
        {
            title: "Role",
            dataIndex: "isAdmin",
            key: "isAdmin",
            align: "center",
            render: (_, { isAdmin }) => <RolesTag role={isAdmin} />,
        },
        {
            title: "Action",
            key: "action",
            align: "center",
            render: (_, record) => (
                <Space>
                    <Tooltip title="Delete">
                    <Button type={"default"} onClick={() => handleDelete(record.id)}>
                        <DeleteOutlined />
                    </Button>
                    </Tooltip>
                    <Tooltip title={"Update"}>
                    <Button type={"default"} onClick={() => showModalUpdate(record)}>
                        <EditOutlined />
                    </Button>
                    </Tooltip>

                </Space>
            ),
        },
        {
            title: "Devices",
            key: "action",
            align: "center",
            render: (_, record) => (
                <Space>
                    <Tooltip title="View devices">
                        <Button type={"primary"} onClick={() => setIsModalViewDevOpen(true)}>
                            <DesktopOutlined />
                        </Button>
                    </Tooltip>
                    <Tooltip title="Add device">
                    <Button type={"primary"} onClick={() => setIsModalAddDevOpen(true)}>
                        <PlusOutlined />
                    </Button>
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
                <Button type="primary" className={"button-add"} onClick={showModalAdd}> Add User</Button>
            </div>
            <CustomTable columns={columns} list={users} handleOnRow={handleUserOnRow} />
            {isModalAddOpen &&<AddUser isModalOpen={isModalAddOpen} handleClose={hideModalAdd} refreshUsers={refreshUsers}/>}
            {isModalUpdateOpen && <UpdateUser user={rowData} isModalOpen={isModalUpdateOpen} handleClose={hideModalUpdate} refreshUsers={refreshUsers}/>}
            {isModalViewDevOpen && <DeviceByUser user={rowData} isModalOpen={isModalViewDevOpen} handleClose={()=>{setIsModalViewDevOpen(false)}}  />}
            {isModalAddDevOpen && <AddDevice2User user={rowData} isModalOpen={isModalAddDevOpen} handleClose={()=>{setIsModalAddDevOpen(false)}}  />}

        </>
    );
}
