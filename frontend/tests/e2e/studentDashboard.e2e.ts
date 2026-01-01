import { test, expect } from '@playwright/test';

test.describe('Student Dashboard Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Assuming user is already logged in for these tests
    await page.goto('/student/dashboard');
  });

  test('should display student dashboard elements', async ({ page }) => {
    // Wait for page to load
    await page.waitForTimeout(1000);
    
    // Check for dashboard elements
    await expect(page.locator('.student-dashboard-container')).toBeVisible();
    await expect(page.locator('text=Welcome')).toBeVisible();
    await expect(page.locator('.upcoming-tests')).toBeVisible();
    await expect(page.locator('.recent-grades')).toBeVisible();
  });

  test('should allow student to view assigned tests', async ({ page }) => {
    // Wait for tests to load
    await page.waitForTimeout(1000);
    
    // Look for test list or "No tests" message
    const testList = page.locator('.test-list');
    if (await testList.count() > 0) {
      await expect(testList).toBeVisible();
    } else {
      await expect(page.locator('text=No tests assigned')).toBeVisible();
    }
  });

  test('should allow navigation to test taking page', async ({ page }) => {
    // Find and click on a test (if available)
    const testLink = page.locator('.test-item a').first();
    if (await testLink.count() > 0) {
      const testHref = await testLink.getAttribute('href');
      await testLink.click();
      
      // Wait for navigation
      await page.waitForTimeout(1000);
      
      // Check if we're on a test taking page
      await expect(page).toHaveURL(new RegExp(testHref || ''));
    }
  });
});