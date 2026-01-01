import { describe, it, expect, vi, beforeEach } from 'vitest';
import { mount } from '@vue/test-utils';
import QuestionManagement from '../../src/components/Question/QuestionManagement.vue';

// Mock the service and any other dependencies
vi.mock('ant-design-vue', async () => {
  const actual = await vi.importActual('ant-design-vue');
  return {
    ...actual,
    message: {
      success: vi.fn(),
      error: vi.fn(),
    },
  };
});

describe('QuestionManagement', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders correctly with initial data', () => {
    // Arrange
    const wrapper = mount(QuestionManagement);

    // Assert
    expect(wrapper.exists()).toBe(true);
    expect(wrapper.find('.question-management-container').exists()).toBe(true);
  });

  it('should have proper structure', () => {
    // Arrange
    const wrapper = mount(QuestionManagement);

    // Assert
    expect(wrapper.find('.question-list').exists()).toBe(true);
    expect(wrapper.find('.question-actions').exists()).toBe(true);
    expect(wrapper.find('.search-bar').exists()).toBe(true);
  });

  it('should initialize with default state values', () => {
    // Arrange
    const wrapper = mount(QuestionManagement);

    // Assert
    const vm = wrapper.vm as any;
    expect(vm.questions).toEqual([]);
    expect(vm.loading).toBe(false);
    expect(vm.pagination.current).toBe(1);
  });
});