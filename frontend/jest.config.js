module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1'
  },
  testMatch: [
    '**/__tests__/**/*.(test|spec).ts',
    '**/*.(test|spec).ts'
  ],
  moduleFileExtensions: ['ts', 'js', 'vue']
}
