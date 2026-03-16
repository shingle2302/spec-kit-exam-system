#!/bin/bash

# 监控脚本 - 监控系统运行状态

# 配置
ALERT_EMAIL="admin@example.com"
LOG_FILE="/var/log/exam-system/monitor.log"
THRESHOLD_CPU=80
THRESHOLD_MEMORY=80
THRESHOLD_DISK=90

# 函数：发送告警
send_alert() {
    local subject=$1
    local message=$2
    echo "$message" | mail -s "$subject" $ALERT_EMAIL
    echo "[$(date)] ALERT: $subject - $message" >> $LOG_FILE
}

# 函数：检查服务状态
check_services() {
    echo "[$(date)] Checking services..." >> $LOG_FILE
    
    # 检查后端服务
    BACKEND_STATUS=$(curl -s http://localhost:8080/actuator/health | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    if [ "$BACKEND_STATUS" != "UP" ]; then
        send_alert "Backend Service Down" "Backend service is not responding properly. Status: $BACKEND_STATUS"
    fi
    
    # 检查前端服务
    FRONTEND_STATUS=$(curl -s http://localhost/health)
    if [ "$FRONTEND_STATUS" != "healthy" ]; then
        send_alert "Frontend Service Down" "Frontend service is not responding properly."
    fi
    
    # 检查数据库
    DB_STATUS=$(docker exec exam-postgres pg_isready -U exam_user 2>&1)
    if ! echo "$DB_STATUS" | grep -q "accepting connections"; then
        send_alert "Database Service Down" "Database service is not responding properly."
    fi
    
    # 检查Redis
    REDIS_STATUS=$(docker exec exam-redis redis-cli ping 2>&1)
    if [ "$REDIS_STATUS" != "PONG" ]; then
        send_alert "Redis Service Down" "Redis service is not responding properly."
    fi
}

# 函数：检查系统资源
check_resources() {
    echo "[$(date)] Checking system resources..." >> $LOG_FILE
    
    # 检查CPU使用率
    CPU_USAGE=$(top -bn1 | grep "Cpu(s)" | sed "s/.*, *\([0-9.]*\)%* id.*/\1/" | awk '{print 100 - $1}')
    CPU_USAGE_INT=${CPU_USAGE%.*}
    if [ $CPU_USAGE_INT -gt $THRESHOLD_CPU ]; then
        send_alert "High CPU Usage" "CPU usage is ${CPU_USAGE}%"
    fi
    
    # 检查内存使用率
    MEMORY_USAGE=$(free | grep Mem | awk '{printf "%.0f", $3/$2 * 100.0}')
    if [ $MEMORY_USAGE -gt $THRESHOLD_MEMORY ]; then
        send_alert "High Memory Usage" "Memory usage is ${MEMORY_USAGE}%"
    fi
    
    # 检查磁盘使用率
    DISK_USAGE=$(df -h / | awk 'NR==2 {print $5}' | sed 's/%//')
    if [ $DISK_USAGE -gt $THRESHOLD_DISK ]; then
        send_alert "High Disk Usage" "Disk usage is ${DISK_USAGE}%"
    fi
}

# 函数：检查应用日志
check_logs() {
    echo "[$(date)] Checking application logs..." >> $LOG_FILE
    
    # 检查错误日志
    ERROR_COUNT=$(docker-compose logs --tail=100 backend 2>&1 | grep -i "error" | wc -l)
    if [ $ERROR_COUNT -gt 10 ]; then
        send_alert "High Error Rate" "Found $ERROR_COUNT errors in backend logs"
    fi
    
    # 检查异常日志
    EXCEPTION_COUNT=$(docker-compose logs --tail=100 backend 2>&1 | grep -i "exception" | wc -l)
    if [ $EXCEPTION_COUNT -gt 5 ]; then
        send_alert "High Exception Rate" "Found $EXCEPTION_COUNT exceptions in backend logs"
    fi
}

# 函数：检查数据库连接
check_database() {
    echo "[$(date)] Checking database connections..." >> $LOG_FILE
    
    # 检查活跃连接数
    ACTIVE_CONNECTIONS=$(docker exec exam-postgres psql -U exam_user -d exam_system -t -c "SELECT count(*) FROM pg_stat_activity WHERE state = 'active';")
    if [ $ACTIVE_CONNECTIONS -gt 100 ]; then
        send_alert "High Database Connections" "Active database connections: $ACTIVE_CONNECTIONS"
    fi
    
    # 检查慢查询
    SLOW_QUERIES=$(docker exec exam-postgres psql -U exam_user -d exam_system -t -c "SELECT count(*) FROM pg_stat_statements WHERE mean_exec_time > 1000;")
    if [ $SLOW_QUERIES -gt 10 ]; then
        send_alert "Slow Queries Detected" "Found $SLOW_QUERIES slow queries in database"
    fi
}

# 函数：生成监控报告
generate_report() {
    echo "[$(date)] Generating monitoring report..." >> $LOG_FILE
    
    REPORT_FILE="/var/log/exam-system/report_$(date +%Y%m%d_%H%M%S).txt"
    
    {
        echo "=== 系统监控报告 ==="
        echo "生成时间: $(date)"
        echo ""
        echo "=== 服务状态 ==="
        echo "后端服务: $(curl -s http://localhost:8080/actuator/health | grep -o '"status":"[^"]*"')"
        echo "前端服务: $(curl -s http://localhost/health)"
        echo "数据库: $(docker exec exam-postgres pg_isready -U exam_user)"
        echo "Redis: $(docker exec exam-redis redis-cli ping)"
        echo ""
        echo "=== 系统资源 ==="
        echo "CPU使用率: $CPU_USAGE%"
        echo "内存使用率: $MEMORY_USAGE%"
        echo "磁盘使用率: $DISK_USAGE%"
        echo ""
        echo "=== 数据库统计 ==="
        echo "活跃连接数: $ACTIVE_CONNECTIONS"
        echo "慢查询数: $SLOW_QUERIES"
    } > $REPORT_FILE
    
    echo "Report generated: $REPORT_FILE" >> $LOG_FILE
}

# 主循环
main() {
    echo "[$(date)] Monitoring started" >> $LOG_FILE
    
    while true; do
        check_services
        check_resources
        check_logs
        check_database
        generate_report
        
        # 每5分钟检查一次
        sleep 300
    done
}

# 启动监控
main