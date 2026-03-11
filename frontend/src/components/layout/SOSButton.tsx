"use client"

import { motion } from "framer-motion"
import { Siren } from "lucide-react"
import Link from "next/link"

export default function SOSButton() {
  return (
    <Link href="/sos" className="fixed bottom-6 right-6 z-50">
      <motion.div
        whileHover={{ scale: 1.15 }}
        whileTap={{ scale: 0.95 }}
        className="relative flex h-16 w-16 cursor-pointer flex-col items-center justify-center rounded-full bg-accent text-white shadow-2xl shadow-accent/40"
      >
        {/* Pulse ring */}
        <span className="absolute inset-0 rounded-full bg-red-400 opacity-30 animate-pulse-ring" />
        <Siren className="h-6 w-6 mb-0.5" />
        <span className="text-[10px] font-bold tracking-widest">SOS</span>
      </motion.div>
    </Link>
  )
}
