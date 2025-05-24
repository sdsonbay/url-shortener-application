# URL Shortener Application on Google Cloud Platform (GCP)

This project is a scalable, containerized URL shortener system built with **Spring Boot**, **React**, **Docker**, and **Google Cloud Platform** services. It supports URL shortening and redirection. It has been fully deployed to the cloud with performance testing and autoscaling support.

---

## Tech Stack

| Layer         | Technology |
|---------------|------------|
| Frontend      | React (GKE Pod) |
| Backend       | Spring Boot (GKE Pod) |
| Database      | PostgreSQL (Dockerized on GCE VM) |
| Serverless Functions    | Cloud Functions (shorten & project information) |
| Container Registry | Artifact Registry |
| Orchestration | Google Kubernetes Engine (GKE) |
| Load Testing  | Locust |
| Autoscaling   | Horizontal Pod Autoscaler (HPA) |

---

## Cloud Architecture Overview

User -> React -> Spring Boot -> Cloud Function -> Spring Boot -> PostgreSQL -> Spring Boot -> React -> User

---

## Deployment Guide

### 1. PostgreSQL on Compute Engine (Docker)

1. Create a VM (e2-micro/e2-medium, Debian/Ubuntu)
2. SSH into it
   ```bash
   gcloud compute ssh your-vm-name --zone=your-zone
   ```
3. Install Docker
   ```bash
   sudo apt update && sudo apt install docker.io -y
   sudo systemctl start docker && sudo systemctl enable docker
   ```
4. Run PostgreSQL
   ```bash
   sudo docker run --name postgres \
   -e POSTGRES_USER=postgres \
   -e POSTGRES_PASSWORD=postgres \
   -e POSTGRES_DB=url_shortener_db \
   -p 5432:5432 \
   -v postgres-data:/var/lib/postgresql/data \
   -d postgres:15-alpine
   ```

### 2. Artifact Registry

1. Create repository (Docker type)
2. Authenticate
   ```bash
   gcloud auth configure-docker
   ```
3. Build & push images

   Backend:
   
   ```bash
   mvn clean install
   docker build -t urlshortener-be .
   docker buildx inspect --bootstrap
   docker buildx build --platform linux/amd64 -t us-central1-docker.pkg.dev/urlshortener-project-460318/url-shortener-repo/urlshortener-backend:latest --push .
   ```
   
   Frontend:
   
   ```bash
   docker buildx build --platform linux/amd64 -t us-central1-docker.pkg.dev/urlshortener-project-460318/url-shortener-repo/urlshortener-frontend:latest --push .
   ```

### 3. GKE Cluster Setup

  ```bash
   gcloud container clusters create url-shortener-cluster \
   --zone=us-central1-a --num-nodes=3
   gcloud container clusters get-credentials url-shortener-cluster --zone=us-central1-a
   ```

### 4. Deploy to Kubernetes

  ```bash
  kubectl apply -f k8s/backend-deployment.yaml
  kubectl apply -f k8s/backend-service.yaml
  kubectl apply -f k8s/backend-hpa.yaml
  kubectl apply -f k8s/frontend-deployment.yaml
  kubectl apply -f k8s/frontend-service.yaml
  kubectl apply -f k8s/frontend-hpa.yaml
  kubectl apply -f k8s/ingress.yaml
  ```

- Ensure application.yaml in backend points to PostgreSQL VM’s IP: spring.datasource.url=jdbc:postgresql://<VM-IP>:5432/url_shortener_db

### 5. Cloud Functions

- URL Shortener Function

  ```js
  const crypto = require('crypto');

  exports.shortenUrl = (req, res) => {
  const longUrl = req.body.url;

  if (!longUrl) {
    return res.status(400).json({ error: 'Missing "url" in request body' });
  }

  const hash = crypto.createHash('sha256').update(longUrl).digest();
  const shortCode = hash.toString('base64url').substring(0, 8);

    res.status(200).json({
      longUrl: longUrl,
      shortUrl: shortCode,
    });
  };
  ```

  ```json
  {
    "name": "shorten-url-function",
    "version": "1.0.0",
    "main": "index.js",
    "dependencies": {}
  }
  ```

  ```bash
  gcloud functions deploy shortenUrl \
  --runtime=nodejs20 \
  --trigger-http \
  --allow-unauthenticated
  ```

### 6. Locust Performance Testing

- Create virtual environment

  ```bash
  python3 -m venv locust-env
  source locust-env/bin/activate
  pip install locust
  ```

- Run test

  ```bash
  locust -f locust/locustfile.py -H http://<backend-loadbalancer-ip>
  ```

- Visit http://localhost:8089 on terminal

### Autoscaling

- Check scaling using:

  ```bash
  kubectl get hpa
  ```
  
- Check running pods using:

  ```bash
  kubectl get pods
  ```
