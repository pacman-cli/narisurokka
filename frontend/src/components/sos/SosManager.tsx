"use client";

import React, { useState, useEffect, useRef } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { ShieldAlert, X, Activity, CheckCircle, WifiOff, AlertTriangle, Users } from "lucide-react";

type SosState = "IDLE" | "ARMING" | "ACTIVATING" | "ACTIVE" | "CANCELLING" | "ERROR";

interface SosManagerProps {
  onSosStateChange: (isActive: boolean) => void;
}

export default function SosManager({ onSosStateChange }: SosManagerProps) {
  const [sosState, setSosState] = useState<SosState>("IDLE");
  const [progress, setProgress] = useState(0);
  const holdTimerRef = useRef<NodeJS.Timeout | null>(null);

  // Mock tracking states
  const [contactsNotified, setContactsNotified] = useState(0);
  const [networkStatus, setNetworkStatus] = useState<"ok" | "connecting" | "failed">("ok");

  // Start the press-and-hold trigger (3 seconds)
  const startHold = () => {
    if (sosState !== "IDLE") return;
    setSosState("ARMING");
    setProgress(0);

    const startTime = Date.now();
    const duration = 2500; // 2.5 seconds to trigger

    holdTimerRef.current = setInterval(() => {
      const elapsed = Date.now() - startTime;
      const currentProgress = Math.min((elapsed / duration) * 100, 100);
      setProgress(currentProgress);

      if (currentProgress >= 100) {
        clearInterval(holdTimerRef.current!);
        triggerEmergency();
      }
    }, 50);
  };

  // Cancel the press-and-hold
  const stopHold = () => {
    if (sosState === "ARMING") {
      if (holdTimerRef.current) clearInterval(holdTimerRef.current);
      setSosState("IDLE");
      setProgress(0);
    }
  };

  // Trigger -> Activating (simulating Kafka send) -> Active
  const triggerEmergency = () => {
    setSosState("ACTIVATING");
    onSosStateChange(true);
    setProgress(100);

    // Mock network call / Kafka publish delay
    setTimeout(() => {
      // randomly mock a failure for demonstration? No, let's keep it robust, but allow error via a toggle if needed.
      // E.g., simulate success
      setSosState("ACTIVE");
      simulateActiveTracking();
    }, 1500);
  };

  // Simulate notifications sent out
  const simulateActiveTracking = () => {
    let count = 0;
    const interval = setInterval(() => {
      count++;
      setContactsNotified(prev => Math.min(prev + 1, 3)); // Mock 3 contacts
      if (count >= 3) clearInterval(interval);
    }, 2000);
  };

  // Cancel flow (Hold to cancel)
  const [cancelProgress, setCancelProgress] = useState(0);
  const cancelTimerRef = useRef<NodeJS.Timeout | null>(null);

  const startCancelHold = () => {
    if (sosState !== "ACTIVE") return;
    setSosState("CANCELLING");
    setCancelProgress(0);

    const startTime = Date.now();
    const duration = 3000; // 3 secs to cancel

    cancelTimerRef.current = setInterval(() => {
      const elapsed = Date.now() - startTime;
      const currentProgress = Math.min((elapsed / duration) * 100, 100);
      setCancelProgress(currentProgress);

      if (currentProgress >= 100) {
        clearInterval(cancelTimerRef.current!);
        resolveEmergency();
      }
    }, 50);
  };

  const stopCancelHold = () => {
    if (sosState === "CANCELLING") {
      if (cancelTimerRef.current) clearInterval(cancelTimerRef.current);
      setSosState("ACTIVE");
      setCancelProgress(0);
    }
  };

  const resolveEmergency = () => {
    setSosState("IDLE");
    onSosStateChange(false);
    setProgress(0);
    setCancelProgress(0);
    setContactsNotified(0);
  };


  // --- Sub-components for UI ---

  // 1. Floating Action Button (Idle / Arming)
  const renderFloatingButton = () => {
    return (
      <div
        className="fixed bottom-[88px] left-1/2 -translate-x-1/2 z-50 sm:bottom-8 sm:left-auto sm:right-8 sm:translate-x-0"
        style={{ paddingBottom: 'env(safe-area-inset-bottom)' }}
      >
        <div className="relative w-24 h-24 flex items-center justify-center">
          {/* Progress Ring */}
          {(sosState === "ARMING" || sosState === "ACTIVATING") && (
            <svg className="absolute inset-0 w-full h-full -rotate-90 pointer-events-none">
              <circle
                cx="48"
                cy="48"
                r="44"
                stroke="rgba(220,38,38,0.2)" /* danger-600 */
                strokeWidth="4"
                fill="none"
              />
              <motion.circle
                cx="48"
                cy="48"
                r="44"
                stroke="#dc2626"
                strokeWidth="4"
                fill="none"
                initial={{ strokeDasharray: "276", strokeDashoffset: "276" }}
                animate={{ strokeDashoffset: 276 - (276 * progress) / 100 }}
                transition={{ duration: 0.1, ease: "linear" }}
              />
            </svg>
          )}

          <motion.button
            onPointerDown={startHold}
            onPointerUp={stopHold}
            onPointerLeave={stopHold}
            onContextMenu={(e) => e.preventDefault()}
            whileTap={{ scale: 0.9 }}
            className={`w-20 h-20 shadow-[0_8px_30px_rgba(220,38,38,0.4)] flex flex-col items-center justify-center rounded-full transition-colors ${
              sosState === "ARMING" ? "bg-danger-700" : "bg-danger-600"
            } text-white focus:outline-none focus-visible:ring-4 focus-visible:ring-danger-500`}
            aria-label="Hold to Trigger Emergency SOS"
          >
            {sosState === "ACTIVATING" ? (
              <motion.div animate={{ rotate: 360 }} transition={{ repeat: Infinity, duration: 1, ease: "linear" }}>
                <Activity className="w-8 h-8" />
              </motion.div>
            ) : (
              <>
                <ShieldAlert className="w-8 h-8" aria-hidden="true" />
                <span className="text-[10px] font-bold tracking-wider mt-1">HOLD SOS</span>
              </>
            )}
          </motion.button>
        </div>
      </div>
    );
  };

  // 2. Active Screen Overlay (Active / Cancelling)
  const renderActiveScreen = () => {
    return (
      <motion.div
        initial={{ y: "100%", opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        exit={{ y: "100%", opacity: 0 }}
        transition={{ type: "spring", damping: 25, stiffness: 200 }}
        className="fixed inset-0 z-[60] bg-bg-base/95 backdrop-blur-xl flex flex-col justify-between"
      >
        <div className="flex-1 overflow-y-auto px-6 py-12 safe-top">
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-24 h-24 rounded-full bg-danger-500/20 text-danger-600 mb-6 animate-sos-pulse shadow-[0_0_40px_rgba(220,38,38,0.3)]">
              <AlertTriangle className="w-12 h-12" />
            </div>
            <h1 className="text-3xl font-black tracking-tight text-text-main mb-2">SOS ACTIVE</h1>
            <p className="text-text-muted text-base leading-relaxed">
              Do not close the app. We are silently broadcasting your coordinates and recording audio.
            </p>
          </div>

          {/* Cards for Statuses */}
          <div className="space-y-4">
            <div className="card flex items-center justify-between p-4 bg-bg-surface border-border-subtle shadow-sm">
              <div className="flex items-center gap-4">
                <div className="p-3 bg-brand-100 rounded-lg text-brand-600">
                  <Activity className="w-5 h-5 animate-pulse" />
                </div>
                <div>
                  <h3 className="font-bold text-sm">Live Tracking</h3>
                  <p className="text-xs text-text-muted mt-0.5">Location updated 2s ago</p>
                </div>
              </div>
              <span className="text-xs font-bold text-success-600 bg-success-50 px-2.5 py-1 rounded-full">ACTIVE</span>
            </div>

            <div className="card flex items-center justify-between p-4 bg-bg-surface border-border-subtle shadow-sm">
              <div className="flex items-center gap-4">
                <div className="p-3 bg-brand-100 rounded-lg text-brand-600">
                  <Users className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="font-bold text-sm">Trusted Contacts</h3>
                  <p className="text-xs text-text-muted mt-0.5">
                    {contactsNotified === 3 ? "All notified" : `Notifying ${contactsNotified}/3`}
                  </p>
                </div>
              </div>
              {contactsNotified === 3 ? (
                <CheckCircle className="w-5 h-5 text-success-600" />
              ) : (
                <motion.div animate={{ rotate: 360 }} transition={{ repeat: Infinity, duration: 2, ease: "linear" }}>
                  <Activity className="w-5 h-5 text-brand-500" />
                </motion.div>
              )}
            </div>

            {/* Error state injection example */}
            {networkStatus === "failed" && (
               <div className="card flex items-center justify-between p-4 bg-danger-50 border-danger-200">
                 <div className="flex items-center gap-4">
                   <div className="p-3 bg-danger-100 rounded-lg text-danger-700">
                     <WifiOff className="w-5 h-5" />
                   </div>
                   <div>
                     <h3 className="font-bold text-sm text-danger-700">Network Disconnected</h3>
                     <p className="text-xs text-danger-700/80 mt-0.5">Trying SMS failover...</p>
                   </div>
                 </div>
               </div>
            )}
          </div>
        </div>

        {/* Hold to Cancel Footer */}
        <div className="pb-[max(2rem,env(safe-area-inset-bottom))] px-6 pt-4 bg-bg-surface border-t border-border-subtle shadow-[0_-10px_40px_rgba(0,0,0,0.05)]">
          <div className="relative w-full h-16 rounded-2xl overflow-hidden bg-neutral-200 flex items-center justify-center">
             {/* Progress Fill */}
             <motion.div
               className="absolute left-0 top-0 bottom-0 bg-neutral-800"
               initial={{ width: "0%" }}
               animate={{ width: `${cancelProgress}%` }}
               transition={{ duration: 0.05, ease: "linear" }}
             />
             <motion.button
               onPointerDown={startCancelHold}
               onPointerUp={stopCancelHold}
               onPointerLeave={stopCancelHold}
               onContextMenu={(e) => e.preventDefault()}
               className="absolute inset-0 w-full h-full z-10 flex items-center justify-center focus:outline-none"
               aria-label="Hold to cancel emergency"
             >
               <span className={`font-bold transition-colors z-20 ${cancelProgress > 50 ? 'text-white' : 'text-neutral-600'}`}>
                 {sosState === "CANCELLING" ? "HOLDING TO CANCEL..." : "HOLD TO CANCEL SOS"}
               </span>
             </motion.button>
          </div>
          <p className="text-[10px] text-center text-text-muted mt-3 uppercase tracking-widest font-semibold">
            Forces a system-wide safe resolve
          </p>
        </div>
      </motion.div>
    );
  };

  return (
    <>
      {/* Either show the floating armable button or it's hidden because the overlay is taking over */}
      <AnimatePresence>
        {(sosState === "IDLE" || sosState === "ARMING" || sosState === "ACTIVATING") && (
          <motion.div initial={{ opacity: 0, scale: 0.8 }} animate={{ opacity: 1, scale: 1 }} exit={{ opacity: 0, scale: 0.5 }}>
            {renderFloatingButton()}
          </motion.div>
        )}
      </AnimatePresence>

      {/* The full screen modal taking over during the emergency */}
      <AnimatePresence>
        {(sosState === "ACTIVE" || sosState === "CANCELLING") && (
          renderActiveScreen()
        )}
      </AnimatePresence>
    </>
  );
}
