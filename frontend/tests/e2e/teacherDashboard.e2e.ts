import { test, expect } from '@playwright/test';

test.describe('Teacher Dashboard Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Assuming teacher is already logged in for these tests
    await page.goto('/teacher/dashboard');
  });

  test('should display teacher dashboard elements', async ({ page }) => {
    // Wait for page to load
    await page.waitForTimeout(1000);
    
    // Check for dashboard elements
    await expect(page.locator('.teacher-dashboard-container')).toBeVisible();
    await expect(page.locator('text=Teacher Dashboard')).toBeVisible();
    await expect(page.locator('.my-classes')).toBeVisible();
    await expect(page.locator('.assigned-tests')).toBeVisible();
  });

  test('should allow teacher to create a new test', async ({ page }) => {
    // Wait for page to load
    await page.waitForTimeout(1000);
    
    // Look for "Create Test" button
    const createTestButton = page.locator('button:has-text("Create Test")');
    if (await createTestButton.count() > 0) {
      await createTestButton.click();
      
      // Wait for navigation to test creation page
      await page.waitForTimeout(1000);
      
      // Check if we're on the test creation page
      await expect(page).toHaveURL(/.*test.*create.*/);
    }
  });

  test('should allow navigation to question management', async ({ page }) => {
    // Look for "Manage Questions" link/button
    const manageQuestionsLink = page.locator('a:has-text("Manage Questions")');
    if (await manageQuestionsLink.count() > 0) {
      await manageQuestionsLink.click();
      
      // Wait for navigation
      await page.waitForTimeout(1000);
      
      // Check if we're on the question management page
      await expect(page).toHaveURL(/.*question.*/);
    }
  });
});