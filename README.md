## Chirp

**Chirp** is an Android chat application built with Kotlin. It features real-time messaging with friends through a backend API built using Ktor (also in Kotlin) and a MongoDB database for data storage.

> 🚧 **Work in Progress**

### Getting Started

#### 1. Prepare the database
```bash
docker build -t custom-mongo .
docker run -d -p 27017:27017 --name mongo-server custom-mongo
```
#### 2. Configure the API in the mobile app
In `constants.kt`, change `URL_REST` to match rest api url
### Screenshots

#### Registration
<img src="https://github.com/user-attachments/assets/b2dce398-e49d-44a2-8c09-d3b9964f86a1" alt="image" width="200"/>

#### Login
<img src="https://github.com/user-attachments/assets/39054977-e857-4c78-99f9-498d45510fef" alt="image" width="200"/>

#### Input validation
<img src="https://github.com/user-attachments/assets/8179d80e-18ac-43a7-9261-e701dacfc01d" alt="image" width="200"/>

#### Menu
<img src="https://github.com/user-attachments/assets/293ca5c3-4c7e-45de-aaac-68cc8ed25562" alt="image" width="200"/>

#### Browse Friend list
<img src="https://github.com/user-attachments/assets/4bb69b61-87c4-47d0-b964-e7c4d077e8cf" alt="image" width="200"/>

#### Add friend
<img src="https://github.com/user-attachments/assets/4cdeb242-e10e-451f-b975-42b16f7a597c" alt="image" width="200"/>

#### Browse requests
<img src="https://github.com/user-attachments/assets/a76390d0-6273-42f6-b7dc-a0f737afd356" alt="image" width="200"/>


