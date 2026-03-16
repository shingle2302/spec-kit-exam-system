# 部署指南

本指南将帮助您将考试系统部署到生产环境。

## 📋 部署前准备

### 系统要求
- **操作系统**：Linux (Ubuntu 20.04+ 推荐)
- **内存**：最少 4GB，推荐 8GB+
- **存储**：最少 20GB，推荐 50GB+
- **CPU**：最少 2核，推荐 4核+

### 软件要求
- **Docker**：20.10+
- **Docker Compose**：2.0+
- **Git**：2.0+

## 🚀 快速部署

### 1. 克隆项目
```bash
git clone <repository-url>
cd spec-kit-exam-system
```

### 2. 配置环境变量
```bash
cp .env.example .env
# 编辑.env文件，修改敏感信息
nano .env
```

**重要**：请务必修改以下敏感配置：
```bash
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production-min-32-chars
ENCRYPTION_KEY=your-super-secret-encryption-key-change-this-in-production-min-32-chars
SPRING_DATASOURCE_PASSWORD=your-strong-database-password
```

### 3. 启动服务
```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

### 4. 验证部署
```bash
# 检查后端健康状态
curl http://localhost:8080/actuator/health

# 检查前端健康状态
curl http://localhost/health

# 检查数据库连接
docker-compose exec postgres pg_isready -U exam_user

# 检查Redis连接
docker-compose exec redis redis-cli ping
```

## 📊 服务说明

### 服务架构
```
┌─────────────┐
│   Nginx     │ (端口 80)
│  (Frontend) │
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  Spring Boot│ (端口 8080)
│  (Backend)  │
└──────┬──────┘
       │
       ├──────────────┐
       ↓              ↓
┌─────────────┐ ┌─────────────┐
│ PostgreSQL  │ │   Redis     │
│  (端口 5432)│ │  (端口 6379) │
└─────────────┘ └─────────────┘
```

### 服务端口
- **前端**：80 (HTTP)
- **后端**：8080 (HTTP)
- **数据库**：5432 (PostgreSQL)
- **缓存**：6379 (Redis)

## 🔧 高级配置

### 数据库备份
```bash
# 创建备份
docker-compose exec postgres pg_dump -U exam_user exam_system > backup.sql

# 恢复备份
docker-compose exec -T postgres psql -U exam_user exam_system < backup.sql

# 自动备份脚本
cat > backup.sh << 'EOF'
#!/bin/bash
BACKUP_DIR="/backups"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/exam_system_$DATE.sql"

mkdir -p $BACKUP_DIR
docker-compose exec -T postgres pg_dump -U exam_user exam_system > $BACKUP_FILE

# 保留最近7天的备份
find $BACKUP_DIR -name "exam_system_*.sql" -mtime +7 -delete
EOF

chmod +x backup.sh
```

### SSL/HTTPS配置
```bash
# 使用Let's Encrypt获取免费SSL证书
sudo apt-get install certbot

# 获取证书
sudo certbot certonly --standalone -d your-domain.com

# 修改nginx配置
cat > nginx-ssl.conf << 'EOF'
server {
    listen 443 ssl http2;
    server_name your-domain.com;

    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;

    # SSL配置
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # 其他配置...
}

server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}
EOF
```

### 性能优化
```bash
# 调整PostgreSQL配置
cat > postgresql.conf << 'EOF'
# 内存配置
shared_buffers = 256MB
effective_cache_size = 1GB
maintenance_work_mem = 64MB
checkpoint_completion_target = 0.9
wal_buffers = 16MB
default_statistics_target = 100
random_page_cost = 1.1
effective_io_concurrency = 200
work_mem = 2621kB
min_wal_size = 1GB
max_wal_size = 4GB

# 连接配置
max_connections = 100
EOF

# 调整Redis配置
cat > redis.conf << 'EOF'
maxmemory 512mb
maxmemory-policy allkeys-lru
save 900 1
save 300 10
save 60 10000
EOF
```

## 🔍 监控和日志

### 查看日志
```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres

# 查看最近100行日志
docker-compose logs --tail=100 backend
```

### 健康检查
```bash
# 检查所有服务健康状态
docker-compose ps

# 检查后端健康状态
curl http://localhost:8080/actuator/health

# 检查应用指标
curl http://localhost:8080/actuator/metrics
```

### 性能监控
```bash
# 查看资源使用情况
docker stats

# 查看容器详情
docker inspect exam-backend

# 查看数据库连接数
docker-compose exec postgres psql -U exam_user -d exam_system -c "SELECT count(*) FROM pg_stat_activity;"
```

## 🛠️ 维护操作

### 更新应用
```bash
# 拉取最新代码
git pull origin main

# 重新构建并启动
docker-compose up -d --build

# 清理旧镜像
docker image prune -f
```

### 扩展服务
```bash
# 扩展后端服务实例
docker-compose up -d --scale backend=3

# 扩展前端服务实例
docker-compose up -d --scale frontend=2
```

### 数据迁移
```bash
# 执行数据库迁移
docker-compose exec backend java -jar app.jar --spring.profiles.active=migration

# 备份当前数据
docker-compose exec postgres pg_dump -U exam_user exam_system > backup_before_migration.sql
```

## 🔒 安全加固

### 防火墙配置
```bash
# 配置UFW防火墙
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 22/tcp
sudo ufw enable

# 限制数据库访问
sudo ufw deny 5432
sudo ufw deny 6379
```

### 定期更新
```bash
# 更新系统
sudo apt-get update && sudo apt-get upgrade -y

# 更新Docker
sudo apt-get install docker-ce docker-ce-cli containerd.io

# 更新Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### 安全扫描
```bash
# 扫描镜像漏洞
docker scan exam-backend:latest
docker scan exam-frontend:latest

# 检查容器安全
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
  -v /usr/local/bin/docker:/usr/local/bin/docker \
  aquasec/trivy image exam-backend:latest
```

## 🚨 故障排查

### 常见问题

#### 1. 服务无法启动
```bash
# 检查端口占用
sudo netstat -tulpn | grep :8080

# 检查日志
docker-compose logs backend

# 检查资源使用
docker stats
```

#### 2. 数据库连接失败
```bash
# 检查数据库状态
docker-compose ps postgres

# 检查数据库日志
docker-compose logs postgres

# 测试数据库连接
docker-compose exec postgres psql -U exam_user -d exam_system
```

#### 3. 内存不足
```bash
# 检查内存使用
free -h

# 清理Docker缓存
docker system prune -a

# 增加swap空间
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
```

#### 4. 性能问题
```bash
# 检查慢查询
docker-compose exec postgres psql -U exam_user -d exam_system -c "SELECT * FROM pg_stat_statements ORDER BY mean_exec_time DESC LIMIT 10;"

# 检查Redis性能
docker-compose exec redis redis-cli info stats

# 分析应用日志
docker-compose logs backend | grep "ERROR"
```

## 📞 技术支持

### 获取帮助
- **文档**：查看项目README.md
- **问题反馈**：提交GitHub Issue
- **邮件支持**：support@example.com

### 应急联系
- **技术负责人**：tech-lead@example.com
- **运维团队**：ops-team@example.com
- **紧急热线**：+86-xxx-xxxx-xxxx

---

<div align="center">
  <strong>🎯 部署完成后，请及时修改默认密码和配置！</strong>
</div>