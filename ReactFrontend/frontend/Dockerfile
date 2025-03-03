# Step 1: Use a lightweight web server to serve the production build
FROM nginx:alpine

# Step 2: Copy the build directory from the local machine into the container
COPY ./build /usr/share/nginx/html

# Expose port 80 (default for Nginx)
EXPOSE 80

# Step 3: Start Nginx server
CMD ["nginx", "-g", "daemon off;"]