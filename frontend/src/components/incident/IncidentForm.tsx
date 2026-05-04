"use client";

import React, { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { MapPin, Mic, Lock, Send, CheckCircle, RefreshCcw, Loader2 } from "lucide-react";

const QUICK_TAGS = [
  "Being Followed",
  "Verbal Harassment",
  "Suspicious Activity",
  "Physical Threat",
  "Medical Issue",
  "Transportation Issue",
];

export default function IncidentForm({ onSuccess }: { onSuccess: () => void }) {
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [details, setDetails] = useState("");
  const [location, setLocation] = useState<string | null>(null);
  const [isLoadingLocation, setIsLoadingLocation] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);

  // Auto-fill location on mount
  useEffect(() => {
    fetchLocation();
  }, []);

  const fetchLocation = () => {
    setIsLoadingLocation(true);
    // Mocking Geolocation API fetch
    setTimeout(() => {
      setLocation("Current location: 23.8103° N, 90.4125° E (Dhaka)");
      setIsLoadingLocation(false);
    }, 1500);
  };

  const toggleTag = (tag: string) => {
    setSelectedTags((prev) =>
      prev.includes(tag) ? prev.filter((t) => t !== tag) : [...prev, tag]
    );
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (selectedTags.length === 0 && !details) return; // Prevent empty submission

    setIsSubmitting(true);

    // Mock API call to Incident Service
    setTimeout(() => {
      setIsSubmitting(false);
      setIsSuccess(true);
      setTimeout(() => {
        onSuccess();
        setIsSuccess(false);
        setSelectedTags([]);
        setDetails("");
      }, 2000); // Reset after showing success state
    }, 1500);
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="card flex flex-col gap-6"
    >
      <div className="flex items-center gap-2 text-brand-600 bg-brand-50 px-3 py-2 rounded-lg">
        <Lock className="w-4 h-4" />
        <span className="text-xs font-bold uppercase tracking-wider">End-to-End Encrypted & Private</span>
      </div>

      <form onSubmit={handleSubmit} className="flex flex-col gap-6">
        {/* Section 1: Quick Tags */}
        <div className="flex flex-col gap-3">
          <label className="text-sm font-bold text-text-main">
            What is happening? <span className="text-danger-600">*</span>
          </label>
          <div className="flex flex-wrap gap-2">
            {QUICK_TAGS.map((tag) => (
              <button
                key={tag}
                type="button"
                onClick={() => toggleTag(tag)}
                className={`px-4 py-2.5 rounded-full text-sm font-medium transition-all min-h-[44px] ${
                  selectedTags.includes(tag)
                    ? "bg-danger-600 text-white shadow-md border border-danger-600"
                    : "bg-bg-base border border-border-subtle text-text-muted hover:border-text-muted"
                }`}
                aria-pressed={selectedTags.includes(tag)}
              >
                {tag}
              </button>
            ))}
          </div>
        </div>

        {/* Section 2: Auto-Location */}
        <div className="flex flex-col gap-3">
          <label className="text-sm font-bold text-text-main">Location</label>
          <div className="flex items-center gap-3 p-3 bg-bg-base border border-border-subtle rounded-xl">
            <div className="p-2 bg-brand-100 rounded-lg text-brand-600 shrink-0">
              <MapPin className="w-5 h-5" />
            </div>
            <div className="flex-1 flex flex-col min-w-0">
              {isLoadingLocation ? (
                <div className="flex items-center gap-2">
                  <Loader2 className="w-4 h-4 animate-spin text-text-muted" />
                  <span className="text-sm text-text-muted">Acquiring accurate location...</span>
                </div>
              ) : (
                <span className="text-sm font-medium text-text-main truncate">{location}</span>
              )}
            </div>
            <button
              type="button"
              onClick={fetchLocation}
              className="p-2 text-text-muted hover:text-brand-600 transition-colors shrink-0"
              aria-label="Refresh Location"
            >
              <RefreshCcw className={`w-5 h-5 ${isLoadingLocation ? "animate-spin" : ""}`} />
            </button>
          </div>
        </div>

        {/* Section 3: Extra Details / Voice */}
        <div className="flex flex-col gap-3">
          <label className="text-sm font-bold text-text-main">Additional Details (Optional)</label>
          <div className="relative">
            <textarea
              className="input-field min-h-[100px] resize-none pr-12"
              placeholder="Type any specific details like descriptions, vehicle numbers, etc."
              value={details}
              onChange={(e) => setDetails(e.target.value)}
            />
            {/* Optional Voice Input Mock */}
            <button
              type="button"
              className="absolute right-2 bottom-2 p-2 rounded-full bg-brand-50 text-brand-600 hover:bg-brand-100 transition-colors"
              aria-label="Record voice note"
            >
              <Mic className="w-5 h-5" />
            </button>
          </div>
        </div>

        {/* Section 4: Submit Actions */}
        <AnimatePresence mode="wait">
          {isSuccess ? (
            <motion.div
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0 }}
              className="flex items-center justify-center gap-3 p-4 bg-success-50 text-success-700 rounded-xl border border-success-200"
            >
              <CheckCircle className="w-6 h-6" />
              <span className="font-bold">Report Submitted Safely</span>
            </motion.div>
          ) : (
            <motion.button
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              type="submit"
              disabled={isSubmitting || (selectedTags.length === 0 && !details)}
              className={`btn-primary w-full flex items-center justify-center gap-2 h-[52px] ${
                isSubmitting || (selectedTags.length === 0 && !details) ? "opacity-50 cursor-not-allowed" : ""
              }`}
            >
              {isSubmitting ? (
                <Loader2 className="w-5 h-5 animate-spin" />
              ) : (
                <>
                  <Send className="w-5 h-5" />
                  <span className="font-bold">Securely Submit Report</span>
                </>
              )}
            </motion.button>
          )}
        </AnimatePresence>
      </form>
    </motion.div>
  );
}

