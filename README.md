# Supabase Java Web Application

A simple Java web application with authentication using Supabase PostgreSQL.

## Local Development

1. Clone the repository
2. Copy `.env.example` to `.env`
3. Update `.env` with your Supabase credentials
4. Run: `mvn clean tomcat7:run`
5. Access: http://localhost:8080

## Deployment to Render

### Prerequisites
1. GitHub account with this repository
2. Render account (free tier available)
3. Supabase account with PostgreSQL database

### Steps

#### Option 1: Deploy with Render PostgreSQL (Recommended for beginners)
1. Push code to GitHub
2. Go to [Render Dashboard](https://dashboard.render.com)
3. Click "New +" → "Web Service"
4. Connect your GitHub repository
5. Configure:
    - **Name:** your-app-name
    - **Environment:** Docker
    - **Region:** Choose closest
    - **Branch:** main
    - **Plan:** Free
6. Click "Create Web Service"
7. Render will automatically detect Dockerfile and deploy

#### Option 2: Use Supabase PostgreSQL
1. Follow Option 1 steps 1-6
2. Before creating, add environment variables: