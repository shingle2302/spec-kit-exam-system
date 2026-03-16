#!/bin/bash

# 第一阶段测试运行脚本

echo "🧪 开始运行第一阶段测试..."

# 设置颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 测试计数器
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# 运行单元测试
echo -e "${BLUE}📋 运行单元测试...${NC}"
mvn test -Dtest=DataPermissionAspectTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 数据权限切面测试通过${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}❌ 数据权限切面测试失败${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

mvn test -Dtest=JwtUtilEnhancedTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ JWT工具测试通过${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}❌ JWT工具测试失败${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

mvn test -Dtest=DataEncryptUtilTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 数据加密工具测试通过${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}❌ 数据加密工具测试失败${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

# 运行集成测试
echo -e "${BLUE}🔗 运行集成测试...${NC}"
mvn test -Dtest=AuthIntegrationTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 认证集成测试通过${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}❌ 认证集成测试失败${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

# 运行性能测试
echo -e "${BLUE}⚡ 运行性能测试...${NC}"
mvn test -Dtest=PerformanceTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 性能测试通过${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}❌ 性能测试失败${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

# 运行安全测试
echo -e "${BLUE}🔒 运行安全测试...${NC}"
mvn test -Dtest=SecurityTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 安全测试通过${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}❌ 安全测试失败${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

# 运行服务测试
echo -e "${BLUE}🧪 运行服务测试...${NC}"
mvn test -Dtest=AuthServiceTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 认证服务测试通过${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}❌ 认证服务测试失败${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

mvn test -Dtest=UserServiceTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 用户服务测试通过${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}❌ 用户服务测试失败${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

mvn test -Dtest=OperationLogServiceTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 操作日志服务测试通过${NC}"
    ((PASSED_TESTS++))
else
    echo -e "${RED}❌ 操作日志服务测试失败${NC}"
    ((FAILED_TESTS++))
fi
((TOTAL_TESTS++))

# 测试总结
echo ""
echo -e "${YELLOW}📊 测试总结${NC}"
echo -e "总测试数: ${TOTAL_TESTS}"
echo -e "${GREEN}通过: ${PASSED_TESTS}${NC}"
echo -e "${RED}失败: ${FAILED_TESTS}${NC}"
echo -e "通过率: $(awk "BEGIN {printf \"%.1f\", (${PASSED_TESTS}/${TOTAL_TESTS})*100}")%"

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}🎉 所有测试通过！${NC}"
    exit 0
else
    echo -e "${RED}❌ 有测试失败，请检查日志${NC}"
    exit 1
fi