import { createPinia } from 'pinia';
import { useAuthStore } from './auth';
import { useUserStore } from './user';
import { usePermissionStore } from './permission';

const pinia = createPinia();

export { pinia, useAuthStore, useUserStore, usePermissionStore };

export default pinia;