#!/bin/bash
sudo apt update
sudo apt upgrade
# Create a new Linux group for the application
echo "Creating new group 'csye6225'..."
sudo groupadd -f csye6225


# Create a new user for the application if not exists
echo "Creating new user 'csye6225user'..."
if id "csye6225user" &>/dev/null; then
   echo "User 'csye6225user' already exists."
else
   sudo useradd -m -s /bin/bash -g csye6225 csye6225user
fi


# Unzip the application in /opt/csye6225 directory
echo "Unzipping application to /opt/csye6225..."
sudo mkdir -p /opt/csye6225
sudo unzip -o /home/webapp-main.zip -d /opt/csye6225


# Update permissions of the folder and artifacts
echo "Updating permissions..."
sudo chown -R csye6225user:csye6225 /opt/csye6225
sudo chmod -R 750 /opt/csye6225
source .env
export DB_URL=$DB_URL
export DB_USERNAME=$DB_USERNAME
export DB_NAME=$DB_NAME
export DB_PASSWORD=$DB_PASSWORD
export DB_USER=$DB_USERNAME

sudo apt install postgresql postgresql-contrib
sudo systemctl status postgresql
sudo systemctl start postgresql
sudo -u postgres psql <<EOF
CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD';
ALTER USER $DB_USER CREATEDB CREATEROLE;
CREATE DATABASE $DB_NAME;
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;
GRANT ALL ON SCHEMA public TO $DB_USER;
EOF

sudo -u $USERNAME psql -c "ALTER USER $DB_USERNAME WITH PASSWORD '$DB_PASSWORD';"

# Create a new Linux group for the application
echo "Creating new group 'csye6225'..."
sudo groupadd -f csye6225


# Create a new user for the application if not exists
echo "Creating new user 'csye6225user'..."
if id "csye6225user" &>/dev/null; then
   echo "User 'csye6225user' already exists."
else
  sudo useradd -m -s /bin/bash -g csye6225 csye6225user
  fi


  # Unzip the application in /opt/csye6225 directory
  echo "Unzipping application to /opt/csye6225..."
  sudo mkdir -p /opt/csye6225
  sudo unzip -o /tmp/webapp-fork.zip -d /opt/csye6225
  sudo cp /tmp/webapp-fork/.env /opt/csye6225/webapp-fork/.env

  # Update permissions of the folder and artifacts
  echo "Updating permissions..."
  sudo chown -R csye6225user:csye6225 /opt/csye6225
  sudo chmod -R 750 /opt/csye6225

  apt install openjdk-21-jdk
  apt install maven
  mvn clean install