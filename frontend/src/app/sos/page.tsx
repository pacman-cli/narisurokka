"use client"

import Navbar from "@/components/layout/Navbar"
import { AnimatePresence, motion } from "framer-motion"
import {
  AlertTriangle,
  CheckCircle2,
  Database,
  Radio,
  RefreshCw,
  Shield,
  X
} from "lucide-react"
import { useCallback, useEffect, useState } from "react"

const fadeInUp = {
  hidden: { opacity: 0, y: 20 },
  visible: { opacity: 1, y: 0, transition: { duration: 0.5, ease: "easeOut" as const } },
}

const stagger = {
  hidden: {},
  visible: { transition: { staggerChildren: 0.1 } },
}

// Mock SOS cases for the dashboard
const mockCases = [
  {
    id: "sos-001",
    userId: "user-a1b2c3",
    status: "ACTIVE" as const,
    triggeredAt: "2026-02-27T02:30:00Z",
    resolvedAt: null,
    cancelReason: null,
  },
  {
    id: "sos-002",
    userId: "user-d4e5f6",
    status: "RESOLVED" as const,
    triggeredAt: "2026-02-26T18:15:00Z",
    resolvedAt: "2026-02-26T18:45:00Z",
    cancelReason: null,
  },
  {
    id: "sos-003",
    userId: "user-g7h8i9",
    status: "CANCELLED" as const,
    triggeredAt: "2026-02-26T14:00:00Z",
    resolvedAt: "2026-02-26T14:02:00Z",
    cancelReason: "False alarm - situation resolved",
  },
]

const statusStyles = {
  ACTIVE: "bg-red-50 text-red-700 border-red-200",
  RESOLVED: "bg-emerald-50 text-emerald-700 border-emerald-200",
  CANCELLED: "bg-slate-50 text-slate-600 border-slate-200",
}

const statusIcons = {
  ACTIVE: AlertTriangle,
  RESOLVED: CheckCircle2,
  CANCELLED: X,
}

export default function SOSPage() {
  const [view, setView] = useState<"trigger" | "dashboard">("trigger")
  const [countdown, setCountdown] = useState(5)
  const [phase, setPhase] = useState<"countdown" | "active" | "cancelled">("countdown")
  const [events, setEvents] = useState<string[]>([])

  const resetSOS = useCallback(() => {
    setCountdown(5)
    setPhase("countdown")
    setEvents([])
  }, [])

  useEffect(() => {
    if (phase !== "countdown" || countdown <= 0) return
    const timer = setTimeout(() => setCountdown((c) => c - 1), 1000)
    return () => clearTimeout(timer)
  }, [countdown, phase])

  // When countdown reaches 0, transition to active
  useEffect(() => {
    if (phase === "countdown" && countdown === 0) {
      setPhase("active")
      // Simulate event flow
      const e1 = setTimeout(() => setEvents((p) => [...p, "SOS case created in PostgreSQL"]), 500)
      const e2 = setTimeout(() => setEvents((p) => [...p, "Event published to Kafka (sos-event topic)"]), 1200)
      const e3 = setTimeout(() => setEvents((p) => [...p, "Cached in Redis for instant access"]), 1800)
      const e4 = setTimeout(() => setEvents((p) => [...p, "Notification Service consumed event"]), 2500)
      const e5 = setTimeout(() => setEvents((p) => [...p, "Email notification dispatched"]), 3200)
      const e6 = setTimeout(() => setEvents((p) => [...p, "SMS alert sent to emergency contacts"]), 3800)
      const e7 = setTimeout(() => setEvents((p) => [...p, "Push notification delivered"]), 4400)
      return () => { clearTimeout(e1); clearTimeout(e2); clearTimeout(e3); clearTimeout(e4); clearTimeout(e5); clearTimeout(e6); clearTimeout(e7) }
    }
  }, [phase, countdown])

  const cancelSOS = () => setPhase("cancelled")
  const progress = ((5 - countdown) / 5) * 100

  return (
    <div className="min-h-screen flex flex-col bg-bg-light">
      <Navbar />

      <main className="flex-1">
        {/* Page header */}
        <div className="border-b border-slate-200 bg-white">
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-6">
            <div className="flex items-center justify-between">
              <div>
                <div className="flex items-center gap-3 mb-2">
                  <div className="h-10 w-10 rounded-xl bg-red-50 text-red-600 flex items-center justify-center">
                    <Shield className="h-5 w-5" />
                  </div>
                  <h1 className="text-2xl font-bold text-slate-900">SOS Service</h1>
                  <span className="rounded-full bg-emerald-50 text-emerald-700 border border-emerald-200 px-3 py-0.5 text-xs font-semibold">
                    Online
                  </span>
                </div>
                <p className="text-sm text-slate-500">
                  POST /api/v1/sos/trigger • POST /api/v1/sos/cancel — Spring Boot @ Port 8081
                </p>
              </div>
              {/* Tab switch */}
              <div className="flex gap-1 bg-slate-100 rounded-xl p-1">
                <button
                  onClick={() => setView("trigger")}
                  className={`px-4 py-2 rounded-lg text-sm font-medium transition-all ${view === "trigger" ? "bg-white text-slate-900 shadow-sm" : "text-slate-500 hover:text-slate-700"
                    }`}
                >
                  Trigger SOS
                </button>
                <button
                  onClick={() => setView("dashboard")}
                  className={`px-4 py-2 rounded-lg text-sm font-medium transition-all ${view === "dashboard" ? "bg-white text-slate-900 shadow-sm" : "text-slate-500 hover:text-slate-700"
                    }`}
                >
                  Cases Dashboard
                </button>
              </div>
            </div>
          </div>
        </div>

        <AnimatePresence mode="wait">
          {view === "trigger" ? (
            <motion.div
              key="trigger"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="py-8 px-4"
            >
              <div className="mx-auto max-w-5xl grid lg:grid-cols-5 gap-8">
                {/* SOS Card */}
                <div className="lg:col-span-2">
                  <motion.div
                    initial="hidden"
                    animate="visible"
                    variants={fadeInUp}
                    className="bg-white rounded-3xl border border-slate-200 shadow-sm p-8 text-center"
                  >
                    <AnimatePresence mode="wait">
                      {phase === "countdown" && (
                        <motion.div
                          key="cd"
                          initial={{ opacity: 0, scale: 0.9 }}
                          animate={{ opacity: 1, scale: 1 }}
                          exit={{ opacity: 0, scale: 0.9 }}
                        >
                          <motion.div
                            animate={{ scale: [1, 1.08, 1] }}
                            transition={{ repeat: Infinity, duration: 1.5 }}
                            className="mx-auto h-16 w-16 rounded-full bg-red-100 flex items-center justify-center mb-6"
                          >
                            <span className="text-2xl font-black text-red-600">SOS</span>
                          </motion.div>
                          <h2 className="text-xl font-bold text-slate-900 mb-1">Sending SOS Alert</h2>
                          <p className="text-sm text-slate-500 mb-8">
                            Alert dispatches in
                          </p>

                          {/* Countdown circle */}
                          <div className="relative mx-auto h-32 w-32 mb-8">
                            <svg className="h-32 w-32 -rotate-90" viewBox="0 0 120 120">
                              <circle cx="60" cy="60" r="52" fill="none" stroke="#fee2e2" strokeWidth="8" />
                              <circle
                                cx="60"
                                cy="60"
                                r="52"
                                fill="none"
                                stroke="#ef4444"
                                strokeWidth="8"
                                strokeLinecap="round"
                                strokeDasharray={2 * Math.PI * 52}
                                strokeDashoffset={2 * Math.PI * 52 * (1 - progress / 100)}
                                className="transition-all duration-1000 ease-linear"
                              />
                            </svg>
                            <div className="absolute inset-0 flex flex-col items-center justify-center">
                              <span className="text-4xl font-extrabold text-red-600">{countdown}</span>
                              <span className="text-[10px] text-slate-400 uppercase tracking-wider font-medium">Seconds</span>
                            </div>
                          </div>

                          <motion.button
                            whileHover={{ scale: 1.02 }}
                            whileTap={{ scale: 0.98 }}
                            onClick={cancelSOS}
                            className="inline-flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-6 py-3 text-sm font-semibold text-slate-700 hover:bg-slate-50 transition-colors"
                          >
                            <X className="h-4 w-4" />
                            Cancel SOS
                          </motion.button>
                        </motion.div>
                      )}

                      {phase === "active" && (
                        <motion.div
                          key="active"
                          initial={{ opacity: 0, scale: 0.9 }}
                          animate={{ opacity: 1, scale: 1 }}
                          exit={{ opacity: 0, scale: 0.9 }}
                        >
                          <div className="mx-auto h-16 w-16 rounded-full bg-red-500 flex items-center justify-center mb-6 animate-glow">
                            <AlertTriangle className="h-8 w-8 text-white" />
                          </div>
                          <h2 className="text-xl font-bold text-red-600 mb-1">SOS ACTIVE</h2>
                          <p className="text-sm text-slate-500 mb-6">
                            Alert dispatched — all channels notified
                          </p>
                          <motion.button
                            whileHover={{ scale: 1.02 }}
                            whileTap={{ scale: 0.98 }}
                            onClick={cancelSOS}
                            className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-6 py-3 text-sm font-semibold text-white hover:bg-slate-800 transition-colors"
                          >
                            <X className="h-4 w-4" />
                            Cancel & Resolve
                          </motion.button>
                        </motion.div>
                      )}

                      {phase === "cancelled" && (
                        <motion.div
                          key="cancelled"
                          initial={{ opacity: 0, scale: 0.9 }}
                          animate={{ opacity: 1, scale: 1 }}
                        >
                          <div className="mx-auto h-16 w-16 rounded-full bg-emerald-100 flex items-center justify-center mb-6">
                            <CheckCircle2 className="h-8 w-8 text-emerald-600" />
                          </div>
                          <h2 className="text-xl font-bold text-slate-900 mb-1">SOS Cancelled</h2>
                          <p className="text-sm text-slate-500 mb-6">
                            Status updated to CANCELLED in PostgreSQL & Redis
                          </p>
                          <motion.button
                            whileTap={{ scale: 0.98 }}
                            onClick={resetSOS}
                            className="inline-flex items-center gap-2 rounded-xl bg-primary px-6 py-3 text-sm font-semibold text-white shadow-lg shadow-primary/25 hover:bg-primary-dark transition-colors"
                          >
                            <RefreshCw className="h-4 w-4" />
                            Trigger Again
                          </motion.button>
                        </motion.div>
                      )}
                    </AnimatePresence>
                  </motion.div>
                </div>

                {/* Event Stream */}
                <div className="lg:col-span-3">
                  <motion.div
                    initial="hidden"
                    animate="visible"
                    variants={fadeInUp}
                    className="bg-white rounded-3xl border border-slate-200 shadow-sm h-full"
                  >
                    <div className="px-6 py-4 border-b border-slate-100 flex items-center gap-2">
                      <Radio className="h-4 w-4 text-primary" />
                      <h3 className="text-sm font-bold text-slate-900">Live Event Stream</h3>
                      {events.length > 0 && (
                        <span className="ml-auto text-xs text-emerald-600 bg-emerald-50 rounded-full px-2.5 py-0.5 font-medium">
                          {events.length} events
                        </span>
                      )}
                    </div>
                    <div className="p-6 space-y-3 min-h-[300px]">
                      {events.length === 0 && (
                        <div className="flex flex-col items-center justify-center h-[260px] text-slate-400">
                          <Radio className="h-8 w-8 mb-3 opacity-30" />
                          <p className="text-sm font-medium">Waiting for SOS trigger...</p>
                          <p className="text-xs mt-1">Events will appear here in real-time</p>
                        </div>
                      )}
                      {events.map((event, i) => (
                        <motion.div
                          key={i}
                          initial={{ opacity: 0, x: -20 }}
                          animate={{ opacity: 1, x: 0 }}
                          transition={{ duration: 0.3 }}
                          className="flex items-start gap-3"
                        >
                          <div className="mt-1">
                            <div className={`h-2.5 w-2.5 rounded-full ${i < 1 ? "bg-blue-500" : i < 3 ? "bg-amber-500" : "bg-emerald-500"
                              }`} />
                          </div>
                          <div className="flex-1 min-w-0">
                            <p className="text-sm font-medium text-slate-900">{event}</p>
                            <p className="text-[10px] text-slate-400 mt-0.5">
                              {new Date().toLocaleTimeString()} •{" "}
                              {i < 1 ? "sos-service" : i < 3 ? "infrastructure" : "notification-service"}
                            </p>
                          </div>
                          <span className={`shrink-0 rounded-full px-2 py-0.5 text-[10px] font-semibold ${i < 1 ? "bg-blue-50 text-blue-600" : i < 3 ? "bg-amber-50 text-amber-600" : "bg-emerald-50 text-emerald-600"
                            }`}>{i < 1 ? "DB" : i < 3 ? "INFRA" : "NOTIF"}</span>
                        </motion.div>
                      ))}
                    </div>
                  </motion.div>
                </div>
              </div>

              {/* API Reference */}
              <motion.div
                initial="hidden"
                whileInView="visible"
                viewport={{ once: true }}
                variants={stagger}
                className="mx-auto max-w-5xl mt-8 grid md:grid-cols-2 gap-6"
              >
                {[
                  {
                    method: "POST",
                    endpoint: "/api/v1/sos/trigger",
                    body: '{ "userId": "uuid", "lat": 23.81, "lng": 90.41 }',
                    desc: "Triggers SOS → PostgreSQL + Redis + Kafka",
                  },
                  {
                    method: "POST",
                    endpoint: "/api/v1/sos/cancel",
                    body: '{ "userID": "uuid", "reason": "False alarm" }',
                    desc: "Cancels active SOS → Updates DB + clears Redis",
                  },
                ].map((api) => (
                  <motion.div
                    key={api.endpoint}
                    variants={fadeInUp}
                    className="bg-white rounded-2xl border border-slate-200 p-5"
                  >
                    <div className="flex items-center gap-2 mb-2">
                      <span className="rounded-md bg-emerald-50 text-emerald-700 px-2 py-0.5 text-xs font-bold">
                        {api.method}
                      </span>
                      <code className="text-sm font-medium text-slate-900">{api.endpoint}</code>
                    </div>
                    <p className="text-xs text-slate-500 mb-3">{api.desc}</p>
                    <pre className="text-xs bg-slate-50 rounded-lg p-3 text-slate-600 overflow-x-auto">
                      {api.body}
                    </pre>
                  </motion.div>
                ))}
              </motion.div>
            </motion.div>
          ) : (
            /* Dashboard view */
            <motion.div
              key="dashboard"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="py-8 px-4"
            >
              <div className="mx-auto max-w-5xl">
                {/* Stats */}
                <motion.div
                  initial="hidden"
                  animate="visible"
                  variants={stagger}
                  className="grid grid-cols-3 gap-6 mb-8"
                >
                  {[
                    { label: "Active", value: "1", color: "text-red-600 bg-red-50" },
                    { label: "Resolved", value: "1", color: "text-emerald-600 bg-emerald-50" },
                    { label: "Cancelled", value: "1", color: "text-slate-600 bg-slate-50" },
                  ].map((stat) => (
                    <motion.div
                      key={stat.label}
                      variants={fadeInUp}
                      className="bg-white rounded-2xl border border-slate-200 p-6 text-center"
                    >
                      <p className="text-3xl font-extrabold text-slate-900">{stat.value}</p>
                      <p className={`text-sm font-medium mt-1 ${stat.color.split(" ")[0]}`}>{stat.label}</p>
                    </motion.div>
                  ))}
                </motion.div>

                {/* Cases table */}
                <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
                  <div className="px-6 py-4 border-b border-slate-100 flex items-center gap-2">
                    <Database className="h-4 w-4 text-primary" />
                    <h3 className="text-sm font-bold text-slate-900">SOS Cases (PostgreSQL)</h3>
                  </div>
                  <div className="overflow-x-auto">
                    <table className="w-full">
                      <thead>
                        <tr className="border-b border-slate-100">
                          <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Case ID</th>
                          <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">User ID</th>
                          <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Status</th>
                          <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Triggered</th>
                          <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Reason</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-50">
                        {mockCases.map((c) => {
                          const StatusIcon = statusIcons[c.status]
                          return (
                            <tr key={c.id} className="hover:bg-slate-50/50 transition-colors">
                              <td className="px-6 py-4 text-sm font-mono text-slate-600">{c.id}</td>
                              <td className="px-6 py-4 text-sm font-mono text-slate-600">{c.userId}</td>
                              <td className="px-6 py-4">
                                <span className={`inline-flex items-center gap-1.5 rounded-full px-3 py-1 text-xs font-semibold border ${statusStyles[c.status]}`}>
                                  <StatusIcon className="h-3 w-3" />
                                  {c.status}
                                </span>
                              </td>
                              <td className="px-6 py-4 text-sm text-slate-500">
                                {new Date(c.triggeredAt).toLocaleString()}
                              </td>
                              <td className="px-6 py-4 text-sm text-slate-500">
                                {c.cancelReason || "—"}
                              </td>
                            </tr>
                          )
                        })}
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </main>
    </div>
  )
}
