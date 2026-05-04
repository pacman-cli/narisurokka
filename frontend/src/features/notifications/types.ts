export type PriorityLevel = 'info' | 'warning' | 'critical';

export interface Notification {
  id: string;
  title: string;
  message: string;
  priority: PriorityLevel;
  channel: ('in-app' | 'toast' | 'sms' | 'email')[];
  createdAt: number;
  read: boolean;
  persisted?: boolean; // For SOS
}

