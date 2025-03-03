import {Button, Form, Input, message, Modal, Select} from "antd";
import React from "react";
import {MicroserviceType} from "../../global_manager/BaseUrl";
import {MicroserviceUtils} from "../../global_manager/BaseUrl";
const { Option } = Select;

export default function AddDevice({ isModalOpen, handleClose , refreshDevices}) {
    const [form] = Form.useForm();
    const deviceServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.Device);

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            const token = localStorage.getItem("jwtToken");

            const response = await fetch(deviceServiceUrl+"/device/add", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,

                },
                body: JSON.stringify(values)
            });

            if (response.ok) {
                message.success("Device added successfully");
                form.resetFields();
                handleClose();
                refreshDevices();
            } else {
                message.error("Failed to add device");
            }
        } catch (error) {
            console.error("Error adding device:", error);
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
            title={<h2 style={{ textAlign: "center" }}>New Device</h2>}
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
                    name="name"
                    label="Device"
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
                    name="description"
                    label="Description"
                    style={{ width: "100%" }}
                    rules={[
                        {
                            required: true,
                        },
                    ]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="address"
                    label="Address"
                    style={{ width: "100%" }}
                    rules={[
                        {
                            required: true,
                        },
                    ]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="mhc"
                    label="MHC"
                    style={{ width: "100%" }}
                    rules={[
                        {
                            required: true,
                            message: "Please input a number!",
                        },
                        {
                            validator: (_, value) =>
                                value && isNaN(Number(value))
                                    ? Promise.reject("Value must be a number")
                                    : Promise.resolve(),
                        },
                    ]}
                >
                    <Input />
                </Form.Item >
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