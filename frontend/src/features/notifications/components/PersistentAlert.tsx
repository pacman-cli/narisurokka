"use client";

import { AlertTriangle, MapPin } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";

export const PersistentAlert = ({ active, onCancel }: { active?: boolean; onCancel?: () => void }) => {
  if (!active) return null;

  return (
    <AnimatePresence>
      <motion.div
        initial={{ opacity: 0, y: -50 }}
        animate={{ opacity: 1, y: 0 }}
        exit={{ opacity: 0, y: -50 }}
        className="fixed top-16 left-0 right-0 z-40 px-4 pointer-events-none"
      >
        <div className="max-w-md mx-auto bg-danger-600 text-white rounded-xl shadow-lg p-4 pointer-events-auto border border-danger-500 overflow-hidden relative">
          <div className="absolute inset-0 bg-[linear-gradient(45deg,transparent_25%,rgba(255,255,255,0.1)_50%,transparent_75%,transparent_100%)] bg-[length:24px_24px] animate-[slide_1s_linear_infinite]" />

          <div className="relative z-10 flex items-start gap-4">
            <div className="p-2 bg-danger-500 rounded-full animate-pulse">
              <AlertTriangle className="w-6 h-6" />
            </div>
            <div className="flex-1">
              <h3 className="font-bold text-lg leading-tight uppercase tracking-wide">
                SOS Active
              </h3>
              <p className="text-danger-100 text-sm mt-1 flex items-center gap-1">
                <MapPin className="w-3.5 h-3.5" /> Broadcasting Live Location
              </p>
            </div>
            {onCancel && (
              <button
                onClick={onCancel}
                className="px-3 py-1.5 bg-white text-danger-600 text-sm font-bold rounded hover:bg-danger-50 transition-colors"
              >
                DISARM
              </button>
            )}
          </div>
        </div>
      </motion.div>
    </AnimatePresence>
  );
};
