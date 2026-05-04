"use client";

import { motion, AnimatePresence } from "framer-motion";
import { formatDistanceToNow } from "date-fns";
import { Bell, CheckCheck, Trash2, X } from "lucide-react";
import { useNotificationStore } from "../store/notificationStore";
import { cn } from "@/lib/utils";
import type { Notification } from "../types";

export const NotificationPanel = () => {
  const { isHistoryOpen, toggleHistory, notifications, markAsRead, markAllAsRead, clearHistory } = useNotificationStore();
  const unreadCount = notifications.filter(n => !n.read).length;

  if (!isHistoryOpen) return null;

  return (
    <AnimatePresence>
      <motion.div
        initial={{ opacity: 0, x: 20 }}
        animate={{ opacity: 1, x: 0 }}
        exit={{ opacity: 0, x: 20 }}
        transition={{ type: "spring", stiffness: 400, damping: 30 }}
        className="fixed inset-y-0 right-0 z-50 w-full sm:w-96 bg-bg-surface border-l border-border-subtle shadow-2xl flex flex-col"
      >
        <div className="flex items-center justify-between p-4 border-b border-border-subtle">
          <div className="flex items-center gap-2">
            <Bell className="w-5 h-5 text-text-main" />
            <h2 className="text-lg font-bold text-text-main">Notifications</h2>
            {unreadCount > 0 && (
              <span className="bg-brand-100 text-brand-700 text-xs font-bold px-2 py-0.5 rounded-full">
                {unreadCount}
              </span>
            )}
          </div>
          <div className="flex items-center gap-2">
            <button
              onClick={markAllAsRead}
              className="p-2 text-text-muted hover:text-text-main hover:bg-bg-base/50 rounded-lg transition-colors duration-200"
              title="Mark all as read"
            >
              <CheckCheck className="w-5 h-5" />
            </button>
            <button
              onClick={clearHistory}
              className="p-2 text-text-muted hover:text-danger-600 hover:bg-danger-50 dark:hover:bg-danger-900/20 rounded-lg transition-colors duration-200"
              title="Clear all"
            >
              <Trash2 className="w-5 h-5" />
            </button>
            <button
              onClick={toggleHistory}
              className="p-2 text-text-muted hover:text-text-main hover:bg-bg-base/50 rounded-lg transition-colors duration-200"
            >
              <X className="w-5 h-5" />
            </button>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto p-4 space-y-3">
          {notifications.length === 0 ? (
            <div className="h-full flex flex-col items-center justify-center text-text-muted space-y-4">
              <Bell className="w-12 h-12 opacity-20" />
              <p className="text-sm font-medium">No system notifications yet</p>
            </div>
          ) : (
            notifications.map((notif) => (
              <NotificationItem key={notif.id} item={notif} onRead={() => markAsRead(notif.id)} />
            ))
          )}
        </div>
      </motion.div>
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        onClick={toggleHistory}
        className="fixed inset-0 z-40 bg-black/20 backdrop-blur-sm sm:hidden"
      />
    </AnimatePresence>
  );
};

const NotificationItem = ({ item, onRead }: { item: Notification; onRead: () => void }) => {
  return (
    <motion.div
      layout
      whileHover={{ scale: 1.01 }}
      whileTap={{ scale: 0.99 }}
      onClick={() => !item.read && onRead()}
      className={cn(
        "p-4 rounded-xl border transition-all duration-200 relative group overflow-hidden cursor-pointer",
        item.read ? "bg-bg-base border-border-subtle opacity-70" : "bg-bg-surface border-brand-200 shadow-sm",
        item.priority === 'critical' && !item.read ? "border-danger-300 bg-danger-50 dark:bg-danger-900/20" : ""
      )}
    >
      <div className="flex items-start justify-between gap-4">
        <div className="flex-1 min-w-0">
          <h4 className={cn("text-sm font-semibold mb-1", item.read ? "text-text-muted" : "text-text-main")}>
            {item.title}
          </h4>
          <p className="text-sm text-text-muted line-clamp-3">
             {item.message}
          </p>
        </div>
        {!item.read && (
          <span className="w-2.5 h-2.5 rounded-full bg-brand-500 shadow-sm flex-shrink-0 mt-1.5" />
        )}
      </div>
      <div className="mt-3 flex items-center justify-between text-xs font-medium text-text-muted">
        <span className="uppercase tracking-wider">{item.priority}</span>
        <span>{formatDistanceToNow(item.createdAt, { addSuffix: true })}</span>
      </div>
    </motion.div>
  );
};

