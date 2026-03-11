"use client"

import Navbar from "@/components/layout/Navbar"
import SOSButton from "@/components/layout/SOSButton"
import { DynamicLiveMap } from "@/components/map"
import { motion } from "framer-motion"
import {
  Activity,
  Building2,
  Clock,
  Cross,
  Database,
  MapPin,
  Phone,
  Radio,
  Search,
  Share2,
  ShieldCheck,
  Wifi,
  Zap
} from "lucide-react"
import { useEffect, useState } from "react"

const fadeInUp = {
  hidden: { opacity: 0, y: 20 },
  visible: { opacity: 1, y: 0, transition: { duration: 0.5, ease: "easeOut" as const } },
}

const stagger = {
  hidden: {},
  visible: { transition: { staggerChildren: 0.1 } },
}

const filters = [
  { icon: Building2, label: "Police", active: false },
  { icon: Cross, label: "Hospitals", active: false },
  { icon: ShieldCheck, label: "Safe Zones", active: true },
]

// Initial history point starting near Gulshan Police Station
const initialHistory = [
  { lat: 23.8113, lng: 90.4125, time: "08:34:12", accuracy: "High" },
  { lat: 23.8123, lng: 90.4135, time: "08:34:22", accuracy: "High" },
  { lat: 23.8133, lng: 90.4130, time: "08:34:32", accuracy: "Medium" },
  { lat: 23.8143, lng: 90.4145, time: "08:34:42", accuracy: "High" },
]

export default function TrackingPage() {
  const [pingCount, setPingCount] = useState(initialHistory.length)
  const [activePing, setActivePing] = useState(false)
  const [history, setHistory] = useState(initialHistory)
  const [activeFilters, setActiveFilters] = useState(["Police", "Hospitals", "Safe Zones"])
  const [currentLocation, setCurrentLocation] = useState(initialHistory[initialHistory.length - 1])

  // Simulate location pings moving towards the police station
  useEffect(() => {
    const interval = setInterval(() => {
      setPingCount((p) => p + 1)
      setActivePing(true)

      setHistory((prev) => {
        const last = prev[prev.length - 1]
        // Move slightly north-east
        const newLat = last.lat + 0.0005
        const newLng = last.lng + 0.0002

        const newPing = {
          lat: newLat,
          lng: newLng,
          time: new Date().toLocaleTimeString('en-US', { hour12: false }),
          accuracy: Math.random() > 0.8 ? "Medium" : "High"
        }

        setCurrentLocation(newPing)
        return [...prev, newPing].slice(-15) // Keep last 15 pings visible in sidebar
      })

      setTimeout(() => setActivePing(false), 600)
    }, 3000)
    return () => clearInterval(interval)
  }, [])

  const toggleFilter = (label: string) => {
    setActiveFilters((prev) =>
      prev.includes(label) ? prev.filter((f) => f !== label) : [...prev, label]
    )
  }

  return (
    <div className="min-h-screen flex flex-col bg-bg-light">
      <Navbar />

      <main className="flex-1">
        {/* Page header */}
        <div className="border-b border-slate-200 bg-white">
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-6">
            <div className="flex items-center gap-3 mb-2">
              <div className="h-10 w-10 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center">
                <MapPin className="h-5 w-5" />
              </div>
              <h1 className="text-2xl font-bold text-slate-900">Location Service</h1>
              <span className="rounded-full bg-emerald-50 text-emerald-700 border border-emerald-200 px-3 py-0.5 text-xs font-semibold">
                Online
              </span>
              {activePing && (
                <motion.span
                  initial={{ opacity: 0, scale: 0.5 }}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0, scale: 0.5 }}
                  className="rounded-full bg-blue-100 text-blue-700 px-2.5 py-0.5 text-[10px] font-bold"
                >
                  PING
                </motion.span>
              )}
            </div>
            <p className="text-sm text-slate-500">
              POST /api/v1/location/update • WebSocket Handler — Spring Boot @ Port 8082
            </p>
          </div>
        </div>

        <div className="py-8 px-4">
          <div className="mx-auto max-w-7xl grid lg:grid-cols-3 gap-8">
            {/* Map area */}
            <div className="lg:col-span-2">
              <motion.div
                initial="hidden"
                animate="visible"
                variants={fadeInUp}
                className="relative rounded-3xl overflow-hidden border border-slate-200 bg-white shadow-sm"
              >
                {/* Real interactive map */}
                <DynamicLiveMap
                  currentLocation={currentLocation}
                  pingHistory={history}
                  activeFilters={activeFilters}
                />

                {/* Search overlay & Filters */}
                <div className="absolute top-4 left-4 right-4 max-w-lg z-[1000]">
                  <div className="glass rounded-2xl shadow-md border border-slate-200/50">
                    <div className="flex items-center gap-3 p-3">
                      <Search className="h-5 w-5 text-slate-400 ml-1" />
                      <input
                        type="text"
                        placeholder="Search location or landmark..."
                        className="flex-1 bg-transparent text-sm text-slate-800 placeholder:text-slate-400 outline-none"
                      />
                    </div>
                    <div className="flex gap-2 px-3 pb-3">
                      {filters.map((f) => {
                        const isActive = activeFilters.includes(f.label)
                        return (
                          <button
                            key={f.label}
                            onClick={() => toggleFilter(f.label)}
                            className={`inline-flex items-center gap-1.5 rounded-full px-3 py-1.5 text-xs font-medium transition-all shadow-sm ${isActive
                                ? "bg-slate-900 border border-slate-800 text-white"
                                : "bg-white/80 backdrop-blur-md text-slate-700 border border-white/40 hover:bg-white"
                              }`}
                          >
                            <f.icon className="h-3.5 w-3.5" />
                            {f.label}
                          </button>
                        )
                      })}
                    </div>
                  </div>
                </div>

                {/* Location info panel */}
                <div className="p-5 border-t border-slate-100 bg-white/90 backdrop-blur-lg absolute bottom-0 left-0 right-0 z-[1000] rounded-b-3xl">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className="h-10 w-10 rounded-xl bg-blue-50 flex items-center justify-center text-blue-600">
                        <MapPin className="h-5 w-5" />
                      </div>
                      <div>
                        <p className="text-sm font-bold text-slate-900">Current Tracking Fix</p>
                        <p className="text-xs text-slate-500 font-mono mt-0.5">
                          Lat: {currentLocation.lat.toFixed(5)} • Lng: {currentLocation.lng.toFixed(5)}
                        </p>
                      </div>
                    </div>
                    <div className="flex gap-2">
                      <button className="h-9 w-9 rounded-xl border border-slate-200 bg-white flex items-center justify-center text-slate-500 hover:text-primary transition-colors shadow-sm">
                        <Phone className="h-4 w-4" />
                      </button>
                      <button className="h-9 w-9 rounded-xl border border-slate-200 bg-white flex items-center justify-center text-slate-500 hover:text-primary transition-colors shadow-sm">
                        <Share2 className="h-4 w-4" />
                      </button>
                    </div>
                  </div>
                </div>
              </motion.div>
            </div>

            {/* Right panel */}
            <div className="lg:col-span-1 space-y-6">
              {/* Connection status */}
              <motion.div
                initial="hidden"
                animate="visible"
                variants={fadeInUp}
                className="bg-white rounded-2xl border border-slate-200 p-5"
              >
                <h3 className="text-sm font-bold text-slate-900 mb-4 flex items-center gap-2">
                  <Activity className="h-4 w-4 text-primary" />
                  Connection Status
                </h3>
                <div className="space-y-3">
                  {[
                    { icon: Wifi, label: "WebSocket", status: "Connected", color: "text-emerald-600" },
                    { icon: Radio, label: "Kafka Producer", status: "Active", color: "text-emerald-600" },
                    { icon: Database, label: "Redis Cache", status: "Online", color: "text-emerald-600" },
                  ].map((item) => (
                    <div key={item.label} className="flex items-center justify-between py-2">
                      <div className="flex items-center gap-2 text-sm text-slate-600">
                        <item.icon className="h-4 w-4 text-slate-400" />
                        {item.label}
                      </div>
                      <span className={`text-xs font-semibold ${item.color}`}>{item.status}</span>
                    </div>
                  ))}
                </div>
              </motion.div>

              {/* Ping counter */}
              <motion.div
                initial="hidden"
                animate="visible"
                variants={fadeInUp}
                className="bg-white rounded-2xl border border-slate-200 p-5"
              >
                <h3 className="text-sm font-bold text-slate-900 mb-2 flex items-center gap-2">
                  <Zap className="h-4 w-4 text-amber-500" />
                  Location Pings
                </h3>
                <p className="text-4xl font-extrabold text-slate-900">{pingCount}</p>
                <p className="text-xs text-slate-500 mt-1">Updates sent every 3 seconds</p>
              </motion.div>

              {/* Ping history */}
              <motion.div
                initial="hidden"
                animate="visible"
                variants={fadeInUp}
                className="bg-white rounded-2xl border border-slate-200"
              >
                <div className="px-5 py-4 border-b border-slate-100">
                  <h3 className="text-sm font-bold text-slate-900 flex items-center gap-2">
                    <Clock className="h-4 w-4 text-slate-400" />
                    Recent Pings
                  </h3>
                </div>
                <div className="divide-y divide-slate-50 overflow-y-auto max-h-[300px]">
                  {history.map((ping, i) => (
                    <div key={i} className="px-5 py-3 flex items-center justify-between hover:bg-slate-50 transition-colors">
                      <div>
                        <p className="text-xs font-mono text-slate-700">
                          {ping.lat.toFixed(5)}, {ping.lng.toFixed(5)}
                        </p>
                        <p className="text-[10px] text-slate-400 mt-0.5">{ping.time}</p>
                      </div>
                      <span className={`text-[10px] font-semibold rounded-full px-2 py-0.5 ${ping.accuracy === "High"
                          ? "bg-emerald-50 text-emerald-600 border border-emerald-100"
                          : "bg-amber-50 text-amber-600 border border-amber-100"
                        }`}>
                        {ping.accuracy} Fix
                      </span>
                    </div>
                  ))}
                </div>
              </motion.div>

              {/* API Reference */}
              <motion.div
                initial="hidden"
                animate="visible"
                variants={fadeInUp}
                className="bg-white rounded-2xl border border-slate-200 p-5"
              >
                <div className="flex items-center gap-2 mb-3">
                  <span className="rounded-md bg-emerald-50 text-emerald-700 px-2 py-0.5 text-xs font-bold">POST</span>
                  <code className="text-xs font-medium text-slate-700">/api/v1/location/update</code>
                </div>
                <pre className="text-[11px] bg-slate-50 rounded-lg p-3 text-slate-600 overflow-x-auto">
                  {`{
  "sosId": "uuid",
  "userId": "uuid",
  "lat": 23.8103,
  "lng": 90.4125
}`}
                </pre>
              </motion.div>
            </div>
          </div>
        </div>
      </main>

      <SOSButton />
    </div>
  )
}
