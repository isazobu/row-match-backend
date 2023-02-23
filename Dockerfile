# Use an official Java runtime as a parent image
FROM openjdk:8-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Install Maven to build the project
RUN apk add --no-cache curl tar bash procps git && \
    curl -fL https://archive.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz | tar xz && \
    ln -s /app/apache-maven-3.5.4/bin/mvn /usr/bin/mvn

# Build the project with Maven
RUN mvn package

# Set environment variables for MySQL
ENV MYSQL_DATABASE=mydatabase
ENV MYSQL_USER=myuser
ENV MYSQL_PASSWORD=mypassword
ENV MYSQL_ROOT_PASSWORD=myrootpassword

# Install MySQL
RUN apk add --no-cache mysql mysql-client && \
    mkdir /run/mysqld && \
    chown -R mysql:mysql /run/mysqld && \
    sed -i 's/^\(bind-address\s.*\)/# \1/' /etc/mysql/my.cnf && \
    sed -i 's/^\(log_error\s.*\)/# \1/' /etc/mysql/my.cnf && \
    echo -e '[mysqld]\nskip-host-cache\nskip-name-resolve' > /etc/mysql/conf.d/docker.cnf && \
    /usr/bin/mysql_install_db --user=mysql --datadir=/var/lib/mysql

# Expose port 8080 for the Java Spring application
EXPOSE 8080

# Start MySQL and the Java Spring application
CMD service mysql start && java -jar target/myapp.jar