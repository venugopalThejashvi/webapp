packer {
  required_plugins {
    googlecompute = {
      version = "~> 1"
      source  = "github.com/hashicorp/googlecompute"
    }
    amazon = {
      version = ">= 1.0.0, < 2.0.0"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

# Local timestamp for AMI naming
locals {
  timestamp = formatdate("MM-DD-YYYY-hh-mm-ss", timestamp())
}

# Variables for Google Cloud Platform

# Variables for AWS
variable "aws_region" {
  type = string
}

variable "subnet_id" {
  type = string
}

variable "volume_type" {
  type = string
}

variable "instance_type" {
  type = string
}

variable "device_name" {
  type = string
}

variable "aws_secret_key" {
  type = string
}

variable "aws_access_id" {
  type = string
}

variable "ami_regions" {
  type = list(string)
}

# Common variable used by both GCP and AWS
variable "ssh_username" {
  type = string
}

variable "demo_aws_account_id" {
  type        = string
  description = "AWS demo account ID to share the AMI with"
}

variable "source_ami" {
  type    = string
  default = "ami-04b4f1a9cf54c11d0"
}

# Source for AWS
source "amazon-ebs" "my-ami" {
  region        = var.aws_region
  source_ami    = var.source_ami
  instance_type = var.instance_type
  ssh_username  = var.ssh_username
  ami_users     = [var.demo_aws_account_id]

  ami_name        = "ssl3_csye6225_app_${formatdate("YYYY_MM_DD", timestamp())}"
  ami_description = "AMI for assignment 4"
  ami_regions     = var.ami_regions

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = var.device_name
    volume_size           = 8
    volume_type           = var.volume_type
    encrypted             = false
  }
}

# Build block for and AWS
build {
  # Define both sources to build images and AWS
  sources = [
    "source.amazon-ebs.my-ami"
  ]

  # Provisioners for and AWS (common steps)
  provisioner "file" {
    source      = "target/cloud-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/cloud-0.0.1-SNAPSHOT.jar"
  }

  provisioner "file" {
    source      = "myservice.service"
    destination = "/tmp/myservice.service"
  }

  provisioner "file" {
    source      = "./.env"
    destination = "/tmp/.env"
  }

  # Copy CloudWatch Agent config to /tmp
  provisioner "file" {
    source      = "cloudwatch-config.json"
    destination = "/tmp/amazon-cloudwatch-agent.json"
  }

  # Creating user and giving required permissions
  provisioner "shell" {
    script = "userCreation1.sh"
  }

  # Cloud watch installation
  provisioner "shell" {
    inline = [
      "sudo apt update",
      "wget https://s3.amazonaws.com/amazoncloudwatch-agent/ubuntu/amd64/latest/amazon-cloudwatch-agent.deb",
      "sudo dpkg -i amazon-cloudwatch-agent.deb || sudo apt install -f -y",
      "sudo mkdir -p /opt/aws/amazon-cloudwatch-agent/etc",
      "sudo mv /tmp/amazon-cloudwatch-agent.json /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json",
      "sudo chown root:root /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json",
      "sudo chmod 644 /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json",
      "sudo systemctl enable amazon-cloudwatch-agent"
    ]
  }

  # Final setup (app installation, service config)
  provisioner "shell" {
    script = "finalSetup3.sh"
  }

  post-processor "manifest" {
    output     = "packer-manifest.json"
    strip_path = true
  }
}