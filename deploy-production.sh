#!/bin/bash

# 生产环境部署脚本

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置变量
PROJECT_DIR="/opt/exam-system"
BACKUP_DIR="/opt/backups/exam-system"
LOG_DIR="/var/log/exam-system"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  考试系统生产环境部署脚本${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# 检查是否为root用户
if [ "$EUID" -ne 0 ]; then 
    echo -e "${RED}请使用root用户运行此脚本${NC}"
    exit 1
fi

# 1. 环境检查
echo -e "${YELLOW}[1/10] 环境检查...${NC}"

# 检查Docker
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker未安装，正在安装...${NC}"
    curl -fsSL https://get.docker.com | sh
    systemctl enable docker
    systemctl start docker
else
    echo -e "${GREEN}✓ Docker已安装${NC}"
fi

# 检查Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Docker Compose未安装，正在安装...${NC}"
    curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
else
    echo -e "${GREEN}✓ Docker Compose已安装${NC}"
fi

# 检查Git
if ! command -v git &> /dev/null; then
    echo -e "${RED}Git未安装，正在安装...${NC}"
    apt-get update && apt-get install -y git
else
    echo -e "${GREEN}✓ Git已安装${NC}"
fi

# 2. 创建必要的目录
echo -e "${YELLOW}[2/10] 创建目录结构...${NC}"
mkdir -p $PROJECT_DIR
mkdir -p $BACKUP_DIR
mkdir -p $LOG_DIR
mkdir -p /opt/exam-system/data/postgres
mkdir -p /opt/exam-system/data/redis
echo -e "${GREEN}✓ 目录创建完成${NC}"

# 3. 备份现有数据
echo -e "${YELLOW}[3/10] 备份现有数据...${NC}"
if [ -f "$PROJECT_DIR/docker-compose.yml" ]; then
    cd $PROJECT_DIR
    docker-compose exec -T postgres pg_dump -U exam_user exam_system > $BACKUP_DIR/backup_$TIMESTAMP.sql
    echo -e "${GREEN}✓ 数据备份完成: $BACKUP_DIR/backup_$TIMESTAMP.sql${NC}"
else
    echo -e "${YELLOW}⚠ 首次部署，无需备份${NC}"
fi

# 4. 拉取最新代码
echo -e "${YELLOW}[4/10] 拉取最新代码...${NC}"
if [ -d "$PROJECT_DIR/.git" ]; then
    cd $PROJECT_DIR
    git fetch origin
    git pull origin main
else
    git clone <repository-url> $PROJECT_DIR
    cd $PROJECT_DIR
fi
echo -e "${GREEN}✓ 代码更新完成${NC}"

# 5. 配置环境变量
echo -e "${YELLOW}[5/10] 配置环境变量...${NC}"
if [ ! -f "$PROJECT_DIR/.env" ]; then
    cp $PROJECT_DIR/.env.example $PROJECT_DIR/.env
    
    # 生成随机密钥
    JWT_SECRET=$(openssl rand -base64 32)
    ENCRYPTION_KEY=$(openssl rand -base64 32)
    DB_PASSWORD=$(openssl rand -base64 16)
    
    sed -i "s/your-super-secret-jwt-key-change-this-in-production-min-32-chars/$JWT_SECRET/g" $PROJECT_DIR/.env
    sed -i "s/your-super-secret-encryption-key-change-this-in-production-min-32-chars/$ENCRYPTION_KEY/g" $PROJECT_DIR/.env
    sed -i "s/exam_password/$DB_PASSWORD/g" $PROJECT_DIR/.env
    
    echo -e "${GREEN}✓ 环境变量配置完成${NC}"
    echo -e "${YELLOW}⚠ 请检查并修改 $PROJECT_DIR/.env 中的配置${NC}"
else
    echo -e "${GREEN}✓ 环境变量已存在${NC}"
fi

# 6. 构建Docker镜像
echo -e "${YELLOW}[6/10] 构建Docker镜像...${NC}"
cd $PROJECT_DIR
docker-compose build --no-cache
echo -e "${GREEN}✓ Docker镜像构建完成${NC}"

# 7. 停止旧服务
echo -e "${YELLOW}[7/10] 停止旧服务...${NC}"
if docker-compose ps | grep -q "Up"; then
    docker-compose down
    echo -e "${GREEN}✓ 旧服务已停止${NC}"
else
    echo -e "${YELLOW}⚠ 没有运行中的服务${NC}"
fi

# 8. 启动新服务
echo -e "${YELLOW}[8/10] 启动新服务...${NC}"
docker-compose up -d
echo -e "${GREEN}✓ 新服务已启动${NC}"

# 9. 健康检查
echo -e "${YELLOW}[9/10] 健康检查...${NC}"
sleep 30

# 检查后端服务
BACKEND_HEALTH=$(curl -s http://localhost:8080/actuator/health || echo "failed")
if echo "$BACKEND_HEALTH" | grep -q "UP"; then
    echo -e "${GREEN}✓ 后端服务健康${NC}"
else
    echo -e "${RED}✗ 后端服务异常${NC}"
    docker-compose logs backend
    exit 1
fi

# 检查前端服务
FRONTEND_HEALTH=$(curl -s http://localhost/health || echo "failed")
if echo "$FRONTEND_HEALTH" | grep -q "healthy"; then
    echo -e "${GREEN}✓ 前端服务健康${NC}"
else
    echo -e "${RED}✗ 前端服务异常${NC}"
    docker-compose logs frontend
    exit 1
fi

# 检查数据库
DB_HEALTH=$(docker-compose exec -T postgres pg_isready -U exam_user || echo "failed")
if echo "$DB_HEALTH" | grep -q "accepting connections"; then
    echo -e "${GREEN}✓ 数据库服务健康${NC}"
else
    echo -e "${RED}✗ 数据库服务异常${NC}"
    docker-compose logs postgres
    exit 1
fi

# 检查Redis
REDIS_HEALTH=$(docker-compose exec -T redis redis-cli ping || echo "failed")
if [ "$REDIS_HEALTH" = "PONG" ]; then
    echo -e "${GREEN}✓ Redis服务健康${NC}"
else
    echo -e "${RED}✗ Redis服务异常${NC}"
    docker-compose logs redis
    exit 1
fi

# 10. 配置防火墙
echo -e "${YELLOW}[10/10] 配置防火墙...${NC}"
if command -v ufw &> /dev/null; then
    ufw allow 80/tcp
    ufw allow 443/tcp
    ufw allow 22/tcp
    ufw deny 5432
    ufw deny 6379
    echo -e "${GREEN}✓ 防火墙配置完成${NC}"
else
    echo -e "${YELLOW}⚠ UFW未安装，跳过防火墙配置${NC}"
fi

# 部署完成
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  部署完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "服务地址:"
echo -e "  前端: http://$(hostname -I | awk '{print $1}')"
echo -e "  后端: http://$(hostname -I | awk '{print $1}'):8080"
echo ""
echo -e "管理命令:"
echo -e "  查看日志: docker-compose logs -f"
echo -e "  查看状态: docker-compose ps"
echo -e "  停止服务: docker-compose down"
echo -e "  重启服务: docker-compose restart"
echo ""
echo -e "备份位置: $BACKUP_DIR"
echo -e "日志位置: $LOG_DIR"
echo ""
echo -e "${YELLOW}⚠ 重要提醒:${NC}"
echo -e "1. 请修改 $PROJECT_DIR/.env 中的敏感配置"
echo -e "2. 请配置SSL证书以启用HTTPS"
echo -e "3. 请定期备份数据库"
echo -e "4. 请监控系统运行状态"
echo ""