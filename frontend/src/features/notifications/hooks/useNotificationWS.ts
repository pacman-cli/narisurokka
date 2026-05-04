import { useEffect } from 'react';
import { useNotificationStore } from '../store/notificationStore';
import { notificationWS } from '@/lib/api/ws';
import type { Notification, PriorityLevel } from '../types';

export const useNotificationWS = () => {
  const addNotification = useNotificationStore((state) => state.addNotification);

  useEffect(() => {
    notificationWS.connect();

    const unsubscribe = notificationWS.subscribe((message) => {
      // Assuming standard structured payload from backend
      if (message.type === 'notification' && message.data) {
        addNotification(
          message.data.title,
          message.data.message,
          message.data.priority,
        );
      }
    });

    return () => {
      unsubscribe();
      // Optional: disconnect if we want to drop connection when nothing uses it
      // notificationWS.disconnect();
    };
  }, [addNotification]);
};
