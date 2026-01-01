<template>
  <div class="language-switcher">
    <a-dropdown>
      <template #overlay>
        <a-menu @click="handleMenuClick">
          <a-menu-item key="en">
            <span>English</span>
          </a-menu-item>
          <a-menu-item key="zh">
            <span>中文</span>
          </a-menu-item>
        </a-menu>
      </template>
      <a-button type="text">
        {{ currentLanguage }}
        <template #icon>
          <DownOutlined />
        </template>
      </a-button>
    </a-dropdown>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { DownOutlined } from '@ant-design/icons-vue';
import { useI18n } from 'vue-i18n';

const { locale } = useI18n();

// Get current language display text
const currentLanguage = computed(() => {
  return locale.value === 'zh' ? '中文' : 'English';
});

// Handle language change
const handleMenuClick = ({ key }: { key: string }) => {
  locale.value = key;
  // Store the selected language in localStorage
  localStorage.setItem('lang', key);
};

// Initialize language from localStorage or browser preference
const initLanguage = () => {
  const savedLang = localStorage.getItem('lang');
  const browserLang = navigator.language.startsWith('zh') ? 'zh' : 'en';
  locale.value = savedLang || browserLang;
};

// Initialize on component mount
initLanguage();
</script>

<style scoped>
.language-switcher {
  margin-right: 16px;
}
</style>