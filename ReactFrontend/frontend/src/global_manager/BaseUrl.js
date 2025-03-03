export const MicroserviceType = {
    User: "User",
    Device: "Device",
    Monitor: "Monitor",
    Chat : "Chat",
};

export const MicroserviceUtils = {
    baseUrl: (type) => {
        switch (type) {
            case MicroserviceType.User:
                return "http://user.localhost";
                // return "http://localhost:8083";
            case MicroserviceType.Device:
                // return "http://localhost:8081";
                return "http://device.localhost";
            case MicroserviceType.Monitor:
                // return "http://localhost:8082";
                return "http://monitoring.localhost";
            case MicroserviceType.Chat:
                // return "http://localhost:8084";
                return "http://chat.localhost";
            default:
                throw new Error("Invalid MicroserviceType");
        }
    },
};
