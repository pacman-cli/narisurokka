"use client"

import Navbar from "@/components/layout/Navbar"
import SOSButton from "@/components/layout/SOSButton"
import { motion } from "framer-motion"
import {
  AlertTriangle,
  Bell,
  CheckCircle2,
  Info,
  Mail,
  MapPin,
  MessageSquare,
  Radio,
  Smartphone,
  Trash2,
  X
} from "lucide-react"
import { useState } from "react"

const fadeInUp = {
  hidden: { opacity: 0, y: 20 },
  visible: { opacity: 1, y: 0, transition: { duration: 0.5, ease: "easeOut" as const } },
}

const stagger = {
  hidden: {},
  visible: { transition: { staggerChildren: 0.08 } },
}

const tabs = ["All", "SOS Alerts", "Location", "System"]

interface Notification {
  id: number
  icon: typeof AlertTriangle
  title: string
  description: string
  time: string
  type: "alert" | "success" | "info" | "warning"
  channel: string[]
  source: string
  group: "today" | "yesterday"
}

const notifications: Notification[] = [
  {
    id: 1,
    icon: AlertTriangle,
    title: "SOS Alert Triggered",
    description: "User user-a1b2c3 triggered SOS at Lat: 23.8103, Lng: 90.4125. Kafka event published to sos-event topic.",
    time: "2 min ago",
    type: "alert",
    channel: ["Email", "SMS", "Push"],
    source: "sos-service → kafka → notification-service",
    group: "today",
  },
  {
    id: 2,
    icon: CheckCircle2,
    title: "SOS Resolved Successfully",
    description: "SOS case sos-002 was resolved. All emergency contacts notified of safe status.",
    time: "1 hour ago",
    type: "success",
    channel: ["Email", "Push"],
    source: "sos-service",
    group: "today",
  },
  {
    id: 3,
    icon: MapPin,
    title: "Location Service Update",
    description: "Location ping received for active SOS. Updated Redis cache and forwarded coordinates via WebSocket.",
    time: "3 hours ago",
    type: "info",
    channel: ["WebSocket"],
    source: "location-service",
    group: "today",
  },
  {
    id: 4,
    icon: X,
    title: "SOS Cancelled",
    description: "User user-g7h8i9 cancelled SOS case sos-003. Reason: False alarm - situation resolved.",
    time: "Yesterday, 2:02 PM",
    type: "warning",
    channel: ["Email", "SMS"],
    source: "sos-service → kafka → notification-service",
    group: "yesterday",
  },
  {
    id: 5,
    icon: Info,
    title: "Kafka Consumer Lag = 0",
    description: "All SOS events processed by notification-service. No consumer lag detected on sos-event topic.",
    time: "Yesterday, 10:00 AM",
    type: "info",
    channel: [],
    source: "notification-service",
    group: "yesterday",
  },
]

const typeStyles = {
  alert: { iconBg: "bg-red-50", iconColor: "text-red-600", border: "border-l-4 border-l-red-500" },
  success: { iconBg: "bg-emerald-50", iconColor: "text-emerald-600", border: "" },
  info: { iconBg: "bg-blue-50", iconColor: "text-blue-600", border: "" },
  warning: { iconBg: "bg-amber-50", iconColor: "text-amber-600", border: "" },
}

const channelIcons: Record<string, typeof Mail> = {
  Email: Mail,
  SMS: MessageSquare,
  Push: Smartphone,
  WebSocket: Radio,
}

export default function NotificationsPage() {
  const [activeTab, setActiveTab] = useState("All")

  return (
    <div className="min-h-screen flex flex-col bg-bg-light">
      <Navbar />

      <main className="flex-1">
        {/* Page header */}
        <div className="border-b border-slate-200 bg-white">
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-6">
            <div className="flex items-center gap-3 mb-2">
              <div className="h-10 w-10 rounded-xl bg-violet-50 text-violet-600 flex items-center justify-center">
                <Bell className="h-5 w-5" />
              </div>
              <h1 className="text-2xl font-bold text-slate-900">Notification Service</h1>
              <span className="rounded-full bg-emerald-50 text-emerald-700 border border-emerald-200 px-3 py-0.5 text-xs font-semibold">
                Online
              </span>
            </div>
            <p className="text-sm text-slate-500">
              Kafka Consumer (sos-event topic) → Email / SMS / Push — Spring Boot @ Port 8083
            </p>
          </div>
        </div>

        <div className="py-8 px-4">
          <div className="mx-auto max-w-5xl">
            {/* Channel stats */}
            <motion.div
              initial="hidden"
              animate="visible"
              variants={stagger}
              className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8"
            >
              {[
                { icon: Mail, label: "Email", count: 12, color: "text-blue-600 bg-blue-50" },
                { icon: MessageSquare, label: "SMS", count: 8, color: "text-emerald-600 bg-emerald-50" },
                { icon: Smartphone, label: "Push", count: 15, color: "text-violet-600 bg-violet-50" },
                { icon: Radio, label: "Kafka Events", count: 35, color: "text-slate-600 bg-slate-50" },
              ].map((stat) => (
                <motion.div
                  key={stat.label}
                  variants={fadeInUp}
                  className="bg-white rounded-2xl border border-slate-200 p-5 hover:shadow-md transition-all"
                >
                  <div className={`h-10 w-10 rounded-xl flex items-center justify-center ${stat.color} mb-3`}>
                    <stat.icon className="h-5 w-5" />
                  </div>
                  <p className="text-2xl font-extrabold text-slate-900">{stat.count}</p>
                  <p className="text-xs text-slate-500 mt-0.5">{stat.label} Sent</p>
                </motion.div>
              ))}
            </motion.div>

            {/* Tabs + clear */}
            <div className="flex items-center justify-between mb-6">
              <div className="flex gap-2 overflow-x-auto pb-1">
                {tabs.map((tab) => (
                  <button
                    key={tab}
                    onClick={() => setActiveTab(tab)}
                    className={`rounded-full px-4 py-2 text-sm font-medium whitespace-nowrap transition-all ${activeTab === tab
                        ? "bg-primary text-white shadow-lg shadow-primary/25"
                        : "bg-white text-slate-600 border border-slate-200 hover:bg-slate-50"
                      }`}
                  >
                    {tab}
                  </button>
                ))}
              </div>
              <button className="text-sm font-medium text-slate-400 hover:text-red-500 transition-colors flex items-center gap-1">
                <Trash2 className="h-3.5 w-3.5" />
                Clear
              </button>
            </div>

            {/* Notification groups */}
            {["today", "yesterday"].map((group) => {
              const groupNotifs = notifications.filter((n) => n.group === group)
              if (groupNotifs.length === 0) return null
              return (
                <div key={group} className="mb-8">
                  <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-3">
                    {group === "today" ? "Today" : "Yesterday"}
                  </h3>
                  <motion.div
                    initial="hidden"
                    animate="visible"
                    variants={stagger}
                    className="space-y-3"
                  >
                    {groupNotifs.map((notif) => {
                      const styles = typeStyles[notif.type]
                      return (
                        <motion.div
                          key={notif.id}
                          variants={fadeInUp}
                          className={`bg-white rounded-2xl border border-slate-200 p-5 hover:shadow-md transition-all ${styles.border}`}
                        >
                          <div className="flex items-start gap-4">
                            <div className={`h-10 w-10 rounded-xl flex items-center justify-center shrink-0 ${styles.iconBg} ${styles.iconColor}`}>
                              <notif.icon className="h-5 w-5" />
                            </div>
                            <div className="flex-1 min-w-0">
                              <div className="flex items-start justify-between gap-2">
                                <h4 className="text-sm font-bold text-slate-900">{notif.title}</h4>
                                <span className="text-xs text-slate-400 whitespace-nowrap shrink-0">{notif.time}</span>
                              </div>
                              <p className="text-sm text-slate-500 mt-1 leading-relaxed">{notif.description}</p>

                              {/* Channel badges + source */}
                              <div className="flex items-center gap-3 mt-3 flex-wrap">
                                {notif.channel.map((ch) => {
                                  const Icon = channelIcons[ch] || Bell
                                  return (
                                    <span
                                      key={ch}
                                      className="inline-flex items-center gap-1 rounded-lg bg-slate-50 border border-slate-100 px-2 py-1 text-[10px] font-medium text-slate-600"
                                    >
                                      <Icon className="h-3 w-3" />
                                      {ch}
                                    </span>
                                  )
                                })}
                                {notif.source && (
                                  <span className="text-[10px] text-slate-400 font-mono">
                                    {notif.source}
                                  </span>
                                )}
                              </div>
                            </div>
                          </div>
                        </motion.div>
                      )
                    })}
                  </motion.div>
                </div>
              )
            })}

            {/* Architecture info */}
            <motion.div
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true }}
              variants={fadeInUp}
              className="bg-gradient-to-br from-violet-600 to-purple-700 rounded-3xl p-8 text-white mt-8"
            >
              <div className="flex items-center gap-3 mb-4">
                <Radio className="h-6 w-6" />
                <h3 className="text-lg font-bold">Event-Driven Architecture</h3>
              </div>
              <p className="text-sm text-violet-100 leading-relaxed mb-6">
                The Notification Service consumes events from the <code className="bg-white/10 rounded px-1.5 py-0.5">sos-event</code> Kafka
                topic. When a SOS is triggered or cancelled, it automatically dispatches notifications via all configured channels.
              </p>
              <div className="grid grid-cols-3 gap-4">
                {[
                  { icon: Mail, label: "EmailNotificationService", desc: "SMTP email alerts" },
                  { icon: MessageSquare, label: "SmsNotificationService", desc: "SMS via gateway" },
                  { icon: Smartphone, label: "PushNotificationService", desc: "FCM push alerts" },
                ].map((svc) => (
                  <div key={svc.label} className="bg-white/10 rounded-2xl p-4 border border-white/10">
                    <svc.icon className="h-6 w-6 mb-2" />
                    <p className="text-xs font-bold">{svc.label}</p>
                    <p className="text-[10px] text-violet-200 mt-0.5">{svc.desc}</p>
                  </div>
                ))}
              </div>
            </motion.div>
          </div>
        </div>
      </main>

      <SOSButton />
    </div>
  )
}
