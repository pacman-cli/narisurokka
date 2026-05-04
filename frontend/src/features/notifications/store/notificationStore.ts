import { create } from 'zustand';
import { Notification, PriorityLevel } from '../types';

interface NotificationState {
  notifications: Notification[];
  isHistoryOpen: boolean;
  addNotification: (title: string, message: string, priority?: PriorityLevel) => void;
  markAsRead: (id: string) => void;
  markAllAsRead: () => void;
  clearHistory: () => void;
  toggleHistory: () => void;
}

export const useNotificationStore = create<NotificationState>((set) => ({
  notifications: [],
  isHistoryOpen: false,
  addNotification: (title, message, priority = 'info') => set((state) => ({
    notifications: [{
      id: Math.random().toString(36).substring(2, 9),
      title,
      message,
      priority,
      channel: ['in-app', 'toast'],
      read: false,
      createdAt: Date.now(),
    }, ...state.notifications]
  })),
  markAsRead: (id) => set((state) => ({
    notifications: state.notifications.map((n) => n.id === id ? { ...n, read: true } : n)
  })),
  markAllAsRead: () => set((state) => ({
    notifications: state.notifications.map((n) => ({ ...n, read: true }))
  })),
  clearHistory: () => set({ notifications: [] }),
  toggleHistory: () => set((state) => ({ isHistoryOpen: !state.isHistoryOpen })),
}));
