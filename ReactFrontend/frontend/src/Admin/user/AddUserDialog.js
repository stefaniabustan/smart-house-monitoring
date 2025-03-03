import {Button, Form, Input, message, Modal, Select} from "antd";
import "../../styles/AddUser.css";
import {MicroserviceType} from "../../global_manager/BaseUrl";
import {MicroserviceUtils} from "../../global_manager/BaseUrl";
import bcrypt from 'bcryptjs';
const { Option } = Select;


export default function AddUser({ isModalOpen, handleClose , refreshUsers}) {
    const [form] = Form.useForm();
    const userServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.User);

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            // Encriptarea parolei
            const salt =  await bcrypt.genSalt(10);
            const hashedPassword = await bcrypt.hash(values.password, salt);

            const token = localStorage.getItem("jwtToken");

            const response = await fetch(userServiceUrl + "/person/add", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({
                    ...values,
                    password: hashedPassword,
                }),
            });


            if (response.ok) {
                message.success("user added successfully");
                form.resetFields();
                handleClose();
                refreshUsers();
            } else {
                message.error("Failed to add user");
            }
        } catch (error) {
            console.error("Error adding user:", error);
            message.error("An error occurred");
        }
    };

    const onReset = () => {
        form.resetFields();
    };
    return (
        <Modal
            centered={true}
            open={isModalOpen}
            onOk={handleSubmit}
            onCancel={handleClose}
            title={<h2 style={{ textAlign: "center" }}>New User</h2>}
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

                <Form.Item >
                    <div className={"container-butt-submit"}>
                        <Button htmlType="button" onClick={onReset}>
                            Reset
                        </Button>
                    </div>
                </Form.Item>
            </Form>
        </Modal>
    )
}