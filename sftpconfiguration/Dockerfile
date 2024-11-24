# Use Ubuntu as base image
FROM ubuntu:latest

# Avoid prompts from apt
ENV DEBIAN_FRONTEND=noninteractive

# Update packages and install OpenSSH Server
RUN apt-get update && \
    apt-get install -y openssh-server && \
    rm -rf /var/lib/apt/lists/*

# Create SSH directory and initialize SSHD
RUN mkdir /var/run/sshd

# Add an SFTP user with password authentication
RUN useradd -m -d /home/sftpuser -s /bin/bash sftpuser && \
    echo "sftpuser:sftppassword" | chpasswd && \
    mkdir -p /home/sftpuser/.ssh && \
    chown sftpuser:sftpuser /home/sftpuser/.ssh && \
    chmod 700 /home/sftpuser/.ssh

# Copy the public key
# Ensure you replace 'docker_rsa.pub' with your actual public key file name
COPY docker_rsa.pub /home/sftpuser/.ssh/authorized_keys

# Set permissions for the public key
RUN chmod 600 /home/sftpuser/.ssh/authorized_keys && \
    chown sftpuser:sftpuser /home/sftpuser/.ssh/authorized_keys

# Create a directory for SFTP that the user will have access to
RUN mkdir -p /home/sftpuser/sftp/upload && \
    chown root:root /home/sftpuser /home/sftpuser/sftp && \
    chmod 755 /home/sftpuser /home/sftpuser/sftp && \
    chown sftpuser:sftpuser /home/sftpuser/sftp/upload && \
    chmod 755 /home/sftpuser/sftp/upload

# Configure SSH for SFTP
RUN mkdir -p /run/sshd && \
    echo "Match User sftpuser" >> /etc/ssh/sshd_config && \
    echo "    ChrootDirectory /home/sftpuser/sftp" >> /etc/ssh/sshd_config && \
    echo "    ForceCommand internal-sftp" >> /etc/ssh/sshd_config && \
    echo "    PasswordAuthentication yes" >> /etc/ssh/sshd_config && \
    echo "    PubkeyAuthentication yes" >> /etc/ssh/sshd_config && \
    echo "    PermitTunnel no" >> /etc/ssh/sshd_config && \
    echo "    AllowAgentForwarding no" >> /etc/ssh/sshd_config && \
    echo "    AllowTcpForwarding no" >> /etc/ssh/sshd_config && \
    echo "    X11Forwarding no" >> /etc/ssh/sshd_config

# Expose SSH port
EXPOSE 22

# Start SSH service
CMD ["/usr/sbin/sshd", "-D"]

