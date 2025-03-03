import React from "react";
import {Pagination, Table} from "antd";
import "../styles/Table.css";

export default function CustomTable(props) {
    return (
        <div className="table-container">
            <Table
                columns={props.columns}
                dataSource={props.list}
                onRow={(record) => {
                    return {
                        onClick: () => {
                            props.handleOnRow(record);
                        },
                    };
                }}
                pagination={{
                    pageSize: 6,
                }}

            />
        </div>
    );
}
