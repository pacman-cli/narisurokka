"use client"

import Footer from "@/components/layout/Footer"
import Navbar from "@/components/layout/Navbar"
import SOSButton from "@/components/layout/SOSButton"
import { motion } from "framer-motion"
import {
  Activity,
  ArrowRight,
  Bell,
  ChevronRight,
  Database,
  Globe,
  Lock,
  MapPin,
  Play,
  Radio,
  Server,
  Shield,
  Zap,
} from "lucide-react"
import Link from "next/link"

const fadeInUp = {
  hidden: { opacity: 0, y: 30 },
  visible: { opacity: 1, y: 0, transition: { duration: 0.6, ease: "easeOut" as const } },
}

const stagger = {
  hidden: {},
  visible: { transition: { staggerChildren: 0.15 } },
}

const services = [
  {
    icon: Shield,
    title: "SOS Service",
    description:
      "One-tap emergency SOS triggers with real-time status tracking. Publishes events via Kafka and caches active cases in Redis for instant access.",
    href: "/sos",
    color: "from-red-500 to-rose-600",
    iconBg: "bg-red-50 text-red-600",
    features: ["POST /api/v1/sos/trigger", "POST /api/v1/sos/cancel", "Kafka Events", "Redis Cache"],
    badge: "Spring Boot",
  },
  {
    icon: MapPin,
    title: "Location Service",
    description:
      "Real-time GPS location updates via REST API and WebSocket connections. Tracks emergency location pings tied to active SOS cases.",
    href: "/tracking",
    color: "from-blue-500 to-indigo-600",
    iconBg: "bg-blue-50 text-blue-600",
    features: ["POST /api/v1/location/update", "WebSocket Handler", "Kafka Producer", "Redis Store"],
    badge: "Spring Boot",
  },
  {
    icon: Bell,
    title: "Notification Service",
    description:
      "Multi-channel emergency notifications via Email, SMS, and Push. Consumes SOS Kafka events and dispatches alerts instantly.",
    href: "/notifications",
    color: "from-violet-500 to-purple-600",
    iconBg: "bg-violet-50 text-violet-600",
    features: ["Kafka Consumer", "Email Service", "SMS Service", "Push Notifications"],
    badge: "Spring Boot",
  },
]

const techStack = [
  { icon: Server, label: "Spring Boot 3", desc: "Microservices" },
  { icon: Database, label: "PostgreSQL", desc: "Persistence" },
  { icon: Radio, label: "Apache Kafka", desc: "Event Streaming" },
  { icon: Activity, label: "Redis", desc: "Cache & Sessions" },
]

const stats = [
  { value: "3", label: "Microservices" },
  { value: "< 50ms", label: "Avg Response" },
  { value: "99.9%", label: "Uptime SLA" },
  { value: "24/7", label: "Always Active" },
]

export default function LandingPage() {
  return (
    <div className="min-h-screen flex flex-col">
      <Navbar />

      <main className="flex-grow">
        {/* ═══ HERO ═══ */}
        <section className="relative overflow-hidden pt-20 pb-28 lg:pt-28 lg:pb-36">
          <div className="absolute inset-0 -z-10 bg-grid" />
          <div className="absolute inset-0 -z-10 bg-gradient-to-br from-indigo-50 via-white to-violet-50 opacity-80" />
          <div className="absolute top-0 right-0 -z-10 h-[600px] w-[600px] blob-primary rounded-full translate-x-1/3 -translate-y-1/4" />
          <div className="absolute bottom-0 left-0 -z-10 h-[500px] w-[500px] blob-purple rounded-full -translate-x-1/3 translate-y-1/4" />

          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            <motion.div
              initial="hidden"
              animate="visible"
              variants={stagger}
              className="text-center max-w-4xl mx-auto"
            >
              {/* Badge */}
              <motion.div variants={fadeInUp} className="mb-8">
                <span className="inline-flex items-center rounded-full border border-indigo-200 bg-indigo-50 px-5 py-2 text-sm font-medium text-indigo-700 gap-2">
                  <span className="flex h-2 w-2 rounded-full bg-indigo-600 animate-pulse" />
                  Microservice Architecture — 3 Services Live
                </span>
              </motion.div>

              {/* Headline */}
              <motion.h1
                variants={fadeInUp}
                className="text-5xl font-extrabold tracking-tight sm:text-6xl xl:text-7xl text-slate-900 leading-[1.1]"
              >
                Your Safety,{" "}
                <span className="gradient-text">Our Priority</span>
              </motion.h1>

              {/* Subtitle */}
              <motion.p
                variants={fadeInUp}
                className="mt-6 max-w-2xl mx-auto text-lg text-slate-600 md:text-xl leading-relaxed"
              >
                A microservice-powered platform for women safety — featuring real-time
                SOS alerts, live GPS tracking, and multi-channel emergency notifications,
                all built with Spring Boot, Kafka & Redis.
              </motion.p>

              {/* CTA */}
              <motion.div
                variants={fadeInUp}
                className="mt-10 flex flex-col sm:flex-row justify-center gap-4"
              >
                <Link href="/sos">
                  <motion.div
                    whileHover={{ y: -3, scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                    className="inline-flex h-14 items-center justify-center rounded-2xl bg-primary px-8 text-base font-semibold text-white shadow-xl shadow-primary/25 hover:bg-primary-dark transition-all gap-2 cursor-pointer"
                  >
                    Trigger SOS Demo
                    <ArrowRight className="h-5 w-5" />
                  </motion.div>
                </Link>
                <Link href="/tracking">
                  <motion.div
                    whileHover={{ y: -3, scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                    className="inline-flex h-14 items-center justify-center rounded-2xl border border-slate-200 bg-white px-8 text-base font-semibold text-slate-900 shadow-sm hover:bg-slate-50 transition-all gap-2 cursor-pointer"
                  >
                    <Play className="h-5 w-5 text-primary" />
                    Live Tracking
                  </motion.div>
                </Link>
              </motion.div>

              {/* Trust badges */}
              <motion.div
                variants={fadeInUp}
                className="mt-8 flex items-center justify-center gap-6 text-sm text-slate-500"
              >
                <span className="flex items-center gap-1.5">
                  <Lock className="h-4 w-4 text-emerald-500" />
                  End-to-end encrypted
                </span>
                <span className="hidden sm:flex items-center gap-1.5">
                  <Globe className="h-4 w-4 text-blue-500" />
                  Distributed system
                </span>
                <span className="flex items-center gap-1.5">
                  <Zap className="h-4 w-4 text-amber-500" />
                  Sub-second alerts
                </span>
              </motion.div>
            </motion.div>
          </div>
        </section>

        {/* ═══ SERVICES ═══ */}
        <section className="py-24 bg-white relative overflow-hidden">
          <div className="absolute inset-0 bg-grid opacity-30" />
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 relative">
            <motion.div
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true, amount: 0.3 }}
              variants={fadeInUp}
              className="text-center max-w-3xl mx-auto mb-16"
            >
              <span className="inline-flex items-center rounded-full bg-primary/5 border border-primary/10 px-4 py-1.5 text-xs font-semibold text-primary uppercase tracking-wider mb-4">
                Backend Services
              </span>
              <h2 className="text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">
                Three Microservices. One Mission.
              </h2>
              <p className="mt-4 text-lg text-slate-600">
                Each service is independently deployable, scalable, and communicates
                through Kafka event streaming.
              </p>
            </motion.div>

            <motion.div
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true, amount: 0.2 }}
              variants={stagger}
              className="grid grid-cols-1 md:grid-cols-3 gap-8"
            >
              {services.map((service) => (
                <motion.div
                  key={service.title}
                  variants={fadeInUp}
                  whileHover={{ y: -8, boxShadow: "0 25px 60px -12px rgba(0,0,0,0.12)" }}
                  className="group relative rounded-3xl border border-slate-200 bg-white p-8 transition-all overflow-hidden"
                >
                  {/* Gradient top stripe */}
                  <div className={`absolute top-0 left-0 right-0 h-1 bg-gradient-to-r ${service.color}`} />

                  {/* Badge */}
                  <div className="flex items-center justify-between mb-6">
                    <div className={`h-14 w-14 rounded-2xl flex items-center justify-center ${service.iconBg}`}>
                      <service.icon className="h-7 w-7" />
                    </div>
                    <span className="text-[10px] font-semibold text-slate-400 uppercase tracking-wider bg-slate-50 rounded-full px-3 py-1">
                      {service.badge}
                    </span>
                  </div>

                  <h3 className="text-xl font-bold text-slate-900 mb-3">{service.title}</h3>
                  <p className="text-sm text-slate-600 leading-relaxed mb-6">
                    {service.description}
                  </p>

                  {/* API badges */}
                  <div className="flex flex-wrap gap-2 mb-6">
                    {service.features.map((feat) => (
                      <span
                        key={feat}
                        className="inline-flex items-center rounded-lg bg-slate-50 px-2.5 py-1 text-[11px] font-medium text-slate-600 border border-slate-100"
                      >
                        {feat}
                      </span>
                    ))}
                  </div>

                  <Link
                    href={service.href}
                    className="inline-flex items-center text-sm font-semibold text-primary group-hover:translate-x-1 transition-transform"
                  >
                    Explore Service
                    <ChevronRight className="ml-1 h-4 w-4" />
                  </Link>
                </motion.div>
              ))}
            </motion.div>
          </div>
        </section>

        {/* ═══ ARCHITECTURE ═══ */}
        <section className="py-24 bg-slate-50/80 border-y border-slate-200">
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            <motion.div
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true }}
              variants={fadeInUp}
              className="text-center max-w-3xl mx-auto mb-16"
            >
              <span className="inline-flex items-center rounded-full bg-primary/5 border border-primary/10 px-4 py-1.5 text-xs font-semibold text-primary uppercase tracking-wider mb-4">
                System Design
              </span>
              <h2 className="text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">
                Event-Driven Architecture
              </h2>
              <p className="mt-4 text-lg text-slate-600">
                Built on top of Apache Kafka for reliable event streaming between services.
              </p>
            </motion.div>

            {/* Architecture diagram */}
            <motion.div
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true, amount: 0.3 }}
              variants={fadeInUp}
              className="max-w-4xl mx-auto"
            >
              <div className="bg-white rounded-3xl border border-slate-200 p-8 sm:p-12 shadow-sm">
                {/* Flow diagram */}
                <div className="grid grid-cols-1 sm:grid-cols-5 gap-6 items-center">
                  {/* SOS Service */}
                  <div className="text-center">
                    <div className="mx-auto h-16 w-16 rounded-2xl bg-gradient-to-br from-red-500 to-rose-600 flex items-center justify-center text-white shadow-lg shadow-red-500/25">
                      <Shield className="h-8 w-8" />
                    </div>
                    <p className="mt-3 text-sm font-bold text-slate-900">SOS Service</p>
                    <p className="text-xs text-slate-500">Port 8081</p>
                  </div>

                  {/* Arrow */}
                  <div className="hidden sm:flex items-center justify-center">
                    <div className="flex items-center gap-1 text-slate-300">
                      <div className="h-px w-8 bg-slate-300" />
                      <ChevronRight className="h-4 w-4" />
                    </div>
                  </div>

                  {/* Kafka */}
                  <div className="text-center">
                    <div className="mx-auto h-16 w-16 rounded-2xl bg-gradient-to-br from-slate-700 to-slate-900 flex items-center justify-center text-white shadow-lg">
                      <Radio className="h-8 w-8" />
                    </div>
                    <p className="mt-3 text-sm font-bold text-slate-900">Apache Kafka</p>
                    <p className="text-xs text-slate-500">sos-event topic</p>
                  </div>

                  {/* Arrow */}
                  <div className="hidden sm:flex items-center justify-center">
                    <div className="flex items-center gap-1 text-slate-300">
                      <div className="h-px w-8 bg-slate-300" />
                      <ChevronRight className="h-4 w-4" />
                    </div>
                  </div>

                  {/* Notification Service */}
                  <div className="text-center">
                    <div className="mx-auto h-16 w-16 rounded-2xl bg-gradient-to-br from-violet-500 to-purple-600 flex items-center justify-center text-white shadow-lg shadow-violet-500/25">
                      <Bell className="h-8 w-8" />
                    </div>
                    <p className="mt-3 text-sm font-bold text-slate-900">Notification</p>
                    <p className="text-xs text-slate-500">Email/SMS/Push</p>
                  </div>
                </div>

                {/* Location service below */}
                <div className="mt-8 pt-8 border-t border-dashed border-slate-200 text-center">
                  <div className="inline-flex items-center gap-4">
                    <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white shadow-lg shadow-blue-500/25">
                      <MapPin className="h-6 w-6" />
                    </div>
                    <div className="text-left">
                      <p className="text-sm font-bold text-slate-900">Location Service</p>
                      <p className="text-xs text-slate-500">
                        WebSocket + REST — real-time GPS pings tied to active SOS cases
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </motion.div>

            {/* Tech stack */}
            <motion.div
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true }}
              variants={stagger}
              className="mt-16 grid grid-cols-2 md:grid-cols-4 gap-6"
            >
              {techStack.map((tech) => (
                <motion.div
                  key={tech.label}
                  variants={fadeInUp}
                  className="bg-white rounded-2xl border border-slate-200 p-6 text-center hover:shadow-md transition-all"
                >
                  <div className="mx-auto h-12 w-12 rounded-xl bg-slate-50 flex items-center justify-center text-slate-600 mb-3">
                    <tech.icon className="h-6 w-6" />
                  </div>
                  <p className="text-sm font-bold text-slate-900">{tech.label}</p>
                  <p className="text-xs text-slate-500 mt-1">{tech.desc}</p>
                </motion.div>
              ))}
            </motion.div>
          </div>
        </section>

        {/* ═══ STATS ═══ */}
        <section className="py-16 bg-white">
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            <motion.div
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true }}
              variants={stagger}
              className="grid grid-cols-2 gap-8 md:grid-cols-4 text-center"
            >
              {stats.map((stat) => (
                <motion.div key={stat.label} variants={fadeInUp}>
                  <dt className="text-3xl font-extrabold text-slate-900 md:text-4xl">
                    {stat.value}
                  </dt>
                  <dd className="mt-1 text-sm font-medium text-slate-500">{stat.label}</dd>
                </motion.div>
              ))}
            </motion.div>
          </div>
        </section>

        {/* ═══ CTA ═══ */}
        <section className="py-24 relative overflow-hidden">
          <div className="absolute inset-0 bg-gradient-to-br from-primary via-indigo-600 to-violet-700" />
          <div className="absolute inset-0">
            <div className="absolute -top-1/2 -left-[10%] w-[500px] h-[500px] rounded-full bg-white/10 blur-3xl" />
            <div className="absolute -bottom-1/2 -right-[10%] w-[500px] h-[500px] rounded-full bg-purple-500/20 blur-3xl" />
          </div>
          <motion.div
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true }}
            variants={fadeInUp}
            className="relative max-w-4xl mx-auto px-4 text-center"
          >
            <h2 className="text-3xl font-bold tracking-tight text-white sm:text-4xl">
              Ready to explore the SOS system?
            </h2>
            <p className="mt-4 text-lg text-blue-100 max-w-2xl mx-auto">
              Try the SOS trigger flow, watch real-time location updates, and see how
              notifications are dispatched — all from the browser.
            </p>
            <div className="mt-10 flex flex-col sm:flex-row justify-center gap-4">
              <Link href="/sos">
                <motion.div
                  whileHover={{ y: -2, scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                  className="flex items-center justify-center gap-3 rounded-2xl bg-white px-8 py-4 text-base font-bold text-slate-900 shadow-lg hover:bg-slate-50 transition-colors cursor-pointer"
                >
                  <Shield className="h-5 w-5 text-red-500" />
                  Try SOS Demo
                </motion.div>
              </Link>
              <Link href="/tracking">
                <motion.div
                  whileHover={{ y: -2, scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                  className="flex items-center justify-center gap-3 rounded-2xl bg-white/10 border border-white/20 px-8 py-4 text-base font-bold text-white shadow-lg hover:bg-white/20 transition-colors backdrop-blur-sm cursor-pointer"
                >
                  <MapPin className="h-5 w-5" />
                  Open Live Map
                </motion.div>
              </Link>
            </div>
          </motion.div>
        </section>
      </main>

      <Footer />
      <SOSButton />
    </div>
  )
}
