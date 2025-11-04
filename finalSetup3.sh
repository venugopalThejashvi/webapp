#!/bin/bash

# Install OpenJDK 21 and Maven
echo "Installing OpenJDK 21 and Maven..."
sudo apt install -y openjdk-21-jdk
sudo apt install -y maven

# Build project with Maven
echo "Building the project with Maven..."

echo "Moving .service file to sys"
sudo mv /tmp/myservice.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable myservice.service
sudo systemctl start myservice.service