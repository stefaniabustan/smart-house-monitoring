import React, {useCallback, useEffect, useRef, useState} from 'react';
import '../styles/AdminChat.css';
import {Button, Input, Layout, List, Space, Typography,} from 'antd';
import {CheckCircleTwoTone, CheckOutlined, UsergroupAddOutlined, UserOutlined} from '@ant-design/icons';
import UserNavBar from './UserNavBar';
import {MicroserviceType, MicroserviceUtils} from "../global_manager/BaseUrl";
import SockJS from 'sockjs-client';
import {Client} from '@stomp/stompjs';

const { Header, Content, Sider } = Layout;
const { TextArea, Search } = Input;

export default function UserChat() {
    const userServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.User);
    const [messages, setMessages] = useState({});
    const loggedUser = JSON.parse(localStorage.getItem('user')) || {};
    const [newMessage, setNewMessage] = useState('');
    const [searchValue, setSearchValue] = useState('');
    const [users, setUsers] = useState([]);
    const groupChatName = 'All Users'; // Group chat identifier
    const stompClientRef = useRef(null); // Store WebSocket client



    ///***
    const [selectedRecipient, setSelectedRecipient] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [connectionStatus, setConnectionStatus] = useState('Disconnected');
    const [typingNotifications, setTypingNotifications] = useState({});
    const [unreadMessages, setUnreadMessages] = useState({});
    const chatServiceUrl = MicroserviceUtils.baseUrl(MicroserviceType.Chat);

    const fetchUsers = () => {
        const token = localStorage.getItem('jwtToken');
        fetch(userServiceUrl + '/person/username', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        })
            .then((response) => response.json())
            .then((data) => setUsers(data))
            .catch((error) => console.error('Error fetching users:', error));
    };
    useEffect(() => {
        fetchUsers();
    }, []);

    // WebSocket connection setup
    useEffect(() => {
        const socket = new SockJS(chatServiceUrl+'/chat');
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            debug: (str) => console.log(str),

            onConnect: () => {
                console.log('WebSocket connected');
                setConnectionStatus('Connected');

                client.subscribe(`/topic/chat/${loggedUser.username}`, (message) => {
                    console.log('Message received for user:', message);
                    const receivedMessage = JSON.parse(message.body);
                    if (receivedMessage.isRead === true) {
                        handleReadReceipt(receivedMessage);
                    } else if (receivedMessage.content.includes('is typing...')) {
                        handleTypingNotification(receivedMessage);
                    } else {
                        handleChatMessage(receivedMessage);
                    }
                });

                client.subscribe(`/topic/chat/typing/${loggedUser.username}`, (message) => {
                    console.log('Typing notification received for user:', message);
                    const typingMessage = JSON.parse(message.body);
                    handleTypingNotification(typingMessage);
                });

                client.subscribe(`/topic/chat/typing/${selectedRecipient}`, (message) => {
                    console.log('Typing notification for selected recipient:', message);
                    const typingMessage = JSON.parse(message.body);
                    handleTypingNotification(typingMessage);
                });

                client.subscribe(`/topic/chat/typing/all`, (message) => {
                    console.log('Group typing notification received:', message);
                    const typingMessage = JSON.parse(message.body);
                    handleTypingNotification(typingMessage, 'All Users');
                });

                client.subscribe(`/topic/chat/all`, (message) => {
                    console.log('Message received for EVERYONE:', message);
                    const receivedMessage = JSON.parse(message.body);
                    handleChatMessage(receivedMessage, 'All Users');
                });
            },
            onStompError: (frame) => {
                console.error('Broker reported error:', frame.headers['message']);
                setConnectionStatus('Error');
            },
            onDisconnect: () => {
                console.log('WebSocket disconnected');
                setConnectionStatus('Disconnected');
            },


        });

        stompClientRef.current = client;
        client.activate();
        setStompClient(client);
        return () => {
            if (stompClientRef.current) stompClientRef.current.deactivate();
        };
    }, [ selectedRecipient]);


    /** HANDLE Functions **/

    const handleChatMessage = (receivedMessage, conversationKey = null) => {
        const sender = receivedMessage.sender;
        const key = conversationKey || sender;

        if (sender === loggedUser.username && key === "All Users") {
            // Ignore your own messages in group chat (assuming you see them right after sending)
            return;
        }

        console.log('Handling chat message:', receivedMessage);

        setMessages((prevMessages) => ({
            ...prevMessages,
            [key]: [...(prevMessages[key] || []), receivedMessage],
        }));

        if (selectedRecipient === key) {
            sendReadReceipt(sender);
        } else {
            setUnreadMessages((prev) => ({
                ...prev,
                [key]: (prev[key] || 0) + 1,
            }));
        }
    };

    const handleTypingNotification = (typingMessage, conversationKey = null) => {
        const sender = typingMessage.sender;
        const key = conversationKey || (typingMessage.groupChat ? groupChatName : sender);


        if (sender === loggedUser.username && key === "All Users") {
            return;
        }

        console.log('Handling typing notification from:', sender);

        if (key === groupChatName) {
            //for the group
            setTypingNotifications((prev) => ({
                ...prev,
                [key]: {
                    ...(prev[key] || {}),
                    [sender]: true,
                },
            }));

            setTimeout(() => {
                setTypingNotifications((prev) => {
                    const updatedGroupNotifications = { ...(prev[key] || {}) };
                    delete updatedGroupNotifications[sender];

                    return {
                        ...prev,
                        [key]: updatedGroupNotifications,
                    };
                });
            }, 3000);
        } else {
            setTypingNotifications((prev) => ({
                ...prev,
                [sender]: true,
            }));

            setTimeout(() => {
                setTypingNotifications((prev) => ({
                    ...prev,
                    [sender]: false,
                }));
            }, 3000);
        }
    };

    const handleReadReceipt = (readMessage) => {
        setMessages((prevMessages) => {
            const updatedConversations = { ...prevMessages };

            if (updatedConversations[readMessage.recipient]) {
                updatedConversations[readMessage.recipient] =
                    updatedConversations[readMessage.recipient].map(msg => {
                        if (msg.sender === readMessage.sender) {
                            return {
                                ...msg,
                                isRead: readMessage.isRead,
                                readBy: readMessage.readBy || []
                            };
                        }
                        return msg;
                    });
            }

            return updatedConversations;
        });
    };

    const sendMessage = () => {
        if (!stompClient || connectionStatus !== 'Connected' || !newMessage) return;
        console.log(`Selected Recipient: '${selectedRecipient}', Group Chat Name: '${groupChatName}'`);
        console.log("Comparison Result: ", selectedRecipient === groupChatName);

        const chatMessage = {
            sender: loggedUser.username,
            recipient: selectedRecipient,
            content: newMessage,
            groupChat: selectedRecipient === groupChatName,
        };


        if (messages[selectedRecipient]) {
            messages[selectedRecipient]
                .filter(msg => msg.sender !== loggedUser.username && (!msg.isRead || msg.isRead !== true))
                .forEach(msg => {
                    sendReadReceipt(msg.sender);
                });
        }


        stompClient.publish({
            destination: '/app/sendMessage',
            body: JSON.stringify(chatMessage)
        });

        setMessages((prevMessages) => {
            return {
                ...prevMessages,
                [selectedRecipient]: [
                    ...(prevMessages[selectedRecipient] || []),
                    {...chatMessage, timestamp: new Date().toISOString()},
                ],
            };
        });

        setNewMessage('');
    };
    const sendTypingNotification = useCallback(() => {
        if (stompClient && selectedRecipient && connectionStatus === 'Connected') {
            const typingMessage = {
                sender: loggedUser.username,
                recipient: selectedRecipient,
                groupChat: selectedRecipient === groupChatName,
            };

            console.log('Sending typing notification:', typingMessage);

            stompClient.publish({
                destination: '/app/typing',
                body: JSON.stringify(typingMessage),
            });
        }
    }, [stompClient, selectedRecipient, loggedUser, connectionStatus]);

    const sendReadReceipt = useCallback((sender) => {
        if (stompClient && connectionStatus === 'Connected') {
            const readMessage = {
                sender: sender,
                recipient: loggedUser.username,
                isRead: true,
                readBy: [loggedUser.username]
            };

            stompClient.publish({
                destination: '/app/readNotification',
                body: JSON.stringify(readMessage),
            });

            setUnreadMessages((prev) => ({
                ...prev,
                [sender]: 0,
            }));
        }
    }, [stompClient, loggedUser, connectionStatus]);

    const handleRecipientSelection = (recipient) => {
        console.log('Recipient selected:', recipient);
        setSelectedRecipient(recipient);

        if (recipient) {
            sendReadReceipt(recipient);
        }
    };

    const handleTextBoxClick = () => {
        if (selectedRecipient && messages[selectedRecipient]) {
            messages[selectedRecipient]
                .filter(msg => msg.sender !== loggedUser.username && (!msg.isRead || msg.isRead !== true))
                .forEach(msg => {
                    sendReadReceipt(msg.sender);
                });
        }
    };

    const handleTypingEvent = (e) => {
        const newValue = e.target.value;
        setNewMessage(newValue);

        console.log('Typing event, new value:', newValue);

        if (newValue.length > 0 && selectedRecipient) {
            if (messages[selectedRecipient]) {
                messages[selectedRecipient]
                    .filter(msg => msg.sender !== loggedUser.username && (!msg.isRead || msg.isRead !== true))
                    .forEach(msg => {
                        sendReadReceipt(msg.sender);
                    });
            }

            sendTypingNotification();
        }
    };





    const filteredChats = [groupChatName, ...users.filter(
        (user) => user !== loggedUser.username
    )].filter((username) =>
        username.toLowerCase().includes(searchValue.toLowerCase())
    );


    return (
        <Layout style={{ height: '100vh' }}>
            <UserNavBar />
            <Layout>
                <Sider width={300} className="chat-sider">
                    <div className="title-container">
                        <span>Chats</span>
                    </div>
                    <div className="search-container">
                        <Search
                            placeholder="Search users"
                            onChange={(e) => setSearchValue(e.target.value)}
                            value={searchValue}
                            allowClear
                        />
                    </div>
                    <List
                        itemLayout="horizontal"
                        dataSource={filteredChats}
                        renderItem={(item) => (
                            <List.Item
                                className={`chat-list-item ${
                                    selectedRecipient === item ? 'active-chat' : ''
                                }`}
                                onClick={(e) => handleRecipientSelection(item)}
                            >
                                <List.Item.Meta
                                    avatar={
                                        item.username === groupChatName ? (
                                            <UsergroupAddOutlined style={{ color: 'blue' }} />
                                        ) : (
                                            <UserOutlined />
                                        )
                                    }
                                    title={<Typography.Text>{item}</Typography.Text>}
                                />
                            </List.Item>
                        )}
                    />
                </Sider>
                <Layout>
                    <Header className="chat-header">
                        <Space className="header-name">
                            {selectedRecipient === groupChatName ? <UsergroupAddOutlined /> : <UserOutlined />}
                            <Typography.Text>{selectedRecipient}</Typography.Text>
                            {selectedRecipient === groupChatName && (
                                <Typography.Text>(Group Members: {users.map((u) => u).join(', ')})</Typography.Text>
                            )}
                        </Space>
                    </Header>
                    <Content className="chat-content">
                        {(messages[selectedRecipient] || []).map((msg, index) => (
                            <div key={index} className={`message-container ${msg.sender === loggedUser.username ? 'message-right' : 'message-left'}`}>
                                <div>
                                    <div className="message-username">
                                        <UserOutlined
                                            style={{ marginRight: '8px',
                                                color: msg.sender === loggedUser.username ? '#000' : '#1890ff',
                                            }}
                                        />
                                        <span>
                                                {msg.sender === loggedUser.username ? 'You:' : `${msg.sender}:`}
                                        </span>
                                    </div>
                                </div>
                                <div className="message-content">
                                    <Typography.Text>{msg.text || msg.content}</Typography.Text>
                                </div>

                                {msg.sender === loggedUser.username && (
                                    <div className="read-status">
                                        {msg.isRead ? (
                                            <CheckCircleTwoTone twoToneColor="#1890ff" />
                                        ) : (
                                            <CheckOutlined />
                                        )}
                                    </div>
                                )}
                            </div>
                        ))}
                    </Content>

                    <div className="chat-input">
                        <TextArea
                            rows={2}
                            value={newMessage}
                            onChange={handleTypingEvent}
                            onClick={handleTextBoxClick}
                            onKeyDown={(e) => {
                                if (e.key === 'Enter') {
                                    sendMessage();
                                }
                            }}
                            placeholder="Type something..."
                        />
                        {selectedRecipient === groupChatName ? (
                            typingNotifications[groupChatName] &&
                            Object.keys(typingNotifications[groupChatName]).length > 0 && (
                                <div className="typing-indicator">
                                    <Typography.Text type="secondary">
                                        {Object.keys(typingNotifications[groupChatName]).join(', ')}{' '}
                                        {Object.keys(typingNotifications[groupChatName]).length > 1 ? 'are' : 'is'} typing...
                                    </Typography.Text>
                                </div>
                            )
                        ) :(typingNotifications[selectedRecipient] && (
                            <div className="typing-indicator">
                                <Typography.Text type="secondary">
                                    {selectedRecipient === groupChatName
                                        ? "Someone is typing..."
                                        : `${selectedRecipient} is typing...`}
                                </Typography.Text>
                            </div>
                        ))}

                        <Button type="primary" onClick={sendMessage} className="send-button">
                            Send
                        </Button>

                    </div>
                </Layout>
            </Layout>
        </Layout>
    );
}

