import { describe, it, expect, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import TeacherDashboard from '../../src/views/Teacher/Dashboard.vue';

describe('TeacherDashboard', () => {
  it('renders correctly with dashboard structure', () => {
    // Arrange
    const wrapper = mount(TeacherDashboard);

    // Assert
    expect(wrapper.exists()).toBe(true);
    expect(wrapper.find('.teacher-dashboard-container').exists()).toBe(true);
  });

  it('should have proper dashboard elements', () => {
    // Arrange
    const wrapper = mount(TeacherDashboard);

    // Assert
    expect(wrapper.find('.dashboard-header').exists()).toBe(true);
    expect(wrapper.find('.dashboard-content').exists()).toBe(true);
    expect(wrapper.find('.my-classes').exists()).toBe(true);
    expect(wrapper.find('.assigned-tests').exists()).toBe(true);
  });

  it('should initialize with default values', () => {
    // Arrange
    const wrapper = mount(TeacherDashboard);

    // Assert
    const vm = wrapper.vm as any;
    expect(vm.loading).toBe(false);
  });
});