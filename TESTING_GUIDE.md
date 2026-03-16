# 测试指南

本项目包含前后端单元测试，用于确保系统功能的正确性和稳定性。

## 后端测试

### 运行后端测试

1. **进入后端目录**：
   ```bash
   cd backend
   ```

2. **运行测试**：
   ```bash
   mvn test
   ```

3. **查看测试报告**：
   测试结果将显示在控制台，详细报告位于 `target/surefire-reports` 目录。

### 后端测试覆盖范围

- **AuthServiceTest**：测试用户认证相关功能
- **UserServiceTest**：测试用户管理相关功能
- **RoleServiceTest**：测试角色管理相关功能
- **PermissionServiceImplTest**：测试权限管理相关功能
- **ExamWorkflowServiceTest**：测试考试工作流相关功能
- **ExamPlanServiceTest**：测试考试计划相关功能（已存在）

## 前端测试

### 运行前端测试

1. **进入前端目录**：
   ```bash
   cd frontend
   ```

2. **安装依赖**（如果尚未安装）：
   ```bash
   npm install
   ```

3. **运行测试**：
   ```bash
   npm test
   ```

4. **运行测试并监视文件变化**：
   ```bash
   npm run test:watch
   ```

### 前端测试覆盖范围

- **authService.test.ts**：测试认证服务相关功能
- **userService.test.ts**：测试用户服务相关功能
- **auth.test.ts**：测试认证状态管理相关功能

## 测试最佳实践

1. **测试隔离**：每个测试应该独立运行，不依赖于其他测试的状态
2. **测试覆盖**：确保测试覆盖主要功能和边界情况
3. **测试命名**：使用清晰的命名方式，描述测试的目的
4. **测试数据**：使用模拟数据，避免依赖真实数据
5. **测试断言**：使用明确的断言，验证测试结果

## 测试工具

- **后端**：JUnit 5 + Mockito
- **前端**：Jest + Vue Test Utils

## 测试覆盖率

运行测试后，可以查看测试覆盖率报告，了解测试覆盖情况。

### 后端覆盖率

```bash
mvn jacoco:report
```

报告将生成在 `target/site/jacoco` 目录。

### 前端覆盖率

```bash
npm test -- --coverage
```

报告将生成在 `coverage` 目录。