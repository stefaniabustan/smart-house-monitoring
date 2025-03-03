import { Form, Input, message, Modal, Select} from "antd";
import "../../styles/AddUser.css";
import {MicroserviceType} from "../../global_manager/BaseUrl";
import {MicroserviceUtils} from "../../global_manager/BaseUrl";
const { Option } = Select;
export default function UpdateUser({user, isModalOpen, handleClose, refreshUsers }) {
    const [form] = Form.useForm();
    const userServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.User);

    form.setFieldsValue({
        username: user.username,
        password: user.password,
        isAdmin: user.isAdmin ? "true" : "false"
    });

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            const token = localStorage.getItem("jwtToken");
            const updatedUser = { id: user.id, ...values };

            const response = await fetch(userServiceUrl+"/person/update", {
                method: "PUT",
                headers: { "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify(updatedUser)
            });
            const result = await response.json();

            if (result.message === 'success') {
                message.success("user updated successfully");
                form.resetFields();
                handleClose();
                refreshUsers();

            } else {
                message.error("Failed to update user");
            }
        } catch (error) {
            console.error("Error updating user:", error);
            message.error("An error occurred");
        }
    };



    return (
        <Modal
            centered={true}
            open={isModalOpen}
            onOk={handleSubmit}
            onCancel={handleClose}
            title={<h2 style={{ textAlign: "center" }}>Update User</h2>}
        >
            <Form
                form={form}
                name="control-hooks"
                style={{
                    maxWidth: 400,
                    margin: "0 auto",
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center"
                }}
                layout="vertical"
            >
                <Form.Item
                    name="username"
                    label="Username"
                    rules={[
                        {
                            required: true,
                        },
                    ]}
                    style={{ width: "100%" }}

                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="password"
                    label="Password"
                    rules={[
                        {
                            required: true,
                        },
                    ]}
                    style={{ width: "100%" }}

                >
                    <Input.Password />
                </Form.Item>
                <Form.Item
                    name="isAdmin"
                    label="User type"
                    style={{ width: "100%" }}

                >
                    <Select
                        placeholder="Select user type"
                    >
                        <Option value="true">ADMIN</Option>
                        <Option value="false">USER</Option>
                    </Select>
                </Form.Item>


            </Form>
        </Modal>
    )
}