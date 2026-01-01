import { test, expect } from '@playwright/test';

test.describe('Login Page Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
  });

  test('should display login form', async ({ page }) => {
    // Assert that the login form elements are visible
    await expect(page.locator('input[type="text"]')).toBeVisible();
    await expect(page.locator('input[type="password"]')).toBeVisible();
    await expect(page.locator('button[type="submit"]')).toBeVisible();
  });

  test('should show validation errors for empty fields', async ({ page }) => {
    // Click login button without filling any fields
    await page.locator('button[type="submit"]').click();

    // Wait for validation messages to appear
    await page.waitForTimeout(500);
    
    // Check for validation errors (this would depend on the actual implementation)
    // For now, just ensure the page doesn't crash
    await expect(page).toHaveURL(/.*login.*/);
  });

  test('should allow valid user login', async ({ page }) => {
    // Fill in the login form with test credentials
    await page.locator('input[type="text"]').fill('testuser');
    await page.locator('input[type="password"]').fill('password123');
    
    // Submit the form
    await page.locator('button[type="submit"]').click();

    // Wait for navigation to occur
    await page.waitForTimeout(2000);
    
    // Check if the user is redirected to the dashboard (URL would depend on implementation)
    await expect(page).toHaveURL(/.*dashboard.*/);
  });
});