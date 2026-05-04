"use client";

import { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { AlertCircle, AlertTriangle, CheckCircle, Info, X } from "lucide-react";
import { useNotificationStore } from "../store/notificationStore";
import { cn } from "@/lib/utils";
import type { Notification } from "../types";

export const ToastSystem = () => {
  const notifications = useNotificationStore((state) => state.notifications);
  const [toasts, setToasts] = useState<Notification[]>([]);

  useEffect(() => {
    // When a new unread notification comes in, show it as a toast
    const newUnread = notifications.filter(n => !n.read).slice(0, 3);
    setToasts(newUnread);
  }, [notifications]);

  return (
    <div className="fixed top-4 left-1/2 -translate-x-1/2 z-50 flex flex-col gap-2 w-full max-w-sm px-4 pointer-events-none">
      <AnimatePresence mode="popLayout">
        {toasts.map((toast) => (
          <ToastItem key={toast.id} toast={toast} />
        ))}
      </AnimatePresence>
    </div>
  );
};

const ToastItem = ({ toast }: { toast: Notification }) => {
  const markAsRead = useNotificationStore((state) => state.markAsRead);

  // Auto-dismiss after 5s unless critical
  useEffect(() => {
    if (toast.priority === 'critical') return;
    const timer = setTimeout(() => {
      markAsRead(toast.id);
    }, 5000);
    return () => clearTimeout(timer);
  }, [toast, markAsRead]);

  const icons = {
    info: <Info className="w-5 h-5 text-brand-500" />,
    warning: <AlertTriangle className="w-5 h-5 text-warning-500" />,
    critical: <AlertCircle className="w-5 h-5 text-danger-500" />
  };

  const borders = {
    info: "border-brand-200 bg-brand-50/90 dark:bg-brand-900/20",
    warning: "border-warning-200 bg-warning-50/90 dark:bg-warning-900/20",
    critical: "border-danger-500 bg-danger-50 text-danger-900 shadow-danger-500/20 shadow-lg animate-pulse-border"
  };

  return (
    <motion.div
      layout
      initial={{ opacity: 0, y: -20, scale: 0.95 }}
      animate={{ opacity: 1, y: 0, scale: 1 }}
      exit={{ opacity: 0, scale: 0.95, transition: { duration: 0.2 } }}
      className={cn(
        "pointer-events-auto p-4 rounded-xl border backdrop-blur-md flex items-start gap-3 shadow-lg",
        borders[toast.priority]
      )}
    >
      <div className="mt-0.5">{icons[toast.priority]}</div>
      <div className="flex-1 min-w-0">
        <h4 className="text-sm font-bold text-text-main">
          {toast.title}
        </h4>
        <p className="text-sm text-text-muted mt-1 leading-snug">
          {toast.message}
        </p>
      </div>
      <button
        onClick={() => markAsRead(toast.id)}
        className="p-1 text-text-muted hover:text-text-main rounded-md transition-colors"
      >
        <X className="w-4 h-4" />
      </button>
    </motion.div>
  );
};
