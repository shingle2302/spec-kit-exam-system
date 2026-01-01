import { describe, it, expect, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import StudentDashboard from '../../src/views/Student/Dashboard.vue';

describe('StudentDashboard', () => {
  it('renders correctly with dashboard structure', () => {
    // Arrange
    const wrapper = mount(StudentDashboard);

    // Assert
    expect(wrapper.exists()).toBe(true);
    expect(wrapper.find('.student-dashboard-container').exists()).toBe(true);
  });

  it('should have proper dashboard elements', () => {
    // Arrange
    const wrapper = mount(StudentDashboard);

    // Assert
    expect(wrapper.find('.dashboard-header').exists()).toBe(true);
    expect(wrapper.find('.dashboard-content').exists()).toBe(true);
    expect(wrapper.find('.upcoming-tests').exists()).toBe(true);
    expect(wrapper.find('.recent-grades').exists()).toBe(true);
  });

  it('should initialize with default values', () => {
    // Arrange
    const wrapper = mount(StudentDashboard);

    // Assert
    const vm = wrapper.vm as any;
    expect(vm.loading).toBe(false);
  });
});