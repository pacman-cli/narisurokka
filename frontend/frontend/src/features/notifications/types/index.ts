export type NotificationPriority = 'info' | 'warning' | 'critical';
export interface Notification {
  id: string;
  title: string;
  message: string;
  priority: NotificationPriority;
  read: boolean;
  createdAt: Date;
}
