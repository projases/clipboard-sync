# 📋 Clipboard Sync

Cross-device clipboard syncing with Spring Boot. Share clipboard content between your phone and computer over the internet.

## 🚀 Features

- ✅ Share clipboard between devices
- ✅ Works over internet (not just LAN)
- ✅ Simple REST API
- ✅ Web interface (works on any device)
- ✅ Clipboard history
- ✅ API key authentication

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.2.3 (Java 17)
- **Database**: H2 (file-based)
- **Build**: Maven
- **Dev Environment**: Nix Flakes

## 📦 Development Setup

### Prerequisites

- Nix with flakes enabled
- Git

### Getting Started

1. **Clone the repository:**
```bash
   git clone https://github.com/projases/clipboard-sync.git
   cd clipboard-sync
```

2. **Enter the Nix development shell:**
```bash
   nix develop
```
   
   Or if using direnv:
```bash
   direnv allow
```

3. **Run the application:**
```bash
   mvn spring-boot:run
```

4. **Access the web interface:**
   - Local: http://localhost:8080/
   - API Health Check: http://localhost:8080/api/health

## 🔧 Configuration

Edit `src/main/resources/application.properties`:
```properties
# Generate a secure API key
app.api.key=your-secure-key-here
```

Generate a key:
```bash
openssl rand -hex 32
```

## 📡 API Endpoints

All endpoints require `Authorization: Bearer YOUR_API_KEY` header.

- **POST** `/api/copy` - Send clipboard content
```json
  {"content": "your text here"}
```

- **GET** `/api/latest` - Get most recent clipboard item

- **GET** `/api/history` - Get all clipboard items

- **GET** `/api/health` - Health check (no auth)

## 🧪 Testing
```bash
# Run tests
mvn test

# Build
mvn clean package

# Test API with curl
curl -X POST http://localhost:8080/api/copy \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_KEY" \
  -d '{"content":"test"}'
```

## 🏗️ Project Structure
```
clipboard-sync/
├── flake.nix              # Nix development environment
├── pom.xml                # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/yourname/clipboard/
│   │   │   ├── controller/    # REST endpoints
│   │   │   ├── service/       # Business logic
│   │   │   ├── repository/    # Database access
│   │   │   └── model/         # Data models
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           └── index.html # Web interface
│   └── test/
└── data/                  # H2 database files (gitignored)
```

## 🚀 Deployment

Coming soon: Instructions for deploying to Render.com or similar platforms.

## 📝 License

MIT License - feel free to use this for learning!

## 🤝 Contributing

This is a learning project, but suggestions are welcome!# clipboard-sync
Cross-device clipboard syncing with Spring Boot
# clipboard-sync
Cross-device clipboard syncing with Spring Boot
