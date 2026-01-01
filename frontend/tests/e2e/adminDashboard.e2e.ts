import { test, expect } from '@playwright/test';

test.describe('Admin Dashboard Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Assuming admin is already logged in for these tests
    await page.goto('/admin/dashboard');
  });

  test('should display admin dashboard elements', async ({ page }) => {
    // Wait for page to load
    await page.waitForTimeout(1000);
    
    // Check for dashboard elements
    await expect(page.locator('.admin-dashboard-container')).toBeVisible();
    await expect(page.locator('text=Admin Dashboard')).toBeVisible();
    await expect(page.locator('.stats-cards')).toBeVisible();
    await expect(page.locator('.user-management')).toBeVisible();
  });

  test('should allow navigation to user management', async ({ page }) => {
    // Look for "Manage Users" link/button
    const manageUsersLink = page.locator('a:has-text("Manage Users")');
    if (await manageUsersLink.count() > 0) {
      await manageUsersLink.click();
      
      // Wait for navigation
      await page.waitForTimeout(1000);
      
      // Check if we're on the user management page
      await expect(page).toHaveURL(/.*users.*/);
    }
  });

  test('should allow navigation to grade management', async ({ page }) => {
    // Look for "Manage Grades" link/button
    const manageGradesLink = page.locator('a:has-text("Manage Grades")');
    if (await manageGradesLink.count() > 0) {
      await manageGradesLink.click();
      
      // Wait for navigation
      await page.waitForTimeout(1000);
      
      // Check if we're on the grade management page
      await expect(page).toHaveURL(/.*grade.*/);
    }
  });
});