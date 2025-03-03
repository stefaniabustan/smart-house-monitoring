import { Menu } from "antd";
import { useState } from "react";

export default function Header({ menuItems }) {
    const [current, setCurrent] = useState("");

    const onClick = (event) => {
        setCurrent(event.key);
    };

    return (
        <Menu
            onClick={onClick}
            selectedKeys={[current]}
            mode="horizontal"
            items={menuItems}
            style={{ paddingLeft: 30 }}
        />
    );
}
