# ImageStore-App

### Note 1:

### If you want to run the backend + webapp together in a container and test in UI, please refer to the instructions towards the end of the README.md

### Note 2:

### If you want to run just the backend imagestore-server, please keep reading below.

## ImageStore-Server

Microservice for storing uploading and managing images.
This section focuses on only the backed `imagestore-server`

### Feature/Functionality

#### Auth Service Module

- Register User with required details
- Login with user email and password
- Refresh the expired access token with refresh-token

#### Image Service Module

- Upload image
- Get image by id
- Get all images
- Update an image and description with metadata in place
- Delete an image by id

# Architecture

Architecture diagram is kept in the root folder `imagestore-app`

### How it works?

The image on upload will be stored in two phase.

1. Store the Image metadata along with the Image filepath in MYSQL DB.
2. Store the Image file in Filesystem or Docker volumes depending on how the app is run.

Structure of Image Metadata:

```
 {
    "id: "1",
    "name": "Picture 1",
    "description: "Desc",
    "type": "JPEG",
    "filePath: "/test-dir/image.jpg",
    "createdOn": "2022:01:11",
    "createdBy: "User1",
    "updatedOn": "2022:01:11",
    "updatedBy": "Admin2"
 }
```

The service provides the below APIs to upload and manage images in the system:

- Upload Image -

  #### POST /images

  Request:

  - body: form-data
    ```
    "image": File,
    "desc": "Picture 1"
    ```
  - header: Authorization | type: Bearer token

  Response:

  ```
  id
  ```

- Get Image by Image Id

  #### GET /images/{id}

  - header: Authorization | type: Bearer token

  - Response:

  ```
  {
  "id": 1,
  "name": "PicsArt_05-16-06",
  "description": "Picture 2",
  "type": "JPEG",
  "filePath": "imagetest/97096876-9e53-4011-82fc-d4756fe09e8d_PicsArt_05-16-06.50.32.jpg",
  "image": {byte[]}
  }
  ```

- Load Image file by Image Id - GET /images/{id}/\_load

  #### GET /images/{id}/\_load

  - header: Authorization | type: Bearer token

  Response:

  ```
  byte[]
  ```

- Get All Images in the system -

  #### GET /images

  - header: Authorization | type: Bearer token

  Response:

  ```
  {
    [
      {
        "id": 1,
        "name": "PicsArt_05-16-06",
        "description": "Picture 2",
        "type": "JPEG",
        "filePath": "imagetest/97096876-9e53-4011-82fc-d4756fe09e8d_PicsArt_05-16-06.50.32.jpg",
        "image": {byte[]}
      }
    ]
  }
  ```

- Update an Image and its description with the metadata in place

  #### PATCH /images/{id}

  - header: Authorization | type: Bearer token

  Request:

  - body: form-data
    ```
    "image": File,
    "desc": "Picture 2"
    ```
  - header: Authorization | type: Bearer token

  Response:

  ```
  id
  ```

- Delete an uploaded Image and its metadata
  #### DELETE /images/{id}
  - header: Authorization | type: Bearer token

The above APIs are secured with spring security using JWT Token Authentication.

#### How to access the secured APIs:

1. The user will have to register themselves with details using the API:

   ##### POST /api/auth/signup

   Request:

   ```
   {
     "firstname": "Test",
     "lastname": "User",
     "email": "test.user@imgapp.com",
     "password": "testPassword@123"
   }
   ```

   Response: User successfully registered.

2. The user will have to now login to access the security token that should be used to access the secured APIs:

   ##### POST /api/auth/login

   Request:

   ```
   {
     "email": "test.user@imgapp.com",
     "password": "testPassword@123"
   }
   ```

   Response:

   ```
   {
     "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW5qYXkuc3VyZXNoQGltZ2FwcC5jb20iLCJpYXQiOjE3MDczOTIzMTYsImV4cCI6MTcwNzM5NTkxNn0.rLWtBUimwm050bRAlK2REi7WjFaokv1FaRArPIDP5rE",
     "expirationDate": "2024-02-08T12:38:36.000+00:00",
     "refreshToken": "f101a352-f2be-4525-a450-6116dbf42c12",
     "email": "sanjay.suresh@imgapp.com"
   }
   ```

   Note: Once the accessToken is generated, this accessToken can be passed in the Authorization header as a Bearer token.

3. API to refresh the access token when it gets expired using refresh-token

   ##### POST /api/auth/refresh-token

   Request:

   ```
   {
     "email": "test.user@imgapp.com",
     "password": "testPassword@123"
   }
   ```

   Response:

   ```
   {
     "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW5qYXkuc3VyZXNoQGltZ2FwcC5jb20iLCJpYXQiOjE3MDczOTIzMTYsImV4cCI6MTcwNzM5NTkxNn0.rLWtBUimwm050bRAlK2REi7WjFaokv1FaRArPIDP5rE",
     "expirationDate": "2024-02-08T12:38:36.000+00:00",
     "refreshToken": "f101a352-f2be-4525-a450-6116dbf42c12",
     "email": "sanjay.suresh@imgapp.com"
   }
   ```

## Getting Started

## Dependencies:

You may refer gradle dependencies.

### Build and Run:

The following instructions should be executed from the folder `imagestore-server/`
The application can be run using two approaches:

1. Run the application in local with IDE:

   - Prerequisite:
     - Java 17
     - Gradle
     - MySQL
     - IntelliJ or any other IDE of choice
   - Build:

     The project uses gradle to manage dependencies and build the project.

     ```
        ./gradlew clean build
     ```

   - Run and Debug:
     Using 'local' profile, you can start the service or execute the command `gradle bootRun` from the cmd prompt

2. Run the application in Docker:

   - Prerequisite:

     - Docker
     - Gradle

   - Build:

     The project uses gradle to manage dependencies and build the project.

     ```
      ./gradlew clean build
     ```

   - Run:
     I have kept a docker file in the root of imagestore-server which can be used to run the image in container.
     The compose file pull mysql, jaeger and run it with imagestore-server image in a shared network.

     ```
       docker compose up
     ```

     ```
     Note: If you dont have gradle in you system, If you wish to run the Dockerfile I have kept
     one more docker file in the folder docker-gradle/ that uses builds the image in build steps
     with the gradle being used to wrap and build the jar.
     You can replace the Dockerfile in the root with the file in the above folder
     and run docker compose up.


     ```

## Documentation, Monitor, Tracing, Test

### Swagger UI:

You can find the documentation and test the APIs [here](http://localhost:8080/imagestore-documentation)

```
    http://localhost:8080/imagestore-documentation
```

### Swagger API-DOCS:

You can view the API docss [here](http://localhost:8080/imagestore-api-docs)

```
    http://localhost:8080/imagestore-api-docs
```

### Spring Boot Actuator:

Spring boot actuator [here](http://localhost:8080/actuator)

```
    http://localhost:8080/actuator
    http://localhost:8080/actuator/health
```

### Monitor and Tracing

For monitoring and telemetry, I have added used OpenTelemetry together with Jaeger UI
You can access Jaeger UI [here](http://localhost:16686/search)

```
    http://localhost:16686/search
```

## ENV Variables:

You can update the below variables in docker-compose.yml to make changes.

- MAX_FILE_SIZE: Maximum size for image in long
- SERVER_URL: Base url of the imagestore-server
- SECRET_KEY: Secret for JWT generation
- JWT_EXP_KEY: Access token expiration time in Long
- REF_TOKEN_EXP: Refresh token expiration in Long
- SPRING_PROFILES_ACTIVE: Profile for the springboot app
- SPRING_DATASOURCE_URL: DB connection url
- SPRING_DATASOURCE_USERNAME: DB username
- SPRING_DATASOURCE_PASSWORD: DB password
- FILE_PATH: Filepath for storing image

### Note: I have intentionally kept the db to drop and start the service every time it restarts.

## Things that I want to add more to the app:

- Logout API && Token blacklisting
- Tests for Security related classes
- A more generalized approach to handling the exceptions
- OAuth2 feature
- Image compression and decompression

### ImageStore-WebApp

- Built in React.js
- A basic UI is set up for the experience of ImageStore-App
- The imagestore-webapp uses React.js coupled with Redux-Saga for statement and axios as http client.
- There is so much more to do with the webapp like the way data is handled and the styling, etc.
- I tried to come up with as much as I could in this short span. Hope it's alright.

### Build and Run Webapp + Server

To build and run both front end and back end,
you will have to run docker compose up on the root folder of repo `imagestore-app/`
The docker compose with build the image of the webapp + server + other necessary services and run it as a container with shared network.

#### Steps:

1. Build the server first by running the below command in the folder `imagestore-server`

   ```
     ./gradlew clean build
   ```

2. Run the below command in the root / `imagestore-app`

```
    docker compose up
```

Once this is done, you should be able to access UI [here](http://localhost:3000/login)

```
  http://localhost:3000/login
```

## Contact

Please feel free to get connect if needed:

```
 sanjay.suresh0695@gmail.com
```
