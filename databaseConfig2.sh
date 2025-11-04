#!/bin/bash

# Install PostgreSQL
echo "Installing PostgreSQL..."
sudo apt install -y postgresql postgresql-contrib
sudo systemctl start postgresql

# Load environment variables
echo "Setting up environment variables..."
source /opt/csye6225/webapp/.env
export DB_URL=$DB_URL
export DB_USERNAME=$DB_USERNAME
export DB_NAME=$DB_NAME
export DB_PASSWORD=$DB_PASSWORD
export DB_USER=$DB_USERNAME

# Setup PostgreSQL database
echo "Configuring PostgreSQL..."
sudo -u postgres psql -c "CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD';"
sudo -u postgres psql -c "ALTER USER $DB_USER CREATEDB CREATEROLE;"
sudo -u postgres psql -c "CREATE DATABASE $DB_NAME;"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;"
sudo -u postgres psql -c "GRANT ALL ON SCHEMA public TO $DB_USER;"
sudo -u postgres psql -c "ALTER USER $DB_USERNAME WITH PASSWORD '$DB_PASSWORD';"
