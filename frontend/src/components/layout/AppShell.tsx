"use client";

import { AlertTriangle, Home, MapPin, Settings, User, Wifi, WifiOff, FileEdit, Bell } from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useState, useEffect } from "react";
import SosManager from "@/components/sos/SosManager";
import { useNotificationStore } from "@/features/notifications/store/notificationStore";
import { NotificationPanel } from "@/features/notifications/components/NotificationPanel";
import { PersistentAlert } from "@/features/notifications/components/PersistentAlert";
import { BottomNav } from "./BottomNav";
import { ToastSystem } from "@/features/notifications/components/ToastSystem";

export default function AppShell({ children }: { children: React.ReactNode }) {
  const pathname = usePathname() || "";

  // Simulated state for demonstration (to be bound to WebSocket/Context)
  const [isOnline, setIsOnline] = useState(true);
  const [sosActive, setSosActive] = useState(false);

  const { toggleHistory, notifications } = useNotificationStore();
  const unreadCount = notifications.filter(n => !n.read).length;

  // Exclude auth routes from app shell overlay
  if (pathname === "/login" || pathname === "/register") {
    return <main className="min-h-screen bg-bg-base">{children}</main>;
  }

  return (
    <div className={`flex flex-col min-h-[100dvh] bg-bg-base text-text-main transition-colors duration-300 ${sosActive ? "state-emergency" : isOnline ? "state-idle" : "state-offline"}`}>

      {/* 1. TOP STATUS BAR (Real-time connection state) */}
      <header className="sticky top-0 z-40 w-full bg-bg-surface/90 backdrop-blur-md border-b border-border-subtle px-4 py-3 flex items-center justify-between shadow-sm safe-top">
        <div className="flex items-center gap-2">
          <div className={`p-1.5 rounded-full ${isOnline ? "bg-success-50 text-success-600" : "bg-danger-50 text-danger-600"}`}>
            {isOnline ? <Wifi className="w-4 h-4" /> : <WifiOff className="w-4 h-4" />}
          </div>
          <div className="flex flex-col">
            <span className="text-[10px] font-bold uppercase tracking-wider text-text-muted">System Status</span>
            <span className={`text-sm font-medium ${isOnline ? (sosActive ? "text-danger-600" : "text-success-600") : "text-text-muted"}`}>
              {isOnline ? (sosActive ? "Broadcasting SOS..." : "Encrypted \u2022 Online") : "Offline \u2022 Reconnecting..."}
            </span>
          </div>
        </div>

        <div className="flex items-center gap-3">
          {/* Notification Bell */}
          <button
            onClick={toggleHistory}
            className="p-2 relative text-text-muted hover:text-text-main transition-colors rounded-full hover:bg-bg-base/50 focus-visible:ring-2 focus-visible:ring-brand-500 outline-none"
            aria-label="View notifications"
          >
            <Bell className="w-5 h-5" />
            {unreadCount > 0 && (
              <span className="absolute top-1.5 right-1.5 w-2.5 h-2.5 bg-danger-500 rounded-full border-2 border-bg-surface flex items-center justify-center">
                <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-danger-400 opacity-75"></span>
              </span>
            )}
          </button>

          {/* User context indicator */}
          <button className="w-8 h-8 rounded-full bg-brand-100 flex items-center justify-center text-brand-700 font-bold text-sm ring-2 ring-transparent hover:ring-brand-500 transition-all focus:outline-none focus-visible:ring-brand-500 aria-label='Account Settings'">
            US
          </button>
        </div>
      </header>

      {/* Global Overlays */}
      <NotificationPanel />
      <PersistentAlert />
      <ToastSystem />

      {/* 2. MAIN BODY CONTENT */}
      <main className="flex-1 overflow-y-auto pb-32 pt-4 px-4 container max-w-md mx-auto relative content-container">
        {/* We have removed the hardcoded inline overlay so SosManager can take over natively when active */}
        {children}
      </main>

      {/* 3. SOS ACTION BUTTON & MANAGER */}
      <SosManager onSosStateChange={setSosActive} />

      {/* 4. STICKY BOTTOM NAVIGATION (Thumb reachable, iOS Safe Area Ready) */}
      <BottomNav />
    </div>
  );
}
