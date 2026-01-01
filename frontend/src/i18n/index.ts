import { createI18n } from 'vue-i18n';

// English translations
const en = {
  // Common
  common: {
    save: 'Save',
    cancel: 'Cancel',
    delete: 'Delete',
    edit: 'Edit',
    add: 'Add',
    search: 'Search',
    reset: 'Reset',
    submit: 'Submit',
    back: 'Back',
    confirm: 'Confirm',
    close: 'Close',
    loading: 'Loading...',
    success: 'Success',
    error: 'Error',
    warning: 'Warning',
    info: 'Info'
  },
  
  // Navigation
  nav: {
    dashboard: 'Dashboard',
    questions: 'Questions',
    tests: 'Tests',
    assignments: 'Assignments',
    results: 'Results',
    errorBook: 'Error Book',
    profile: 'Profile',
    logout: 'Logout',
    login: 'Login',
    register: 'Register'
  },
  
  // Authentication
  auth: {
    username: 'Username',
    email: 'Email',
    password: 'Password',
    confirmPassword: 'Confirm Password',
    login: 'Login',
    register: 'Register',
    forgotPassword: 'Forgot Password?',
    rememberMe: 'Remember Me',
    welcome: 'Welcome',
    signIn: 'Sign In',
    signUp: 'Sign Up'
  },
  
  // Questions
  questions: {
    title: 'Questions Management',
    questionText: 'Question Text',
    questionType: 'Question Type',
    subject: 'Subject',
    grade: 'Grade',
    knowledgePoint: 'Knowledge Point',
    standardExplanation: 'Standard Explanation',
    answerOptions: 'Answer Options',
    optionText: 'Option Text',
    addOption: 'Add Option',
    correctAnswer: 'Correct Answer',
    create: 'Create Question',
    update: 'Update Question',
    deleteConfirm: 'Are you sure you want to delete this question?',
    searchPlaceholder: 'Search questions by text, subject, or grade...'
  },
  
  // Tests
  tests: {
    title: 'Tests Management',
    testName: 'Test Name',
    testDescription: 'Test Description',
    timeLimit: 'Time Limit (minutes)',
    assign: 'Assign Test',
    assignToStudents: 'Assign to Students',
    dueDate: 'Due Date',
    assigned: 'Assigned',
    inProgress: 'In Progress',
    submitted: 'Submitted',
    graded: 'Graded',
    myTests: 'My Tests',
    testTaking: 'Taking Test',
    submitConfirm: 'Are you sure you want to submit the test?'
  },
  
  // Students
  students: {
    title: 'Students',
    firstName: 'First Name',
    lastName: 'Last Name',
    studentId: 'Student ID',
    class: 'Class',
    addStudent: 'Add Student',
    editStudent: 'Edit Student'
  },
  
  // Teachers
  teachers: {
    title: 'Teachers',
    addTeacher: 'Add Teacher',
    editTeacher: 'Edit Teacher'
  },
  
  // Error Book
  errorBook: {
    title: 'Error Book',
    practice: 'Practice',
    mastered: 'Mastered',
    notMastered: 'Not Mastered',
    attempts: 'Attempts',
    correctInARow: 'Correct in a row',
    needPractice: 'Need Practice'
  },
  
  // Messages
  messages: {
    success: {
      save: 'Saved successfully',
      delete: 'Deleted successfully',
      create: 'Created successfully',
      update: 'Updated successfully',
      submit: 'Submitted successfully'
    },
    error: {
      save: 'Failed to save',
      delete: 'Failed to delete',
      create: 'Failed to create',
      update: 'Failed to update',
      load: 'Failed to load data',
      auth: 'Authentication failed'
    }
  }
};

// Chinese translations
const zh = {
  // Common
  common: {
    save: '保存',
    cancel: '取消',
    delete: '删除',
    edit: '编辑',
    add: '添加',
    search: '搜索',
    reset: '重置',
    submit: '提交',
    back: '返回',
    confirm: '确认',
    close: '关闭',
    loading: '加载中...',
    success: '成功',
    error: '错误',
    warning: '警告',
    info: '信息'
  },
  
  // Navigation
  nav: {
    dashboard: '仪表板',
    questions: '题目',
    tests: '测试',
    assignments: '作业',
    results: '结果',
    errorBook: '错题本',
    profile: '个人资料',
    logout: '退出登录',
    login: '登录',
    register: '注册'
  },
  
  // Authentication
  auth: {
    username: '用户名',
    email: '邮箱',
    password: '密码',
    confirmPassword: '确认密码',
    login: '登录',
    register: '注册',
    forgotPassword: '忘记密码？',
    rememberMe: '记住我',
    welcome: '欢迎',
    signIn: '登录',
    signUp: '注册'
  },
  
  // Questions
  questions: {
    title: '题目管理',
    questionText: '题目内容',
    questionType: '题目类型',
    subject: '科目',
    grade: '年级',
    knowledgePoint: '知识点',
    standardExplanation: '标准解析',
    answerOptions: '答案选项',
    optionText: '选项内容',
    addOption: '添加选项',
    correctAnswer: '正确答案',
    create: '创建题目',
    update: '更新题目',
    deleteConfirm: '确定要删除这个题目吗？',
    searchPlaceholder: '通过题目内容、科目或年级搜索...'
  },
  
  // Tests
  tests: {
    title: '测试管理',
    testName: '测试名称',
    testDescription: '测试描述',
    timeLimit: '时间限制（分钟）',
    assign: '分配测试',
    assignToStudents: '分配给学生',
    dueDate: '截止日期',
    assigned: '已分配',
    inProgress: '进行中',
    submitted: '已提交',
    graded: '已评分',
    myTests: '我的测试',
    testTaking: '参加测试',
    submitConfirm: '确定要提交测试吗？'
  },
  
  // Students
  students: {
    title: '学生',
    firstName: '名',
    lastName: '姓',
    studentId: '学号',
    class: '班级',
    addStudent: '添加学生',
    editStudent: '编辑学生'
  },
  
  // Teachers
  teachers: {
    title: '教师',
    addTeacher: '添加教师',
    editTeacher: '编辑教师'
  },
  
  // Error Book
  errorBook: {
    title: '错题本',
    practice: '练习',
    mastered: '已掌握',
    notMastered: '未掌握',
    attempts: '尝试次数',
    correctInARow: '连续正确',
    needPractice: '需要练习'
  },
  
  // Messages
  messages: {
    success: {
      save: '保存成功',
      delete: '删除成功',
      create: '创建成功',
      update: '更新成功',
      submit: '提交成功'
    },
    error: {
      save: '保存失败',
      delete: '删除失败',
      create: '创建失败',
      update: '更新失败',
      load: '加载数据失败',
      auth: '认证失败'
    }
  }
};

const i18n = createI18n({
  legacy: false, // Use Composition API
  locale: 'en', // Default locale
  fallbackLocale: 'en', // Fallback locale
  messages: {
    en,
    zh
  }
});

export default i18n;