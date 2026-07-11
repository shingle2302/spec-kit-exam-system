# ADR-002: 工作区隔离技术方案

## 状态

**已通过** — 2026-07-11

## 上下文

多个 agent 共享同一 filesystem workspace（`/home/shingle2302/桌面`），导致各 agent 的运行相互覆写文件。SeniorEngineer 在 [CMP-43](/CMP/issues/CMP-43) 上的工作反复被其他 agent 的运行覆写。

需要评估以下三种方案的可行性：

1. **Git Worktree** — 通过 `git worktree add` 创建链接工作树
2. **独立目录** — 每个 agent 一个完整 Git 克隆
3. **Paperclip Execution Workspace** — 使用 Paperclip 的内置 workspace 管理

## 方案评估

### 方案一：Git Worktree

| 维度 | 评估 |
|------|------|
| **隔离性** | 文件隔离完全。共享 `.git` 目录，但不共享工作区文件 |
| **磁盘占用** | 低 — 共享 objects/pack，增量占空间小 |
| **同步机制** | Git 原生。一个 worktree 提交后，其他 worktree 可通过 `git pull` 获取 |
| **分支管理** | 每个 worktree 可 checkout 不同分支，但共享同一个 remote/origin |
| **复杂度** | 中 — 需要初始 `git worktree add` 配置，删除/清理 worktree 需额外命令 |
| **风险** | 同一 `.git` 目录下多个 worktree 的操作可能互相影响（如 `git gc`） |

**结论：** 可行性中。对于纯 Git 操作是好的方案，但当前各 agent 并非都在同一台机器上用同一 Git 仓库；未来如果 agents 分布在 Docker 容器中，worktree 方案无法扩展。

### 方案二：独立目录（完整克隆）— **推荐**

| 维度 | 评估 |
|------|------|
| **隔离性** | 完全隔离。每个 agent 拥有独立的 `.git` 和 working directory |
| **磁盘占用** | 中 — 每份克隆独立存储 objects，但现代 Git 有 `--reference` / `alternates` 可优化 |
| **同步机制** | `git push/pull origin` — 标准 Git 工作流，agent 无额外学习成本 |
| **分支管理** | 各 agent 完全独立管理分支 |
| **复杂度** | 低 — 每 agent 一次 `git clone`，日常工作流不变 |
| **风险** | 极低 — 纯标准 Git 操作，无特殊机制 |

**结论：** 推荐方案。已在 worktrees 目录中预实施（5 个 agent 各有独立克隆），已验证可行。

### 方案三：Paperclip Execution Workspace

| 维度 | 评估 |
|------|------|
| **隔离性** | 理论上完全隔离（基于容器或独立目录） |
| **磁盘占用** | 取决于底层实现，通常较高（每 workspace 完整环境） |
| **同步机制** | 依赖 Paperclip API，非 Git 原生 |
| **分支管理** | 不直接关联 Git 分支，需额外同步步骤 |
| **复杂度** | 高 — 依赖 Paperclip 平台能力，当前 `executionWorkspacePreference` 为 null，无现成 workspace |
| **风险** | 高 — Paperclip 的 execution workspace 功能仍在开发/试验阶段；设置复杂；若平台不可用则全盘阻塞 |

**结论：** 暂不推荐。Paperclip execution workspace 功能尚未成熟，当前基础设施不具备条件。

## 推荐方案：独立目录（完整克隆）

### 架构概览

```
/home/shingle2302/worktrees/
├── spec-kit-exam-system.git/          # 共享裸仓库（可选，用于 reference clone 优化）
├── workspace-helper.sh                # 工作区管理脚本（已就位）
├── README.md                          # 使用说明文档（已就位）
├── architectureengineer/
│   └── exam-system/                   # 完整克隆，独立 .git
├── seniorengineer/
│   └── exam-system/
├── frontendengineer/
│   └── exam-system/
├── devopsengineer/
│   └── exam-system/
└── qaengineer/
    └── exam-system/
```

### 核心原则

1. **每个 agent 拥有独立的 Git 克隆** — 文件操作完全隔离，互不影响
2. **所有克隆共享同一个 GitHub remote (origin)** — 通过 `git push/pull` 同步变更
3. **不允许直接跨 agent 文件操作** — 必须通过 Git 提交来传递代码变更

### 工作流

#### 日常开发

```bash
# 在你的工作目录中工作
cd /home/shingle2302/worktrees/<agent-name>/exam-system/

# 拉取最新变更
git pull origin <branch-name>

# 编写代码、提交、推送
git add -A
git commit -m "feat: 具体变更描述"
git push origin <branch-name>
```

#### 跨 Agent 协作

1. 完成当前任务并提交推送
2. 在 issue 评论中通知目标 agent，包含 commit SHA 和变更摘要
3. 目标 agent 执行 `git pull` 获取最新代码

#### 同步工作区

```bash
bash /home/shingle2302/worktrees/workspace-helper.sh sync
```

### 优化措施

为降低磁盘占用，可采用 `--reference` 克隆方式，使各克隆共享 objects：

```bash
# 创建裸仓库作为引用
git clone --bare https://github.com/shingle2302/spec-kit-exam-system.git \
  /home/shingle2302/worktrees/spec-kit-exam-system.git

# 以此创建各 agent 的工作克隆
git clone --reference /home/shingle2302/worktrees/spec-kit-exam-system.git \
  /home/shingle2302/worktrees/spec-kit-exam-system.git \
  /home/shingle2302/worktrees/<agent-name>/exam-system
```

> **注意：** `--reference` 会使新克隆依赖引用仓库的存在。如果引用仓库被删除，克隆将不可用。该优化为可选项，不是强制要求。

### Agent 工作目录映射

| Agent | 工作目录 | AGENTS.md 中的 workdir |
|-------|---------|----------------------|
| ArchitectureEngineer | `/home/shingle2302/worktrees/architectureengineer/exam-system/` | 已配置 |
| SeniorEngineer | `/home/shingle2302/worktrees/seniorengineer/exam-system/` | 已配置 |
| FrontendEngineer | `/home/shingle2302/worktrees/frontendengineer/exam-system/` | 已配置 |
| DevOpsEngineer | `/home/shingle2302/worktrees/devopsengineer/exam-system/` | 已配置 |
| QAEngineer | `/home/shingle2302/worktrees/qaengineer/exam-system/` | 已配置 |

### 分支策略

- `main` — 稳定分支，仅通过 PR 合并
- `{issue-number}-{feature}`（如 `003-class-subject-mgmt`）— 功能开发分支
- 各 agent 在自己负责的分支上工作
- 合并前通过 PR review，必要时可省略（按团队约定）

## 实施步骤

### 第一步：初始化各 agent 的独立工作区（已完成）

每个 agent 在 worktrees 目录下已有独立克隆。无需额外操作。

### 第二步：更新 AGENTS.md 工作目录配置（已完成）

各 agent 的 AGENTS.md 已配置正确的 `workdir`。已验证。

### 第三步：建立协作规范（本文档）

通过本文档确立规则：
- 禁止跨 agent 直接文件操作
- 所有代码传递必须通过 Git 提交
- 使用 `workspace-helper.sh` 进行状态查看和同步

### 第四步：验证隔离效果

见 [CMP-59](/CMP/issues/CMP-59) — 由 QAEngineer 执行。

## 验证场景

| 场景 | 预期行为 | 验证方法 |
|------|---------|---------|
| 两个 agent 同时修改同一文件 | 各自工作区不受影响，推送时有合并冲突 | 模拟并发修改 |
| Agent A 推送后 Agent B 拉取 | Agent B 可正常获取变更 | `git pull` 测试 |
| 一个 agent 误删工作区 | 其他 agent 不受影响 | `rm -rf` 后重新克隆 |
| 新 agent 加入 | 新增一个独立克隆即可 | 执行 `git clone` |

## 风险与缓解

| 风险 | 概率 | 缓解措施 |
|------|------|---------|
| 克隆间同步延迟导致冲突 | 中 | 提前沟通，先 pull 再 push |
| 磁盘空间随克隆数增长 | 低 | 使用 `--reference` / `alternates` 优化 |
| 新 agent 分支管理混乱 | 低 | 按 issue number 命名分支，保持线性历史 |
| Paperclip 将来需要统一 workspace | 低 | 独立克隆可被 UnionFS / 容器化方案替代，迁移成本低 |

## 决策记录

- **2026-07-11**：选择「独立目录（完整克隆）」方案
- 理由：隔离性完全、复杂度最低、风险最小、易于扩展
- 否决 Git Worktree：虽然磁盘更优但复杂度和风险中等，且不易于未来容器化改造
- 否决 Paperclip Execution Workspace：功能不成熟，依赖平台可用性
