#!/bin/bash

# Set non-interactive mode for APT and disable HashiCorp telemetry
export DEBIAN_FRONTEND=noninteractive
export CHECKPOINT_DISABLE=1

# Update and upgrade system
echo "Updating and upgrading system..."
sudo apt update -y
sudo apt upgrade -y

# Create a group and user
echo "Creating new group csye6225..."
sudo groupadd -f csye6225

echo "Creating new user csye6225user..."
if id "csye6225user" &>/dev/null; then
    echo "User already exists."
else
    sudo useradd -m -s /bin/bash -g csye6225 csye6225user
fi

# Copy application folder from /tmp/webapp to /opt/csye6225/webapp
echo "Copying application folder from /tmp/webapp to /opt/csye6225/webapp..."
sudo mkdir -p /opt/csye6225/webapp
sudo cp -a /tmp/.env /opt/csye6225/webapp/.env
sudo cp -a /tmp/cloud-0.0.1-SNAPSHOT.jar /opt/csye6225/webapp/cloud-0.0.1-SNAPSHOT.jar

# Update permissions
echo "Updating permissions..."
sudo chown -R csye6225user:csye6225 /opt/csye6225/webapp
sudo chmod -R 755 /opt/csye6225/webapp

echo "To read .env"
sudo chmod 755 /opt/csye6225/
sudo chown csye6225user:csye6225 /opt/csye6225/
sudo chmod -R 755 /opt/csye6225/webapp