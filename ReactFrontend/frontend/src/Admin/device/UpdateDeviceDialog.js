import {Form, Input, message, Modal, Select} from "antd";
import React from "react";
import {MicroserviceType} from "../../global_manager/BaseUrl";
import {MicroserviceUtils} from "../../global_manager/BaseUrl";

const { Option } = Select;

export default function UpdateDevice({device, isModalOpen, handleClose, refreshDevices }) {
    const [form] = Form.useForm();
    const deviceServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.Device);
    const token = localStorage.getItem("jwtToken");

    form.setFieldsValue({
        name: device.name,
        description: device.description,
        address: device.address,
        mhc: device.mhc,
    });

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();

            const updatedDevice = { id: device.id, ...values};
            const response = await fetch(deviceServiceUrl+"/device/update", {
                method: "PATCH",
                headers: { "Content-Type": "application/json" ,
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify(updatedDevice)
            });
            const result = await response.json();


            if (result.message === 'success') {
                message.success("Device updated successfully");
                form.resetFields(); // Reset form fields
                handleClose(); // Close the modal
                refreshDevices(); // Refresh users table

            } else {
                message.error("Failed to update the device");
            }
        } catch (error) {
            console.error("Error updating device:", error);
            message.error("An error occurred");
        }
    };



    return (
        <Modal
            centered={true}
            open={isModalOpen}
            onOk={handleSubmit}
            onCancel={handleClose}
            title={<h2 style={{ textAlign: "center" }}>Update Device</h2>} // Center the title
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

                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="address"
                    label="Address"
                    style={{ width: "100%" }}

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
                </Form.Item>
            </Form>
        </Modal>
    )
}